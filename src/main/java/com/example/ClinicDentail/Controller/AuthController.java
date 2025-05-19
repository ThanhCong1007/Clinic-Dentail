package com.example.ClinicDentail.Controller;

import com.example.ClinicDentail.Enity.BacSi;
import com.example.ClinicDentail.Enity.BenhNhan;
import com.example.ClinicDentail.Enity.NguoiDung;
import com.example.ClinicDentail.Enity.VaiTro;
import com.example.ClinicDentail.Repository.NguoiDungRepository;
import com.example.ClinicDentail.Repository.VaiTroRepository;
import com.example.ClinicDentail.Repository.bacSiRepository;
import com.example.ClinicDentail.Repository.benhNhanRepository;
import com.example.ClinicDentail.Security.Jwt.JwtUtils;
import com.example.ClinicDentail.Security.Service.UserDetailsImpl;
import com.example.ClinicDentail.payload.request.JwtResponse;
import com.example.ClinicDentail.payload.request.LoginRequest;
import com.example.ClinicDentail.payload.request.MessageResponse;
import com.example.ClinicDentail.payload.request.SignupRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller xử lý các yêu cầu xác thực và đăng ký
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
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
    private benhNhanRepository benhNhanRepository;
    @Autowired
    private bacSiRepository bacSiRepository;

    /**
     * Endpoint kiểm tra trạng thái hoạt động của API auth
     * @return thông báo trạng thái
     */
    @GetMapping("/status")
    public ResponseEntity<MessageResponse> getStatus() {
        logger.info("Auth API status check");
        return ResponseEntity.ok(new MessageResponse("Auth API is running"));
    }

    /**
     * Xử lý yêu cầu đăng nhập
     * @param loginRequest Thông tin đăng nhập
     * @return JWT token và thông tin người dùng
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("Processing login request for user: {}", loginRequest.getTenDangNhap());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getTenDangNhap(), loginRequest.getMatKhau()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // Lấy thông tin người dùng
        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy người dùng"));

        logger.info("Login successful for user: {}", loginRequest.getTenDangNhap());

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                nguoiDung.getHoTen(),
                roles));
    }
    /**
     * Xử lý yêu cầu đăng xuất
     *
//     * @param request Đối tượng HttpServletRequest để lấy token từ header
     * @return Thông báo kết quả đăng xuất
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        logger.info("Processing logout request");

        // Kiểm tra authorization header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            // Nếu bạn muốn thêm token vào blacklist, hãy thực hiện ở đây
            // Ví dụ: jwtUtils.addToBlacklist(jwt);

            // Xóa thông tin xác thực khỏi context
            SecurityContextHolder.clearContext();

            logger.info("User logged out successfully");
            return ResponseEntity.ok(new MessageResponse("Đăng xuất thành công!"));
        }

        logger.warn("Logout failed: No valid token provided");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse("Đăng xuất thất bại: Không tìm thấy token hợp lệ!"));
    }



//    @PostMapping("/register")
//    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
//        logger.info("Processing registration request for user: {}", signupRequest.getTenDangNhap());
//
//        // Kiểm tra username đã tồn tại chưa
//        if (nguoiDungRepository.existsByTenDangNhap(signupRequest.getTenDangNhap())) {
//            logger.warn("Registration failed: Username {} is already taken", signupRequest.getTenDangNhap());
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Lỗi: Tên đăng nhập đã được sử dụng!"));
//        }
//
//        // Kiểm tra email đã tồn tại chưa
//        if (nguoiDungRepository.existsByEmail(signupRequest.getEmail())) {
//            logger.warn("Registration failed: Email {} is already in use", signupRequest.getEmail());
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Lỗi: Email đã được sử dụng!"));
//        }
//
//        // Kiểm tra vai trò hợp lệ không
//        VaiTro vaiTro = vaiTroRepository.findByTenVaiTro(signupRequest.getVaiTro())
//                .orElseThrow(() -> {
//                    logger.error("Registration failed: Role {} not found", signupRequest.getVaiTro());
//                    return new RuntimeException("Lỗi: Vai trò không tồn tại!");
//                });
//
//        // Tạo tài khoản người dùng mới
//        NguoiDung nguoiDung = new NguoiDung();
//        nguoiDung.setTenDangNhap(signupRequest.getTenDangNhap());
//        nguoiDung.setMatKhau(encoder.encode(signupRequest.getMatKhau()));
//        nguoiDung.setEmail(signupRequest.getEmail());
//        nguoiDung.setHoTen(signupRequest.getHoTen());
//        nguoiDung.setSoDienThoai(signupRequest.getSoDienThoai());
//        nguoiDung.setTrangThaiHoatDong(true);
//        nguoiDung.setVaiTro(vaiTro);
//
//        // Lưu thông tin người dùng vào database
//        NguoiDung savedNguoiDung = nguoiDungRepository.save(nguoiDung);
//
//        // Xử lý dữ liệu phụ thuộc vào vai trò
//        String tenVaiTro = vaiTro.getTenVaiTro();
//
//        if ("BACSI".equals(tenVaiTro)) {
//            // Tạo thông tin bác sĩ
//            BacSi bacSi = new BacSi();
//            bacSi.setNguoiDung(savedNguoiDung);  // Thiết lập mối quan hệ với đối tượng NguoiDung
//            bacSi.setChuyenKhoa(signupRequest.getChuyenKhoa());
//            bacSi.setSoNamKinhNghiem(signupRequest.getSoNamKinhNghiem());
//            bacSi.setTrangThaiLamViec(true);
//
//            bacSiRepository.save(bacSi);
//            logger.info("Doctor information created for user: {}", signupRequest.getTenDangNhap());
//        } else if ("USER".equals(tenVaiTro)) {
//            // Tạo thông tin bệnh nhân
//            BenhNhan benhNhan = new BenhNhan();
//            benhNhan.setNguoiDung(savedNguoiDung);  // Thiết lập mối quan hệ với đối tượng NguoiDung
//            benhNhan.setHoTen(signupRequest.getHoTen());
//            benhNhan.setNgaySinh(signupRequest.getNgaySinh());
//            benhNhan.setGioiTinh(signupRequest.getGioiTinh());
//            benhNhan.setSoDienThoai(signupRequest.getSoDienThoai());
//            benhNhan.setEmail(signupRequest.getEmail());
//            benhNhan.setDiaChi(signupRequest.getDiaChi());
//            benhNhan.setTienSuBenh(signupRequest.getTienSuBenh());
//            benhNhan.setDiUng(signupRequest.getDiUng());
//
//            benhNhanRepository.save(benhNhan);
//            logger.info("Patient information created for user: {}", signupRequest.getTenDangNhap());
//        }
//
//        logger.info("User registered successfully: {}", signupRequest.getTenDangNhap());
//        return ResponseEntity.ok(new MessageResponse("Đăng ký người dùng thành công!"));
//    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        try {
            logger.info("Processing registration request for user: {}", signupRequest.getTenDangNhap());

            // Kiểm tra username đã tồn tại chưa
            if (nguoiDungRepository.existsByTenDangNhap(signupRequest.getTenDangNhap())) {
                logger.warn("Registration failed: Username {} is already taken", signupRequest.getTenDangNhap());
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Lỗi: Tên đăng nhập đã được sử dụng!"));
            }

            // Kiểm tra email đã tồn tại chưa
            if (nguoiDungRepository.existsByEmail(signupRequest.getEmail())) {
                logger.warn("Registration failed: Email {} is already in use", signupRequest.getEmail());
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Lỗi: Email đã được sử dụng!"));
            }

            // Lấy vai trò USER mặc định
            VaiTro vaiTro = vaiTroRepository.findByTenVaiTro("USER")
                    .orElseThrow(() -> {
                        logger.error("Registration failed: Default USER role not found");
                        return new RuntimeException("Lỗi: Vai trò mặc định không tồn tại!");
                    });

            // Tạo tài khoản người dùng mới
            NguoiDung nguoiDung = new NguoiDung();
            nguoiDung.setTenDangNhap(signupRequest.getTenDangNhap());
            nguoiDung.setMatKhau(encoder.encode(signupRequest.getMatKhau()));
            nguoiDung.setEmail(signupRequest.getEmail());
            nguoiDung.setHoTen(signupRequest.getHoTen());
            nguoiDung.setSoDienThoai(signupRequest.getSoDienThoai());
            nguoiDung.setTrangThaiHoatDong(true);
            nguoiDung.setVaiTro(vaiTro);
            // Ngày tạo sẽ được tự động thiết lập bởi @PrePersist

            // Lưu thông tin người dùng vào database
            NguoiDung savedNguoiDung = nguoiDungRepository.save(nguoiDung);
            logger.info("User registered successfully: {}", signupRequest.getTenDangNhap());

            try {
                // Tạo thông tin bệnh nhân với thông tin cơ bản
                BenhNhan benhNhan = new BenhNhan();
                benhNhan.setNguoiDung(savedNguoiDung);
                benhNhan.setHoTen(signupRequest.getHoTen());
                benhNhan.setSoDienThoai(signupRequest.getSoDienThoai());
                benhNhan.setEmail(signupRequest.getEmail());

                benhNhanRepository.save(benhNhan);
                logger.info("Basic patient information created for user: {}", signupRequest.getTenDangNhap());
            } catch (Exception ex) {
                // Log lỗi nhưng vẫn tiếp tục vì người dùng đã được tạo thành công
                logger.warn("Error creating patient record: " + ex.getMessage(), ex);
                // Không trả về lỗi cho client vì việc đăng ký người dùng vẫn thành công
            }

            return ResponseEntity.ok(new MessageResponse("Đăng ký người dùng thành công!"));
        } catch (Exception e) {
            logger.error("Registration error: " + e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi đăng ký: " + e.getMessage()));
        }
    }

}