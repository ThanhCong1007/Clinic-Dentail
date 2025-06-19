package com.example.ClinicDentail.DTO;

import com.example.ClinicDentail.Enity.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhamBenhDTO {

    // Thông tin bệnh nhân
    private Integer maBenhNhan;
    private String hoTen;
    private String soDienThoai;
    private String email;
    private String diaChi;
    private LocalDate ngaySinh;
    private String gioiTinh;
    private String tienSuBenh;
    private String diUng;

    public KhamBenhDTO(BenhNhan benhNhan) {
        this.maBenhNhan = benhNhan.getMaBenhNhan();
        this.hoTen = benhNhan.getHoTen();
        this.soDienThoai = benhNhan.getSoDienThoai();
        this.email = benhNhan.getEmail();
        this.diaChi = benhNhan.getDiaChi();
        this.ngaySinh = benhNhan.getNgaySinh();
        this.gioiTinh = benhNhan.getGioiTinh().toString();
        this.tienSuBenh = benhNhan.getTienSuBenh();
        this.diUng = benhNhan.getDiUng();
    }
    // Thông tin khám bệnh
    private Integer maLichHen;
    private Integer maBacSi;
    private String tenBacSi;
    private String lyDoKham;
    private String chanDoan;
    private String ghiChuDieuTri;
    private LocalDate ngayTaiKham;
    private List<DichVuDTO> danhSachDichVu;

    // Thông tin đơn thuốc
    private List<ChiTietThuocDTO> danhSachThuoc;
    private String ghiChuDonThuoc;

    // Thông tin phản hồi
    private Integer maBenhAn;
    private Integer maDonThuoc;
    private Integer maHoaDon;
    private BigDecimal tongTien;
    private String trangThaiKham;
    private LocalDateTime ngayKham;

    // DTO lồng bên trong
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChiTietThuocDTO {
        private Integer maThuoc;
        private String tenThuoc;
        private String lieuDung;
        private String tanSuat;
        private String thoiDiem;
        private Integer thoiGianDieuTri;
        private Integer soLuong;
        private String donViDung;
        private BigDecimal donGia;
        private String ghiChu;
        private String lyDoDonThuoc;
    }

    public KhamBenhDTO(BenhAn benhAn, DonThuoc donThuoc, HoaDon hoaDon) {
        this.maBenhAn = benhAn.getMaBenhAn();
        this.lyDoKham = benhAn.getLyDoKham();
        this.chanDoan = benhAn.getChanDoan();
        this.ghiChuDieuTri = benhAn.getGhiChuDieuTri();
        this.ngayTaiKham = benhAn.getNgayTaiKham();

        if (benhAn.getBenhNhan() != null) {
            var bn = benhAn.getBenhNhan();
            this.maBenhNhan = bn.getMaBenhNhan();
            this.hoTen = bn.getHoTen();
            this.soDienThoai = bn.getSoDienThoai();
            this.email = bn.getEmail();
            this.diaChi = bn.getDiaChi();
            this.ngaySinh = bn.getNgaySinh();
            this.gioiTinh = (bn.getGioiTinh() != null) ? bn.getGioiTinh().toString() : null;
            this.tienSuBenh = bn.getTienSuBenh();
            this.diUng = bn.getDiUng();
        }

        if (benhAn.getBacSi() != null) {
            this.maBacSi = benhAn.getBacSi().getMaBacSi();
            if (benhAn.getBacSi().getNguoiDung() != null) {
                this.tenBacSi = benhAn.getBacSi().getNguoiDung().getHoTen();
            }
        }

        if (benhAn.getLichHen() != null) {
            this.maLichHen = benhAn.getLichHen().getMaLichHen();
        }

        this.maDonThuoc = (donThuoc != null) ? donThuoc.getMaDonThuoc() : null;
        this.ghiChuDonThuoc = (donThuoc != null) ? donThuoc.getGhiChu() : null;
        this.maHoaDon = (hoaDon != null) ? hoaDon.getMaHoaDon() : null;
        this.tongTien = (hoaDon != null) ? hoaDon.getTongTien() : null;
        this.trangThaiKham = "Hoàn thành";
        this.ngayKham = LocalDateTime.now();

    }

    public KhamBenhDTO(LichHen lichHen) {
        this.maLichHen = lichHen.getMaLichHen();
        this.maBacSi = lichHen.getBacSi() != null ? lichHen.getBacSi().getMaBacSi() : null;
        this.lyDoKham = lichHen.getLydo();

        if (lichHen.getBenhNhan() != null) {
            BenhNhan bn = lichHen.getBenhNhan();
            this.maBenhNhan = bn.getMaBenhNhan();
            this.hoTen = bn.getHoTen();
            this.soDienThoai = bn.getSoDienThoai();
            this.email = bn.getEmail();
            this.diaChi = bn.getDiaChi();
            this.ngaySinh = bn.getNgaySinh();
            this.gioiTinh = bn.getGioiTinh() != null ? bn.getGioiTinh().toString() : null;
            this.tienSuBenh = bn.getTienSuBenh();
            this.diUng = bn.getDiUng();
        }
    }

}