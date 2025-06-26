package com.example.ClinicDentail.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoaDonPaymentRequest {
    private Integer maHoaDon;
    private String bankCode;
    private String language;
}