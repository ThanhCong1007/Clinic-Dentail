package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.BenhAnDichVu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BenhAnDichVuRepository extends JpaRepository<BenhAnDichVu,Integer> {
}
