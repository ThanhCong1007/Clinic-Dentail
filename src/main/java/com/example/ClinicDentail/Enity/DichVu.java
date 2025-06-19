package com.example.ClinicDentail.Enity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "dich_vu")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DichVu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_dich_vu")
    private Integer maDichVu;

    @Column(name = "ten_dich_vu", nullable = false, length = 100)
    private String tenDichVu;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "gia", nullable = false, precision = 12, scale = 2)
    private BigDecimal gia;

    @Column(name = "thoi_gian_du_kien", nullable = false)
    private Integer thoiGianDuKien;

    @Column(name = "trang_thai_hoat_dong")
    private Boolean trangThaiHoatDong = true;

    @OneToMany(mappedBy = "dichVu", fetch = FetchType.LAZY)
    private List<LichHen> lichHens;

    @OneToMany(mappedBy = "dichVu", fetch = FetchType.LAZY)
    private List<ChiTietHoaDon> chiTietHoaDons;

    @OneToMany(mappedBy = "dichVu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BenhAnDichVu> benhAnDichVus;
}