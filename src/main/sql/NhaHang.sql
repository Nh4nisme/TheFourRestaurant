-- ================================
-- Tạo CSDL
-- ================================
CREATE DATABASE NhaHangDB;
GO
USE NhaHangDB;
GO

-- ================================
-- Bảng VaiTro
-- ================================
CREATE TABLE VaiTro (
    maVT CHAR(8) PRIMARY KEY CHECK(maVT LIKE 'VT%' AND LEN(maVT) = 8),
    tenVaiTro NVARCHAR(20) NOT NULL UNIQUE,
    isDeleted BIT DEFAULT 0
);
GO

-- ================================
-- Bảng TaiKhoan
-- ================================
CREATE TABLE TaiKhoan (
    maTK CHAR(8) PRIMARY KEY CHECK (maTK LIKE 'TK%' AND LEN(maTK) = 8),
    tenDangNhap VARCHAR(50) NOT NULL UNIQUE CHECK(LEN(tenDangNhap) >= 6),
    matKhau VARCHAR(50) NOT NULL CHECK(LEN(matKhau) >= 6),
    maVT CHAR(8) NOT NULL,
    isDeleted BIT DEFAULT 0,
    CONSTRAINT FK_TaiKhoan_VaiTro FOREIGN KEY (maVT) REFERENCES VaiTro(maVT)
);
GO

-- ================================
-- Bảng CaLamViec
-- ================================
CREATE TABLE CaLamViec (
    maCa CHAR(8) PRIMARY KEY CHECK (maCa LIKE 'CA%' AND LEN(maCa) = 8),
    tenCa NVARCHAR(50) NOT NULL,
    gioBatDau TIME NOT NULL,
    gioKetThuc TIME NOT NULL
);
GO

-- ================================
-- Bảng NhanVien
-- ================================
CREATE TABLE NhanVien (
    maNV CHAR(8) PRIMARY KEY CHECK (maNV LIKE 'NV%' AND LEN(maNV) = 8),
    hoTen NVARCHAR(50) NOT NULL,
    ngaySinh DATETIME CHECK(ngaySinh < GETDATE()),
    gioiTinh VARCHAR(5) CHECK(gioiTinh IN ('Nam','Nu')),
    soDienThoai VARCHAR(15) UNIQUE,
    luong DECIMAL(12,2) CHECK(luong >= 0),
    maTK CHAR(8) NOT NULL UNIQUE,
    isDeleted BIT DEFAULT 0,
    CONSTRAINT FK_NhanVien_TaiKhoan FOREIGN KEY (maTK) REFERENCES TaiKhoan(maTK)
);
GO

-- ================================
-- Bảng PhanCongCa
-- ================================
CREATE TABLE PhanCongCa (
    maNV CHAR(8) NOT NULL,
    maCa CHAR(8) NOT NULL,
    ngay DATETIME NOT NULL CHECK (ngay >= '2000-01-01'),
    PRIMARY KEY(maNV, maCa, ngay),
    CONSTRAINT FK_PhanCongCa_NhanVien FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
    CONSTRAINT FK_PhanCongCa_CaLamViec FOREIGN KEY (maCa) REFERENCES CaLamViec(maCa)
);
GO

-- ================================
-- Bảng LoaiKhachHang
-- ================================
CREATE TABLE LoaiKhachHang (
    maLoaiKH CHAR(8) PRIMARY KEY CHECK (maLoaiKH LIKE 'LKH%' AND LEN(maLoaiKH) = 8),
    tenLoaiKH NVARCHAR(20) NOT NULL UNIQUE
);
GO

-- ================================
-- Bảng KhachHang
-- ================================
CREATE TABLE KhachHang (
    maKH CHAR(8) PRIMARY KEY CHECK (maKH LIKE 'KH%' AND LEN(maKH) = 8),
    hoTen NVARCHAR(50) NOT NULL,
    ngaySinh DATETIME CHECK(ngaySinh < GETDATE()),
    gioiTinh VARCHAR(5) CHECK(gioiTinh IN ('Nam','Nu')),
    soDT VARCHAR(15) UNIQUE,
    maLoaiKH CHAR(8) NOT NULL,
    isDeleted BIT DEFAULT 0,
    CONSTRAINT FK_KhachHang_LoaiKH FOREIGN KEY (maLoaiKH) REFERENCES LoaiKhachHang(maLoaiKH)
);
GO

-- ================================
-- Bảng Tang
-- ================================
CREATE TABLE Tang (
    maTang CHAR(8) PRIMARY KEY CHECK (maTang LIKE 'TG%' AND LEN(maTang) = 8),
    tenTang NVARCHAR(50) NOT NULL UNIQUE,
    moTa NVARCHAR(200) NULL,
    isDeleted BIT DEFAULT 0
);
GO

