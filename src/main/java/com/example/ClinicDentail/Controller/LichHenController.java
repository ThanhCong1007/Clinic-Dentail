package com.example.ClinicDentail.Controller;

import com.example.ClinicDentail.DTO.LichHenDTO;
import com.example.ClinicDentail.Service.LichHenService;
import com.example.ClinicDentail.payload.request.AppointmentRequest;
import com.example.ClinicDentail.payload.request.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class LichHenController {

    private static final Logger logger = LoggerFactory.getLogger(LichHenController.class);

    @Autowired
    private LichHenService lichHenService;

    /**
     * API đăng ký lịch hẹn mới
     * @param appointmentRequest Thông tin lịch hẹn từ client
     * @return Thông báo kết quả
     */
    @PostMapping("/register")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> registerAppointment(@Valid @RequestBody AppointmentRequest appointmentRequest) {
        try {
            LichHenDTO result = lichHenService.dangKyLichHen(appointmentRequest);
            return ResponseEntity.ok(new MessageResponse("Đăng ký lịch hẹn thành công!"));
        } catch (RuntimeException e) {
            logger.error("Appointment registration error: " + e.getMessage(), e);
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected appointment registration error: " + e.getMessage(), e);
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
            List<LichHenDTO> appointments = lichHenService.getDanhSachBenhNhan(maBenhNhan);
            return ResponseEntity.ok(appointments);
        } catch (RuntimeException e) {
            logger.error("Get appointments by patient error: " + e.getMessage(), e);
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected get appointments error: " + e.getMessage(), e);
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
            LichHenDTO appointment = lichHenService.getChiTietLichHen(maLichHen);
            return ResponseEntity.ok(appointment);
        } catch (RuntimeException e) {
            logger.error("Get appointment detail error: " + e.getMessage(), e);
            if (e.getMessage().contains("Không tìm thấy lịch hẹn")) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new MessageResponse(e.getMessage()));
            }
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected get appointment detail error: " + e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi khi lấy thông tin lịch hẹn: " + e.getMessage()));
        }
    }

    /**
     * API để cập nhật lịch hẹn
     */
    @PutMapping("/{maLichHen}")
    public ResponseEntity<?> updateAppointment(@PathVariable Integer maLichHen,
                                               @Valid @RequestBody AppointmentRequest appointmentRequest) {
        try {
            LichHenDTO updatedAppointment = lichHenService.capNhapLichHen(maLichHen, appointmentRequest);
            return ResponseEntity.ok(updatedAppointment);
        } catch (RuntimeException e) {
            logger.error("Update appointment error: " + e.getMessage(), e);
            if (e.getMessage().contains("Không tìm thấy lịch hẹn")) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new MessageResponse(e.getMessage()));
            }
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected update appointment error: " + e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi khi cập nhật lịch hẹn: " + e.getMessage()));
        }
    }

    /**
     * API để hủy lịch hẹn (thay đổi trạng thái thành "Đã hủy" thay vì xóa hoàn toàn)
     */
    @PutMapping("/{maLichHen}/cancel")
    public ResponseEntity<?> cancelAppointment(@PathVariable Integer maLichHen,
                                               @RequestBody(required = false) Map<String, Object> body) {
        try {
            String lyDo = body != null && body.containsKey("lyDo") ? (String) body.get("lyDo") : null;

            LichHenDTO cancelledAppointment = lichHenService.huyLichHen(maLichHen, lyDo);
            return ResponseEntity.ok(cancelledAppointment);
        } catch (RuntimeException e) {
            logger.error("Cancel appointment error: " + e.getMessage(), e);
            if (e.getMessage().contains("Không tìm thấy lịch hẹn")) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new MessageResponse(e.getMessage()));
            }
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected cancel appointment error: " + e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi khi hủy lịch hẹn: " + e.getMessage()));
        }
    }

}