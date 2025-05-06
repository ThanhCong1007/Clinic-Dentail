package com.example.ClinicDentail.Enity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "phuong_thuc_thanh_toan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhuongThucThanhToan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_phuong_thuc")
    private Integer maPhuongThuc;

    @Column(name = "ten_phuong_thuc", nullable = false, unique = true, length = 50)
    private String tenPhuongThuc;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "trang_thai_hoat_dong")
    private Boolean trangThaiHoatDong = true;

    @OneToMany(mappedBy = "phuongThucThanhToan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ThanhToan> thanhToans;
}