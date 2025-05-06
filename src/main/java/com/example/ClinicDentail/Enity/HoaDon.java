package com.example.ClinicDentail.Enity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "hoa_don")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_hoa_don")
    private Integer maHoaDon;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ma_benh_nhan", nullable = false)
    private BenhNhan benhNhan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_lich_hen")
    private LichHen lichHen;

    @Column(name = "tong_tien", nullable = false, precision = 12, scale = 2)
    private BigDecimal tongTien;

    @Column(name = "thanh_tien", nullable = false, precision = 12, scale = 2)
    private BigDecimal thanhTien;

    @Column(name = "ngay_hoa_don")
    private LocalDateTime ngayHoaDon;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThaiHoaDon trangThai = TrangThaiHoaDon.CHUA_THANH_TOAN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_tao")
    private NguoiDung nguoiTao;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @PrePersist
    protected void onCreate() {
        ngayHoaDon = LocalDateTime.now();
        ngayTao = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChiTietHoaDon> chiTietHoaDons;

    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ThanhToan> thanhToans;

    public enum TrangThaiHoaDon {
        CHUA_THANH_TOAN("Chưa thanh toán"),
        DA_THANH_TOAN("Đã thanh toán"),
        HUY_BO("Hủy bỏ");

        private final String value;

        TrangThaiHoaDon(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}