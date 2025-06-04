package com.example.ClinicDentail.Repository;

import com.example.ClinicDentail.Enity.Thuoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThuocRepository extends JpaRepository<Thuoc, Integer> {

    // Tìm thuốc theo tên
    List<Thuoc> findByTenThuocContainingIgnoreCase(String tenThuoc);

    // Tìm thuốc theo hoạt chất
    List<Thuoc> findByHoatChatContainingIgnoreCase(String hoatChat);

    // Tìm thuốc theo trạng thái hoạt động
    List<Thuoc> findByTrangThaiHoatDong(Boolean trangThaiHoatDong);

    // Tìm thuốc sắp hết hàng
    List<Thuoc> findBySoLuongTonLessThanEqual(Integer soLuong);

    // Tìm thuốc theo loại thuốc
    List<Thuoc> findByLoaiThuoc_MaLoaiThuoc(Integer maLoaiThuoc);

    // Tìm thuốc theo phân loại kê đơn
    List<Thuoc> findByPhanLoaiKeDon(Thuoc.PhanLoaiKeDon phanLoaiKeDon);

    // Tìm thuốc có tồn kho
    List<Thuoc> findByTrangThaiHoatDongTrueAndSoLuongTonGreaterThan(Integer soLuong);
}
