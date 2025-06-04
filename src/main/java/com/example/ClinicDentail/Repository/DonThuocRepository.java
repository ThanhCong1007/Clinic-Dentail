package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.DonThuoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DonThuocRepository extends JpaRepository<DonThuoc, Integer> {

    // Đếm số lượng đơn thuốc trong ngày
    Long countByNgayKe(LocalDate ngayKe);

    // Tìm đơn thuốc theo bệnh án
    List<DonThuoc> findByBenhAn_MaBenhAn(Integer maBenhAn);

    // Tìm đơn thuốc theo bệnh nhân
    List<DonThuoc> findByBenhNhan_MaBenhNhan(Integer maBenhNhan);

    // Tìm đơn thuốc theo bác sĩ
    List<DonThuoc> findByBacSi_MaBacSi(Integer maBacSi);

    // Tìm đơn thuốc theo trạng thái
    List<DonThuoc> findByTrangThaiToa(DonThuoc.TrangThaiToa trangThaiToa);

    // Tìm đơn thuốc theo ngày kê
    List<DonThuoc> findByNgayKeBetween(LocalDate tuNgay, LocalDate denNgay);

    // Tìm đơn thuốc sắp hết hạn
    List<DonThuoc> findByNgayHetHanBefore(LocalDate ngayHienTai);

    // Tìm theo số toa
    Optional<DonThuoc> findBySoToa(String soToa);
}