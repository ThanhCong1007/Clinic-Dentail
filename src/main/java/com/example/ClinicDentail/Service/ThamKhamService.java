package com.example.ClinicDentail.Service;

import com.example.ClinicDentail.DTO.*;
import com.example.ClinicDentail.Enity.*;
import com.example.ClinicDentail.Repository.*;
import com.example.ClinicDentail.payload.request.AppointmentRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired
    private LichHenService lichHenService;

    @Transactional
    public BenhAnDTO thamKhamBenhNhan(BenhAnDTO request) {
        // Lấy thông tin bác sĩ
        BacSi bacSi = bacSiRepository.findById(request.getMaBacSi())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

        BenhNhan benhNhan;
        LichHen lichHen = null;

        if (request.getMaLichHen() != null) {
            // Trường hợp 1: Có lịch hẹn
            lichHen = lichHenRepository.findById(request.getMaLichHen())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn"));

            // Kiểm tra đã có bệnh án chưa (tránh tạo trùng)
            if (benhAnRepository.findByLichHen_MaLichHen(request.getMaLichHen()).isPresent()) {
                throw new RuntimeException("Lịch hẹn này đã có bệnh án");
            }

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

        // Tạo bệnh án (logic chung cho cả 2 trường hợp)
        BenhAn benhAn = new BenhAn();
        benhAn.setBenhNhan(benhNhan);
        benhAn.setBacSi(bacSi);
        benhAn.setLichHen(lichHen); // Có thể null với khách vãng lai
        benhAn.setLyDoKham(request.getLyDoKham());
        benhAn.setChanDoan(request.getChanDoan());
        benhAn.setGhiChuDieuTri(request.getGhiChuDieuTri());
        benhAn.setNgayTaiKham(request.getNgayTaiKham());

        benhAn = benhAnRepository.save(benhAn);

        // *** SỬ DỤNG LICHHENSERVICE ĐỂ TẠO LỊCH HẸN MỚI ***
        BenhAnDTO response = new BenhAnDTO(benhAn);

        // Kiểm tra và tạo lịch hẹn mới nếu có thông tin
        if (request.getMaDichVu() != null && request.getNgayHenMoi() != null &&
                request.getGioBatDauMoi() != null && request.getGioKetThucMoi() != null) {

            try {
                LichHenDTO lichHenMoi = taoLichHenMoi(request, benhNhan, bacSi);

                // Cập nhật thông tin lịch hẹn mới vào response
                response.setMaDichVu(request.getMaDichVu());
                response.setNgayHenMoi(request.getNgayHenMoi());
                response.setGioBatDauMoi(request.getGioBatDauMoi());
                response.setGioKetThucMoi(request.getGioKetThucMoi());
                response.setGhiChuLichHen(request.getGhiChuLichHen());
                response.setMaLichHenMoi(lichHenMoi.getMaLichHen());
                response.setThongBao("Đã tạo lịch hẹn mới thành công");

            } catch (Exception e) {
                response.setThongBao("Tạo bệnh án thành công nhưng lỗi khi tạo lịch hẹn mới: " + e.getMessage());
            }
        }

        return response;
    }

    /**
     * Tạo lịch hẹn mới sau khi khám - sử dụng LichHenService
     */
    private LichHenDTO taoLichHenMoi(BenhAnDTO request, BenhNhan benhNhan, BacSi bacSi) {
        // Tạo AppointmentRequest từ BenhAnDTO
        AppointmentRequest appointmentRequest = new AppointmentRequest();
        appointmentRequest.setMaBenhNhan(benhNhan.getMaBenhNhan());
        appointmentRequest.setMaBacSi(bacSi.getMaBacSi());
        appointmentRequest.setMaDichVu(request.getMaDichVu());
        appointmentRequest.setNgayHen(request.getNgayHenMoi());
        appointmentRequest.setGioBatDau(request.getGioBatDauMoi());
        appointmentRequest.setGioKetThuc(request.getGioKetThucMoi());
        appointmentRequest.setGhiChu(request.getGhiChuLichHen());

        // Lấy trạng thái "Đã xác nhận" cho lịch hẹn mới
        TrangThaiLichHen trangThaiXacNhan = trangThaiLichHenRepository
                .findByTenTrangThai("Đã xác nhận")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái 'Đã xác nhận'"));
        appointmentRequest.setMaTrangThai(trangThaiXacNhan.getMaTrangThai());

        // GỌI METHOD TỪ LICHHENSERVICE
        return lichHenService.registerAppointment(appointmentRequest);
    }



    /**
     * Tạo hoặc cập nhật thông tin bệnh nhân vãng lai
     */
    private BenhNhan taoHoacCapNhatBenhNhanVangLai(BenhAnDTO request) {
        // Tìm bệnh nhân theo số điện thoại
        Optional<BenhNhan> existingBenhNhan = benhNhanRepository.findBySoDienThoai(request.getSoDienThoai());

        BenhNhan benhNhan;
        if (existingBenhNhan.isPresent()) {
            // Cập nhật thông tin nếu đã tồn tại
            benhNhan = existingBenhNhan.get();
            benhNhan.setHoTen(request.getTenBenhNhan());
            if (request.getNgaySinh() != null) benhNhan.setNgaySinh(request.getNgaySinh());
            if (request.getGioiTinh() != null) benhNhan.setGioiTinh(BenhNhan.GioiTinh.valueOf(request.getGioiTinh()));
            if (request.getEmail() != null) benhNhan.setEmail(request.getEmail());
            if (request.getDiaChi() != null) benhNhan.setDiaChi(request.getDiaChi());
            if (request.getTienSuBenh() != null) benhNhan.setTienSuBenh(request.getTienSuBenh());
            if (request.getDiUng() != null) benhNhan.setDiUng(request.getDiUng());
        } else {
            // Tạo mới nếu chưa tồn tại
            benhNhan = new BenhNhan();
            benhNhan.setHoTen(request.getTenBenhNhan());
            benhNhan.setSoDienThoai(request.getSoDienThoai());
            benhNhan.setNgaySinh(request.getNgaySinh());
            if (request.getGioiTinh() != null) {
                benhNhan.setGioiTinh(BenhNhan.GioiTinh.valueOf(request.getGioiTinh()));
            }
            benhNhan.setEmail(request.getEmail());
            benhNhan.setDiaChi(request.getDiaChi());
            benhNhan.setTienSuBenh(request.getTienSuBenh());
            benhNhan.setDiUng(request.getDiUng());
        }

        return benhNhanRepository.save(benhNhan);
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

    public List<LichHenBenhAnDTO> getLichHenBenhAnByBacSi(Integer maBacSi) {
        // Validate bác sĩ
        bacSiRepository.findById(maBacSi)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

        List<LichHen> lichHens = lichHenRepository
                .findByBacSi_MaBacSiOrderByNgayTaoDesc(maBacSi);

        return lichHens.stream()
                .map(LichHenBenhAnDTO::new)
                .collect(Collectors.toList());
    }

    public LichHenBenhAnDTO getChiTietLichHenBenhAn(Integer maLichHen) {
        LichHen lichHen = lichHenRepository.findById(maLichHen)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn"));

        return new LichHenBenhAnDTO(lichHen);
    }

    public BenhAnDTO capNhatBenhAn(BenhAnDTO request) {
        BenhAn benhAn = benhAnRepository.findById(request.getMaBenhAn())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh án"));

        // Cập nhật thông tin
        if (request.getLyDoKham() != null) benhAn.setLyDoKham(request.getLyDoKham());
        if (request.getChanDoan() != null) benhAn.setChanDoan(request.getChanDoan());
        if (request.getGhiChuDieuTri() != null) benhAn.setGhiChuDieuTri(request.getGhiChuDieuTri());
        if (request.getNgayTaiKham() != null) benhAn.setNgayTaiKham(request.getNgayTaiKham());

        return new BenhAnDTO(benhAnRepository.save(benhAn));
    }

    public void xoaBenhAn(Integer maBenhAn) {
        BenhAn benhAn = benhAnRepository.findById(maBenhAn)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh án"));

        benhAnRepository.delete(benhAn);
    }

    public List<BenhAnDTO> getBenhAnByBenhNhan(Integer maBenhNhan) {
        List<BenhAn> benhAns = benhAnRepository
                .findByBenhNhan_MaBenhNhanOrderByNgayTaoDesc(maBenhNhan);

        return benhAns.stream()
                .map(BenhAnDTO::new)
                .collect(Collectors.toList());
    }
}
