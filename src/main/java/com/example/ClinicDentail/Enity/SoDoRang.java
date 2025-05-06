package com.example.ClinicDentail.Enity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "so_do_rang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoDoRang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_so_do")
    private Integer maSoDo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ma_benh_nhan", nullable = false)
    private BenhNhan benhNhan;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "soDo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChiTietRang> chiTietRangs;
}