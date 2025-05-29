package com.example.ClinicDentail.Controller;

import com.example.ClinicDentail.DTO.UserDTO;
import com.example.ClinicDentail.Enity.BacSi;
import com.example.ClinicDentail.Enity.BenhNhan;
import com.example.ClinicDentail.Repository.BacSiRepository;
import com.example.ClinicDentail.Repository.BenhNhanRepository;
import com.example.ClinicDentail.Repository.NguoiDungRepository;
import com.example.ClinicDentail.Repository.VaiTroRepository;
import com.example.ClinicDentail.Service.UserDTOConverter;
import com.example.ClinicDentail.payload.request.MessageResponse;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/bacsi")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BacsiController {
    private static final Logger logger = LoggerFactory.getLogger(BacsiController.class);

    @Autowired
    private BenhNhanRepository benhNhanRepository;

    @Autowired
    private BacSiRepository bacSiRepository;

    @Autowired
    private UserDTOConverter userDTOConverter;

    /**
     * Xem tất cả bệnh nhân
     * @param page
     * @param size
     * @param sortBy
     * @param sortDir
     * @return
     */
    @GetMapping("/patients")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BACSI')")
    public ResponseEntity<?> getAllPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "hoTen") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        logger.info("Getting all patients - page: {}, size: {}, sortBy: {}, sortDir: {}",
                page, size, sortBy, sortDir);

        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ?
                    Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(page, size, sort);
            Page<BenhNhan> patientsPage = benhNhanRepository.findAll(pageable);
            Page<UserDTO> patientDTOPage = userDTOConverter.convertBenhNhanToDTO(patientsPage);

            Map<String, Object> response = new HashMap<>();
            response.put("patients", patientDTOPage.getContent());
            response.put("currentPage", patientDTOPage.getNumber());
            response.put("totalItems", patientDTOPage.getTotalElements());
            response.put("totalPages", patientDTOPage.getTotalPages());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error getting all patients: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi: Không thể lấy danh sách bệnh nhân!"));
        }
    }


    /**
     * Xem thông tin chi tiết bệnh nhân theo ID
     * @param id
     * @param authentication
     * @return
     */
    @GetMapping("/patients/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BACSI') or hasRole('USER')")
    public ResponseEntity<?> getPatientById(@PathVariable Integer id, Authentication authentication) {
        logger.info("Getting patient details for ID: {} by user: {}", id, authentication.getName());

        try {
            BenhNhan benhNhan = benhNhanRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Lỗi: Bệnh nhân không tồn tại!"));

            // Kiểm tra quyền truy cập cho USER
            if (authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))) {
                String currentUsername = authentication.getName();
                if (!benhNhan.getNguoiDung().getTenDangNhap().equals(currentUsername)) {
                    logger.warn("User {} attempted to access patient info of {}",
                            currentUsername, benhNhan.getNguoiDung().getTenDangNhap());
                    return ResponseEntity
                            .status(HttpStatus.FORBIDDEN)
                            .body(new MessageResponse("Lỗi: Bạn chỉ có thể xem thông tin của chính mình!"));
                }
            }

            UserDTO patientDTO = new UserDTO(benhNhan);
            return ResponseEntity.ok(patientDTO);

        } catch (RuntimeException e) {
            logger.error("Error getting patient by ID {}: {}", id, e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error getting patient by ID {}: {}", id, e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi: Không thể lấy thông tin bệnh nhân!"));
        }
    }

    //

    /**
     * Xem thông tin chi tiết bác sĩ theo ID
     * @param id
     * @return
     */
    @GetMapping("/doctors/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BACSI') or hasRole('USER')")
    public ResponseEntity<?> getDoctorById(@PathVariable Integer id) {
        logger.info("Getting doctor details for ID: {}", id);

        try {
            BacSi bacSi = bacSiRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Lỗi: Bác sĩ không tồn tại!"));

            UserDTO doctorDTO = new UserDTO(bacSi);
            return ResponseEntity.ok(doctorDTO);

        } catch (RuntimeException e) {
            logger.error("Error getting doctor by ID {}: {}", id, e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error getting doctor by ID {}: {}", id, e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi: Không thể lấy thông tin bác sĩ!"));
        }
    }


}
