CREATE DATABASE IF NOT EXISTS nhakhoachuan;
USE nhakhoachuan;

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

-- Thêm dữ liệu mẫu cho bảng trạng thái lịch hẹn
INSERT INTO trang_thai_lich_hen (ten_trang_thai, mo_ta, ma_mau) VALUES
    ('Đã đặt', 'Lịch hẹn đã được đặt thành công', '#3498db'),
    ('Đã xác nhận', 'Lịch hẹn đã được xác nhận', '#2ecc71'),
    ('Đang thực hiện', 'Bệnh nhân đang được khám', '#f39c12'),
    ('Hoàn thành', 'Lịch hẹn đã hoàn thành', '#27ae60'),
    ('Đã hủy', 'Lịch hẹn đã bị hủy', '#e74c3c');

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
    ly_do TEXT,
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

CREATE TABLE benh_an_dich_vu (
 id INT AUTO_INCREMENT PRIMARY KEY,
 ma_benh_an INT NOT NULL,
 ma_dich_vu INT NOT NULL,
 gia DECIMAL(12, 2) NOT NULL,
 UNIQUE (ma_benh_an, ma_dich_vu), -- tránh trùng lặp dịch vụ trong 1 bệnh án
 FOREIGN KEY (ma_benh_an) REFERENCES benh_an(ma_benh_an) ON DELETE CASCADE,
 FOREIGN KEY (ma_dich_vu) REFERENCES dich_vu(ma_dich_vu) ON DELETE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
    trang_thai ENUM('Chưa thanh toán', 'Đã thanh toán', 'Hủy bỏ') DEFAULT 'Chưa thanh toán' ,
    nguoi_tao INT,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ma_benh_nhan) REFERENCES benh_nhan(ma_benh_nhan),
    FOREIGN KEY (ma_lich_hen) REFERENCES lich_hen(ma_lich_hen),
    FOREIGN KEY (nguoi_tao) REFERENCES nguoi_dung(ma_nguoi_dung)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE chi_tiet_hoa_don (
  ma_muc INT AUTO_INCREMENT PRIMARY KEY,
  ma_hoa_don INT NOT NULL,
  ma_benh_an_dich_vu INT, -- dùng để liên kết tới bảng trung gian
  mo_ta VARCHAR(255) NOT NULL,
  so_luong INT NOT NULL DEFAULT 1,
  don_gia DECIMAL(12, 2) NOT NULL,
  thanh_tien DECIMAL(12, 2) NOT NULL,
  ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  FOREIGN KEY (ma_hoa_don) REFERENCES hoa_don(ma_hoa_don),
  FOREIGN KEY (ma_benh_an_dich_vu) REFERENCES benh_an_dich_vu(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng thanh toán
CREATE TABLE thanh_toan (
    ma_thanh_toan INT AUTO_INCREMENT PRIMARY KEY,
    ma_hoa_don INT NOT NULL,
    ma_phuong_thuc INT NOT NULL,
    so_tien DECIMAL(12, 2) NOT NULL,
    ngay_thanh_toan TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    trang_thai_thanh_toan ENUM('Thành công', 'Đang xử lý', 'Thất bại','Chưa thanh toán') DEFAULT 'Thành công',
    nguoi_tao INT,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ma_hoa_don) REFERENCES hoa_don(ma_hoa_don),
    FOREIGN KEY (ma_phuong_thuc) REFERENCES phuong_thuc_thanh_toan(ma_phuong_thuc),
    FOREIGN KEY (nguoi_tao) REFERENCES nguoi_dung(ma_nguoi_dung)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- Thêm dữ liệu mẫu cho bảng phương thức thanh toán
INSERT INTO phuong_thuc_thanh_toan (ten_phuong_thuc, mo_ta) VALUES
('Tiền mặt', 'Thanh toán bằng tiền mặt tại quầy'),
('Thẻ tín dụng/ghi nợ', 'Thanh toán bằng thẻ tín dụng hoặc thẻ ghi nợ'),
('Chuyển khoản ngân hàng', 'Thanh toán bằng chuyển khoản ngân hàng'),
('VNPay', 'Thanh toán trực tuyến qua VNPay');

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
    phan_loai_don_thuoc varchar(50),
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
CREATE TABLE don_thuoc (
    ma_don_thuoc INT AUTO_INCREMENT PRIMARY KEY,
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
    trang_thai_toa ENUM('MOI', 'DA_PHAT_HANH', 'DA_PHAT_THUOC', 'HUY') DEFAULT 'MOI',
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
CREATE TABLE chi_tiet_don_thuoc (
    ma_chi_tiet INT AUTO_INCREMENT PRIMARY KEY,
    ma_don_thuoc INT NOT NULL,
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
    ly_do_don_thuoc TEXT,                     -- Lý do kê đơn (chỉ định)
    
    -- Ghi chú và thông tin bổ sung
    ghi_chu TEXT,
    
    -- Khóa ngoại
    FOREIGN KEY (ma_don_thuoc) REFERENCES don_thuoc(ma_don_thuoc),
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
CREATE TABLE kiem_tra_don_thuoc (
    ma_kiem_tra INT AUTO_INCREMENT PRIMARY KEY,
    ma_don_thuoc INT NOT NULL,
    thoi_gian_kiem_tra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ma_api_ref VARCHAR(100),  -- Mã tham chiếu từ API
    ket_qua JSON,            -- Kết quả kiểm tra dạng JSON
    trang_thai ENUM('Đạt', 'Cảnh báo', 'Không đạt') NOT NULL,
    mo_ta TEXT,
    nguoi_kiem_tra INT,
    FOREIGN KEY (ma_don_thuoc) REFERENCES don_thuoc(ma_don_thuoc),
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

INSERT INTO vai_tro (ma_vai_tro, ten_vai_tro, mo_ta) VALUES
(5, 'ADMIN', 'Quản trị hệ thống'),
(6, 'BACSI', 'Bác sĩ trong hệ thống'),
(7, 'USER', 'Người dùng bình thường');

INSERT INTO nguoi_dung (ma_vai_tro, ten_dang_nhap, mat_khau, email, ho_ten, so_dien_thoai)
VALUES
-- ADMIN
(5, 'admin01', '234234', 'admin01@example.com', 'Nguyễn Văn A', '0901234567'),

-- BACSI
(6, 'bacsi01', '234234', 'bacsi01@example.com', 'Trần Thị B', '0912345678'),
(6, 'bacsi02', '234234', 'bacsi02@example.com', 'Lê Văn C', '0923456789'),

-- USER
(7, 'user01', '234234', 'user01@example.com', 'Phạm Thị D', '0934567890'),
(7, 'user02', '234234', 'user02@example.com', 'Đỗ Văn E', '0945678901');
INSERT INTO bac_si (ma_nguoi_dung, chuyen_khoa, so_nam_kinh_nghiem)
VALUES
    (2, 'Răng hàm mặt', 5),
    (3, 'Nha chu', 3);
INSERT INTO benh_nhan (ma_nguoi_dung, ho_ten, ngay_sinh, gioi_tinh, so_dien_thoai, email, dia_chi, tien_su_benh, di_ung)
VALUES
    (4, 'Phạm Thị D', '1995-04-12', 'Nữ', '0934567890', 'user01@example.com', '123 Lê Lợi, TP.HCM', 'Viêm lợi mãn tính', 'Penicillin'),
    (5, 'Đỗ Văn E', '1989-08-22', 'Nam', '0945678901', 'user02@example.com', '456 Trần Hưng Đạo, TP.HCM', 'Không rõ', 'Không');
INSERT INTO dich_vu (ten_dich_vu, mo_ta, gia, thoi_gian_du_kien, trang_thai_hoat_dong) VALUES
('Khám tổng quát', 'Kiểm tra tổng quát tình trạng răng miệng', 100000, 30, TRUE),
('Lấy cao răng', 'Làm sạch cao răng và mảng bám', 200000, 45, TRUE),
('Trám răng', 'Trám răng sâu bằng vật liệu composite', 300000, 60, TRUE),
('Nhổ răng sữa', 'Nhổ răng sữa cho trẻ em', 150000, 30, TRUE),
('Nhổ răng vĩnh viễn', 'Nhổ răng vĩnh viễn thông thường', 500000, 60, TRUE),
('Tẩy trắng răng', 'Tẩy trắng răng bằng công nghệ laser', 1200000, 90, TRUE),
('Niềng răng', 'Tư vấn và thực hiện niềng răng chỉnh nha', 25000000, 120, TRUE),
('Chụp X-quang răng', 'Chụp phim X-quang toàn hàm', 250000, 20, TRUE);

INSERT INTO `thuoc` VALUES (101,NULL,'Paracetamol','N02BE01',NULL,NULL,NULL,NULL,NULL,'Paracetamol','500','Hoffmann-La Roche','Thụy Sĩ','Viên nén','mg','Uống','Giảm đau, hạ sốt','Nơi khô mát dưới 30°C','KE_DON','Suy gan, quá mẫn','Tăng men gan, dị ứng','Warfarin','B',10000.00,100,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(102,NULL,'Ibuprofen','M01AE01',NULL,NULL,NULL,NULL,NULL,'Ibuprofen','200','Abbott Laboratories','Mỹ','Viên nén','mg','Uống','Giảm đau, chống viêm','Nơi khô mát dưới 30°C','KE_DON','Loét tiêu hóa, suy thận nặng','Loét dạ dày, suy thận','Thuốc chống đông, ACEI','D',12000.00,50,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(103,NULL,'Aspirin','N02BA01',NULL,NULL,NULL,NULL,NULL,'Axít acetylsalicylic','325','Bayer AG','Đức','Viên nén','mg','Uống','Giảm đau, hạ sốt, kháng tiểu cầu','Nơi khô mát dưới 25°C','KE_DON','Loét dạ dày, thiếu máu, trẻ em','Xuất huyết, loét','Kháng đông','D',8000.00,80,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(104,NULL,'Amoxicillin','J01CA04',NULL,NULL,NULL,NULL,NULL,'Amoxicillin','500','GlaxoSmithKline','Anh','Viên nang','mg','Uống','Kháng sinh phổ rộng','Nơi khô mát dưới 25°C','KE_DON','Dị ứng penicillin','Tiêu chảy, dị ứng','Methotrexate','B',15000.00,30,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(105,NULL,'Azithromycin','J01FA10',NULL,NULL,NULL,NULL,NULL,'Azithromycin','250','Pfizer','Mỹ','Viên nén','mg','Uống','Kháng sinh macrolide','Nơi khô mát dưới 25°C','KE_DON','Dị ứng macrolide, bệnh tim','Tiêu chảy, buồn nôn','Warfarin','B',30000.00,20,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(106,NULL,'Ciprofloxacin','J01MA02',NULL,NULL,NULL,NULL,NULL,'Ciprofloxacin','500','Bayer','Đức','Viên nén','mg','Uống','Kháng sinh fluoroquinolone','Nơi khô mát dưới 30°C','KE_DON','Trẻ em, thai kỳ','Buồn nôn, đau đầu','Antacid, Theophylline','C',20000.00,25,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(107,NULL,'Metformin','A10BA02',NULL,NULL,NULL,NULL,NULL,'Metformin','500','Merck','Mỹ','Viên nén','mg','Uống','Hạ đường máu nhóm Biguanide','Nơi khô mát dưới 25°C','KE_DON','Suy thận nặng, suy tim','Tiêu chảy, buồn nôn','Cản quang','B',2000.00,50,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(108,NULL,'Omeprazole','A02BC01',NULL,NULL,NULL,NULL,NULL,'Omeprazole','20','AstraZeneca','Anh','Viên bao tan','mg','Uống','Giảm tiết acid, điều trị loét','Nơi khô mát dưới 25°C','KE_DON','Dị ứng thuốc nhóm PPI','Đau bụng, nhức đầu','Clopidogrel','B',10000.00,40,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(109,NULL,'Salbutamol','R03AC02',NULL,NULL,NULL,NULL,NULL,'Salbutamol','100','GlaxoSmithKline','Anh','Bình xịt','µg/phút','Hít','Giãn phế quản điều trị hen','Nơi khô mát dưới 30°C','KE_DON','Tim mạch không ổn định','Run tay, đánh trống ngực','Thuốc chẹn β','B',50000.00,15,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(110,NULL,'Insulin (trung tính)','A10AC01',NULL,NULL,NULL,NULL,NULL,'Insulin','100','Novo Nordisk','Đan Mạch','Dung dịch tiêm','IU/ml','Tiêm dưới da','Hạ đường huyết điều trị tiểu đường','Bảo quản 2–8°C','KE_DON','Hạ đường huyết cấp','Hạ đường huyết, phản ứng tại chỗ','Salicylat, rượu','B',200000.00,10,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(111,NULL,'Prednisolone','H02AB06',NULL,NULL,NULL,NULL,NULL,'Prednisolone','5','Pfizer','Ireland','Viên nén','mg','Uống','Glucocorticoid giảm viêm','Nơi khô mát dưới 25°C','KE_DON','Nhiễm trùng toàn thân không kiểm soát','Tăng đường huyết, xương giòn','NSAID','C',5000.00,40,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(112,NULL,'Atorvastatin','C10AA05',NULL,NULL,NULL,NULL,NULL,'Atorvastatin','20','Pfizer','Mỹ','Viên nén','mg','Uống','Statin hạ mỡ máu','Nơi khô mát dưới 25°C','KE_DON','Bệnh gan nặng, thai kỳ','Đau cơ, tăng men gan','CYP3A4 inhibitors','X',20000.00,30,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(113,NULL,'Simvastatin','C10AA01',NULL,NULL,NULL,NULL,NULL,'Simvastatin','10','Merck','Mỹ','Viên nén','mg','Uống','Statin hạ mỡ máu','Nơi khô mát dưới 25°C','KE_DON','Bệnh gan nặng, thai kỳ','Đau cơ, tăng men gan','Fibrate, Amiodarone','X',15000.00,30,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(114,NULL,'Furosemide','C03CA01',NULL,NULL,NULL,NULL,NULL,'Furosemide','40','Roche','Thụy Sĩ','Viên nén','mg','Uống','Lợi tiểu quai','Nơi khô mát dưới 30°C','KE_DON','Vô niệu','Hạ kali, hạ áp','NSAID','C',5000.00,40,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(115,NULL,'Metronidazol','J01XD01',NULL,NULL,NULL,NULL,NULL,'Metronidazol','400','Bayer','Đức','Viên nén','mg','Uống','Kháng khuẩn anaerobe, thuốc ký sinh','Nơi khô mát dưới 25°C','KE_DON','Rượu (tương tác nặng), thiếu máu tán huyết','Vị kim loại, đau đầu','Rượu, Warfarin','B',7000.00,30,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(116,NULL,'Doxycycline','J01AA02',NULL,NULL,NULL,NULL,NULL,'Doxycycline','100','Pfizer','Mỹ','Viên nang','mg','Uống','Tetracycline phổ rộng','Nơi khô mát dưới 30°C','KE_DON','Thai kỳ, trẻ nhỏ','Nhạy cảm ánh sáng, tiêu chảy','Retinoid, Ca/Sắt','D',7000.00,25,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(117,NULL,'Gentamicin','J01GB03',NULL,NULL,NULL,NULL,NULL,'Gentamicin','10','Sandoz','Hà Lan','Dung dịch tiêm','mg/ml','Tiêm bắp/tĩnh mạch','Kháng sinh aminoglycoside','Bảo quản 2–8°C','KE_DON','Suy thận nặng, nhược cơ','Độc thận, độc tai','Thuốc độc thận khác','D',25000.00,20,10,1,'2025-06-06 11:38:25','2025-06-06 11:39:42'),(118,NULL,'Loratadin','R06AX13',NULL,NULL,NULL,NULL,NULL,'Loratadin','10','Schering-Plough','Mỹ','Viên nén','mg','Uống','Kháng histamin H1 thế hệ 2','Nơi khô mát dưới 30°C','KE_DON','Dị ứng loratadine','Khô miệng, mệt mỏi','Rượu','B',20000.00,40,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(119,NULL,'Chloramphenicol','J01BA01',NULL,NULL,NULL,NULL,NULL,'Chloramphenicol','250','Roche','Thụy Sĩ','Viên nang','mg','Uống','Kháng sinh phổ rộng','Nơi khô mát dưới 25°C','KE_DON','Thiếu máu nặng, mang thai','Ức chế tủy xương, ban phát ban','Thuốc ức chế tủy khác','C',8000.00,15,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(120,NULL,'Atenolol','C07AB03',NULL,NULL,NULL,NULL,NULL,'Atenolol','50','AstraZeneca','Anh','Viên nén','mg','Uống','Chẹn β1 điều trị tăng HA','Nơi khô mát dưới 30°C','KE_DON','Block tim độ cao, hen nặng','Mệt mỏi, nhịp tim chậm','Thuốc HA khác','C',10000.00,30,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(121,NULL,'Amoxicillin','J01CA04',NULL,NULL,NULL,NULL,NULL,'Amoxicillin','500','GSK','Anh','Viên nang','mg','Uống','500 mg x 3 lần/ngày','Dưới 25°C','KE_DON','Dị ứng penicillin','Tiêu chảy, dị ứng','Methotrexate','B',12000.00,120,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(122,NULL,'Metformin','A10BA02',NULL,NULL,NULL,NULL,NULL,'Metformin','500','Merck','Mỹ','Viên nén','mg','Uống','500 mg x 2 lần/ngày','Dưới 25°C','KE_DON','Suy thận','Buồn nôn, tiêu chảy','Cản quang, rượu','B',15000.00,90,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(123,NULL,'Salbutamol','R03AC02',NULL,NULL,NULL,NULL,NULL,'Salbutamol','100','GSK','Anh','Bình xịt định liều','µg','Hít','1–2 hít khi cần','Dưới 30°C','KE_DON','Bệnh tim nặng','Run, hồi hộp','Chẹn beta','B',30000.00,50,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(124,NULL,'Prednisolone','H02AB06',NULL,NULL,NULL,NULL,NULL,'Prednisolone','5','Pfizer','Ireland','Viên nén','mg','Uống','5–10 mg/ngày','Dưới 25°C','KE_DON','Nhiễm trùng nặng','Tăng đường huyết, loãng xương','NSAID, vaccine sống','C',8000.00,70,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07'),(125,NULL,'Azithromycin','J01FA10',NULL,NULL,NULL,NULL,NULL,'Azithromycin','250','Pfizer','Mỹ','Viên nén','mg','Uống','500 mg ngày 1, sau đó 250 mg/ngày','Dưới 25°C','KE_DON','Dị ứng, kéo dài QT','Tiêu chảy, buồn nôn','Warfarin','B',20000.00,85,10,1,'2025-06-06 11:38:25','2025-06-06 11:43:07');

