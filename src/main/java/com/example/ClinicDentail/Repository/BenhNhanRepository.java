package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.BenhNhan;
import com.example.ClinicDentail.Enity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BenhNhanRepository extends JpaRepository<BenhNhan,Integer> {
    Optional<BenhNhan> findByNguoiDung(NguoiDung nguoiDung);
}
