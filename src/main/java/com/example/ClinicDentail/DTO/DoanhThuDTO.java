package com.example.ClinicDentail.DTO;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoanhThuDTO {
    private String nhan; // Nhãn cho trục X (ngày/tháng)
    private BigDecimal doanhThu; // Doanh thu
    private Integer soHoaDon; // Số lượng hóa đơn
    private String loaiThongKe; // NGAY/THANG/QUY/NAM

    // Dành cho API bác sĩ
    private Integer maBacSi;
    private String tenBacSi;
    private String chuyenKhoa;

    // Constructor cho tổng kết doanh thu chung
    public DoanhThuDTO(String nhan, BigDecimal doanhThu, Integer soHoaDon, String loaiThongKe) {
        this.nhan = nhan;
        this.doanhThu = doanhThu;
        this.soHoaDon = soHoaDon;
        this.loaiThongKe = loaiThongKe;
    }

    // Constructor cho doanh thu theo bác sĩ
    public DoanhThuDTO(Integer maBacSi, String tenBacSi, String chuyenKhoa,
                       String nhan, BigDecimal doanhThu, Integer soHoaDon, String loaiThongKe) {
        this.maBacSi = maBacSi;
        this.tenBacSi = tenBacSi;
        this.chuyenKhoa = chuyenKhoa;
        this.nhan = nhan;
        this.doanhThu = doanhThu;
        this.soHoaDon = soHoaDon;
        this.loaiThongKe = loaiThongKe;
    }
}