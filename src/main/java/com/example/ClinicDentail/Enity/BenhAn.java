package com.example.ClinicDentail.Enity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "benh_an")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BenhAn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_benh_an")
    private Integer maBenhAn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ma_lich_hen", nullable = true)
    private LichHen lichHen;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ma_benh_nhan", nullable = false)
    private BenhNhan benhNhan;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ma_bac_si", nullable = false)
    private BacSi bacSi;

    @Column(name = "ly_do_kham", columnDefinition = "TEXT")
    private String lyDoKham;

    @Column(name = "chan_doan", columnDefinition = "TEXT")
    private String chanDoan;

    @Column(name = "ghi_chu_dieu_tri", columnDefinition = "TEXT")
    private String ghiChuDieuTri;

    @Column(name = "ngay_tai_kham")
    private LocalDate ngayTaiKham;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "benhAn", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DonThuoc> donThuocs;
}