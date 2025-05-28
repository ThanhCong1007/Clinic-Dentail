package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.BacSi;
import com.example.ClinicDentail.Enity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BacSiRepository extends JpaRepository<BacSi,Integer> {
    Optional<BacSi> findByNguoiDung(NguoiDung nguoiDung);
}