-- ================================
-- Bảng LoaiBan
-- ================================
CREATE TABLE LoaiBan (
    maLoaiBan CHAR(8) PRIMARY KEY CHECK (maLoaiBan LIKE 'LB%' AND LEN(maLoaiBan) = 8),
    tenLoaiBan NVARCHAR(50) NOT NULL UNIQUE,
    giaTien DECIMAL(10, 2) NOT NULL CHECK (giaTien >= 0)
);
GO

-- ================================
-- Bảng Ban
-- ================================
CREATE TABLE Ban (
    maBan CHAR(8) PRIMARY KEY CHECK (maBan LIKE 'BA%' AND LEN(maBan) = 8),
    tenBan NVARCHAR(50) NOT NULL UNIQUE,
    trangThai NVARCHAR(20) CHECK(trangThai IN (N'Trống', N'Đang sử dụng', N'Đặt trước', N'Bảo trì')) DEFAULT N'Trống',
    toaDoX INT CHECK(toaDoX >= 0),
    toaDoY INT CHECK(toaDoY >= 0),
    maTang CHAR(8) NOT NULL,
    maLoaiBan CHAR(8) NOT NULL,
    anhBan NVARCHAR(255),
    isDeleted BIT DEFAULT 0,
    CONSTRAINT FK_Ban_Tang FOREIGN KEY (maTang) REFERENCES Tang(maTang),
    CONSTRAINT FK_Ban_LoaiBan FOREIGN KEY (maLoaiBan) REFERENCES LoaiBan(maLoaiBan)
);
GO

-- ================================
-- Bảng LoaiMonAn
-- ================================
CREATE TABLE LoaiMonAn (
    maLoaiMon CHAR(8) PRIMARY KEY CHECK (maLoaiMon LIKE 'LM%' AND LEN(maLoaiMon) = 8),
    tenLoaiMon NVARCHAR(50) NOT NULL UNIQUE,
    hinhAnh NVARCHAR(255) NULL
);
GO

-- ================================
-- Bảng MonAn
-- ================================
CREATE TABLE MonAn (
    maMonAn CHAR(8) PRIMARY KEY CHECK (maMonAn LIKE 'MA%' AND LEN(maMonAn) = 8),
    tenMon NVARCHAR(50) NOT NULL,
    donGia DECIMAL(12,2) CHECK (donGia >= 0),
    trangThai NVARCHAR(10) CHECK (trangThai IN ('Con', 'Het')),
    maLoaiMon CHAR(8) NOT NULL,
    hinhAnh NVARCHAR(255) NULL,
    isDeleted BIT DEFAULT 0,
    CONSTRAINT FK_MonAn_LoaiMon FOREIGN KEY (maLoaiMon) REFERENCES LoaiMonAn(maLoaiMon)
);
GO

-- ================================
-- Bảng ThucDon
-- ================================
CREATE TABLE ThucDon (
    maTD CHAR(8) PRIMARY KEY CHECK(maTD LIKE 'TD%' AND LEN(maTD) = 8),
    tenTD NVARCHAR(100) NOT NULL UNIQUE
);

-- ================================
-- Bảng ChiTietThucDon
-- ================================
CREATE TABLE ChiTietThucDon (
    maMonAn CHAR(8) NOT NULL,
    maTD CHAR(8) NOT NULL,
    PRIMARY KEY(maMonAn, maTD),
    CONSTRAINT FK_MonAn_ThucDon_MonAn FOREIGN KEY (maMonAn) REFERENCES MonAn(maMonAn),
    CONSTRAINT FK_MonAn_ThucDon_ThucDon FOREIGN KEY (maTD) REFERENCES ThucDon(maTD)
);
GO

-- ================================
-- Migrate existing DB: widen ThucDon.tenTD to NVARCHAR(100)
-- Run-safe: only applies if table exists and current length < 100
-- ================================
IF OBJECT_ID('dbo.ThucDon','U') IS NOT NULL AND COL_LENGTH('dbo.ThucDon', 'tenTD') IS NOT NULL
BEGIN
    -- NVARCHAR(100) = 200 bytes
    IF COL_LENGTH('dbo.ThucDon', 'tenTD') < 200
    BEGIN
        ALTER TABLE dbo.ThucDon ALTER COLUMN tenTD NVARCHAR(100) NOT NULL;
    END
END
GO

