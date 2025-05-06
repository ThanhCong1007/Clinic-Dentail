package com.example.ClinicDentail.Enity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "chi_tiet_hoa_don")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietHoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_muc")
    private Integer maMuc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_hoa_don", nullable = false)
    private HoaDon hoaDon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_dich_vu")
    private DichVu dichVu;

    @Column(name = "mo_ta", nullable = false)
    private String moTa;

    @Column(name = "so_luong", nullable = false)
    private Integer soLuong = 1;

    @Column(name = "don_gia", nullable = false, precision = 12, scale = 2)
    private BigDecimal donGia;

    @Column(name = "thanh_tien", nullable = false, precision = 12, scale = 2)
    private BigDecimal thanhTien;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
    }
}