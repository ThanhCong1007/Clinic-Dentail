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

    Page<BacSi> findByTrangThaiLamViec(boolean b, Pageable pageable);

    Page<BacSi> findByChuyenKhoaContainingIgnoreCase(String chuyenKhoa, Pageable pageable);

    Page<BacSi> findByNguoiDung_HoTenContainingIgnoreCaseOrChuyenKhoaContainingIgnoreCase(String keyword, String keyword1, Pageable pageable);

    Page<BacSi> findBySoNamKinhNghiemGreaterThanEqual(int minYears, Pageable pageable);

    BacSi findByNguoiDung_MaNguoiDung(Integer maNguoiDung);

    /**
     * Tìm tất cả bác sĩ đang làm việc và tài khoản đang hoạt động
     */
    List<BacSi> findByTrangThaiLamViecTrueAndNguoiDung_TrangThaiHoatDongTrue();

    /**
     * Tìm bác sĩ theo chuyên khoa (không phân biệt hoa thường)
     */
    List<BacSi> findByChuyenKhoaContainingIgnoreCaseAndTrangThaiLamViecTrueAndNguoiDung_TrangThaiHoatDongTrue(String chuyenKhoa);

    /**
     * Tìm bác sĩ theo ID và đang hoạt động
     */
    Optional<BacSi> findByMaBacSiAndTrangThaiLamViecTrueAndNguoiDung_TrangThaiHoatDongTrue(Integer maBacSi);

    /**
     * Lấy danh sách chuyên khoa duy nhất của các bác sĩ đang hoạt động
     */
    @Query("SELECT DISTINCT b.chuyenKhoa FROM BacSi b WHERE b.trangThaiLamViec = true AND b.nguoiDung.trangThaiHoatDong = true")
    List<String> findDistinctChuyenKhoaByTrangThaiLamViecTrueAndNguoiDung_TrangThaiHoatDongTrue();

    /**
     * Đếm số bác sĩ đang hoạt động
     */
    long countByTrangThaiLamViecTrueAndNguoiDung_TrangThaiHoatDongTrue();

    /**
     * Đếm số chuyên khoa khác nhau
     */
    @Query("SELECT COUNT(DISTINCT b.chuyenKhoa) FROM BacSi b WHERE b.trangThaiLamViec = true AND b.nguoiDung.trangThaiHoatDong = true")
    long countDistinctChuyenKhoaByTrangThaiLamViecTrueAndNguoiDung_TrangThaiHoatDongTrue();

    /**
     * Kiểm tra bác sĩ có tồn tại và đang hoạt động
     */
    boolean existsByMaBacSiAndTrangThaiLamViecTrueAndNguoiDung_TrangThaiHoatDongTrue(Integer maBacSi);

    /**
     * Tìm kiếm bác sĩ theo tên hoặc chuyên khoa
     */
    @Query("SELECT b FROM BacSi b WHERE b.trangThaiLamViec = true AND b.nguoiDung.trangThaiHoatDong = true AND " +
            "(LOWER(b.nguoiDung.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.chuyenKhoa) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<BacSi> findByNguoiDung_HoTenContainingIgnoreCaseOrChuyenKhoaContainingIgnoreCaseAndTrangThaiLamViecTrueAndNguoiDung_TrangThaiHoatDongTrue(@Param("keyword") String keyword1, @Param("keyword") String keyword2);

    /**
     * Lấy top bác sĩ có nhiều lịch hẹn nhất
     */
    @Query("SELECT b, COUNT(lh) as appointmentCount FROM BacSi b " +
            "LEFT JOIN LichHen lh ON b.maBacSi = lh.bacSi.maBacSi " +
            "WHERE b.trangThaiLamViec = true AND b.nguoiDung.trangThaiHoatDong = true " +
            "GROUP BY b ORDER BY appointmentCount DESC")
    List<Object[]> findTopDoctorsByAppointmentCount();

    /**
     * Lấy bác sĩ theo số năm kinh nghiệm tối thiểu
     */
    List<BacSi> findByTrangThaiLamViecTrueAndNguoiDung_TrangThaiHoatDongTrueAndSoNamKinhNghiemGreaterThanEqual(Integer soNamKinhNghiem);

}