-- ================================
-- Bảng PhieuDatBan
-- ================================
CREATE TABLE PhieuDatBan (
    maPDB CHAR(8) PRIMARY KEY CHECK (maPDB LIKE 'PD%' AND LEN(maPDB) = 8),
    ngayTao DATETIME DEFAULT GETDATE(),
    ngayDat DATETIME CHECK(ngayDat >= CAST(GETDATE() AS DATE)),
    soNguoi INT CHECK(soNguoi > 0),
    maKH CHAR(8) NOT NULL,
    maNV CHAR(8) NOT NULL,
    maBan CHAR(8) NOT NULL,
    trangThai NVARCHAR(50) DEFAULT N'Đang phục vụ' 
        CHECK (trangThai IN (N'Đang phục vụ', N'Đặt trước', N'Đã thanh toán', N'Đã hủy')),
    isDeleted BIT DEFAULT 0,
    CONSTRAINT FK_PDB_KhachHang FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),
    CONSTRAINT FK_PDB_NhanVien FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
    CONSTRAINT FK_PDB_Ban FOREIGN KEY (maBan) REFERENCES Ban(maBan)
);
GO

-- ================================
-- Bảng ChiTietPDB
-- ================================
CREATE TABLE ChiTietPDB (
    maCT CHAR(8) PRIMARY KEY CHECK(maCT LIKE 'CTP%' AND LEN(maCT) = 8),
    maPDB CHAR(8) NOT NULL,
    maMonAn CHAR(8) NULL,
    soLuong INT CHECK(soLuong > 0),
    donGia DECIMAL(12,2) CHECK(donGia >= 0),
    ghiChu NVARCHAR(255) NULL,
    CONSTRAINT FK_ChiTietPDB_PDB FOREIGN KEY (maPDB) REFERENCES PhieuDatBan(maPDB),
    CONSTRAINT FK_ChiTietPDB_MonAn FOREIGN KEY (maMonAn) REFERENCES MonAn(maMonAn)
);
GO

-- ================================
-- Bảng LoaiThue
-- ================================
CREATE TABLE LoaiThue (
    maLoaiThue CHAR(8) PRIMARY KEY CHECK (maLoaiThue LIKE 'LT%' AND LEN(maLoaiThue) = 8),
    tenLoaiThue NVARCHAR(50) NOT NULL UNIQUE
);
GO

-- ================================
-- Bảng Thue
-- ================================
CREATE TABLE Thue (
    maThue CHAR(8) PRIMARY KEY CHECK (maThue LIKE 'TH%' AND LEN(maThue) = 8),
    tyLe DECIMAL(5,2) CHECK(tyLe >= 0 AND tyLe <= 100),
    ghiChu NVARCHAR(200) NULL,
    maLoaiThue CHAR(8) NOT NULL,
    CONSTRAINT FK_Thue_LoaiThue FOREIGN KEY (maLoaiThue) REFERENCES LoaiThue(maLoaiThue)
);
GO

-- ================================
-- Bảng LoaiKhuyenMai
-- ================================
CREATE TABLE LoaiKhuyenMai (
    maLoaiKM CHAR(8) PRIMARY KEY CHECK (maLoaiKM LIKE 'LKM%' AND LEN(maLoaiKM) = 8),
    tenLoaiKM NVARCHAR(50) NOT NULL UNIQUE
);
GO

-- ================================
-- Bảng KhuyenMai
-- ================================
CREATE TABLE KhuyenMai (
    maKM CHAR(8) PRIMARY KEY CHECK (maKM LIKE 'KM%' AND LEN(maKM) = 8),
    maLoaiKM CHAR(8) NOT NULL,
    tenKM NVARCHAR(100) NOT NULL DEFAULT N'',
    tyLe DECIMAL(5,2) NULL CHECK(tyLe >= 0 AND tyLe <= 100),
    soTien DECIMAL(12,2) NULL CHECK(soTien >= 0),
    ngayBatDau DATETIME NULL,
    ngayKetThuc DATETIME NULL,
    moTa NVARCHAR(200) NULL,
    CONSTRAINT FK_KM_LoaiKM FOREIGN KEY (maLoaiKM) REFERENCES LoaiKhuyenMai(maLoaiKM),
    CHECK ((ngayBatDau IS NULL AND ngayKetThuc IS NULL) OR (ngayKetThuc >= ngayBatDau)),
    CHECK (
    (tyLe IS NOT NULL AND soTien IS NULL) OR
    (tyLe IS NULL AND soTien IS NOT NULL) OR
    (tyLe IS NULL AND soTien IS NULL)
    )
);
GO

