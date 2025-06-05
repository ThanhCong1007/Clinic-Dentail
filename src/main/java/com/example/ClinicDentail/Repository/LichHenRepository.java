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

    /**
     * Tìm lịch hẹn theo bác sĩ và ngày, sắp xếp theo giờ bắt đầu
     */
    List<LichHen> findByBacSi_MaBacSiAndNgayHenOrderByGioBatDauAsc(Integer maBacSi, LocalDate ngayHen);

    /**
     * Tìm lịch hẹn theo bác sĩ trong khoảng thời gian
     */
    List<LichHen> findByBacSi_MaBacSiAndNgayHenBetweenOrderByNgayHenAscGioBatDauAsc(Integer maBacSi, LocalDate fromDate, LocalDate toDate);

    /**
     * Đếm lịch hẹn trong ngày
     */
    long countByNgayHen(LocalDate ngayHen);

    /**
     * Đếm lịch hẹn của bác sĩ trong ngày
     */
    long countByBacSi_MaBacSiAndNgayHen(Integer maBacSi, LocalDate ngayHen);

    /**
     * Kiểm tra xung đột lịch hẹn
     */
    @Query("SELECT COUNT(lh) FROM LichHen lh WHERE lh.bacSi.maBacSi = :maBacSi AND lh.ngayHen = :ngayHen AND " +
            "((:gioBatDau < lh.gioKetThuc AND :gioKetThuc > lh.gioBatDau))")
    long countConflictingAppointments(@Param("maBacSi") Integer maBacSi,
                                      @Param("ngayHen") LocalDate ngayHen,
                                      @Param("gioBatDau") LocalTime gioBatDau,
                                      @Param("gioKetThuc") LocalTime gioKetThuc);

    /**
     * Lấy lịch hẹn sắp tới của bác sĩ
     */
    @Query("SELECT lh FROM LichHen lh WHERE lh.bacSi.maBacSi = :maBacSi AND " +
            "(lh.ngayHen > :currentDate OR (lh.ngayHen = :currentDate AND lh.gioBatDau > :currentTime)) " +
            "ORDER BY lh.ngayHen ASC, lh.gioBatDau ASC")
    List<LichHen> findUpcomingAppointmentsByDoctor(@Param("maBacSi") Integer maBacSi,
                                                   @Param("currentDate") LocalDate currentDate,
                                                   @Param("currentTime") LocalTime currentTime,
                                                   @Param("limit") int limit);

    /**
     * Tìm lịch hẹn theo trạng thái
     */
    List<LichHen> findByTrangThai_MaTrangThaiOrderByNgayHenDescGioBatDauDesc(Integer maTrangThai);

    /**
     * Đếm lịch hẹn theo trạng thái
     */
    long countByTrangThai_MaTrangThai(Integer maTrangThai);

    /**
     * Tìm lịch hẹn theo dịch vụ
     */
    List<LichHen> findByDichVu_MaDichVuOrderByNgayHenDescGioBatDauDesc(Integer maDichVu);

    /**
     * Lấy thống kê lịch hẹn theo tháng
     */
    @Query("SELECT DAY(lh.ngayHen) as day, COUNT(lh) as count " +
            "FROM LichHen lh WHERE YEAR(lh.ngayHen) = :year AND MONTH(lh.ngayHen) = :month " +
            "GROUP BY DAY(lh.ngayHen) ORDER BY day")
    List<Object[]> getMonthlyAppointmentStats(@Param("year") int year, @Param("month") int month);

    /**
     * Lấy thống kê lịch hẹn theo tuần
     */
    @Query("SELECT DAYOFWEEK(lh.ngayHen) as dayOfWeek, COUNT(lh) as count " +
            "FROM LichHen lh WHERE lh.ngayHen BETWEEN :startDate AND :endDate " +
            "GROUP BY DAYOFWEEK(lh.ngayHen) ORDER BY dayOfWeek")
    List<Object[]> getWeeklyAppointmentStats(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Lấy lịch hẹn trong khoảng thời gian với trạng thái cụ thể
     */
    List<LichHen> findByNgayHenBetweenAndTrangThai_MaTrangThaiOrderByNgayHenAscGioBatDauAsc(
            LocalDate fromDate, LocalDate toDate, Integer maTrangThai);

    /**
     * Tìm lịch hẹn theo multiple điều kiện
     */
    @Query("SELECT lh FROM LichHen lh WHERE " +
            "(:maBacSi IS NULL OR lh.bacSi.maBacSi = :maBacSi) AND " +
            "(:maBenhNhan IS NULL OR lh.benhNhan.maBenhNhan = :maBenhNhan) AND " +
            "(:maTrangThai IS NULL OR lh.trangThai.maTrangThai = :maTrangThai) AND " +
            "(:fromDate IS NULL OR lh.ngayHen >= :fromDate) AND " +
            "(:toDate IS NULL OR lh.ngayHen <= :toDate) " +
            "ORDER BY lh.ngayHen DESC, lh.gioBatDau DESC")
    List<LichHen> findAppointmentsByMultipleConditions(
            @Param("maBacSi") Integer maBacSi,
            @Param("maBenhNhan") Integer maBenhNhan,
            @Param("maTrangThai") Integer maTrangThai,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate);

    /**
     * Đếm tổng số lịch hẹn hoàn thành trong tháng
     */
    @Query("SELECT COUNT(lh) FROM LichHen lh WHERE YEAR(lh.ngayHen) = :year AND MONTH(lh.ngayHen) = :month AND lh.trangThai.maTrangThai = :completedStatus")
    long countCompletedAppointmentsInMonth(@Param("year") int year, @Param("month") int month, @Param("completedStatus") Integer completedStatus);

    /**
     * Lấy top bác sĩ có nhiều lịch hẹn nhất trong tháng
     */
    @Query("SELECT lh.bacSi, COUNT(lh) as appointmentCount FROM LichHen lh " +
            "WHERE YEAR(lh.ngayHen) = :year AND MONTH(lh.ngayHen) = :month " +
            "GROUP BY lh.bacSi ORDER BY appointmentCount DESC")
    List<Object[]> getTopDoctorsByAppointmentsInMonth(@Param("year") int year, @Param("month") int month);


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
