package com.example.ClinicDentail.Service;

import com.example.ClinicDentail.Controller.AuthUtils;
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

        // Tạo thông tin bệnh nhân
        createBenhNhan(signupRequest, savedNguoiDung);
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
     * Tạo thông tin bệnh nhân cho người dùng mới
     */
    private void createBenhNhan(SignupRequest signupRequest, NguoiDung nguoiDung) {
        try {
            BenhNhan benhNhan = new BenhNhan();
            benhNhan.setNguoiDung(nguoiDung);
            benhNhan.setHoTen(signupRequest.getHoTen());
            benhNhan.setSoDienThoai(signupRequest.getSoDienThoai());
            benhNhan.setEmail(signupRequest.getEmail());
            benhNhan.setNgaySinh(signupRequest.getNgaySinh());
            benhNhanRepository.save(benhNhan);
            logger.info("Basic patient information created for user: {}", signupRequest.getTenDangNhap());
        } catch (Exception ex) {
            // Log lỗi nhưng vẫn tiếp tục vì người dùng đã được tạo thành công
            logger.warn("Error creating patient record: " + ex.getMessage(), ex);
        }
    }
}