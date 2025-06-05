package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.ChiTietHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChiTietHoaDonRepository extends JpaRepository<ChiTietHoaDon,Integer> {
}
