package com.example.ClinicDentail.VNpay;

import com.nimbusds.jose.shaded.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/vnpay")
@RequiredArgsConstructor
@Slf4j
public class VNPayController {

    private final VNPayConfig vnPayConfig;
    private final VNPayUtil vnPayUtil;
    private final RestTemplate restTemplate;

    @PostMapping("/create-payment")
    public ResponseEntity<PaymentResponse> createPayment(
            @RequestBody PaymentRequest request,
            HttpServletRequest httpRequest) {

        try {
            String vnpVersion = "2.1.0";
            String vnpCommand = "pay";
            String orderType = "other";
            long amount = request.getAmount() * 100;
            String bankCode = request.getBankCode();

            String vnpTxnRef = vnPayUtil.getRandomNumber(8);
            String vnpIpAddr = vnPayUtil.getIpAddress(httpRequest);
            String vnpTmnCode = vnPayConfig.getTmnCode();

            Map<String, String> vnpParams = new HashMap<>();
            vnpParams.put("vnp_Version", vnpVersion);
            vnpParams.put("vnp_Command", vnpCommand);
            vnpParams.put("vnp_TmnCode", vnpTmnCode);
            vnpParams.put("vnp_Amount", String.valueOf(amount));
            vnpParams.put("vnp_CurrCode", "VND");

            if (bankCode != null && !bankCode.isEmpty()) {
                vnpParams.put("vnp_BankCode", bankCode);
            }

            vnpParams.put("vnp_TxnRef", vnpTxnRef);
            vnpParams.put("vnp_OrderInfo", "Thanh toan don hang:" + vnpTxnRef);
            vnpParams.put("vnp_OrderType", orderType);

            String locale = request.getLanguage();
            if (locale != null && !locale.isEmpty()) {
                vnpParams.put("vnp_Locale", locale);
            } else {
                vnpParams.put("vnp_Locale", "vn");
            }

            vnpParams.put("vnp_ReturnUrl", vnPayConfig.getReturnUrl());
            vnpParams.put("vnp_IpAddr", vnpIpAddr);

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnpCreateDate = formatter.format(cld.getTime());
            vnpParams.put("vnp_CreateDate", vnpCreateDate);

            cld.add(Calendar.MINUTE, 15);
            String vnpExpireDate = formatter.format(cld.getTime());
            vnpParams.put("vnp_ExpireDate", vnpExpireDate);

            List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();

            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = vnpParams.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));

