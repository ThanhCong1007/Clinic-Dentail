package com.example.ClinicDentail.payload.request;

import lombok.Data;

@Data
public class TokenValidationResponse {
    private boolean valid;
    private String message;
    private String username;

    public TokenValidationResponse(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    public TokenValidationResponse(boolean valid, String message, String username) {
        this.valid = valid;
        this.message = message;
        this.username = username;
    }

    // Getters and Setters
    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
