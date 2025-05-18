package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {
    Optional<NguoiDung> findByTenDangNhap(String tenDangNhap);
    Optional<NguoiDung> findByEmail(String email);
    Boolean existsByTenDangNhap(String tenDangNhap);
    Boolean existsByEmail(String email);
}