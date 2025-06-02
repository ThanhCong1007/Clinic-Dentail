package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.BenhAn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BenhAnRepository extends JpaRepository<BenhAn,Integer> {
    Optional<BenhAn> findByLichHen_MaLichHen(Integer maLichHen);
    List<BenhAn> findByBenhNhan_MaBenhNhanOrderByNgayTaoDesc(Integer maBenhNhan);
    List<BenhAn> findByBacSi_MaBacSiOrderByNgayTaoDesc(Integer maBacSi);
}
