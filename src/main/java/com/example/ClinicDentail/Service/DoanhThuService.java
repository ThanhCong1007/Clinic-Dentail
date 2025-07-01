package com.example.ClinicDentail.Service;

import com.example.ClinicDentail.DTO.DoanhThuDTO;
import com.example.ClinicDentail.Repository.DoanhThuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DoanhThuService {

    @Autowired
    private DoanhThuRepository doanhThuRepository;

    public List<DoanhThuDTO> getTongKetDoanhThu(String loaiThongKe, Integer nam, Integer thang, Integer quy) {
        List<Object[]> results = doanhThuRepository.getTongKetDoanhThu(loaiThongKe, nam, thang, quy);

        return results.stream()
                .map(row -> new DoanhThuDTO(
                        (String) row[0],           // nhan
                        (BigDecimal) row[1],       // doanh_thu
                        ((Number) row[2]).intValue(), // so_hoa_don
                        (String) row[3]            // loai_thong_ke
                ))
                .collect(Collectors.toList());
    }

    public List<DoanhThuDTO> getDoanhThuTheoBacSi(String loaiThongKe, Integer nam, Integer thang, Integer quy, Integer maBacSi) {
        List<Object[]> results = doanhThuRepository.getDoanhThuTheoBacSi(loaiThongKe, nam, thang, quy, maBacSi);

        return results.stream()
                .map(row -> new DoanhThuDTO(
                        ((Number) row[0]).intValue(), // ma_bac_si
                        (String) row[1],              // ten_bac_si
                        (String) row[2],              // chuyen_khoa
                        (String) row[3],              // nhan
                        (BigDecimal) row[4],          // doanh_thu
                        ((Number) row[5]).intValue(), // so_hoa_don
                        (String) row[6]               // loai_thong_ke
                ))
                .collect(Collectors.toList());
    }
}