package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.ThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThanhToanRepository extends JpaRepository<ThanhToan, Integer> {
    List<ThanhToan> findByHoaDonMaHoaDon(Integer maHoaDon);
    List<ThanhToan> findByTrangThaiThanhToan(ThanhToan.TrangThaiThanhToan trangThai);
}
