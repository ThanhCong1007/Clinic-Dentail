package com.example.ClinicDentail.Enity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "chi_tiet_rang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietRang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_chi_tiet_rang")
    private Integer maChiTietRang;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ma_so_do", nullable = false)
    private SoDoRang soDo;

    @Column(name = "so_rang", nullable = false)
    private Integer soRang;

    @Column(name = "tinh_trang_rang", length = 50)
    private String tinhTrangRang;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_cap_nhat")
    private NguoiDung nguoiCapNhat;

    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @PrePersist
    protected void onCreate() {
        ngayCapNhat = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        ngayCapNhat = LocalDateTime.now();
    }
}