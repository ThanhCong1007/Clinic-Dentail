package com.example.ClinicDentail.Enity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "nha_cung_cap")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhaCungCap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_nha_cung_cap")
    private Integer maNhaCungCap;

    @Column(name = "ten_nha_cung_cap", nullable = false, length = 100)
    private String tenNhaCungCap;

    @Column(name = "dia_chi", columnDefinition = "TEXT")
    private String diaChi;

    @Column(name = "so_dien_thoai", length = 20)
    private String soDienThoai;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "nguoi_lien_he", length = 100)
    private String nguoiLienHe;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "trang_thai_hoat_dong")
    private Boolean trangThaiHoatDong = true;

    @Column(name = "ngay_tao", updatable = false)
    private LocalDateTime ngayTao;

    @PrePersist
    protected void onCreate() {
        this.ngayTao = LocalDateTime.now();
    }
}