-- ================================
-- Bảng ChiTietKhuyenMai
-- ================================
CREATE TABLE ChiTietKhuyenMai (
    maCTKM CHAR(8) PRIMARY KEY CHECK (maCTKM LIKE 'CTKM%' AND LEN(maCTKM) = 8),
    maKM CHAR(8) NOT NULL,
    maMonApDung CHAR(8) NOT NULL,     -- Món được áp dụng khuyến mãi
    maMonTang CHAR(8) NULL,           -- Món được tặng (nếu có)
    tyLeGiam DECIMAL(5,2) NULL CHECK(tyLeGiam >= 0 AND tyLeGiam <= 100),
    soTienGiam DECIMAL(12,2) NULL CHECK(soTienGiam >= 0),
    soLuongTang INT NULL CHECK(soLuongTang >= 0),
    CONSTRAINT FK_CTKM_KM FOREIGN KEY (maKM) REFERENCES KhuyenMai(maKM),
    CONSTRAINT FK_CTKM_MonApDung FOREIGN KEY (maMonApDung) REFERENCES MonAn(maMonAn),
    CONSTRAINT FK_CTKM_MonTang FOREIGN KEY (maMonTang) REFERENCES MonAn(maMonAn)
);
GO

-- ================================
-- Bảng KhungGio (dùng chung cho KM và Combo)
-- ================================
CREATE TABLE KhungGio (
    maTG CHAR(8) PRIMARY KEY CHECK(maTG LIKE 'TG%' AND LEN(maTG) = 8),
    gioBatDau TIME NOT NULL,
    gioKetThuc TIME NOT NULL,
    lapLaiHangNgay BIT DEFAULT 0,
    CHECK (gioKetThuc > gioBatDau)
);
GO

-- ================================
-- Bảng liên kết KhungGio <-> KhuyenMai
-- ================================
CREATE TABLE KhungGio_KM (
    maTG CHAR(8) NOT NULL,
    maKM CHAR(8) NOT NULL,
    PRIMARY KEY(maTG, maKM),
    CONSTRAINT FK_KGKM_KhungGio FOREIGN KEY (maTG) REFERENCES KhungGio(maTG),
    CONSTRAINT FK_KGKM_KhuyenMai FOREIGN KEY (maKM) REFERENCES KhuyenMai(maKM)
);
GO

-- ================================
-- Bảng PhuongThucThanhToan
-- ================================
CREATE TABLE PhuongThucThanhToan (
    maPTTT CHAR(8) PRIMARY KEY CHECK (maPTTT LIKE 'PT%' AND LEN(maPTTT) = 8),
    tenPTTT NVARCHAR(50) NOT NULL UNIQUE CHECK(tenPTTT IN (N'Tiền mặt', N'Chuyển khoản')),
    moTa NVARCHAR(200) NULL
);
GO

-- ================================
-- Bảng HoaDon
-- ================================
CREATE TABLE HoaDon (
    maHD CHAR(8) PRIMARY KEY CHECK (maHD LIKE 'HD%' AND LEN(maHD) = 8),
    ngayLap DATETIME NOT NULL,
    maNV CHAR(8) NOT NULL,
    maKH CHAR(8) NULL,
    maPDB CHAR(8) NULL,
    maKM CHAR(8) NULL,
    maThue CHAR(8) NULL,
    tienKhachDua DECIMAL(12,2) NOT NULL CHECK(tienKhachDua >= 0),
    tienThua DECIMAL(12,2) NOT NULL CHECK(tienThua >= 0),
    maPTTT CHAR(8) NOT NULL DEFAULT 'PT000001',
    isDeleted BIT DEFAULT 0,
    CONSTRAINT FK_HD_NhanVien FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
    CONSTRAINT FK_HD_KhachHang FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),
    CONSTRAINT FK_HD_PhieuDatBan FOREIGN KEY (maPDB) REFERENCES PhieuDatBan(maPDB),
    CONSTRAINT FK_HD_KhuyenMai FOREIGN KEY (maKM) REFERENCES KhuyenMai(maKM),
    CONSTRAINT FK_HD_Thue FOREIGN KEY (maThue) REFERENCES Thue(maThue),
    CONSTRAINT FK_HD_PhuongThucThanhToan FOREIGN KEY (maPTTT) REFERENCES PhuongThucThanhToan(maPTTT)
);
GO

-- ================================
-- Bảng ChiTietHD
-- ================================
CREATE TABLE ChiTietHD (
    maHD CHAR(8) NOT NULL,
    maMonAn CHAR(8) NOT NULL,
    soLuong INT CHECK(soLuong > 0),
    donGia DECIMAL(12,2) CHECK(donGia >= 0),
    PRIMARY KEY(maHD, maMonAn),
    CONSTRAINT FK_CTHD_HoaDon FOREIGN KEY (maHD) REFERENCES HoaDon(maHD),
    CONSTRAINT FK_CTHD_MonAn FOREIGN KEY (maMonAn) REFERENCES MonAn(maMonAn)
);
GO

-- ================================
-- DỮ LIỆU MẪU NHÀ HÀNG
-- ================================

-- Vai trò
INSERT INTO VaiTro (maVT, tenVaiTro) VALUES
('VT000001', N'QuanLy'),
('VT000002', N'ThuNgan');
GO

