package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.BenhAn;
import com.example.ClinicDentail.Enity.BenhAnDichVu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BenhAnDichVuRepository extends JpaRepository<BenhAnDichVu,Integer> {
    List<BenhAnDichVu> findByBenhAn(BenhAn benhAn);
    Optional<BenhAnDichVu> findByBenhAn_MaBenhAnAndDichVu_MaDichVu(Integer maBenhAn, Integer maDichVu);

}
