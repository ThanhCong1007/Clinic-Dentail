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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
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

    @Transactional
    public KhamBenhDTO khamBenh(KhamBenhDTO dto) {
        logger.info("Bắt đầu quá trình khám bệnh cho bệnh nhân mã: {}", dto.getMaBenhNhan());

        try {
            // 1. Kiểm tra và lấy thông tin bệnh nhân hoặc tạo mới
            logger.debug("Bước 1: Kiểm tra thông tin bệnh nhân mã: {}", dto.getMaBenhNhan());
            BenhNhan benhNhan=new BenhNhan();
            try {
                if (dto.getMaBenhNhan() != null) {
                    // Tìm kiếm bệnh nhân theo mã
                    benhNhan = layThongTinBenhNhan(dto);
                    logger.info("Đã tìm thấy thông tin bệnh nhân: {} - {}", benhNhan.getHoTen(), benhNhan.getSoDienThoai());
                } else {
                    // Tạo bệnh nhân mới nếu không có mã
                    benhNhan = taoBenhNhanMoi(dto);
                    logger.info("Đã tạo bệnh nhân mới: {} - {}", benhNhan.getHoTen(), benhNhan.getSoDienThoai());
                }
            } catch (EntityNotFoundException e) {
                // Nếu không tìm thấy bệnh nhân theo mã, tạo bệnh nhân mới
                logger.warn("Không tìm thấy bệnh nhân mã: {}, tạo bệnh nhân mới", dto.getMaBenhNhan());
                benhNhan = taoBenhNhanMoi(dto);
                logger.info("Đã tạo bệnh nhân mới: {} - {}", benhNhan.getHoTen(), benhNhan.getSoDienThoai());
            } catch (Exception e) {
                throw new RuntimeException("Lỗi bước 1 - Kiểm tra thông tin bệnh nhân: " + e.getMessage(), e);
            }

            // 2. Cập nhật trạng thái lịch hẹn nếu có
            LichHen lichHen = null;
            if (dto.getMaLichHen() != null) {
                logger.debug("Bước 2: Cập nhật trạng thái lịch hẹn mã: {}", dto.getMaLichHen());
                try {
                    lichHen = capNhatTrangThaiLichHen(dto.getMaLichHen());
                    logger.info("Đã cập nhật trạng thái lịch hẹn mã: {} thành 'Đang thực hiện'", dto.getMaLichHen());
                } catch (Exception e) {
                    throw new RuntimeException("Lỗi bước 2 - Cập nhật trạng thái lịch hẹn: " + e.getMessage(), e);
                }
            } else {
                logger.debug("Bước 2: Không có lịch hẹn để cập nhật");
            }

            // 3. Tạo bệnh án
            logger.debug("Bước 3: Tạo bệnh án cho bệnh nhân mã: {}", benhNhan.getMaBenhNhan());
            BenhAn benhAn;
            try {
                benhAn = taoBenhAn(dto, benhNhan, lichHen);
                logger.info("Đã tạo bệnh án mã: {} cho bệnh nhân: {}", benhAn.getMaBenhAn(), benhNhan.getHoTen());
            } catch (Exception e) {
                throw new RuntimeException("Lỗi bước 3 - Tạo bệnh án: " + e.getMessage(), e);
            }

            // 4. Tạo đơn thuốc nếu có
            DonThuoc donThuoc = null;
            if (dto.getDanhSachThuoc() != null && !dto.getDanhSachThuoc().isEmpty()) {
                logger.debug("Bước 4: Tạo đơn thuốc với {} loại thuốc", dto.getDanhSachThuoc().size());
                try {
                    donThuoc = taoDonThuoc(dto, benhAn);
                    logger.info("Đã tạo đơn thuốc mã: {} với {} loại thuốc", donThuoc.getMaDonThuoc(), dto.getDanhSachThuoc().size());
                } catch (Exception e) {
                    throw new RuntimeException("Lỗi bước 4 - Tạo đơn thuốc: " + e.getMessage(), e);
                }
            } else {
                logger.debug("Bước 4: Không có thuốc để kê đơn");
            }

            // 5. Tạo hóa đơn
            logger.debug("Bước 5: Tạo hóa đơn cho bệnh nhân mã: {}", benhNhan.getMaBenhNhan());
            HoaDon hoaDon;
            try {
                hoaDon = taoHoaDon(dto, benhNhan, lichHen, donThuoc);
                logger.info("Đã tạo hóa đơn mã: {} với tổng tiền: {}", hoaDon.getMaHoaDon(), hoaDon.getTongTien());
            } catch (Exception e) {
                throw new RuntimeException("Lỗi bước 5 - Tạo hóa đơn: " + e.getMessage(), e);
            }

            // 6. Cập nhật trạng thái lịch hẹn thành hoàn thành
            if (lichHen != null) {
                logger.debug("Bước 6: Cập nhật trạng thái lịch hẹn thành hoàn thành");
                try {
                    capNhatTrangThaiHoanThanh(lichHen);
                    logger.info("Đã hoàn thành lịch hẹn mã: {}", lichHen.getMaLichHen());
                } catch (Exception e) {
                    throw new RuntimeException("Lỗi bước 6 - Hoàn thành lịch hẹn: " + e.getMessage(), e);
                }
            }

            // 7. Trả về kết quả
            try {
                logger.info("Hoàn thành quá trình khám bệnh cho bệnh nhân mã: {} - Bệnh án: {}, Hóa đơn: {}",
                        benhNhan.getMaBenhNhan(), benhAn.getMaBenhAn(), hoaDon.getMaHoaDon());
                return taoKetQuaTraVe(dto, benhAn, donThuoc, hoaDon);
            } catch (Exception e) {
                throw new RuntimeException("Lỗi bước 7 - Tạo kết quả trả về: " + e.getMessage(), e);
            }

        } catch (RuntimeException e) {
            logger.error("Lỗi trong quá trình khám bệnh cho bệnh nhân mã: {} - Chi tiết: {}",
                    dto.getMaBenhNhan(), e.getMessage(), e);
            throw e; // Re-throw để Controller xử lý
        } catch (Exception e) {
            logger.error("Lỗi không xác định trong quá trình khám bệnh cho bệnh nhân mã: {} - Chi tiết: {}",
                    dto.getMaBenhNhan(), e.getMessage(), e);
            throw new RuntimeException("Lỗi hệ thống: " + e.getMessage(), e);
        }
    }

    // Phương thức hỗ trợ tạo bệnh nhân mới
    private BenhNhan taoBenhNhanMoi(KhamBenhDTO dto) {
        // Kiểm tra các thông tin bắt buộc
        if (dto.getHoTen() == null || dto.getHoTen().trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên là thông tin bắt buộc khi tạo bệnh nhân mới");
        }
        if (dto.getNgaySinh() == null) {
            throw new IllegalArgumentException("Ngày sinh là thông tin bắt buộc khi tạo bệnh nhân mới");
        }
        if (dto.getSoDienThoai() == null || dto.getSoDienThoai().trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại là thông tin bắt buộc khi tạo bệnh nhân mới");
        }

        // Tạo đối tượng bệnh nhân mới
        BenhNhan benhNhan = new BenhNhan();
        benhNhan.setHoTen(dto.getHoTen().trim());
        benhNhan.setNgaySinh(dto.getNgaySinh());
        benhNhan.setSoDienThoai(dto.getSoDienThoai().trim());

        // Các thông tin không bắt buộc
        if (dto.getGioiTinh() != null) {
            benhNhan.setGioiTinh(BenhNhan.GioiTinh.valueOf(dto.getGioiTinh()));
        }
        if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
            benhNhan.setEmail(dto.getEmail().trim());
        }
        if (dto.getDiaChi() != null && !dto.getDiaChi().trim().isEmpty()) {
            benhNhan.setDiaChi(dto.getDiaChi().trim());
        }
        if (dto.getTienSuBenh() != null && !dto.getTienSuBenh().trim().isEmpty()) {
            benhNhan.setTienSuBenh(dto.getTienSuBenh().trim());
        }
        if (dto.getDiUng() != null && !dto.getDiUng().trim().isEmpty()) {
            benhNhan.setDiUng(dto.getDiUng().trim());
        }

        // Lưu bệnh nhân vào database
        try {
            benhNhan = benhNhanRepository.save(benhNhan);
            logger.info("Đã lưu bệnh nhân mới với mã: {}", benhNhan.getMaBenhNhan());
            return benhNhan;
        } catch (Exception e) {
            logger.error("Lỗi khi lưu bệnh nhân mới: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể tạo bệnh nhân mới: " + e.getMessage(), e);
        }
    }

    // Cập nhật các method con để có thông báo lỗi rõ ràng hơn
    private BenhNhan layThongTinBenhNhan(KhamBenhDTO dto) {
        logger.debug("Tìm kiếm bệnh nhân với mã: {}", dto.getMaBenhNhan());

        try {
            Optional<BenhNhan> optBenhNhan = benhNhanRepository.findById(dto.getMaBenhNhan());

            if (!optBenhNhan.isPresent()) {
                logger.error("Không tìm thấy bệnh nhân với mã: {}", dto.getMaBenhNhan());
                throw new RuntimeException("Không tìm thấy bệnh nhân với mã: " + dto.getMaBenhNhan());
            }

            BenhNhan benhNhan = optBenhNhan.get();

            // Force load các field cần thiết trong transaction
            String hoTen = benhNhan.getHoTen();
            String soDienThoai = benhNhan.getSoDienThoai();
            String email = benhNhan.getEmail();
            String diaChi = benhNhan.getDiaChi();

            logger.debug("Đã load đầy đủ thông tin bệnh nhân: {} - {} - {} - {}",
                    hoTen, soDienThoai, email, diaChi);

            return benhNhan;

        } catch (Exception e) {
            logger.error("Lỗi khi truy vấn thông tin bệnh nhân mã: {} - Chi tiết: {}",
                    dto.getMaBenhNhan(), e.getMessage(), e);
            throw new RuntimeException("Lỗi truy vấn cơ sở dữ liệu khi tìm bệnh nhân: " + e.getMessage(), e);
        }
    }

    private LichHen capNhatTrangThaiLichHen(Integer maLichHen) {
        logger.debug("Cập nhật trạng thái lịch hẹn mã: {}", maLichHen);
        try {
            Optional<LichHen> optLichHen = lichHenRepository.findById(maLichHen);
            if (!optLichHen.isPresent()) {
                logger.error("Không tìm thấy lịch hẹn với mã: {}", maLichHen);
                throw new RuntimeException("Không tìm thấy lịch hẹn với mã: " + maLichHen);
            }

            LichHen lichHen = optLichHen.get();
            logger.debug("Tìm thấy lịch hẹn: {} - Bác sĩ: {}", lichHen.getMaLichHen(), lichHen.getBacSi().getNguoiDung().getHoTen());

            // Cập nhật trạng thái thành "Đang thực hiện" (ma_trang_thai = 3)
            Optional<TrangThaiLichHen> optTrangThai = trangThaiLichHenRepository.findById(3);
            if (!optTrangThai.isPresent()) {
                logger.error("Không tìm thấy trạng thái 'Đang thực hiện' (ID=3) để cập nhật lịch hẹn mã: {}", maLichHen);
                throw new RuntimeException("Không tìm thấy trạng thái lịch hẹn trong hệ thống");
            }

            lichHen.setTrangThai(optTrangThai.get());
            lichHenRepository.save(lichHen);
            logger.debug("Đã cập nhật trạng thái lịch hẹn mã: {} thành 'Đang thực hiện'", maLichHen);

            return lichHen;
        } catch (Exception e) {
            logger.error("Lỗi khi cập nhật trạng thái lịch hẹn mã: {} - Chi tiết: {}", maLichHen, e.getMessage(), e);
            throw new RuntimeException("Lỗi cập nhật trạng thái lịch hẹn: " + e.getMessage(), e);
        }
    }

    private BenhAn taoBenhAn(KhamBenhDTO dto, BenhNhan benhNhan, LichHen lichHen) {
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
            logger.debug("Đã lưu bệnh án mã: {} thành công", savedBenhAn.getMaBenhAn());
            return savedBenhAn;

        } catch (Exception e) {
            logger.error("Lỗi khi tạo bệnh án cho bệnh nhân mã: {} - Chi tiết: {}",
                    benhNhan.getMaBenhNhan(), e.getMessage(), e);
            throw e;
        }
    }


    private DonThuoc taoDonThuoc(KhamBenhDTO dto, BenhAn benhAn) {
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

    private HoaDon taoHoaDon(KhamBenhDTO dto, BenhNhan benhNhan, LichHen lichHen, DonThuoc donThuoc) {
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
                    BigDecimal giaDichVu = taoChiTietHoaDon(hoaDon, dichVuKhamTongQuat.get());
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
                            BigDecimal giaDichVu = taoChiTietHoaDon(hoaDon, optDichVu.get());
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
                        BigDecimal giaThuoc = taoChiTietHoaDonThuoc(hoaDon, chiTiet);
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

    private BigDecimal taoChiTietHoaDon(HoaDon hoaDon, DichVu dichVu) {
        ChiTietHoaDon chiTiet = new ChiTietHoaDon();
        chiTiet.setHoaDon(hoaDon);
        chiTiet.setDichVu(dichVu);
        chiTiet.setMoTa(dichVu.getTenDichVu());
        chiTiet.setSoLuong(1);
        chiTiet.setDonGia(dichVu.getGia());
        chiTiet.setThanhTien(dichVu.getGia());
        chiTiet.setNgayTao(LocalDateTime.now());

        chiTietHoaDonRepository.save(chiTiet);
        return dichVu.getGia();
    }

    private BigDecimal taoChiTietHoaDonThuoc(HoaDon hoaDon, ChiTietDonThuoc chiTietThuoc) {
        ChiTietHoaDon chiTiet = new ChiTietHoaDon();
        chiTiet.setHoaDon(hoaDon);
        chiTiet.setMoTa("Thuốc: " + chiTietThuoc.getThuoc().getTenThuoc());
        chiTiet.setSoLuong(chiTietThuoc.getSoLuong());
        chiTiet.setDonGia(chiTietThuoc.getDonGia());
        chiTiet.setThanhTien(chiTietThuoc.getThanhTien());
        chiTiet.setNgayTao(LocalDateTime.now());

        chiTietHoaDonRepository.save(chiTiet);
        return chiTietThuoc.getThanhTien();
    }

    private void capNhatTrangThaiHoanThanh(LichHen lichHen) {
        // Cập nhật trạng thái thành "Hoàn thành" (ma_trang_thai = 4)
        Optional<TrangThaiLichHen> optTrangThai = trangThaiLichHenRepository.findById(4);
        if (optTrangThai.isPresent()) {
            lichHen.setTrangThai(optTrangThai.get());
            lichHenRepository.save(lichHen);
        }
    }

    private KhamBenhDTO taoKetQuaTraVe(KhamBenhDTO dto, BenhAn benhAn, DonThuoc donThuoc, HoaDon hoaDon) {
        logger.debug("Tạo kết quả trả về cho quá trình khám bệnh");
        try {
            // Set thông tin cơ bản
            dto.setMaBenhAn(benhAn.getMaBenhAn());
            dto.setMaDonThuoc(donThuoc != null ? donThuoc.getMaDonThuoc() : null);
            dto.setMaHoaDon(hoaDon.getMaHoaDon());
            dto.setTongTien(hoaDon.getTongTien());
            dto.setTrangThaiKham("Hoàn thành");
            dto.setNgayKham(LocalDateTime.now());

            // *** THÊM: Set lại thông tin bệnh nhân từ BenhAn ***
            if (benhAn.getBenhNhan() != null) {
                var benhNhan = benhAn.getBenhNhan();
                dto.setHoTen(benhNhan.getHoTen());
                dto.setHoTen(benhNhan.getHoTen());
                dto.setSoDienThoai(benhNhan.getSoDienThoai());
                dto.setEmail(benhNhan.getEmail());
                dto.setDiaChi(benhNhan.getDiaChi());
                dto.setNgaySinh(benhNhan.getNgaySinh());
                dto.setGioiTinh(benhNhan.getGioiTinh() != null ? benhNhan.getGioiTinh().toString() : null);
                dto.setTienSuBenh(benhNhan.getTienSuBenh());
                dto.setDiUng(benhNhan.getDiUng());
            }

            // *** THÊM: Set lại thông tin bác sĩ từ BenhAn ***
            if (benhAn.getBacSi() != null && benhAn.getBacSi().getNguoiDung() != null) {
                dto.setHoTen(benhAn.getBacSi().getNguoiDung().getHoTen());
            }

            // *** THÊM: Set lại thông tin lịch hẹn từ BenhAn ***
            if (benhAn.getLichHen() != null) {
                dto.setMaLichHen(benhAn.getLichHen().getMaLichHen());
            }

            logger.debug("Đã tạo kết quả trả về thành công");
            return dto;

        } catch (Exception e) {
            logger.error("Lỗi khi tạo kết quả trả về - Chi tiết: {}", e.getMessage(), e);
            throw e;
        }
    }

    public KhamBenhDTO layThongTinBenhNhan(Integer maBenhNhan) {
        Optional<BenhNhan> optBenhNhan = benhNhanRepository.findById(maBenhNhan);
        if (!optBenhNhan.isPresent()) {
            throw new RuntimeException("Không tìm thấy bệnh nhân với mã: " + maBenhNhan);
        }

        BenhNhan benhNhan = optBenhNhan.get();
        KhamBenhDTO dto = new KhamBenhDTO();

        // Ánh xạ thông tin bệnh nhân
        dto.setMaBenhNhan(benhNhan.getMaBenhNhan());
        dto.setHoTen(benhNhan.getHoTen());
        dto.setSoDienThoai(benhNhan.getSoDienThoai());
        dto.setEmail(benhNhan.getEmail());
        dto.setDiaChi(benhNhan.getDiaChi());
        dto.setNgaySinh(benhNhan.getNgaySinh());
        dto.setGioiTinh(benhNhan.getGioiTinh().toString());
        dto.setTienSuBenh(benhNhan.getTienSuBenh());
        dto.setDiUng(benhNhan.getDiUng());

        return dto;
    }

    public KhamBenhDTO layThongTinLichHen(Integer maLichHen) {
        Optional<LichHen> optLichHen = lichHenRepository.findById(maLichHen);
        if (!optLichHen.isPresent()) {
            throw new RuntimeException("Không tìm thấy lịch hẹn với mã: " + maLichHen);
        }

        LichHen lichHen = optLichHen.get();
        KhamBenhDTO dto = new KhamBenhDTO();

        // Ánh xạ thông tin từ lịch hẹn
        dto.setMaLichHen(lichHen.getMaLichHen());
        dto.setMaBacSi(lichHen.getBacSi().getMaBacSi());
        dto.setLyDoKham(lichHen.getLydo());

        // Ánh xạ thông tin bệnh nhân
        BenhNhan benhNhan = lichHen.getBenhNhan();
        dto.setMaBenhNhan(benhNhan.getMaBenhNhan());
        dto.setHoTen(benhNhan.getHoTen());
        dto.setSoDienThoai(benhNhan.getSoDienThoai());
        dto.setEmail(benhNhan.getEmail());
        dto.setDiaChi(benhNhan.getDiaChi());
        dto.setNgaySinh(benhNhan.getNgaySinh());
        dto.setGioiTinh(benhNhan.getGioiTinh().toString());
        dto.setTienSuBenh(benhNhan.getTienSuBenh());
        dto.setDiUng(benhNhan.getDiUng());

        // Ánh xạ thông tin dịch vụ nếu có
        if (lichHen.getDichVu() != null) {
            dto.setMaDichVu(Arrays.asList(lichHen.getDichVu().getMaDichVu()));
        }

        return dto;
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
}
