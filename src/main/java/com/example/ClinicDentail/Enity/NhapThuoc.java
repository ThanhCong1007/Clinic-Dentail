package com.example.ClinicDentail.Enity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "nhap_thuoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhapThuoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_nhap_thuoc")
    private Integer maNhapThuoc;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ma_nha_cung_cap", nullable = false)
    private NhaCungCap nhaCungCap;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ma_nguoi_dung", nullable = false)
    private NguoiDung nguoiDung;

    @Column(name = "ngay_nhap", nullable = false)
    private LocalDate ngayNhap;

    @Column(name = "tong_tien", nullable = false, precision = 12, scale = 2)
    private BigDecimal tongTien;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "ngay_tao", updatable = false)
    private LocalDateTime ngayTao;

    @PrePersist
    protected void onCreate() {
        this.ngayTao = LocalDateTime.now();
    }
}