-- Tài khoản
INSERT INTO TaiKhoan (maTK, tenDangNhap, matKhau, maVT) VALUES
('TK000001', 'admin123', 'Admin@123', 'VT000001'),
('TK000002', 'thungan01', 'TNpass01', 'VT000002');
GO

-- Ca làm việc (3 ca)
INSERT INTO CaLamViec (maCa, tenCa, gioBatDau, gioKetThuc) VALUES
('CA000001', N'Ca Sáng', '07:00', '11:00'),
('CA000002', N'Ca Trưa', '11:00', '15:00'),
('CA000003', N'Ca Tối', '17:00', '22:00');
GO

-- Nhân viên
INSERT INTO NhanVien (maNV, hoTen, ngaySinh, gioiTinh, soDienThoai, luong, maTK) VALUES
('NV000001', N'Nguyễn Văn A', '1990-01-01', 'Nam', '0901234567', 10000000, 'TK000001'),
('NV000002', N'Trần Thị B', '1992-02-02', 'Nu', '0912345678', 8000000, 'TK000002');
GO

-- Phân công ca
INSERT INTO PhanCongCa (maNV, maCa, ngay) VALUES
('NV000001','CA000001','2025-10-14'),
('NV000001','CA000002','2025-10-14'),
('NV000002','CA000003','2025-10-14');
GO

-- Loại khách hàng (chỉ 2 loại)
INSERT INTO LoaiKhachHang (maLoaiKH, tenLoaiKH) VALUES
('LKH00001', N'Thuong'),
('LKH00002', N'VIP');

-- Khách hàng
INSERT INTO KhachHang (maKH, hoTen, ngaySinh, gioiTinh, soDT, maLoaiKH) VALUES
('KH000001', N'Nguyễn Văn F', '1990-06-06', 'Nam', '0956789012', 'LKH00001'),
('KH000002', N'Trần Thị G', '1991-07-07', 'Nu', '0967890123', 'LKH00002');
GO

-- Tầng
INSERT INTO Tang (maTang, tenTang, moTa) VALUES
('TG000001', N'Tầng 1', N'Khu thường'),
('TG000002', N'Tầng 2', N'Khu VIP'),
('TG000003', N'Tầng 3', N'Khu thường'),
('TG000004', N'Tầng 4', N'Khu VIP'),
('TG000005', N'Tầng 5', N'Khu thường'),
('TG000006', N'Tầng 6', N'Khu VIP'),
('TG000007', N'Tầng 7', N'Khu thường');
GO

-- Loại bàn
-- ==============================
INSERT INTO LoaiBan (maLoaiBan, tenLoaiBan, giaTien) VALUES
('LB000001', N'Bàn 8', 500000),
('LB000002', N'Bàn 6', 400000),
('LB000003', N'Bàn 4', 300000),
('LB000004', N'Bàn 2', 200000);
GO

-- ==============================
-- Bàn (đúng mã loại bàn & ảnh)
-- ==============================
INSERT INTO Ban (maBan, tenBan, trangThai, toaDoX, toaDoY, maTang, maLoaiBan, anhBan) VALUES
-- ===== Tầng 1 =====
('BA000001', N'Bàn 1-T1', N'Đang sử dụng', 8, 2, 'TG000001', 'LB000001', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),
('BA000002', N'Bàn 2-T1', N'Đang sử dụng', 100, 300, 'TG000001', 'LB000001', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),
('BA000003', N'Bàn 3-T1', N'Đặt trước', 100, 500, 'TG000001', 'LB000001', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),
('BA000004', N'Bàn 4-T1', N'Trống', 400, 100, 'TG000001', 'LB000002', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),
('BA000005', N'Bàn 5-T1', N'Đang sử dụng', 400, 300, 'TG000001', 'LB000002', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),
('BA000006', N'Bàn 6-T1', N'Trống', 400, 500, 'TG000001', 'LB000002', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),
('BA000007', N'Bàn 7-T1', N'Trống', 700, 150, 'TG000001', 'LB000003', N'/com/thefourrestaurant/images/Ban/Ban_4.png'),
('BA000008', N'Bàn 8-T1', N'Đang sử dụng', 700, 350, 'TG000001', 'LB000003', N'/com/thefourrestaurant/images/Ban/Ban_4.png'),

