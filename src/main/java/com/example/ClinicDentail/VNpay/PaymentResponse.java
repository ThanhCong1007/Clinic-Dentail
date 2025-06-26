package com.example.ClinicDentail.VNpay;

import lombok.Data;

    @Data
    public class PaymentResponse {
        private String code;
        private String message;
        private String data;
    }