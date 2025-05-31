package com.example.ClinicDentail.DTO;

public
class TimeSlotDTO {
    private String gioBatDau;
    private String gioKetThuc;
    private boolean daDat;
    private String trangThai;

    // Constructors
    public TimeSlotDTO() {}

    public TimeSlotDTO(String gioBatDau, String gioKetThuc, boolean daDat, String trangThai) {
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
        this.daDat = daDat;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public String getGioBatDau() { return gioBatDau; }
    public void setGioBatDau(String gioBatDau) { this.gioBatDau = gioBatDau; }

    public String getGioKetThuc() { return gioKetThuc; }
    public void setGioKetThuc(String gioKetThuc) { this.gioKetThuc = gioKetThuc; }

    public boolean isDaDat() { return daDat; }
    public void setDaDat(boolean daDat) { this.daDat = daDat; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
