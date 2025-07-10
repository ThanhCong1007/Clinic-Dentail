package com.example.ClinicDentail.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OTPService {
    private static final Logger logger = LoggerFactory.getLogger(OTPService.class);
    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> otpExpiry = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public String taoOTP() {
        return String.format("%06d", random.nextInt(999999));
    }

    public void luuOTP(String soDienThoai, String otp) {
        otpStorage.put(soDienThoai, otp);
        otpExpiry.put(soDienThoai, LocalDateTime.now().plusMinutes(5)); // OTP hết hạn sau 5 phút
        logger.info("OTP đã được lưu cho số điện thoại: {}", soDienThoai);
    }

    public boolean kiemTraOTP(String soDienThoai, String otp) {
        String storedOTP = otpStorage.get(soDienThoai);
        LocalDateTime expiry = otpExpiry.get(soDienThoai);

        if (storedOTP == null || expiry == null) {
            return false;
        }

        if (LocalDateTime.now().isAfter(expiry)) {
            // OTP hết hạn, xóa khỏi storage
            otpStorage.remove(soDienThoai);
            otpExpiry.remove(soDienThoai);
            return false;
        }

        return storedOTP.equals(otp);
    }

    public void xoaOTP(String soDienThoai) {
        otpStorage.remove(soDienThoai);
        otpExpiry.remove(soDienThoai);
    }
}