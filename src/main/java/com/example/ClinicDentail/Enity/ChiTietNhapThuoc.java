package com.example.ClinicDentail.Enity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "chi_tiet_nhap_thuoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietNhapThuoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_chi_tiet")
    private Integer maChiTiet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_nhap_thuoc", nullable = false)
    private NhapThuoc nhapThuoc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_thuoc", nullable = false)
    private Thuoc thuoc;

    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;

    @Column(name = "don_gia", nullable = false)
    private BigDecimal donGia;

    @Column(name = "thanh_tien", nullable = false)
    private BigDecimal thanhTien;

    @Column(name = "han_su_dung")
    private LocalDate hanSuDung;

    @Column(name = "so_lo", length = 50)
    private String soLo;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
    }
}
