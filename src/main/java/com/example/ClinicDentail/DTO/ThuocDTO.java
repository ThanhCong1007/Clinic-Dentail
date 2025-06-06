package com.example.ClinicDentail.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThuocDTO {
    private Integer maThuoc;
    private Integer maLoaiThuoc; // Chỉ dùng ID thay vì đối tượng LoaiThuoc

    private String tenThuoc;

    private String hoatChat;
    private String hamLuong;
    private String nhaSanXuat;
    private String nuocSanXuat;
    private String dangBaoChe;
    private String donViTinh;
    private String duongDung;
    private String huongDanSuDung;
    private String cachBaoQuan;

    private String phanLoaiDonThuoc; // Dùng String để dễ dàng ánh xạ Enum khi gửi JSON

    private String chongChiDinh;
    private String tacDungPhu;
    private String tuongTacThuoc;
    private String nhomThuocThaiKy;

    private BigDecimal gia;
    private Integer soLuongTon;
    private Integer nguongCanhBao;
    private Boolean trangThaiHoatDong;

    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
}
