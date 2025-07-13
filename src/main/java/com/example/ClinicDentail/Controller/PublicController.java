package com.example.ClinicDentail.Controller;

import com.example.ClinicDentail.DTO.*;
import com.example.ClinicDentail.Enity.DichVu;
import com.example.ClinicDentail.Enity.Thuoc;
import com.example.ClinicDentail.Repository.DichVuRepository;
import com.example.ClinicDentail.Repository.ThuocRepository;
import com.example.ClinicDentail.Service.BacSiService;
import com.example.ClinicDentail.Service.LichHenService;
import com.example.ClinicDentail.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "*")
public class PublicController {
    private final static Logger log = LoggerFactory.getLogger(PublicController.class);

    @Autowired
    private BacSiService bacSiService;

    @Autowired
    private LichHenService lichHenService;

    @Autowired
    private UserService userService;

    @Autowired
    private DichVuRepository dichVuRepository;

    @Autowired
    private ThuocRepository thuocRepository;

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
            List<LichHenDTO> appointments = lichHenService.getAppointmentsByDoctorAndDate(maBacSi, date);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/dichvu")
    public ResponseEntity<List<DichVuDTO>> getDichVu() {
        try {
            List<DichVu> dvList = dichVuRepository.findAll();
            List<DichVuDTO> dtoList = new ArrayList<>();

            for (DichVu dv : dvList) {
                DichVuDTO dto = new DichVuDTO();
                dto.setMaDichVu(dv.getMaDichVu());
                dto.setTenDichVu(dv.getTenDichVu());
                dto.setMoTa(dv.getMoTa());
                dto.setGia(dv.getGia());
                dtoList.add(dto);
            }

            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/Thuoc")
    public ResponseEntity<List<ThuocDTO>> getThuoc() {
        try {
            List<Thuoc> thuocList = thuocRepository.findAll();
            List<ThuocDTO> dtoList = thuocList.stream()
                    .map(ThuocDTO::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            log.error("Lỗi khi lấy danh sách thuốc: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ====== HELPER METHODS ======

    /**
     * Lấy các khung giờ trống của bác sĩ trong ngày
     */
    private List<TimeSlotDTO> getAvailableTimeSlotsForDoctor(Integer maBacSi, LocalDate date) {
        List<TimeSlotDTO> availableSlots = new ArrayList<>();

        // Lấy danh sách lịch hẹn đã đặt trong ngày
        List<LichHenDTO> bookedAppointments = lichHenService.getAppointmentsByDoctorAndDate(maBacSi, date);

        // Khung giờ làm việc (8:00 - 17:00, mỗi slot 30 phút)
        LocalTime startWork = LocalTime.of(8, 0);
        LocalTime endWork = LocalTime.of(17, 0);
        LocalTime breakStart = LocalTime.of(12, 0); // Nghỉ trưa
        LocalTime breakEnd = LocalTime.of(13, 0);

        LocalTime currentTime = startWork;
        while (currentTime.isBefore(endWork)) {
            LocalTime slotEnd = currentTime.plusMinutes(30);

            // 👉 Bỏ qua giờ nghỉ trưa
            if (!(currentTime.isBefore(breakEnd) && slotEnd.isAfter(breakStart))) {

                // 👉 Bỏ qua những slot đã qua nếu là hôm nay
                if (date.equals(LocalDate.now()) && slotEnd.isBefore(LocalTime.now())) {
                    currentTime = currentTime.plusMinutes(30);
                    continue;
                }

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
     * Kiểm tra khung giờ có khả dụng không
     */
    private boolean isTimeSlotAvailable(Integer maBacSi, LocalDate date, LocalTime startTime, LocalTime endTime) {
        List<LichHenDTO> bookedAppointments = lichHenService.getAppointmentsByDoctorAndDate(maBacSi, date);

        return bookedAppointments.stream()
                .noneMatch(app ->
                        (startTime.isBefore(app.getGioKetThuc()) && endTime.isAfter(app.getGioBatDau()))
                );
    }
}
