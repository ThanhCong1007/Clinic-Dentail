package com.example.ClinicDentail.VNpay;

import lombok.Data;

@Data
public class RefundRequest {
    private String tranType;
    private String orderId;
    private Long amount;
    private String transDate;
    private String user;
}