-- ===== Tầng 2 =====
('BA000009', N'Bàn 1-T2', N'Trống', 50, 100, 'TG000002', 'LB000003', N'/com/thefourrestaurant/images/Ban/Ban_4.png'),
('BA000010', N'Bàn 2-T2', N'Đang sử dụng', 250, 100, 'TG000002', 'LB000003', N'/com/thefourrestaurant/images/Ban/Ban_4.png'),
('BA000011', N'Bàn 3-T2', N'Trống', 650, 100, 'TG000002', 'LB000003', N'/com/thefourrestaurant/images/Ban/Ban_4.png'),
('BA000012', N'Bàn 4-T2', N'Đặt trước', 50, 300, 'TG000002', 'LB000002', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),
('BA000013', N'Bàn 5-T2', N'Trống', 250, 300, 'TG000002', 'LB000002', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),
('BA000014', N'Bàn 6-T2', N'Đang sử dụng', 650, 300, 'TG000002', 'LB000002', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),
('BA000015', N'Bàn 7-T2', N'Trống', 50, 450, 'TG000002', 'LB000004', N'/com/thefourrestaurant/images/Ban/Ban_2.png'),
('BA000016', N'Bàn 8-T2', N'Trống', 250, 450, 'TG000002', 'LB000004', N'/com/thefourrestaurant/images/Ban/Ban_2.png'),
('BA000017', N'Bàn 9-T2', N'Đang sử dụng', 450, 450, 'TG000002', 'LB000004', N'/com/thefourrestaurant/images/Ban/Ban_2.png'),
('BA000018', N'Bàn 10-T2', N'Trống', 650, 450, 'TG000002', 'LB000004', N'/com/thefourrestaurant/images/Ban/Ban_2.png'),
('BA000019', N'Bàn 11-T2', N'Trống', 850, 450, 'TG000002', 'LB000004', N'/com/thefourrestaurant/images/Ban/Ban_2.png'),

-- ===== Tầng 3 =====
('BA000020', N'Bàn 1-T3', N'Trống', 100, 100, 'TG000003', 'LB000002', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),
('BA000021', N'Bàn 2-T3', N'Đang sử dụng', 400, 100, 'TG000003', 'LB000002', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),
('BA000022', N'Bàn 3-T3', N'Trống', 700, 100, 'TG000003', 'LB000002', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),
('BA000023', N'Bàn 4-T3', N'Đặt trước', 300, 450, 'TG000003', 'LB000001', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),
('BA000024', N'Bàn 5-T3', N'Trống', 600, 450, 'TG000003', 'LB000001', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),
('BA000025', N'Bàn 6-T3', N'Đặt trước', 100, 450, 'TG000003', 'LB000002', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),
('BA000026', N'Bàn 7-T3', N'Đang sử dụng', 400, 450, 'TG000003', 'LB000002', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),
('BA000027', N'Bàn 8-T3', N'Trống', 700, 450, 'TG000003', 'LB000002', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),

-- ===== Tầng 4 =====
('BA000028', N'Bàn 1-T4', N'Trống', 200, 300, 'TG000004', 'LB000001', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),
('BA000029', N'Bàn 2-T4', N'Đang sử dụng', 650, 300, 'TG000004', 'LB000001', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),

-- ===== Tầng 5 =====
('BA000030', N'Bàn 1-T5', N'Trống', 50, 100, 'TG000005', 'LB000003', N'/com/thefourrestaurant/images/Ban/Ban_4.png'),
('BA000031', N'Bàn 2-T5', N'Đang sử dụng', 250, 100, 'TG000005', 'LB000003', N'/com/thefourrestaurant/images/Ban/Ban_4.png'),
('BA000032', N'Bàn 3-T5', N'Đặt trước', 650, 100, 'TG000005', 'LB000003', N'/com/thefourrestaurant/images/Ban/Ban_4.png'),
('BA000033', N'Bàn 4-T5', N'Trống', 50, 300, 'TG000005', 'LB000002', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),
('BA000034', N'Bàn 5-T5', N'Trống', 250, 300, 'TG000005', 'LB000002', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),
('BA000035', N'Bàn 6-T5', N'Đang sử dụng', 650, 300, 'TG000005', 'LB000002', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),
('BA000036', N'Bàn 7-T5', N'Trống', 50, 550, 'TG000005', 'LB000004', N'/com/thefourrestaurant/images/Ban/Ban_2.png'),
('BA000037', N'Bàn 8-T5', N'Đặt trước', 250, 550, 'TG000005', 'LB000004', N'/com/thefourrestaurant/images/Ban/Ban_2.png'),
('BA000038', N'Bàn 9-T5', N'Trống', 450, 550, 'TG000005', 'LB000004', N'/com/thefourrestaurant/images/Ban/Ban_2.png'),
('BA000039', N'Bàn 10-T5', N'Trống', 650, 550, 'TG000005', 'LB000004', N'/com/thefourrestaurant/images/Ban/Ban_2.png'),
('BA000040', N'Bàn 11-T5', N'Đang sử dụng', 850, 550, 'TG000005', 'LB000004', N'/com/thefourrestaurant/images/Ban/Ban_2.png'),

