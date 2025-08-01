package com.example.ClinicDentail.DTO;

import com.example.ClinicDentail.Enity.BenhAn;
import com.example.ClinicDentail.Enity.DichVu;
import com.example.ClinicDentail.Enity.LichHen;
import com.example.ClinicDentail.Enity.TrangThaiLichHen;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
@NoArgsConstructor
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



}
