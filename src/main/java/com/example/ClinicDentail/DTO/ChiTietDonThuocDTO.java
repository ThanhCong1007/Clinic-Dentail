package com.example.ClinicDentail.DTO;

import com.example.ClinicDentail.Enity.ChiTietDonThuoc;
import com.example.ClinicDentail.Enity.Thuoc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietDonThuocDTO {
    private Integer maChiTiet;
    private Integer maThuoc;
    private String tenThuoc;
    private String hoatChat;
    private String hamLuong;
    private String dangBaoChe;
    private String lieudung;
    private String tanSuat;
    private String thoiDiem;
    private Integer thoiGianDieuTri;
    private Integer soLuong;
    private String donViDung;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private String ghiChu;
    private String lyDoDonThuoc;

    public ChiTietDonThuocDTO(ChiTietDonThuoc chiTiet) {
        this.maChiTiet = chiTiet.getMaChiTiet();
        this.maThuoc = chiTiet.getThuoc().getMaThuoc();
        this.tenThuoc = chiTiet.getThuoc().getTenThuoc();
        this.hoatChat = chiTiet.getThuoc().getHoatChat();
        this.hamLuong = chiTiet.getThuoc().getHamLuong();
        this.dangBaoChe = chiTiet.getThuoc().getDangBaoChe();

        this.lieudung = chiTiet.getLieuDung();
        this.tanSuat = chiTiet.getTanSuat();
        this.thoiDiem = chiTiet.getThoiDiem();
        this.thoiGianDieuTri = chiTiet.getThoiGianDieuTri();
        this.soLuong = chiTiet.getSoLuong();
        this.donViDung = chiTiet.getDonViDung();
        this.donGia = chiTiet.getDonGia();
        this.thanhTien = chiTiet.getThanhTien();
        this.ghiChu = chiTiet.getGhiChu();
        this.lyDoDonThuoc = chiTiet.getLyDoKeDon();
    }

}