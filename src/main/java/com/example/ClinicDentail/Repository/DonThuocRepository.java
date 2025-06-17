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

    List<DonThuoc> findByBenhAn(BenhAn benhAn);
}