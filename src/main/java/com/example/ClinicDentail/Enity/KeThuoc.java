package com.example.ClinicDentail.Enity;

import jakarta.persistence.*;import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ke_thuoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeThuoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_ke_thuoc")
    private Integer maKeThuoc;

    @ManyToOne
    @JoinColumn(name = "ma_benh_an", nullable = false)
    private BenhAn benhAn;

    @ManyToOne
    @JoinColumn(name = "ma_benh_nhan", nullable = false)
    private BenhNhan benhNhan;

    @ManyToOne
    @JoinColumn(name = "ma_bac_si", nullable = false)
    private BacSi bacSi;

    @Column(name = "ngay_ke", nullable = false)
    private LocalDate ngayKe;

    @Column(name = "so_toa")
    private String soToa;

    @Column(name = "ma_icd")
    private String maIcd;

    @Column(name = "mo_ta_chan_doan")
    private String moTaChanDoan;

    @Column(name = "trang_thai_kiem_tra")
    @Enumerated(EnumType.STRING)
    private TrangThaiKiemTra trangThaiKiemTra = TrangThaiKiemTra.CHO_KIEM_TRA;

    @Column(name = "ket_qua_kiem_tra")
    private String ketQuaKiemTra;

    @Column(name = "ma_kiem_tra")
    private String maKiemTra;

    @Column(name = "trang_thai_toa")
    @Enumerated(EnumType.STRING)
    private TrangThaiToa trangThaiToa = TrangThaiToa.MOI;

    @Column(name = "ngay_phat_hanh")
    private LocalDateTime ngayPhatHanh;

    @Column(name = "ngay_het_han")
    private LocalDate ngayHetHan;

    @ManyToOne
    @JoinColumn(name = "nguoi_phat_thuoc")
    private NguoiDung nguoiPhatThuoc;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    public enum TrangThaiKiemTra {
        CHO_KIEM_TRA("Chờ kiểm tra"),
        DA_KIEM_TRA("Đã kiểm tra"),
        CO_CANH_BAO("Có cảnh báo"),
        KHONG_DAT("Không đạt");

        private String value;

        TrangThaiKiemTra(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum TrangThaiToa {
        MOI("Mới"),
        DA_PHAT_HANH("Đã phát hành"),
        DA_PHAT_THUOC("Đã phát thuốc"),
        HUY("Hủy");

        private String value;

        TrangThaiToa(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}