-- ===== Tầng 6 =====
('BA000047', N'Bàn 1-T6', N'Đặt trước', 140, 150, 'TG000006', 'LB000001', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),
('BA000048', N'Bàn 2-T6', N'Đang sử dụng', 600, 150, 'TG000006', 'LB000001', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),
('BA000049', N'Bàn 3-T6', N'Trống', 140, 400, 'TG000006', 'LB000001', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),
('BA000050', N'Bàn 4-T6', N'Trống', 600, 400, 'TG000006', 'LB000001', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),

-- ===== Tầng 7 =====
('BA000041', N'Bàn 1-T7', N'Trống', 250, 100, 'TG000007', 'LB000001', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),
('BA000042', N'Bàn 2-T7', N'Đặt trước', 450, 100, 'TG000007', 'LB000001', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),
('BA000043', N'Bàn 3-T7', N'Trống', 650, 100, 'TG000007', 'LB000001', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),
('BA000044', N'Bàn 4-T7', N'Đang sử dụng', 250, 350, 'TG000007', 'LB000001', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),
('BA000045', N'Bàn 5-T7', N'Trống', 450, 350, 'TG000007', 'LB000001', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),
('BA000046', N'Bàn 6-T7', N'Đặt trước', 650, 350, 'TG000007', 'LB000001', N'/com/thefourrestaurant/images/Ban/Ban_8.png');
GO

-- Loại món ăn (đơn giản, thực tế)
INSERT INTO LoaiMonAn (maLoaiMon, tenLoaiMon, hinhAnh) VALUES
('LM000001', N'Cơm', N'/com/thefourrestaurant/images/LoaiMonAn/com.png'),
('LM000002', N'Đồ nước', N'/com/thefourrestaurant/images/LoaiMonAn/do_nuoc.png'),
('LM000003', N'Tráng miệng', N'/com/thefourrestaurant/images/LoaiMonAn/trang_mieng.png'),
('LM000004', N'Món đặc biệt', N'/com/thefourrestaurant/images/LoaiMonAn/mon_dac_biet.png');
GO

-- Món ăn
INSERT INTO MonAn (maMonAn, tenMon, donGia, trangThai, maLoaiMon, hinhAnh) VALUES
('MA000001', N'Cơm tấm sườn bì', 55000, 'Con', 'LM000001', N'/com/thefourrestaurant/images/MonAn/com_tam_suon_bi.png'),
('MA000002', N'Cơm gà xối mỡ', 60000, 'Con', 'LM000001', N'/com/thefourrestaurant/images/MonAn/com_ga_xoi_mo.png'),
('MA000003', N'Nước cam ép', 25000, 'Con', 'LM000002', N'/com/thefourrestaurant/images/MonAn/nuoc_cam_ep.png'),
('MA000004', N'Sinh tố bơ', 30000, 'Con', 'LM000002', N'/com/thefourrestaurant/images/MonAn/sinh_to_bo.png'),
('MA000005', N'Bánh flan', 20000, 'Con', 'LM000003', N'/com/thefourrestaurant/images/MonAn/banh_flan.png'),
('MA000006', N'Lẩu thái hải sản', 250000, 'Con', 'LM000004', N'/com/thefourrestaurant/images/MonAn/lau_thai_hai_san.png');
GO

-- Loại ThucDon
INSERT INTO ThucDon(maTD, tenTD) VALUES 
('TD000001', N'Sáng'),
('TD000002', N'Trưa'),
('TD000003', N'Chiều'),
('TD000004', N'Tối');
GO

-- ChiTietThucDon
-- Món MA000001 xuất hiện trong Sáng và Trưa
INSERT INTO ChiTietThucDon(maMonAn, maTD) VALUES 
('MA000001', 'TD000001'), -- Sáng
('MA000001', 'TD000002'); -- Trưa

-- Món MA000002 chỉ xuất hiện trong Chiều
INSERT INTO ChiTietThucDon(maMonAn, maTD) VALUES 
('MA000002', 'TD000003');

-- Món MA000003 xuất hiện trong Trưa và Tối
INSERT INTO ChiTietThucDon(maMonAn, maTD) VALUES 
('MA000003', 'TD000002'), -- Trưa
('MA000003', 'TD000004'); -- Tối

GO

-- Phiếu đặt bàn
INSERT INTO PhieuDatBan (maPDB, ngayDat, soNguoi, maKH, maNV, maBan, ngayTao, trangThai) VALUES
('PD000001', '2025-11-20', 4, 'KH000001', 'NV000001', 'BA000001', '2025-10-23', N'Đặt trước'),
('PD000002', '2025-11-24', 2, 'KH000002', 'NV000002', 'BA000002', '2025-10-23', N'Đang phục vụ'),
('PD000003', '2025-11-26', 3, 'KH000001', 'NV000002', 'BA000003', '2025-10-22', N'Đã thanh toán'),
('PD000004', '2025-11-25', 5, 'KH000002', 'NV000001', 'BA000001', '2025-10-23', N'Đã hủy');
GO

