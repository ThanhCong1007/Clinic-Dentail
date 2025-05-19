package com.example.ClinicDentail.Enity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
@Table(name = "lich_hen")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LichHen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_lich_hen")
    private Integer maLichHen;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ma_benh_nhan", nullable = false)
    @JsonBackReference
    private BenhNhan benhNhan;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ma_bac_si", nullable = false)
    private BacSi bacSi;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ma_dich_vu")
    private DichVu dichVu;

    @Column(name = "ngay_hen", nullable = false)
    private LocalDate ngayHen;

    @Column(name = "gio_bat_dau", nullable = false)
    private LocalTime gioBatDau;

    @Column(name = "gio_ket_thuc", nullable = false)
    private LocalTime gioKetThuc;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ma_trang_thai", nullable = false)
    private TrangThaiLichHen trangThai;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "lichHen", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BenhAn> benhAns;

    @OneToMany(mappedBy = "lichHen", fetch = FetchType.LAZY)
    private List<HoaDon> hoaDons;
    /**
     * Tính thời gian của cuộc hẹn theo phút
     * @return Thời lượng cuộc hẹn (phút)
     */
    @Transient
    public long getThoiLuong() {
        return ChronoUnit.MINUTES.between(gioBatDau, gioKetThuc);
    }
}