package com.example.ClinicDentail.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import com.example.ClinicDentail.Enity.BenhNhan.GioiTinh;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    private String tenDangNhap;

    @NotBlank
    @Size(min = 6, max = 40)
    private String matKhau;

    @NotBlank
    @Size(max = 100)
    @Email
    private String email;

    @NotBlank
    @Size(min = 3, max = 100)
    private String hoTen;

    @Size(max = 20)
    private String soDienThoai;

    @NotBlank
    private String vaiTro;

    // Thông tin cho bác sĩ
    private String chuyenKhoa;
    private Integer soNamKinhNghiem;

    // Thông tin cho bệnh nhân
    private LocalDate ngaySinh;
    private GioiTinh gioiTinh;
    private String diaChi;
    private String tienSuBenh;
    private String diUng;
}
