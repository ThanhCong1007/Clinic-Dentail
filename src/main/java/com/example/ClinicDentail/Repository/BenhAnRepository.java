package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.BenhAn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BenhAnRepository extends JpaRepository<BenhAn,Integer> {

    // Tìm bệnh án theo lịch hẹn
    Optional<BenhAn> findByLichHen_MaLichHen(Integer maLichHen);

    // Tìm bệnh án theo bệnh nhân
    List<BenhAn> findByBenhNhan_MaBenhNhanOrderByNgayTaoDesc(Integer maBenhNhan);

    // Tìm bệnh án theo bác sĩ
    List<BenhAn> findByBacSi_MaBacSiOrderByNgayTaoDesc(Integer maBacSi);

    // Tìm bệnh án trong khoảng thời gian
    List<BenhAn> findByNgayTaoBetween(LocalDateTime tuNgay, LocalDateTime denNgay);
}
