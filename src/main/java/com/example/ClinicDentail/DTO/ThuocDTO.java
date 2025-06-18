package com.example.ClinicDentail.DTO;

import com.example.ClinicDentail.Enity.Thuoc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThuocDTO {
    private Integer maThuoc;
    private Integer maLoaiThuoc; // Chỉ dùng ID thay vì đối tượng LoaiThuoc

    private String tenThuoc;

    private String hoatChat;
    private String hamLuong;
    private String nhaSanXuat;
    private String nuocSanXuat;
    private String dangBaoChe;
    private String donViTinh;
    private String duongDung;
    private String huongDanSuDung;
    private String cachBaoQuan;

    private String phanLoaiDonThuoc; // Dùng String để dễ dàng ánh xạ Enum khi gửi JSON

    private String chongChiDinh;
    private String tacDungPhu;
    private String tuongTacThuoc;
    private String nhomThuocThaiKy;

    private BigDecimal gia;
    private Integer soLuongTon;
    private Integer nguongCanhBao;
    private Boolean trangThaiHoatDong;

    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;

    public ThuocDTO(Thuoc thuoc) {
        if (thuoc == null) return;

        this.maThuoc = thuoc.getMaThuoc();

        if (thuoc.getLoaiThuoc() != null) {
            this.maLoaiThuoc = thuoc.getLoaiThuoc().getMaLoaiThuoc();
        }

        this.tenThuoc = thuoc.getTenThuoc();
        this.hoatChat = thuoc.getHoatChat();
        this.hamLuong = thuoc.getHamLuong();
        this.nhaSanXuat = thuoc.getNhaSanXuat();
        this.nuocSanXuat = thuoc.getNuocSanXuat();
        this.dangBaoChe = thuoc.getDangBaoChe();
        this.donViTinh = thuoc.getDonViTinh();
        this.duongDung = thuoc.getDuongDung();
        this.huongDanSuDung = thuoc.getHuongDanSuDung();
        this.cachBaoQuan = thuoc.getCachBaoQuan();

        // Enum phân loại đơn thuốc ánh xạ sang String
        this.phanLoaiDonThuoc = thuoc.getPhanLoaiDonThuoc() != null
                ? thuoc.getPhanLoaiDonThuoc().toString()
                : null;

        this.chongChiDinh = thuoc.getChongChiDinh();
        this.tacDungPhu = thuoc.getTacDungPhu();
        this.tuongTacThuoc = thuoc.getTuongTacThuoc();
        this.nhomThuocThaiKy = thuoc.getNhomThuocThaiKy();

        this.gia = thuoc.getGia();
        this.soLuongTon = thuoc.getSoLuongTon();
        this.nguongCanhBao = thuoc.getNguongCanhBao();
        this.trangThaiHoatDong = thuoc.getTrangThaiHoatDong();

        this.ngayTao = thuoc.getNgayTao();
        this.ngayCapNhat = thuoc.getNgayCapNhat();
    }

}
