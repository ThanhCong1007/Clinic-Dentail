package com.example.ClinicDentail.VNpay;

import lombok.Data;

@Data
public class PaymentRequest {
    private Long amount;
    private String bankCode;
    private String language;
}
