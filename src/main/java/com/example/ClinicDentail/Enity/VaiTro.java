package com.example.ClinicDentail.Enity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "vai_tro")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VaiTro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_vai_tro")
    private Integer maVaiTro;

    @Column(name = "ten_vai_tro", nullable = false, unique = true, length = 50)
    private String tenVaiTro;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @OneToMany(mappedBy = "vaiTro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NguoiDung> nguoiDungs;
}