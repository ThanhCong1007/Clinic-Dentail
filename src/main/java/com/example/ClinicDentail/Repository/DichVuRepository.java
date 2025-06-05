package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.DichVu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DichVuRepository extends JpaRepository<DichVu,Integer> {

    Optional<DichVu> findByTenDichVu(String tenDichVu);
}
