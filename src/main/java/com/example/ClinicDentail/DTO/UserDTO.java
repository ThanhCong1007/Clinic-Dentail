package com.example.ClinicDentail.DTO;

import com.example.ClinicDentail.Enity.BacSi;
import com.example.ClinicDentail.Enity.BenhNhan;
import com.example.ClinicDentail.Enity.NguoiDung;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Data
public class UserDTO {
    // Thông tin cơ bản từ bảng nguoi_dung
    private Integer maNguoiDung;
    private String tenDangNhap;
    private String email;
    private String hoTen;
    private String soDienThoai;
    private Boolean trangThaiHoatDong;
    private LocalDateTime ngayTao;
    private Integer maVaiTro;
    private String tenVaiTro;

    // Thông tin bác sĩ (chỉ có khi role = BACSI)
    private Integer maBacSi;
    private String chuyenKhoa;
    private Integer soNamKinhNghiem;
    private Boolean trangThaiLamViec;

    // Thông tin bệnh nhân (chỉ có khi role = USER)
    private Integer maBenhNhan;
    private LocalDate ngaySinh;
    private String gioiTinh;
    private String diaChi;
    private String tienSuBenh;
    private String diUng;
    private Integer tuoi; // Tính từ ngày sinh

    // Constructor mặc định
    public UserDTO() {}

    // Constructor từ NguoiDung entity
    public UserDTO(NguoiDung nguoiDung) {
        this.maNguoiDung = nguoiDung.getMaNguoiDung();
        this.tenDangNhap = nguoiDung.getTenDangNhap();
        this.email = nguoiDung.getEmail();
        this.hoTen = nguoiDung.getHoTen();
        this.soDienThoai = nguoiDung.getSoDienThoai();
        this.trangThaiHoatDong = nguoiDung.getTrangThaiHoatDong();
        this.ngayTao = nguoiDung.getNgayTao();

        if (nguoiDung.getVaiTro() != null) {
            this.maVaiTro = nguoiDung.getVaiTro().getMaVaiTro();
            this.tenVaiTro = nguoiDung.getVaiTro().getTenVaiTro();
        }
    }

    // Constructor từ BacSi entity
    public UserDTO(BacSi bacSi) {
        this(bacSi.getNguoiDung()); // Gọi constructor trên

        this.maBacSi = bacSi.getMaBacSi();
        this.chuyenKhoa = bacSi.getChuyenKhoa();
        this.soNamKinhNghiem = bacSi.getSoNamKinhNghiem();
        this.trangThaiLamViec = bacSi.getTrangThaiLamViec();
    }

    // Constructor từ BenhNhan entity
    public UserDTO(BenhNhan benhNhan) {
        if (benhNhan.getNguoiDung() != null) {
            NguoiDung nguoiDung = benhNhan.getNguoiDung();
            this.maNguoiDung = nguoiDung.getMaNguoiDung();
            this.tenDangNhap = nguoiDung.getTenDangNhap();
            this.email = nguoiDung.getEmail();
            this.hoTen = nguoiDung.getHoTen();
            this.soDienThoai = nguoiDung.getSoDienThoai();
            this.trangThaiHoatDong = nguoiDung.getTrangThaiHoatDong();
            this.ngayTao = nguoiDung.getNgayTao();

            if (nguoiDung.getVaiTro() != null) {
                this.maVaiTro = nguoiDung.getVaiTro().getMaVaiTro();
                this.tenVaiTro = nguoiDung.getVaiTro().getTenVaiTro();
            }
        }

        this.maBenhNhan = benhNhan.getMaBenhNhan();
        this.ngaySinh = benhNhan.getNgaySinh();
        this.gioiTinh = benhNhan.getGioiTinh() != null ? benhNhan.getGioiTinh().toString() : null;
        this.diaChi = benhNhan.getDiaChi();
        this.tienSuBenh = benhNhan.getTienSuBenh();
        this.diUng = benhNhan.getDiUng();

        if (benhNhan.getNgaySinh() != null) {
            this.tuoi = Period.between(benhNhan.getNgaySinh(), LocalDate.now()).getYears();
        }
    }


    // Method để tạo DTO từ NguoiDung với thông tin mở rộng
    public static UserDTO fromNguoiDungWithDetails(NguoiDung nguoiDung, BacSi bacSi, BenhNhan benhNhan) {
        UserDTO dto = new UserDTO(nguoiDung);

        if (bacSi != null) {
            dto.setMaBacSi(bacSi.getMaBacSi());
            dto.setChuyenKhoa(bacSi.getChuyenKhoa());
            dto.setSoNamKinhNghiem(bacSi.getSoNamKinhNghiem());
            dto.setTrangThaiLamViec(bacSi.getTrangThaiLamViec());
        }

        if (benhNhan != null) {
            dto.setMaBenhNhan(benhNhan.getMaBenhNhan());
            dto.setNgaySinh(benhNhan.getNgaySinh());
            dto.setGioiTinh(benhNhan.getGioiTinh().toString());
            dto.setDiaChi(benhNhan.getDiaChi());
            dto.setTienSuBenh(benhNhan.getTienSuBenh());
            dto.setDiUng(benhNhan.getDiUng());

            if (benhNhan.getNgaySinh() != null) {
                dto.setTuoi(Period.between(benhNhan.getNgaySinh(), LocalDate.now()).getYears());
            }
        }

        return dto;
    }
}