package com.example.ClinicDentail.Service;

import com.example.ClinicDentail.DTO.BenhAnDTO;
import com.example.ClinicDentail.DTO.ChiTietDonThuocDTO;
import com.example.ClinicDentail.DTO.KhamBenhDTO;
import com.example.ClinicDentail.Enity.BenhAn;
import com.example.ClinicDentail.Enity.ChiTietDonThuoc;
import com.example.ClinicDentail.Enity.DonThuoc;
import com.example.ClinicDentail.Enity.Thuoc;
import com.example.ClinicDentail.Repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DonThuocService {
    private static final Logger logger = LoggerFactory.getLogger(DonThuocService.class);

    @Autowired
    private DonThuocRepository donThuocRepository;
    @Autowired
    private ChiTietDonThuocRepository chiTietDonThuocRepository;

    @Autowired
    private ThuocRepository thuocRepository;

    public DonThuoc taoDonThuoc(KhamBenhDTO dto, BenhAn benhAn) {
        logger.debug("Tạo đơn thuốc cho bệnh án mã: {} với {} loại thuốc",
                benhAn.getMaBenhAn(), dto.getDanhSachThuoc().size());
        try {
            // Tạo đơn thuốc
            DonThuoc donThuoc = new DonThuoc();
            donThuoc.setBenhAn(benhAn);
            donThuoc.setBenhNhan(benhAn.getBenhNhan());
            donThuoc.setBacSi(benhAn.getBacSi());
            donThuoc.setNgayKe(LocalDate.now());
            donThuoc.setMoTaChanDoan(dto.getChanDoan());
            donThuoc.setGhiChu(dto.getGhiChuDonThuoc());
            donThuoc.setTrangThaiToa(DonThuoc.TrangThaiToa.MOI);
            donThuoc.setNgayTao(LocalDateTime.now());

            donThuoc = donThuocRepository.save(donThuoc);
            logger.debug("Đã tạo đơn thuốc mã: {}", donThuoc.getMaDonThuoc());

            // Tạo chi tiết đơn thuốc
            int thuocThanhCong = 0;
            int thuocLoi = 0;

            for (KhamBenhDTO.ChiTietThuocDTO thuocDTO : dto.getDanhSachThuoc()) {
                try {
                    Optional<Thuoc> optThuoc = thuocRepository.findById(thuocDTO.getMaThuoc());
                    if (optThuoc.isPresent()) {
                        Thuoc thuoc = optThuoc.get();
                        logger.debug("Thêm thuốc: {} - Số lượng: {}", thuoc.getTenThuoc(), thuocDTO.getSoLuong());

                        ChiTietDonThuoc chiTiet = new ChiTietDonThuoc();
                        chiTiet.setDonThuoc(donThuoc);
                        chiTiet.setThuoc(thuoc);
                        chiTiet.setLieuDung(thuocDTO.getLieuDung());
                        chiTiet.setTanSuat(thuocDTO.getTanSuat());
                        chiTiet.setThoiDiem(thuocDTO.getThoiDiem());
                        chiTiet.setThoiGianDieuTri(thuocDTO.getThoiGianDieuTri());
                        chiTiet.setSoLuong(thuocDTO.getSoLuong());
                        chiTiet.setDonViDung(thuocDTO.getDonViDung());
                        chiTiet.setDonGia(thuoc.getGia());
                        chiTiet.setThanhTien(thuoc.getGia().multiply(BigDecimal.valueOf(thuocDTO.getSoLuong())));
                        chiTiet.setGhiChu(thuocDTO.getGhiChu());
                        chiTiet.setLyDoKeDon(thuocDTO.getLyDoDonThuoc());

                        chiTietDonThuocRepository.save(chiTiet);
                        thuocThanhCong++;

                    } else {
                        logger.warn("Không tìm thấy thuốc với mã: {} trong đơn thuốc mã: {}",
                                thuocDTO.getMaThuoc(), donThuoc.getMaDonThuoc());
                        thuocLoi++;
                    }
                } catch (Exception e) {
                    logger.error("Lỗi khi thêm thuốc mã: {} vào đơn thuốc mã: {} - Chi tiết: {}",
                            thuocDTO.getMaThuoc(), donThuoc.getMaDonThuoc(), e.getMessage(), e);
                    thuocLoi++;
                }
            }

            logger.info("Hoàn thành tạo đơn thuốc mã: {} - Thành công: {}, Lỗi: {}",
                    donThuoc.getMaDonThuoc(), thuocThanhCong, thuocLoi);

            return donThuoc;

        } catch (Exception e) {
            logger.error("Lỗi khi tạo đơn thuốc cho bệnh án mã: {} - Chi tiết: {}",
                    benhAn.getMaBenhAn(), e.getMessage(), e);
            throw e;
        }
    }

    public DonThuoc capNhatDonThuoc(BenhAn benhAn, BenhAnDTO dto) {
        try {
            // Tìm đơn thuốc hiện tại (nếu có)
            DonThuoc donThuocHienTai = donThuocRepository.findByBenhAn(benhAn);
            DonThuoc donThuoc = null;

            if (donThuocHienTai != null) {
                donThuoc = donThuocHienTai;
                // Xóa chi tiết đơn thuốc cũ
                List<ChiTietDonThuoc> chiTietCu = chiTietDonThuocRepository.findByDonThuoc(donThuoc);
                chiTietDonThuocRepository.deleteAll(chiTietCu);
                logger.debug("Đã xóa {} chi tiết đơn thuốc cũ", chiTietCu.size());

            } else {
                // Tạo đơn thuốc mới
                donThuoc = new DonThuoc();
                donThuoc.setBenhAn(benhAn);
                donThuoc.setBenhNhan(benhAn.getBenhNhan());
                donThuoc.setBacSi(benhAn.getBacSi());
                donThuoc.setNgayKe(LocalDate.now());
                donThuoc.setTrangThaiToa(DonThuoc.TrangThaiToa.MOI);
                donThuoc.setNgayTao(LocalDateTime.now());
            }

            // Cập nhật thông tin đơn thuốc
            if (dto.getMoTaChanDoan() != null) {
                donThuoc.setMoTaChanDoan(dto.getMoTaChanDoan());
            } else if (dto.getChanDoan() != null) {
                donThuoc.setMoTaChanDoan(dto.getChanDoan());
            }

            if (dto.getGhiChuDonThuoc() != null) {
                donThuoc.setGhiChu(dto.getGhiChuDonThuoc());
            }

            // Lưu đơn thuốc
            donThuoc = donThuocRepository.save(donThuoc);
            logger.debug("Đã lưu đơn thuốc mã: {}", donThuoc.getMaDonThuoc());

            // Thêm chi tiết thuốc mới
            int thuocThanhCong = 0;
            int thuocLoi = 0;

            for (ChiTietDonThuocDTO thuocDTO : dto.getDanhSachThuoc()) {
                try {
                    Optional<Thuoc> optThuoc = thuocRepository.findById(thuocDTO.getMaThuoc());
                    if (optThuoc.isPresent()) {
                        Thuoc thuoc = optThuoc.get();

                        ChiTietDonThuoc chiTiet = new ChiTietDonThuoc();
                        chiTiet.setDonThuoc(donThuoc);
                        chiTiet.setThuoc(thuoc);
                        chiTiet.setLieuDung(thuocDTO.getLieudung());
                        chiTiet.setTanSuat(thuocDTO.getTanSuat());
                        chiTiet.setThoiDiem(thuocDTO.getThoiDiem());
                        chiTiet.setThoiGianDieuTri(thuocDTO.getThoiGianDieuTri());
                        chiTiet.setSoLuong(thuocDTO.getSoLuong());
                        chiTiet.setDonViDung(thuocDTO.getDonViDung());
                        chiTiet.setDonGia(thuoc.getGia());
                        chiTiet.setThanhTien(thuoc.getGia().multiply(BigDecimal.valueOf(thuocDTO.getSoLuong())));
                        chiTiet.setGhiChu(thuocDTO.getGhiChu());
                        chiTiet.setLyDoKeDon(thuocDTO.getLyDoDonThuoc());

                        chiTietDonThuocRepository.save(chiTiet);
                        thuocThanhCong++;

                        logger.debug("Đã thêm thuốc: {} - Số lượng: {}", thuoc.getTenThuoc(), thuocDTO.getSoLuong());

                    } else {
                        logger.warn("Không tìm thấy thuốc với mã: {}", thuocDTO.getMaThuoc());
                        thuocLoi++;
                    }
                } catch (Exception e) {
                    logger.error("Lỗi khi thêm thuốc mã: {} - Chi tiết: {}", thuocDTO.getMaThuoc(), e.getMessage(), e);
                    thuocLoi++;
                }
            }

            logger.info("Hoàn thành cập nhật đơn thuốc mã: {} - Thành công: {}, Lỗi: {}",
                    donThuoc.getMaDonThuoc(), thuocThanhCong, thuocLoi);

            return donThuoc;

        } catch (Exception e) {
            logger.error("Lỗi khi cập nhật đơn thuốc: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi cập nhật đơn thuốc: " + e.getMessage(), e);
        }
    }
}
