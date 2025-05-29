package com.example.ClinicDentail.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ThamKhamResponseDTO {
    private Integer maBenhAn;
    private Integer maBenhNhan;
    private String tenBenhNhan;
    private String soDienThoai;
    private String lyDoKham;
    private String chanDoan;
    private String ghiChuDieuTri;
    private LocalDate ngayTaiKham;
    private Integer maLichHenMoi; // Nếu có tạo lịch hẹn mới
    private String thongBao;
}
