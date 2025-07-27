package com.example.ClinicDentail.Service;

import com.example.ClinicDentail.DTO.AnhBenhAnDTO;
import com.example.ClinicDentail.DTO.BenhAnDTO;
import com.example.ClinicDentail.DTO.DichVuDTO;
import com.example.ClinicDentail.DTO.KhamBenhDTO;
import com.example.ClinicDentail.Enity.*;
import com.example.ClinicDentail.Repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BenhAnService {
    private static final Logger logger = LoggerFactory.getLogger(BenhAnService.class);

    @Autowired
    private BacSiRepository bacSiRepository;

    @Autowired
    private BenhAnRepository benhAnRepository;

    @Autowired
    private DichVuRepository dichVuRepository;

    @Autowired
    private BenhAnDichVuRepository benhAnDichVuRepository;

    @Autowired
    private DonThuocRepository donThuocRepository;

    @Autowired
    private ChiTietDonThuocRepository chiTietDonThuocRepository;

    @Autowired
    private BenhNhanRepository benhNhanRepository;
    @Autowired
    private AnhBenhAnRepository anhBenhAnRepository;

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

                BenhAn savedBenhAn = benhAnRepository.save(benhAn);

                for (DichVuDTO dvDTO : dto.getDanhSachDichVu()) {
                    DichVu dichVu = dichVuRepository.findById(dvDTO.getMaDichVu())
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy dịch vụ"));

                    BenhAnDichVu banDv = new BenhAnDichVu();
                    banDv.setBenhAn(benhAn);
                    banDv.setDichVu(dichVu);
                    banDv.setGia(dvDTO.getGia());

                    benhAnDichVuRepository.save(banDv);
                }

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
    /**
     * Lấy danh sách bệnh án theo mã bác sĩ (cho bác sĩ)
     */
    public Page<BenhAnDTO> getDanhSachBenhAnTheoBacSi(Integer maBacSi, int page, int size, String keyword) {
        // Kiểm tra bác sĩ có tồn tại không
        BacSi bacSi = bacSiRepository.findById(maBacSi)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ với mã: " + maBacSi));

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "ngayTao"));

        Page<BenhAn> benhAnPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            // Tìm kiếm theo từ khóa (tên bệnh nhân, số điện thoại, chẩn đoán)
            benhAnPage = benhAnRepository.findByBacSiAndKeyword(maBacSi, keyword.trim(), pageable);
        } else {
            // Lấy tất cả bệnh án của bác sĩ
            benhAnPage = benhAnRepository.findByBacSi_MaBacSi(maBacSi, pageable);
        }

        return benhAnPage.map(BenhAnDTO::new);
    }
    public List<BenhAnDTO> getAllForDoctor(Integer maBacSiDangNhap) {
        List<BenhAn> list = benhAnRepository.findAll();
        return list.stream()
                .map(benhAn -> {
                    BenhAnDTO dto = new BenhAnDTO(benhAn);
                    boolean isOwner = benhAn.getBacSi().getMaBacSi().equals(maBacSiDangNhap);
                    dto.setEditable(isOwner);
                    return dto;
                })
                .collect(Collectors.toList());
    }
    public List<BenhAnDTO> getAllForDoctor(NguoiDung currentUser) {
        List<BenhAn> danhSachBenhAn = benhAnRepository.findAll(); // Lấy tất cả bệnh án

        return danhSachBenhAn.stream()
                .map(benhAn -> {
                    BenhAnDTO dto = new BenhAnDTO(benhAn);
                    // Gắn cờ editable nếu là người tạo
                    if (benhAn.getBacSi().getMaBacSi().equals(currentUser.getMaNguoiDung())) {
                        dto.setEditable(true);
                    } else {
                        dto.setEditable(false);
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }
    /**
     * Lấy chi tiết bệnh án
     */
    public BenhAnDTO getChiTietBenhAnChoBacSi(Integer maBenhAn) {
        BenhAn benhAn = benhAnRepository.findById(maBenhAn)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh án với mã: " + maBenhAn));

        // Lấy danh sách dịch vụ
        List<BenhAnDichVu> danhSachDichVu = benhAnDichVuRepository.findByBenhAn_MaBenhAn(maBenhAn);

        // Lấy đơn thuốc
        DonThuoc donThuoc = donThuocRepository.findByBenhAn_MaBenhAn(maBenhAn).orElse(null);
        List<AnhBenhAn> anhBenhAn = anhBenhAnRepository.findByBenhAn(benhAn);
        // Lấy chi tiết thuốc nếu có đơn thuốc
        List<ChiTietDonThuoc> danhSachThuoc = new ArrayList<>();
        if (donThuoc != null) {
            danhSachThuoc = chiTietDonThuocRepository.findByDonThuoc_MaDonThuoc(donThuoc.getMaDonThuoc());
        }

        // Khởi tạo DTO
        BenhAnDTO dto = new BenhAnDTO(benhAn, danhSachDichVu, donThuoc, danhSachThuoc);

        // Gán thêm danh sách ảnh vào DTO
        if (anhBenhAn != null && !anhBenhAn.isEmpty()) {
            List<AnhBenhAnDTO> danhSachAnh = anhBenhAn.stream()
                    .map(AnhBenhAnDTO::new)
                    .collect(Collectors.toList());
            dto.setDanhSachAnhBenhAn(danhSachAnh);
        }

        return dto;
    }

    /**
     * Lấy danh sách bệnh án theo mã bệnh nhân (cho bệnh nhân)
     */
    public Page<BenhAnDTO> getDanhSachBenhAnTheoBenhNhan(Integer maBenhNhan, int page, int size) {
        // Kiểm tra bệnh nhân có tồn tại không
        BenhNhan benhNhan = benhNhanRepository.findById(maBenhNhan)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh nhân với mã: " + maBenhNhan));

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "ngayTao"));

        Page<BenhAn> benhAnPage = benhAnRepository.findByBenhNhan_MaBenhNhan(maBenhNhan, pageable);

        return benhAnPage.map(BenhAnDTO::new);
    }

}
