package com.example.ClinicDentail.Service;

import com.example.ClinicDentail.Enity.ChiTietHoaDon;
import com.example.ClinicDentail.Enity.DichVu;
import com.example.ClinicDentail.Enity.HoaDon;
import com.example.ClinicDentail.Repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ChiTietHoaDonService {
    private static final Logger logger = LoggerFactory.getLogger(ChiTietHoaDonService.class);

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
    @Autowired
    private DonThuocRepository donThuocRepository;
    @Autowired
    private ChiTietDonThuocRepository chiTietDonThuocRepository;
    @Autowired
    private ChiTietHoaDonRepository chiTietHoaDonRepository;
    @Autowired
    private HoaDonRepository hoaDonRepository;
    @Autowired
    private ThuocRepository thuocRepository;

    public BigDecimal taoChiTietHoaDon(HoaDon hoaDon, DichVu dichVu) {
        ChiTietHoaDon chiTiet = new ChiTietHoaDon();
        chiTiet.setHoaDon(hoaDon);
        chiTiet.setDichVu(dichVu);
        chiTiet.setMoTa(dichVu.getTenDichVu());
        chiTiet.setSoLuong(1);
        chiTiet.setDonGia(dichVu.getGia());
        chiTiet.setThanhTien(dichVu.getGia());
        chiTiet.setNgayTao(LocalDateTime.now());

        chiTietHoaDonRepository.save(chiTiet);
        return dichVu.getGia();
    }
}
