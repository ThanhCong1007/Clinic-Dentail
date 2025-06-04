package com.example.ClinicDentail.Enity;

import jakarta.persistence.*;import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "chi_tiet_don_thuoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietDonThuoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_chi_tiet")
    private Integer maChiTiet;

    @ManyToOne
    @JoinColumn(name = "ma_don_thuoc", nullable = false)
    private DonThuoc donThuoc;

    @ManyToOne
    @JoinColumn(name = "ma_thuoc", nullable = false)
    private Thuoc thuoc;

    @Column(name = "lieu_dung", nullable = false)
    private String lieuDung;

    @Column(name = "tan_suat", nullable = false)
    private String tanSuat;

    @Column(name = "thoi_diem")
    private String thoiDiem;

    @Column(name = "thoi_gian_dieu_tri")
    private Integer thoiGianDieuTri;

    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;

    @Column(name = "don_vi_dung", nullable = false)
    private String donViDung;

    @Column(name = "don_gia", nullable = false, precision = 12, scale = 2)
    private BigDecimal donGia;

    @Column(name = "thanh_tien", nullable = false, precision = 12, scale = 2)
    private BigDecimal thanhTien;

    @Column(name = "canh_bao_tuong_tac")
    private String canhBaoTuongTac;

    @Column(name = "canh_bao_lieu_dung")
    private String canhBaoLieuDung;

    @Column(name = "ly_do_ke_don")
    private String lyDoKeDon;

    @Column(name = "ghi_chu")
    private String ghiChu;
}