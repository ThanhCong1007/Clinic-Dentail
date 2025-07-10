package com.example.ClinicDentail.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class SMSService {
    private static final Logger logger = LoggerFactory.getLogger(SMSService.class);
    private static final String SMS_API_URL = "https://api.textbee.dev/api/v1/gateway/devices/684e82e14080863dda265bd5/send-sms";
    private static final String SMS_API_KEY = "f4644e52-989b-4460-8eb8-df5dce06a4d1";
    private final RestTemplate restTemplate;

    public SMSService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Chuyển đổi số điện thoại từ format 0xxx thành +84xxx
     */
    private String chuyenDoiSoDienThoai(String soDienThoai) {
        if (soDienThoai == null || soDienThoai.trim().isEmpty()) {
            return soDienThoai;
        }

        String soDT = soDienThoai.trim();

        // Nếu số điện thoại bắt đầu bằng 0, thay thế bằng +84
        if (soDT.startsWith("0")) {
            return "+84" + soDT.substring(1);
        }

        // Nếu đã có +84 thì giữ nguyên
        if (soDT.startsWith("+84")) {
            return soDT;
        }

        // Nếu chỉ có 9 số (không có số 0 đầu), thêm +84
        if (soDT.matches("\\d{9}")) {
            return "+84" + soDT;
        }

        return soDT;
    }

    /**
     * Gửi SMS thông báo tài khoản cho bệnh nhân
     */
    public void guiSMSThongBaoTaiKhoan(String soDienThoai, String matKhau) {
        try {
            // Chuyển đổi số điện thoại sang format +84
            String soDienThoaiFormatted = chuyenDoiSoDienThoai(soDienThoai);

            // Tạo nội dung tin nhắn
            String noiDungTinNhan = String.format(
                    "Cảm ơn quý khách đã sử dụng dịch vụ tại Phòng khám I-Dent. " +
                            "Để theo dõi bệnh án của mình hãy truy cập vào trang web:  " +
                            "Tài khoản: \"%s\", mật khẩu: \"%s\"",
                    soDienThoai, matKhau
            );

            // Tạo request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("recipients", Arrays.asList(soDienThoaiFormatted));
            requestBody.put("message", noiDungTinNhan);

            // Tạo headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-api-key", SMS_API_KEY);
            headers.set("Content-Type", "application/json");

            // Tạo HTTP entity
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // Gửi request
            ResponseEntity<String> response = restTemplate.exchange(
                    SMS_API_URL,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("SMS đã được gửi thành công. Response: {}", response.getBody());
            } else {
                logger.error("Lỗi khi gửi SMS. Status: {}, Response: {}",
                        response.getStatusCode(), response.getBody());
                throw new RuntimeException("Lỗi khi gửi SMS: " + response.getStatusCode());
            }

        } catch (Exception e) {
            logger.error("Exception khi gửi SMS: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể gửi SMS thông báo: " + e.getMessage(), e);
        }
    }
    /**
     * Gửi OTP để đặt lại mật khẩu
     */
    public void guiOTPQuenMatKhau(String soDienThoai, String otp) {
        String noiDungTinNhan = String.format(
                "Mã OTP để đặt lại mật khẩu của bạn là: %s. " +
                        "Mã này có hiệu lực trong 5 phút. " +
                        "Không chia sẻ mã này với bất kỳ ai.", otp
        );
        guiTinNhanDonGian(soDienThoai, noiDungTinNhan);
    }

    /**
     * Hàm tái sử dụng nội bộ để gửi tin nhắn đơn giản
     */
    private void guiTinNhanDonGian(String soDienThoai, String noiDungTinNhan) {
        try {
            String soDienThoaiFormatted = chuyenDoiSoDienThoai(soDienThoai);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("recipients", Collections.singletonList(soDienThoaiFormatted));
            requestBody.put("message", noiDungTinNhan);

            HttpHeaders headers = new HttpHeaders();
            headers.set("x-api-key", SMS_API_KEY);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    SMS_API_URL,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("SMS đã được gửi đến {}. Nội dung: {}", soDienThoai, noiDungTinNhan);
            } else {
                logger.error("Lỗi khi gửi SMS. Status: {}, Response: {}",
                        response.getStatusCode(), response.getBody());
                throw new RuntimeException("Lỗi khi gửi SMS: " + response.getStatusCode());
            }

        } catch (Exception e) {
            logger.error("Exception khi gửi SMS: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể gửi SMS: " + e.getMessage(), e);
        }
    }
}
