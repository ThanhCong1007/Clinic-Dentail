package com.example.ClinicDentail.Controller;

import com.example.ClinicDentail.DTO.DoanhThuDTO;
import com.example.ClinicDentail.Service.DoanhThuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/doanh-thu")
@PreAuthorize("hasRole('ADMIN')")
public class DoanhThuController {

    @Autowired
    private DoanhThuService doanhThuService;

    @GetMapping("/tong-ket")
    public ResponseEntity<List<DoanhThuDTO>> getTongKetDoanhThu(
            @RequestParam String loaiThongKe, // THANG, QUY, NAM
            @RequestParam Integer nam,
            @RequestParam(required = false) Integer thang,
            @RequestParam(required = false) Integer quy) {

        List<DoanhThuDTO> result = doanhThuService.getTongKetDoanhThu(loaiThongKe, nam, thang, quy);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/theo-bac-si")
    public ResponseEntity<List<DoanhThuDTO>> getDoanhThuTheoBacSi(
            @RequestParam String loaiThongKe, // THANG, QUY, NAM
            @RequestParam Integer nam,
            @RequestParam(required = false) Integer thang,
            @RequestParam(required = false) Integer quy,
            @RequestParam(required = false) Integer maBacSi) {

        List<DoanhThuDTO> result = doanhThuService.getDoanhThuTheoBacSi(loaiThongKe, nam, thang, quy, maBacSi);
        return ResponseEntity.ok(result);
    }
}