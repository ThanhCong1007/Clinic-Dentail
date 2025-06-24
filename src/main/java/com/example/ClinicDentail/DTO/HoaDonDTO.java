package com.example.ClinicDentail.DTO;

import com.example.ClinicDentail.Enity.ChiTietHoaDon;
import com.example.ClinicDentail.Enity.HoaDon;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoaDonDTO {
    private Integer maHoaDon;
    private Integer maBenhNhan;
    private String tenBenhNhan;
    private String soDienThoai;
    private String email;
    private Integer maLichHen;
    private BigDecimal tongTien;
    private BigDecimal thanhTien;
    private LocalDateTime ngayHoaDon;
    private String trangThai;
    private Integer nguoiTao;
    private String tenNguoiTao;
    private LocalDateTime ngayTao;
    private List<ChiTietHoaDonDTO> chiTietHoaDon;


    // Constructor từ Entity
    public HoaDonDTO(HoaDon hoaDon) {
        this.maHoaDon = hoaDon.getMaHoaDon();
        this.tongTien = hoaDon.getTongTien();
        this.thanhTien = hoaDon.getThanhTien();
        this.ngayHoaDon = hoaDon.getNgayHoaDon();
        this.trangThai = hoaDon.getTrangThai().toString();
        this.ngayTao = hoaDon.getNgayTao();

        // Thông tin bệnh nhân
        if (hoaDon.getBenhNhan() != null) {
            this.maBenhNhan = hoaDon.getBenhNhan().getMaBenhNhan();
            this.tenBenhNhan = hoaDon.getBenhNhan().getHoTen();
            this.soDienThoai = hoaDon.getBenhNhan().getSoDienThoai();
            this.email = hoaDon.getBenhNhan().getEmail();
        }

        // Thông tin lịch hẹn
        if (hoaDon.getLichHen() != null) {
            this.maLichHen = hoaDon.getLichHen().getMaLichHen();
        }

        // Thông tin người tạo
        if (hoaDon.getNguoiTao() != null) {
            this.nguoiTao = hoaDon.getNguoiTao().getMaNguoiDung();
            this.tenNguoiTao = hoaDon.getNguoiTao().getHoTen();
        }

        // Chi tiết hóa đơn
        if (hoaDon.getChiTietHoaDons() != null && !hoaDon.getChiTietHoaDons().isEmpty()) {
            this.chiTietHoaDon = hoaDon.getChiTietHoaDons().stream()
                    .map(ChiTietHoaDonDTO::new)
                    .collect(Collectors.toList());
        }
    }

    // Inner class cho chi tiết hóa đơn
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChiTietHoaDonDTO {
        private Integer maMuc;
        private Integer maHoaDon;
        private Integer maBenhAnDichVu;
        private String moTa;
        private Integer soLuong;
        private BigDecimal donGia;
        private BigDecimal thanhTien;
        private LocalDateTime ngayTao;

        public ChiTietHoaDonDTO(ChiTietHoaDon chiTiet) {
            this.maMuc = chiTiet.getMaMuc();
            this.maHoaDon = chiTiet.getHoaDon() != null ? chiTiet.getHoaDon().getMaHoaDon() : null;

            // Tránh lỗi nếu BenhAnDichVu hoặc DichVu null
            if (chiTiet.getBenhAnDichVu() != null && chiTiet.getBenhAnDichVu().getDichVu() != null) {
                this.maBenhAnDichVu = chiTiet.getBenhAnDichVu().getDichVu().getMaDichVu();
            } else {
                this.maBenhAnDichVu = null; // hoặc -1 nếu bạn muốn đánh dấu rõ
            }

            this.moTa = chiTiet.getMoTa();
            this.soLuong = chiTiet.getSoLuong();
            this.donGia = chiTiet.getDonGia();
            this.thanhTien = chiTiet.getThanhTien();
            this.ngayTao = chiTiet.getNgayTao();
        }
    }

}