package com.example.ClinicDentail.Controller;

import com.example.ClinicDentail.DTO.UserDTO;

import com.example.ClinicDentail.Enity.BacSi;
import com.example.ClinicDentail.Enity.BenhNhan;
import com.example.ClinicDentail.Enity.NguoiDung;
import com.example.ClinicDentail.Enity.VaiTro;
import com.example.ClinicDentail.Repository.NguoiDungRepository;
import com.example.ClinicDentail.Repository.VaiTroRepository;
import com.example.ClinicDentail.Repository.BacSiRepository;
import com.example.ClinicDentail.Repository.BenhNhanRepository;
import com.example.ClinicDentail.Security.Jwt.JwtUtils;
import com.example.ClinicDentail.Security.Service.UserDetailsImpl;
import com.example.ClinicDentail.payload.request.MessageResponse;
import com.example.ClinicDentail.payload.request.SignupRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private BenhNhanRepository benhNhanRepository;

    @Autowired
    private BacSiRepository bacSiRepository;
    @Autowired
    private AuthUtils authUtils;

    /**
     * Xem thông tin tài khoản của người dùng hiện tại (hỗ trợ tất cả các role)
     * @return Thông tin chi tiết tài khoản theo role
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getAccountInfo() {
        try {
            // Lấy thông tin người dùng đã xác thực từ AuthUtils
            NguoiDung nguoiDung = authUtils.getCurrentUser(); // <-- refactor tại đây

            logger.info("Getting account info for user: {}", nguoiDung.getTenDangNhap());

            // Tạo UserDTO với thông tin đầy đủ
            UserDTO userInfo = createUserInfo(nguoiDung);

            logger.info("Successfully retrieved account info for user: {} with role: {}",
                    nguoiDung.getTenDangNhap(), nguoiDung.getVaiTro().getTenVaiTro());

            return ResponseEntity.ok(userInfo);

        } catch (ResponseStatusException e) {
            logger.error("Authentication error while getting account info: {}", e.getReason());
            return ResponseEntity.status(e.getStatusCode())
                    .body(new MessageResponse(e.getReason()));
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
    /**
     * Cập nhật thông tin tài khoản của người dùng hiện tại
     * @param userDTO Thông tin cần cập nhật
     * @return Thông tin tài khoản sau khi cập nhật
     */
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody UserDTO userDTO) {
        try {
            // Lấy thông tin người dùng đã xác thực
            NguoiDung nguoiDung = authUtils.getCurrentUser(); // <-- dùng AuthUtils

            logger.info("Updating profile for user: {}", nguoiDung.getTenDangNhap());

            // Cập nhật thông tin cơ bản
            updateBasicUserInfo(nguoiDung, userDTO);

            // Cập nhật thông tin chi tiết theo vai trò
            String roleName = nguoiDung.getVaiTro().getTenVaiTro().toUpperCase();
            switch (roleName) {
                case "BACSI":
                    updateDoctorInfo(nguoiDung, userDTO);
                    break;
                case "USER":
                    updatePatientInfo(nguoiDung, userDTO);
                    break;
                case "ADMIN":
                    // Admin chỉ cập nhật thông tin cơ bản
                    break;
            }

            // Lưu thông tin đã cập nhật
            nguoiDungRepository.save(nguoiDung);

            // Trả về thông tin đã cập nhật
            UserDTO updatedUserInfo = createUserInfo(nguoiDung);

            logger.info("Successfully updated profile for user: {}", nguoiDung.getTenDangNhap());
            return ResponseEntity.ok(updatedUserInfo);

        } catch (ResponseStatusException e) {
            logger.error("Authentication error while updating profile: {}", e.getReason());
            return ResponseEntity.status(e.getStatusCode())
                    .body(new MessageResponse(e.getReason()));
        } catch (Exception e) {
            logger.error("Error updating profile: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Lỗi khi cập nhật thông tin: " + e.getMessage()));
        }
    }


    private void updateBasicUserInfo(NguoiDung nguoiDung, UserDTO userDTO) {
        // Cập nhật họ tên nếu có
        if (userDTO.getHoTen() != null && !userDTO.getHoTen().trim().isEmpty()) {
            nguoiDung.setHoTen(userDTO.getHoTen().trim());
        }

        // Cập nhật email nếu khác hiện tại
        if (userDTO.getEmail() != null && !userDTO.getEmail().trim().isEmpty()) {
            String newEmail = userDTO.getEmail().trim();
            if (!newEmail.equalsIgnoreCase(nguoiDung.getEmail())) {
                Optional<NguoiDung> existingUser = nguoiDungRepository.findByEmail(newEmail);
                if (existingUser.isPresent()) {
                    throw new RuntimeException("Email đã được sử dụng bởi tài khoản khác");
                }
                nguoiDung.setEmail(newEmail);
            }
        }

        // Cập nhật số điện thoại nếu hợp lệ
        if (userDTO.getSoDienThoai() != null && !userDTO.getSoDienThoai().trim().isEmpty()) {
            String newPhone = userDTO.getSoDienThoai().trim();
            if (!newPhone.equals(nguoiDung.getSoDienThoai())) {
                if (!isValidPhoneNumber(newPhone)) {
                    throw new RuntimeException("Số điện thoại không hợp lệ");
                }
                nguoiDung.setSoDienThoai(newPhone);
            }
        }

        logger.info("Updated basic info for user: {}", nguoiDung.getTenDangNhap());
    }


    /**
     * Cập nhật thông tin bác sĩ
     */
    private void updateDoctorInfo(NguoiDung nguoiDung, UserDTO userDTO) {
        try {
            BacSi bacSi = nguoiDung.getBacSi();
            if (bacSi == null) {
                // Fallback: tìm qua repository
                Optional<BacSi> bacSiOpt = bacSiRepository.findByNguoiDung(nguoiDung);
                if (bacSiOpt.isPresent()) {
                    bacSi = bacSiOpt.get();
                } else {
                    logger.warn("No doctor info found for user: {}", nguoiDung.getMaNguoiDung());
                    return;
                }
            }

            // Cập nhật thông tin bác sĩ
            if (userDTO.getChuyenKhoa() != null && !userDTO.getChuyenKhoa().trim().isEmpty()) {
                bacSi.setChuyenKhoa(userDTO.getChuyenKhoa().trim());
            }

            if (userDTO.getSoNamKinhNghiem() != null && userDTO.getSoNamKinhNghiem() >= 0) {
                bacSi.setSoNamKinhNghiem(userDTO.getSoNamKinhNghiem());
            }

            if (userDTO.getTrangThaiLamViec() != null) {
                bacSi.setTrangThaiLamViec(userDTO.getTrangThaiLamViec());
            }

            bacSiRepository.save(bacSi);
            logger.info("Updated doctor info for user: {}", nguoiDung.getTenDangNhap());

        } catch (Exception e) {
            logger.error("Error updating doctor info for user {}: {}", nguoiDung.getMaNguoiDung(), e.getMessage(), e);
            throw new RuntimeException("Lỗi khi cập nhật thông tin bác sĩ: " + e.getMessage());
        }
    }

    /**
     * Cập nhật thông tin bệnh nhân
     */
    private void updatePatientInfo(NguoiDung nguoiDung, UserDTO userDTO) {
        try {
            Optional<BenhNhan> benhNhanOpt = benhNhanRepository.findByNguoiDung(nguoiDung);
            if (!benhNhanOpt.isPresent()) {
                logger.warn("No patient info found for user: {}", nguoiDung.getMaNguoiDung());
                return;
            }

            BenhNhan benhNhan = benhNhanOpt.get();

            // Cập nhật thông tin bệnh nhân
            if (userDTO.getNgaySinh() != null) {
                // Validate ngày sinh
                if (userDTO.getNgaySinh().isAfter(LocalDate.now())) {
                    throw new RuntimeException("Ngày sinh không thể là ngày trong tương lai");
                }
                benhNhan.setNgaySinh(userDTO.getNgaySinh());
            }

            if (userDTO.getGioiTinh() != null && !userDTO.getGioiTinh().trim().isEmpty()) {
                try {
                    // Giả sử GioiTinh là enum, cần convert string thành enum
                    benhNhan.setGioiTinh(convertStringToGender(userDTO.getGioiTinh()));
                } catch (Exception e) {
                    throw new RuntimeException("Giới tính không hợp lệ");
                }
            }

            if (userDTO.getDiaChi() != null) {
                benhNhan.setDiaChi(userDTO.getDiaChi().trim());
            }

            if (userDTO.getTienSuBenh() != null) {
                benhNhan.setTienSuBenh(userDTO.getTienSuBenh().trim());
            }

            if (userDTO.getDiUng() != null) {
                benhNhan.setDiUng(userDTO.getDiUng().trim());
            }

            benhNhanRepository.save(benhNhan);
            logger.info("Updated patient info for user: {}", nguoiDung.getTenDangNhap());

        } catch (Exception e) {
            logger.error("Error updating patient info for user {}: {}", nguoiDung.getMaNguoiDung(), e.getMessage(), e);
            throw new RuntimeException("Lỗi khi cập nhật thông tin bệnh nhân: " + e.getMessage());
        }
    }

    /**
     * Validate số điện thoại
     */
    private boolean isValidPhoneNumber(String phoneNumber) {
        // Regex cho số điện thoại Việt Nam
        String phoneRegex = "^(\\+84|84|0)(3[2-9]|5[689]|7[06-9]|8[1-689]|9[0-46-9])[0-9]{7}$";
        return phoneNumber.matches(phoneRegex);
    }

    /**
     * Convert string thành enum GioiTinh
     * Bạn cần thay đổi logic này dựa trên enum GioiTinh thực tế của bạn
     */
    private BenhNhan.GioiTinh convertStringToGender(String gioiTinh) {
        // Giả sử enum GioiTinh có các giá trị NAM, NU
        switch (gioiTinh.toUpperCase()) {
            case "NAM":
            case "MALE":
                return BenhNhan.GioiTinh.Nam;
            case "NU":
            case "NỮ":
            case "FEMALE":
                return BenhNhan.GioiTinh.Nữ;
            default:
                throw new RuntimeException("Giới tính không hợp lệ: " + gioiTinh);
        }
    }



}
