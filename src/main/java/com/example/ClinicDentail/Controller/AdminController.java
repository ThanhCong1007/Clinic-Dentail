package com.example.ClinicDentail.Controller;

import com.example.ClinicDentail.DTO.UserDTO;
import com.example.ClinicDentail.Enity.BacSi;
import com.example.ClinicDentail.Enity.BenhNhan;
import com.example.ClinicDentail.Enity.NguoiDung;
import com.example.ClinicDentail.Enity.VaiTro;
import com.example.ClinicDentail.Repository.BacSiRepository;
import com.example.ClinicDentail.Repository.BenhNhanRepository;
import com.example.ClinicDentail.Repository.NguoiDungRepository;
import com.example.ClinicDentail.Repository.VaiTroRepository;
import com.example.ClinicDentail.Security.Jwt.JwtUtils;
import com.example.ClinicDentail.Service.UserDTOConverter;
import com.example.ClinicDentail.payload.request.MessageResponse;
import com.example.ClinicDentail.payload.request.SignupRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private VaiTroRepository vaiTroRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private BenhNhanRepository benhNhanRepository;

    @Autowired
    private BacSiRepository bacSiRepository;

    @Autowired
    private UserDTOConverter userDTOConverter;
    /**
     * API thêm mới người dùng vào hệ thống (chỉ dành cho ADMIN).
     * @param signupRequest Thông tin đăng ký người dùng, bao gồm tên đăng nhập, mật khẩu, email, họ tên, số điện thoại, vai trò, v.v.
     * @return {@link ResponseEntity} chứa thông báo thành công hoặc lỗi nếu có
     */
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
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

        // Kiểm tra vai trò hợp lệ không
        VaiTro vaiTro = vaiTroRepository.findByTenVaiTro(signupRequest.getVaiTro())
                .orElseThrow(() -> {
                    logger.error("Registration failed: Role {} not found", signupRequest.getVaiTro());
                    return new RuntimeException("Lỗi: Vai trò không tồn tại!");
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

        // Lưu thông tin người dùng vào database
        NguoiDung savedNguoiDung = nguoiDungRepository.save(nguoiDung);

        // Xử lý dữ liệu phụ thuộc vào vai trò
        String tenVaiTro = vaiTro.getTenVaiTro();

        if ("BACSI".equals(tenVaiTro)) {
            // Tạo thông tin bác sĩ
            BacSi bacSi = new BacSi();
            bacSi.setNguoiDung(savedNguoiDung);  // Thiết lập mối quan hệ với đối tượng NguoiDung
            bacSi.setChuyenKhoa(signupRequest.getChuyenKhoa());
            bacSi.setSoNamKinhNghiem(signupRequest.getSoNamKinhNghiem());
            bacSi.setTrangThaiLamViec(true);

            bacSiRepository.save(bacSi);
            logger.info("Doctor information created for user: {}", signupRequest.getTenDangNhap());
        } else if ("USER".equals(tenVaiTro)) {
            // Tạo thông tin bệnh nhân
            BenhNhan benhNhan = new BenhNhan();
            benhNhan.setNguoiDung(savedNguoiDung);  // Thiết lập mối quan hệ với đối tượng NguoiDung
            benhNhan.setHoTen(signupRequest.getHoTen());
            benhNhan.setNgaySinh(signupRequest.getNgaySinh());
            benhNhan.setGioiTinh(signupRequest.getGioiTinh());
            benhNhan.setSoDienThoai(signupRequest.getSoDienThoai());
            benhNhan.setEmail(signupRequest.getEmail());
            benhNhan.setDiaChi(signupRequest.getDiaChi());
            benhNhan.setTienSuBenh(signupRequest.getTienSuBenh());
            benhNhan.setDiUng(signupRequest.getDiUng());

            benhNhanRepository.save(benhNhan);
            logger.info("Patient information created for user: {}", signupRequest.getTenDangNhap());
        }

        logger.info("User registered successfully: {}", signupRequest.getTenDangNhap());
        return ResponseEntity.ok(new MessageResponse("Đăng ký người dùng thành công!"));
    }

    /**
     *
     * @param id ID của người dùng cần vô hiệu hóa
     * @return ResponseEntity chứa thông báo thành công hoặc lỗi tương ứng
     */
    @PutMapping("/deactivate/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deactivateUser(@PathVariable Integer id) {
        logger.info("Processing deactivation request for user ID: {}", id);

        try {
            // Kiểm tra người dùng có tồn tại không
            NguoiDung nguoiDung = nguoiDungRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.error("Deactivation failed: User with ID {} not found", id);
                        return new RuntimeException("Lỗi: Người dùng không tồn tại!");
                    });

            // Vô hiệu hóa tài khoản
            nguoiDung.setTrangThaiHoatDong(false);
            nguoiDungRepository.save(nguoiDung);

            // Nếu là bác sĩ, cũng vô hiệu hóa trạng thái làm việc
            if ("BACSI".equals(nguoiDung.getVaiTro().getTenVaiTro())) {
                Optional<BacSi> bacSiOpt = bacSiRepository.findByNguoiDung(nguoiDung);
                if (bacSiOpt.isPresent()) {
                    BacSi bacSi = bacSiOpt.get();
                    bacSi.setTrangThaiLamViec(false);
                    bacSiRepository.save(bacSi);
                }
            }

            logger.info("User deactivated successfully: {}", nguoiDung.getTenDangNhap());
            return ResponseEntity.ok(new MessageResponse("Vô hiệu hóa người dùng thành công!"));

        } catch (RuntimeException e) {
            logger.error("Error during user deactivation: {}", e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error during user deactivation: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi: Không thể vô hiệu hóa người dùng!"));
        }
    }

    /**
     * Xem tất cả người dùng (có phân trang)
     * @param page
     * @param size
     * @param sortBy
     * @param sortDir
     * @return
     */
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "maNguoiDung") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        logger.info("Getting all users - page: {}, size: {}, sortBy: {}, sortDir: {}",
                page, size, sortBy, sortDir);

        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ?
                    Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(page, size, sort);
            Page<NguoiDung> usersPage = nguoiDungRepository.findAll(pageable);
            Page<UserDTO> userDTOPage = userDTOConverter.convertToDTO(usersPage);

            Map<String, Object> response = new HashMap<>();
            response.put("users", userDTOPage.getContent());
            response.put("currentPage", userDTOPage.getNumber());
            response.put("totalItems", userDTOPage.getTotalElements());
            response.put("totalPages", userDTOPage.getTotalPages());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error getting all users: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi: Không thể lấy danh sách người dùng!"));
        }
    }

    /**
     * Xem danh sách người dùng theo vai trò
     * @param roleName
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/users/role/{roleName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUsersByRole(
            @PathVariable String roleName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        logger.info("Getting users by role: {} - page: {}, size: {}", roleName, page, size);

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<NguoiDung> usersPage = nguoiDungRepository.findByVaiTro_TenVaiTro(roleName, pageable);
            Page<UserDTO> userDTOPage = userDTOConverter.convertToDTO(usersPage);

            Map<String, Object> response = new HashMap<>();
            response.put("users", userDTOPage.getContent());
            response.put("role", roleName);
            response.put("currentPage", userDTOPage.getNumber());
            response.put("totalItems", userDTOPage.getTotalElements());
            response.put("totalPages", userDTOPage.getTotalPages());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error getting users by role {}: {}", roleName, e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi: Không thể lấy danh sách người dùng theo vai trò!"));
        }
    }


    /**
     * Xem tất cả bác sĩ
     * @param page
     * @param size
     * @param sortBy
     * @param sortDir
     * @return
     */
    @GetMapping("/doctors")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BACSI') or hasRole('USER')")
    public ResponseEntity<?> getAllDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nguoiDung.hoTen") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        logger.info("Getting all doctors - page: {}, size: {}, sortBy: {}, sortDir: {}",
                page, size, sortBy, sortDir);

        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ?
                    Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(page, size, sort);
            Page<BacSi> doctorsPage = bacSiRepository.findAll(pageable);
            Page<UserDTO> doctorDTOPage = userDTOConverter.convertBacSiToDTO(doctorsPage);

            Map<String, Object> response = new HashMap<>();
            response.put("doctors", doctorDTOPage.getContent());
            response.put("currentPage", doctorDTOPage.getNumber());
            response.put("totalItems", doctorDTOPage.getTotalElements());
            response.put("totalPages", doctorDTOPage.getTotalPages());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error getting all doctors: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi: Không thể lấy danh sách bác sĩ!"));
        }
    }


}
