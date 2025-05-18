package com.example.ClinicDentail.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Integer id;
    private String tenDangNhap;
    private String email;
    private String hoTen;
    private List<String> roles;

    public JwtResponse(String token, Integer id, String tenDangNhap, String email, String hoTen, List<String> roles) {
        this.token = token;
        this.id = id;
        this.tenDangNhap = tenDangNhap;
        this.email = email;
        this.hoTen = hoTen;
        this.roles = roles;
    }
}