package com.example.ClinicDentail.Service;

import com.example.ClinicDentail.Enity.BenhNhan;
import com.example.ClinicDentail.Enity.NguoiDung;
import com.example.ClinicDentail.Enity.VaiTro;
import com.example.ClinicDentail.Repository.BenhNhanRepository;
import com.example.ClinicDentail.Repository.NguoiDungRepository;
import com.example.ClinicDentail.Repository.VaiTroRepository;
import com.example.ClinicDentail.Security.Jwt.JwtUtils;
import com.example.ClinicDentail.Security.Service.UserDetailsImpl;
import com.example.ClinicDentail.payload.request.JwtResponse;
import com.example.ClinicDentail.payload.request.LoginRequest;
import com.example.ClinicDentail.payload.request.SignupRequest;
import com.example.ClinicDentail.payload.request.TokenValidationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private VaiTroRepository vaiTroRepository;

    @Autowired
    private BenhNhanRepository benhNhanRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthUtils authUtils;

    /**
     * Xử lý đăng nhập người dùng
     */
    public JwtResponse login(LoginRequest loginRequest) {
        logger.info("Processing login request for user: {}", loginRequest.getTenDangNhap());

        // Xác thực thông tin đăng nhập
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getTenDangNhap(), loginRequest.getMatKhau()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Sinh JWT token
        String jwt = jwtUtils.generateJwtToken(authentication);

        // Lấy thông tin người dùng từ AuthUtils
        NguoiDung nguoiDung = authUtils.getCurrentUser();

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        logger.info("Login successful for user: {}", loginRequest.getTenDangNhap());

        return new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                nguoiDung.getHoTen(),
                roles);
    }

    /**
     * Xử lý đăng xuất người dùng
     */
    public void logout(String jwt) {
        logger.info("Processing logout request");

        // Nếu bạn muốn thêm token vào blacklist, hãy thực hiện ở đây
        // Ví dụ: jwtUtils.addToBlacklist(jwt);

        // Xóa thông tin xác thực khỏi context
        SecurityContextHolder.clearContext();

        logger.info("User logged out successfully");
    }

    /**
     * Kiểm tra tính hợp lệ của thông tin đăng ký
     */
    public void validateSignupRequest(SignupRequest signupRequest) {
        // Kiểm tra username đã tồn tại chưa
        if (nguoiDungRepository.existsByTenDangNhap(signupRequest.getTenDangNhap())) {
            logger.warn("Registration failed: Username {} is already taken", signupRequest.getTenDangNhap());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi: Tên đăng nhập đã được sử dụng!");
        }

        // Kiểm tra email đã tồn tại chưa
        if (nguoiDungRepository.existsByEmail(signupRequest.getEmail())) {
            logger.warn("Registration failed: Email {} is already in use", signupRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi: Email đã được sử dụng!");
        }

        // Kiểm tra số điện thoại trong bảng bệnh nhân
        validatePhoneNumber(signupRequest.getSoDienThoai());
    }
    /**
     * Kiểm tra số điện thoại trong bảng bệnh nhân
     */
    private void validatePhoneNumber(String soDienThoai) {
        Optional<BenhNhan> existingBenhNhan = benhNhanRepository.findBySoDienThoai(soDienThoai);

        if (existingBenhNhan.isPresent()) {
            BenhNhan benhNhan = existingBenhNhan.get();

            // Trường hợp 1: Số điện thoại đã có người dùng (mã người dùng != null)
            if (benhNhan.getNguoiDung() != null) {
                logger.warn("Registration failed: Phone number {} is already associated with a user account", soDienThoai);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi: Số điện thoại đã được sử dụng cho tài khoản khác!");
            }

            // Trường hợp 2: Số điện thoại có trong bệnh nhân nhưng chưa có tài khoản (mã người dùng = null)
            logger.info("Phone number {} found in patient records without user account, will be linked", soDienThoai);
        }

        // Trường hợp 3: Số điện thoại chưa tồn tại - không cần làm gì thêm
    }

    /**
     * Đăng ký người dùng mới
     */
    public void registerUser(SignupRequest signupRequest) {
        logger.info("Processing registration request for user: {}", signupRequest.getTenDangNhap());

        // Kiểm tra tính hợp lệ
        validateSignupRequest(signupRequest);

        // Lấy vai trò USER mặc định
        VaiTro vaiTro = vaiTroRepository.findByTenVaiTro("USER")
                .orElseThrow(() -> {
                    logger.error("Registration failed: Default USER role not found");
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi: Vai trò mặc định không tồn tại!");
                });

        // Tạo tài khoản người dùng mới
        NguoiDung nguoiDung = createNguoiDung(signupRequest, vaiTro);

        // Lưu thông tin người dùng vào database
        NguoiDung savedNguoiDung = nguoiDungRepository.save(nguoiDung);
        logger.info("User registered successfully: {}", signupRequest.getTenDangNhap());

        // Xử lý thông tin bệnh nhân
        handleBenhNhanInfo(signupRequest, savedNguoiDung);
    }

    /**
     * Tạo đối tượng NguoiDung từ thông tin đăng ký
     */
    private NguoiDung createNguoiDung(SignupRequest signupRequest, VaiTro vaiTro) {
        NguoiDung nguoiDung = new NguoiDung();
        nguoiDung.setTenDangNhap(signupRequest.getTenDangNhap());
        nguoiDung.setMatKhau(encoder.encode(signupRequest.getMatKhau()));
        nguoiDung.setEmail(signupRequest.getEmail());
        nguoiDung.setHoTen(signupRequest.getHoTen());
        nguoiDung.setSoDienThoai(signupRequest.getSoDienThoai());
        nguoiDung.setTrangThaiHoatDong(true);
        nguoiDung.setVaiTro(vaiTro);
        return nguoiDung;
    }
    /**
     * Xử lý thông tin bệnh nhân cho người dùng mới
     */
    private void handleBenhNhanInfo(SignupRequest signupRequest, NguoiDung nguoiDung) {
        try {
            Optional<BenhNhan> existingBenhNhan = benhNhanRepository.findBySoDienThoai(signupRequest.getSoDienThoai());

            if (existingBenhNhan.isPresent() && existingBenhNhan.get().getNguoiDung() == null) {
                // Trường hợp 2: Cập nhật thông tin bệnh nhân đã có
                updateExistingBenhNhan(existingBenhNhan.get(), signupRequest, nguoiDung);
            } else {
                // Trường hợp 3: Tạo mới thông tin bệnh nhân
                createNewBenhNhan(signupRequest, nguoiDung);
            }
        } catch (Exception ex) {
            // Log lỗi nhưng vẫn tiếp tục vì người dùng đã được tạo thành công
            logger.warn("Error handling patient record: " + ex.getMessage(), ex);
        }
    }

    /**
     * Cập nhật thông tin bệnh nhân đã có (trường hợp 2)
     */
    private void updateExistingBenhNhan(BenhNhan benhNhan, SignupRequest signupRequest, NguoiDung nguoiDung) {
        // Gán người dùng cho bệnh nhân
        benhNhan.setNguoiDung(nguoiDung);

        // Cập nhật thông tin từ đăng ký (nếu cần)
        if (signupRequest.getHoTen() != null && !signupRequest.getHoTen().trim().isEmpty()) {
            benhNhan.setHoTen(signupRequest.getHoTen());
        }

        if (signupRequest.getEmail() != null && !signupRequest.getEmail().trim().isEmpty()) {
            benhNhan.setEmail(signupRequest.getEmail());
        }

        if (signupRequest.getNgaySinh() != null) {
            benhNhan.setNgaySinh(signupRequest.getNgaySinh());
        }

        benhNhanRepository.save(benhNhan);
        logger.info("Existing patient record updated and linked to user: {}", signupRequest.getTenDangNhap());
    }

    /**
     * Tạo thông tin bệnh nhân mới (trường hợp 3)
     */
    private void createNewBenhNhan(SignupRequest signupRequest, NguoiDung nguoiDung) {
        BenhNhan benhNhan = new BenhNhan();
        benhNhan.setNguoiDung(nguoiDung);
        benhNhan.setHoTen(signupRequest.getHoTen());
        benhNhan.setSoDienThoai(signupRequest.getSoDienThoai());
        benhNhan.setEmail(signupRequest.getEmail());
        benhNhan.setNgaySinh(signupRequest.getNgaySinh());
        benhNhanRepository.save(benhNhan);
        logger.info("New patient record created for user: {}", signupRequest.getTenDangNhap());
    }

    /**
     * Refresh token - tạo access token mới từ refresh token
     */
    public String refreshToken(String refreshToken) {
        logger.info("Processing refresh token request");

        try {
            // Validate refresh token
            if (!jwtUtils.validateJwtToken(refreshToken)) {
                logger.warn("Invalid refresh token provided");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token không hợp lệ!");
            }

            // Lấy username từ refresh token
            String username = jwtUtils.getUserNameFromJwtToken(refreshToken);

            // Tìm user trong database
            NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(username)
                    .orElseThrow(() -> {
                        logger.warn("User not found for refresh token: {}", username);
                        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Người dùng không tồn tại!");
                    });

            // Tạo access token mới
            String newAccessToken = jwtUtils.generateTokenFromUsername(username);

            logger.info("Access token refreshed successfully for user: {}", username);
            return newAccessToken;

        } catch (Exception e) {
            logger.error("Error refreshing token: " + e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Lỗi refresh token!");
        }
    }

    /**
     * Validate token - kiểm tra token có hợp lệ không
     */
    public TokenValidationResponse validateToken(String token) {
        logger.info("Processing token validation request");

        try {
            // Kiểm tra token có hợp lệ không
            if (!jwtUtils.validateJwtToken(token)) {
                logger.info("Token validation failed: invalid token");
                return new TokenValidationResponse(false, "Token không hợp lệ hoặc đã hết hạn");
            }

            // Lấy username từ token
            String username = jwtUtils.getUserNameFromJwtToken(token);

            // Kiểm tra user có tồn tại trong database không
            boolean userExists = nguoiDungRepository.existsByTenDangNhap(username);

            if (!userExists) {
                logger.warn("Token validation failed: user not found - {}", username);
                return new TokenValidationResponse(false, "Người dùng không tồn tại");
            }

            logger.info("Token validation successful for user: {}", username);
            return new TokenValidationResponse(true, "Token hợp lệ", username);

        } catch (Exception e) {
            logger.error("Error validating token: " + e.getMessage(), e);
            return new TokenValidationResponse(false, "Lỗi kiểm tra token: " + e.getMessage());
        }
    }
}