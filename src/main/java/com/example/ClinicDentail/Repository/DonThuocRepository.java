package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.BenhAn;
import com.example.ClinicDentail.Enity.DonThuoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DonThuocRepository extends JpaRepository<DonThuoc, Integer> {

    DonThuoc findByBenhAn(BenhAn benhAn);
    Optional<DonThuoc> findByBenhAn_MaBenhAn(Integer maBenhAn);

    List<DonThuoc> findByBenhNhan_MaBenhNhan(Integer maBenhNhan);

    List<DonThuoc> findByBacSi_MaBacSi(Integer maBacSi);
}