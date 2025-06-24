package com.example.ClinicDentail.Service;

import com.example.ClinicDentail.DTO.DichVuDTO;
import com.example.ClinicDentail.DTO.HoaDonDTO;
import com.example.ClinicDentail.DTO.KhamBenhDTO;
import com.example.ClinicDentail.Enity.*;
import com.example.ClinicDentail.Repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Autowired
    private BenhAnDichVuRepository benhAnDichVuRepository;

    public HoaDon taoHoaDon(KhamBenhDTO dto, BenhNhan benhNhan, LichHen lichHen, DonThuoc donThuoc, BenhAn benhAn) {
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

            // Thêm các dịch vụ khác
            if (dto.getDanhSachDichVu() != null) {
                for (DichVuDTO ctDV : dto.getDanhSachDichVu()) {

                    Optional<BenhAnDichVu> optDichVu = benhAnDichVuRepository
                            .findByBenhAn_MaBenhAnAndDichVu_MaDichVu(benhAn.getMaBenhAn(), ctDV.getMaDichVu());

                    if (optDichVu.isPresent()) {
                        BigDecimal giaDV = chiTietHoaDonService.taoChiTietHoaDon(hoaDon.getMaHoaDon(), optDichVu.get());
                        if (giaDV != null) {
                            tongTien = tongTien.add(giaDV);
                        }
                    } else {
                        logger.warn("Không tìm thấy dịch vụ [{}] trong bệnh án [{}]", ctDV.getMaDichVu(), benhAn.getMaBenhAn());
                    }
                }
            }


            // Thêm thuốc từ đơn thuốc
            if (donThuoc != null) {
                for (ChiTietDonThuoc ct : chiTietDonThuocRepository.findByDonThuoc(donThuoc)) {
                    tongTien = tongTien.add(
                            chiTietDonThuocService.taoChiTietDonThuoc(hoaDon.getMaHoaDon(), ct)
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

    /**
     * Lấy danh sách hóa đơn theo bệnh nhân (người dùng)
     * @param maBenhNhan ID của bệnh nhân
     * @return Danh sách hóa đơn của bệnh nhân
     */
    public List<HoaDonDTO> getHoaDonByBenhNhan(Integer maBenhNhan) {
        try {
            List<HoaDon> hoaDons = hoaDonRepository.findByBenhNhan_MaBenhNhanOrderByNgayTaoDesc(maBenhNhan);
            return hoaDons.stream()
                    .map(HoaDonDTO::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Lỗi khi lấy danh sách hóa đơn của bệnh nhân với ID {}: {}", maBenhNhan, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * Lấy tất cả hóa đơn (dành cho Admin)
     * @return Danh sách tất cả hóa đơn
     */
    public List<HoaDonDTO> getAllHoaDon() {
        try {
            List<HoaDon> hoaDons = hoaDonRepository.findAllByOrderByNgayTaoDesc();
            return hoaDons.stream()
                    .map(HoaDonDTO::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Lỗi khi lấy tất cả hóa đơn: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * Xem chi tiết hóa đơn theo ID
     * @param maHoaDon ID của hóa đơn
     * @return Chi tiết hóa đơn
     */
    public Optional<HoaDonDTO> getHoaDonById(Integer maHoaDon) {
        try {
            Optional<HoaDon> hoaDon = hoaDonRepository.findByIdWithDetails(maHoaDon);
            return hoaDon.map(HoaDonDTO::new);
        } catch (Exception e) {
            logger.error("Lỗi khi lấy chi tiết hóa đơn với ID {}: {}", maHoaDon, e.getMessage(), e);
            return Optional.empty();
        }
    }

}
