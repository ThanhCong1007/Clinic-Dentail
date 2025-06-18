package com.example.ClinicDentail.Service;

import com.example.ClinicDentail.DTO.BenhAnDTO;
import com.example.ClinicDentail.DTO.KhamBenhDTO;
import com.example.ClinicDentail.Enity.BenhAn;
import com.example.ClinicDentail.Enity.BenhNhan;
import com.example.ClinicDentail.Repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BenhNhanService {
    private static final Logger logger = LoggerFactory.getLogger(BenhNhanService.class);

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

    // Cập nhật các method con để có thông báo lỗi rõ ràng hơn
    public BenhNhan layThongTinBenhNhan(KhamBenhDTO dto) {
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


    public BenhNhan taoBenhNhanMoi(KhamBenhDTO dto) {
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
    public void capNhatThongTinBenhNhan(BenhAn benhAn, BenhAnDTO dto) {
        try {
            BenhNhan benhNhan = benhAn.getBenhNhan();
            if (benhNhan == null) {
                logger.warn("Không có thông tin bệnh nhân để cập nhật cho bệnh án mã: {}", benhAn.getMaBenhAn());
                return;
            }

            boolean coThayDoi = false;

            // Cập nhật các thông tin bệnh nhân nếu có
            if (dto.getHoTen() != null && !dto.getHoTen().trim().isEmpty()
                    && !dto.getHoTen().equals(benhNhan.getHoTen())) {
                benhNhan.setHoTen(dto.getHoTen().trim());
                coThayDoi = true;
            }

            if (dto.getSoDienThoai() != null && !dto.getSoDienThoai().trim().isEmpty()
                    && !dto.getSoDienThoai().equals(benhNhan.getSoDienThoai())) {
                benhNhan.setSoDienThoai(dto.getSoDienThoai().trim());
                coThayDoi = true;
            }

            if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()
                    && !dto.getEmail().equals(benhNhan.getEmail())) {
                benhNhan.setEmail(dto.getEmail().trim());
                coThayDoi = true;
            }

            if (dto.getDiaChi() != null && !dto.getDiaChi().trim().isEmpty()
                    && !dto.getDiaChi().equals(benhNhan.getDiaChi())) {
                benhNhan.setDiaChi(dto.getDiaChi().trim());
                coThayDoi = true;
            }

            if (dto.getNgaySinh() != null && !dto.getNgaySinh().equals(benhNhan.getNgaySinh())) {
                benhNhan.setNgaySinh(dto.getNgaySinh());
                coThayDoi = true;
            }

            if (dto.getGioiTinh() != null && !dto.getGioiTinh().trim().isEmpty()) {
                try {
                    BenhNhan.GioiTinh gioiTinhMoi = BenhNhan.GioiTinh.valueOf(dto.getGioiTinh().toUpperCase());
                    if (!gioiTinhMoi.equals(benhNhan.getGioiTinh())) {
                        benhNhan.setGioiTinh(gioiTinhMoi);
                        coThayDoi = true;
                    }
                } catch (IllegalArgumentException e) {
                    logger.warn("Giới tính không hợp lệ: {}", dto.getGioiTinh());
                }
            }

            if (dto.getTienSuBenh() != null && !dto.getTienSuBenh().equals(benhNhan.getTienSuBenh())) {
                benhNhan.setTienSuBenh(dto.getTienSuBenh().trim());
                coThayDoi = true;
            }

            if (dto.getDiUng() != null && !dto.getDiUng().equals(benhNhan.getDiUng())) {
                benhNhan.setDiUng(dto.getDiUng().trim());
                coThayDoi = true;
            }

            if (coThayDoi) {
                benhNhanRepository.save(benhNhan);
                logger.debug("Đã cập nhật thông tin bệnh nhân mã: {}", benhNhan.getMaBenhNhan());
            } else {
                logger.debug("Không có thay đổi thông tin bệnh nhân mã: {}", benhNhan.getMaBenhNhan());
            }

        } catch (Exception e) {
            logger.error("Lỗi khi cập nhật thông tin bệnh nhân: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi cập nhật thông tin bệnh nhân: " + e.getMessage(), e);
        }
    }
}
