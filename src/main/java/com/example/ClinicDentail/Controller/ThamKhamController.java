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
     * ENDPOINT 1: Thực hiện thăm khám (dành cho bác sĩ khi khám bệnh nhân)
     * - Xử lý cả khách hẹn trước và khách vãng lai
     * - Tạo bệnh án tự động sau khi khám
     * - Có thể tạo lịch hẹn mới nếu cần
     */
    @PostMapping("/tham-kham")
    @PreAuthorize("hasRole('BACSI')")
    public ResponseEntity<?> thucHienThamKham(@RequestBody BenhAnDTO request) {
        try {
            // Validate dữ liệu đầu vào cơ bản
            if (request.getMaBacSi() == null) {
                return ResponseEntity.badRequest().body("Mã bác sĩ không được để trống");
            }

            if (request.getLyDoKham() == null || request.getLyDoKham().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Lý do khám không được để trống");
            }

            // Validate theo từng trường hợp
            if (request.getMaLichHen() == null) {
                // Trường hợp khách vãng lai
                if (request.getTenBenhNhan() == null || request.getTenBenhNhan().trim().isEmpty()) {
                    return ResponseEntity.badRequest().body("Họ tên không được để trống");
                }
                if (request.getSoDienThoai() == null || request.getSoDienThoai().trim().isEmpty()) {
                    return ResponseEntity.badRequest().body("Số điện thoại không được để trống");
                }
            } else {
                // Trường hợp có lịch hẹn - kiểm tra lịch hẹn có thể khám
                if (!thamKhamService.kiemTraLichHenCoTheKham(request.getMaLichHen())) {
                    return ResponseEntity.badRequest().body("Lịch hẹn không trong trạng thái có thể thăm khám");
                }
            }

            BenhAnDTO response = thamKhamService.thamKhamBenhNhan(request);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    /**
     * Cập nhật bệnh án hiện có
     */
    @PutMapping("/benh-an/{maBenhAn}")
    @PreAuthorize("hasRole('BACSI')")
    public ResponseEntity<?> capNhatBenhAn(
            @PathVariable Integer maBenhAn,
            @RequestBody BenhAnDTO request) {
        try {
            // Set mã bệnh án từ path variable
            request.setMaBenhAn(maBenhAn);

            BenhAnDTO result = thamKhamService.capNhatBenhAn(request);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

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
     * Kiểm tra xem lịch hẹn có thể thăm khám được hay không
     */
    @GetMapping("/lich-hen/{maLichHen}/kiem-tra")
    @PreAuthorize("hasRole('BACSI') or hasRole('ADMIN')")
    public ResponseEntity<?> kiemTraLichHen(@PathVariable Integer maLichHen) {
        try {
            boolean coTheKham = thamKhamService.kiemTraLichHenCoTheKham(maLichHen);
            return ResponseEntity.ok(java.util.Map.of("coTheKham", coTheKham));
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
     * Xóa bệnh án
     */
    @DeleteMapping("/benh-an/{maBenhAn}")
    @PreAuthorize("hasRole('BACSI') or hasRole('ADMIN')")
    public ResponseEntity<?> xoaBenhAn(@PathVariable Integer maBenhAn) {
        try {
            thamKhamService.xoaBenhAn(maBenhAn);
            return ResponseEntity.ok("Xóa bệnh án thành công");
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
