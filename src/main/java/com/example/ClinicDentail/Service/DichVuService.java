package com.example.ClinicDentail.Service;

import com.example.ClinicDentail.DTO.DichVuDTO;
import com.example.ClinicDentail.Enity.DichVu;
import com.example.ClinicDentail.Repository.DichVuRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DichVuService {
    private static Logger logger = LoggerFactory.getLogger(DichVuService.class);
    @Autowired
    private DichVuRepository dichVuRepository;

    public DichVu themDichVu(DichVuDTO dichVu) {
        try {
            DichVu dv = new DichVu();
            dv.setTenDichVu(dichVu.getTenDichVu());
            dv.setGia(dichVu.getGia());
            dv.setMoTa(dichVu.getMoTa());
            logger.info("Adding new DichVu: {}", dichVu.getTenDichVu());
            return dichVuRepository.save(dv);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi thêm dịch vụ: " + e.getMessage());
        }
    }
    public DichVu capNhatDichVu(DichVuDTO dichVu) {
        try {
            if (dichVu.getMaDichVu() == null) {
                throw new RuntimeException("Mã dịch vụ không được để trống");
            }
            if (dichVu.getTenDichVu() == null || dichVu.getTenDichVu().isEmpty()) {
                throw new RuntimeException("Tên dịch vụ không được để trống");
            }
            if (dichVu.getGia() == null || dichVu.getGia().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Giá dịch vụ phải lớn hơn 0");
            }
            DichVu dv = dichVuRepository.findById(dichVu.getMaDichVu())
                    .orElseThrow(() -> new RuntimeException("Dịch vụ không tồn tại"));
            dv.setTenDichVu(dichVu.getTenDichVu());
            dv.setGia(dichVu.getGia());
            dv.setMoTa(dichVu.getMoTa());
            logger.info("Updating DichVu: {}", dichVu.getTenDichVu());
            if (!dichVuRepository.existsById(dichVu.getMaDichVu())) {
                throw new RuntimeException("Dịch vụ không tồn tại");
            }
            return dichVuRepository.save(dv);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật dịch vụ: " + e.getMessage());
        }
    }
    public void xoaDichVu(Integer maDichVu) {
        try {
            if (maDichVu == null) {
                throw new RuntimeException("Mã dịch vụ không được để trống");
            }
            logger.info("Deleting DichVu with ID: {}", maDichVu);
            if (!dichVuRepository.existsById(maDichVu)) {
                throw new RuntimeException("Dịch vụ không tồn tại");
            }
            dichVuRepository.deleteById(maDichVu);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa dịch vụ: " + e.getMessage());
        }
    }

}
