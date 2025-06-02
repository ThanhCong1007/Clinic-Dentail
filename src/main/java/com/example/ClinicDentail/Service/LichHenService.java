package com.example.ClinicDentail.Service;

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
import java.time.LocalDateTime;
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
    private BacSiService bacSiService;

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

    /**
     * Kiểm tra thủ công số lượng lịch hẹn quá hạn
     */
    public int kiemTraLichHenQuaHan() {
        return lichHenRepository.demLichHenQuaHan();
    }

    /**
     * Hủy thủ công lịch hẹn quá hạn (nếu cần)
     */
    @Transactional
    public int huyThuCongLichHenQuaHan() {
        String lyDo = "Hủy thủ công do quá hạn khám bệnh";
        int soLuong = lichHenRepository.huyLichHenQuaHan(lyDo);
        logger.info("Đã hủy thủ công {} lịch hẹn quá hạn.", soLuong);
        return soLuong;
    }

    /**
     * Lấy danh sách chi tiết lịch hẹn quá hạn
     */
    public List<LichHen> getDanhSachLichHenQuaHan() {
        return lichHenRepository.findLichHenQuaHan();
    }

    /**
     * Đăng ký lịch hẹn mới
     */
    public LichHenDTO registerAppointment(AppointmentRequest appointmentRequest) {
        logger.info("Processing appointment registration for patient ID: {}", appointmentRequest.getMaBenhNhan());

        // Validate patient
        BenhNhan benhNhan = validatePatient(appointmentRequest.getMaBenhNhan());

        // Validate doctor
        BacSi bacSi = validateDoctor(appointmentRequest.getMaBacSi());

        // Validate service (optional)
        DichVu dichVu = validateService(appointmentRequest.getMaDichVu());

        // Validate appointment status
        TrangThaiLichHen trangThai = validateAppointmentStatus(appointmentRequest.getMaTrangThai());

        // Validate appointment time
        validateAppointmentTime(appointmentRequest.getNgayHen(),
                appointmentRequest.getGioBatDau(),
                appointmentRequest.getGioKetThuc());

        // Check for conflicts
        checkDoctorAvailability(bacSi.getMaBacSi(),
                appointmentRequest.getNgayHen(),
                appointmentRequest.getGioBatDau(),
                appointmentRequest.getGioKetThuc());

        // Create new appointment
        LichHen lichHen = createAppointment(benhNhan, bacSi, dichVu, trangThai, appointmentRequest);

        // Save appointment
        LichHen savedLichHen = lichHenRepository.save(lichHen);
        logger.info("Appointment registered successfully for patient ID {}, doctor ID {}, on {} from {} to {}",
                benhNhan.getMaBenhNhan(), bacSi.getMaBacSi(),
                appointmentRequest.getNgayHen(), appointmentRequest.getGioBatDau(), appointmentRequest.getGioKetThuc());

        return new LichHenDTO(savedLichHen);
    }

    /**
     * Lấy danh sách lịch hẹn theo mã bệnh nhân
     */
    public List<LichHenDTO> getAppointmentsByPatient(Integer maBenhNhan) {
        logger.info("Retrieving appointments for patient ID: {}", maBenhNhan);

        // Validate patient exists
        validatePatient(maBenhNhan);

        List<LichHen> appointments = lichHenRepository.findByBenhNhan_MaBenhNhanOrderByNgayHenDescGioBatDauDesc(maBenhNhan);
        logger.info("Found {} appointments for patient ID {}", appointments.size(), maBenhNhan);

        return appointments.stream()
                .map(LichHenDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Lấy thông tin chi tiết của một lịch hẹn
     */
    public LichHenDTO getAppointmentDetail(Integer maLichHen) {
        logger.info("Retrieving appointment details for ID: {}", maLichHen);

        Optional<LichHen> lichHenOpt = lichHenRepository.findById(maLichHen);
        if (!lichHenOpt.isPresent()) {
            logger.warn("Get appointment detail failed: Appointment ID {} not found", maLichHen);
            throw new RuntimeException("Không tìm thấy lịch hẹn với mã: " + maLichHen);
        }

        logger.info("Successfully retrieved details for appointment ID {}", maLichHen);
        return new LichHenDTO(lichHenOpt.get());
    }

    /**
     * Cập nhật lịch hẹn
     */
    public LichHenDTO updateAppointment(Integer maLichHen, AppointmentRequest appointmentRequest) {
        logger.info("Updating appointment ID: {}", maLichHen);

        Optional<LichHen> lichHenOpt = lichHenRepository.findById(maLichHen);
        if (!lichHenOpt.isPresent()) {
            logger.warn("Update appointment failed: Appointment ID {} not found", maLichHen);
            throw new RuntimeException("Không tìm thấy lịch hẹn với mã: " + maLichHen);
        }

        LichHen lichHen = lichHenOpt.get();

        // Validate doctor
        BacSi bacSi = validateDoctor(appointmentRequest.getMaBacSi());

        // Validate service (optional)
        DichVu dichVu = validateService(appointmentRequest.getMaDichVu());

        // Validate appointment status
        TrangThaiLichHen trangThai = validateAppointmentStatus(appointmentRequest.getMaTrangThai());

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

    /**
     * Hủy lịch hẹn theo mã và lý do cung cấp.
     *
     * @param maLichHen Mã lịch hẹn
     * @param lyDo      Lý do hủy
     * @return Thông tin lịch hẹn đã được cập nhật trạng thái
     * @throws RuntimeException nếu không tìm thấy lịch hẹn, trạng thái "Đã hủy" hoặc không hợp lệ để hủy
     */
    public LichHenDTO cancelAppointment(Integer maLichHen, String lyDo) {
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

    // Private helper methods
    /**
     * Kiểm tra sự tồn tại của bệnh nhân theo mã bệnh nhân.
     *
     * @param maBenhNhan Mã bệnh nhân
     * @return Thực thể BenhNhan tương ứng
     * @throws RuntimeException nếu không tìm thấy bệnh nhân
     */
    private BenhNhan validatePatient(Integer maBenhNhan) {
        Optional<BenhNhan> benhNhanOpt = benhNhanRepository.findById(maBenhNhan);
        if (!benhNhanOpt.isPresent()) {
            logger.warn("Patient ID {} not found", maBenhNhan);
            throw new RuntimeException("Lỗi: Không tìm thấy thông tin bệnh nhân!");
        }
        return benhNhanOpt.get();
    }

    /**
     * Kiểm tra sự tồn tại và tình trạng làm việc của bác sĩ.
     *
     * @param maBacSi Mã bác sĩ
     * @return Thực thể BacSi nếu tồn tại và đang làm việc
     * @throws RuntimeException nếu không tìm thấy hoặc bác sĩ đã nghỉ làm
     */
    private BacSi validateDoctor(Integer maBacSi) {
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

    /**
     * Kiểm tra dịch vụ có tồn tại và còn hoạt động hay không.
     *
     * @param maDichVu Mã dịch vụ
     * @return Thực thể DichVu hoặc null nếu mã là null
     * @throws RuntimeException nếu không tìm thấy hoặc dịch vụ đã ngừng hoạt động
     */
    private DichVu validateService(Integer maDichVu) {
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

    /**
     * Kiểm tra và lấy trạng thái lịch hẹn theo mã.
     * Nếu không tìm thấy, thử lấy trạng thái mặc định "Chờ xác nhận".
     *
     * @param maTrangThai Mã trạng thái lịch hẹn
     * @return Thực thể TrangThaiLichHen
     * @throws RuntimeException nếu không tìm thấy trạng thái hợp lệ
     */
    private TrangThaiLichHen validateAppointmentStatus(Integer maTrangThai) {
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

    /**
     * Kiểm tra ngày và giờ hẹn có hợp lệ không.
     *
     * @param ngayHen     Ngày hẹn
     * @param gioBatDau   Giờ bắt đầu
     * @param gioKetThuc  Giờ kết thúc
     * @throws RuntimeException nếu ngày trong quá khứ hoặc giờ không hợp lệ
     */
    private void validateAppointmentTime(LocalDate ngayHen, LocalTime gioBatDau, LocalTime gioKetThuc) {
        if (ngayHen.isBefore(LocalDate.now())) {
            logger.warn("Appointment date {} is in the past", ngayHen);
            throw new RuntimeException("Lỗi: Ngày hẹn không thể là ngày trong quá khứ!");
        }

        if (gioKetThuc.isBefore(gioBatDau) || gioKetThuc.equals(gioBatDau)) {
            logger.warn("End time {} is not after start time {}", gioKetThuc, gioBatDau);
            throw new RuntimeException("Lỗi: Thời gian kết thúc phải sau thời gian bắt đầu!");
        }
    }

    /**
     * Kiểm tra bác sĩ có bị trùng lịch trong khung giờ được chọn hay không.
     *
     * @param maBacSi     Mã bác sĩ
     * @param ngayHen     Ngày hẹn
     * @param gioBatDau   Giờ bắt đầu
     * @param gioKetThuc  Giờ kết thúc
     * @throws RuntimeException nếu có lịch trùng
     */
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

    /**
     * Đếm số lượng lịch hẹn hôm nay
     */
    public long countTodayAppointments() {
        try {
            LocalDate today = LocalDate.now();
            return lichHenRepository.countByNgayHen(today);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi đếm lịch hẹn hôm nay: " + e.getMessage());
        }
    }
}