package com.example.ClinicDentail.Controller;

import com.example.ClinicDentail.DTO.UserDTO;
import com.example.ClinicDentail.Service.UserService;
import com.example.ClinicDentail.payload.request.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    /**
     * Xem thông tin tài khoản của người dùng hiện tại (hỗ trợ tất cả các role)
     * @return Thông tin chi tiết tài khoản theo role
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getAccountInfo() {
        try {
            UserDTO userInfo = userService.getCurrentUserProfile();
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
     * Cập nhật thông tin tài khoản của người dùng hiện tại
     * @param userDTO Thông tin cần cập nhật
     * @return Thông tin tài khoản sau khi cập nhật
     */
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody UserDTO userDTO) {
        try {
            UserDTO updatedUserInfo = userService.updateCurrentUserProfile(userDTO);
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
}