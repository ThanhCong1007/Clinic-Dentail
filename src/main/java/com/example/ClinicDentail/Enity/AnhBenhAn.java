package com.example.ClinicDentail.Enity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "anh_benh_an")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnhBenhAn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_anh")
    private Integer maAnh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_benh_an", nullable = false)
    private BenhAn benhAn;

    @Column(name = "url", nullable = false, length = 255)
    private String url;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "ngay_tao", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime ngayTao;

    @PrePersist
    protected void onCreate() {
        if (ngayTao == null) {
            ngayTao = LocalDateTime.now();
        }
    }
}
