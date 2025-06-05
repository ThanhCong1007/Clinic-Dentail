package com.example.ClinicDentail.Enity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "nguoi_dung")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NguoiDung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_nguoi_dung")
    private Integer maNguoiDung;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ma_vai_tro", nullable = false)
    private VaiTro vaiTro;

    @Column(name = "ten_dang_nhap", nullable = false, unique = true, length = 50)
    private String tenDangNhap;

    @Column(name = "mat_khau", nullable = false)
    private String matKhau;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "ho_ten", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "so_dien_thoai", length = 20)
    private String soDienThoai;

    @Column(name = "trang_thai_hoat_dong")
    private Boolean trangThaiHoatDong = true;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "nguoiDung", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<BenhNhan> benhNhans;

    @OneToOne(mappedBy = "nguoiDung", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private BacSi bacSi;

    @OneToMany(mappedBy = "nguoiTao", fetch = FetchType.LAZY)
    private List<HoaDon> hoaDons;

    @OneToMany(mappedBy = "nguoiTao", fetch = FetchType.LAZY)
    private List<ThanhToan> thanhToans;


}