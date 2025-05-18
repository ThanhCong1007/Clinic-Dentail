package com.example.ClinicDentail.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth/test")
public class TestAuthController {

    private static final Logger logger = LoggerFactory.getLogger(TestAuthController.class);

    @GetMapping("/public")
    public ResponseEntity<?> publicEndpoint() {
        logger.info("Public endpoint accessed successfully");
        return ResponseEntity.ok("Public endpoint is working correctly");
    }

    @PostMapping("/signup-test")
    public ResponseEntity<?> testSignup(@RequestBody TestRequest request) {
        logger.info("Test signup endpoint accessed with data: {}", request);
        return ResponseEntity.ok("Test signup endpoint working correctly with data: " + request.getMessage());
    }

    // Class đơn giản để nhận dữ liệu test
    public static class TestRequest {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "TestRequest{message='" + message + "'}";
        }
    }
}