package com.example.ClinicDentail.Enity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bac_si")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BacSi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_bac_si")
    private Integer maBacSi;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ma_nguoi_dung", nullable = false)
    private NguoiDung nguoiDung;

    @Column(name = "chuyen_khoa", length = 100)
    private String chuyenKhoa;

    @Column(name = "so_nam_kinh_nghiem")
    private Integer soNamKinhNghiem;

    @Column(name = "trang_thai_lam_viec")
    private Boolean trangThaiLamViec = true;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "bacSi", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LichHen> lichHens;

    @OneToMany(mappedBy = "bacSi", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BenhAn> benhAns;

    @OneToMany(mappedBy = "bacSi", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DonThuoc> donThuocs;
}