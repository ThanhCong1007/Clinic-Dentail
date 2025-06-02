package com.example.ClinicDentail.Service;

import com.example.ClinicDentail.DTO.ThamKhamRequestDTO;
import com.example.ClinicDentail.DTO.ThamKhamResponseDTO;
import com.example.ClinicDentail.DTO.UserDTO;
import com.example.ClinicDentail.Enity.*;
import com.example.ClinicDentail.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ThamKhamService {

    @Autowired
    private BenhNhanRepository benhNhanRepository;

    @Autowired
    private BacSiRepository bacSiRepository;

    @Autowired
    private LichHenRepository lichHenRepository;

    @Autowired
    private BenhAnRepository benhAnRepository;

    @Autowired
    private TrangThaiLichHenRepository trangThaiLichHenRepository;

    @Autowired
    private DichVuRepository dichVuRepository;

    @Transactional
    public ThamKhamResponseDTO thamKhamBenhNhan(ThamKhamRequestDTO request) {
        // Lấy thông tin bác sĩ
        BacSi bacSi = bacSiRepository.findById(request.getMaBacSi())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

        BenhNhan benhNhan;
        LichHen lichHen = null;

        if (request.getMaLichHen() != null) {
            // Trường hợp 1: Khách hàng đã có lịch hẹn
            lichHen = lichHenRepository.findById(request.getMaLichHen())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn"));

            benhNhan = lichHen.getBenhNhan();

            // Cập nhật trạng thái lịch hẹn thành "Hoàn thành"
            TrangThaiLichHen trangThaiHoanThanh = trangThaiLichHenRepository
                    .findByTenTrangThai("Hoàn thành")
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái 'Hoàn thành'"));

            lichHen.setTrangThai(trangThaiHoanThanh);
            lichHenRepository.save(lichHen);

        } else {
            // Trường hợp 2: Khách vãng lai
            benhNhan = taoHoacCapNhatBenhNhanVangLai(request);
        }

        // Tạo bệnh án
        BenhAn benhAn = new BenhAn();
        benhAn.setBenhNhan(benhNhan);
        benhAn.setBacSi(bacSi);
        benhAn.setLichHen(lichHen);
        benhAn.setLyDoKham(request.getLyDoKham());
        benhAn.setChanDoan(request.getChanDoan());
        benhAn.setGhiChuDieuTri(request.getGhiChuDieuTri());
        benhAn.setNgayTaiKham(request.getNgayTaiKham());

        benhAn = benhAnRepository.save(benhAn);

        // Tạo lịch hẹn mới nếu cần
        LichHen lichHenMoi = null;
        if (request.getNgayHenMoi() != null && request.getGioBatDauMoi() != null) {
            lichHenMoi = taoLichHenMoi(benhNhan, bacSi, request);
        }

        // Tạo response
        return taoThamKhamResponse(benhAn, lichHenMoi);
    }


    private BenhNhan taoHoacCapNhatBenhNhanVangLai(ThamKhamRequestDTO request) {
        // Kiểm tra xem bệnh nhân đã tồn tại chưa (dựa trên số điện thoại)
        Optional<BenhNhan> benhNhanExists = benhNhanRepository
                .findBySoDienThoai(request.getSoDienThoai());

        BenhNhan benhNhan;
        if (benhNhanExists.isPresent()) {
            // Cập nhật thông tin bệnh nhân
            benhNhan = benhNhanExists.get();
            benhNhan.setHoTen(request.getHoTen());
            benhNhan.setNgaySinh(request.getNgaySinh());
            if (request.getGioiTinh() != null) {
                benhNhan.setGioiTinh(BenhNhan.GioiTinh.valueOf(request.getGioiTinh()));
            }
            benhNhan.setEmail(request.getEmail());
            benhNhan.setDiaChi(request.getDiaChi());
            benhNhan.setTienSuBenh(request.getTienSuBenh());
            benhNhan.setDiUng(request.getDiUng());
        } else {
            // Tạo mới bệnh nhân
            benhNhan = new BenhNhan();
            benhNhan.setHoTen(request.getHoTen());
            benhNhan.setSoDienThoai(request.getSoDienThoai());
            benhNhan.setNgaySinh(request.getNgaySinh());
            if (request.getGioiTinh() != null) {
                benhNhan.setGioiTinh(BenhNhan.GioiTinh.valueOf(request.getGioiTinh()));
            }
            benhNhan.setEmail(request.getEmail());
            benhNhan.setDiaChi(request.getDiaChi());
            benhNhan.setTienSuBenh(request.getTienSuBenh());
            benhNhan.setDiUng(request.getDiUng());
            // nguoiDung = null cho khách vãng lai
        }

        return benhNhanRepository.save(benhNhan);
    }

    private LichHen taoLichHenMoi(BenhNhan benhNhan, BacSi bacSi, ThamKhamRequestDTO request) {
        // Lấy trạng thái "Đã đặt" hoặc "Chờ xác nhận"
        TrangThaiLichHen trangThai = trangThaiLichHenRepository
                .findByTenTrangThai("Đã đặt")
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái lịch hẹn"));

        LichHen lichHenMoi = new LichHen();
        lichHenMoi.setBenhNhan(benhNhan);
        lichHenMoi.setBacSi(bacSi);
        lichHenMoi.setNgayHen(request.getNgayHenMoi());
        lichHenMoi.setGioBatDau(request.getGioBatDauMoi());
        lichHenMoi.setGioKetThuc(request.getGioKetThucMoi());
        lichHenMoi.setTrangThai(trangThai);
        lichHenMoi.setGhiChu(request.getGhiChuLichHen());

        // Nếu có dịch vụ
        if (request.getMaDichVu() != null) {
            DichVu dichVu = dichVuRepository.findById(request.getMaDichVu())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy dịch vụ"));
            lichHenMoi.setDichVu(dichVu);
        }

        return lichHenRepository.save(lichHenMoi);
    }

    private ThamKhamResponseDTO taoThamKhamResponse(BenhAn benhAn, LichHen lichHenMoi) {
        ThamKhamResponseDTO response = new ThamKhamResponseDTO();
        response.setMaBenhAn(benhAn.getMaBenhAn());
        response.setMaBenhNhan(benhAn.getBenhNhan().getMaBenhNhan());
        response.setTenBenhNhan(benhAn.getBenhNhan().getHoTen());
        response.setSoDienThoai(benhAn.getBenhNhan().getSoDienThoai());
        response.setLyDoKham(benhAn.getLyDoKham());
        response.setChanDoan(benhAn.getChanDoan());
        response.setGhiChuDieuTri(benhAn.getGhiChuDieuTri());
        response.setNgayTaiKham(benhAn.getNgayTaiKham());

        if (lichHenMoi != null) {
            response.setMaLichHenMoi(lichHenMoi.getMaLichHen());
            response.setThongBao("Thăm khám thành công và đã tạo lịch hẹn mới");
        } else {
            response.setThongBao("Thăm khám thành công");
        }

        return response;
    }

    // Lấy thông tin bệnh nhân theo số điện thoại (cho khách vãng lai)
    public UserDTO getBenhNhanDTOBySoDienThoai(String soDienThoai) {
        return benhNhanRepository.findBySoDienThoai(soDienThoai)
                .map(UserDTO::new)
                .orElse(null);
    }

    // Kiểm tra lịch hẹn có thể thăm khám
    public boolean kiemTraLichHenCoTheKham(Integer maLichHen) {
        Optional<LichHen> lichHen = lichHenRepository.findById(maLichHen);
        if (lichHen.isPresent()) {
            String trangThai = lichHen.get().getTrangThai().getTenTrangThai();
            return "Đã xác nhận".equals(trangThai) || "Đã đặt".equals(trangThai);
        }
        return false;
    }
}
