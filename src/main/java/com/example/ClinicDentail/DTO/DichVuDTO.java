package com.example.ClinicDentail.DTO;

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
}
