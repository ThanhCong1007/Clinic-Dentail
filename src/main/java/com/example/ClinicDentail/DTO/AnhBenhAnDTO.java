package com.example.ClinicDentail.DTO;

import com.example.ClinicDentail.Enity.AnhBenhAn;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class AnhBenhAnDTO {
    private Integer maBenhAn;
    private Integer maAnh;
    private String url;
    private String moTa;
    private MultipartFile file; // Thêm field này để nhận file từ FE

    public AnhBenhAnDTO(AnhBenhAn anhBenhAn) {
        this.maBenhAn = anhBenhAn.getBenhAn() != null ? anhBenhAn.getBenhAn().getMaBenhAn() : null;
        this.maAnh = anhBenhAn.getMaAnh();
        this.url = anhBenhAn.getUrl() != null ? buildUrl(anhBenhAn.getUrl()) : null;
        this.moTa = anhBenhAn.getMoTa();
    }

    private String buildUrl(String filePath) {
        return "http://localhost:8080/uploads/" + filePath;
    }
}