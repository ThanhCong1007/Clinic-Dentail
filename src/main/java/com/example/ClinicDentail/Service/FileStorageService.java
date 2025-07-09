package com.example.ClinicDentail.Service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final String uploadDir = new File("src/main/resources/static/uploads/benh-an").getAbsolutePath();
    @Value("${app.base-url}")
    private String baseUrl;

    private String getFullImageUrl(String filePath) {
        return baseUrl  + filePath;
    }
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("Không thể tạo thư mục upload: " + uploadDir, e);
        }
    }

    public String storeFile(MultipartFile file, Integer maBenhAn) {
        try {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("File không được rỗng");
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Chỉ chấp nhận file ảnh");
            }

            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String fileName = "benh_an_" + maBenhAn + "_" + System.currentTimeMillis() + fileExtension;

            // Đường dẫn thực tế trên ổ đĩa
            Path targetLocation = Paths.get("src/main/resources/static/uploads/benh-an").resolve(fileName);
            Files.createDirectories(targetLocation.getParent());
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Trả về URL đầy đủ để client sử dụng
            return getFullImageUrl("benh-an/" + fileName);

        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi lưu file: " + file.getOriginalFilename(), e);
        }
    }


    public void deleteFile(String filePath) {
        try {
            Path path = Paths.get(uploadDir).resolve(filePath.replace("benh-an/", ""));
            Files.deleteIfExists(path);
        } catch (IOException e) {
            // Log error nhưng không throw exception
            System.err.println("Không thể xóa file: " + filePath);
        }
    }
}