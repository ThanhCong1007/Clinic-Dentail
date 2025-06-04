package com.example.ClinicDentail.DTO;

import com.example.ClinicDentail.Enity.BenhAn;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
public class BenhAnDTO {
    // Thông tin bệnh án
    private Integer maBenhAn;
    private LocalDateTime ngayTao;

    // Thông tin lịch hẹn
    private Integer maLichHen;

    // Thông tin bác sĩ
    private Integer maBacSi;
    private String tenBacSi;

    // Thông tin bệnh nhân
    private Integer maBenhNhan;
    private String tenBenhNhan;
    private String soDienThoai;

    // Thông tin bệnh nhân vãng lai (khi không có tài khoản)
    private String hoTen;
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

    // Lịch hẹn mới (sau khám nếu có)
    private Integer maDichVu;
    private LocalDate ngayHenMoi;
    private LocalTime gioBatDauMoi;
    private LocalTime gioKetThucMoi;
    private String ghiChuLichHen;
    private Integer maLichHenMoi;

    // *** THÊM THÔNG TIN ĐON THUỐC ***
    private String maIcd;
    private String moTaChanDoan;
    private String ghiChuDonThuoc;
    private List<ChiTietDonThuocDTO> danhSachThuoc;
    private Integer maDonThuoc; // Mã đơn thuốc được tạo (response)

    // Thông báo kết quả xử lý nếu cần
    private String thongBao;

    // Constructor từ Entity (dùng cho hiển thị)
    public BenhAnDTO(BenhAn benhAn) {
        this.maBenhAn = benhAn.getMaBenhAn();
        this.ngayTao = benhAn.getNgayTao();

        if (benhAn.getLichHen() != null) {
            this.maLichHen = benhAn.getLichHen().getMaLichHen();
        }

        if (benhAn.getBenhNhan() != null) {
            this.maBenhNhan = benhAn.getBenhNhan().getMaBenhNhan();
            this.tenBenhNhan = benhAn.getBenhNhan().getHoTen();
            this.soDienThoai = benhAn.getBenhNhan().getSoDienThoai();
        }

        if (benhAn.getBacSi() != null) {
            this.maBacSi = benhAn.getBacSi().getMaBacSi();
            if (benhAn.getBacSi().getNguoiDung() != null) {
                this.tenBacSi = benhAn.getBacSi().getNguoiDung().getHoTen();
            }
        }

        this.lyDoKham = benhAn.getLyDoKham();
        this.chanDoan = benhAn.getChanDoan();
        this.ghiChuDieuTri = benhAn.getGhiChuDieuTri();
        this.ngayTaiKham = benhAn.getNgayTaiKham();
    }
}