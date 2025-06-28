package com.example.ClinicDentail.Controller;

import com.example.ClinicDentail.DTO.HoaDonPaymentRequest;
import com.example.ClinicDentail.DTO.HoaDonPaymentResponse;
import com.example.ClinicDentail.Enity.HoaDon;
import com.example.ClinicDentail.Enity.ThanhToan;
import com.example.ClinicDentail.Repository.HoaDonRepository;
import com.example.ClinicDentail.Repository.ThanhToanRepository;
import com.example.ClinicDentail.Service.HoaDonPaymentService;
import com.example.ClinicDentail.VNpay.PaymentReturnResponse;
import com.example.ClinicDentail.VNpay.VNPayConfig;
import com.example.ClinicDentail.VNpay.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/hoa-don/payment")
@Slf4j
public class HoaDonPaymentController {
    @Autowired
    private  VNPayUtil vnPayUtil;
    @Autowired
    private VNPayConfig vnPayConfig;

    @Autowired
    private HoaDonPaymentService hoaDonPaymentService;
    @Autowired
    private ThanhToanRepository thanhToanRepository;
    @Autowired
    private HoaDonRepository hoaDonRepository;

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
    public void paymentReturn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Map<String, String> fields = new HashMap<>();
            for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
                String fieldName = params.nextElement();
                String fieldValue = request.getParameter(fieldName);
                if (fieldValue != null && fieldValue.length() > 0) {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnpSecureHash = request.getParameter("vnp_SecureHash");
            fields.remove("vnp_SecureHashType");
            fields.remove("vnp_SecureHash");

            String signValue = vnPayUtil.hashAllFields(fields, vnPayConfig.getSecretKey());

            String responseCode = request.getParameter("vnp_ResponseCode");
            String transactionStatus = request.getParameter("vnp_TransactionStatus");
            String orderInfo = request.getParameter("vnp_OrderInfo");
            String amount = request.getParameter("vnp_Amount");
            String vnpTxnRef = request.getParameter("vnp_TxnRef");
            String transactionNumber = request.getParameter("vnp_TransactionNo");
            String bankCode = request.getParameter("vnp_BankCode");
            String payDate = request.getParameter("vnp_PayDate");

            if (signValue.equals(vnpSecureHash)) {
                if ("00".equals(responseCode)) {
                    // Thanh toán thành công
                    try {
                        Integer maThanhToan = Integer.parseInt(vnpTxnRef.substring(2));
                        ThanhToan thanhToan = thanhToanRepository.findById(maThanhToan)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản ghi thanh toán"));

                        thanhToan.setTrangThaiThanhToan(ThanhToan.TrangThaiThanhToan.THANH_CONG);
                        thanhToan.setNgayThanhToan(LocalDateTime.now());

                        HoaDon hoaDon = thanhToan.getHoaDon();
                        hoaDon.setTrangThai(HoaDon.TrangThaiHoaDon.DA_THANH_TOAN);

                        hoaDonRepository.save(hoaDon);
                        thanhToanRepository.save(thanhToan);

                        log.info("Thanh toán thành công cho hóa đơn: {}, Transaction: {}", hoaDon.getMaHoaDon(), transactionNumber);

                    } catch (Exception dbException) {
                        log.error("Lỗi khi cập nhật database cho transaction: {}", vnpTxnRef, dbException);
                    }

                } else {
                    // Thanh toán thất bại
                    try {
                        Integer maThanhToan = Integer.parseInt(vnpTxnRef.substring(2));
                        ThanhToan thanhToan = thanhToanRepository.findById(maThanhToan)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản ghi thanh toán"));

                        thanhToan.setTrangThaiThanhToan(ThanhToan.TrangThaiThanhToan.THAT_BAI);
                        thanhToanRepository.save(thanhToan);

                        log.warn("Thanh toán thất bại cho hóa đơn: {}, Response code: {}", thanhToan.getHoaDon().getMaHoaDon(), responseCode);

                    } catch (Exception dbException) {
                        log.error("Lỗi khi cập nhật database cho transaction thất bại: {}", vnpTxnRef, dbException);
                    }
                }
            } else {
                log.error("Chữ ký không hợp lệ cho transaction: {}", vnpTxnRef);
            }

            String redirectUrl = UriComponentsBuilder
                    .fromUriString("http://localhost:5173/thong-tin-tai-khoan")
                    .queryParam("status", responseCode)
                    .queryParam("amount", amount)
                    .queryParam("orderNumber", vnpTxnRef)
                    .queryParam("bankCode", bankCode)
                    .queryParam("payDate", payDate)
                    .queryParam("transactionNumber", transactionNumber)
                    .build()
                    .toUriString();

            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            log.error("Error processing payment return", e);
            // nếu lỗi vẫn redirect về FE
            response.sendRedirect("http://localhost:5173/thong-tin-tai-khoan?status=error");
        }
    }


    private String getErrorMessage(String responseCode) {
        switch (responseCode) {
            case "07":
                return "Trừ tiền thành công. Giao dịch bị nghi ngờ (liên quan tới lừa đảo, giao dịch bất thường).";
            case "09":
                return "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng chưa đăng ký dịch vụ InternetBanking tại ngân hàng.";
            case "10":
                return "Giao dịch không thành công do: Khách hàng xác thực thông tin thẻ/tài khoản không đúng quá 3 lần";
            case "11":
                return "Giao dịch không thành công do: Đã hết hạn chờ thanh toán. Xin quý khách vui lòng thực hiện lại giao dịch.";
            case "12":
                return "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng bị khóa.";
            case "13":
                return "Giao dịch không thành công do Quý khách nhập sai mật khẩu xác thực giao dịch (OTP).";
            case "24":
                return "Giao dịch không thành công do: Khách hàng hủy giao dịch";
            case "51":
                return "Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch.";
            case "65":
                return "Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày.";
            case "75":
                return "Ngân hàng thanh toán đang bảo trì.";
            case "79":
                return "Giao dịch không thành công do: KH nhập sai mật khẩu thanh toán quá số lần quy định.";
            default:
                return "Giao dịch thất bại";
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
