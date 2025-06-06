package com.example.ClinicDentail.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DichVuDTO {
    private Integer  maDichVu;
    private String tenDichVu;
    private String moTa;
    private String gia;
}
