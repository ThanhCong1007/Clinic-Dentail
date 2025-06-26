package com.example.ClinicDentail.VNpay;

import lombok.Data;

@Data
public class PaymentReturnResponse {
    private String status;
    private String message;
    private String orderInfo;
    private String amount;
    private String orderNumber;
    private String transactionNumber;
    private String bankCode;
    private String payDate;
    private String responseCode;
    private String transactionStatus;
}