package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.BenhAn;
import com.example.ClinicDentail.Enity.BenhAnDichVu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface BenhAnDichVuRepository extends JpaRepository<BenhAnDichVu,Integer> {
    List<BenhAnDichVu> findByBenhAn(BenhAn benhAn);
    Optional<BenhAnDichVu> findByBenhAn_MaBenhAnAndDichVu_MaDichVu(Integer maBenhAn, Integer maDichVu);
    List<BenhAnDichVu> findByBenhAn_MaBenhAn(Integer maBenhAn);

    @Query("SELECT SUM(bad.gia) FROM BenhAnDichVu bad WHERE bad.benhAn.maBenhAn = :maBenhAn")
    BigDecimal getTongGiaDichVuByBenhAn(@Param("maBenhAn") Integer maBenhAn);
}
