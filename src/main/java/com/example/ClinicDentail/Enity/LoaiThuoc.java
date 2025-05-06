package com.example.ClinicDentail.Enity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "loai_thuoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoaiThuoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_loai_thuoc")
    private Integer maLoaiThuoc;

    @Column(name = "ten_loai_thuoc", nullable = false, unique = true, length = 100)
    private String tenLoaiThuoc;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;
}
