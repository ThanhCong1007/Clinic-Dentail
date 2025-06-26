package com.example.ClinicDentail.VNpay;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "vnpay")
@Data
public class VNPayConfig {
    private String payUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private String returnUrl = "http://localhost:8080/api/vnpay/return";
    private String tmnCode = "92K975SY";
    private String secretKey = "ZKHA0C8TRK5R3BNEZOTAP9AS2LSCH4XW";
    private String apiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";
}
