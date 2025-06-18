package com.example.ClinicDentail.Service;

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
    @Autowired
    private ChiTietHoaDonService chiTietHoaDonService;
    @Autowired
    private ChiTietDonThuocService chiTietDonThuocService;

    public HoaDon taoHoaDon(KhamBenhDTO dto, BenhNhan benhNhan, LichHen lichHen, DonThuoc donThuoc) {
        logger.debug("Tạo hóa đơn cho bệnh nhân: {}", benhNhan.getHoTen());
        try {
            HoaDon hoaDon = new HoaDon();
            hoaDon.setBenhNhan(benhNhan);
            hoaDon.setLichHen(lichHen);
            hoaDon.setTrangThai(HoaDon.TrangThaiHoaDon.CHUA_THANH_TOAN);
            hoaDon.setNgayHoaDon(LocalDateTime.now());
            hoaDon.setNgayTao(LocalDateTime.now());

            // Khởi tạo với giá trị 0 để tránh null
            hoaDon.setTongTien(BigDecimal.ZERO);
            hoaDon.setThanhTien(BigDecimal.ZERO);

            BigDecimal tongTien = BigDecimal.ZERO;

            // Lưu hóa đơn trước để có ID
            hoaDon = hoaDonRepository.save(hoaDon);
            logger.debug("Đã tạo hóa đơn mã: {}", hoaDon.getMaHoaDon());

            // // Kiểm tra nếu dịch tổng quát chưa nằm trong danh sách
            boolean daCoDichVuKhamTongQuat = dto.getMaDichVu() != null && dto.getMaDichVu().contains(1);

            if (!daCoDichVuKhamTongQuat) {
                Optional<DichVu> dichVuKhamTongQuat = dichVuRepository.findById(1);
                if (dichVuKhamTongQuat.isPresent()) {
                    BigDecimal giaDichVu = chiTietHoaDonService.taoChiTietHoaDon(hoaDon, dichVuKhamTongQuat.get());
                    if (giaDichVu != null) {
                        tongTien = tongTien.add(giaDichVu);
                    }
                    logger.debug("Đã thêm dịch vụ 'Khám tổng quát' - Giá: {}", giaDichVu);
                } else {
                    logger.warn("Không tìm thấy dịch vụ 'Khám tổng quát' để thêm vào hóa đơn mã: {}", hoaDon.getMaHoaDon());
                }
            }

            // Thêm các dịch vụ khác từ yêu cầu
            if (dto.getMaDichVu() != null && !dto.getMaDichVu().isEmpty()) {
                logger.debug("Thêm {} dịch vụ bổ sung vào hóa đơn", dto.getMaDichVu().size());
                for (Integer maDichVu : dto.getMaDichVu()) {
                    try {
                        Optional<DichVu> optDichVu = dichVuRepository.findById(maDichVu);
                        if (optDichVu.isPresent()) {
                            BigDecimal giaDichVu = chiTietHoaDonService.taoChiTietHoaDon(hoaDon, optDichVu.get());
                            if (giaDichVu != null) { // FIX: Thêm null check
                                tongTien = tongTien.add(giaDichVu);
                            }
                            logger.debug("Đã thêm dịch vụ: {} - Giá: {}", optDichVu.get().getTenDichVu(), giaDichVu);
                        } else {
                            logger.warn("Không tìm thấy dịch vụ với mã: {} để thêm vào hóa đơn", maDichVu);
                        }
                    } catch (Exception e) {
                        logger.error("Lỗi khi thêm dịch vụ mã: {} vào hóa đơn mã: {} - Chi tiết: {}",
                                maDichVu, hoaDon.getMaHoaDon(), e.getMessage(), e);
                    }
                }
            }

            // Thêm tiền thuốc nếu có
            if (donThuoc != null) {
                logger.debug("Thêm chi phí thuốc từ đơn thuốc mã: {} vào hóa đơn", donThuoc.getMaDonThuoc());
                try {
                    List<ChiTietDonThuoc> danhSachThuoc = chiTietDonThuocRepository.findByDonThuoc(donThuoc);
                    for (ChiTietDonThuoc chiTiet : danhSachThuoc) {
                        BigDecimal giaThuoc =chiTietDonThuocService.taoChiTietHoaThuoc(hoaDon, chiTiet);
                        if (giaThuoc != null) { // FIX: Thêm null check
                            tongTien = tongTien.add(giaThuoc);
                        }
                        logger.debug("Đã thêm thuốc: {} - Giá: {}", chiTiet.getThuoc().getTenThuoc(), giaThuoc);
                    }
                } catch (Exception e) {
                    logger.error("Lỗi khi thêm chi phí thuốc vào hóa đơn mã: {} - Chi tiết: {}",
                            hoaDon.getMaHoaDon(), e.getMessage(), e);
                }
            }

            // Cập nhật tổng tiền
            hoaDon.setTongTien(tongTien);
            hoaDon.setThanhTien(tongTien);

            HoaDon savedHoaDon = hoaDonRepository.save(hoaDon);
            logger.info("Đã hoàn thành tạo hóa đơn mã: {} với tổng tiền: {}",
                    savedHoaDon.getMaHoaDon(), savedHoaDon.getTongTien());

            return savedHoaDon;

        } catch (Exception e) {
            logger.error("Lỗi khi tạo hóa đơn cho bệnh nhân mã: {} - Chi tiết: {}",
                    benhNhan.getMaBenhNhan(), e.getMessage(), e);
            throw e;
        }
    }

}
