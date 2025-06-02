package com.example.ClinicDentail.DTO;

import com.example.ClinicDentail.Enity.LichHen;
import com.example.ClinicDentail.Enity.BenhAn;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class LichHenBenhAnDTO {
    // Thông tin lịch hẹn
    private Integer maLichHen;
    private Integer maBenhNhan;
    private String tenBenhNhan;
    private String soDienThoaiBenhNhan;
    private Integer maBacSi;
    private String tenBacSi;
    private Integer maDichVu;
    private String tenDichVu;
    private LocalDate ngayHen;
    private LocalTime gioBatDau;
    private LocalTime gioKetThuc;
    private Integer maTrangThai;
    private String tenTrangThai;
    private String ghiChuLichHen;
    private String lyDoHen;
    private long thoiGian;

    // Thông tin bệnh án (nullable)
    private Integer maBenhAn;
    private String lyDoKham;
    private String chanDoan;
    private String ghiChuDieuTri;
    private LocalDate ngayTaiKham;
    private LocalDateTime ngayTaoBenhAn;
    private Boolean coBenhAn;

    public LichHenBenhAnDTO(LichHen lichHen) {
        // Thông tin lịch hẹn
        this.maLichHen = lichHen.getMaLichHen();

        if (lichHen.getBenhNhan() != null) {
            this.maBenhNhan = lichHen.getBenhNhan().getMaBenhNhan();
            this.tenBenhNhan = lichHen.getBenhNhan().getHoTen();
            this.soDienThoaiBenhNhan = lichHen.getBenhNhan().getSoDienThoai();
        }

        if (lichHen.getBacSi() != null) {
            this.maBacSi = lichHen.getBacSi().getMaBacSi();
            if (lichHen.getBacSi().getNguoiDung() != null) {
                this.tenBacSi = lichHen.getBacSi().getNguoiDung().getHoTen();
            }
        }

        if (lichHen.getDichVu() != null) {
            this.maDichVu = lichHen.getDichVu().getMaDichVu();
            this.tenDichVu = lichHen.getDichVu().getTenDichVu();
        }

        if (lichHen.getTrangThai() != null) {
            this.maTrangThai = lichHen.getTrangThai().getMaTrangThai();
            this.tenTrangThai = lichHen.getTrangThai().getTenTrangThai();
        }

        this.ngayHen = lichHen.getNgayHen();
        this.gioBatDau = lichHen.getGioBatDau();
        this.gioKetThuc = lichHen.getGioKetThuc();
        this.ghiChuLichHen = lichHen.getGhiChu();
        this.lyDoHen = lichHen.getLydo();
        this.thoiGian = lichHen.getThoiLuong();

        // Thông tin bệnh án (nếu có)
        if (lichHen.getBenhAns() != null && !lichHen.getBenhAns().isEmpty()) {
            BenhAn benhAn = lichHen.getBenhAns().get(0);
            this.maBenhAn = benhAn.getMaBenhAn();
            this.lyDoKham = benhAn.getLyDoKham();
            this.chanDoan = benhAn.getChanDoan();
            this.ghiChuDieuTri = benhAn.getGhiChuDieuTri();
            this.ngayTaiKham = benhAn.getNgayTaiKham();
            this.ngayTaoBenhAn = benhAn.getNgayTao();
            this.coBenhAn = true;
        } else {
            this.coBenhAn = false;
        }
    }
}