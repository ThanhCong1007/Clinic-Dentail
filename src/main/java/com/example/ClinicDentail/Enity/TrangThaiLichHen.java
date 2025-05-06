package com.example.ClinicDentail.Enity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "trang_thai_lich_hen")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrangThaiLichHen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_trang_thai")
    private Integer maTrangThai;

    @Column(name = "ten_trang_thai", nullable = false, unique = true, length = 50)
    private String tenTrangThai;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "ma_mau", length = 10)
    private String maMau;

    @OneToMany(mappedBy = "trangThai", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LichHen> lichHens;
}