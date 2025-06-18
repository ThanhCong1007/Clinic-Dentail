package com.example.ClinicDentail.DTO;

import com.example.ClinicDentail.Enity.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
        if (lichHen == null) return;

        // Lịch hẹn cơ bản
        this.maLichHen = lichHen.getMaLichHen();
        this.ngayHen = lichHen.getNgayHen();
        this.gioBatDau = lichHen.getGioBatDau();
        this.gioKetThuc = lichHen.getGioKetThuc();
        this.ghiChuLichHen = lichHen.getGhiChu();
        this.lyDoHen = lichHen.getLydo();
        this.thoiGian = lichHen.getThoiLuong();

        // Bệnh nhân
        BenhNhan bn = lichHen.getBenhNhan();
        if (bn != null) {
            this.maBenhNhan = bn.getMaBenhNhan();
            this.tenBenhNhan = bn.getHoTen();
            this.soDienThoaiBenhNhan = bn.getSoDienThoai();
        }

        // Bác sĩ
        BacSi bs = lichHen.getBacSi();
        if (bs != null) {
            this.maBacSi = bs.getMaBacSi();
            if (bs.getNguoiDung() != null) {
                this.tenBacSi = bs.getNguoiDung().getHoTen();
            }
        }

        // Dịch vụ
        DichVu dv = lichHen.getDichVu();
        if (dv != null) {
            this.maDichVu = dv.getMaDichVu();
            this.tenDichVu = dv.getTenDichVu();
        }

        // Trạng thái lịch hẹn
        TrangThaiLichHen tt = lichHen.getTrangThai();
        if (tt != null) {
            this.maTrangThai = tt.getMaTrangThai();
            this.tenTrangThai = tt.getTenTrangThai();
        }

        // Thông tin bệnh án (nếu có)
        List<BenhAn> benhAnList = lichHen.getBenhAns();
        if (benhAnList != null && !benhAnList.isEmpty()) {
            BenhAn benhAn = benhAnList.get(0);
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