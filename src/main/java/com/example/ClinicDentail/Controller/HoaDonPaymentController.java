package com.example.ClinicDentail.Controller;

import com.example.ClinicDentail.DTO.HoaDonPaymentRequest;
import com.example.ClinicDentail.DTO.HoaDonPaymentResponse;
import com.example.ClinicDentail.Service.HoaDonPaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/hoa-don/payment")
@Slf4j
public class HoaDonPaymentController {

    @Autowired
    private HoaDonPaymentService hoaDonPaymentService;

    @PostMapping("/create")
    public ResponseEntity<HoaDonPaymentResponse> createPaymentForHoaDon(
            @RequestBody HoaDonPaymentRequest request,
            HttpServletRequest httpRequest) {

        HoaDonPaymentResponse response = hoaDonPaymentService.createPaymentForHoaDon(request, httpRequest);

        if ("00".equals(response.getCode())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/return")
    public ResponseEntity<String> paymentReturn(HttpServletRequest request) {
        try {
            hoaDonPaymentService.processPaymentReturn(request);

            // Redirect đến trang thành công hoặc thất bại
            String responseCode = request.getParameter("vnp_ResponseCode");
            String vnpTxnRef = request.getParameter("vnp_TxnRef");

            if ("00".equals(responseCode)) {
                return ResponseEntity.ok("Thanh toán thành công! Mã giao dịch: " + vnpTxnRef);
            } else {
                return ResponseEntity.ok("Thanh toán thất bại! Mã lỗi: " + responseCode);
            }

        } catch (Exception e) {
            log.error("Lỗi khi xử lý kết quả thanh toán", e);
            return ResponseEntity.badRequest().body("Có lỗi xảy ra khi xử lý kết quả thanh toán");
        }
    }

    @GetMapping("/status/{maHoaDon}")
    public ResponseEntity<Map<String, Object>> getPaymentStatus(@PathVariable Integer maHoaDon) {
        try {
            // Logic để lấy trạng thái thanh toán của hóa đơn
            Map<String, Object> response = new HashMap<>();
            response.put("maHoaDon", maHoaDon);
            response.put("message", "API để kiểm tra trạng thái thanh toán");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Lỗi khi kiểm tra trạng thái thanh toán");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
