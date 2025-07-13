package com.example.ClinicDentail.Controller;

import com.example.ClinicDentail.DTO.DichVuDTO;
import com.example.ClinicDentail.DTO.DoanhThuDTO;
import com.example.ClinicDentail.Service.DichVuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dichvu")
public class DichVuController {
    private static Logger logger = LoggerFactory.getLogger(DichVuController.class);
    @Autowired
    private DichVuService dichVuService;

    @PostMapping("/them")
    public ResponseEntity<?> themDichVu(@RequestBody DichVuDTO dto) {
        try {
            // Giả sử bạn có một phương thức để thêm dịch vụ
             dichVuService.themDichVu(dto);
            return ResponseEntity.ok("Thêm dịch vụ thành công");
        } catch (Exception e) {
            logger.error("Lỗi khi thêm dịch vụ: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Lỗi khi thêm dịch vụ: " + e.getMessage());
        }
    }
    @PostMapping("/xoa/{maDichVu}")
    public  ResponseEntity<?> xoaDichVu(@PathVariable Integer maDichVu) {
        try {
            dichVuService.xoaDichVu(maDichVu);
            return ResponseEntity.ok("Xóa dịch vụ thành công");
        } catch (Exception e) {
            logger.error("Lỗi khi xóa dịch vụ: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Lỗi khi xóa dịch vụ: " + e.getMessage());
        }
    }
    @PostMapping("/capnhat")
    public ResponseEntity<?> capNhatDichVu(@RequestBody DichVuDTO dto) {
        try {
            // Giả sử bạn có một phương thức để cập nhật dịch vụ
            dichVuService.capNhatDichVu(dto);
            return ResponseEntity.ok("Cập nhật dịch vụ thành công");
        } catch (Exception e) {
            logger.error("Lỗi khi cập nhật dịch vụ: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Lỗi khi cập nhật dịch vụ: " + e.getMessage());
        }
    }
}
