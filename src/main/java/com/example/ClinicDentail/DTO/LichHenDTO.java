package com.example.ClinicDentail.DTO;

import com.example.ClinicDentail.Enity.BenhAn;
import com.example.ClinicDentail.Enity.DichVu;
import com.example.ClinicDentail.Enity.LichHen;
import com.example.ClinicDentail.Enity.TrangThaiLichHen;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Data
public class LichHenDTO {
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
    private String ghiChu;
    private String lydo;
    private long thoiGian; // thời lượng phút

    // Constructors
    public LichHenDTO() {
    }

    // Constructor từ entity
    public LichHenDTO(LichHen lichHen) {
        this.maLichHen = lichHen.getMaLichHen();

        if (lichHen.getBenhNhan() != null) {
            this.maBenhNhan = lichHen.getBenhNhan().getMaBenhNhan();
            this.tenBenhNhan = lichHen.getBenhNhan().getHoTen();
            this.soDienThoaiBenhNhan = lichHen.getBenhNhan().getSoDienThoai();
        }

        if (lichHen.getBacSi() != null) {
            this.maBacSi = lichHen.getBacSi().getMaBacSi();
            // Assuming that NguoiDung has a field hoTen
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
        this.ghiChu = lichHen.getGhiChu();
        this.lydo = lichHen.getLydo();
        this.thoiGian = lichHen.getThoiLuong();
    }
    public LichHenDTO(BenhAn benhAn, BenhAnDTO dto, DichVu dichVu, TrangThaiLichHen trangThai) {
        if (benhAn.getBenhNhan() != null) {
            this.maBenhNhan = benhAn.getBenhNhan().getMaBenhNhan();
            this.tenBenhNhan = benhAn.getBenhNhan().getHoTen();
            this.soDienThoaiBenhNhan = benhAn.getBenhNhan().getSoDienThoai();
        }

        if (benhAn.getBacSi() != null) {
            this.maBacSi = benhAn.getBacSi().getMaBacSi();
            if (benhAn.getBacSi().getNguoiDung() != null) {
                this.tenBacSi = benhAn.getBacSi().getNguoiDung().getHoTen();
            }
        }

        if (dichVu != null) {
            this.maDichVu = dichVu.getMaDichVu();
            this.tenDichVu = dichVu.getTenDichVu();
        }

        if (trangThai != null) {
            this.maTrangThai = trangThai.getMaTrangThai();
            this.tenTrangThai = trangThai.getTenTrangThai();
        }

        this.ngayHen = dto.getNgayHenMoi();
        this.gioBatDau = dto.getGioBatDauMoi();
        this.gioKetThuc = dto.getGioKetThucMoi();

        if (dto.getGhiChuLichHen() != null && !dto.getGhiChuLichHen().isBlank()) {
            this.lydo = dto.getGhiChuLichHen().trim();
        } else if (dto.getChanDoan() != null && !dto.getChanDoan().isBlank()) {
            this.lydo = "Tái khám: " + dto.getChanDoan().trim();
        } else {
            this.lydo = "Lịch hẹn tái khám";
        }

        if (gioBatDau != null && gioKetThuc != null) {
            this.thoiGian = java.time.Duration.between(gioBatDau, gioKetThuc).toMinutes();
        }
    }


}
