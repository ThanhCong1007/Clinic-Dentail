package com.example.ClinicDentail.Controller;

import com.example.ClinicDentail.DTO.ThamKhamRequestDTO;
import com.example.ClinicDentail.DTO.ThamKhamResponseDTO;
import com.example.ClinicDentail.DTO.UserDTO;
import com.example.ClinicDentail.Enity.BenhNhan;
import com.example.ClinicDentail.Service.ThamKhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tham-kham")
@CrossOrigin(origins = "*")
public class ThamKhamController {

    @Autowired
    private ThamKhamService thamKhamService;

    /**
     * Thực hiện thăm khám cho bệnh nhân.
     * - Nếu có mã lịch hẹn: kiểm tra lịch hẹn có thể thăm khám hay không.
     * - Nếu không có lịch hẹn (khách vãng lai): kiểm tra họ tên và số điện thoại.
     * @param request DTO chứa thông tin yêu cầu thăm khám:
     *
     * @return 200 OK nếu thăm khám thành công; 400 Bad Request nếu dữ liệu không hợp lệ hoặc có lỗi.
     */
    @PostMapping("/kham-benh")
    @PreAuthorize("hasRole('BACSI')")
    public ResponseEntity<?> thucHienThamKham(@RequestBody ThamKhamRequestDTO request) {
        try {
            // Validate dữ liệu đầu vào
            if (request.getMaBacSi() == null) {
                return ResponseEntity.badRequest().body("Mã bác sĩ không được để trống");
            }

            if (request.getMaLichHen() == null) {
                // Trường hợp khách vãng lai - validate thông tin bắt buộc
                if (request.getHoTen() == null || request.getHoTen().trim().isEmpty()) {
                    return ResponseEntity.badRequest().body("Họ tên không được để trống");
                }
                if (request.getSoDienThoai() == null || request.getSoDienThoai().trim().isEmpty()) {
                    return ResponseEntity.badRequest().body("Số điện thoại không được để trống");
                }
            } else {
                // Trường hợp có lịch hẹn - kiểm tra lịch hẹn có thể khám
                if (!thamKhamService.kiemTraLichHenCoTheKham(request.getMaLichHen())) {
                    return ResponseEntity.badRequest().body("Lịch hẹn không trong trạng thái có thể thăm khám");
                }
            }

            if (request.getLyDoKham() == null || request.getLyDoKham().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Lý do khám không được để trống");
            }

            ThamKhamResponseDTO response = thamKhamService.thamKhamBenhNhan(request);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    /**
     * Tìm kiếm thông tin bệnh nhân theo số điện thoại.
     * @param soDienThoai Số điện thoại của bệnh nhân.
     * @return 200 OK với thông tin bệnh nhân nếu tìm thấy; 404 Not Found nếu không có dữ liệu;
     *         400 Bad Request nếu có lỗi trong quá trình xử lý.
     */
    @GetMapping("/benh-nhan/sdt/{soDienThoai}")
    @PreAuthorize("hasRole('BACSI') or hasRole('ADMIN')")
    public ResponseEntity<?> getBenhNhanBySoDienThoai(@PathVariable String soDienThoai) {
        try {
            UserDTO benhNhanDTO = thamKhamService.getBenhNhanDTOBySoDienThoai(soDienThoai);
            if (benhNhanDTO != null) {
                return ResponseEntity.ok(benhNhanDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    /**
     * Kiểm tra xem lịch hẹn có thể thăm khám được hay không.
     * @param maLichHen Mã lịch hẹn cần kiểm tra.
     * @return 200 OK với kết quả {@code { "coTheKham": true/false }};
     *         400 Bad Request nếu có lỗi xử lý.
     */
    @GetMapping("/lich-hen/{maLichHen}/kiem-tra")
    public ResponseEntity<?> kiemTraLichHen(@PathVariable Integer maLichHen) {
        try {
            boolean coTheKham = thamKhamService.kiemTraLichHenCoTheKham(maLichHen);
            return ResponseEntity.ok(java.util.Map.of("coTheKham", coTheKham));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }
}