package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.BacSi;
import com.example.ClinicDentail.Enity.NguoiDung;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BacSiRepository extends JpaRepository<BacSi,Integer> {
    Optional<BacSi> findByNguoiDung(NguoiDung nguoiDung);

    BacSi findByNguoiDung_MaNguoiDung(Integer maNguoiDung);

    /**
     * Tìm tất cả bác sĩ đang làm việc và tài khoản đang hoạt động
     */
    List<BacSi> findByTrangThaiLamViecTrueAndNguoiDung_TrangThaiHoatDongTrue();

    /**
     * Lấy danh sách chuyên khoa duy nhất của các bác sĩ đang hoạt động
     */
    @Query("SELECT DISTINCT b.chuyenKhoa FROM BacSi b WHERE b.trangThaiLamViec = true AND b.nguoiDung.trangThaiHoatDong = true")
    List<String> findDistinctChuyenKhoaByTrangThaiLamViecTrueAndNguoiDung_TrangThaiHoatDongTrue();


}
