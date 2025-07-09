package com.example.ClinicDentail.Controller;


import com.example.ClinicDentail.DTO.BenhAnDTO;
import com.example.ClinicDentail.Service.BenhAnService;
import com.example.ClinicDentail.payload.request.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/benh-an")
public class BenhAnController {
    private final BenhAnService benhAnService;

    public BenhAnController(BenhAnService benhAnService) {
        this.benhAnService = benhAnService;
    }

    /**
     *  Bác sĩ lấy danh sách bệnh án theo mã bác sĩ
     */
    @GetMapping("/bac-si/{maBacSi}")
    public ResponseEntity<?> getDanhSachBenhAnTheoBacSi(
            @PathVariable Integer maBacSi,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {

        try {
            Page<BenhAnDTO> danhSachBenhAn = benhAnService.getDanhSachBenhAnTheoBacSi(maBacSi, page, size, keyword);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Lấy danh sách bệnh án thành công");
            response.put("data", danhSachBenhAn.getContent());
            response.put("totalPages", danhSachBenhAn.getTotalPages());
            response.put("totalElements", danhSachBenhAn.getTotalElements());
            response.put("currentPage", page);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Lỗi khi lấy danh sách bệnh án: " + e.getMessage()
            ));
        }
    }

    /**
     * xem chi tiết bệnh án
     */
    @GetMapping("/chi-tiet/{maBenhAn}")
    public ResponseEntity<?> getChiTietBenhAnChoBacSi(@PathVariable Integer maBenhAn) {

        try {
            BenhAnDTO benhAnChiTiet = benhAnService.getChiTietBenhAnChoBacSi(maBenhAn);

            return ResponseEntity.ok(benhAnChiTiet);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi khi lấy danh sách bênh án: " + e.getMessage()));
        }
    }

    /**
     *  Bệnh nhân lấy danh sách bệnh án theo mã bệnh nhân
     */
    @GetMapping("/benh-nhan/{maBenhNhan}")
    public ResponseEntity<?> getDanhSachBenhAnTheoBenhNhan(
            @PathVariable Integer maBenhNhan,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            Page<BenhAnDTO> danhSachBenhAn = benhAnService.getDanhSachBenhAnTheoBenhNhan(maBenhNhan, page, size);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Lấy danh sách bệnh án thành công");
            response.put("data", danhSachBenhAn.getContent());
            response.put("totalPages", danhSachBenhAn.getTotalPages());
            response.put("totalElements", danhSachBenhAn.getTotalElements());
            response.put("currentPage", page);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Lỗi khi lấy danh sách bệnh án: " + e.getMessage()
            ));
        }
    }


}
