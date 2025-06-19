package com.example.ClinicDentail.Service;

import com.example.ClinicDentail.DTO.BenhAnDTO;
import com.example.ClinicDentail.DTO.ChiTietDichVuDTO;
import com.example.ClinicDentail.DTO.KhamBenhDTO;
import com.example.ClinicDentail.Enity.*;
import com.example.ClinicDentail.Repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BenhAnService {
    private static final Logger logger = LoggerFactory.getLogger(BenhAnService.class);

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
    private BenhAnDichVuRepository benhAnDichVuRepository;

    public BenhAn layBenhAnHienTai(Integer maBenhAn) {
        logger.debug("Tìm kiếm bệnh án với mã: {}", maBenhAn);

        try {
            Optional<BenhAn> optBenhAn = benhAnRepository.findById(maBenhAn);

            if (!optBenhAn.isPresent()) {
                logger.error("Không tìm thấy bệnh án với mã: {}", maBenhAn);
                throw new RuntimeException("Không tìm thấy bệnh án với mã: " + maBenhAn);
            }

            BenhAn benhAn = optBenhAn.get();
            logger.debug("Đã tìm thấy bệnh án cho bệnh nhân: {}",
                    benhAn.getBenhNhan() != null ? benhAn.getBenhNhan().getHoTen() : "Unknown");

            return benhAn;

        } catch (Exception e) {
            logger.error("Lỗi khi truy vấn bệnh án mã: {} - Chi tiết: {}", maBenhAn, e.getMessage(), e);
            throw new RuntimeException("Lỗi truy vấn cơ sở dữ liệu khi tìm bệnh án: " + e.getMessage(), e);
        }
    }
    public BenhAn taoBenhAn(KhamBenhDTO dto, BenhNhan benhNhan, LichHen lichHen) {
            logger.debug("Tạo bệnh án cho bệnh nhân: {} - Bác sĩ: {}",
                    benhNhan.getHoTen(),
                    lichHen != null ? lichHen.getBacSi().getNguoiDung().getHoTen() : "Mã bác sĩ: " + dto.getMaBacSi());
            try {
                BenhAn benhAn = new BenhAn();
                benhAn.setBenhNhan(benhNhan);

                // Xử lý thông tin bác sĩ
                if (lichHen != null) {
                    benhAn.setBacSi(lichHen.getBacSi());
                } else {
                    Optional<BacSi> optBacSi = bacSiRepository.findById(dto.getMaBacSi());
                    if (optBacSi.isPresent()) {
                        benhAn.setBacSi(optBacSi.get());
                    } else {
                        logger.error("Không tìm thấy bác sĩ với mã: {}", dto.getMaBacSi());
                        throw new RuntimeException("Không tìm thấy bác sĩ với mã: " + dto.getMaBacSi());
                    }
                }

                benhAn.setLichHen(lichHen);
                benhAn.setLyDoKham(dto.getLyDoKham());
                benhAn.setChanDoan(dto.getChanDoan());
                benhAn.setGhiChuDieuTri(dto.getGhiChuDieuTri());
                benhAn.setNgayTaiKham(dto.getNgayTaiKham());
                benhAn.setNgayTao(LocalDateTime.now());
                for (ChiTietDichVuDTO dvDTO : dto.getDanhSachDichVu()) {
                    DichVu dichVu = dichVuRepository.findById(dvDTO.getMaDichVu())
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy dịch vụ"));

                    BenhAnDichVu banDv = new BenhAnDichVu();
                    banDv.setBenhAn(benhAn);
                    banDv.setDichVu(dichVu);
                    banDv.setGia(dvDTO.getGia());

                    benhAnDichVuRepository.save(banDv);
                }

                BenhAn savedBenhAn = benhAnRepository.save(benhAn);
                logger.debug("Đã lưu bệnh án mã: {} thành công", savedBenhAn.getMaBenhAn());
                return savedBenhAn;

            } catch (Exception e) {
                logger.error("Lỗi khi tạo bệnh án cho bệnh nhân mã: {} - Chi tiết: {}",
                        benhNhan.getMaBenhNhan(), e.getMessage(), e);
                throw e;
            }
    }


    public void capNhatThongTinKhamBenh(BenhAn benhAn, BenhAnDTO dto) {
        try {
            // Cập nhật thông tin khám bệnh
            if (dto.getLyDoKham() != null && !dto.getLyDoKham().trim().isEmpty()) {
                benhAn.setLyDoKham(dto.getLyDoKham().trim());
            }

            if (dto.getChanDoan() != null && !dto.getChanDoan().trim().isEmpty()) {
                benhAn.setChanDoan(dto.getChanDoan().trim());
            }

            if (dto.getGhiChuDieuTri() != null && !dto.getGhiChuDieuTri().trim().isEmpty()) {
                benhAn.setGhiChuDieuTri(dto.getGhiChuDieuTri().trim());
            }

            if (dto.getNgayTaiKham() != null) {
                benhAn.setNgayTaiKham(dto.getNgayTaiKham());
            }

            logger.debug("Đã cập nhật thông tin khám bệnh cho bệnh án mã: {}", benhAn.getMaBenhAn());

        } catch (Exception e) {
            logger.error("Lỗi khi cập nhật thông tin khám bệnh: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi cập nhật thông tin khám bệnh: " + e.getMessage(), e);
        }
    }
}
