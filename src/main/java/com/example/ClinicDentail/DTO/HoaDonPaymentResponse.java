package com.example.ClinicDentail.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoaDonPaymentResponse {
    private String code;
    private String message;
    private String paymentUrl;
    private Integer maThanhToan;
}
