package com.example.ClinicDentail.Controller;

import com.example.ClinicDentail.DTO.QuenMatKhauDTO;
import com.example.ClinicDentail.Enity.NguoiDung;
import com.example.ClinicDentail.Repository.NguoiDungRepository;
import com.example.ClinicDentail.Service.AuthService;
import com.example.ClinicDentail.Service.OTPService;
import com.example.ClinicDentail.Service.SMSService;
import com.example.ClinicDentail.payload.request.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller xử lý các yêu cầu xác thực và đăng ký
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;
    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    @Autowired
    private OTPService otpService;
    @Autowired
    private SMSService smsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Xử lý yêu cầu đăng nhập
     * @param loginRequest Thông tin đăng nhập
     * @return JWT token và thông tin người dùng
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse jwtResponse = authService.login(loginRequest);
            return ResponseEntity.ok(jwtResponse);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(new MessageResponse(e.getReason()));
        } catch (Exception e) {
            logger.error("Login error: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi đăng nhập: " + e.getMessage()));
        }
    }

    /**
     * API refresh token - tạo access token mới từ refresh token
     * @param refreshTokenRequest chứa refresh token
     * @return access token mới
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {
            String newAccessToken = authService.refreshToken(refreshTokenRequest.getRefreshToken());
            return ResponseEntity.ok(new RefreshTokenResponse(newAccessToken));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(new MessageResponse(e.getReason()));
        } catch (Exception e) {
            logger.error("Refresh token error: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi refresh token: " + e.getMessage()));
        }
    }

    /**
     * API validate token - kiểm tra token còn hợp lệ không
     * @param authHeader Authorization header chứa JWT token
     * @return thông tin validation
     */
    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String jwt = authHeader.substring(7);
                TokenValidationResponse validationResponse = authService.validateToken(jwt);
                return ResponseEntity.ok(validationResponse);
            }

            return ResponseEntity.ok(new TokenValidationResponse(false, "Token không hợp lệ"));
        } catch (Exception e) {
            logger.error("Token validation error: " + e.getMessage(), e);
            return ResponseEntity.ok(new TokenValidationResponse(false, "Token không hợp lệ: " + e.getMessage()));
        }
    }

    /**
     * Xử lý yêu cầu đăng xuất
     * @param authHeader Authorization header chứa JWT token
     * @return Thông báo kết quả đăng xuất
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        // Kiểm tra authorization header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            authService.logout(jwt);
            return ResponseEntity.ok(new MessageResponse("Đăng xuất thành công!"));
        }

        logger.warn("Logout failed: No valid token provided");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse("Đăng xuất thất bại: Không tìm thấy token hợp lệ!"));
    }

    /**
     * Xử lý yêu cầu đăng ký người dùng mới
     * @param signupRequest Thông tin đăng ký
     * @return Thông báo kết quả đăng ký
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        try {
            authService.registerUser(signupRequest);
            return ResponseEntity.ok(new MessageResponse("Đăng ký người dùng thành công!"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(new MessageResponse(e.getReason()));
        } catch (Exception e) {
            logger.error("Registration error: " + e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi đăng ký: " + e.getMessage()));
        }
    }
    @PostMapping("/gui-otp")
    public ResponseEntity<QuenMatKhauDTO> guiOTP(@RequestBody QuenMatKhauDTO request) {
        try {
            // Kiểm tra số điện thoại có tồn tại không
            NguoiDung nguoiDung = nguoiDungRepository.findBySoDienThoai(request.getSoDienThoai());
            if (nguoiDung == null) {
                return ResponseEntity.badRequest()
                        .body(new QuenMatKhauDTO(false, "Số điện thoại không tồn tại trong hệ thống"));
            }

            // Tạo và gửi OTP
            String otp = otpService.taoOTP();
            otpService.luuOTP(request.getSoDienThoai(), otp);
            smsService.guiOTPQuenMatKhau(request.getSoDienThoai(), otp);

            return ResponseEntity.ok(new QuenMatKhauDTO(true, "OTP đã được gửi đến số điện thoại của bạn"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new QuenMatKhauDTO(false, "Lỗi khi gửi OTP: " + e.getMessage()));
        }
    }

    // Bước 2: Xác thực OTP và đặt lại mật khẩu
    @PostMapping("/dat-lai-mat-khau")
    public ResponseEntity<QuenMatKhauDTO> datLaiMatKhau(@RequestBody QuenMatKhauDTO request) {
        try {
            // Kiểm tra OTP
            if (!otpService.kiemTraOTP(request.getSoDienThoai(), request.getOtp())) {
                return ResponseEntity.badRequest()
                        .body(new QuenMatKhauDTO(false, "OTP không đúng hoặc đã hết hạn"));
            }

            // Tìm người dùng
            NguoiDung nguoiDung = nguoiDungRepository.findBySoDienThoai(request.getSoDienThoai());
            if (nguoiDung == null) {
                return ResponseEntity.badRequest()
                        .body(new QuenMatKhauDTO(false, "Số điện thoại không tồn tại"));
            }

            // Cập nhật mật khẩu mới
            nguoiDung.setMatKhau(passwordEncoder.encode(request.getMatKhauMoi()));
            nguoiDungRepository.save(nguoiDung);

            // Xóa OTP đã sử dụng
            otpService.xoaOTP(request.getSoDienThoai());

            return ResponseEntity.ok(new QuenMatKhauDTO(true, "Mật khẩu đã được đặt lại thành công"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new QuenMatKhauDTO(false, "Lỗi khi đặt lại mật khẩu: " + e.getMessage()));
        }
    }
}