package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.BenhAn;
import com.example.ClinicDentail.Enity.BenhNhan;
import com.example.ClinicDentail.Enity.NguoiDung;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BenhNhanRepository extends JpaRepository<BenhNhan,Integer> {
    Optional<BenhNhan> findByNguoiDung(NguoiDung nguoiDung);

    BenhNhan findByNguoiDung_MaNguoiDung(Integer maNguoiDung);

    Optional<BenhNhan> findBySoDienThoai(String soDienThoai);

    @Query("SELECT b FROM BenhNhan b WHERE b.soDienThoai LIKE %:soDienThoai%")
    List<BenhNhan> findBySoDienThoaiContaining(@Param("soDienThoai") String soDienThoai);


}
