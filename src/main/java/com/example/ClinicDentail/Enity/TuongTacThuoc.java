package com.example.ClinicDentail.Enity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "tuong_tac_thuoc",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ma_thuoc_1", "ma_thuoc_2"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TuongTacThuoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_tuong_tac")
    private Integer maTuongTac;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_thuoc_1", nullable = false)
    private Thuoc thuoc1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_thuoc_2", nullable = false)
    private Thuoc thuoc2;

    @Column(name = "muc_do_nghiem_trong", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private MucDoNghiemTrong mucDoNghiemTrong;

    @Column(name = "mo_ta", columnDefinition = "TEXT", nullable = false)
    private String moTa;

    @Column(name = "khuyen_nghi", columnDefinition = "TEXT")
    private String khuyenNghi;

    @Column(name = "ma_ref_ext", length = 50)
    private String maRefExt;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        ngayCapNhat = LocalDateTime.now();
    }

    public enum MucDoNghiemTrong {
        Nhẹ,
        Trung_bình,
        Nghiêm_trọng,
        Chống_chỉ_định
    }
}
