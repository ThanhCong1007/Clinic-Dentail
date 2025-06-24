package com.example.ClinicDentail.Controller;

import com.example.ClinicDentail.DTO.HoaDonDTO;
import com.example.ClinicDentail.Service.HoaDonService;
import com.example.ClinicDentail.payload.request.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/hoa-don")
public class HoaDonController {
    private final static Logger logger = LoggerFactory.getLogger(HoaDonController.class);

    @Autowired
    private HoaDonService hoaDonService;

    /**
     * API lấy danh sách hóa đơn theo bệnh nhân (người dùng)
     * GET /api/hoa-don/benh-nhan/{maBenhNhan}
     */
    @GetMapping("/benh-nhan/{maBenhNhan}")
    public ResponseEntity<?> getHoaDonByBenhNhan(@PathVariable Integer maBenhNhan) {
        try {
            List<HoaDonDTO> hoaDons = hoaDonService.getHoaDonByBenhNhan(maBenhNhan);
            return ResponseEntity.ok(hoaDons);
        } catch (Exception e) {
            logger.error("Lỗi khi lấy danh sách hóa đơn của bệnh nhân ID {}: {}", maBenhNhan, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi khi lấy danh sách hóa đơn: " + e.getMessage()));
        }
    }

    /**
     * API lấy danh sách tất cả hóa đơn (Admin)
     * GET /api/hoa-don/admin
     */
    @GetMapping("/admin")
    public ResponseEntity<?> getAllHoaDonForAdmin() {
        try {
            List<HoaDonDTO> hoaDons = hoaDonService.getAllHoaDon();
            return ResponseEntity.ok(hoaDons);
        } catch (Exception e) {
            logger.error("Lỗi khi lấy tất cả hóa đơn (admin): {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi khi lấy danh sách hóa đơn: " + e.getMessage()));
        }
    }

    /**
     * API xem chi tiết hóa đơn (dùng chung)
     * GET /api/hoa-don/{maHoaDon}
     */
    @GetMapping("/{maHoaDon}")
    public ResponseEntity<?> getHoaDonById(@PathVariable Integer maHoaDon) {
        try {
            Optional<HoaDonDTO> hoaDon = hoaDonService.getHoaDonById(maHoaDon);
            if (hoaDon.isPresent()) {
                return ResponseEntity.ok(hoaDon.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MessageResponse("Không tìm thấy hóa đơn có ID: " + maHoaDon));
            }
        } catch (Exception e) {
            logger.error("Lỗi khi xem chi tiết hóa đơn ID {}: {}", maHoaDon, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi khi lấy chi tiết hóa đơn: " + e.getMessage()));
        }
    }
}
