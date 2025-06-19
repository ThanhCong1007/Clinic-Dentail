package com.example.ClinicDentail.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietDichVuDTO {
    private Integer maDichVu;
    private BigDecimal gia;
}