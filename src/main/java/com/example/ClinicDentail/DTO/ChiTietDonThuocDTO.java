package com.example.ClinicDentail.DTO;

import com.example.ClinicDentail.Enity.ChiTietDonThuoc;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ChiTietDonThuocDTO {
    // Thông tin thuốc
    private Integer maThuoc;
    private String tenThuoc;
    private String hoatChat;
    private String hamLuong;
    private String dangBaoChe;
    private String donViTinh;

    // Thông tin kê đơn
    private String lieuDung;
    private String tanSuat;
    private String thoiDiem;
    private Integer thoiGianDieuTri; // Số ngày điều trị
    private Integer soLuong;
    private String donViDung;
    private BigDecimal donGia;
    private BigDecimal thanhTien;

    // Ghi chú và cảnh báo
    private String canhBaoTuongTac;
    private String canhBaoLieuDung;
    private String lyDoKeDon;
    private String ghiChu;

    // Constructor từ Entity
    public ChiTietDonThuocDTO(ChiTietDonThuoc chiTiet) {
        if (chiTiet.getThuoc() != null) {
            this.maThuoc = chiTiet.getThuoc().getMaThuoc();
            this.tenThuoc = chiTiet.getThuoc().getTenThuoc();
            this.hoatChat = chiTiet.getThuoc().getHoatChat();
            this.hamLuong = chiTiet.getThuoc().getHamLuong();
            this.dangBaoChe = chiTiet.getThuoc().getDangBaoChe();
            this.donViTinh = chiTiet.getThuoc().getDonViTinh();
        }

        this.lieuDung = chiTiet.getLieuDung();
        this.tanSuat = chiTiet.getTanSuat();
        this.thoiDiem = chiTiet.getThoiDiem();
        this.thoiGianDieuTri = chiTiet.getThoiGianDieuTri();
        this.soLuong = chiTiet.getSoLuong();
        this.donViDung = chiTiet.getDonViDung();
        this.donGia = chiTiet.getDonGia();
        this.thanhTien = chiTiet.getThanhTien();
        this.canhBaoTuongTac = chiTiet.getCanhBaoTuongTac();
        this.canhBaoLieuDung = chiTiet.getCanhBaoLieuDung();
        this.lyDoKeDon = chiTiet.getLyDoKeDon();
        this.ghiChu = chiTiet.getGhiChu();
    }
}