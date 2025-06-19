package com.example.ClinicDentail.Service;

import com.example.ClinicDentail.DTO.ChiTietDichVuDTO;
import com.example.ClinicDentail.DTO.KhamBenhDTO;
import com.example.ClinicDentail.Enity.*;
import com.example.ClinicDentail.Repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HoaDonService {
    private static final Logger logger = LoggerFactory.getLogger(HoaDonService.class);


    @Autowired
    private DichVuRepository dichVuRepository;

    @Autowired
    private ChiTietDonThuocRepository chiTietDonThuocRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private ChiTietHoaDonService chiTietHoaDonService;

    @Autowired
    private ChiTietDonThuocService chiTietDonThuocService;
    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    public HoaDon taoHoaDon(KhamBenhDTO dto, BenhNhan benhNhan, LichHen lichHen, DonThuoc donThuoc) {
        try {
            HoaDon hoaDon = new HoaDon();
            hoaDon.setBenhNhan(benhNhan);
            hoaDon.setLichHen(lichHen);
            hoaDon.setTrangThai(HoaDon.TrangThaiHoaDon.CHUA_THANH_TOAN);
            hoaDon.setNgayHoaDon(LocalDateTime.now());
            hoaDon.setNgayTao(LocalDateTime.now());
            hoaDon.setTongTien(BigDecimal.ZERO);
            hoaDon.setThanhTien(BigDecimal.ZERO);
            NguoiDung bacSi = nguoiDungRepository.findById(dto.getMaBacSi())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ với mã: " + dto.getMaBacSi()));
            hoaDon.setNguoiTao(bacSi);
            hoaDon = hoaDonRepository.save(hoaDon);
            BigDecimal tongTien = BigDecimal.ZERO;


            // Thêm dịch vụ "Khám tổng quát" nếu chưa có
            if (dto.getDanhSachDichVu() == null || !dto.getDanhSachDichVu().contains(1)) {
                Optional<DichVu> optDichVu = dichVuRepository.findById(1);
                if (optDichVu.isPresent()) {
                    BigDecimal giaDV = chiTietHoaDonService.taoChiTietHoaDon(hoaDon, optDichVu.get());
                    if (giaDV != null) {
                        tongTien = tongTien.add(giaDV);
                    }
                }
            }

            // Thêm các dịch vụ khác
            if (dto.getDanhSachDichVu() != null) {
                for ( ChiTietDichVuDTO ctDV : dto.getDanhSachDichVu()) {
                    Optional<DichVu> optDichVu = dichVuRepository.findById(ctDV.getMaDichVu());
                    if (optDichVu.isPresent()) {
                        BigDecimal giaDV = chiTietHoaDonService.taoChiTietHoaDon(hoaDon, optDichVu.get());
                        if (giaDV != null) {
                            tongTien = tongTien.add(giaDV);
                        }
                    } else {
                        logger.warn("Không tìm thấy dịch vụ với mã: {} để thêm vào hóa đơn", ctDV.getMaDichVu());
                    }
                }
            }

            // Thêm thuốc từ đơn thuốc
            if (donThuoc != null) {
                for (ChiTietDonThuoc ct : chiTietDonThuocRepository.findByDonThuoc(donThuoc)) {
                    tongTien = tongTien.add(
                            chiTietDonThuocService.taoChiTietHoaThuoc(hoaDon, ct)
                    );
                }
            }

            hoaDon.setTongTien(tongTien);
            hoaDon.setThanhTien(tongTien);

            return hoaDonRepository.save(hoaDon);
        } catch (Exception e) {
            logger.error("Lỗi tạo hóa đơn cho bệnh nhân mã: {}", benhNhan.getMaBenhNhan(), e);
            throw new RuntimeException("Lỗi tạo hóa đơn: " + e.getMessage(), e);
        }
    }


}
