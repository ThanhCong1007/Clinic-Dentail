package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.BenhAn;
import com.example.ClinicDentail.Enity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon,Integer> {
    /**
     * Tìm hóa đơn theo mã bệnh nhân, sắp xếp theo ngày tạo mới nhất
     */
    List<HoaDon> findByBenhNhan_MaBenhNhanOrderByNgayTaoDesc(Integer maBenhNhan);

    /**
     * Lấy tất cả hóa đơn, sắp xếp theo ngày tạo mới nhất
     */
    List<HoaDon> findAllByOrderByNgayTaoDesc();

    /**
     * Tìm hóa đơn theo ID với đầy đủ thông tin chi tiết
     */
    @Query("SELECT h FROM HoaDon h " +
            "LEFT JOIN FETCH h.benhNhan bn " +
            "LEFT JOIN FETCH h.lichHen lh " +
            "LEFT JOIN FETCH h.nguoiTao nt " +
            "LEFT JOIN FETCH h.chiTietHoaDons ct " +
            "WHERE h.maHoaDon = :maHoaDon")
    Optional<HoaDon> findByIdWithDetails(@Param("maHoaDon") Integer maHoaDon);

    /**
     * Kiểm tra hóa đơn có thuộc về bệnh nhân không
     */
    boolean existsByMaHoaDonAndBenhNhan_MaBenhNhan(Integer maHoaDon, Integer maBenhNhan);

}
