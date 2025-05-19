package com.example.ClinicDentail.payload.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentRequest {
    @NotNull(message = "Mã bệnh nhân không được để trống")
    private Integer maBenhNhan;

    @NotNull(message = "Mã bác sĩ không được để trống")
    private Integer maBacSi;

    private Integer maDichVu;

    @NotNull(message = "Ngày hẹn không được để trống")
    private LocalDate ngayHen;

    @NotNull(message = "Giờ bắt đầu không được để trống")
    private LocalTime gioBatDau;

    @NotNull(message = "Giờ kết thúc không được để trống")
    private LocalTime gioKetThuc;

    @NotNull(message = "Mã trạng thái không được để trống")
    private Integer maTrangThai;

    private String ghiChu;

    // Getters and Setters
    public Integer getMaBenhNhan() {
        return maBenhNhan;
    }

    public void setMaBenhNhan(Integer maBenhNhan) {
        this.maBenhNhan = maBenhNhan;
    }

    public Integer getMaBacSi() {
        return maBacSi;
    }

    public void setMaBacSi(Integer maBacSi) {
        this.maBacSi = maBacSi;
    }

    public Integer getMaDichVu() {
        return maDichVu;
    }

    public void setMaDichVu(Integer maDichVu) {
        this.maDichVu = maDichVu;
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

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}