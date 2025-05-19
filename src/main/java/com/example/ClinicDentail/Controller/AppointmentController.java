package com.example.ClinicDentail.Controller;

import com.example.ClinicDentail.DTO.LichHenDTO;
import com.example.ClinicDentail.Repository.*;
import com.example.ClinicDentail.Enity.*;
import com.example.ClinicDentail.payload.request.AppointmentRequest;
import com.example.ClinicDentail.payload.request.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);

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
     * API đăng ký lịch hẹn mới
     * @param appointmentRequest Thông tin lịch hẹn từ client
     * @return Thông báo kết quả
     */
    @PostMapping("/register")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> registerAppointment(@Valid @RequestBody AppointmentRequest appointmentRequest) {
        try {
            logger.info("Processing appointment registration for patient ID: {}", appointmentRequest.getMaBenhNhan());

            // Kiểm tra bệnh nhân tồn tại
            Optional<BenhNhan> benhNhanOpt = benhNhanRepository.findById(appointmentRequest.getMaBenhNhan());
            if (!benhNhanOpt.isPresent()) {
                logger.warn("Appointment registration failed: Patient ID {} not found", appointmentRequest.getMaBenhNhan());
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Lỗi: Không tìm thấy thông tin bệnh nhân!"));
            }
            BenhNhan benhNhan = benhNhanOpt.get();

            // Kiểm tra bác sĩ tồn tại
            Optional<BacSi> bacSiOpt = bacSiRepository.findById(appointmentRequest.getMaBacSi());
            if (!bacSiOpt.isPresent()) {
                logger.warn("Appointment registration failed: Doctor ID {} not found", appointmentRequest.getMaBacSi());
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Lỗi: Không tìm thấy thông tin bác sĩ!"));
            }
            BacSi bacSi = bacSiOpt.get();

            // Kiểm tra bác sĩ còn làm việc không
            if (!bacSi.getTrangThaiLamViec()) {
                logger.warn("Appointment registration failed: Doctor ID {} is not available", appointmentRequest.getMaBacSi());
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Lỗi: Bác sĩ hiện không còn làm việc tại phòng khám!"));
            }

            // Kiểm tra dịch vụ tồn tại
            Optional<DichVu> dichVuOpt = null;
            DichVu dichVu = null;
            if (appointmentRequest.getMaDichVu() != null) {
                dichVuOpt = dichVuRepository.findById(appointmentRequest.getMaDichVu());
                if (!dichVuOpt.isPresent()) {
                    logger.warn("Appointment registration failed: Service ID {} not found", appointmentRequest.getMaDichVu());
                    return ResponseEntity
                            .badRequest()
                            .body(new MessageResponse("Lỗi: Không tìm thấy thông tin dịch vụ!"));
                }
                dichVu = dichVuOpt.get();

                // Kiểm tra dịch vụ còn hoạt động không
                if (!dichVu.getTrangThaiHoatDong()) {
                    logger.warn("Appointment registration failed: Service ID {} is not active", appointmentRequest.getMaDichVu());
                    return ResponseEntity
                            .badRequest()
                            .body(new MessageResponse("Lỗi: Dịch vụ hiện không còn hoạt động!"));
                }
            }

            // Kiểm tra trạng thái lịch hẹn tồn tại
            Optional<TrangThaiLichHen> trangThaiOpt = trangThaiLichHenRepository.findById(appointmentRequest.getMaTrangThai());
            if (!trangThaiOpt.isPresent()) {
                logger.warn("Appointment registration failed: Status ID {} not found", appointmentRequest.getMaTrangThai());
                // Mặc định trạng thái "Đã đặt" hoặc "Chờ xác nhận" nếu không tìm thấy
                trangThaiOpt = trangThaiLichHenRepository.findByTenTrangThai("Chờ xác nhận");
                if (!trangThaiOpt.isPresent()) {
                    logger.error("Appointment registration failed: Default appointment status not found");
                    return ResponseEntity
                            .badRequest()
                            .body(new MessageResponse("Lỗi: Không tìm thấy trạng thái lịch hẹn mặc định!"));
                }
            }
            TrangThaiLichHen trangThai = trangThaiOpt.get();

            // Kiểm tra thời gian lịch hẹn
            LocalDate ngayHen = appointmentRequest.getNgayHen();
            LocalTime gioBatDau = appointmentRequest.getGioBatDau();
            LocalTime gioKetThuc = appointmentRequest.getGioKetThuc();

            // Kiểm tra ngày hẹn phải là ngày trong tương lai
            if (ngayHen.isBefore(LocalDate.now())) {
                logger.warn("Appointment registration failed: Appointment date {} is in the past", ngayHen);
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Lỗi: Ngày hẹn không thể là ngày trong quá khứ!"));
            }

            // Kiểm tra thời gian kết thúc phải sau thời gian bắt đầu
            if (gioKetThuc.isBefore(gioBatDau) || gioKetThuc.equals(gioBatDau)) {
                logger.warn("Appointment registration failed: End time {} is not after start time {}", gioKetThuc, gioBatDau);
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Lỗi: Thời gian kết thúc phải sau thời gian bắt đầu!"));
            }

            // Kiểm tra lịch trùng của bác sĩ
            boolean isConflict = lichHenRepository.existsByMaBacSiAndNgayHenAndGioBatDauBeforeAndGioKetThucAfter(
                    bacSi.getMaBacSi(), ngayHen, gioKetThuc, gioBatDau);

            if (isConflict) {
                logger.warn("Appointment registration failed: Doctor {} has conflicting appointments on {} between {} and {}",
                        bacSi.getMaBacSi(), ngayHen, gioBatDau, gioKetThuc);
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Lỗi: Bác sĩ đã có lịch hẹn khác trong khoảng thời gian này!"));
            }

            // Tạo lịch hẹn mới
            LichHen lichHen = new LichHen();
            lichHen.setBenhNhan(benhNhan);
            lichHen.setBacSi(bacSi);
            lichHen.setDichVu(dichVu);
            lichHen.setNgayHen(ngayHen);
            lichHen.setGioBatDau(gioBatDau);
            lichHen.setGioKetThuc(gioKetThuc);
            lichHen.setTrangThai(trangThai);
            lichHen.setGhiChu(appointmentRequest.getGhiChu());
            // Ngày tạo sẽ được tự động thiết lập

            // Lưu lịch hẹn vào database
            LichHen savedLichHen = lichHenRepository.save(lichHen);
            logger.info("Appointment registered successfully for patient ID {}, doctor ID {}, on {} from {} to {}",
                    benhNhan.getMaBenhNhan(), bacSi.getMaBacSi(), ngayHen, gioBatDau, gioKetThuc);

            return ResponseEntity.ok(new MessageResponse("Đăng ký lịch hẹn thành công!"));
        } catch (Exception e) {
            logger.error("Appointment registration error: " + e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi đăng ký lịch hẹn: " + e.getMessage()));
        }
    }


    /**
     * API để lấy danh sách lịch hẹn theo mã bệnh nhân
     */
    @GetMapping("/patient/{maBenhNhan}")
    public ResponseEntity<?> getAppointmentsByPatient(@PathVariable Integer maBenhNhan) {
        try {
            logger.info("Retrieving appointments for patient ID: {}", maBenhNhan);

            // Kiểm tra bệnh nhân tồn tại
            Optional<BenhNhan> benhNhanOpt = benhNhanRepository.findById(maBenhNhan);
            if (!benhNhanOpt.isPresent()) {
                logger.warn("Get appointments failed: Patient ID {} not found", maBenhNhan);
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Lỗi: Không tìm thấy thông tin bệnh nhân!"));
            }

            List<LichHen> appointments = lichHenRepository.findByBenhNhan_MaBenhNhanOrderByNgayHenDescGioBatDauDesc(maBenhNhan);
            logger.info("Found {} appointments for patient ID {}", appointments.size(), maBenhNhan);

            // Convert appointments to DTOs to avoid recursive JSON serialization
            List<LichHenDTO> appointmentDTOs = appointments.stream()
                    .map(LichHenDTO::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(appointmentDTOs);
        } catch (Exception e) {
            logger.error("Error retrieving appointments: " + e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi khi lấy danh sách lịch hẹn: " + e.getMessage()));
        }
    }

    /**
     * API để lấy thông tin chi tiết của một lịch hẹn
     */
    @GetMapping("/{maLichHen}")
    public ResponseEntity<?> getAppointmentDetail(@PathVariable Integer maLichHen) {
        try {
            logger.info("Retrieving appointment details for ID: {}", maLichHen);

            Optional<LichHen> lichHenOpt = lichHenRepository.findById(maLichHen);
            if (!lichHenOpt.isPresent()) {
                logger.warn("Get appointment detail failed: Appointment ID {} not found", maLichHen);
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new MessageResponse("Không tìm thấy lịch hẹn với mã: " + maLichHen));
            }

            LichHenDTO lichHenDTO = new LichHenDTO(lichHenOpt.get());
            logger.info("Successfully retrieved details for appointment ID {}", maLichHen);

            return ResponseEntity.ok(lichHenDTO);
        } catch (Exception e) {
            logger.error("Error retrieving appointment detail: " + e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi khi lấy thông tin lịch hẹn: " + e.getMessage()));
        }
    }


    @PutMapping("/{maLichHen}")
    public ResponseEntity<?> updateAppointment(@PathVariable Integer maLichHen,
                                               @Valid @RequestBody AppointmentRequest appointmentRequest) {
        try {
            logger.info("Updating appointment ID: {}", maLichHen);

            Optional<LichHen> lichHenOpt = lichHenRepository.findById(maLichHen);
            if (!lichHenOpt.isPresent()) {
                logger.warn("Update appointment failed: Appointment ID {} not found", maLichHen);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MessageResponse("Không tìm thấy lịch hẹn với mã: " + maLichHen));
            }

            LichHen lichHen = lichHenOpt.get();

            // Bác sĩ
            Optional<BacSi> bacSiOpt = bacSiRepository.findById(appointmentRequest.getMaBacSi());
            if (!bacSiOpt.isPresent()) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Lỗi: Không tìm thấy thông tin bác sĩ!"));
            }
            BacSi bacSi = bacSiOpt.get();
            if (!bacSi.getTrangThaiLamViec()) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Lỗi: Bác sĩ hiện không còn làm việc tại phòng khám!"));
            }

            // Dịch vụ
            DichVu dichVu = null;
            if (appointmentRequest.getMaDichVu() != null) {
                Optional<DichVu> dichVuOpt = dichVuRepository.findById(appointmentRequest.getMaDichVu());
                if (!dichVuOpt.isPresent()) {
                    return ResponseEntity.badRequest()
                            .body(new MessageResponse("Lỗi: Không tìm thấy thông tin dịch vụ!"));
                }
                dichVu = dichVuOpt.get();
                if (!dichVu.getTrangThaiHoatDong()) {
                    return ResponseEntity.badRequest()
                            .body(new MessageResponse("Lỗi: Dịch vụ hiện không còn hoạt động!"));
                }
            }

            // Trạng thái
            Optional<TrangThaiLichHen> trangThaiOpt = trangThaiLichHenRepository.findById(appointmentRequest.getMaTrangThai());
            if (!trangThaiOpt.isPresent()) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Lỗi: Không tìm thấy trạng thái lịch hẹn!"));
            }
            TrangThaiLichHen trangThai = trangThaiOpt.get();

            // Ngày & thời gian
            LocalDate ngayHen = appointmentRequest.getNgayHen();
            LocalTime gioBatDau = appointmentRequest.getGioBatDau();
            LocalTime gioKetThuc = appointmentRequest.getGioKetThuc();

            if (ngayHen.isBefore(LocalDate.now())) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Lỗi: Ngày hẹn không thể là ngày trong quá khứ!"));
            }

            if (!gioKetThuc.isAfter(gioBatDau)) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Lỗi: Thời gian kết thúc phải sau thời gian bắt đầu!"));
            }

            // Kiểm tra trùng lịch
            boolean isConflict = lichHenRepository.existsByBacSi_MaBacSiAndMaLichHenNotAndNgayHenAndGioBatDauBeforeAndGioKetThucAfter(
                    bacSi.getMaBacSi(), maLichHen, ngayHen, gioKetThuc, gioBatDau);

            if (isConflict) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Lỗi: Bác sĩ đã có lịch hẹn khác trong khoảng thời gian này!"));
            }

            // Cập nhật
            lichHen.setBacSi(bacSi);
            lichHen.setDichVu(dichVu);
            lichHen.setNgayHen(ngayHen);
            lichHen.setGioBatDau(gioBatDau);
            lichHen.setGioKetThuc(gioKetThuc);
            lichHen.setTrangThai(trangThai);
            lichHen.setGhiChu(appointmentRequest.getGhiChu());

            // Lưu và trả về DTO
            LichHen updatedLichHen = lichHenRepository.save(lichHen);
            logger.info("Appointment ID {} updated successfully", maLichHen);

            LichHenDTO lichHenDTO = new LichHenDTO(updatedLichHen);
            return ResponseEntity.ok(lichHenDTO);

        } catch (Exception e) {
            logger.error("Error updating appointment: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi khi cập nhật lịch hẹn: " + e.getMessage()));
        }
    }

