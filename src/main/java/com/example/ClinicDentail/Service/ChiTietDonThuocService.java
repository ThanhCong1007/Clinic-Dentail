package com.example.ClinicDentail.Service;

import com.example.ClinicDentail.Enity.ChiTietDonThuoc;
import com.example.ClinicDentail.Enity.ChiTietHoaDon;
import com.example.ClinicDentail.Enity.HoaDon;
import com.example.ClinicDentail.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.action.internal.EntityActionVetoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ChiTietDonThuocService {
    private static final Logger logger = LoggerFactory.getLogger(ChiTietDonThuocService.class);

    @Autowired
    private ChiTietHoaDonRepository chiTietHoaDonRepository;
    @Autowired
    private HoaDonRepository hoaDonRepository;


    public BigDecimal taoChiTietDonThuoc(Integer maHoaDon, ChiTietDonThuoc chiTietThuoc) {
        ChiTietHoaDon chiTiet = new ChiTietHoaDon();
        HoaDon hoaDon = hoaDonRepository.findById(maHoaDon)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hóa đơn với mã: "  + maHoaDon));
        chiTiet.setHoaDon(hoaDon);
        chiTiet.setMoTa("Thuốc: " + chiTietThuoc.getThuoc().getTenThuoc());
        chiTiet.setSoLuong(chiTietThuoc.getSoLuong());
        chiTiet.setDonGia(chiTietThuoc.getDonGia());
        chiTiet.setThanhTien(chiTietThuoc.getThanhTien());
        chiTiet.setNgayTao(LocalDateTime.now());

        chiTietHoaDonRepository.save(chiTiet);
        return chiTietThuoc.getThanhTien();
    }

}
