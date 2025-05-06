package com.example.ClinicDentail.Enity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "kiem_tra_ke_don")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KiemTraKeDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_kiem_tra")
    private Integer maKiemTra;

    @ManyToOne
    @JoinColumn(name = "ma_ke_thuoc", nullable = false)
    private KeThuoc keThuoc;

    @Column(name = "thoi_gian_kiem_tra")
    private LocalDateTime thoiGianKiemTra;

    @Column(name = "ma_api_ref")
    private String maApiRef;

    @Column(name = "ket_qua", columnDefinition = "json")
    private String ketQua;

    @Column(name = "trang_thai", nullable = false)
    @Enumerated(EnumType.STRING)
    private TrangThaiKiemTra trangThai;

    @Column(name = "mo_ta")
    private String moTa;

    @ManyToOne
    @JoinColumn(name = "nguoi_kiem_tra")
    private NguoiDung nguoiKiemTra;

    public enum TrangThaiKiemTra {
        DAT("Đạt"),
        CANH_BAO("Cảnh báo"),
        KHONG_DAT("Không đạt");

        private String value;

        TrangThaiKiemTra(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}