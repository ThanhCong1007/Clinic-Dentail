package com.example.ClinicDentail.Service;

import com.example.ClinicDentail.DTO.BenhAnDTO;
import com.example.ClinicDentail.DTO.KhamBenhDTO;
import com.example.ClinicDentail.Enity.BenhAn;
import com.example.ClinicDentail.Enity.BenhNhan;
import com.example.ClinicDentail.Enity.NguoiDung;
import com.example.ClinicDentail.Enity.VaiTro;
import com.example.ClinicDentail.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BenhNhanService {
    private static final Logger logger = LoggerFactory.getLogger(BenhNhanService.class);

    @Autowired
    private BenhNhanRepository benhNhanRepository;
    @Autowired
    private VaiTroRepository vaiTroRepository;
    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SMSService smsService;
    // Cập nhật các method con để có thông báo lỗi rõ ràng hơn
    public BenhNhan layThongTinBenhNhan(KhamBenhDTO dto) {
        Integer maBenhNhan = dto.getMaBenhNhan();
        logger.debug("Tìm kiếm bệnh nhân với mã: {}", maBenhNhan);

        try {
            return benhNhanRepository.findById(maBenhNhan)
                    .orElseThrow(() -> {
                        logger.error("Không tìm thấy bệnh nhân với mã: {}", maBenhNhan);
                        return new EntityNotFoundException("Không tìm thấy bệnh nhân với mã: " + maBenhNhan);
                    });
        } catch (Exception e) {
            logger.error("Lỗi truy vấn bệnh nhân mã {} - {}", maBenhNhan, e.getMessage(), e);
            throw new RuntimeException("Lỗi truy vấn bệnh nhân: " + e.getMessage(), e);
        }
    }

    public BenhNhan timBenhNhanTheoSoDienThoai(String soDienThoai) {
        if (soDienThoai == null || soDienThoai.trim().isEmpty()) {
            return null;
        }

        logger.debug("Tìm kiếm bệnh nhân với số điện thoại: {}", soDienThoai);

        try {
            return benhNhanRepository.findBySoDienThoai(soDienThoai.trim()).orElse(null);
        } catch (Exception e) {
            logger.error("Lỗi tìm kiếm bệnh nhân theo số điện thoại {} - {}", soDienThoai, e.getMessage(), e);
            return null;
        }
    }


    public KhamBenhDTO layThongTinBenhNhan(Integer maBenhNhan) {
        BenhNhan benhNhan = benhNhanRepository.findById(maBenhNhan)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh nhân với mã: " + maBenhNhan));

        return new KhamBenhDTO(benhNhan);
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

        try {
            // 1. Tạo tài khoản người dùng với vai trò bệnh nhân
            VaiTro vaiTroBenhNhan = vaiTroRepository.findByTenVaiTro("USER")
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò bệnh nhân"));
            NguoiDung nguoiDung = createNguoiDungForBenhNhan(dto, vaiTroBenhNhan);
            nguoiDung = nguoiDungRepository.save(nguoiDung);
            logger.info("Đã tạo tài khoản người dùng cho bệnh nhân: {}", nguoiDung.getMaNguoiDung());

            // 2. Tạo đối tượng bệnh nhân mới
            BenhNhan benhNhan = new BenhNhan();
            benhNhan.setNguoiDung(nguoiDung);
            benhNhan.setHoTen(dto.getHoTen().trim());
            benhNhan.setNgaySinh(dto.getNgaySinh());
            benhNhan.setSoDienThoai(dto.getSoDienThoai().trim());

            // Các thông tin không bắt buộc
            if (dto.getGioiTinh() != null && !dto.getGioiTinh().trim().isEmpty()) {
                try {
                    benhNhan.setGioiTinh(BenhNhan.GioiTinh.valueOf(dto.getGioiTinh().trim()));
                } catch (IllegalArgumentException e) {
                    logger.warn("Giới tính không hợp lệ: {}", dto.getGioiTinh());
                }
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
            benhNhan = benhNhanRepository.save(benhNhan);
            logger.info("Đã tạo bệnh nhân mới với mã: {}", benhNhan.getMaBenhNhan());

            // 3. Gửi SMS thông báo tài khoản cho bệnh nhân
            try {
                smsService.guiSMSThongBaoTaiKhoan(dto.getSoDienThoai(), dto.getSoDienThoai());
                logger.info("Đã gửi SMS thông báo tài khoản cho số điện thoại: {}", dto.getSoDienThoai());
            } catch (Exception smsException) {
                logger.error("Lỗi khi gửi SMS thông báo: {}", smsException.getMessage(), smsException);
                // Không throw exception để không ảnh hưởng đến việc tạo bệnh nhân
                // Chỉ log lỗi để theo dõi
            }

            return benhNhan;

        } catch (Exception e) {
            logger.error("Lỗi khi tạo bệnh nhân mới: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể tạo bệnh nhân mới: " + e.getMessage(), e);
        }
    }

    private NguoiDung createNguoiDungForBenhNhan(KhamBenhDTO dto, VaiTro vaiTro) {
        String soDienThoai = dto.getSoDienThoai().trim();

        // Kiểm tra xem tên đăng nhập (số điện thoại) đã tồn tại chưa
        if (nguoiDungRepository.existsByTenDangNhap(soDienThoai)) {
            throw new RuntimeException("Số điện thoại đã được sử dụng làm tài khoản: " + soDienThoai);
        }

        NguoiDung nguoiDung = new NguoiDung();
        nguoiDung.setTenDangNhap(soDienThoai); // Tên đăng nhập là số điện thoại
        nguoiDung.setMatKhau(passwordEncoder.encode(soDienThoai)); // Mật khẩu mặc định là số điện thoại
        nguoiDung.setEmail(dto.getEmail() != null ? dto.getEmail().trim() : soDienThoai + "@temp.com");
        nguoiDung.setHoTen(dto.getHoTen().trim());
        nguoiDung.setSoDienThoai(soDienThoai);
        nguoiDung.setTrangThaiHoatDong(true);
        nguoiDung.setVaiTro(vaiTro);

        return nguoiDung;
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
