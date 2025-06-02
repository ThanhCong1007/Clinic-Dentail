package com.example.ClinicDentail.Controller;

import com.example.ClinicDentail.DTO.UserDTO;
import com.example.ClinicDentail.Service.AdminService;
import com.example.ClinicDentail.Service.BacSiService;
import com.example.ClinicDentail.payload.request.MessageResponse;
import com.example.ClinicDentail.payload.request.SignupRequest;
import jakarta.validation.Valid;
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
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;
    @Autowired
    private BacSiService bacSiService;

    /**
     * API thêm mới người dùng vào hệ thống (chỉ dành cho ADMIN).
     * @param signupRequest Thông tin đăng ký người dùng, bao gồm tên đăng nhập, mật khẩu, email, họ tên, số điện thoại, vai trò, v.v.
     * @return {@link ResponseEntity} chứa thông báo thành công hoặc lỗi nếu có
     */
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        try {
            adminService.registerUser(signupRequest);
            return ResponseEntity.ok(new MessageResponse("Đăng ký người dùng thành công!"));
        } catch (RuntimeException e) {
            logger.error("Error during user registration: {}", e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error during user registration: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi: Không thể đăng ký người dùng!"));
        }
    }

    /**
     * Vô hiệu hóa người dùng
     * @param id ID của người dùng cần vô hiệu hóa
     * @return ResponseEntity chứa thông báo thành công hoặc lỗi tương ứng
     */
    @PutMapping("/deactivate/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deactivateUser(@PathVariable Integer id) {
        try {
            adminService.deactivateUser(id);
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
     * @param page trang hiện tại
     * @param size số lượng item trên mỗi trang
     * @param sortBy trường sắp xếp
     * @param sortDir hướng sắp xếp (asc/desc)
     * @return ResponseEntity chứa danh sách người dùng và thông tin phân trang
     */
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "maNguoiDung") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        try {
            Map<String, Object> response = adminService.getAllUsers(page, size, sortBy, sortDir);
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
     * @param roleName tên vai trò
     * @param page trang hiện tại
     * @param size số lượng item trên mỗi trang
     * @return ResponseEntity chứa danh sách người dùng theo vai trò
     */
    @GetMapping("/users/role/{roleName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUsersByRole(
            @PathVariable String roleName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            Map<String, Object> response = adminService.getUsersByRole(roleName, page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting users by role {}: {}", roleName, e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi: Không thể lấy danh sách người dùng theo vai trò!"));
        }
    }

    /**
     * Xem thông tin chi tiết bệnh nhân theo ID
     * @param id ID của bệnh nhân
     * @param authentication thông tin xác thực
     * @return ResponseEntity chứa thông tin bệnh nhân
     */
    @GetMapping("/patients/{id}")
    @PreAuthorize("hasRole('ADMIN')")
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