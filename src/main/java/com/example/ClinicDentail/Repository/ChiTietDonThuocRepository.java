package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.ChiTietDonThuoc;
import com.example.ClinicDentail.Enity.DonThuoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ChiTietDonThuocRepository extends JpaRepository<ChiTietDonThuoc, Integer> {

    // Tìm chi tiết theo đơn thuốc
    List<ChiTietDonThuoc> findByDonThuoc_MaDonThuoc(Integer maDonThuoc);

    // Tìm chi tiết theo thuốc
    List<ChiTietDonThuoc> findByThuoc_MaThuoc(Integer maThuoc);

    // Xóa chi tiết theo đơn thuốc
    void deleteByDonThuoc_MaDonThuoc(Integer maDonThuoc);

    // Tổng tiền theo đơn thuốc
    @Query("SELECT SUM(ct.thanhTien) FROM ChiTietDonThuoc ct WHERE ct.donThuoc.maDonThuoc = :maDonThuoc")
    BigDecimal tinhTongTienDonThuoc(@Param("maDonThuoc") Integer maDonThuoc);

    // Thống kê thuốc được kê nhiều nhất
    @Query("SELECT ct.thuoc.tenThuoc, SUM(ct.soLuong) as tongSoLuong " +
            "FROM ChiTietDonThuoc ct " +
            "GROUP BY ct.thuoc.maThuoc, ct.thuoc.tenThuoc " +
            "ORDER BY tongSoLuong DESC")
    List<Object[]> thongKeThuocKhaNhieu();

    List<ChiTietDonThuoc> findByDonThuoc(DonThuoc donThuoc);
}
