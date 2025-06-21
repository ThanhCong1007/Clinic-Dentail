package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.BenhAn;
import com.example.ClinicDentail.Enity.LichHen;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BenhAnRepository extends JpaRepository<BenhAn,Integer> {

    // Tìm bệnh án theo bệnh nhân
    @Query("SELECT ba FROM BenhAn ba " +
            "LEFT JOIN FETCH ba.benhNhan bn " +
            "LEFT JOIN FETCH bn.nguoiDung " +
            "WHERE ba.benhNhan.maBenhNhan = :maBenhNhan " +
            "ORDER BY ba.ngayTao DESC")
    List<BenhAn> findByBenhNhan_MaBenhNhanOrderByNgayTaoDesc(@Param("maBenhNhan") Integer maBenhNhan);

    /**
     * Tìm bệnh án theo mã bác sĩ
     */
    Page<BenhAn> findByBacSi_MaBacSi(Integer maBacSi, Pageable pageable);

    /**
     * Tìm bệnh án theo mã bệnh nhân
     */
    Page<BenhAn> findByBenhNhan_MaBenhNhan(Integer maBenhNhan, Pageable pageable);

    /**
     * Tìm kiếm bệnh án theo bác sĩ và từ khóa
     */
    @Query("SELECT ba FROM BenhAn ba " +
            "WHERE ba.bacSi.maBacSi = :maBacSi " +
            "AND (LOWER(ba.benhNhan.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR ba.benhNhan.soDienThoai LIKE CONCAT('%', :keyword, '%') " +
            "OR LOWER(ba.chanDoan) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(ba.lyDoKham) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<BenhAn> findByBacSiAndKeyword(@Param("maBacSi") Integer maBacSi,
                                       @Param("keyword") String keyword,
                                       Pageable pageable);

    /**
     * Đếm số lượng bệnh án theo bác sĩ
     */
    long countByBacSi_MaBacSi(Integer maBacSi);

    /**
     * Đếm số lượng bệnh án theo bệnh nhân
     */
    long countByBenhNhan_MaBenhNhan(Integer maBenhNhan);

    /**
     * Tìm bệnh án gần nhất của bệnh nhân
     */
    Optional<BenhAn> findTopByBenhNhan_MaBenhNhanOrderByNgayTaoDesc(Integer maBenhNhan);

    /**
     * Tìm bệnh án theo khoảng thời gian
     */
    @Query("SELECT ba FROM BenhAn ba " +
            "WHERE ba.ngayTao BETWEEN :tuNgay AND :denNgay " +
            "ORDER BY ba.ngayTao DESC")
    List<BenhAn> findByNgayTaoBetween(@Param("tuNgay") LocalDateTime tuNgay,
                                      @Param("denNgay") LocalDateTime denNgay);

    /**
     * Tìm bệnh án theo bác sĩ và khoảng thời gian
     */
    @Query("SELECT ba FROM BenhAn ba " +
            "WHERE ba.bacSi.maBacSi = :maBacSi " +
            "AND ba.ngayTao BETWEEN :tuNgay AND :denNgay " +
            "ORDER BY ba.ngayTao DESC")
    Page<BenhAn> findByBacSiAndDateRange(@Param("maBacSi") Integer maBacSi,
                                         @Param("tuNgay") LocalDateTime tuNgay,
                                         @Param("denNgay") LocalDateTime denNgay,
                                         Pageable pageable);
}
