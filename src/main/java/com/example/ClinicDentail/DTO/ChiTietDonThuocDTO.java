package com.example.ClinicDentail.DTO;

import com.example.ClinicDentail.Enity.ChiTietDonThuoc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietDonThuocDTO {
    private Integer maChiTiet;
    private Integer maThuoc;
    private String tenThuoc;
    private String hoatChat;
    private String hamLuong;
    private String dangBaoChe;
    private String lieudung;
    private String tanSuat;
    private String thoiDiem;
    private Integer thoiGianDieuTri;
    private Integer soLuong;
    private String donViDung;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private String ghiChu;
    private String lyDonThuoc;
}