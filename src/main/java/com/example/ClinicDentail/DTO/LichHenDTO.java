package com.example.ClinicDentail.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

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
    private long thoiGian; // thời lượng phút

    // Constructors
    public LichHenDTO() {
    }

    // Constructor từ entity
    public LichHenDTO(com.example.ClinicDentail.Enity.LichHen lichHen) {
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
        this.thoiGian = lichHen.getThoiLuong();
    }

    // Getters and Setters
    public Integer getMaLichHen() {
        return maLichHen;
    }

    public void setMaLichHen(Integer maLichHen) {
        this.maLichHen = maLichHen;
    }

    public Integer getMaBenhNhan() {
        return maBenhNhan;
    }

    public void setMaBenhNhan(Integer maBenhNhan) {
        this.maBenhNhan = maBenhNhan;
    }

    public String getTenBenhNhan() {
        return tenBenhNhan;
    }

    public void setTenBenhNhan(String tenBenhNhan) {
        this.tenBenhNhan = tenBenhNhan;
    }

    public String getSoDienThoaiBenhNhan() {
        return soDienThoaiBenhNhan;
    }

    public void setSoDienThoaiBenhNhan(String soDienThoaiBenhNhan) {
        this.soDienThoaiBenhNhan = soDienThoaiBenhNhan;
    }

    public Integer getMaBacSi() {
        return maBacSi;
    }

    public void setMaBacSi(Integer maBacSi) {
        this.maBacSi = maBacSi;
    }

    public String getTenBacSi() {
        return tenBacSi;
    }

    public void setTenBacSi(String tenBacSi) {
        this.tenBacSi = tenBacSi;
    }

    public Integer getMaDichVu() {
        return maDichVu;
    }

    public void setMaDichVu(Integer maDichVu) {
        this.maDichVu = maDichVu;
    }

    public String getTenDichVu() {
        return tenDichVu;
    }

    public void setTenDichVu(String tenDichVu) {
        this.tenDichVu = tenDichVu;
    }

    public LocalDate getNgayHen() {
        return ngayHen;
    }

    public void setNgayHen(LocalDate ngayHen) {
        this.ngayHen = ngayHen;
    }

    public LocalTime getGioBatDau() {
        return gioBatDau;
    }

    public void setGioBatDau(LocalTime gioBatDau) {
        this.gioBatDau = gioBatDau;
    }

    public LocalTime getGioKetThuc() {
        return gioKetThuc;
    }

    public void setGioKetThuc(LocalTime gioKetThuc) {
        this.gioKetThuc = gioKetThuc;
    }

    public Integer getMaTrangThai() {
        return maTrangThai;
    }

    public void setMaTrangThai(Integer maTrangThai) {
        this.maTrangThai = maTrangThai;
    }

    public String getTenTrangThai() {
        return tenTrangThai;
    }

    public void setTenTrangThai(String tenTrangThai) {
        this.tenTrangThai = tenTrangThai;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public long getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(long thoiGian) {
        this.thoiGian = thoiGian;
    }
}
