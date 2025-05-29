package com.example.ClinicDentail.DTO;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ThamKhamRequestDTO {
    // Thông tin chung
    private Integer maLichHen; // Null nếu là khách vãng lai
    private Integer maBacSi;

    // Thông tin bệnh nhân (cho khách vãng lai)
    private String hoTen;
    private String soDienThoai;
    private LocalDate ngaySinh;
    private String gioiTinh;
    private String email;
    private String diaChi;
    private String tienSuBenh;
    private String diUng;

    // Thông tin khám bệnh
    private String lyDoKham;
    private String chanDoan;
    private String ghiChuDieuTri;
    private LocalDate ngayTaiKham;

    // Thông tin lịch hẹn mới (nếu cần)
    private Integer maDichVu;
    private LocalDate ngayHenMoi;
    private LocalTime gioBatDauMoi;
    private LocalTime gioKetThucMoi;
    private String ghiChuLichHen;
}