                    query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));

                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }

            String queryUrl = query.toString();
            String vnpSecureHash = vnPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
            String paymentUrl = vnPayConfig.getPayUrl() + "?" + queryUrl;

            PaymentResponse response = new PaymentResponse();
            response.setCode("00");
            response.setMessage("success");
            response.setData(paymentUrl);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error creating payment", e);
            PaymentResponse response = new PaymentResponse();
            response.setCode("99");
            response.setMessage("error");
            response.setData(null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/query")
    public ResponseEntity<String> queryPayment(
            @RequestBody QueryRequest request,
            HttpServletRequest httpRequest) {

        try {
            String vnpRequestId = vnPayUtil.getRandomNumber(8);
            String vnpVersion = "2.1.0";
            String vnpCommand = "querydr";
            String vnpTmnCode = vnPayConfig.getTmnCode();
            String vnpTxnRef = request.getOrderId();
            String vnpOrderInfo = "Kiem tra ket qua GD OrderId:" + vnpTxnRef;
            String vnpTransDate = request.getTransDate();

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnpCreateDate = formatter.format(cld.getTime());

            String vnpIpAddr = vnPayUtil.getIpAddress(httpRequest);

            JsonObject vnpParams = new JsonObject();
            vnpParams.addProperty("vnp_RequestId", vnpRequestId);
            vnpParams.addProperty("vnp_Version", vnpVersion);
            vnpParams.addProperty("vnp_Command", vnpCommand);
            vnpParams.addProperty("vnp_TmnCode", vnpTmnCode);
            vnpParams.addProperty("vnp_TxnRef", vnpTxnRef);
            vnpParams.addProperty("vnp_OrderInfo", vnpOrderInfo);
            vnpParams.addProperty("vnp_TransactionDate", vnpTransDate);
            vnpParams.addProperty("vnp_CreateDate", vnpCreateDate);
            vnpParams.addProperty("vnp_IpAddr", vnpIpAddr);

            String hashData = String.join("|", vnpRequestId, vnpVersion, vnpCommand,
                    vnpTmnCode, vnpTxnRef, vnpTransDate, vnpCreateDate, vnpIpAddr, vnpOrderInfo);
            String vnpSecureHash = vnPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);

            vnpParams.addProperty("vnp_SecureHash", vnpSecureHash);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(vnpParams.toString(), headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    vnPayConfig.getApiUrl(), entity, String.class);

            log.info("Query response: {}", response.getBody());
            return response;

        } catch (Exception e) {
            log.error("Error querying payment", e);
            return ResponseEntity.badRequest().body("Error occurred");
        }
    }

    @PostMapping("/refund")
    public ResponseEntity<String> refundPayment(
            @RequestBody RefundRequest request,
            HttpServletRequest httpRequest) {

        try {
            String vnpRequestId = vnPayUtil.getRandomNumber(8);
            String vnpVersion = "2.1.0";
            String vnpCommand = "refund";
            String vnpTmnCode = vnPayConfig.getTmnCode();
            String vnpTransactionType = request.getTranType();
            String vnpTxnRef = request.getOrderId();
            long amount = request.getAmount() * 100;
            String vnpAmount = String.valueOf(amount);
            String vnpOrderInfo = "Hoan tien GD OrderId:" + vnpTxnRef;
            String vnpTransactionNo = "";
            String vnpTransactionDate = request.getTransDate();
            String vnpCreateBy = request.getUser();

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnpCreateDate = formatter.format(cld.getTime());

            String vnpIpAddr = vnPayUtil.getIpAddress(httpRequest);

            JsonObject vnpParams = new JsonObject();
            vnpParams.addProperty("vnp_RequestId", vnpRequestId);
            vnpParams.addProperty("vnp_Version", vnpVersion);
            vnpParams.addProperty("vnp_Command", vnpCommand);
            vnpParams.addProperty("vnp_TmnCode", vnpTmnCode);
            vnpParams.addProperty("vnp_TransactionType", vnpTransactionType);
            vnpParams.addProperty("vnp_TxnRef", vnpTxnRef);
            vnpParams.addProperty("vnp_Amount", vnpAmount);
            vnpParams.addProperty("vnp_OrderInfo", vnpOrderInfo);

            if (vnpTransactionNo != null && !vnpTransactionNo.isEmpty()) {
                vnpParams.addProperty("vnp_TransactionNo", vnpTransactionNo);
            }

            vnpParams.addProperty("vnp_TransactionDate", vnpTransactionDate);
            vnpParams.addProperty("vnp_CreateBy", vnpCreateBy);
            vnpParams.addProperty("vnp_CreateDate", vnpCreateDate);
            vnpParams.addProperty("vnp_IpAddr", vnpIpAddr);

            String hashData = String.join("|", vnpRequestId, vnpVersion, vnpCommand, vnpTmnCode,
                    vnpTransactionType, vnpTxnRef, vnpAmount, vnpTransactionNo, vnpTransactionDate,
                    vnpCreateBy, vnpCreateDate, vnpIpAddr, vnpOrderInfo);

            String vnpSecureHash = vnPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
            vnpParams.addProperty("vnp_SecureHash", vnpSecureHash);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(vnpParams.toString(), headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    vnPayConfig.getApiUrl(), entity, String.class);

            log.info("Refund response: {}", response.getBody());
            return response;

        } catch (Exception e) {
            log.error("Error processing refund", e);
            return ResponseEntity.badRequest().body("Error occurred");
        }
    }

    @GetMapping("/return")
    public ResponseEntity<PaymentReturnResponse> paymentReturn(HttpServletRequest request) {
        PaymentReturnResponse response = new PaymentReturnResponse();

        try {
            // Lấy tất cả parameters từ VNPay
            Map<String, String> fields = new HashMap<>();
            for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
                String fieldName = params.nextElement();
                String fieldValue = request.getParameter(fieldName);
                if (fieldValue != null && fieldValue.length() > 0) {
                    fields.put(fieldName, fieldValue);
                }
            }

            // Lấy secure hash từ VNPay
            String vnpSecureHash = request.getParameter("vnp_SecureHash");

            // Xóa các trường hash và hashtype khỏi data cần verify
            fields.remove("vnp_SecureHashType");
            fields.remove("vnp_SecureHash");

            // Tạo lại secure hash để verify
            String signValue = vnPayUtil.hashAllFields(fields, vnPayConfig.getSecretKey());

            // Lấy thông tin từ response
            String responseCode = request.getParameter("vnp_ResponseCode");
            String transactionStatus = request.getParameter("vnp_TransactionStatus");
            String orderInfo = request.getParameter("vnp_OrderInfo");
            String amount = request.getParameter("vnp_Amount");
            String orderNumber = request.getParameter("vnp_TxnRef");
            String transactionNumber = request.getParameter("vnp_TransactionNo");
            String bankCode = request.getParameter("vnp_BankCode");
            String payDate = request.getParameter("vnp_PayDate");

            // Verify signature
            if (signValue.equals(vnpSecureHash)) {
                // Signature hợp lệ
                if ("00".equals(responseCode)) {
                    // Giao dịch thành công
                    response.setStatus("success");
                    response.setMessage("Thanh toán thành công!");
                    response.setResponseCode(responseCode);
                    response.setTransactionStatus(transactionStatus);

                    log.info("Payment successful for order: {}", orderNumber);

                    // TODO: Cập nhật trạng thái đơn hàng trong database
                    // orderService.updatePaymentStatus(orderNumber, "PAID");

                } else {
                    // Giao dịch thất bại
                    response.setStatus("failed");
                    response.setMessage("Thanh toán thất bại. " + getErrorMessage(responseCode));
                    response.setResponseCode(responseCode);

                    log.warn("Payment failed for order: {}, response code: {}", orderNumber, responseCode);
                }
            } else {
                // Signature không hợp lệ
                response.setStatus("error");
                response.setMessage("Chữ ký không hợp lệ!");
                log.error("Invalid signature for order: {}", orderNumber);
            }

            // Set common response data
            response.setOrderInfo(orderInfo);
            response.setAmount(amount);
            response.setOrderNumber(orderNumber);
            response.setTransactionNumber(transactionNumber);
            response.setBankCode(bankCode);
            response.setPayDate(payDate);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error processing payment return", e);
            response.setStatus("error");
            response.setMessage("Có lỗi xảy ra khi xử lý kết quả thanh toán");
            return ResponseEntity.badRequest().body(response);
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
}