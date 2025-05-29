package com.example.ClinicDentail.Service;

import com.example.ClinicDentail.Enity.NguoiDung;
import com.example.ClinicDentail.Repository.NguoiDungRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class AuthUtils {

    private final NguoiDungRepository nguoiDungRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthUtils.class);

    // Lấy người dùng hiện tại, nếu không tồn tại sẽ throw 401
    public NguoiDung getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Authentication is null or not authenticated");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Không có quyền truy cập");
        }

        String username = extractUsername(authentication);
        if (username == null || username.isBlank()) {
            logger.error("Failed to extract username from authentication");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token không hợp lệ");
        }

        return nguoiDungRepository.findByTenDangNhap(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy thông tin người dùng"));
    }

    // Lấy người dùng và kiểm tra có vai trò hợp lệ không
    public NguoiDung requireRole(String... allowedRoles) {
        NguoiDung user = getCurrentUser();

        String userRole = user.getVaiTro().getTenVaiTro().toUpperCase();
        for (String role : allowedRoles) {
            if (userRole.equalsIgnoreCase(role)) {
                return user; // Vai trò hợp lệ
            }
        }

        logger.warn("User {} does not have required role. Found: {}, Required: {}", user.getTenDangNhap(), userRole, String.join(", ", allowedRoles));
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Không đủ quyền. Vai trò yêu cầu: " + String.join(", ", allowedRoles));
    }

    private String extractUsername(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else if (principal instanceof String str) {
            return str;
        }
        return null;
    }
}