package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.TrangThaiLichHen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrangThaiLichHenRepository extends JpaRepository<TrangThaiLichHen, Integer> {

    /**
     * Tìm trạng thái lịch hẹn theo tên
     */
    Optional<TrangThaiLichHen> findByTenTrangThai(String tenTrangThai);
}