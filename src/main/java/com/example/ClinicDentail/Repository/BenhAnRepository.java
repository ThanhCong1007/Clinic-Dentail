package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.BenhAn;
import com.example.ClinicDentail.Enity.LichHen;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BenhAnRepository extends JpaRepository<BenhAn,Integer> {

    // Tìm bệnh án theo bệnh nhân
    @Query("SELECT ba FROM BenhAn ba " +
            "LEFT JOIN FETCH ba.benhNhan bn " +
            "LEFT JOIN FETCH bn.nguoiDung " +
            "WHERE ba.benhNhan.maBenhNhan = :maBenhNhan " +
            "ORDER BY ba.ngayTao DESC")
    List<BenhAn> findByBenhNhan_MaBenhNhanOrderByNgayTaoDesc(@Param("maBenhNhan") Integer maBenhNhan);


}
