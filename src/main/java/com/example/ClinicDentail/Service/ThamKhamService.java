package com.example.ClinicDentail.Service;

import com.example.ClinicDentail.DTO.*;
import com.example.ClinicDentail.Enity.*;
import com.example.ClinicDentail.Repository.*;
import com.example.ClinicDentail.Repository.AnhBenhAnRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ThamKhamService {
    private static final Logger logger = LoggerFactory.getLogger(ThamKhamService.class);

    @Autowired
    private BenhNhanRepository benhNhanRepository;

    @Autowired
    private BacSiRepository bacSiRepository;

    @Autowired
    private LichHenRepository lichHenRepository;

    @Autowired
    private BenhAnRepository benhAnRepository;

    @Autowired
    private LichHenService lichHenService;

    @Autowired
    private DonThuocService donThuocService;
    
    @Autowired
    private BenhAnService benhAnService;
    @Autowired
    private BenhNhanService benhNhanService;

    @Autowired
    private HoaDonService hoaDonService;
    @Autowired
    private BenhAnDichVuRepository benhAnDichVuRepository;
    @Autowired
    private DonThuocRepository donThuocRepository;
    @Autowired
    private ChiTietDonThuocRepository chiTietDonThuocRepository;

    @Autowired
    private AnhBenhAnRepository anhBenhAnRepository;
    @Autowired
    private AnhBenhAnService anhBenhAnService;

    @Transactional
    public KhamBenhDTO khamBenh(KhamBenhDTO dto) {
        try {
            // 1. Lấy hoặc tạo mới bệnh nhân
            BenhNhan benhNhan = layHoacTaoBenhNhan(dto);

            // 2. Cập nhật trạng thái lịch hẹn nếu có
            LichHen lichHen = capNhatTrangThaiLichHenNeuCo(dto.getMaLichHen());

            // 3. Tạo bệnh án
            BenhAn benhAn = taoBenhAn(dto, benhNhan, lichHen);

            // 4. Tạo đơn thuốc nếu có
            DonThuoc donThuoc = taoDonThuocNeuCo(dto, benhAn);

            // 5. Lưu ảnh bệnh án nếu có
            if (dto.getDanhSachAnhBenhAn() != null && !dto.getDanhSachAnhBenhAn().isEmpty()) {
                for (AnhBenhAnDTO anhDTO : dto.getDanhSachAnhBenhAn()) {
                    anhDTO.setMaBenhAn(benhAn.getMaBenhAn());
                    AnhBenhAn anhBenhAn = anhBenhAnService.luuAnhBenhAn(anhDTO, benhAn);
                }
            }

            // 6. Tạo hóa đơn
            HoaDon hoaDon = taoHoaDon(dto, benhNhan, lichHen, donThuoc, benhAn);

            // 7. Cập nhật trạng thái hoàn thành lịch hẹn
            hoanThanhLichHenNeuCo(lichHen);

            // 8. Nếu có lịch hẹn mới, tạo lịch hẹn mới
            LichHen lichHenMoi = taoLichHenMoi(dto, benhNhan);

            // 9. Trả về kết quả DTO
            return taoKetQuaTraVe(benhAn, donThuoc, hoaDon);

        } catch (Exception e) {
            throw new RuntimeException("Lỗi hệ thống trong quá trình khám bệnh: " + e.getMessage(), e);
        }
    }

    public BenhNhan layHoacTaoBenhNhan(KhamBenhDTO dto) {
        BenhNhan benhNhan = null;

        // 1. Tìm theo mã bệnh nhân nếu có
        if (dto.getMaBenhNhan() != null) {
            benhNhan = benhNhanRepository.findById(dto.getMaBenhNhan()).orElse(null);
        }

        // 2. Nếu chưa tìm được và có số điện thoại, tìm tiếp theo số điện thoại
        if (benhNhan == null && dto.getSoDienThoai() != null) {
            benhNhan = benhNhanRepository.findBySoDienThoai(dto.getSoDienThoai()).orElse(null);
        }

        // 3. Nếu vẫn không tìm được, tạo mới
        if (benhNhan == null) {
            benhNhan = benhNhanService.taoBenhNhanMoi(dto);
        }

        return benhNhan;
    }


    public LichHen capNhatTrangThaiLichHenNeuCo(Integer maLichHen) {
        if (maLichHen == null) {
            logger.debug("Không có mã lịch hẹn để cập nhật");
            return null;
        }
        try {
            return lichHenService.capNhatTrangThaiLichHen(maLichHen);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi cập nhật lịch hẹn: " + e.getMessage(), e);
        }
    }

    public BenhAn taoBenhAn(KhamBenhDTO dto, BenhNhan benhNhan, LichHen lichHen) {
        try {
            return benhAnService.taoBenhAn(dto, benhNhan, lichHen);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo bệnh án: " + e.getMessage(), e);
        }
    }

    public DonThuoc taoDonThuocNeuCo(KhamBenhDTO dto, BenhAn benhAn) {
        if (dto.getDanhSachThuoc() == null || dto.getDanhSachThuoc().isEmpty()) {
            logger.debug("Không có thuốc để kê đơn");
            return null;
        }
        try {
            return donThuocService.taoDonThuoc(dto, benhAn);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo đơn thuốc: " + e.getMessage(), e);
        }
    }

    public HoaDon taoHoaDon(KhamBenhDTO dto, BenhNhan benhNhan, LichHen lichHen, DonThuoc donThuoc, BenhAn benhAn) {
        try {
            return hoaDonService.taoHoaDon(dto, benhNhan, lichHen, donThuoc, benhAn);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo hóa đơn: " + e.getMessage(), e);
        }
    }
    public LichHen taoLichHenMoi( KhamBenhDTO dto,BenhNhan benhNhan) {
        if (dto.getNgayTaiKham() == null || dto.getGioBatDau() == null || dto.getGioKetThuc() == null) {
            return null;
        }
        try {
            return lichHenService.taoLichHenMoi(dto,benhNhan);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo lịch hẹn mới: " + e.getMessage(), e);
        }
    }

    public void hoanThanhLichHenNeuCo(LichHen lichHen) {
        if (lichHen == null) return;
        try {
            lichHenService.capNhatTrangThaiHoanThanh(lichHen);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi cập nhật trạng thái hoàn thành lịch hẹn: " + e.getMessage(), e);
        }
    }

    @Transactional
    public BenhAnDTO capNhatBenhAn(Integer maBenhAn, BenhAnDTO dto) {
        logger.info("Bắt đầu cập nhật bệnh án mã: {}", maBenhAn);

        try {
            BenhAn benhAn = layBenhAn(maBenhAn);

            capNhatThongTinKhamBenh(benhAn, dto);

            capNhatBenhNhan(benhAn, dto);

            DonThuoc donThuoc = capNhatDonThuocNeuCo(benhAn, dto);

            if (dto.getDanhSachAnhBenhAn() != null && !dto.getDanhSachAnhBenhAn().isEmpty()) {
                for (AnhBenhAnDTO anhDTO : dto.getDanhSachAnhBenhAn()) {
                    anhDTO.setMaBenhAn(benhAn.getMaBenhAn());
                    AnhBenhAn anhBenhAn = anhBenhAnService.luuAnhBenhAn(anhDTO, benhAn);
                }
            }

            LichHen lichHenMoi = taoLichHenMoiNeuCo(benhAn, dto);

            BenhAn benhAnDaCapNhat = benhAnRepository.save(benhAn);

            logger.info("Hoàn tất cập nhật bệnh án mã: {}", maBenhAn);
            return taoKetQuaCapNhat(benhAnDaCapNhat, donThuoc, lichHenMoi);

        } catch (RuntimeException e) {
            logger.error("Lỗi trong quá trình cập nhật bệnh án mã: {} - {}", maBenhAn, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Lỗi hệ thống khi cập nhật bệnh án mã: {} - {}", maBenhAn, e.getMessage(), e);
            throw new RuntimeException("Lỗi hệ thống: " + e.getMessage(), e);
        }
    }
    private BenhAn layBenhAn(Integer maBenhAn) {
        logger.debug("Bước 1: Lấy bệnh án mã: {}", maBenhAn);
        return benhAnService.layBenhAnHienTai(maBenhAn);
    }

    private void capNhatThongTinKhamBenh(BenhAn benhAn, BenhAnDTO dto) {
        logger.debug("Bước 2: Cập nhật thông tin khám bệnh");
        benhAnService.capNhatThongTinKhamBenh(benhAn, dto);
    }

    private void capNhatBenhNhan(BenhAn benhAn, BenhAnDTO dto) {
        logger.debug("Bước 3: Cập nhật thông tin bệnh nhân");
        benhNhanService.capNhatThongTinBenhNhan(benhAn, dto);
    }

    private DonThuoc capNhatDonThuocNeuCo(BenhAn benhAn, BenhAnDTO dto) {
        if (dto.getDanhSachThuoc() == null || dto.getDanhSachThuoc().isEmpty()) {
            logger.debug("Bước 4: Không có đơn thuốc để cập nhật");
            return null;
        }
        logger.debug("Bước 4: Cập nhật đơn thuốc với {} loại thuốc", dto.getDanhSachThuoc().size());
        return donThuocService.capNhatDonThuoc(benhAn, dto);
    }

    private LichHen taoLichHenMoiNeuCo(BenhAn benhAn, BenhAnDTO dto) {
        if (dto.getNgayHenMoi() == null || dto.getGioBatDauMoi() == null) {
            logger.debug("Bước 5: Không có lịch hẹn mới để tạo");
            return null;
        }
        logger.debug("Bước 5: Tạo lịch hẹn mới cho ngày: {}", dto.getNgayHenMoi());
        return lichHenService.taoLichHenMoi(benhAn, dto);
    }

    public KhamBenhDTO taoKetQuaTraVe(BenhAn benhAn, DonThuoc donThuoc, HoaDon hoaDon) {
        logger.debug("Tạo kết quả trả về cho quá trình khám bệnh");
        try {
            return new KhamBenhDTO(benhAn, donThuoc, hoaDon);
        } catch (Exception e) {
            logger.error("Lỗi khi tạo kết quả trả về - Chi tiết: {}", e.getMessage(), e);
            throw e;
        }
    }

    // Lấy thông tin bệnh nhân theo số điện thoại (cho khách vãng lai)
    public List<UserDTO> searchBenhNhanBySoDienThoai(String soDienThoai) {
        List<BenhNhan> benhNhanList = benhNhanRepository.findBySoDienThoaiContaining(soDienThoai);
        return benhNhanList.stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
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

    public List<BenhAnDTO> getBenhAnByBenhNhan(Integer maBenhNhan) {
        List<BenhAn> benhAns = benhAnRepository
                .findByBenhNhan_MaBenhNhanOrderByNgayTaoDesc(maBenhNhan);
        return benhAns.stream()
                .map(BenhAnDTO::new)
                .collect(Collectors.toList());
    }
    @Transactional
    public BenhAnDTO getChiTietBenhAn(Integer maBenhAn) {
        BenhAn benhAn = benhAnRepository.findById(maBenhAn)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh án với mã: " + maBenhAn));
        List<BenhAnDichVu> benhAnDichVu= benhAnDichVuRepository.findByBenhAn(benhAn);
        DonThuoc donThuoc= donThuocRepository.findByBenhAn(benhAn);
        List<ChiTietDonThuoc> chiTietDonThuoc= chiTietDonThuocRepository.findByDonThuoc(donThuoc);
        return new BenhAnDTO(benhAn,benhAnDichVu,donThuoc,chiTietDonThuoc);
    }

    private BenhAnDTO taoKetQuaCapNhat(BenhAn benhAn, DonThuoc donThuoc, LichHen lichHenMoi) {
        try {
            BenhAnDTO result = new BenhAnDTO(benhAn, donThuoc, lichHenMoi);
            result.setThongBao("Cập nhật bệnh án thành công");
            logger.debug("Đã tạo kết quả cập nhật bệnh án thành công");
            return result;

        } catch (Exception e) {
            logger.error("Lỗi khi tạo kết quả cập nhật: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi tạo kết quả cập nhật: " + e.getMessage(), e);
        }
    }

}
