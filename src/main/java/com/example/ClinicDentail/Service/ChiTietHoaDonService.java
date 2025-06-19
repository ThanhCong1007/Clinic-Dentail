package com.example.ClinicDentail.Service;

import com.example.ClinicDentail.Enity.BenhAnDichVu;
import com.example.ClinicDentail.Enity.ChiTietHoaDon;
import com.example.ClinicDentail.Enity.DichVu;
import com.example.ClinicDentail.Enity.HoaDon;
import com.example.ClinicDentail.Repository.*;
import jakarta.persistence.EntityNotFoundException;
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
    private ChiTietHoaDonRepository chiTietHoaDonRepository;
    @Autowired
    private HoaDonRepository hoaDonRepository;


    public BigDecimal taoChiTietHoaDon(Integer maHoaDon, BenhAnDichVu benhAnDichVu) {
        ChiTietHoaDon chiTiet = new ChiTietHoaDon();
        HoaDon hoaDon = hoaDonRepository.findById(maHoaDon)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hóa đơn với mã: " + maHoaDon));

        chiTiet.setHoaDon(hoaDon);
        chiTiet.setBenhAnDichVu(benhAnDichVu);
        chiTiet.setMoTa(benhAnDichVu.getDichVu().getMoTa());
        chiTiet.setSoLuong(1);
        chiTiet.setDonGia(benhAnDichVu.getGia());
        chiTiet.setThanhTien(benhAnDichVu.getGia());
        chiTiet.setNgayTao(LocalDateTime.now());

        chiTietHoaDonRepository.save(chiTiet);
        return benhAnDichVu.getGia();
    }
}
