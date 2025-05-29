package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.LichHen;
import org.springframework.data.jpa.repository.JpaRepository;
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
    @Query("SELECT l FROM LichHen l WHERE l.bacSi.maBacSi = :maBacSi AND l.ngayHen = :ngayHen AND " +
            "((l.gioBatDau <= :gioBatDau AND l.gioKetThuc > :gioBatDau) OR " +
            "(l.gioBatDau < :gioKetThuc AND l.gioKetThuc >= :gioKetThuc) OR " +
            "(l.gioBatDau >= :gioBatDau AND l.gioKetThuc <= :gioKetThuc))")
    List<LichHen> findConflictingAppointments(@Param("maBacSi") Integer maBacSi,
                                              @Param("ngayHen") LocalDate ngayHen,
                                              @Param("gioBatDau") LocalTime gioBatDau,
                                              @Param("gioKetThuc") LocalTime gioKetThuc);
////     Kiểm tra xem bác sĩ có lịch hẹn trùng trong khoảng thời gian này không
//    boolean existsByMaBacSiAndNgayHenAndGioBatDauBeforeAndGioKetThucAfter(
//            Integer maBacSi, LocalDate ngayHen, LocalTime gioKetThuc, LocalTime gioBatDau);

    // Tìm tất cả các lịch hẹn của một bệnh nhân, sắp xếp theo ngày và giờ giảm dần
    List<LichHen> findByBenhNhan_MaBenhNhanOrderByNgayHenDescGioBatDauDesc(Integer maBenhNhan);


    // Kiểm tra xem bác sĩ có lịch hẹn trùng trong khoảng thời gian này không (loại trừ lịch hẹn hiện tại)
    boolean existsByBacSi_MaBacSiAndMaLichHenNotAndNgayHenAndGioBatDauBeforeAndGioKetThucAfter(
            Integer maBacSi, Integer maLichHen, LocalDate ngayHen, LocalTime gioBatDau, LocalTime gioKetThuc);

    // Đếm số lượng lịch hẹn của một bệnh nhân
    long countByBenhNhan_MaBenhNhan(Integer maBenhNhan);

    // Đếm số lượng lịch hẹn theo trạng thái trong khoảng thời gian
    long countByTrangThai_MaTrangThaiAndNgayHenBetween(Integer maTrangThai, LocalDate startDate, LocalDate endDate);

}
