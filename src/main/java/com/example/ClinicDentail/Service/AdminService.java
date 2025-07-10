package com.example.ClinicDentail.Service;

import com.example.ClinicDentail.DTO.UserDTO;
import com.example.ClinicDentail.Enity.BacSi;
import com.example.ClinicDentail.Enity.BenhNhan;
import com.example.ClinicDentail.Enity.NguoiDung;
import com.example.ClinicDentail.Enity.VaiTro;
import com.example.ClinicDentail.Repository.BacSiRepository;
import com.example.ClinicDentail.Repository.BenhNhanRepository;
import com.example.ClinicDentail.Repository.NguoiDungRepository;
import com.example.ClinicDentail.Repository.VaiTroRepository;
import com.example.ClinicDentail.payload.request.SignupRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

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
     * Đăng ký người dùng mới
     */
    @Transactional
    public void registerUser(SignupRequest signupRequest) {
        logger.info("Processing registration request for user: {}", signupRequest.getTenDangNhap());

        // Kiểm tra username đã tồn tại chưa
        if (nguoiDungRepository.existsByTenDangNhap(signupRequest.getTenDangNhap())) {
            logger.warn("Registration failed: Username {} is already taken", signupRequest.getTenDangNhap());
            throw new RuntimeException("Lỗi: Tên đăng nhập đã được sử dụng!");
        }

        // Kiểm tra email đã tồn tại chưa
        if (nguoiDungRepository.existsByEmail(signupRequest.getEmail())) {
            logger.warn("Registration failed: Email {} is already in use", signupRequest.getEmail());
            throw new RuntimeException("Lỗi: Email đã được sử dụng!");
        }

        // Kiểm tra vai trò hợp lệ không
        VaiTro vaiTro = vaiTroRepository.findByTenVaiTro(signupRequest.getVaiTro())
                .orElseThrow(() -> {
                    logger.error("Registration failed: Role {} not found", signupRequest.getVaiTro());
                    return new RuntimeException("Lỗi: Vai trò không tồn tại!");
                });

        // Tạo tài khoản người dùng mới
        NguoiDung nguoiDung = createNguoiDung(signupRequest, vaiTro);

        // Lưu thông tin người dùng vào database
        NguoiDung savedNguoiDung = nguoiDungRepository.save(nguoiDung);

        // Xử lý dữ liệu phụ thuộc vào vai trò
        handleRoleSpecificData(signupRequest, savedNguoiDung, vaiTro.getTenVaiTro());

        logger.info("User registered successfully: {}", signupRequest.getTenDangNhap());
    }

    /**
     * Vô hiệu hóa người dùng
     */
    @Transactional
    public void deactivateUser(Integer id) {
        logger.info("Processing deactivation request for user ID: {}", id);

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
            deactivateDoctor(nguoiDung);
        }

        logger.info("User deactivated successfully: {}", nguoiDung.getTenDangNhap());
    }

    /**
     * Lấy tất cả người dùng với phân trang
     */
    public List<UserDTO> getAllUsers() {
        logger.info("Getting all users (no pagination)");

        List<NguoiDung> users = nguoiDungRepository.findAll(Sort.by("maNguoiDung").ascending()); // Có thể sửa sort nếu muốn
        List<UserDTO> userDTOs = users.stream()
                .map(userDTOConverter::convertToDTO)
                .collect(Collectors.toList());

        return userDTOs;
    }

    /**
     * Lấy người dùng theo vai trò
     */
    public Map<String, Object> getUsersByRole(String roleName, int page, int size) {
        logger.info("Getting users by role: {} - page: {}, size: {}", roleName, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<NguoiDung> usersPage = nguoiDungRepository.findByVaiTro_TenVaiTro(roleName, pageable);
        Page<UserDTO> userDTOPage = userDTOConverter.convertToDTO(usersPage);

        Map<String, Object> response = new HashMap<>();
        response.put("users", userDTOPage.getContent());
        response.put("role", roleName);
        response.put("currentPage", userDTOPage.getNumber());
        response.put("totalItems", userDTOPage.getTotalElements());
        response.put("totalPages", userDTOPage.getTotalPages());

        return response;
    }

    /**
     * Lấy tất cả bác sĩ
     */
    public Map<String, Object> getAllDoctors(int page, int size, String sortBy, String sortDir) {
        logger.info("Getting all doctors - page: {}, size: {}, sortBy: {}, sortDir: {}",
                page, size, sortBy, sortDir);

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

        return response;
    }

    // Private helper methods

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

    private void handleRoleSpecificData(SignupRequest signupRequest, NguoiDung savedNguoiDung, String tenVaiTro) {
        if ("BACSI".equals(tenVaiTro)) {
            createDoctorInfo(signupRequest, savedNguoiDung);
        } else if ("USER".equals(tenVaiTro)) {
            createPatientInfo(signupRequest, savedNguoiDung);
        }
    }

    private void createDoctorInfo(SignupRequest signupRequest, NguoiDung savedNguoiDung) {
        BacSi bacSi = new BacSi();
        bacSi.setNguoiDung(savedNguoiDung);
        bacSi.setChuyenKhoa(signupRequest.getChuyenKhoa());
        bacSi.setSoNamKinhNghiem(signupRequest.getSoNamKinhNghiem());
        bacSi.setTrangThaiLamViec(true);

        bacSiRepository.save(bacSi);
        logger.info("Doctor information created for user: {}", signupRequest.getTenDangNhap());
    }

    private void createPatientInfo(SignupRequest signupRequest, NguoiDung savedNguoiDung) {
        BenhNhan benhNhan = new BenhNhan();
        benhNhan.setNguoiDung(savedNguoiDung);
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

    private void deactivateDoctor(NguoiDung nguoiDung) {
        Optional<BacSi> bacSiOpt = bacSiRepository.findByNguoiDung(nguoiDung);
        if (bacSiOpt.isPresent()) {
            BacSi bacSi = bacSiOpt.get();
            bacSi.setTrangThaiLamViec(false);
            bacSiRepository.save(bacSi);
        }
    }
}