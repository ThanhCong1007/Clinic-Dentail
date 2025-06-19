package com.example.ClinicDentail.DTO;

import com.example.ClinicDentail.Enity.BenhAnDichVu;
import com.example.ClinicDentail.Enity.DichVu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DichVuDTO {
    private Integer  maDichVu;
    private String tenDichVu;
    private String moTa;
    private BigDecimal gia;
    public DichVuDTO(BenhAnDichVu dichVu) {
        this.maDichVu = dichVu.getDichVu().getMaDichVu();
        this.tenDichVu = dichVu.getDichVu().getTenDichVu();
        this.moTa = dichVu.getDichVu().getMoTa();
        this.gia = dichVu.getGia();
    }
}
