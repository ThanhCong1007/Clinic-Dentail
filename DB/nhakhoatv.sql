CREATE DATABASE IF NOT EXISTS nhakhoa;
USE nhakhoa;

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

-- Bảng thuốc
CREATE TABLE thuoc (
    ma_thuoc INT AUTO_INCREMENT PRIMARY KEY,
    ma_loai_thuoc INT,
    ten_thuoc VARCHAR(100) NOT NULL,
    hoat_chat VARCHAR(255),
    ham_luong VARCHAR(100),
    don_vi_tinh VARCHAR(50) NOT NULL,
    duong_dung VARCHAR(100),
    huong_dan_su_dung TEXT,
    cach_bao_quan TEXT,
    gia DECIMAL(12, 2) NOT NULL,
    so_luong_ton INT NOT NULL DEFAULT 0,
    nguong_canh_bao INT DEFAULT 10,
    trang_thai_hoat_dong BOOLEAN DEFAULT TRUE,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ma_loai_thuoc) REFERENCES loai_thuoc(ma_loai_thuoc) ON DELETE SET NULL
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
    ngay_ke DATE NOT NULL,
    ghi_chu TEXT,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ma_benh_an) REFERENCES benh_an(ma_benh_an),
    FOREIGN KEY (ma_benh_nhan) REFERENCES benh_nhan(ma_benh_nhan),
    FOREIGN KEY (ma_bac_si) REFERENCES bac_si(ma_bac_si)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng chi tiết kê thuốc
CREATE TABLE chi_tiet_ke_thuoc (
    ma_chi_tiet INT AUTO_INCREMENT PRIMARY KEY,
    ma_ke_thuoc INT NOT NULL,
    ma_thuoc INT NOT NULL,
    lieu_dung VARCHAR(255) NOT NULL,
    so_luong INT NOT NULL,
    don_gia DECIMAL(12, 2) NOT NULL,
    thanh_tien DECIMAL(12, 2) NOT NULL,
    ghi_chu TEXT,
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