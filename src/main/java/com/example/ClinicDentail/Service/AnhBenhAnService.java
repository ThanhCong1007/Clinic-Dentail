package com.example.ClinicDentail.Service;

import com.example.ClinicDentail.DTO.AnhBenhAnDTO;
import com.example.ClinicDentail.Enity.AnhBenhAn;
import com.example.ClinicDentail.Enity.BenhAn;
import com.example.ClinicDentail.Repository.AnhBenhAnRepository;
import com.example.ClinicDentail.Repository.BenhAnRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AnhBenhAnService {

    @Autowired
    private BenhAnRepository benhAnRepository;

    @Autowired
    private AnhBenhAnRepository anhBenhAnRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Transactional
    public AnhBenhAn luuAnhBenhAn(AnhBenhAnDTO dto, BenhAn benhAn) {
        String filePath = null;

        // Xử lý file upload nếu có
        if (dto.getFile() != null && !dto.getFile().isEmpty()) {
            filePath = fileStorageService.storeFile(dto.getFile(), dto.getMaBenhAn());
        } else if (dto.getUrl() != null && !dto.getUrl().isEmpty()) {
            // Nếu không có file nhưng có URL (trường hợp cũ)
            filePath = dto.getUrl();
        }

        if (filePath == null) {
            throw new IllegalArgumentException("Cần cung cấp file ảnh hoặc URL");
        }

        // Tạo mới ảnh
        AnhBenhAn anh = new AnhBenhAn();
        anh.setBenhAn(benhAn);
        anh.setUrl(filePath); // Lưu relative path
        anh.setMoTa(dto.getMoTa());
        anh.setNgayTao(LocalDateTime.now());

        return anhBenhAnRepository.save(anh);
    }

    @Transactional
    public void xoaAnhBenhAn(Integer maAnh) {
        AnhBenhAn anh = anhBenhAnRepository.findById(maAnh)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy ảnh với mã: " + maAnh));

        // Xóa file khỏi storage
        fileStorageService.deleteFile(anh.getUrl());

        // Xóa record khỏi DB
        anhBenhAnRepository.delete(anh);
    }
}