package com.example.ClinicDentail.Service;

import com.example.ClinicDentail.DTO.UserDTO;
import com.example.ClinicDentail.Enity.BacSi;
import com.example.ClinicDentail.Enity.BenhNhan;
import com.example.ClinicDentail.Repository.BacSiRepository;
import com.example.ClinicDentail.Repository.BenhNhanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BacSiService {

    private static final Logger logger = LoggerFactory.getLogger(BacSiService.class);

    @Autowired
    private BenhNhanRepository benhNhanRepository;

    @Autowired
    private BacSiRepository bacSiRepository;

    @Autowired
    private UserDTOConverter userDTOConverter;

    /**
     * Lấy thông tin chi tiết bệnh nhân theo ID với kiểm tra quyền truy cập
     * @param id ID của bệnh nhân
     * @param authentication thông tin xác thực của người dùng hiện tại
     * @return UserDTO chứa thông tin bệnh nhân
     */
    public UserDTO getPatientById(Integer id, Authentication authentication) {
        logger.info("Getting patient details for ID: {} by user: {}", id, authentication.getName());

        try {
            BenhNhan benhNhan = benhNhanRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Bệnh nhân không tồn tại!"));

            // Kiểm tra quyền truy cập cho USER
            if (isUserRole(authentication)) {
                validateUserAccess(authentication, benhNhan);
            }

            return new UserDTO(benhNhan);

        } catch (RuntimeException e) {
            logger.error("Error getting patient by ID {}: {}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error getting patient by ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Không thể lấy thông tin bệnh nhân!");
        }
    }

    /**
     * Lấy thông tin chi tiết bác sĩ theo ID
     * @param id ID của bác sĩ
     * @return UserDTO chứa thông tin bác sĩ
     */
    public UserDTO getDoctorById(Integer id) {
        logger.info("Getting doctor details for ID: {}", id);

        try {
            BacSi bacSi = bacSiRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Bác sĩ không tồn tại!"));

            return new UserDTO(bacSi);

        } catch (RuntimeException e) {
            logger.error("Error getting doctor by ID {}: {}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error getting doctor by ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Không thể lấy thông tin bác sĩ!");
        }
    }

    /**
     * Kiểm tra xem người dùng hiện tại có role USER không
     * @param authentication thông tin xác thực
     * @return true nếu có role USER
     */
    private boolean isUserRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));
    }

    /**
     * Kiểm tra quyền truy cập của USER - chỉ được xem thông tin của chính mình
     * @param authentication thông tin xác thực
     * @param benhNhan bệnh nhân cần kiểm tra
     * @throws RuntimeException nếu không có quyền truy cập
     */
    private void validateUserAccess(Authentication authentication, BenhNhan benhNhan) {
        String currentUsername = authentication.getName();
        if (!benhNhan.getNguoiDung().getTenDangNhap().equals(currentUsername)) {
            logger.warn("User {} attempted to access patient info of {}",
                    currentUsername, benhNhan.getNguoiDung().getTenDangNhap());
            throw new SecurityException("Bạn chỉ có thể xem thông tin của chính mình!");
        }
    }
    /**
     * Lấy danh sách tất cả bác sĩ đang hoạt động
     */
    public List<UserDTO> getAllActiveDoctors() {
        try {
            List<BacSi> bacSiList = bacSiRepository.findByTrangThaiLamViecTrueAndNguoiDung_TrangThaiHoatDongTrue();
            return bacSiList.stream()
                    .map(UserDTO::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy danh sách bác sĩ: " + e.getMessage());
        }
    }

    /**
     * Lấy danh sách tất cả chuyên khoa
     */
    public List<String> getAllSpecialties() {
        try {
            return bacSiRepository.findDistinctChuyenKhoaByTrangThaiLamViecTrueAndNguoiDung_TrangThaiHoatDongTrue();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy danh sách chuyên khoa: " + e.getMessage());
        }
    }

}