package com.example.ClinicDentail.Enity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "benh_nhan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BenhNhan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_benh_nhan")
    private Integer maBenhNhan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_nguoi_dung")
    private NguoiDung nguoiDung;

    @Column(name = "ho_ten", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    @Column(name = "gioi_tinh")
    @Enumerated(EnumType.STRING)
    private GioiTinh gioiTinh;

    @Column(name = "so_dien_thoai", nullable = false, length = 20)
    private String soDienThoai;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "dia_chi", columnDefinition = "TEXT")
    private String diaChi;

    @Column(name = "tien_su_benh", columnDefinition = "TEXT")
    private String tienSuBenh;

    @Column(name = "di_ung", columnDefinition = "TEXT")
    private String diUng;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "benhNhan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<LichHen> lichHens;

    @OneToMany(mappedBy = "benhNhan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BenhAn> benhAns;

    @OneToMany(mappedBy = "benhNhan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SoDoRang> soDoRangs;

    @OneToMany(mappedBy = "benhNhan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HoaDon> hoaDons;

    @OneToMany(mappedBy = "benhNhan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<KeThuoc> keThuocs;

    public enum GioiTinh {
        Nam, Nữ, Khác
    }
}