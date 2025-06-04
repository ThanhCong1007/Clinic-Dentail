package com.example.ClinicDentail.Controller;

import com.example.ClinicDentail.DTO.*;
import com.example.ClinicDentail.Enity.BenhNhan;
import com.example.ClinicDentail.Service.ThamKhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tham-kham")
@CrossOrigin(origins = "*")
public class ThamKhamController {

    @Autowired
    private ThamKhamService thamKhamService;

    /**
     * Tìm kiếm thông tin bệnh nhân theo số điện thoại
     */
    @GetMapping("/benh-nhan/sdt/{soDienThoai}")
    @PreAuthorize("hasRole('BACSI') or hasRole('ADMIN')")
    public ResponseEntity<?> getBenhNhanBySoDienThoai(@PathVariable String soDienThoai) {
        try {
            UserDTO benhNhanDTO = thamKhamService.getBenhNhanDTOBySoDienThoai(soDienThoai);
            if (benhNhanDTO != null) {
                return ResponseEntity.ok(benhNhanDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    /**
     * Lấy danh sách lịch hẹn kèm bệnh án theo bác sĩ
     */
    @GetMapping("/bac-si/{maBacSi}/lich-hen-benh-an")
    @PreAuthorize("hasRole('BACSI') or hasRole('ADMIN')")
    public ResponseEntity<?> getLichHenBenhAnByBacSi(@PathVariable Integer maBacSi) {
        try {
            List<LichHenBenhAnDTO> result = thamKhamService.getLichHenBenhAnByBacSi(maBacSi);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    /**
     * Lấy chi tiết lịch hẹn và bệnh án
     */
    @GetMapping("/lich-hen/{maLichHen}/chi-tiet")
    @PreAuthorize("hasRole('BACSI') or hasRole('ADMIN')")
    public ResponseEntity<?> getChiTietLichHenBenhAn(@PathVariable Integer maLichHen) {
        try {
            LichHenBenhAnDTO result = thamKhamService.getChiTietLichHenBenhAn(maLichHen);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    /**
     * Lấy danh sách bệnh án của bệnh nhân
     */
    @GetMapping("/benh-nhan/{maBenhNhan}/benh-an")
    @PreAuthorize("hasRole('BACSI') or hasRole('ADMIN')")
    public ResponseEntity<?> getBenhAnByBenhNhan(@PathVariable Integer maBenhNhan) {
        try {
            List<BenhAnDTO> result = thamKhamService.getBenhAnByBenhNhan(maBenhNhan);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }
}
