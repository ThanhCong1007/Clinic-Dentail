package com.example.ClinicDentail.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserDTO {
    // Thông tin cơ bản từ bảng nguoi_dung
    private Integer maNguoiDung;
    private String tenDangNhap;
    private String email;
    private String hoTen;
    private String soDienThoai;
    private Boolean trangThaiHoatDong;
    private LocalDateTime ngayTao;
    private Integer maVaiTro;
    private String tenVaiTro;

    // Thông tin bác sĩ (chỉ có khi role = BACSI)
    private Integer maBacSi;
    private String chuyenKhoa;
    private Integer soNamKinhNghiem;
    private Boolean trangThaiLamViec;

    // Thông tin bệnh nhân (chỉ có khi role = USER)
    private Integer maBenhNhan;
    private LocalDate ngaySinh;
    private String gioiTinh;
    private String diaChi;
    private String tienSuBenh;
    private String diUng;
    private Integer tuoi; // Tính từ ngày sinh
}