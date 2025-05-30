package com.example.ClinicDentail.Service;

import com.example.ClinicDentail.DTO.LichHenDTO;
import com.example.ClinicDentail.Repository.*;
import com.example.ClinicDentail.Enity.*;
import com.example.ClinicDentail.payload.request.AppointmentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

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
     * Hủy lịch hẹn
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

        // Save to database
        LichHen lichHenDaHuy = lichHenRepository.save(lichHen);
        logger.info("Appointment ID {} cancelled successfully", maLichHen);

        return new LichHenDTO(lichHenDaHuy);
    }

    // Private helper methods

    private BenhNhan validatePatient(Integer maBenhNhan) {
        Optional<BenhNhan> benhNhanOpt = benhNhanRepository.findById(maBenhNhan);
        if (!benhNhanOpt.isPresent()) {
            logger.warn("Patient ID {} not found", maBenhNhan);
            throw new RuntimeException("Lỗi: Không tìm thấy thông tin bệnh nhân!");
        }
        return benhNhanOpt.get();
    }

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

    private TrangThaiLichHen validateAppointmentStatus(Integer maTrangThai) {
        Optional<TrangThaiLichHen> trangThaiOpt = trangThaiLichHenRepository.findById(maTrangThai);
        if (!trangThaiOpt.isPresent()) {
            logger.warn("Status ID {} not found", maTrangThai);
            // Try to get default status
            trangThaiOpt = trangThaiLichHenRepository.findByTenTrangThai("Chờ xác nhận");
            if (!trangThaiOpt.isPresent()) {
                logger.error("Default appointment status not found");
                throw new RuntimeException("Lỗi: Không tìm thấy trạng thái lịch hẹn mặc định!");
            }
        }
        return trangThaiOpt.get();
    }

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

    private void checkDoctorAvailability(Integer maBacSi, LocalDate ngayHen, LocalTime gioBatDau, LocalTime gioKetThuc) {
        boolean isConflict = lichHenRepository.existsByMaBacSiAndNgayHenAndGioBatDauBeforeAndGioKetThucAfter(
                maBacSi, ngayHen, gioKetThuc, gioBatDau);

        if (isConflict) {
            logger.warn("Doctor {} has conflicting appointments on {} between {} and {}",
                    maBacSi, ngayHen, gioBatDau, gioKetThuc);
            throw new RuntimeException("Lỗi: Bác sĩ đã có lịch hẹn khác trong khoảng thời gian này!");
        }
    }

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

    private void updateCancellationNotes(LichHen lichHen, String lyDo) {
        if (lyDo != null && !lyDo.trim().isEmpty()) {
            String ghiChuHienTai = lichHen.getGhiChu();
            String ghiChuMoi = (ghiChuHienTai != null && !ghiChuHienTai.isEmpty())
                    ? ghiChuHienTai + "\nLý do hủy: " + lyDo
                    : "Lý do hủy: " + lyDo;
            lichHen.setGhiChu(ghiChuMoi);
        }
    }
}