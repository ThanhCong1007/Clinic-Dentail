package com.example.ClinicDentail.DTO;

import com.example.ClinicDentail.Enity.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class BenhAnDTO {

    private Integer nguoiDung;
    // Thông tin bệnh án
    private Integer maBenhAn;
    private LocalDateTime ngayTao;

    // Thông tin lịch hẹn
    private Integer maLichHen;

    // Thông tin bác sĩ
    private Integer maBacSi;
    private String tenBacSi;

    // Thông tin bệnh nhân
    private Integer maBenhNhan;
    private String tenBenhNhan;
    private String soDienThoai;

    // Thông tin bệnh nhân vãng lai (khi không có tài khoản)
    private String hoTen;
    private LocalDate ngaySinh;
    private String gioiTinh;
    private String email;
    private String diaChi;
    private String tienSuBenh;
    private String diUng;

    // Thông tin khám bệnh
    private String lyDoKham;
    private String chanDoan;
    private String ghiChuDieuTri;
    private LocalDate ngayTaiKham;

    // Lịch hẹn mới (sau khám nếu có)
    private List<DichVuDTO> danhSachDichVu;
    private List<AnhBenhAnDTO> danhSachAnhBenhAn;
    private LocalDate ngayHenMoi;
    private LocalTime gioBatDauMoi;
    private LocalTime gioKetThucMoi;
    private String ghiChuLichHen;
    private Integer maLichHenMoi;

    // *** THÊM THÔNG TIN ĐON THUỐC ***
    private String maIcd;
    private String moTaChanDoan;
    private String ghiChuDonThuoc;
    private List<ChiTietDonThuocDTO> danhSachThuoc;
    private Integer maDonThuoc; // Mã đơn thuốc được tạo (response)

    // Thông báo kết quả xử lý nếu cần
    private String thongBao;
    private boolean editable;

    // Constructor từ Entity (dùng cho hiển thị)
    public BenhAnDTO(BenhAn benhAn) {
        this.maBenhAn = benhAn.getMaBenhAn();
        this.ngayTao = benhAn.getNgayTao();
        this.chanDoan = benhAn.getChanDoan();
//        this.nguoiDung = benhAn.getNguoiDung();
        if (benhAn.getLichHen() != null) {
            this.maLichHen = benhAn.getLichHen().getMaLichHen();
        }

        // Bệnh nhân
        if (benhAn.getBenhNhan() != null) {
            BenhNhan benhNhan = benhAn.getBenhNhan();
            if (benhNhan.getNguoiDung() != null) {
                this.nguoiDung = benhNhan.getNguoiDung().getMaNguoiDung();
            }
            var bn = benhAn.getBenhNhan();
            this.maBenhNhan = bn.getMaBenhNhan();
            this.tenBenhNhan = bn.getHoTen();  // Tên rút gọn
            this.hoTen = bn.getHoTen();        // Họ tên đầy đủ (cùng dữ liệu nếu không tách)
            this.soDienThoai = bn.getSoDienThoai();
            this.ngaySinh = bn.getNgaySinh();
            this.gioiTinh = (bn.getGioiTinh() != null) ? bn.getGioiTinh().toString() : null;
            this.email = bn.getEmail();
            this.diaChi = bn.getDiaChi();
            this.tienSuBenh = bn.getTienSuBenh();
            this.diUng = bn.getDiUng();
        }

        // Bác sĩ
        if (benhAn.getBacSi() != null && benhAn.getBacSi().getNguoiDung() != null) {
            this.maBacSi = benhAn.getBacSi().getMaBacSi();
            this.tenBacSi = benhAn.getBacSi().getNguoiDung().getHoTen().toString();
        }

        // Khám bệnh
        this.lyDoKham = benhAn.getLyDoKham();
        this.chanDoan = benhAn.getChanDoan();
        this.ghiChuDieuTri = benhAn.getGhiChuDieuTri();
        this.ngayTaiKham = benhAn.getNgayTaiKham();
    }
    public BenhAnDTO(BenhAn benhAn, List<BenhAnDichVu> danhSachDichVu, DonThuoc donThuoc, List<ChiTietDonThuoc> danhSachThuoc) {
        this.maBenhAn = benhAn.getMaBenhAn();
        this.ngayTao = benhAn.getNgayTao();
        this.lyDoKham = benhAn.getLyDoKham();
        this.chanDoan = benhAn.getChanDoan();
        this.ghiChuDieuTri = benhAn.getGhiChuDieuTri();
        this.ngayTaiKham = benhAn.getNgayTaiKham();

        // Lịch hẹn
        if (benhAn.getLichHen() != null) {
            this.maLichHen = benhAn.getLichHen().getMaLichHen();
        }

        // Bác sĩ
        if (benhAn.getBacSi() != null && benhAn.getBacSi().getNguoiDung() != null) {
            this.maBacSi = benhAn.getBacSi().getMaBacSi();
            this.tenBacSi = benhAn.getBacSi().getNguoiDung().getHoTen();
        }

        // Bệnh nhân
        if (benhAn.getBenhNhan() != null) {
            BenhNhan bn = benhAn.getBenhNhan();
            this.maBenhNhan = bn.getMaBenhNhan();
            this.tenBenhNhan = bn.getHoTen();
            this.hoTen = bn.getHoTen();
            this.soDienThoai = bn.getSoDienThoai();
            this.ngaySinh = bn.getNgaySinh();
            this.gioiTinh = (bn.getGioiTinh() != null) ? bn.getGioiTinh().toString() : null;
            this.email = bn.getEmail();
            this.diaChi = bn.getDiaChi();
            this.tienSuBenh = bn.getTienSuBenh();
            this.diUng = bn.getDiUng();

            if (bn.getNguoiDung() != null) {
                this.nguoiDung = bn.getNguoiDung().getMaNguoiDung();
            }
        }

        // Danh sách dịch vụ (mỗi dịch vụ có giá riêng)
        if (danhSachDichVu != null) {
            this.danhSachDichVu = danhSachDichVu.stream()
                    .map(DichVuDTO::new)
                    .collect(Collectors.toList());
        }

        // Đơn thuốc
        if (donThuoc != null) {
            this.maDonThuoc = donThuoc.getMaDonThuoc();
            this.maIcd = donThuoc.getMaIcd();
            this.moTaChanDoan = donThuoc.getMoTaChanDoan();
            this.ghiChuDonThuoc = donThuoc.getGhiChu();
        }

        // Chi tiết thuốc
        if (danhSachThuoc != null) {
            this.danhSachThuoc = danhSachThuoc.stream()
                    .map(ChiTietDonThuocDTO::new)
                    .collect(Collectors.toList());
        }
    }


    public BenhAnDTO(BenhAn benhAn, DonThuoc donThuoc, LichHen lichHenMoi) {
        // Thông tin bệnh án
        this.maBenhAn = benhAn.getMaBenhAn();
        this.ngayTao = benhAn.getNgayTao();

        // Lịch hẹn gốc
        if (benhAn.getLichHen() != null) {
            this.maLichHen = benhAn.getLichHen().getMaLichHen();
        }

        // Thông tin bệnh nhân
        if (benhAn.getBenhNhan() != null) {
            BenhNhan bn = benhAn.getBenhNhan();
            this.maBenhNhan = bn.getMaBenhNhan();
            this.tenBenhNhan = bn.getHoTen();
            this.hoTen = bn.getHoTen();
            this.soDienThoai = bn.getSoDienThoai();
            this.ngaySinh = bn.getNgaySinh();
            this.gioiTinh = bn.getGioiTinh() != null ? bn.getGioiTinh().toString() : null;
            this.email = bn.getEmail();
            this.diaChi = bn.getDiaChi();
            this.tienSuBenh = bn.getTienSuBenh();
            this.diUng = bn.getDiUng();

            if (bn.getNguoiDung() != null) {
                this.nguoiDung = bn.getNguoiDung().getMaNguoiDung();
            }
        }

        // Thông tin bác sĩ
        if (benhAn.getBacSi() != null) {
            this.maBacSi = benhAn.getBacSi().getMaBacSi();
            if (benhAn.getBacSi().getNguoiDung() != null) {
                this.tenBacSi = benhAn.getBacSi().getNguoiDung().getHoTen();
            }
        }

        // Thông tin khám bệnh
        this.lyDoKham = benhAn.getLyDoKham();
        this.chanDoan = benhAn.getChanDoan();
        this.ghiChuDieuTri = benhAn.getGhiChuDieuTri();
        this.ngayTaiKham = benhAn.getNgayTaiKham();

        // Đơn thuốc nếu có
        if (donThuoc != null) {
            this.maDonThuoc = donThuoc.getMaDonThuoc();
            this.moTaChanDoan = donThuoc.getMoTaChanDoan();
            this.ghiChuDonThuoc = donThuoc.getGhiChu();
            this.maIcd = donThuoc.getMaIcd();
        }

        // Lịch hẹn mới sau khám nếu có
        if (lichHenMoi != null) {
            this.maLichHenMoi = lichHenMoi.getMaLichHen();
            this.ngayHenMoi = lichHenMoi.getNgayHen();
            this.gioBatDauMoi = lichHenMoi.getGioBatDau();
            this.gioKetThucMoi = lichHenMoi.getGioKetThuc();
            this.ghiChuLichHen = lichHenMoi.getLydo();
        }

        // Tùy chọn: gán thông báo mặc định
        this.thongBao = "Lấy thông tin bệnh án thành công";
    }

}