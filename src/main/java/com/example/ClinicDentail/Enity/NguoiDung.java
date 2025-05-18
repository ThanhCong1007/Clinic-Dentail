package com.example.ClinicDentail.Enity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "nguoi_dung")
@Data
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
    private List<BenhNhan> benhNhans;

    @OneToOne(mappedBy = "nguoiDung", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private BacSi bacSi;

    @OneToMany(mappedBy = "nguoiTao", fetch = FetchType.LAZY)
    private List<HoaDon> hoaDons;

    @OneToMany(mappedBy = "nguoiTao", fetch = FetchType.LAZY)
    private List<ThanhToan> thanhToans;

    public NguoiDung() {
    }

    public NguoiDung(Integer maNguoiDung, VaiTro vaiTro, String tenDangNhap, String matKhau, String email, String hoTen, String soDienThoai, Boolean trangThaiHoatDong, LocalDateTime ngayTao, List<BenhNhan> benhNhans, BacSi bacSi, List<HoaDon> hoaDons, List<ThanhToan> thanhToans) {
        this.maNguoiDung = maNguoiDung;
        this.vaiTro = vaiTro;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.email = email;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.trangThaiHoatDong = trangThaiHoatDong;
        this.ngayTao = ngayTao;
        this.benhNhans = benhNhans;
        this.bacSi = bacSi;
        this.hoaDons = hoaDons;
        this.thanhToans = thanhToans;
    }
}