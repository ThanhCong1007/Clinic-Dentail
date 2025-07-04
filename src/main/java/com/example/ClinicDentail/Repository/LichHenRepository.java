package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.LichHen;
import com.example.ClinicDentail.Enity.TrangThaiLichHen;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface LichHenRepository extends JpaRepository<LichHen, Integer> {

    // Kiểm tra xem bác sĩ có lịch hẹn trùng trong khoảng thời gian không
    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM LichHen l " +
            "WHERE l.bacSi.maBacSi = :maBacSi " +
            "AND l.ngayHen = :ngayHen " +
            "AND l.gioBatDau < :gioKetThuc " +
            "AND l.gioKetThuc > :gioBatDau")
    boolean existsByMaBacSiAndNgayHenAndGioBatDauBeforeAndGioKetThucAfter(
            @Param("maBacSi") Integer maBacSi,
            @Param("ngayHen") LocalDate ngayHen,
            @Param("gioKetThuc") LocalTime gioKetThuc,
            @Param("gioBatDau") LocalTime gioBatDau);

    // Tìm tất cả các lịch hẹn của một bệnh nhân, sắp xếp theo ngày và giờ giảm dần
    List<LichHen> findByBenhNhan_MaBenhNhanOrderByNgayHenDescGioBatDauDesc(Integer maBenhNhan);


    // Kiểm tra xem bác sĩ có lịch hẹn trùng trong khoảng thời gian này không (loại trừ lịch hẹn hiện tại)
    boolean existsByBacSi_MaBacSiAndMaLichHenNotAndNgayHenAndGioBatDauBeforeAndGioKetThucAfter(
            Integer maBacSi, Integer maLichHen, LocalDate ngayHen, LocalTime gioBatDau, LocalTime gioKetThuc);


    /**
     * Tìm lịch hẹn theo bác sĩ và ngày, sắp xếp theo giờ bắt đầu
     */
    List<LichHen> findByBacSi_MaBacSiAndNgayHenOrderByGioBatDauAsc(Integer maBacSi, LocalDate ngayHen);

    /**
     * Kiểm tra xung đột lịch hẹn
     */
    @Query("SELECT COUNT(lh) FROM LichHen lh WHERE lh.bacSi.maBacSi = :maBacSi AND lh.ngayHen = :ngayHen AND " +
            "((:gioBatDau < lh.gioKetThuc AND :gioKetThuc > lh.gioBatDau))")
    long countConflictingAppointments(@Param("maBacSi") Integer maBacSi,
                                      @Param("ngayHen") LocalDate ngayHen,
                                      @Param("gioBatDau") LocalTime gioBatDau,
                                      @Param("gioKetThuc") LocalTime gioKetThuc);

    @Modifying
    @Transactional
    @Query(value = "UPDATE lich_hen SET ma_trang_thai = 5, ly_do = :lyDo " +
            "WHERE ngay_hen < CURRENT_DATE AND ma_trang_thai NOT IN (4, 5)",
            nativeQuery = true)
    int huyLichHenQuaHan(@Param("lyDo") String lyDo);


    // Query đếm số lượng
    @Query("SELECT COUNT(lh) FROM LichHen lh WHERE lh.ngayHen < CURRENT_DATE AND lh.trangThai.maTrangThai NOT IN (4, 5)")
    int demLichHenQuaHan();

    // Lấy danh sách chi tiết
    @Query("SELECT lh FROM LichHen lh WHERE lh.ngayHen < CURRENT_DATE AND lh.trangThai.maTrangThai NOT IN (4, 5)")
    List<LichHen> findLichHenQuaHan();

    List<LichHen> findByBacSi_MaBacSiOrderByNgayTaoDesc(Integer bacSiMaBacSi);


}
