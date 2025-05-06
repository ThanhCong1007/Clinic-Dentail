package com.example.ClinicDentail.Enity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "thuoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Thuoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_thuoc")
    private Integer maThuoc;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ma_loai_thuoc")
    private LoaiThuoc loaiThuoc;

    @Column(name = "ten_thuoc", nullable = false, length = 100)
    private String tenThuoc;

    @Column(name = "ma_atc", length = 10)
    private String maAtc;

    @Column(name = "ma_ndc", length = 20)
    private String maNdc;

    @Column(name = "ma_rxnorm", length = 20)
    private String maRxnorm;

    @Column(name = "ma_snomed", length = 20)
    private String maSnomed;

    @Column(name = "ma_product", length = 100)
    private String maProduct;

    @Column(name = "ma_gtin", length = 14)
    private String maGtin;

    @Column(name = "hoat_chat", length = 255)
    private String hoatChat;

    @Column(name = "ham_luong", length = 100)
    private String hamLuong;

    @Column(name = "nha_san_xuat", length = 100)
    private String nhaSanXuat;

    @Column(name = "nuoc_san_xuat", length = 50)
    private String nuocSanXuat;

    @Column(name = "dang_bao_che", length = 50)
    private String dangBaoChe;

    @Column(name = "don_vi_tinh", nullable = false, length = 50)
    private String donViTinh;

    @Column(name = "duong_dung", length = 100)
    private String duongDung;

    @Column(name = "huong_dan_su_dung", columnDefinition = "TEXT")
    private String huongDanSuDung;

    @Column(name = "cach_bao_quan", columnDefinition = "TEXT")
    private String cachBaoQuan;

    @Enumerated(EnumType.STRING)
    @Column(name = "phan_loai_ke_don", columnDefinition = "ENUM('Không kê đơn', 'Kê đơn', 'Kiểm soát đặc biệt') DEFAULT 'Kê đơn'")
    private PhanLoaiKeDon phanLoaiKeDon;

    @Column(name = "chong_chi_dinh", columnDefinition = "TEXT")
    private String chongChiDinh;

    @Column(name = "tac_dung_phu", columnDefinition = "TEXT")
    private String tacDungPhu;

    @Column(name = "tuong_tac_thuoc", columnDefinition = "TEXT")
    private String tuongTacThuoc;

    @Column(name = "nhom_thuoc_thai_ky", length = 5)
    private String nhomThuocThaiKy;

    @Column(name = "gia", nullable = false, precision = 12, scale = 2)
    private BigDecimal gia;

    @Column(name = "so_luong_ton", nullable = false)
    private Integer soLuongTon = 0;

    @Column(name = "nguong_canh_bao")
    private Integer nguongCanhBao = 10;

    @Column(name = "trang_thai_hoat_dong")
    private Boolean trangThaiHoatDong = true;

    @Column(name = "ngay_tao", updatable = false)
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @PrePersist
    protected void onCreate() {
        this.ngayTao = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.ngayCapNhat = LocalDateTime.now();
    }

    public enum PhanLoaiKeDon {
        Không_kê_đơn,
        Kê_đơn,
        Kiểm_soát_đặc_biệt
    }
}