-- Chi tiết phiếu đặt bàn
INSERT INTO ChiTietPDB (maCT, maPDB, maMonAn, soLuong, donGia, ghiChu) VALUES
('CTP00001', 'PD000001', 'MA000001', 2, 55000, N'Không hành'),
('CTP00002', 'PD000002', 'MA000003', 2, 25000, N'Ít cay'),
('CTP00003', 'PD000002', 'MA000002', 2, 60000, NULL);
GO

-- Loại thuế
INSERT INTO LoaiThue (maLoaiThue, tenLoaiThue) VALUES
('LT000001', N'VAT'),
('LT000002', N'Phí dịch vụ');
GO

-- Thuế
INSERT INTO Thue (maThue, tyLe, ghiChu, maLoaiThue) VALUES
('TH000001',10,NULL,'LT000001'),
('TH000002',5,N'Phí phục vụ','LT000002');
GO

-- Loại khuyến mãi
INSERT INTO LoaiKhuyenMai (maLoaiKM, tenLoaiKM) VALUES
('LKM00001', N'Giảm giá theo tỷ lệ'),
('LKM00002', N'Tặng món'),
('LKM00003', N'Giảm giá theo số tiền');
GO

-- Khuyến mãi
INSERT INTO KhuyenMai (maKM, maLoaiKM, tenKM, tyLe, soTien, ngayBatDau, ngayKetThuc, moTa) VALUES
('KM000001', 'LKM00001', N'Giảm 10% hóa đơn', 10, NULL, '2025-10-01', '2025-10-31', N'Áp dụng cho tất cả đơn hàng trong tháng 10'),
('KM000002', 'LKM00002', N'Mua cà phê tặng nước cam', NULL, NULL, '2025-10-10', '2025-10-31', N'Chương trình khuyến mãi đặc biệt cho khách hàng thân thiết'),
('KM000003', 'LKM00003', N'Giảm 15.000đ cho bún bò Huế', NULL, 15000, '2025-11-01', '2025-11-30', N'Áp dụng cho món bún bò Huế tại tất cả chi nhánh');
GO

-- Chi tiết khuyến mãi
INSERT INTO ChiTietKhuyenMai (maCTKM, maKM, maMonApDung, maMonTang, tyLeGiam, soTienGiam, soLuongTang)  VALUES
('CTKM0001', 'KM000001', 'MA000001', NULL, 10, NULL, NULL),         -- Giảm 10% cho món MA000001
('CTKM0002', 'KM000002', 'MA000002', 'MA000003', NULL, NULL, 1),    -- Mua cà phê đá (MA000002) tặng nước cam ép (MA000003)
('CTKM0003', 'KM000003', 'MA000004', NULL, NULL, 15000, NULL);      -- Giảm 15k cho món bún bò Huế (MA000004)
GO

-- Khung giờ chung
INSERT INTO KhungGio (maTG, gioBatDau, gioKetThuc, lapLaiHangNgay) VALUES
('TG000001', '08:00', '22:00', 1),
('TG000002', '09:00', '21:00', 1),
('TG000003', '10:00', '14:00', 0);
GO

-- Khung giờ áp dụng cho KM
INSERT INTO KhungGio_KM (maTG, maKM) VALUES
('TG000001', 'KM000001'),  -- KM000001 áp dụng khung 08:00-22:00
('TG000002', 'KM000002'),  -- KM000002 áp dụng khung 09:00-21:00
('TG000003', 'KM000003');  -- KM000003 áp dụng khung 10:00-14:00
GO

-- Phương thức thanh toán
INSERT INTO PhuongThucThanhToan (maPTTT, tenPTTT, moTa)
VALUES 
('PT000001', N'Tiền mặt', N'Thanh toán trực tiếp bằng tiền mặt'),
('PT000002', N'Chuyển khoản', N'Thanh toán qua tài khoản ngân hàng');

-- Hóa đơn
INSERT INTO HoaDon (maHD, ngayLap, maNV, maKH, maPDB, maKM, maThue, tienKhachDua, tienThua, maPTTT) VALUES
('HD000001','2025-10-20','NV000001','KH000001','PD000001','KM000001','TH000001',200000,10000,'PT000001'),
('HD000002','2025-10-21','NV000002','KH000002','PD000002','KM000002','TH000002',180000,5000,'PT000002');
GO

-- Chi tiết hóa đơn
INSERT INTO ChiTietHD (maHD, maMonAn, soLuong, donGia) VALUES
('HD000001','MA000001',2,55000),
('HD000001','MA000003',2,25000),
('HD000002','MA000002',2,60000),
('HD000002','MA000004',1,30000);
GO