//    /**
//     * API để xóa lịch hẹn
//     */
//    @DeleteMapping("/{maLichHen}")
//    public ResponseEntity<?> deleteAppointment(@PathVariable Integer maLichHen) {
//        try {
//            logger.info("Deleting appointment ID: {}", maLichHen);
//
//            Optional<LichHen> lichHenOpt = lichHenRepository.findById(maLichHen);
//            if (!lichHenOpt.isPresent()) {
//                logger.warn("Delete appointment failed: Appointment ID {} not found", maLichHen);
//                return ResponseEntity
//                        .status(HttpStatus.NOT_FOUND)
//                        .body(new MessageResponse("Không tìm thấy lịch hẹn với mã: " + maLichHen));
//            }
//
//            LichHen lichHen = lichHenOpt.get();
//
//            LocalDate today = LocalDate.now();
//            if (lichHen.getNgayHen().isBefore(today)) {
//                logger.warn("Delete appointment failed: Cannot delete past appointment ID {}", maLichHen);
//                return ResponseEntity
//                        .badRequest()
//                        .body(new MessageResponse("Không thể xóa lịch hẹn đã diễn ra trong quá khứ!"));
//            }
//
//            String tenTrangThai = lichHen.getTrangThai().getTenTrangThai();
//            if ("Đã hoàn thành".equalsIgnoreCase(tenTrangThai) || "Đã hủy".equalsIgnoreCase(tenTrangThai)) {
//                logger.warn("Delete appointment failed: Cannot delete appointment ID {} with status {}", maLichHen, tenTrangThai);
//                return ResponseEntity
//                        .badRequest()
//                        .body(new MessageResponse("Không thể xóa lịch hẹn đã hoàn thành hoặc đã hủy!"));
//            }
//
//            // Lưu thông tin để trả về sau khi xóa
//            LichHenDTO lichHenDTO = new LichHenDTO(lichHen);
//
//            lichHenRepository.delete(lichHen);
//            logger.info("Appointment ID {} deleted successfully", maLichHen);
//
//            return ResponseEntity.ok(lichHenDTO);
//        } catch (Exception e) {
//            logger.error("Error deleting appointment: " + e.getMessage(), e);
//            return ResponseEntity
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new MessageResponse("Lỗi khi xóa lịch hẹn: " + e.getMessage()));
//        }
//    }


    /**
     * API để hủy lịch hẹn (thay đổi trạng thái thành "Đã hủy" thay vì xóa hoàn toàn)
     */
    @PutMapping("/{maLichHen}/cancel")
    public ResponseEntity<?> cancelAppointment(@PathVariable Integer maLichHen, @RequestBody(required = false) String lyDo) {
        try {
            logger.info("Cancelling appointment ID: {}", maLichHen);

            Optional<LichHen> lichHenOpt = lichHenRepository.findById(maLichHen);
            if (!lichHenOpt.isPresent()) {
                logger.warn("Cancel appointment failed: Appointment ID {} not found", maLichHen);
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new MessageResponse("Không tìm thấy lịch hẹn với mã: " + maLichHen));
            }

            LichHen lichHen = lichHenOpt.get();

            LocalDate today = LocalDate.now();
            if (lichHen.getNgayHen().isBefore(today)) {
                logger.warn("Cancel appointment failed: Cannot cancel past appointment ID {}", maLichHen);
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Không thể hủy lịch hẹn đã diễn ra trong quá khứ!"));
            }

            String tenTrangThai = lichHen.getTrangThai().getTenTrangThai();
            if ("Đã hoàn thành".equalsIgnoreCase(tenTrangThai) || "Đã hủy".equalsIgnoreCase(tenTrangThai)) {
                logger.warn("Cancel appointment failed: Cannot cancel appointment ID {} with status {}", maLichHen, tenTrangThai);
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Không thể hủy lịch hẹn đã hoàn thành hoặc đã hủy!"));
            }

            Optional<TrangThaiLichHen> trangThaiHuyOpt = trangThaiLichHenRepository.findByTenTrangThai("Đã hủy");
            if (!trangThaiHuyOpt.isPresent()) {
                logger.error("Cancel appointment failed: 'Đã hủy' status not found in database");
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new MessageResponse("Lỗi hệ thống: Không tìm thấy trạng thái 'Đã hủy'!"));
            }

            // Cập nhật trạng thái
            lichHen.setTrangThai(trangThaiHuyOpt.get());

            // Cập nhật ghi chú với lý do hủy nếu có
            if (lyDo != null && !lyDo.trim().isEmpty()) {
                String ghiChuHienTai = lichHen.getGhiChu();
                String ghiChuMoi = (ghiChuHienTai != null && !ghiChuHienTai.isEmpty())
                        ? ghiChuHienTai + "\nLý do hủy: " + lyDo
                        : "Lý do hủy: " + lyDo;
                lichHen.setGhiChu(ghiChuMoi);
            }

            // Lưu vào database
            LichHen lichHenDaHuy = lichHenRepository.save(lichHen);
            logger.info("Appointment ID {} cancelled successfully", maLichHen);

            // Trả về DTO
            return ResponseEntity.ok(new LichHenDTO(lichHenDaHuy));
        } catch (Exception e) {
            logger.error("Error cancelling appointment: " + e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi khi hủy lịch hẹn: " + e.getMessage()));
        }
    }

}