package com.example.ClinicDentail.Service;

import com.example.ClinicDentail.DTO.UserDTO;
import com.example.ClinicDentail.Enity.BacSi;
import com.example.ClinicDentail.Enity.BenhNhan;
import com.example.ClinicDentail.Enity.NguoiDung;
import com.example.ClinicDentail.Repository.BacSiRepository;
import com.example.ClinicDentail.Repository.BenhNhanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDTOConverter {

    @Autowired
    private BacSiRepository bacSiRepository;

    @Autowired
    private BenhNhanRepository benhNhanRepository;

    public UserDTO convertToDTO(NguoiDung nguoiDung) {
        if (nguoiDung == null) return null;

        String roleName = nguoiDung.getVaiTro().getTenVaiTro();

        switch (roleName) {
            case "BACSI":
                BacSi bacSi = bacSiRepository.findByNguoiDung_MaNguoiDung(nguoiDung.getMaNguoiDung());
                return bacSi != null ? new UserDTO(bacSi) : new UserDTO(nguoiDung);

            case "USER":
                BenhNhan benhNhan = benhNhanRepository.findByNguoiDung_MaNguoiDung(nguoiDung.getMaNguoiDung());
                return benhNhan != null ? new UserDTO(benhNhan) : new UserDTO(nguoiDung);

            default: // ADMIN
                return new UserDTO(nguoiDung);
        }
    }

    public List<UserDTO> convertToDTO(List<NguoiDung> nguoiDungList) {
        return nguoiDungList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Page<UserDTO> convertToDTO(Page<NguoiDung> nguoiDungPage) {
        return nguoiDungPage.map(this::convertToDTO);
    }

    // Convert BacSi entities to DTO
    public List<UserDTO> convertBacSiToDTO(List<BacSi> bacSiList) {
        return bacSiList.stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    public Page<UserDTO> convertBacSiToDTO(Page<BacSi> bacSiPage) {
        return bacSiPage.map(UserDTO::new);
    }

    // Convert BenhNhan entities to DTO
    public List<UserDTO> convertBenhNhanToDTO(List<BenhNhan> benhNhanList) {
        return benhNhanList.stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    public Page<UserDTO> convertBenhNhanToDTO(Page<BenhNhan> benhNhanPage) {
        return benhNhanPage.map(UserDTO::new);
    }
}
