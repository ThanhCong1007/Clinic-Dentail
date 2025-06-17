package com.example.ClinicDentail.DTO;

import com.example.ClinicDentail.Enity.BenhAn;
import com.example.ClinicDentail.Enity.BenhNhan;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BenhAnDTO {

    private Integer nguoiDung;
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
//        this.nguoiDung = benhAn.getNguoiDung();
        if (benhAn.getLichHen() != null) {
            this.maLichHen = benhAn.getLichHen().getMaLichHen();
        }

        // Bệnh nhân
        if (benhAn.getBenhNhan() != null) {
            BenhNhan benhNhan = benhAn.getBenhNhan();
            if (benhNhan.getNguoiDung() != null) {
                this.nguoiDung = benhNhan.getNguoiDung().getMaNguoiDung();
            }
            var bn = benhAn.getBenhNhan();
            this.maBenhNhan = bn.getMaBenhNhan();
            this.tenBenhNhan = bn.getHoTen();  // Tên rút gọn
            this.hoTen = bn.getHoTen();        // Họ tên đầy đủ (cùng dữ liệu nếu không tách)
            this.soDienThoai = bn.getSoDienThoai();
            this.ngaySinh = bn.getNgaySinh();
            this.gioiTinh = (bn.getGioiTinh() != null) ? bn.getGioiTinh().toString() : null;
            this.email = bn.getEmail();
            this.diaChi = bn.getDiaChi();
            this.tienSuBenh = bn.getTienSuBenh();
            this.diUng = bn.getDiUng();
        }

        // Bác sĩ
        if (benhAn.getBacSi() != null && benhAn.getBacSi().getNguoiDung() != null) {
            this.maBacSi = benhAn.getBacSi().getMaBacSi();
            this.tenBacSi = benhAn.getBacSi().getNguoiDung().getHoTen();
        }

        // Khám bệnh
        this.lyDoKham = benhAn.getLyDoKham();
        this.chanDoan = benhAn.getChanDoan();
        this.ghiChuDieuTri = benhAn.getGhiChuDieuTri();
        this.ngayTaiKham = benhAn.getNgayTaiKham();
    }
}