package com.example.ClinicDentail.Enity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "thanh_toan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThanhToan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_thanh_toan")
    private Integer maThanhToan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_hoa_don", nullable = false)
    private HoaDon hoaDon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_phuong_thuc", nullable = false)
    private PhuongThucThanhToan phuongThucThanhToan;

    @Column(name = "so_tien", nullable = false, precision = 12, scale = 2)
    private BigDecimal soTien;

    @Column(name = "ngay_thanh_toan")
    private LocalDateTime ngayThanhToan;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai_thanh_toan")
    private TrangThaiThanhToan trangThaiThanhToan = TrangThaiThanhToan.THANH_CONG;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_tao")
    private NguoiDung nguoiTao;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @PrePersist
    protected void onCreate() {
        ngayThanhToan = LocalDateTime.now();
        ngayTao = LocalDateTime.now();
    }

    public enum TrangThaiThanhToan {
        THANH_CONG("Thành công"),
        DANG_XU_LY("Đang xử lý"),
        THAT_BAI("Thất bại");

        private final String value;

        TrangThaiThanhToan(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}