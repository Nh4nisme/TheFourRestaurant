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
                          tenDangNhap VARCHAR(255) NOT NULL UNIQUE CHECK(LEN(tenDangNhap) >= 6),
                          matKhau VARCHAR(255) NOT NULL CHECK(LEN(matKhau) >= 6),
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
                          hinhAnh NVARCHAR(255) NULL,
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
                         tenLoaiBan NVARCHAR(50) NOT NULL,
                         giaTien DECIMAL(10, 2) NOT NULL CHECK (giaTien >= 0),
                         soChoNgoi INT NOT NULL CHECK(soChoNgoi > 0),
                         moTa NVARCHAR(200) NULL
);
GO

-- ================================
-- Bảng Ban
-- ================================
CREATE TABLE Ban (
                     maBan CHAR(8) PRIMARY KEY CHECK (maBan LIKE 'BA%' AND LEN(maBan) = 8),
                     tenBan NVARCHAR(50) NOT NULL UNIQUE,
                     trangThai NVARCHAR(20) CHECK(trangThai IN (N'Trống', N'Đang phục vụ', N'Bảo trì')) DEFAULT N'Trống',
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
-- Bảng LoaiKhuyenMai (DI CHUYỂN LÊN TRƯỚC)
-- ================================
CREATE TABLE LoaiKhuyenMai (
                               maLoaiKM CHAR(8) PRIMARY KEY CHECK (maLoaiKM LIKE 'LKM%' AND LEN(maLoaiKM) = 8),
                               tenLoaiKM NVARCHAR(50) NOT NULL UNIQUE
);
GO

-- ================================
-- Bảng KhuyenMai (DI CHUYỂN LÊN TRƯỚC)
-- ================================
CREATE TABLE KhuyenMai (
                           maKM CHAR(8) PRIMARY KEY CHECK (maKM LIKE 'KM%' AND LEN(maKM) = 8),
                           maLoaiKM CHAR(8) NOT NULL,
                           tenKM NVARCHAR(100) NOT NULL DEFAULT N'',
                           kieuKM NVARCHAR(20) NOT NULL DEFAULT N'SuKien' CHECK(kieuKM IN (N'SuKien', N'MaGiamGia')),
                           maCode VARCHAR(50) NULL,
                           soLuotSuDung INT NULL CHECK(soLuotSuDung IS NULL OR soLuotSuDung >= 0),
                           ngayBatDau DATETIME NULL,
                           ngayKetThuc DATETIME NULL,
                           moTa NVARCHAR(200) NULL,
                           isDeleted BIT NOT NULL DEFAULT 0,
                           CONSTRAINT FK_KM_LoaiKM FOREIGN KEY (maLoaiKM) REFERENCES LoaiKhuyenMai(maLoaiKM),
                           CHECK ((ngayBatDau IS NULL AND ngayKetThuc IS NULL) OR (ngayKetThuc >= ngayBatDau)),
                           CHECK (
                               (kieuKM = N'SuKien') OR
                               (kieuKM = N'MaGiamGia' AND maCode IS NOT NULL AND LEN(maCode) >= 3)
                               )
);
GO

-- ================================
-- Bảng MonAn (BÂY GIỜ CÓ THỂ THAM CHIẾU KhuyenMai)
-- ================================
CREATE TABLE MonAn (
                       maMonAn CHAR(8) PRIMARY KEY CHECK (maMonAn LIKE 'MA%' AND LEN(maMonAn) = 8),
                       tenMon NVARCHAR(50) NOT NULL,
                       donGia DECIMAL(12,2) CHECK (donGia >= 0),
                       trangThai NVARCHAR(10) CHECK (trangThai IN (N'Còn', N'Hết')),
                       maLoaiMon CHAR(8) NOT NULL,
                       hinhAnh NVARCHAR(255) NULL,
                       soLuong INT DEFAULT 0 NOT NULL,
                       daBan INT DEFAULT 0 NOT NULL,
                       isDeleted BIT DEFAULT 0,
                       isVisible BIT NOT NULL DEFAULT 1,
                       CONSTRAINT FK_MonAn_LoaiMon FOREIGN KEY (maLoaiMon) REFERENCES LoaiMonAn(maLoaiMon)
);
GO

-- ================================
-- Bảng ThucDon
-- ================================
CREATE TABLE ThucDon (
                         maTD CHAR(8) PRIMARY KEY CHECK(maTD LIKE 'TD%' AND LEN(maTD) = 8),
                         tenTD NVARCHAR(50) NOT NULL UNIQUE
);
GO

-- ================================
-- Bảng ChiTietThucDon
-- ================================
CREATE TABLE ChiTietThucDon (
                                maLoaiMon CHAR(8) NOT NULL,
                                maTD CHAR(8) NOT NULL,
                                PRIMARY KEY (maLoaiMon, maTD),
                                CONSTRAINT FK_ChiTietThucDon_LoaiMonAn FOREIGN KEY (maLoaiMon) REFERENCES LoaiMonAn(maLoaiMon),
                                CONSTRAINT FK_ChiTietThucDon_ThucDon FOREIGN KEY (maTD) REFERENCES ThucDon(maTD)
);
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
                             trangThai NVARCHAR(50) DEFAULT N'Đang phục vụ' CHECK (trangThai IN (N'Đang phục vụ', N'Đặt trước', N'Đã thanh toán', N'Đã hủy', N'Khách không đến')),
                             tienCoc DECIMAL(18, 2) NOT NULL CHECK (tienCoc >= 0),
                             isDeleted BIT DEFAULT 0,
                             CONSTRAINT FK_PDB_KhachHang FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),
                             CONSTRAINT FK_PDB_NhanVien FOREIGN KEY (maNV) REFERENCES NhanVien(maNV)
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
-- Bảng PhieuDatBan_Ban
-- ================================
CREATE TABLE PhieuDatBan_Ban (
                                 maPDB CHAR(8) NOT NULL,
                                 maBan CHAR(8) NOT NULL,
                                 isBanChinh BIT NOT NULL DEFAULT 0,
                                 PRIMARY KEY (maPDB, maBan),
                                 CONSTRAINT FK_PDBB_PDB FOREIGN KEY (maPDB) REFERENCES PhieuDatBan(maPDB) ON DELETE CASCADE ON UPDATE CASCADE,
                                 CONSTRAINT FK_PDBB_Ban FOREIGN KEY (maBan) REFERENCES Ban(maBan) ON DELETE CASCADE ON UPDATE CASCADE
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

-- CẤU TRÚC KHUYẾN MÃI MỚI
-- 1. Bảng KhuyenMai_DieuKien (Bảng Điều Kiện)
CREATE TABLE KhuyenMai_DieuKien (
    maDieuKien CHAR(8) PRIMARY KEY CHECK (maDieuKien LIKE 'DK%' AND LEN(maDieuKien) = 8),
    maKM CHAR(8) NOT NULL,
    loaiApDung VARCHAR(20) NOT NULL CHECK (loaiApDung IN ('GIAM_TRUC_TIEP', 'THEO_COMBO', 'MUA_X_GIAM_Y')),
    tyLeGiam DECIMAL(5,2) NULL CHECK(tyLeGiam >= 0 AND tyLeGiam <= 100),
    soTienGiam DECIMAL(12,2) NULL CHECK(soTienGiam >= 0),
    soLuongTang INT NULL CHECK(soLuongTang >= 0),
    moTaDieuKien NVARCHAR(255) NULL,
    giaToiThieu DECIMAL(18,2) NULL,
    CONSTRAINT FK_DieuKien_KhuyenMai FOREIGN KEY (maKM) REFERENCES KhuyenMai(maKM) ON DELETE CASCADE
);
GO

-- 2. Bảng DieuKien_Mon (Chi tiết các món trong điều kiện)
CREATE TABLE DieuKien_Mon (
    maDieuKien CHAR(8) NOT NULL,
    maMonAn CHAR(8) NOT NULL,
    soLuong INT NOT NULL DEFAULT 1 CHECK(soLuong > 0),
    vaiTro VARCHAR(20) NOT NULL CHECK (vaiTro IN ('GIAM_TRUC_TIEP', 'MUA', 'NHAN_GIAM')),
    PRIMARY KEY (maDieuKien, maMonAn, vaiTro),
    CONSTRAINT FK_DKMon_DieuKien FOREIGN KEY (maDieuKien) REFERENCES KhuyenMai_DieuKien(maDieuKien) ON DELETE CASCADE,
    CONSTRAINT FK_DKMon_MonAn FOREIGN KEY (maMonAn) REFERENCES MonAn(maMonAn) ON DELETE CASCADE
);
GO

-- 3. Bảng DieuKien_MonTang (Chi tiết các món được tặng)
CREATE TABLE DieuKien_MonTang (
    maDieuKien CHAR(8) NOT NULL,
    maMonAnTang CHAR(8) NOT NULL,
    PRIMARY KEY (maDieuKien, maMonAnTang),
    CONSTRAINT FK_DKMonTang_DieuKien FOREIGN KEY (maDieuKien) REFERENCES KhuyenMai_DieuKien(maDieuKien) ON DELETE CASCADE,
    CONSTRAINT FK_DKMonTang_MonAn FOREIGN KEY (maMonAnTang) REFERENCES MonAn(maMonAn) ON DELETE CASCADE
);
GO


-- ================================
-- Bảng KhungGio
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
-- Bảng KhungGio_KM
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
-- OG: {"TK000001", "admin123", "Admin@123"},
--     {"TK000002", "thungan01", "TNpass01"},
INSERT INTO TaiKhoan (maTK, tenDangNhap, matKhau, maVT) 
VALUES ('TK000001', 'admin123', 'SP2yaGkTfGSyoRplFIbSzA==$c2Obw6pzdXZb/npic3zToDOg5W9AJgWoAbfe2ABs3ig=', 'VT000001');
GO
INSERT INTO TaiKhoan (maTK, tenDangNhap, matKhau, maVT) 
VALUES ('TK000002', 'thungan01', 'YYrA1jPAfL/xZgeZ9Jehgg==$66e6xhhTF7uvqaUdF9pIWaNAD+EXdcL1nNOK7w92PLw=', 'VT000002');
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
                                                                            ('KH000002', N'Trần Thị G', '1991-07-07', 'Nu', '0967890123', 'LKH00002'),
                                                                            ('KH000003', N'Cao Quốc Trung', '2005-06-08', 'Nu', '0932931634', 'LKH00002');
GO

INSERT INTO KhachHang (maKH, hoTen, ngaySinh, gioiTinh, soDT, maLoaiKH)
VALUES
    ('KH000008', N'Trần Minh Khôi', '1995-03-12', 'Nam', '0973845126', 'LKH00001'),
    ('KH000009', N'Lê Thị Thanh Tuyền', '1997-08-21', 'Nu', '0329184753', 'LKH00001'),
    ('KH000010', N'Phạm Quốc Bảo', '1993-11-05', 'Nam', '0906721845', 'LKH00001'),
    ('KH000011', N'Võ Ngọc Diễm', '1998-02-14', 'Nu', '0817394628', 'LKH00001'),
    ('KH000012', N'Hoàng Anh Tuấn', '1990-06-30', 'Nam', '0862459713', 'LKH00001'),
    ('KH000013', N'Bùi Thị Kim Ngân', '1996-09-19', 'Nu', '0836914275', 'LKH00001'),
    ('KH000014', N'Đặng Thanh Phong', '1992-01-27', 'Nam', '0705842931', 'LKH00001'),
    ('KH000015', N'Ngô Mỹ Linh', '1999-04-03', 'Nu', '0389165724', 'LKH00001'),
    ('KH000016', N'Phan Hữu Nghĩa', '1989-12-10', 'Nam', '0914372856', 'LKH00001'),
    ('KH000017', N'Đỗ Thị Bích Trâm', '1994-07-16', 'Nu', '0765928417', 'LKH00001'),

    ('KH000018', N'Huỳnh Tấn Phát', '1991-05-22', 'Nam', '0984172635', 'LKH00001'),
    ('KH000019', N'Đinh Thảo My', '2000-10-08', 'Nu', '0338649152', 'LKH00001'),
    ('KH000020', N'Cao Văn Lộc', '1988-09-01', 'Nam', '0852716493', 'LKH00001'),
    ('KH000021', N'Trịnh Ngọc Hân', '1997-12-25', 'Nu', '0374982165', 'LKH00001'),
    ('KH000022', N'Vũ Đức Thành', '1993-04-18', 'Nam', '0936158427', 'LKH00001'),
    ('KH000023', N'Phùng Thị Mai Anh', '1996-06-09', 'Nu', '0827493618', 'LKH00001'),
    ('KH000024', N'Lâm Hoàng Long', '1992-11-30', 'Nam', '0348612579', 'LKH00001'),
    ('KH000025', N'Tạ Thanh Thúy', '1998-01-05', 'Nu', '0795281643', 'LKH00001'),
    ('KH000026', N'Quách Gia Huy', '2001-03-14', 'Nam', '0961735842', 'LKH00001'),
    ('KH000027', N'Hồ Thị Như Ý', '1999-07-27', 'Nu', '0883649751', 'LKH00001'),

    ('KH000028', N'La Văn Khánh', '1990-02-11', 'Nam', '0359146278', 'LKH00001'),
    ('KH000029', N'Kiều Thị Tuyết Mai', '1995-08-06', 'Nu', '0842769513', 'LKH00001'),
    ('KH000030', N'Tống Minh Quân', '1994-10-19', 'Nam', '0908143672', 'LKH00001'),
    ('KH000031', N'Chu Thị Ánh Nguyệt', '1997-05-01', 'Nu', '0326759184', 'LKH00001'),
    ('KH000032', N'Mai Quốc Việt', '1987-12-09', 'Nam', '0972468135', 'LKH00001'),
    ('KH000033', N'Tô Khánh Linh', '1998-03-23', 'Nu', '0768135492', 'LKH00001'),
    ('KH000034', N'Đoàn Hữu Tín', '1991-09-14', 'Nam', '0835926471', 'LKH00001'),
    ('KH000035', N'Phạm Thị Yến Nhi', '2000-01-30', 'Nu', '0391752864', 'LKH00001'),
    ('KH000036', N'Hà Minh Trí', '1996-06-17', 'Nam', '0918634275', 'LKH00001'),
    ('KH000037', N'Lý Thị Hồng Phúc', '1993-11-02', 'Nu', '0854927163', 'LKH00001'),

    ('KH000038', N'Trần Quốc Huy', '1992-04-08', 'Nam', '0983176452', 'LKH00001'),
    ('KH000039', N'Nguyễn Thị Thu Hà', '1995-06-12', 'Nu', '0337416285', 'LKH00001'),
    ('KH000040', N'Lê Văn Dũng', '1989-09-17', 'Nam', '0708264951', 'LKH00001'),
    ('KH000041', N'Phạm Ngọc Trinh', '1998-02-20', 'Nu', '0829137465', 'LKH00001'),
    ('KH000042', N'Hoàng Minh Tân', '1991-11-01', 'Nam', '0965842173', 'LKH00001'),
    ('KH000043', N'Vũ Thị Thanh Loan', '1996-07-29', 'Nu', '0347182659', 'LKH00001'),
    ('KH000044', N'Bùi Đức Mạnh', '1994-03-15', 'Nam', '0903649281', 'LKH00001'),
    ('KH000045', N'Ngô Thị Hương Giang', '1997-10-09', 'Nu', '0798156423', 'LKH00001'),
    ('KH000046', N'Phan Văn Khoa', '1988-01-25', 'Nam', '0975283641', 'LKH00001'),
    ('KH000047', N'Đỗ Thị Ngọc Huyền', '2000-12-18', 'Nu', '0837462195', 'LKH00001'),

    ('KH000048', N'Huỳnh Văn Toàn', '1990-08-02', 'Nam', '0916427835', 'LKH00001'),
    ('KH000049', N'Đinh Thị Bảo Trân', '1999-05-11', 'Nu', '0358174269', 'LKH00001'),
    ('KH000050', N'Cao Quốc Cường', '1993-02-27', 'Nam', '0863941728', 'LKH00001'),
    ('KH000051', N'Trịnh Thị Minh Châu', '1996-06-14', 'Nu', '0765218493', 'LKH00001'),
    ('KH000052', N'Vũ Hồng Phúc', '1992-09-03', 'Nam', '0987641532', 'LKH00001'),
    ('KH000053', N'Phùng Thị Diễm Quỳnh', '1998-04-22', 'Nu', '0339251847', 'LKH00001'),
    ('KH000054', N'Lâm Quốc Thắng', '1987-10-30', 'Nam', '0905183746', 'LKH00001'),
    ('KH000055', N'Tạ Mỹ Duyên', '2001-01-07', 'Nu', '0824679153', 'LKH00001'),
    ('KH000056', N'Quách Minh Hoàng', '1995-12-19', 'Nam', '0962831745', 'LKH00001'),
    ('KH000057', N'Hồ Thị Kim Oanh', '1994-07-06', 'Nu', '0395647281', 'LKH00001'),

    ('KH000058', N'La Đức Thiện', '1991-03-09', 'Nam', '0859371642', 'LKH00001'),
    ('KH000059', N'Kiều Ngọc Trâm', '1997-08-28', 'Nu', '0708149263', 'LKH00001'),
    ('KH000060', N'Tống Anh Duy', '1990-05-04', 'Nam', '0916738245', 'LKH00001'),
    ('KH000061', N'Chu Thị Thanh Nga', '1996-11-16', 'Nu', '0349621758', 'LKH00001'),
    ('KH000062', N'Mai Hoàng Nam', '1989-02-24', 'Nam', '0974186352', 'LKH00001'),
    ('KH000063', N'Tô Mỹ Hạnh', '1998-09-13', 'Nu', '0835271946', 'LKH00001'),
    ('KH000064', N'Đoàn Văn Phúc', '1993-01-31', 'Nam', '0869142573', 'LKH00001'),
    ('KH000065', N'Phạm Khánh Vy', '2000-06-20', 'Nu', '0356824197', 'LKH00001'),
    ('KH000066', N'Hà Quốc Khánh', '1995-04-07', 'Nam', '0983257461', 'LKH00001'),
    ('KH000067', N'Lý Thị Thu Uyên', '1997-12-02', 'Nu', '0764159283', 'LKH00001'),

    ('KH000068', N'Trần Đức Long', '1992-08-15', 'Nam', '0907641852', 'LKH00001'),
    ('KH000069', N'Lê Ngọc Ánh', '1999-01-10', 'Nu', '0825931746', 'LKH00001'),
    ('KH000070', N'Phạm Văn Hùng', '1988-06-26', 'Nam', '0971624853', 'LKH00001'),
    ('KH000071', N'Võ Thị Thanh Tâm', '1996-03-18', 'Nu', '0338714692', 'LKH00001'),
    ('KH000072', N'Hoàng Quốc Đạt', '1991-10-05', 'Nam', '0852467319', 'LKH00001'),
    ('KH000073', N'Bùi Thị Ngọc Mai', '1998-07-21', 'Nu', '0709351846', 'LKH00001'),
    ('KH000074', N'Đặng Minh Tùng', '1994-02-13', 'Nam', '0918273645', 'LKH00001'),
    ('KH000075', N'Ngô Thị Thanh Nhàn', '2000-11-09', 'Nu', '0394682517', 'LKH00001'),
    ('KH000076', N'Phan Văn Thành', '1987-04-29', 'Nam', '0967341852', 'LKH00001'),
    ('KH000077', N'Đỗ Thị Kim Chi', '1995-09-16', 'Nu', '0839157246', 'LKH00001'),

    ('KH000078', N'Huỳnh Quốc Trung', '1993-12-04', 'Nam', '0902574186', 'LKH00001'),
    ('KH000079', N'Đinh Thị Thanh Vân', '1997-05-19', 'Nu', '0357149628', 'LKH00001'),
    ('KH000080', N'Cao Minh Hiếu', '1990-01-22', 'Nam', '0986412753', 'LKH00001'),
    ('KH000081', N'Trịnh Thị Bảo Ngọc', '1999-08-30', 'Nu', '0762854917', 'LKH00001'),
    ('KH000082', N'Vũ Văn Khánh', '1988-10-11', 'Nam', '0975148629', 'LKH00001'),
    ('KH000083', N'Phùng Thị Thanh Thảo', '1996-04-26', 'Nu', '0829361754', 'LKH00001'),
    ('KH000084', N'Lâm Minh Quang', '1992-06-07', 'Nam', '0906832417', 'LKH00001'),
    ('KH000085', N'Tạ Thị Ngọc Lan', '2001-02-17', 'Nu', '0335178496', 'LKH00001'),
    ('KH000086', N'Quách Văn Sơn', '1994-09-23', 'Nam', '0857192643', 'LKH00001'),
    ('KH000087', N'Hồ Thị Minh Thư', '1998-12-08', 'Nu', '0704629815', 'LKH00001'),

    ('KH000088', N'La Quốc Thịnh', '1991-05-28', 'Nam', '0915742863', 'LKH00001'),
    ('KH000089', N'Kiều Thị Hồng Nhung', '1997-07-03', 'Nu', '0396281754', 'LKH00001'),
    ('KH000090', N'Tống Văn Phú', '1989-03-14', 'Nam', '0982153746', 'LKH00001'),
    ('KH000091', N'Chu Ngọc Bích', '1999-10-21', 'Nu', '0827416935', 'LKH00001'),
    ('KH000092', N'Mai Đức Huy', '1993-08-09', 'Nam', '0978362415', 'LKH00001'),
    ('KH000093', N'Tô Thị Thanh Hương', '1996-01-18', 'Nu', '0359241687', 'LKH00001'),
    ('KH000094', N'Đoàn Quốc Vinh', '1990-11-26', 'Nam', '0904158726', 'LKH00001'),
    ('KH000095', N'Phạm Mỹ Dung', '1998-06-04', 'Nu', '0832674195', 'LKH00001'),
    ('KH000096', N'Hà Văn Lâm', '1987-02-15', 'Nam', '0965281734', 'LKH00001'),
    ('KH000097', N'Lý Thị Thanh Trúc', '1995-12-29', 'Nu', '0769142586', 'LKH00001'),

    ('KH000098', N'Trần Quốc Đăng', '1992-09-06', 'Nam', '0907364185', 'LKH00001'),
    ('KH000099', N'Lê Thị Ánh Dương', '1999-04-11', 'Nu', '0825193746', 'LKH00001'),
    ('KH000100', N'Phạm Minh Đức', '1991-01-24', 'Nam', '0972846153', 'LKH00001'),
    ('KH000101', N'Võ Ngọc Thảo', '1997-08-17', 'Nu', '0336982417', 'LKH00001'),
    ('KH000102', N'Hoàng Văn Cường', '1988-05-02', 'Nam', '0853476192', 'LKH00001'),
    ('KH000103', N'Bùi Thị Thu Trang', '1996-10-20', 'Nu', '0709248156', 'LKH00001'),
    ('KH000104', N'Đặng Quốc Hưng', '1993-03-08', 'Nam', '0914682573', 'LKH00001'),
    ('KH000105', N'Ngô Thị Mai Phương', '2000-07-15', 'Nu', '0397158624', 'LKH00001'),
    ('KH000106', N'Phan Minh Nhật', '1994-12-01', 'Nam', '0963817452', 'LKH00001'),
    ('KH000107', N'Đỗ Thị Thanh Nhã', '1998-02-26', 'Nu', '0835469187', 'LKH00001');
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
INSERT INTO LoaiBan (maLoaiBan, tenLoaiBan, giaTien, soChoNgoi, moTa)
VALUES
('LB000001', N'Bàn thường 2 ghế', 0, 2, N'Bàn tiêu chuẩn 2 ghế'),
('LB000002', N'Bàn thường 4 ghế', 0, 4, N'Bàn tiêu chuẩn 4 ghế'),
('LB000003', N'Bàn thường 6 ghế', 0, 6, N'Bàn tiêu chuẩn 6 ghế'),
('LB000004', N'Bàn thường 8 ghế', 0, 8, N'Bàn tiêu chuẩn 8 ghế'),
('LB000005', N'Bàn VIP 4 ghế', 200000, 4, N'Bàn khu VIP'),
('LB000006', N'Bàn VIP 6 ghế', 200000, 6, N'Bàn khu VIP'),
('LB000007', N'Bàn VIP 8 ghế', 200000, 8, N'Bàn khu VIP');
GO

-- ==============================
-- Bàn (7 tầng, giữ nguyên số lượng nhưng cập nhật trạng thái)
-- ==============================
INSERT INTO Ban (maBan, tenBan, trangThai, toaDoX, toaDoY, maTang, maLoaiBan, anhBan) VALUES
-- ===== Tầng 1 =====
('BA000001', N'Bàn 1-T1', N'Trống', 100, 100, 'TG000001', 'LB000007', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),
('BA000002', N'Bàn 2-T1', N'Trống', 100, 300, 'TG000001', 'LB000004', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),
('BA000003', N'Bàn 3-T1', N'Trống', 100, 500, 'TG000001', 'LB000007', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),

('BA000004', N'Bàn 4-T1', N'Trống', 400, 100, 'TG000001', 'LB000006', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),
('BA000005', N'Bàn 5-T1', N'Trống', 400, 300, 'TG000001', 'LB000006', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),
('BA000006', N'Bàn 6-T1', N'Trống', 400, 500, 'TG000001', 'LB000003', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),

('BA000007', N'Bàn 7-T1', N'Trống', 700, 150, 'TG000001', 'LB000002', N'/com/thefourrestaurant/images/Ban/Ban_4.png'),
('BA000008', N'Bàn 8-T1', N'Trống', 700, 350, 'TG000001', 'LB000002', N'/com/thefourrestaurant/images/Ban/Ban_4.png'),

-- ===== Tầng 2 =====
('BA000009',  N'Bàn 1-T2',  N'Trống', 50, 100,  'TG000002', 'LB000002', N'/com/thefourrestaurant/images/Ban/Ban_4.png'),
('BA000010', N'Bàn 2-T2',   N'Trống', 250,100,  'TG000002', 'LB000002', N'/com/thefourrestaurant/images/Ban/Ban_4.png'),
('BA000011', N'Bàn 3-T2',   N'Trống', 650,100,  'TG000002', 'LB000002', N'/com/thefourrestaurant/images/Ban/Ban_4.png'),

('BA000012', N'Bàn 4-T2',   N'Trống', 50, 300,  'TG000002', 'LB000003', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),
('BA000013', N'Bàn 5-T2',   N'Trống', 250,300,  'TG000002', 'LB000003', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),
('BA000014', N'Bàn 6-T2',   N'Trống', 650,300,  'TG000002', 'LB000003', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),

('BA000015', N'Bàn 7-T2',   N'Trống', 50, 450,  'TG000002', 'LB000001', N'/com/thefourrestaurant/images/Ban/Ban_2.png'),
('BA000016', N'Bàn 8-T2',   N'Trống', 250,450,  'TG000002', 'LB000001', N'/com/thefourrestaurant/images/Ban/Ban_2.png'),
('BA000017', N'Bàn 9-T2',   N'Trống', 450,450,  'TG000002', 'LB000001', N'/com/thefourrestaurant/images/Ban/Ban_2.png'),
('BA000018', N'Bàn 10-T2',  N'Trống', 650,450,  'TG000002', 'LB000001', N'/com/thefourrestaurant/images/Ban/Ban_2.png'),
('BA000019', N'Bàn 11-T2',  N'Trống', 850,450,  'TG000002', 'LB000001', N'/com/thefourrestaurant/images/Ban/Ban_2.png'),

-- ===== Tầng 3 =====
('BA000020', N'Bàn 1-T3', N'Trống', 100,100, 'TG000003', 'LB000003', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),
('BA000021', N'Bàn 2-T3', N'Trống', 400,100, 'TG000003', 'LB000003', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),
('BA000022', N'Bàn 3-T3', N'Trống', 700,100, 'TG000003', 'LB000003', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),

('BA000023', N'Bàn 4-T3', N'Trống', 300,450, 'TG000003', 'LB000004', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),
('BA000024', N'Bàn 5-T3', N'Trống', 600,450, 'TG000003', 'LB000004', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),

('BA000025', N'Bàn 6-T3', N'Trống', 100,450, 'TG000003', 'LB000003', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),
('BA000026', N'Bàn 7-T3', N'Trống', 400,450, 'TG000003', 'LB000003', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),
('BA000027', N'Bàn 8-T3', N'Trống', 700,450, 'TG000003', 'LB000003', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),

-- ===== Tầng 4 =====
('BA000028', N'Bàn 1-T4', N'Trống', 200,300, 'TG000004', 'LB000004', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),
('BA000029', N'Bàn 2-T4', N'Trống', 650,300, 'TG000004', 'LB000004', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),

-- ===== Tầng 5 =====
('BA000030', N'Bàn 1-T5', N'Trống', 50,100,  'TG000005', 'LB000002', N'/com/thefourrestaurant/images/Ban/Ban_4.png'),
('BA000031', N'Bàn 2-T5', N'Trống', 250,100, 'TG000005', 'LB000002', N'/com/thefourrestaurant/images/Ban/Ban_4.png'),
('BA000032', N'Bàn 3-T5', N'Trống', 650,100, 'TG000005', 'LB000002', N'/com/thefourrestaurant/images/Ban/Ban_4.png'),

('BA000033', N'Bàn 4-T5', N'Trống', 50,300,  'TG000005', 'LB000003', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),
('BA000034', N'Bàn 5-T5', N'Trống', 250,300, 'TG000005', 'LB000003', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),
('BA000035', N'Bàn 6-T5', N'Trống', 650,300, 'TG000005', 'LB000003', N'/com/thefourrestaurant/images/Ban/Ban_6.png'),

('BA000036', N'Bàn 7-T5',  N'Trống', 50,550, 'TG000005', 'LB000001', N'/com/thefourrestaurant/images/Ban/Ban_2.png'),
('BA000037', N'Bàn 8-T5',  N'Trống', 250,550, 'TG000005', 'LB000001', N'/com/thefourrestaurant/images/Ban/Ban_2.png'),
('BA000038', N'Bàn 9-T5',  N'Trống', 450,550, 'TG000005', 'LB000001', N'/com/thefourrestaurant/images/Ban/Ban_2.png'),
('BA000039', N'Bàn 10-T5', N'Trống', 650,550, 'TG000005', 'LB000001', N'/com/thefourrestaurant/images/Ban/Ban_2.png'),
('BA000040', N'Bàn 11-T5', N'Trống', 850,550, 'TG000005', 'LB000001', N'/com/thefourrestaurant/images/Ban/Ban_2.png'),

-- ===== Tầng 6 =====
('BA000041', N'Bàn 1-T6', N'Trống', 140,150, 'TG000006', 'LB000004', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),
('BA000042', N'Bàn 2-T6', N'Trống', 600,150, 'TG000006', 'LB000004', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),
('BA000043', N'Bàn 3-T6', N'Trống', 140,400, 'TG000006', 'LB000004', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),
('BA000044', N'Bàn 4-T6', N'Trống', 600,400, 'TG000006', 'LB000004', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),

-- ===== Tầng 7 =====
('BA000045', N'Bàn 1-T7', N'Trống', 250,100, 'TG000007', 'LB000004', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),
('BA000046', N'Bàn 2-T7', N'Trống', 450,100, 'TG000007', 'LB000004', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),
('BA000047', N'Bàn 3-T7', N'Trống', 650,100, 'TG000007', 'LB000004', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),
('BA000048', N'Bàn 4-T7', N'Trống', 250,350, 'TG000007', 'LB000004', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),
('BA000049', N'Bàn 5-T7', N'Trống', 450,350, 'TG000007', 'LB000004', N'/com/thefourrestaurant/images/Ban/Ban_8.png'),
('BA000050', N'Bàn 6-T7', N'Trống', 650,350, 'TG000007', 'LB000004', N'/com/thefourrestaurant/images/Ban/Ban_8.png');
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
('MA000001', N'Cơm tấm sườn bì', 55000, N'Còn', 'LM000001', N'/com/thefourrestaurant/images/MonAn/com_tam_suon_bi.png'),
('MA000002', N'Cơm gà xối mỡ', 60000, N'Còn', 'LM000001', N'/com/thefourrestaurant/images/MonAn/com_ga_xoi_mo.png'),
('MA000003', N'Nước cam ép', 25000, N'Còn', 'LM000002', N'/com/thefourrestaurant/images/MonAn/nuoc_cam_ep.png'),
('MA000004', N'Sinh tố bơ', 30000, N'Còn', 'LM000002', N'/com/thefourrestaurant/images/MonAn/sinh_to_bo.png'),
('MA000005', N'Bánh flan', 20000, N'Còn', 'LM000003', N'/com/thefourrestaurant/images/MonAn/banh_flan.png'),
('MA000006', N'Lẩu thái hải sản', 250000, N'Còn', 'LM000004', N'/com/thefourrestaurant/images/MonAn/lau_thai_hai_san.png');
GO

-- Loại ThucDon
INSERT INTO ThucDon(maTD, tenTD) VALUES
('TD000001', N'Sáng'),
('TD000002', N'Trưa'),
('TD000003', N'Chiều'),
('TD000004', N'Tối');
GO

INSERT INTO ChiTietThucDon(maLoaiMon, maTD) VALUES
('LM000001', 'TD000001'), -- Sáng
('LM000001', 'TD000002'); -- Trưa

-- Loại món LM000002 (Món chính) chỉ xuất hiện trong Chiều
INSERT INTO ChiTietThucDon(maLoaiMon, maTD) VALUES
    ('LM000002', 'TD000003'); -- Chiều

-- Loại món LM000003 (Tráng miệng) xuất hiện trong Trưa và Tối
INSERT INTO ChiTietThucDon(maLoaiMon, maTD) VALUES
                                                ('LM000003', 'TD000002'), -- Trưa
                                                ('LM000003', 'TD000004'); -- Tối
GO

-- Dữ liệu Phiếu đặt bàn
INSERT INTO PhieuDatBan (maPDB, ngayDat, soNguoi, maKH, maNV, ngayTao, trangThai, tienCoc)
VALUES
('PD000001', '2025-12-25 18:30:00', 4, 'KH000001', 'NV000001', '2025-10-23 10:00:00', N'Đã thanh toán', 0),
('PD000002', '2025-12-25 12:00:00', 2, 'KH000002', 'NV000002', '2025-10-23 10:05:00', N'Đã thanh toán', 0),
('PD000003', '2025-12-25 19:00:00', 3, 'KH000001', 'NV000002', '2025-10-22 09:30:00', N'Đã thanh toán', 0);
GO

-- Liên kết Phiếu đặt bàn với Bàn
INSERT INTO PhieuDatBan_Ban (maPDB, maBan, isBanChinh)
VALUES
('PD000001', 'BA000001', 1),
('PD000002', 'BA000002', 0),
('PD000003', 'BA000003', 0);
GO

-- ==============================
-- Chi tiết phiếu đặt bàn
-- ==============================
INSERT INTO ChiTietPDB (maCT, maPDB, maMonAn, soLuong, donGia, ghiChu) VALUES
('CTP00001', 'PD000001', 'MA000001', 2, 55000, N'Không hành'),
('CTP00002', 'PD000002', 'MA000003', 2, 25000, N'Ít cay'),
('CTP00003', 'PD000003', 'MA000002', 2, 60000, NULL);
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
INSERT INTO KhuyenMai (maKM, maLoaiKM, tenKM, kieuKM, maCode, soLuotSuDung, ngayBatDau, ngayKetThuc, moTa) VALUES
('KM000001', 'LKM00001', N'Giảm 10% hóa đơn', N'SuKien', NULL, NULL, '2025-10-01', '2025-10-31', N'Áp dụng cho tất cả đơn hàng trong tháng 10'),
('KM000002', 'LKM00002', N'Mua cà phê tặng nước cam', N'SuKien', NULL, NULL, '2025-10-10', '2025-10-31', N'Chương trình khuyến mãi đặc biệt cho khách hàng thân thiết'),
('KM000003', 'LKM00003', N'Giảm 15.000đ cho bún bò Huế', N'MaGiamGia', 'BUNBO15K', 100, '2025-11-01', '2025-11-30', N'Áp dụng cho món bún bò Huế tại tất cả chi nhánh');
GO

-- Dữ liệu mẫu cho cấu trúc khuyến mãi mới
-- Ví dụ 1: Giảm 10% cho Cơm tấm và Cơm gà
INSERT INTO KhuyenMai_DieuKien (maDieuKien, maKM, loaiApDung, tyLeGiam, moTaDieuKien) VALUES
('DK000001', 'KM000001', 'GIAM_TRUC_TIEP', 10.00, N'Giảm 10% cho các món cơm');
GO
INSERT INTO DieuKien_Mon (maDieuKien, maMonAn, vaiTro) VALUES
('DK000001', 'MA000001', 'GIAM_TRUC_TIEP'),
('DK000001', 'MA000002', 'GIAM_TRUC_TIEP');
GO

-- Ví dụ 2: Mua 1 Lẩu thái, tặng 1 Nước cam
INSERT INTO KhuyenMai_DieuKien (maDieuKien, maKM, loaiApDung, soLuongTang, moTaDieuKien) VALUES
('DK000002', 'KM000002', 'MUA_X_GIAM_Y', 1, N'Mua Lẩu Thái tặng Nước Cam');
GO
INSERT INTO DieuKien_Mon (maDieuKien, maMonAn, vaiTro) VALUES
('DK000002', 'MA000006', 'MUA'); -- Món điều kiện
GO
INSERT INTO DieuKien_MonTang (maDieuKien, maMonAnTang) VALUES
('DK000002', 'MA000003'); -- Món được tặng
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

-- Thêm món ăn
INSERT INTO MonAn
(maMonAn, tenMon, donGia, trangThai, maLoaiMon, hinhAnh, soLuong, daBan, isDeleted, isVisible)
VALUES
-- Bánh & Tráng miệng
('MA000007', N'Bánh kếp socola', 35000, N'Còn', 'LM000003', N'/com/thefourrestaurant/images/MonAn/banh-kep-socola.jpg', 100, 0, 0, 1),
('MA000008', N'Bánh mì', 20000, N'Còn', 'LM000001', N'/com/thefourrestaurant/images/MonAn/banh-mi.jpg', 100, 0, 0, 1),
('MA000009', N'Bánh mì chảo', 45000, N'Còn', 'LM000001', N'/com/thefourrestaurant/images/MonAn/banh-mi-chao.jpg', 100, 0, 0, 1),

-- Bún / Phở
('MA000010', N'Bún bò Huế', 55000, N'Còn', 'LM000002', N'/com/thefourrestaurant/images/MonAn/bun-bo-hue.jpg', 100, 0, 0, 1),
('MA000011', N'Bún thịt nướng', 50000, N'Còn', 'LM000002', N'/com/thefourrestaurant/images/MonAn/bun-thit-nuong.jpg', 100, 0, 0, 1),
('MA000012', N'Bún tôm', 52000, N'Còn', 'LM000002', N'/com/thefourrestaurant/images/MonAn/bun-tom.jpg', 100, 0, 0, 1),

-- Món chính
('MA000013', N'Cá hồi nướng', 120000, N'Còn', 'LM000004', N'/com/thefourrestaurant/images/MonAn/ca-hoi-nuong.jpg', 100, 0, 0, 1),
('MA000014', N'Croissant', 30000, N'Còn', 'LM000003', N'/com/thefourrestaurant/images/MonAn/croisant.jpg', 100, 0, 0, 1),
('MA000015', N'Dim-sum', 65000, N'Còn', 'LM000004', N'/com/thefourrestaurant/images/MonAn/dim-sum.jpg', 100, 0, 0, 1),

-- Khai vị & salad
('MA000016', N'Gỏi cuốn tôm thịt', 40000, N'Còn', 'LM000003', N'/com/thefourrestaurant/images/MonAn/goi-cuon-tom-thit.jpg', 100, 0, 0, 1),
('MA000017', N'Hoa quả mix', 45000, N'Còn', 'LM000003', N'/com/thefourrestaurant/images/MonAn/hoa-qua-mix.jpg', 100, 0, 0, 1),

-- Nướng & đặc biệt
('MA000018', N'Bò nướng tảng', 180000, N'Còn', 'LM000004', N'/com/thefourrestaurant/images/MonAn/bo-nuong-tang.jpg', 100, 0, 0, 1),
('MA000019', N'Mỳ Ý', 60000, N'Còn', 'LM000002', N'/com/thefourrestaurant/images/MonAn/my-y.jpg', 100, 0, 0, 1),
('MA000020', N'Chả tôm', 50000, N'Còn', 'LM000004', N'/com/thefourrestaurant/images/MonAn/cha-tom.jpg', 100, 0, 0, 1),
('MA000021', N'Pasta', 65000, N'Còn', 'LM000002', N'/com/thefourrestaurant/images/MonAn/pasta.jpg', 100, 0, 0, 1),

-- Bánh & xiên
('MA000022', N'Bánh kếp dâu', 38000, N'Còn', 'LM000003', N'/com/thefourrestaurant/images/MonAn/banh-kep-dau.jpg', 100, 0, 0, 1),
('MA000023', N'Xiên nướng bò rau củ', 70000, N'Còn', 'LM000004', N'/com/thefourrestaurant/images/MonAn/xien-nuong-bo-rau-cu.jpg', 100, 0, 0, 1),

-- Phở
('MA000024', N'Phở bò', 55000, N'Còn', 'LM000002', N'/com/thefourrestaurant/images/MonAn/pho-bo.jpg', 100, 0, 0, 1),
('MA000025', N'Phở gà', 50000, N'Còn', 'LM000002', N'/com/thefourrestaurant/images/MonAn/pho-ga.jpg', 100, 0, 0, 1),

-- Salad & Nhật
('MA000026', N'Salad hải sản', 75000, N'Còn', 'LM000003', N'/com/thefourrestaurant/images/MonAn/salad-hai-san.jpg', 100, 0, 0, 1),
('MA000027', N'Salad kiểu Úc', 70000, N'Còn', 'LM000003', N'/com/thefourrestaurant/images/MonAn/salad-kieu-uc.jpg', 100, 0, 0, 1),
('MA000028', N'Sushi tôm tít', 90000, N'Còn', 'LM000004', N'/com/thefourrestaurant/images/MonAn/sushi-tom-tit.jpg', 100, 0, 0, 1),

-- Tráng miệng cao cấp
('MA000029', N'Tiramisu', 55000, N'Còn', 'LM000003', N'/com/thefourrestaurant/images/MonAn/tiramisu.jpg', 100, 0, 0, 1);

CREATE UNIQUE INDEX UX_PDB_OneBanChinh
ON PhieuDatBan_Ban(maPDB)
WHERE isBanChinh = 1;
GO
