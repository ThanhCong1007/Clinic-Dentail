package com.example.ClinicDentail.Controller;

import com.example.ClinicDentail.DTO.LichHenDTO;
import com.example.ClinicDentail.Service.AppointmentService;
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

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);

    @Autowired
    private AppointmentService appointmentService;

    /**
     * API đăng ký lịch hẹn mới
     * @param appointmentRequest Thông tin lịch hẹn từ client
     * @return Thông báo kết quả
     */
    @PostMapping("/register")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> registerAppointment(@Valid @RequestBody AppointmentRequest appointmentRequest) {
        try {
            LichHenDTO result = appointmentService.registerAppointment(appointmentRequest);
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
            List<LichHenDTO> appointments = appointmentService.getAppointmentsByPatient(maBenhNhan);
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
            LichHenDTO appointment = appointmentService.getAppointmentDetail(maLichHen);
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
            LichHenDTO updatedAppointment = appointmentService.updateAppointment(maLichHen, appointmentRequest);
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
                                               @RequestBody(required = false) String lyDo) {
        try {
            LichHenDTO cancelledAppointment = appointmentService.cancelAppointment(maLichHen, lyDo);
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

//    /**
//     * API để xóa lịch hẹn
//     */
//    @DeleteMapping("/{maLichHen}")
//    public ResponseEntity<?> deleteAppointment(@PathVariable Integer maLichHen) {
//        try {
//            LichHenDTO deletedAppointment = appointmentService.deleteAppointment(maLichHen);
//            return ResponseEntity.ok(deletedAppointment);
//        } catch (RuntimeException e) {
//            logger.error("Delete appointment error: " + e.getMessage(), e);
//            if (e.getMessage().contains("Không tìm thấy lịch hẹn")) {
//                return ResponseEntity
//                        .status(HttpStatus.NOT_FOUND)
//                        .body(new MessageResponse(e.getMessage()));
//            }
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse(e.getMessage()));
//        } catch (Exception e) {
//            logger.error("Unexpected delete appointment error: " + e.getMessage(), e);
//            return ResponseEntity
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new MessageResponse("Lỗi khi xóa lịch hẹn: " + e.getMessage()));
//        }
//    }
}