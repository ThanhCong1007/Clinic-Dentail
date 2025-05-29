package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.BacSi;
import com.example.ClinicDentail.Enity.NguoiDung;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BacSiRepository extends JpaRepository<BacSi,Integer> {
    Optional<BacSi> findByNguoiDung(NguoiDung nguoiDung);

    Page<BacSi> findByTrangThaiLamViec(boolean b, Pageable pageable);

    Page<BacSi> findByChuyenKhoaContainingIgnoreCase(String chuyenKhoa, Pageable pageable);

    Page<BacSi> findByNguoiDung_HoTenContainingIgnoreCaseOrChuyenKhoaContainingIgnoreCase(String keyword, String keyword1, Pageable pageable);

    Page<BacSi> findBySoNamKinhNghiemGreaterThanEqual(int minYears, Pageable pageable);

    BacSi findByNguoiDung_MaNguoiDung(Integer maNguoiDung);
}
