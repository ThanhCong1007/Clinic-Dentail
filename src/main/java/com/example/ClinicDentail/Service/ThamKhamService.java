package com.example.ClinicDentail.Service;

import com.example.ClinicDentail.DTO.*;
import com.example.ClinicDentail.Enity.*;
import com.example.ClinicDentail.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Transactional
    public KhamBenhDTO khamBenh(KhamBenhDTO dto) {
        try {
            // 1. Kiểm tra và lấy thông tin bệnh nhân hoặc tạo mới
            BenhNhan benhNhan=new BenhNhan();
            try {
                if (dto.getMaBenhNhan() != null) {
                    // Tìm kiếm bệnh nhân theo mã
                    benhNhan = benhNhanService.layThongTinBenhNhan(dto);
                } else {
                    // Tạo bệnh nhân mới nếu không có mã
                    benhNhan = benhNhanService.taoBenhNhanMoi(dto);
                }
            } catch (EntityNotFoundException e) {
                // Nếu không tìm thấy bệnh nhân theo mã, tạo bệnh nhân mới
                benhNhan = benhNhanService.taoBenhNhanMoi(dto);
            } catch (Exception e) {
                throw new RuntimeException("Lỗi  - Kiểm tra thông tin bệnh nhân: " + e.getMessage(), e);
            }
            // 2. Cập nhật trạng thái lịch hẹn nếu có
            LichHen lichHen = null;
            if (dto.getMaLichHen() != null) {
                try {
                    lichHen = lichHenService.capNhatTrangThaiLichHen(dto.getMaLichHen());
                } catch (Exception e) {
                    throw new RuntimeException("Lỗi - Cập nhật trạng thái lịch hẹn: " + e.getMessage(), e);
                }
            } else {
                logger.debug("Bước 2: Không có lịch hẹn để cập nhật");
            }
            // 3. Tạo bệnh án
            BenhAn benhAn;
            try {
                benhAn = benhAnService.taoBenhAn(dto, benhNhan, lichHen);
            } catch (Exception e) {
                throw new RuntimeException("Lỗi - Tạo bệnh án: " + e.getMessage(), e);
            }
            // 4. Tạo đơn thuốc nếu có
            DonThuoc donThuoc = null;
            if (dto.getDanhSachThuoc() != null && !dto.getDanhSachThuoc().isEmpty()) {
                try {
                    donThuoc = donThuocService.taoDonThuoc(dto, benhAn);
                } catch (Exception e) {
                    throw new RuntimeException("Lỗi  - Tạo đơn thuốc: " + e.getMessage(), e);
                }
            } else {
                logger.debug("Bước 4: Không có thuốc để kê đơn");
            }
            // 5. Tạo hóa đơn
            HoaDon hoaDon;
            try {
                hoaDon = hoaDonService.taoHoaDon(dto, benhNhan, lichHen, donThuoc, benhAn);
            } catch (Exception e) {
                throw new RuntimeException("Lỗi  - Tạo hóa đơn: " + e.getMessage(), e);
            }
            // 6. Cập nhật trạng thái lịch hẹn thành hoàn thành
            if (lichHen != null) {
                try {
                    lichHenService.capNhatTrangThaiHoanThanh(lichHen);
                } catch (Exception e) {
                    throw new RuntimeException("Lỗi  - Hoàn thành lịch hẹn: " + e.getMessage(), e);
                }
            }
            // 7. Trả về kết quả
            try {
                return taoKetQuaTraVe( benhAn, donThuoc, hoaDon);
            } catch (Exception e) {
                throw new RuntimeException("Lỗi  - Tạo kết quả trả về: " + e.getMessage(), e);
            }
        } catch (RuntimeException e) {
            throw e; // Re-throw để Controller xử lý
        } catch (Exception e) {
            throw new RuntimeException("Lỗi hệ thống: " + e.getMessage(), e);
        }
    }

    @Transactional
    public BenhAnDTO capNhatBenhAn(Integer maBenhAn, BenhAnDTO dto) {
        logger.info("Bắt đầu quá trình cập nhật bệnh án mã: {}", maBenhAn);

        try {
            // 1. Kiểm tra và lấy bệnh án hiện tại
            logger.debug("Bước 1: Kiểm tra bệnh án mã: {}", maBenhAn);
            BenhAn benhAn = benhAnService.layBenhAnHienTai(maBenhAn);

            // 2. Cập nhật thông tin khám bệnh
            logger.debug("Bước 2: Cập nhật thông tin khám bệnh");
            benhAnService.capNhatThongTinKhamBenh(benhAn, dto);

            // 3. Cập nhật thông tin bệnh nhân nếu có
            logger.debug("Bước 3: Cập nhật thông tin bệnh nhân");
            benhNhanService.capNhatThongTinBenhNhan(benhAn, dto);

            // 4. Xử lý đơn thuốc nếu có thay đổi
            DonThuoc donThuoc = null;
            if (dto.getDanhSachThuoc() != null && !dto.getDanhSachThuoc().isEmpty()) {
                logger.debug("Bước 4: Cập nhật đơn thuốc với {} loại thuốc", dto.getDanhSachThuoc().size());
                donThuoc = donThuocService.capNhatDonThuoc(benhAn, dto);
            }

            // 5. Tạo lịch hẹn mới nếu có
            LichHen lichHenMoi = null;
            if (dto.getNgayHenMoi() != null && dto.getGioBatDauMoi() != null) {
                logger.debug("Bước 5: Tạo lịch hẹn mới cho ngày: {}", dto.getNgayHenMoi());
                lichHenMoi = lichHenService.taoLichHenMoi(benhAn, dto);
            }

            // 6. Lưu bệnh án đã cập nhật
            logger.debug("Bước 6: Lưu bệnh án đã cập nhật");
            BenhAn benhAnDaCapNhat = benhAnRepository.save(benhAn);

            // 7. Trả về kết quả
            logger.info("Hoàn thành cập nhật bệnh án mã: {}", maBenhAn);
            return taoKetQuaCapNhat(benhAnDaCapNhat, donThuoc, lichHenMoi);

        } catch (RuntimeException e) {
            logger.error("Lỗi trong quá trình cập nhật bệnh án mã: {} - Chi tiết: {}", maBenhAn, e.getMessage(), e);
            throw e; // Re-throw để Controller xử lý
        } catch (Exception e) {
            logger.error("Lỗi không xác định trong quá trình cập nhật bệnh án mã: {} - Chi tiết: {}", maBenhAn, e.getMessage(), e);
            throw new RuntimeException("Lỗi hệ thống: " + e.getMessage(), e);
        }
    }

    private KhamBenhDTO taoKetQuaTraVe(BenhAn benhAn, DonThuoc donThuoc, HoaDon hoaDon) {
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

        return new BenhAnDTO(benhAn);
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
