package com.example.ClinicDentail.Controller;

import com.example.ClinicDentail.DTO.UserDTO;

import com.example.ClinicDentail.Enity.BacSi;
import com.example.ClinicDentail.Enity.BenhNhan;
import com.example.ClinicDentail.Enity.NguoiDung;
import com.example.ClinicDentail.Repository.NguoiDungRepository;
import com.example.ClinicDentail.Repository.VaiTroRepository;
import com.example.ClinicDentail.Repository.BacSiRepository;
import com.example.ClinicDentail.Repository.BenhNhanRepository;
import com.example.ClinicDentail.Security.Jwt.JwtUtils;
import com.example.ClinicDentail.Security.Service.UserDetailsImpl;
import com.example.ClinicDentail.payload.request.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

/**
 * Controller xử lý các yêu cầu xác thực và đăng ký
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private VaiTroRepository vaiTroRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private BenhNhanRepository benhNhanRepository;
    @Autowired
    private BacSiRepository bacSiRepository;

    /**
     * Xem thông tin tài khoản của người dùng hiện tại (hỗ trợ tất cả các role)
     * @return Thông tin chi tiết tài khoản theo role
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getAccountInfo() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Kiểm tra authentication
            if (authentication == null || !authentication.isAuthenticated()) {
                logger.warn("No valid authentication found");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new MessageResponse("Không có quyền truy cập"));
            }

            String username = extractUsername(authentication);
            if (username == null) {
                logger.error("Cannot extract username from authentication");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new MessageResponse("Token không hợp lệ"));
            }

            logger.info("Getting account info for user: {}", username);

            NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(username)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin người dùng"));

            // Tạo UserDTO với thông tin đầy đủ
            UserDTO userInfo = createUserInfo(nguoiDung);

            logger.info("Successfully retrieved account info for user: {} with role: {}",
                    username, nguoiDung.getVaiTro().getTenVaiTro());
            return ResponseEntity.ok(userInfo);

        } catch (Exception e) {
            logger.error("Error getting account info: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Lỗi khi lấy thông tin tài khoản: " + e.getMessage()));
        }
    }

    /**
     * Trích xuất username từ Authentication object
     */
    private String extractUsername(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetailsImpl) {
            return ((UserDetailsImpl) principal).getUsername();
        } else if (principal instanceof String) {
            return (String) principal;
        }
        logger.error("Unknown principal type: {}", principal.getClass());
        return null;
    }

    /**
     * Tạo thông tin user đầy đủ dựa trên vai trò
     */
    private UserDTO createUserInfo(NguoiDung nguoiDung) {
        UserDTO dto = new UserDTO();

        // Thông tin cơ bản từ bảng nguoi_dung
        dto.setMaNguoiDung(nguoiDung.getMaNguoiDung());
        dto.setTenDangNhap(nguoiDung.getTenDangNhap());
        dto.setEmail(nguoiDung.getEmail());
        dto.setHoTen(nguoiDung.getHoTen());
        dto.setSoDienThoai(nguoiDung.getSoDienThoai());
        dto.setTrangThaiHoatDong(nguoiDung.getTrangThaiHoatDong());
        dto.setNgayTao(nguoiDung.getNgayTao());

        if (nguoiDung.getVaiTro() != null) {
            dto.setMaVaiTro(nguoiDung.getVaiTro().getMaVaiTro());
            dto.setTenVaiTro(nguoiDung.getVaiTro().getTenVaiTro());
        }

        // Thêm thông tin chi tiết dựa trên vai trò
        String roleName = nguoiDung.getVaiTro().getTenVaiTro().toUpperCase();
        switch (roleName) {
            case "BACSI":
                enrichDoctorInfo(dto, nguoiDung);
                break;
            case "USER":
                enrichPatientInfo(dto, nguoiDung);
                break;
            case "ADMIN":
                // Admin chỉ cần thông tin cơ bản, có thể thêm thống kê nếu cần
                break;
        }

        return dto;
    }


    /**
     * Bổ sung thông tin cho bác sĩ
     */
    private void enrichDoctorInfo(UserDTO dto, NguoiDung nguoiDung) {
        try {
            // Cách 1: Truy cập trực tiếp thông qua relationship (khuyến nghị)
            BacSi bacSi = nguoiDung.getBacSi();
            if (bacSi != null) {
                dto.setMaBacSi(bacSi.getMaBacSi());
                dto.setChuyenKhoa(bacSi.getChuyenKhoa());
                dto.setSoNamKinhNghiem(bacSi.getSoNamKinhNghiem());
                dto.setTrangThaiLamViec(bacSi.getTrangThaiLamViec());
                logger.info("Successfully loaded doctor info for user: {}", nguoiDung.getMaNguoiDung());
            } else {
                logger.warn("No doctor info found for user: {}", nguoiDung.getMaNguoiDung());

                // Cách 2: Fallback sử dụng repository nếu cần
                Optional<BacSi> bacSiOpt = bacSiRepository.findByNguoiDung(nguoiDung);
                if (bacSiOpt.isPresent()) {
                    BacSi bacSiFromRepo = bacSiOpt.get();
                    dto.setMaBacSi(bacSiFromRepo.getMaBacSi());
                    dto.setChuyenKhoa(bacSiFromRepo.getChuyenKhoa());
                    dto.setSoNamKinhNghiem(bacSiFromRepo.getSoNamKinhNghiem());
                    dto.setTrangThaiLamViec(bacSiFromRepo.getTrangThaiLamViec());
                    logger.info("Successfully loaded doctor info from repository for user: {}", nguoiDung.getMaNguoiDung());
                }
            }
        } catch (Exception e) {
            logger.error("Error getting doctor info for user {}: {}", nguoiDung.getMaNguoiDung(), e.getMessage(), e);

            // Thử cách khác nếu có lỗi lazy loading
            try {
                Optional<BacSi> bacSiOpt = bacSiRepository.findByNguoiDung(nguoiDung);
                if (bacSiOpt.isPresent()) {
                    BacSi bacSiFromRepo = bacSiOpt.get();
                    dto.setMaBacSi(bacSiFromRepo.getMaBacSi());
                    dto.setChuyenKhoa(bacSiFromRepo.getChuyenKhoa());
                    dto.setSoNamKinhNghiem(bacSiFromRepo.getSoNamKinhNghiem());
                    dto.setTrangThaiLamViec(bacSiFromRepo.getTrangThaiLamViec());
                    logger.info("Successfully loaded doctor info from repository as fallback for user: {}", nguoiDung.getMaNguoiDung());
                }
            } catch (Exception ex) {
                logger.error("Failed to load doctor info even with repository for user {}: {}", nguoiDung.getMaNguoiDung(), ex.getMessage());
            }
        }
    }



    /**
     * Bổ sung thông tin cho bệnh nhân
     */
    private void enrichPatientInfo(UserDTO dto, NguoiDung nguoiDung) {
        try {
            Optional<BenhNhan> benhNhanOpt = benhNhanRepository.findByNguoiDung(nguoiDung);
            if (benhNhanOpt.isPresent()) {
                BenhNhan benhNhan = benhNhanOpt.get();
                dto.setMaBenhNhan(benhNhan.getMaBenhNhan());
                dto.setNgaySinh(benhNhan.getNgaySinh());
                dto.setGioiTinh(benhNhan.getGioiTinh().name());
                dto.setDiaChi(benhNhan.getDiaChi());
                dto.setTienSuBenh(benhNhan.getTienSuBenh());
                dto.setDiUng(benhNhan.getDiUng());

                // Tính tuổi nếu có ngày sinh
                if (benhNhan.getNgaySinh() != null) {
                    dto.setTuoi(Period.between(benhNhan.getNgaySinh(), LocalDate.now()).getYears());
                }
            }
        } catch (Exception e) {
            logger.warn("Cannot get patient info for user {}: {}", nguoiDung, e.getMessage());
        }
    }

}
