package com.example.ClinicDentail.Controller;

import com.example.ClinicDentail.DTO.UserDTO;
import com.example.ClinicDentail.DTO.LichHenDTO;
import com.example.ClinicDentail.DTO.TimeSlotDTO;
import com.example.ClinicDentail.Service.BacSiService;
import com.example.ClinicDentail.Service.AppointmentService;
import com.example.ClinicDentail.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "*")
public class PublicController {

    @Autowired
    private BacSiService bacSiService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserService userService;

    /**
     * Lấy danh sách tất cả bác sĩ đang hoạt động
     */
    @GetMapping("/bac-si")
    public ResponseEntity<List<UserDTO>> getAllActiveDoctors() {
        try {
            List<UserDTO> bacSiList = bacSiService.getAllActiveDoctors();
            return ResponseEntity.ok(bacSiList);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Lấy danh sách bác sĩ theo chuyên khoa
     */
    @GetMapping("/bac-si/chuyen-khoa/{chuyenKhoa}")
    public ResponseEntity<List<UserDTO>> getDoctorsBySpecialty(
            @PathVariable String chuyenKhoa) {
        try {
            List<UserDTO> bacSiList = bacSiService.getDoctorsBySpecialty(chuyenKhoa);
            return ResponseEntity.ok(bacSiList);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Lấy thông tin chi tiết bác sĩ
     */
    @GetMapping("/bac-si/{maBacSi}")
    public ResponseEntity<UserDTO> getDoctorDetail(@PathVariable Integer maBacSi) {
        try {
            UserDTO bacSi = bacSiService.getDoctorById(maBacSi);
            if (bacSi != null) {
                return ResponseEntity.ok(bacSi);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Lấy các khung giờ trống của bác sĩ trong ngày cụ thể
     */
    @GetMapping("/bac-si/{maBacSi}/lich-trong")
    public ResponseEntity<List<TimeSlotDTO>> getAvailableTimeSlots(
            @PathVariable Integer maBacSi,
            @RequestParam String ngayHen) {
        try {
            LocalDate date = LocalDate.parse(ngayHen);
            List<TimeSlotDTO> availableSlots = getAvailableTimeSlotsForDoctor(maBacSi, date);
            return ResponseEntity.ok(availableSlots);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Lấy lịch làm việc của bác sĩ trong khoảng thời gian
     */
    @GetMapping("/bac-si/{maBacSi}/lich-lam-viec")
    public ResponseEntity<Map<String, List<TimeSlotDTO>>> getDoctorSchedule(
            @PathVariable Integer maBacSi,
            @RequestParam String tuNgay,
            @RequestParam String denNgay) {
        try {
            LocalDate fromDate = LocalDate.parse(tuNgay);
            LocalDate toDate = LocalDate.parse(denNgay);
            Map<String, List<TimeSlotDTO>> schedule = getDoctorScheduleInRange(maBacSi, fromDate, toDate);
            return ResponseEntity.ok(schedule);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Lấy danh sách chuyên khoa có sẵn
     */
    @GetMapping("/chuyen-khoa")
    public ResponseEntity<List<String>> getAllSpecialties() {
        try {
            List<String> chuyenKhoaList = bacSiService.getAllSpecialties();
            return ResponseEntity.ok(chuyenKhoaList);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Kiểm tra tính khả dụng của khung giờ cụ thể
     */
    @GetMapping("/bac-si/{maBacSi}/kiem-tra-lich")
    public ResponseEntity<Boolean> checkTimeSlotAvailability(
            @PathVariable Integer maBacSi,
            @RequestParam String ngayHen,
            @RequestParam String gioBatDau,
            @RequestParam String gioKetThuc) {
        try {
            LocalDate date = LocalDate.parse(ngayHen);
            LocalTime startTime = LocalTime.parse(gioBatDau);
            LocalTime endTime = LocalTime.parse(gioKetThuc);

            boolean isAvailable = isTimeSlotAvailable(maBacSi, date, startTime, endTime);
            return ResponseEntity.ok(isAvailable);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Lấy lịch hẹn đã đặt của bác sĩ trong ngày
     */
    @GetMapping("/bac-si/{maBacSi}/lich-hen")
    public ResponseEntity<List<LichHenDTO>> getDoctorAppointments(
            @PathVariable Integer maBacSi,
            @RequestParam String ngayHen) {
        try {
            LocalDate date = LocalDate.parse(ngayHen);
            List<LichHenDTO> appointments = appointmentService.getAppointmentsByDoctorAndDate(maBacSi, date);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Lấy thống kê tổng quan
     */
    @GetMapping("/thong-ke")
    public ResponseEntity<Map<String, Object>> getPublicStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("tongSoBacSi", bacSiService.countActiveDoctors());
            stats.put("soChuyenKhoa", bacSiService.countSpecialties());
            stats.put("lichHenHomNay", appointmentService.countTodayAppointments());
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // ====== HELPER METHODS ======

    /**
     * Lấy các khung giờ trống của bác sĩ trong ngày
     */
    private List<TimeSlotDTO> getAvailableTimeSlotsForDoctor(Integer maBacSi, LocalDate date) {
        List<TimeSlotDTO> availableSlots = new ArrayList<>();

        // Lấy danh sách lịch hẹn đã đặt trong ngày
        List<LichHenDTO> bookedAppointments = appointmentService.getAppointmentsByDoctorAndDate(maBacSi, date);

        // Khung giờ làm việc (8:00 - 17:00, mỗi slot 30 phút)
        LocalTime startWork = LocalTime.of(8, 0);
        LocalTime endWork = LocalTime.of(17, 0);
        LocalTime breakStart = LocalTime.of(12, 0); // Nghỉ trưa
        LocalTime breakEnd = LocalTime.of(13, 0);

        LocalTime currentTime = startWork;
        while (currentTime.isBefore(endWork)) {
            LocalTime slotEnd = currentTime.plusMinutes(30);

            // Bỏ qua giờ nghỉ trưa
            if (!(currentTime.isBefore(breakEnd) && slotEnd.isAfter(breakStart))) {
                LocalTime finalCurrentTime = currentTime;
                boolean isBooked = bookedAppointments.stream()
                        .anyMatch(app ->
                                (finalCurrentTime.isBefore(app.getGioKetThuc()) && slotEnd.isAfter(app.getGioBatDau()))
                        );

                if (!isBooked) {
                    availableSlots.add(new TimeSlotDTO(
                            currentTime.toString(),
                            slotEnd.toString(),
                            false,
                            "Trống"
                    ));
                }
            }

            currentTime = currentTime.plusMinutes(30);
        }

        return availableSlots;
    }

    /**
     * Lấy lịch làm việc của bác sĩ trong khoảng thời gian
     */
    private Map<String, List<TimeSlotDTO>> getDoctorScheduleInRange(Integer maBacSi, LocalDate fromDate, LocalDate toDate) {
        Map<String, List<TimeSlotDTO>> schedule = new HashMap<>();

        LocalDate currentDate = fromDate;
        while (!currentDate.isAfter(toDate)) {
            // Chỉ lấy lịch cho các ngày trong tuần (thứ 2 - thứ 6)
            if (currentDate.getDayOfWeek().getValue() <= 5) {
                List<TimeSlotDTO> slots = getAvailableTimeSlotsForDoctor(maBacSi, currentDate);
                schedule.put(currentDate.toString(), slots);
            }
            currentDate = currentDate.plusDays(1);
        }

        return schedule;
    }

    /**
     * Kiểm tra khung giờ có khả dụng không
     */
    private boolean isTimeSlotAvailable(Integer maBacSi, LocalDate date, LocalTime startTime, LocalTime endTime) {
        List<LichHenDTO> bookedAppointments = appointmentService.getAppointmentsByDoctorAndDate(maBacSi, date);

        return bookedAppointments.stream()
                .noneMatch(app ->
                        (startTime.isBefore(app.getGioKetThuc()) && endTime.isAfter(app.getGioBatDau()))
                );
    }
}
