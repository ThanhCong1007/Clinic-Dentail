package com.example.ClinicDentail.DTO;

public class QuenMatKhauDTO {
    private String soDienThoai;
    private String otp;
    private String matKhauMoi;
    private String message;
    private Boolean success;

    // Constructors
    public QuenMatKhauDTO() {}

    public QuenMatKhauDTO(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public QuenMatKhauDTO(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters and Setters
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }

    public String getMatKhauMoi() { return matKhauMoi; }
    public void setMatKhauMoi(String matKhauMoi) { this.matKhauMoi = matKhauMoi; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Boolean getSuccess() { return success; }
    public void setSuccess(Boolean success) { this.success = success; }
}