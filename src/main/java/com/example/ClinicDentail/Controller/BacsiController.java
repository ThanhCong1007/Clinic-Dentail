package com.example.ClinicDentail.Controller;

import com.example.ClinicDentail.DTO.UserDTO;
import com.example.ClinicDentail.Service.BacSiService;
import com.example.ClinicDentail.payload.request.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/bacsi")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BacsiController {

    private static final Logger logger = LoggerFactory.getLogger(BacsiController.class);

    @Autowired
    private BacSiService bacSiService;

    /**
     * Xem tất cả bệnh nhân
     * @param page số trang
     * @param size kích thước trang
     * @param sortBy trường sắp xếp
     * @param sortDir hướng sắp xếp
     * @return ResponseEntity chứa danh sách bệnh nhân
     */
    @GetMapping("/patients")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BACSI')")
    public ResponseEntity<?> getAllPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "hoTen") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        try {
            Map<String, Object> response = bacSiService.getAllPatients(page, size, sortBy, sortDir);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Controller error getting all patients: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi: " + e.getMessage()));
        }
    }

    /**
     * Xem thông tin chi tiết bệnh nhân theo ID
     * @param id ID của bệnh nhân
     * @param authentication thông tin xác thực
     * @return ResponseEntity chứa thông tin bệnh nhân
     */
    @GetMapping("/patients/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BACSI') or hasRole('USER')")
    public ResponseEntity<?> getPatientById(@PathVariable Integer id, Authentication authentication) {

        try {
            UserDTO patientDTO = bacSiService.getPatientById(id, authentication);
            return ResponseEntity.ok(patientDTO);

        } catch (SecurityException e) {
            logger.warn("Access denied for patient ID {}: {}", id, e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse("Lỗi: " + e.getMessage()));

        } catch (RuntimeException e) {
            logger.error("Runtime error getting patient by ID {}: {}", id, e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Lỗi: " + e.getMessage()));

        } catch (Exception e) {
            logger.error("Unexpected error getting patient by ID {}: {}", id, e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi: Không thể lấy thông tin bệnh nhân!"));
        }
    }
}