package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.PhuongThucThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhuongThucThanhToanRepository extends JpaRepository<PhuongThucThanhToan, Integer> {
    Optional<PhuongThucThanhToan> findByTenPhuongThuc(String tenPhuongThuc);
    List<PhuongThucThanhToan> findByTrangThaiHoatDong(Boolean trangThai);
}