package com.example.ClinicDentail.Service;

import com.example.ClinicDentail.DTO.HoaDonPaymentRequest;
import com.example.ClinicDentail.DTO.HoaDonPaymentResponse;
import com.example.ClinicDentail.Enity.HoaDon;
import com.example.ClinicDentail.Enity.PhuongThucThanhToan;
import com.example.ClinicDentail.Enity.ThanhToan;
import com.example.ClinicDentail.Repository.HoaDonRepository;
import com.example.ClinicDentail.Repository.PhuongThucThanhToanRepository;
import com.example.ClinicDentail.Repository.ThanhToanRepository;
import com.example.ClinicDentail.VNpay.VNPayConfig;
import com.example.ClinicDentail.VNpay.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@Slf4j
public class HoaDonPaymentService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private ThanhToanRepository thanhToanRepository;

    @Autowired
    private PhuongThucThanhToanRepository phuongThucThanhToanRepository;

    @Autowired
    private VNPayUtil vnPayUtil;

    @Autowired
    private VNPayConfig vnPayConfig;

    public HoaDonPaymentResponse createPaymentForHoaDon(HoaDonPaymentRequest request, HttpServletRequest httpRequest) {
        try {
            // Tìm hóa đơn
            HoaDon hoaDon = hoaDonRepository.findById(request.getMaHoaDon())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

            // Kiểm tra trạng thái hóa đơn
            if (hoaDon.getTrangThai() == HoaDon.TrangThaiHoaDon.DA_THANH_TOAN) {
                throw new RuntimeException("Hóa đơn đã được thanh toán");
            }

            if (hoaDon.getTrangThai() == HoaDon.TrangThaiHoaDon.HUY_BO) {
                throw new RuntimeException("Hóa đơn đã bị hủy");
            }

            // Tìm phương thức thanh toán VNPay
            PhuongThucThanhToan phuongThucVNPay = phuongThucThanhToanRepository
                    .findByTenPhuongThuc("VNPay")
                    .orElseThrow(() -> new RuntimeException("Phương thức thanh toán VNPay không tồn tại"));

            // Tạo bản ghi thanh toán với trạng thái DANG_XU_LY
            ThanhToan thanhToan = new ThanhToan();
            thanhToan.setHoaDon(hoaDon);
            thanhToan.setPhuongThucThanhToan(phuongThucVNPay);
            thanhToan.setSoTien(hoaDon.getThanhTien());
            thanhToan.setTrangThaiThanhToan(ThanhToan.TrangThaiThanhToan.DANG_XU_LY);
            thanhToan.setNguoiTao(hoaDon.getBenhNhan().getNguoiDung()); // Assuming BenhNhan has NguoiDung

            ThanhToan savedThanhToan = thanhToanRepository.save(thanhToan);

            // Tạo URL thanh toán VNPay
            String paymentUrl = createVNPayUrl(hoaDon, savedThanhToan, request, httpRequest);

            HoaDonPaymentResponse response = new HoaDonPaymentResponse();
            response.setCode("00");
            response.setMessage("Tạo URL thanh toán thành công");
            response.setPaymentUrl(paymentUrl);
            response.setMaThanhToan(savedThanhToan.getMaThanhToan());

            return response;

        } catch (Exception e) {
            log.error("Lỗi khi tạo thanh toán cho hóa đơn: {}", request.getMaHoaDon(), e);
            HoaDonPaymentResponse response = new HoaDonPaymentResponse();
            response.setCode("99");
            response.setMessage("Lỗi: " + e.getMessage());
            return response;
        }
    }

    private String createVNPayUrl(HoaDon hoaDon, ThanhToan thanhToan, HoaDonPaymentRequest request, HttpServletRequest httpRequest) throws Exception {
        String vnpVersion = "2.1.0";
        String vnpCommand = "pay";
        String orderType = "billpayment";
        long amount = hoaDon.getThanhTien().multiply(new BigDecimal(100)).longValue();
        String bankCode = request.getBankCode();

        // Sử dụng mã thanh toán làm transaction reference
        String vnpTxnRef = String.format("TT%08d", thanhToan.getMaThanhToan());
        String vnpIpAddr = vnPayUtil.getIpAddress(httpRequest);
        String vnpTmnCode = vnPayConfig.getTmnCode();

        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", vnpVersion);
        vnpParams.put("vnp_Command", vnpCommand);
        vnpParams.put("vnp_TmnCode", vnpTmnCode);
        vnpParams.put("vnp_Amount", String.valueOf(amount));
        vnpParams.put("vnp_CurrCode", "VND");

        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParams.put("vnp_BankCode", bankCode);
        }

        vnpParams.put("vnp_TxnRef", vnpTxnRef);
        vnpParams.put("vnp_OrderInfo", String.format("Thanh toan hoa don %d - Benh nhan: %s",
                hoaDon.getMaHoaDon(), hoaDon.getBenhNhan().getHoTen()));
        vnpParams.put("vnp_OrderType", orderType);

        String locale = request.getLanguage();
        if (locale != null && !locale.isEmpty()) {
            vnpParams.put("vnp_Locale", locale);
        } else {
            vnpParams.put("vnp_Locale", "vn");
        }

        vnpParams.put("vnp_ReturnUrl", vnPayConfig.getReturnUrl());
        vnpParams.put("vnp_IpAddr", vnpIpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = formatter.format(cld.getTime());
        vnpParams.put("vnp_CreateDate", vnpCreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnpExpireDate = formatter.format(cld.getTime());
        vnpParams.put("vnp_ExpireDate", vnpExpireDate);

        // Tạo query string và secure hash
        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnpParams.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));

                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));

                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = query.toString();
        String vnpSecureHash = vnPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getPayUrl() + "?" + queryUrl;

        return paymentUrl;
    }

    public void processPaymentReturn(HttpServletRequest request) {
        try {
            // Lấy tất cả parameters từ VNPay
            Map<String, String> fields = new HashMap<>();
            for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
                String fieldName = params.nextElement();
                String fieldValue = request.getParameter(fieldName);
                if (fieldValue != null && fieldValue.length() > 0) {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnpSecureHash = request.getParameter("vnp_SecureHash");
            fields.remove("vnp_SecureHashType");
            fields.remove("vnp_SecureHash");

            String signValue = vnPayUtil.hashAllFields(fields, vnPayConfig.getSecretKey());
            String responseCode = request.getParameter("vnp_ResponseCode");
            String vnpTxnRef = request.getParameter("vnp_TxnRef");
            String transactionNo = request.getParameter("vnp_TransactionNo");

            if (signValue.equals(vnpSecureHash)) {
                // Tìm bản ghi thanh toán từ vnpTxnRef
                Integer maThanhToan = Integer.parseInt(vnpTxnRef.substring(2)); // Bỏ "TT" prefix
                ThanhToan thanhToan = thanhToanRepository.findById(maThanhToan)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy bản ghi thanh toán"));

                if ("00".equals(responseCode)) {
                    // Thanh toán thành công
                    thanhToan.setTrangThaiThanhToan(ThanhToan.TrangThaiThanhToan.THANH_CONG);
                    thanhToan.setNgayThanhToan(LocalDateTime.now());

                    // Cập nhật trạng thái hóa đơn
                    HoaDon hoaDon = thanhToan.getHoaDon();
                    hoaDon.setTrangThai(HoaDon.TrangThaiHoaDon.DA_THANH_TOAN);

                    hoaDonRepository.save(hoaDon);
                    thanhToanRepository.save(thanhToan);

                    log.info("Thanh toán thành công cho hóa đơn: {}, Transaction: {}",
                            hoaDon.getMaHoaDon(), transactionNo);
                } else {
                    // Thanh toán thất bại
                    thanhToan.setTrangThaiThanhToan(ThanhToan.TrangThaiThanhToan.THAT_BAI);
                    thanhToanRepository.save(thanhToan);

                    log.warn("Thanh toán thất bại cho hóa đơn: {}, Response code: {}",
                            thanhToan.getHoaDon().getMaHoaDon(), responseCode);
                }
            } else {
                log.error("Chữ ký không hợp lệ cho transaction: {}", vnpTxnRef);
            }
        } catch (Exception e) {
            log.error("Lỗi khi xử lý kết quả thanh toán", e);
        }
    }
}
