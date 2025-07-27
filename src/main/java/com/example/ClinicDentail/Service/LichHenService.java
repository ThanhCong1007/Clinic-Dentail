package com.example.ClinicDentail.Service;

import com.example.ClinicDentail.DTO.BenhAnDTO;
import com.example.ClinicDentail.DTO.KhamBenhDTO;
import com.example.ClinicDentail.DTO.LichHenDTO;
import com.example.ClinicDentail.Repository.*;
import com.example.ClinicDentail.Enity.*;
import com.example.ClinicDentail.payload.request.AppointmentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class LichHenService {

    private static final Logger logger = LoggerFactory.getLogger(LichHenService.class);

    @Autowired
    private LichHenRepository lichHenRepository;

    @Autowired
    private BenhNhanRepository benhNhanRepository;

    @Autowired
    private BacSiRepository bacSiRepository;

    @Autowired
    private DichVuRepository dichVuRepository;

    @Autowired
    private TrangThaiLichHenRepository trangThaiLichHenRepository;
    @Autowired
    private BenhAnDichVuRepository benhAnDichVuRepository;

    /**
     * Tự động hủy lịch hẹn quá hạn - chạy mỗi 30 phút
     */
//    @Scheduled(cron = "0 0 2 * * *") // 02:00 AM mỗi ngày
    @Scheduled(fixedRate = 30 * 60 * 1000) // Mỗi 30 phút
    @Transactional
    public void tuDongHuyLichHenQuaHan() {
        try {
            logger.info("Bắt đầu kiểm tra và hủy lịch hẹn quá hạn...");

            // Kiểm tra số lượng lịch hẹn quá hạn trước khi hủy
            int soLuongTruocKhiHuy = lichHenRepository.demLichHenQuaHan();

            if (soLuongTruocKhiHuy == 0) {
                logger.debug("Không có lịch hẹn quá hạn nào cần hủy.");
                return;
            }

            // Log danh sách lịch hẹn sẽ bị hủy (optional)
            List<LichHen> danhSachQuaHan = lichHenRepository.findLichHenQuaHan();
            for (LichHen lichHen : danhSachQuaHan) {
                logger.warn("Lịch hẹn quá hạn sẽ bị hủy - ID: {}, Bệnh nhân: {}, Ngày hẹn: {}, Trạng thái hiện tại: {}",
                        lichHen.getMaLichHen(),
                        lichHen.getBenhNhan().getHoTen(),
                        lichHen.getNgayHen(),
                        lichHen.getTrangThai().getTenTrangThai());
            }

            String lyDo = "Tự động hủy do quá hạn khám bệnh";
            int soLuong = lichHenRepository.huyLichHenQuaHan(lyDo);

            logger.info("Đã tự động hủy {} lịch hẹn quá hạn.", soLuong);

        } catch (Exception e) {
            logger.error("Lỗi khi tự động hủy lịch hẹn quá hạn: {}", e.getMessage(), e);
        }
    }

    public LichHenDTO dangKyLichHen(AppointmentRequest appointmentRequest) {
        logger.info("Processing appointment registration for patient ID: {}", appointmentRequest.getMaBenhNhan());

        // Validate patient
        BenhNhan benhNhan = kiemTraBenhNhanTonTai(appointmentRequest.getMaBenhNhan());

        // Validate doctor
        BacSi bacSi = kiemTraBacSiTonTai(appointmentRequest.getMaBacSi());

        // Validate service (optional)
        DichVu dichVu = kiemTraDichVu(appointmentRequest.getMaDichVu());

        // Validate appointment status
        TrangThaiLichHen trangThai = kiemTraTranngThaiLichHen(appointmentRequest.getMaTrangThai());

        // Xác thực thời gian hẹn
        validateAppointmentTime(appointmentRequest.getNgayHen(),
                appointmentRequest.getGioBatDau(),
                appointmentRequest.getGioKetThuc());

        // Kiểm tra xung đột lich hẹn
        checkDoctorAvailability(bacSi.getMaBacSi(),
                appointmentRequest.getNgayHen(),
                appointmentRequest.getGioBatDau(),
                appointmentRequest.getGioKetThuc());

        // Create new appointment
        LichHen lichHen = createAppointment(benhNhan, bacSi, dichVu, trangThai, appointmentRequest);

        boolean lh= lichHenRepository.existsByBenhNhan_MaBenhNhanAndNgayHen(appointmentRequest.getMaBenhNhan(),appointmentRequest.getNgayHen());

        if(!lh) {
            // Save appointment
            LichHen savedLichHen = lichHenRepository.save(lichHen);
            logger.info("Appointment registered successfully for patient ID {}, doctor ID {}, on {} from {} to {}",
                    benhNhan.getMaBenhNhan(), bacSi.getMaBacSi(),
                    appointmentRequest.getNgayHen(), appointmentRequest.getGioBatDau(), appointmentRequest.getGioKetThuc());

            return new LichHenDTO(savedLichHen);
        }else
            throw new RuntimeException("Bạn đã có lịch hẹn vào ngày này!");
    }

    public LichHen taoLichHenMoi(KhamBenhDTO dto,BenhNhan benhNhan) {
        try {
            // Kiểm tra thông tin bắt buộc
            if (dto.getNgayTaiKham() == null || dto.getGioBatDau() == null || dto.getGioKetThuc() == null) {
                throw new RuntimeException("Thiếu thông tin bắt buộc: ngày tái khám, giờ bắt đầu hoặc giờ kết thúc");
            }
            // Tìm bác sĩ
            BacSi bacSi = bacSiRepository.findById(dto.getMaBacSi())
                    .orElseThrow(()-> new RuntimeException("Không tìm thấy bác sĩ với mã: " + dto.getMaBacSi()));

            // Lấy trạng thái mặc định
            TrangThaiLichHen trangThai = trangThaiLichHenRepository.findById(1)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái mặc định"));
            // Xác thực thời gian hẹn
            validateAppointmentTime(dto.getNgayTaiKham(),
                    dto.getGioBatDau(),
                    dto.getGioKetThuc());
            //Kiểm tra xung đột lich hẹn
            checkDoctorAvailability(bacSi.getMaBacSi(),
                    dto.getNgayTaiKham(),
                    dto.getGioBatDau(),
                    dto.getGioKetThuc());
            // Tạo lịch hẹn mới
            LichHen lichHen = new LichHen();
            lichHen.setBenhNhan(benhNhan);
            lichHen.setBacSi(bacSi);
            lichHen.setNgayHen(dto.getNgayTaiKham());
            lichHen.setGioBatDau(dto.getGioBatDau());
            lichHen.setGioKetThuc(dto.getGioKetThuc());
            lichHen.setTrangThai(trangThai);

            // Thiết lập lý do khám
            if (dto.getGhiChu() != null && !dto.getGhiChu().isBlank()) {
                lichHen.setLydo(dto.getGhiChu().trim());
            } else if (dto.getChanDoan() != null && !dto.getChanDoan().isBlank()) {
                lichHen.setLydo("Tái khám: " + dto.getChanDoan().trim());
            } else if (dto.getLyDoKham() != null && !dto.getLyDoKham().isBlank()) {
                lichHen.setLydo(dto.getLyDoKham().trim());
            } else {
                lichHen.setLydo("Lịch hẹn tái khám");
            }

            lichHen.setGhiChu(dto.getGhiChu());

            return lichHenRepository.save(lichHen);
        } catch (Exception e) {
            logger.error("Lỗi khi tạo lịch hẹn mới: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi tạo lịch hẹn mới: " + e.getMessage(), e);
        }
    }

    public List<LichHenDTO> getDanhSachBenhNhan(Integer maBenhNhan) {
        logger.info("Retrieving appointments for patient ID: {}", maBenhNhan);

        // Validate patient exists
        kiemTraBenhNhanTonTai(maBenhNhan);

        List<LichHen> appointments = lichHenRepository.findByBenhNhan_MaBenhNhanOrderByNgayHenDescGioBatDauDesc(maBenhNhan);
        logger.info("Found {} appointments for patient ID {}", appointments.size(), maBenhNhan);

        return appointments.stream()
                .map(LichHenDTO::new)
                .collect(Collectors.toList());
    }

    public LichHenDTO getChiTietLichHen(Integer maLichHen) {
        logger.info("Retrieving appointment details for ID: {}", maLichHen);

        Optional<LichHen> lichHenOpt = lichHenRepository.findById(maLichHen);
        if (!lichHenOpt.isPresent()) {
            logger.warn("Get appointment detail failed: Appointment ID {} not found", maLichHen);
            throw new RuntimeException("Không tìm thấy lịch hẹn với mã: " + maLichHen);
        }

        logger.info("Successfully retrieved details for appointment ID {}", maLichHen);
        return new LichHenDTO(lichHenOpt.get());
    }

    public LichHenDTO capNhapLichHen(Integer maLichHen, AppointmentRequest appointmentRequest) {
        logger.info("Updating appointment ID: {}", maLichHen);

        Optional<LichHen> lichHenOpt = lichHenRepository.findById(maLichHen);
        if (!lichHenOpt.isPresent()) {
            logger.warn("Update appointment failed: Appointment ID {} not found", maLichHen);
            throw new RuntimeException("Không tìm thấy lịch hẹn với mã: " + maLichHen);
        }

        LichHen lichHen = lichHenOpt.get();

        // Validate doctor
        BacSi bacSi = kiemTraBacSiTonTai(appointmentRequest.getMaBacSi());

        // Validate service (optional)
        DichVu dichVu = kiemTraDichVu(appointmentRequest.getMaDichVu());

        // Validate appointment status
        TrangThaiLichHen trangThai = kiemTraTranngThaiLichHen(appointmentRequest.getMaTrangThai());

        // Validate appointment time
        validateAppointmentTime(appointmentRequest.getNgayHen(),
                appointmentRequest.getGioBatDau(),
                appointmentRequest.getGioKetThuc());

        // Check for conflicts (excluding current appointment)
        checkDoctorAvailabilityForUpdate(bacSi.getMaBacSi(), maLichHen,
                appointmentRequest.getNgayHen(),
                appointmentRequest.getGioBatDau(),
                appointmentRequest.getGioKetThuc());

        // Update appointment
        updateAppointmentFields(lichHen, bacSi, dichVu, trangThai, appointmentRequest);

        // Save and return DTO
        LichHen updatedLichHen = lichHenRepository.save(lichHen);
        logger.info("Appointment ID {} updated successfully", maLichHen);

        return new LichHenDTO(updatedLichHen);
    }

    public LichHenDTO huyLichHen(Integer maLichHen, String lyDo) {
        logger.info("Cancelling appointment ID: {}", maLichHen);

        Optional<LichHen> lichHenOpt = lichHenRepository.findById(maLichHen);
        if (!lichHenOpt.isPresent()) {
            logger.warn("Cancel appointment failed: Appointment ID {} not found", maLichHen);
            throw new RuntimeException("Không tìm thấy lịch hẹn với mã: " + maLichHen);
        }

        LichHen lichHen = lichHenOpt.get();

        // Validate cancellation conditions
        validateCancellationConditions(lichHen, maLichHen);

        // Get cancelled status
        Optional<TrangThaiLichHen> trangThaiHuyOpt = trangThaiLichHenRepository.findByTenTrangThai("Đã hủy");
        if (!trangThaiHuyOpt.isPresent()) {
            logger.error("Cancel appointment failed: 'Đã hủy' status not found in database");
            throw new RuntimeException("Lỗi hệ thống: Không tìm thấy trạng thái 'Đã hủy'!");
        }

        // Update status and notes
        lichHen.setTrangThai(trangThaiHuyOpt.get());
//        updateCancellationNotes(lichHen, lyDo);
        lichHen.setLydo(lyDo);
        // Save to database
        LichHen lichHenDaHuy = lichHenRepository.save(lichHen);
        logger.info("Appointment ID {} cancelled successfully", maLichHen);

        return new LichHenDTO(lichHenDaHuy);
    }

    private BenhNhan kiemTraBenhNhanTonTai(Integer maBenhNhan) {
        Optional<BenhNhan> benhNhanOpt = benhNhanRepository.findById(maBenhNhan);
        if (!benhNhanOpt.isPresent()) {
            logger.warn("Patient ID {} not found", maBenhNhan);
            throw new RuntimeException("Lỗi: Không tìm thấy thông tin bệnh nhân!");
        }
        return benhNhanOpt.get();
    }

    private BacSi kiemTraBacSiTonTai(Integer maBacSi) {
        Optional<BacSi> bacSiOpt = bacSiRepository.findById(maBacSi);
        if (!bacSiOpt.isPresent()) {
            logger.warn("Doctor ID {} not found", maBacSi);
            throw new RuntimeException("Lỗi: Không tìm thấy thông tin bác sĩ!");
        }

        BacSi bacSi = bacSiOpt.get();
        if (!bacSi.getTrangThaiLamViec()) {
            logger.warn("Doctor ID {} is not available", maBacSi);
            throw new RuntimeException("Lỗi: Bác sĩ hiện không còn làm việc tại phòng khám!");
        }

        return bacSi;
    }

    private DichVu kiemTraDichVu(Integer maDichVu) {
        if (maDichVu == null) {
            return null;
        }

        Optional<DichVu> dichVuOpt = dichVuRepository.findById(maDichVu);
        if (!dichVuOpt.isPresent()) {
            logger.warn("Service ID {} not found", maDichVu);
            throw new RuntimeException("Lỗi: Không tìm thấy thông tin dịch vụ!");
        }

        DichVu dichVu = dichVuOpt.get();
        if (!dichVu.getTrangThaiHoatDong()) {
            logger.warn("Service ID {} is not active", maDichVu);
            throw new RuntimeException("Lỗi: Dịch vụ hiện không còn hoạt động!");
        }

        return dichVu;
    }

    private TrangThaiLichHen kiemTraTranngThaiLichHen(Integer maTrangThai) {
        Optional<TrangThaiLichHen> trangThaiOpt = trangThaiLichHenRepository.findById(maTrangThai);
        if (!trangThaiOpt.isPresent()) {
            logger.warn("Status ID {} not found", maTrangThai);
            trangThaiOpt = trangThaiLichHenRepository.findByTenTrangThai("Chờ xác nhận");
            if (!trangThaiOpt.isPresent()) {
                logger.error("Default appointment status not found");
                throw new RuntimeException("Lỗi: Không tìm thấy trạng thái lịch hẹn mặc định!");
            }
        }
        return trangThaiOpt.get();
    }

    private void validateAppointmentTime(LocalDate ngayHen, LocalTime gioBatDau, LocalTime gioKetThuc) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        if (ngayHen.isBefore(today)) {
            logger.warn("Appointment date {} is in the past", ngayHen);
            throw new RuntimeException("Lỗi: Ngày hẹn không thể là ngày trong quá khứ!");
        }

        if (ngayHen.equals(today) && gioBatDau.isBefore(now)) {
            logger.warn("Appointment time {} today is in the past (now is {})", gioBatDau, now);
            throw new RuntimeException("Lỗi: Giờ bắt đầu không thể là thời gian trong quá khứ hôm nay!");
        }

        if (gioKetThuc.isBefore(gioBatDau) || gioKetThuc.equals(gioBatDau)) {
            logger.warn("End time {} is not after start time {}", gioKetThuc, gioBatDau);
            throw new RuntimeException("Lỗi: Thời gian kết thúc phải sau thời gian bắt đầu!");
        }
    }

    private void checkDoctorAvailability(Integer maBacSi, LocalDate ngayHen, LocalTime gioBatDau, LocalTime gioKetThuc) {
        boolean isConflict = lichHenRepository.existsByMaBacSiAndNgayHenAndGioBatDauBeforeAndGioKetThucAfter(
                maBacSi, ngayHen, gioKetThuc, gioBatDau);

        if (isConflict) {
            logger.warn("Doctor {} has conflicting appointments on {} between {} and {}",
                    maBacSi, ngayHen, gioBatDau, gioKetThuc);
            throw new RuntimeException("Lỗi: Bác sĩ đã có lịch hẹn khác trong khoảng thời gian này!");
        }
    }

    /**
     * Kiểm tra lịch hẹn của bác sĩ khi cập nhật có bị trùng không (loại trừ chính lịch hiện tại).
     *
     * @param maBacSi     Mã bác sĩ
     * @param maLichHen   Mã lịch hẹn hiện tại
     * @param ngayHen     Ngày hẹn
     * @param gioBatDau   Giờ bắt đầu
     * @param gioKetThuc  Giờ kết thúc
     * @throws RuntimeException nếu có lịch trùng
     */
    private void checkDoctorAvailabilityForUpdate(Integer maBacSi, Integer maLichHen,
                                                  LocalDate ngayHen, LocalTime gioBatDau, LocalTime gioKetThuc) {
        boolean isConflict = lichHenRepository.existsByBacSi_MaBacSiAndMaLichHenNotAndNgayHenAndGioBatDauBeforeAndGioKetThucAfter(
                maBacSi, maLichHen, ngayHen, gioKetThuc, gioBatDau);

        if (isConflict) {
            logger.warn("Doctor {} has conflicting appointments on {} between {} and {} (excluding appointment {})",
                    maBacSi, ngayHen, gioBatDau, gioKetThuc, maLichHen);
            throw new RuntimeException("Lỗi: Bác sĩ đã có lịch hẹn khác trong khoảng thời gian này!");
        }
    }

    /**
     * Tạo đối tượng lịch hẹn mới từ các thực thể đã xác minh và request đầu vào.
     *
     * @param benhNhan   Thực thể bệnh nhân
     * @param bacSi      Thực thể bác sĩ
     * @param dichVu     Thực thể dịch vụ (có thể null)
     * @param trangThai  Trạng thái lịch hẹn
     * @param request    Dữ liệu yêu cầu từ client
     * @return Đối tượng LichHen mới chưa lưu vào DB
     */
    private LichHen createAppointment(BenhNhan benhNhan, BacSi bacSi, DichVu dichVu,
                                      TrangThaiLichHen trangThai, AppointmentRequest request) {
        LichHen lichHen = new LichHen();
        lichHen.setBenhNhan(benhNhan);
        lichHen.setBacSi(bacSi);
        lichHen.setDichVu(dichVu);
        lichHen.setNgayHen(request.getNgayHen());
        lichHen.setGioBatDau(request.getGioBatDau());
        lichHen.setGioKetThuc(request.getGioKetThuc());
        lichHen.setTrangThai(trangThai);
        lichHen.setGhiChu(request.getGhiChu());
        return lichHen;
    }

    /**
     * Cập nhật thông tin lịch hẹn hiện tại từ các thực thể và dữ liệu request.
     *
     * @param lichHen    Lịch hẹn cần cập nhật
     * @param bacSi      Bác sĩ mới
     * @param dichVu     Dịch vụ mới
     * @param trangThai  Trạng thái mới
     * @param request    Dữ liệu mới từ client
     */
    private void updateAppointmentFields(LichHen lichHen, BacSi bacSi, DichVu dichVu,
                                         TrangThaiLichHen trangThai, AppointmentRequest request) {
        lichHen.setBacSi(bacSi);
        lichHen.setDichVu(dichVu);
        lichHen.setNgayHen(request.getNgayHen());
        lichHen.setGioBatDau(request.getGioBatDau());
        lichHen.setGioKetThuc(request.getGioKetThuc());
        lichHen.setTrangThai(trangThai);
        lichHen.setGhiChu(request.getGhiChu());
    }

    /**
     * Kiểm tra điều kiện để được phép hủy lịch hẹn.
     * - Không cho hủy nếu lịch hẹn đã ở quá khứ.
     * - Không cho hủy nếu trạng thái là "Đã hoàn thành" hoặc "Đã hủy".
     *
     * @param lichHen   Lịch hẹn cần kiểm tra
     * @param maLichHen Mã lịch hẹn
     * @throws RuntimeException nếu không hợp lệ để hủy
     */
    private void validateCancellationConditions(LichHen lichHen, Integer maLichHen) {
        LocalDate today = LocalDate.now();
        if (lichHen.getNgayHen().isBefore(today)) {
            logger.warn("Cannot cancel past appointment ID {}", maLichHen);
            throw new RuntimeException("Không thể hủy lịch hẹn đã diễn ra trong quá khứ!");
        }

        String tenTrangThai = lichHen.getTrangThai().getTenTrangThai();
        if ("Đã hoàn thành".equalsIgnoreCase(tenTrangThai) || "Đã hủy".equalsIgnoreCase(tenTrangThai)) {
            logger.warn("Cannot cancel appointment ID {} with status {}", maLichHen, tenTrangThai);
            throw new RuntimeException("Không thể hủy lịch hẹn đã hoàn thành hoặc đã hủy!");
        }
    }

    /**
     * Lấy danh sách lịch hẹn của bác sĩ theo ngày
     */
    public List<LichHenDTO> getAppointmentsByDoctorAndDate(Integer maBacSi, LocalDate date) {
        try {
            List<LichHen> lichHenList = lichHenRepository.findByBacSi_MaBacSiAndNgayHenOrderByGioBatDauAsc(maBacSi, date);
            return lichHenList.stream()
                    .map(LichHenDTO::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy lịch hẹn theo bác sĩ và ngày: " + e.getMessage());
        }
    }

    public LichHen taoLichHenMoi(BenhAn benhAn, BenhAnDTO dto) {
        try {
            if (dto.getDanhSachDichVu() == null || dto.getGioKetThucMoi() == null) {
                throw new RuntimeException("Thiếu thông tin bắt buộc: mã dịch vụ hoặc giờ kết thúc");
            }
            TrangThaiLichHen trangThai = trangThaiLichHenRepository.findById(1)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái mặc định"));
            return lichHenRepository.save(new LichHen(benhAn, dto, trangThai));
        } catch (Exception e) {
            logger.error("Lỗi khi tạo lịch hẹn mới: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi tạo lịch hẹn mới: " + e.getMessage(), e);
        }
    }

    public void capNhatTrangThaiHoanThanh(LichHen lichHen) {
        // Cập nhật trạng thái thành "Hoàn thành" (ma_trang_thai = 4)
        Optional<TrangThaiLichHen> optTrangThai = trangThaiLichHenRepository.findById(4);
        if (optTrangThai.isPresent()) {
            lichHen.setTrangThai(optTrangThai.get());
            lichHenRepository.save(lichHen);
        }
    }
    public LichHen capNhatTrangThaiLichHen(Integer maLichHen) {
        LichHen lichHen = lichHenRepository.findById(maLichHen)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn với mã: " + maLichHen));

        logger.debug("Cập nhật trạng thái lịch hẹn mã: {}, Bác sĩ: {}", maLichHen,
                lichHen.getBacSi().getNguoiDung().getHoTen());

        TrangThaiLichHen trangThai = trangThaiLichHenRepository.findById(3)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái lịch hẹn ID = 3"));

        lichHen.setTrangThai(trangThai);
        return lichHenRepository.save(lichHen);
    }


    public KhamBenhDTO layThongTinLichHen(Integer maLichHen) {
        return lichHenRepository.findById(maLichHen)
                .map(KhamBenhDTO::new)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn với mã: " + maLichHen));
    }
}