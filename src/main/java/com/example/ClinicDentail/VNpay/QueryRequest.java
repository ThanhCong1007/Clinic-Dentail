package com.example.ClinicDentail.VNpay;

import lombok.Data;

@Data
public class QueryRequest {
    private String orderId;
    private String transDate;
}