package com.example.ClinicDentail.Controller;

import com.example.ClinicDentail.DTO.*;
import com.example.ClinicDentail.Enity.BenhNhan;
import com.example.ClinicDentail.Service.BenhNhanService;
import com.example.ClinicDentail.Service.LichHenService;
import com.example.ClinicDentail.Service.ThamKhamService;
import com.example.ClinicDentail.payload.request.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tham-kham")
@CrossOrigin(origins = "*")
public class ThamKhamController {
    private final Logger logger = LoggerFactory.getLogger(ThamKhamController.class);

    @Autowired
    private ThamKhamService thamKhamService;
    @Autowired
    private BenhNhanService benhNhanService;
    @Autowired
    private LichHenService lichHenService;

    /**
     * Thực hiện khám bệnh
     * @param dto
     * @return
     */
    @PostMapping(value = "/kham", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<?> khamBenh(
            @RequestPart("data") KhamBenhDTO dto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            // Gán danh sách file vào DTO
            if (images != null && !images.isEmpty()) {
                List<AnhBenhAnDTO> danhSachAnhBenhAn = new ArrayList<>();
                for (int i = 0; i < images.size(); i++) {
                    AnhBenhAnDTO anhDTO = new AnhBenhAnDTO();
                    anhDTO.setFile(images.get(i));

                    // Nếu có mô tả được gửi kèm trong DTO
                    if (dto.getDanhSachAnhBenhAn() != null &&
                            i < dto.getDanhSachAnhBenhAn().size()) {
                        anhDTO.setMoTa(dto.getDanhSachAnhBenhAn().get(i).getMoTa());
                    }

                    danhSachAnhBenhAn.add(anhDTO);
                }
                dto.setDanhSachAnhBenhAn(danhSachAnhBenhAn);
            }

            KhamBenhDTO result = thamKhamService.khamBenh(dto);
            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            logger.error("Lỗi khi khám bệnh: {}", e.getMessage(), e);

            String errorMessage = e.getMessage();
            if (errorMessage == null || errorMessage.trim().isEmpty()) {
                errorMessage = "Lỗi: Có lỗi không xác định trong quá trình khám bệnh!";
            }

            return ResponseEntity.badRequest()
                    .body(new MessageResponse(errorMessage));

        } catch (Exception e) {
            logger.error("Lỗi hệ thống khi khám bệnh: {}", e.getMessage(), e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi hệ thống: Không thể kết nối cơ sở dữ liệu! Vui lòng thử lại sau."));
        }
    }

    /**
     * Cập nhật bệnh án
     */
    @PutMapping(value = "/benh-an/{maBenhAn}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('BACSI') or hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> capNhatBenhAn(
            @PathVariable Integer maBenhAn,
            @RequestPart("data") BenhAnDTO dto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            logger.info("Bắt đầu cập nhật bệnh án mã: {}", maBenhAn);

            // Xử lý hình ảnh tương tự như hàm khám bệnh
            if (images != null && !images.isEmpty()) {
                List<AnhBenhAnDTO> danhSachAnhBenhAn = new ArrayList<>();
                for (int i = 0; i < images.size(); i++) {
                    AnhBenhAnDTO anhDTO = new AnhBenhAnDTO();
                    anhDTO.setFile(images.get(i));

                    // Nếu có mô tả được gửi kèm trong DTO
                    if (dto.getDanhSachAnhBenhAn() != null &&
                            i < dto.getDanhSachAnhBenhAn().size()) {
                        anhDTO.setMoTa(dto.getDanhSachAnhBenhAn().get(i).getMoTa());
                    }

                    danhSachAnhBenhAn.add(anhDTO);
                }
                dto.setDanhSachAnhBenhAn(danhSachAnhBenhAn);
            }

            // Gọi service với DTO đã được xử lý
            BenhAnDTO result = thamKhamService.capNhatBenhAn(maBenhAn, dto);

            logger.info("Cập nhật bệnh án thành công mã: {}", maBenhAn);
            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            logger.error("Lỗi khi cập nhật bệnh án mã: {} - Chi tiết: {}", maBenhAn, e.getMessage(), e);

            String errorMessage = e.getMessage();
            if (errorMessage == null || errorMessage.trim().isEmpty()) {
                errorMessage = "Lỗi: Có lỗi không xác định trong quá trình cập nhật bệnh án!";
            }

            return ResponseEntity.badRequest()
                    .body(new MessageResponse(errorMessage));

        } catch (Exception e) {
            logger.error("Lỗi hệ thống khi cập nhật bệnh án mã: {} - Chi tiết: {}", maBenhAn, e.getMessage(), e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Lỗi hệ thống: Không thể kết nối cơ sở dữ liệu! Vui lòng thử lại sau."));
        }
    }

    /** Lấy thông tin bệnh nhân
     *
     * @param maBenhNhan
     * @return
     */
    @GetMapping("/benh-nhan/{maBenhNhan}")
    public ResponseEntity<KhamBenhDTO> layThongTinBenhNhan(@PathVariable Integer maBenhNhan) {
        try {
            KhamBenhDTO result = benhNhanService.layThongTinBenhNhan(maBenhNhan);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    /** Lấy thông tin lịch hẹn
     *
     * @param maLichHen
     * @return
     */
        @GetMapping("/lich-hen/{maLichHen}")
    public ResponseEntity<KhamBenhDTO> layThongTinLichHen(@PathVariable Integer maLichHen) {
        try {
            KhamBenhDTO result = lichHenService.layThongTinLichHen(maLichHen);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    /**
     * Tìm kiếm thông tin bệnh nhân theo số điện thoại
     */
    @GetMapping("/benh-nhan/sdt/{soDienThoai}")
    @PreAuthorize("hasRole('BACSI') or hasRole('ADMIN')")
    public ResponseEntity<?> getBenhNhanBySoDienThoai(@PathVariable String soDienThoai) {
        try {
            List<UserDTO> benhNhanList = thamKhamService.searchBenhNhanBySoDienThoai(soDienThoai);

            if (!benhNhanList.isEmpty()) {
                return ResponseEntity.ok(benhNhanList);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    /**
     * Lấy danh sách lịch hẹn kèm bệnh án theo bác sĩ
     */
    @GetMapping("/bac-si/{maBacSi}/lich-hen-benh-an")
    @PreAuthorize("hasRole('BACSI') or hasRole('ADMIN')")
    public ResponseEntity<?> getLichHenBenhAnByBacSi(@PathVariable Integer maBacSi) {
        try {
            List<LichHenBenhAnDTO> result = thamKhamService.getLichHenBenhAnByBacSi(maBacSi);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    /**
     * Lấy chi tiết lịch hẹn và bệnh án
     */
    @GetMapping("/lich-hen/{maLichHen}/chi-tiet")
    @PreAuthorize("hasRole('BACSI') or hasRole('ADMIN')")
    public ResponseEntity<?> getChiTietLichHenBenhAn(@PathVariable Integer maLichHen) {
        try {
            LichHenBenhAnDTO result = thamKhamService.getChiTietLichHenBenhAn(maLichHen);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }


}
