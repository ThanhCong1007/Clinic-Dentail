package com.example.ClinicDentail.Service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final String uploadDir = "uploads/benh-an"; // Thư mục lưu ảnh

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
            // Validate file
            if (file.isEmpty()) {
                throw new IllegalArgumentException("File không được rỗng");
            }

            // Validate file type
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Chỉ chấp nhận file ảnh");
            }

            // Tạo tên file unique
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String fileName = "benh_an_" + maBenhAn + "_" +
                    System.currentTimeMillis() + fileExtension;

            // Tạo đường dẫn đầy đủ
            Path targetLocation = Paths.get(uploadDir).resolve(fileName);

            // Lưu file
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Trả về relative path để lưu vào DB
            return "benh-an/" + fileName;

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