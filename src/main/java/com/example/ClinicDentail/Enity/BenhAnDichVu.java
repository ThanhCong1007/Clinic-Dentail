package com.example.ClinicDentail.Enity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BenhAnDichVu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ma_benh_an", nullable = false)
    private BenhAn benhAn;

    @ManyToOne
    @JoinColumn(name = "ma_dich_vu", nullable = false)
    private DichVu dichVu;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal gia;
}