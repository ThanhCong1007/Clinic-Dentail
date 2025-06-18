package com.example.ClinicDentail.DTO;

import com.example.ClinicDentail.Enity.BenhAn;
import com.example.ClinicDentail.Enity.BenhNhan;
import com.example.ClinicDentail.Enity.DonThuoc;
import com.example.ClinicDentail.Enity.LichHen;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.math.BigDecimal;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhamBenhDTO {

    // Thông tin bệnh nhân
    private Integer maBenhNhan;
    private String hoTen;
    private String soDienThoai;
    private String email;
    private String diaChi;
    private LocalDate ngaySinh;
    private String gioiTinh;
    private String tienSuBenh;
    private String diUng;

    // Thông tin khám bệnh
    private Integer maLichHen;
    private Integer maBacSi;
    private String lyDoKham;
    private String chanDoan;
    private String ghiChuDieuTri;
    private LocalDate ngayTaiKham;
    private List<Integer> maDichVu;

    // Thông tin đơn thuốc
    private List<ChiTietThuocDTO> danhSachThuoc;
    private String ghiChuDonThuoc;

    // Thông tin phản hồi
    private Integer maBenhAn;
    private Integer maDonThuoc;
    private Integer maHoaDon;
    private BigDecimal tongTien;
    private String trangThaiKham;
    private LocalDateTime ngayKham;

    // DTO lồng bên trong
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChiTietThuocDTO {
        private Integer maThuoc;
        private String tenThuoc;
        private String lieuDung;
        private String tanSuat;
        private String thoiDiem;
        private Integer thoiGianDieuTri;
        private Integer soLuong;
        private String donViDung;
        private BigDecimal donGia;
        private String ghiChu;
        private String lyDoDonThuoc;
    }


}