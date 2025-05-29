package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.BenhNhan;
import com.example.ClinicDentail.Enity.NguoiDung;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BenhNhanRepository extends JpaRepository<BenhNhan,Integer> {
    Optional<BenhNhan> findByNguoiDung(NguoiDung nguoiDung);

    Page<BenhNhan> findByHoTenContainingIgnoreCaseOrEmailContainingIgnoreCase(String keyword, String keyword1, Pageable pageable);

    BenhNhan findByNguoiDung_MaNguoiDung(Integer maNguoiDung);
}
