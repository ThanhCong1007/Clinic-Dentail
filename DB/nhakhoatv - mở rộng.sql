CREATE DATABASE IF NOT EXISTS nhakhoa_simple;
USE nhakhoa_simple;

-- Bảng vai trò người dùng
CREATE TABLE vai_tro (
    ma_vai_tro INT AUTO_INCREMENT PRIMARY KEY,
    ten_vai_tro VARCHAR(50) NOT NULL UNIQUE,
    mo_ta TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng người dùng
CREATE TABLE nguoi_dung (
    ma_nguoi_dung INT AUTO_INCREMENT PRIMARY KEY,
    ma_vai_tro INT NOT NULL,
    ten_dang_nhap VARCHAR(50) NOT NULL UNIQUE,
    mat_khau VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    ho_ten VARCHAR(100) NOT NULL,
    so_dien_thoai VARCHAR(20),
    trang_thai_hoat_dong BOOLEAN DEFAULT TRUE,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ma_vai_tro) REFERENCES vai_tro(ma_vai_tro)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng bệnh nhân
CREATE TABLE benh_nhan (
    ma_benh_nhan INT AUTO_INCREMENT PRIMARY KEY,
    ma_nguoi_dung INT,
    ho_ten VARCHAR(100) NOT NULL,
    ngay_sinh DATE,
    gioi_tinh ENUM('Nam', 'Nữ', 'Khác'),
    so_dien_thoai VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    dia_chi TEXT,
    tien_su_benh TEXT,
    di_ung TEXT,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ma_nguoi_dung) REFERENCES nguoi_dung(ma_nguoi_dung) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng bác sĩ
CREATE TABLE bac_si (
    ma_bac_si INT AUTO_INCREMENT PRIMARY KEY,
    ma_nguoi_dung INT NOT NULL,
    chuyen_khoa VARCHAR(100),
    so_nam_kinh_nghiem INT,
    trang_thai_lam_viec BOOLEAN DEFAULT TRUE,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ma_nguoi_dung) REFERENCES nguoi_dung(ma_nguoi_dung)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng dịch vụ
CREATE TABLE dich_vu (
    ma_dich_vu INT AUTO_INCREMENT PRIMARY KEY,
    ten_dich_vu VARCHAR(100) NOT NULL,
    mo_ta TEXT,
    gia DECIMAL(12, 2) NOT NULL,
    thoi_gian_du_kien INT NOT NULL, -- Thời gian dự kiến (phút)
    trang_thai_hoat_dong BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng trạng thái lịch hẹn
CREATE TABLE trang_thai_lich_hen (
    ma_trang_thai INT AUTO_INCREMENT PRIMARY KEY,
    ten_trang_thai VARCHAR(50) NOT NULL UNIQUE,
    mo_ta TEXT,
    ma_mau VARCHAR(10) -- Mã màu để hiển thị trên giao diện
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng lịch hẹn
CREATE TABLE lich_hen (
    ma_lich_hen INT AUTO_INCREMENT PRIMARY KEY,
    ma_benh_nhan INT NOT NULL,
    ma_bac_si INT NOT NULL,
    ma_dich_vu INT,
    ngay_hen DATE NOT NULL,
    gio_bat_dau TIME NOT NULL,
    gio_ket_thuc TIME NOT NULL,
    ma_trang_thai INT NOT NULL,
    ghi_chu TEXT,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ma_benh_nhan) REFERENCES benh_nhan(ma_benh_nhan),
    FOREIGN KEY (ma_bac_si) REFERENCES bac_si(ma_bac_si),
    FOREIGN KEY (ma_dich_vu) REFERENCES dich_vu(ma_dich_vu),
    FOREIGN KEY (ma_trang_thai) REFERENCES trang_thai_lich_hen(ma_trang_thai)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng bệnh án
CREATE TABLE benh_an (
    ma_benh_an INT AUTO_INCREMENT PRIMARY KEY,
    ma_lich_hen INT NOT NULL,
    ma_benh_nhan INT NOT NULL,
    ma_bac_si INT NOT NULL,
    ly_do_kham TEXT, -- Lý do khám
    chan_doan TEXT,
    ghi_chu_dieu_tri TEXT,
    ngay_tai_kham DATE,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ma_lich_hen) REFERENCES lich_hen(ma_lich_hen),
    FOREIGN KEY (ma_benh_nhan) REFERENCES benh_nhan(ma_benh_nhan),
    FOREIGN KEY (ma_bac_si) REFERENCES bac_si(ma_bac_si)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng thông tin răng
CREATE TABLE so_do_rang (
    ma_so_do INT AUTO_INCREMENT PRIMARY KEY,
    ma_benh_nhan INT NOT NULL,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ma_benh_nhan) REFERENCES benh_nhan(ma_benh_nhan)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng chi tiết răng
CREATE TABLE chi_tiet_rang (
    ma_chi_tiet_rang INT AUTO_INCREMENT PRIMARY KEY,
    ma_so_do INT NOT NULL,
    so_rang INT NOT NULL, -- Số răng theo hệ thống FDI (1-32)
    tinh_trang_rang VARCHAR(50), -- Trạng thái răng
    ghi_chu TEXT,
    nguoi_cap_nhat INT, -- User ID của bác sĩ cập nhật
    ngay_cap_nhat TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ma_so_do) REFERENCES so_do_rang(ma_so_do),
    FOREIGN KEY (nguoi_cap_nhat) REFERENCES nguoi_dung(ma_nguoi_dung)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng phương thức thanh toán
CREATE TABLE phuong_thuc_thanh_toan (
    ma_phuong_thuc INT AUTO_INCREMENT PRIMARY KEY,
    ten_phuong_thuc VARCHAR(50) NOT NULL UNIQUE,
    mo_ta TEXT,
    trang_thai_hoat_dong BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng hóa đơn
CREATE TABLE hoa_don (
    ma_hoa_don INT AUTO_INCREMENT PRIMARY KEY,
    ma_benh_nhan INT NOT NULL,
    ma_lich_hen INT,
    tong_tien DECIMAL(12, 2) NOT NULL,
    thanh_tien DECIMAL(12, 2) NOT NULL,
    ngay_hoa_don TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    trang_thai ENUM('Chưa thanh toán', 'Đã thanh toán', 'Hủy bỏ') DEFAULT 'Chưa thanh toán',
    nguoi_tao INT,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ma_benh_nhan) REFERENCES benh_nhan(ma_benh_nhan),
    FOREIGN KEY (ma_lich_hen) REFERENCES lich_hen(ma_lich_hen),
    FOREIGN KEY (nguoi_tao) REFERENCES nguoi_dung(ma_nguoi_dung)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng chi tiết hóa đơn
CREATE TABLE chi_tiet_hoa_don (
    ma_muc INT AUTO_INCREMENT PRIMARY KEY,
    ma_hoa_don INT NOT NULL,
    ma_dich_vu INT,
    mo_ta VARCHAR(255) NOT NULL,
    so_luong INT NOT NULL DEFAULT 1,
    don_gia DECIMAL(12, 2) NOT NULL,
    thanh_tien DECIMAL(12, 2) NOT NULL,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ma_hoa_don) REFERENCES hoa_don(ma_hoa_don),
    FOREIGN KEY (ma_dich_vu) REFERENCES dich_vu(ma_dich_vu)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng thanh toán
CREATE TABLE thanh_toan (
    ma_thanh_toan INT AUTO_INCREMENT PRIMARY KEY,
    ma_hoa_don INT NOT NULL,
    ma_phuong_thuc INT NOT NULL,
    so_tien DECIMAL(12, 2) NOT NULL,
    ngay_thanh_toan TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    trang_thai_thanh_toan ENUM('Thành công', 'Đang xử lý', 'Thất bại') DEFAULT 'Thành công',
    nguoi_tao INT,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ma_hoa_don) REFERENCES hoa_don(ma_hoa_don),
    FOREIGN KEY (ma_phuong_thuc) REFERENCES phuong_thuc_thanh_toan(ma_phuong_thuc),
    FOREIGN KEY (nguoi_tao) REFERENCES nguoi_dung(ma_nguoi_dung)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Thêm dữ liệu mẫu cho bảng vai trò
INSERT INTO vai_tro (ten_vai_tro, mo_ta) VALUES 
('Quản trị', 'Quản trị viên hệ thống'),
('Bác sĩ', 'Bác sĩ nha khoa'),
('Lễ tân', 'Nhân viên lễ tân'),
('Bệnh nhân', 'Bệnh nhân');

-- Thêm dữ liệu mẫu cho bảng trạng thái lịch hẹn
INSERT INTO trang_thai_lich_hen (ten_trang_thai, mo_ta, ma_mau) VALUES 
('Đã đặt', 'Lịch hẹn đã được đặt thành công', '#3498db'),
('Đã xác nhận', 'Lịch hẹn đã được xác nhận', '#2ecc71'),
('Đang thực hiện', 'Bệnh nhân đang được khám', '#f39c12'),
('Hoàn thành', 'Lịch hẹn đã hoàn thành', '#27ae60'),
('Đã hủy', 'Lịch hẹn đã bị hủy', '#e74c3c');

-- Thêm dữ liệu mẫu cho bảng phương thức thanh toán
INSERT INTO phuong_thuc_thanh_toan (ten_phuong_thuc, mo_ta) VALUES 
('Tiền mặt', 'Thanh toán bằng tiền mặt tại quầy'),
('Thẻ tín dụng/ghi nợ', 'Thanh toán bằng thẻ tín dụng hoặc thẻ ghi nợ'),
('Chuyển khoản ngân hàng', 'Thanh toán bằng chuyển khoản ngân hàng');

-- Bảng loại thuốc
CREATE TABLE loai_thuoc (
    ma_loai_thuoc INT AUTO_INCREMENT PRIMARY KEY,
    ten_loai_thuoc VARCHAR(100) NOT NULL UNIQUE,
    mo_ta TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng thuốc (chuẩn quốc tế)
CREATE TABLE thuoc (
    ma_thuoc INT AUTO_INCREMENT PRIMARY KEY,
    ma_loai_thuoc INT,
    ten_thuoc VARCHAR(100) NOT NULL,
    
    -- Mã định danh thuốc theo tiêu chuẩn quốc tế
    ma_atc VARCHAR(10),          -- Mã phân loại hóa học điều trị giải phẫu (WHO ATC code)
    ma_ndc VARCHAR(20),          -- Mã National Drug Code (US FDA)
    ma_rxnorm VARCHAR(20),       -- Mã RxNorm (US)
    ma_snomed VARCHAR(20),       -- Mã SNOMED CT
    ma_product VARCHAR(100),     -- Mã sản phẩm của nhà sản xuất
    ma_gtin VARCHAR(14),         -- Global Trade Item Number (mã vạch toàn cầu)
    
    -- Thông tin thành phần hoạt chất
    hoat_chat VARCHAR(255),      -- Thành phần hoạt chất
    ham_luong VARCHAR(100),      -- Hàm lượng
    
    -- Thông tin nhà sản xuất
    nha_san_xuat VARCHAR(100),   -- Tên nhà sản xuất
    nuoc_san_xuat VARCHAR(50),   -- Nước sản xuất
    
    -- Thông tin sử dụng và quản lý
    dang_bao_che VARCHAR(50),    -- Dạng bào chế (viên nén, viên nang, ống tiêm...)
    don_vi_tinh VARCHAR(50) NOT NULL, -- Đơn vị tính (viên, ống, lọ...)
    duong_dung VARCHAR(100),     -- Đường dùng (uống, tiêm, ngậm...)
    huong_dan_su_dung TEXT,      -- Hướng dẫn sử dụng chi tiết
    cach_bao_quan TEXT,          -- Thông tin bảo quản
    
    -- Thông tin phân loại và cảnh báo
    phan_loai_ke_don ENUM('Không kê đơn', 'Kê đơn', 'Kiểm soát đặc biệt') DEFAULT 'Kê đơn',
    chong_chi_dinh TEXT,         -- Chống chỉ định
    tac_dung_phu TEXT,           -- Tác dụng phụ
    tuong_tac_thuoc TEXT,        -- Tương tác thuốc
    nhom_thuoc_thai_ky VARCHAR(5), -- Phân loại FDA về sử dụng khi mang thai (A,B,C,D,X)
    
    -- Thông tin quản lý kho và kinh doanh
    gia DECIMAL(12, 2) NOT NULL, -- Giá bán
    so_luong_ton INT NOT NULL DEFAULT 0, -- Số lượng tồn kho
    nguong_canh_bao INT DEFAULT 10, -- Ngưỡng cảnh báo hết hàng
    trang_thai_hoat_dong BOOLEAN DEFAULT TRUE, -- Trạng thái hoạt động
    
    -- Thời gian tạo và cập nhật
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ngay_cap_nhat TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    
    -- Khóa ngoại
    FOREIGN KEY (ma_loai_thuoc) REFERENCES loai_thuoc(ma_loai_thuoc) ON DELETE SET NULL,
    
    -- Tạo chỉ mục cho các mã định danh để tìm kiếm nhanh
    INDEX idx_ma_atc (ma_atc),
    INDEX idx_ma_rxnorm (ma_rxnorm),
    INDEX idx_ma_ndc (ma_ndc),
    INDEX idx_ma_snomed (ma_snomed)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng nhà cung cấp thuốc
CREATE TABLE nha_cung_cap (
    ma_nha_cung_cap INT AUTO_INCREMENT PRIMARY KEY,
    ten_nha_cung_cap VARCHAR(100) NOT NULL,
    dia_chi TEXT,
    so_dien_thoai VARCHAR(20),
    email VARCHAR(100),
    nguoi_lien_he VARCHAR(100),
    ghi_chu TEXT,
    trang_thai_hoat_dong BOOLEAN DEFAULT TRUE,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng nhập thuốc
CREATE TABLE nhap_thuoc (
    ma_nhap_thuoc INT AUTO_INCREMENT PRIMARY KEY,
    ma_nha_cung_cap INT NOT NULL,
    ma_nguoi_dung INT NOT NULL,
    ngay_nhap DATE NOT NULL,
    tong_tien DECIMAL(12, 2) NOT NULL,
    ghi_chu TEXT,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ma_nha_cung_cap) REFERENCES nha_cung_cap(ma_nha_cung_cap),
    FOREIGN KEY (ma_nguoi_dung) REFERENCES nguoi_dung(ma_nguoi_dung)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng chi tiết nhập thuốc
CREATE TABLE chi_tiet_nhap_thuoc (
    ma_chi_tiet INT AUTO_INCREMENT PRIMARY KEY,
    ma_nhap_thuoc INT NOT NULL,
    ma_thuoc INT NOT NULL,
    so_luong INT NOT NULL,
    don_gia DECIMAL(12, 2) NOT NULL,
    thanh_tien DECIMAL(12, 2) NOT NULL,
    han_su_dung DATE,
    so_lo VARCHAR(50),
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ma_nhap_thuoc) REFERENCES nhap_thuoc(ma_nhap_thuoc),
    FOREIGN KEY (ma_thuoc) REFERENCES thuoc(ma_thuoc)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng kê thuốc (toa thuốc)
CREATE TABLE ke_thuoc (
    ma_ke_thuoc INT AUTO_INCREMENT PRIMARY KEY,
    ma_benh_an INT NOT NULL,
    ma_benh_nhan INT NOT NULL,
    ma_bac_si INT NOT NULL,
    
    -- Thông tin cơ bản của toa thuốc
    ngay_ke DATE NOT NULL,
    so_toa VARCHAR(50),            -- Số toa thuốc
    
    -- Thông tin chẩn đoán và tình trạng bệnh
    ma_icd VARCHAR(20),            -- Mã ICD-10/11 chẩn đoán
    mo_ta_chan_doan TEXT,          -- Mô tả chẩn đoán
    
    -- Thông tin kiểm tra và xác thực
    trang_thai_kiem_tra ENUM('Chờ kiểm tra', 'Đã kiểm tra', 'Có cảnh báo', 'Không đạt') DEFAULT 'Chờ kiểm tra',
    ket_qua_kiem_tra TEXT,         -- Kết quả kiểm tra từ API
    ma_kiem_tra VARCHAR(100),      -- Mã kiểm tra từ API
    
    -- Thông tin phát hành và xác nhận
    trang_thai_toa ENUM('Mới', 'Đã phát hành', 'Đã phát thuốc', 'Hủy') DEFAULT 'Mới',
    ngay_phat_hanh DATETIME,       -- Ngày phát hành toa thuốc
    ngay_het_han DATE,             -- Ngày hết hạn toa thuốc
    nguoi_phat_thuoc INT,          -- Người phát thuốc
    
    -- Ghi chú và thông tin bổ sung
    ghi_chu TEXT,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ngay_cap_nhat TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    
    -- Khóa ngoại
    FOREIGN KEY (ma_benh_an) REFERENCES benh_an(ma_benh_an),
    FOREIGN KEY (ma_benh_nhan) REFERENCES benh_nhan(ma_benh_nhan),
    FOREIGN KEY (ma_bac_si) REFERENCES bac_si(ma_bac_si),
    FOREIGN KEY (nguoi_phat_thuoc) REFERENCES nguoi_dung(ma_nguoi_dung)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng chi tiết kê thuốc
CREATE TABLE chi_tiet_ke_thuoc (
    ma_chi_tiet INT AUTO_INCREMENT PRIMARY KEY,
    ma_ke_thuoc INT NOT NULL,
    ma_thuoc INT NOT NULL,
    
    -- Thông tin kê đơn chi tiết theo tiêu chuẩn quốc tế
    lieu_dung VARCHAR(255) NOT NULL,       -- Liều dùng chi tiết
    tan_suat VARCHAR(100) NOT NULL,        -- Tần suất (VD: 2 lần/ngày)
    thoi_diem VARCHAR(100),                -- Thời điểm (VD: Sau bữa ăn)
    thoi_gian_dieu_tri INT,                -- Thời gian điều trị (số ngày)
    
    -- Thông tin kinh tế và số lượng
    so_luong INT NOT NULL,                 -- Số lượng
    don_vi_dung VARCHAR(50) NOT NULL,      -- Đơn vị dùng (viên, mL, mg...)
    don_gia DECIMAL(12, 2) NOT NULL,       -- Đơn giá
    thanh_tien DECIMAL(12, 2) NOT NULL,    -- Thành tiền
    
    -- Thông tin kiểm tra và cảnh báo
    canh_bao_tuong_tac TEXT,               -- Cảnh báo tương tác thuốc (nếu có)
    canh_bao_lieu_dung TEXT,               -- Cảnh báo liều dùng (nếu có)
    ly_do_ke_don TEXT,                     -- Lý do kê đơn (chỉ định)
    
    -- Ghi chú và thông tin bổ sung
    ghi_chu TEXT,
    
    -- Khóa ngoại
    FOREIGN KEY (ma_ke_thuoc) REFERENCES ke_thuoc(ma_ke_thuoc),
    FOREIGN KEY (ma_thuoc) REFERENCES thuoc(ma_thuoc)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Thêm dữ liệu mẫu cho bảng loại thuốc
INSERT INTO loai_thuoc (ten_loai_thuoc, mo_ta) VALUES 
('Kháng sinh', 'Các loại thuốc kháng sinh dùng trong nha khoa'),
('Giảm đau', 'Các loại thuốc giảm đau dùng trong nha khoa'),
('Kháng viêm', 'Các loại thuốc kháng viêm dùng trong nha khoa'),
('Gây tê', 'Các loại thuốc gây tê/gây mê dùng trong nha khoa'),
('Bổ sung', 'Các loại thuốc bổ sung vitamin, khoáng chất');

-- Bảng tuong_tac_thuoc (bảng trung gian cho việc kiểm tra tương tác thuốc)
CREATE TABLE tuong_tac_thuoc (
    ma_tuong_tac INT AUTO_INCREMENT PRIMARY KEY,
    ma_thuoc_1 INT NOT NULL,
    ma_thuoc_2 INT NOT NULL,
    muc_do_nghiem_trong ENUM('Nhẹ', 'Trung bình', 'Nghiêm trọng', 'Chống chỉ định') NOT NULL,
    mo_ta TEXT NOT NULL,
    khuyen_nghi TEXT,
    ma_ref_ext VARCHAR(50),  -- Mã tham chiếu từ API bên ngoài
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ngay_cap_nhat TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (ma_thuoc_1) REFERENCES thuoc(ma_thuoc),
    FOREIGN KEY (ma_thuoc_2) REFERENCES thuoc(ma_thuoc),
    UNIQUE KEY idx_tuong_tac_thuoc (ma_thuoc_1, ma_thuoc_2)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng kiểm tra kê đơn (lưu log kết quả từ API kiểm tra toa thuốc)
CREATE TABLE kiem_tra_ke_don (
    ma_kiem_tra INT AUTO_INCREMENT PRIMARY KEY,
    ma_ke_thuoc INT NOT NULL,
    thoi_gian_kiem_tra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ma_api_ref VARCHAR(100),  -- Mã tham chiếu từ API
    ket_qua JSON,            -- Kết quả kiểm tra dạng JSON
    trang_thai ENUM('Đạt', 'Cảnh báo', 'Không đạt') NOT NULL,
    mo_ta TEXT,
    nguoi_kiem_tra INT,
    FOREIGN KEY (ma_ke_thuoc) REFERENCES ke_thuoc(ma_ke_thuoc),
    FOREIGN KEY (nguoi_kiem_tra) REFERENCES nguoi_dung(ma_nguoi_dung)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng lịch sử nhập API (lưu log kết nối với API thuốc quốc tế)
CREATE TABLE lich_su_api (
    ma_lich_su INT AUTO_INCREMENT PRIMARY KEY,
    loai_api VARCHAR(50) NOT NULL,  -- Loại API được gọi
    thoi_gian_goi TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    request TEXT,                   -- Dữ liệu gửi đi
    response TEXT,                  -- Dữ liệu trả về
    ma_doi_tuong INT,               -- ID của đối tượng liên quan (thuốc, toa thuốc...)
    loai_doi_tuong VARCHAR(50),     -- Loại đối tượng liên quan
    trang_thai INT,                 -- Mã trạng thái HTTP
    thoi_gian_xu_ly INT,            -- Thời gian xử lý (ms)
    nguoi_dung INT,                 -- Người dùng thực hiện
    FOREIGN KEY (nguoi_dung) REFERENCES nguoi_dung(ma_nguoi_dung)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;