package com.example.ClinicDentail.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    private String tenDangNhap;

    @NotBlank
    private String matKhau;
}