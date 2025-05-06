package com.example.ClinicDentail.Enity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "lich_su_api")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LichSuApi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_lich_su")
    private Integer maLichSu;

    @Column(name = "loai_api", nullable = false, length = 50)
    private String loaiApi;

    @Column(name = "thoi_gian_goi")
    private LocalDateTime thoiGianGoi;

    @Lob
    @Column(name = "request", columnDefinition = "TEXT")
    private String request;

    @Lob
    @Column(name = "response", columnDefinition = "TEXT")
    private String response;

    @Column(name = "ma_doi_tuong")
    private Integer maDoiTuong;

    @Column(name = "loai_doi_tuong", length = 50)
    private String loaiDoiTuong;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @Column(name = "thoi_gian_xu_ly")
    private Integer thoiGianXuLy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_dung")
    private NguoiDung nguoiDung;

    @PrePersist
    protected void onCreate() {
        thoiGianGoi = LocalDateTime.now();
    }
}
