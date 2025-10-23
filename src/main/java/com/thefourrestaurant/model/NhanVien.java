package com.thefourrestaurant.model;

import java.math.BigDecimal;
import java.sql.Date;

public class    NhanVien {
    private String maNV;
    private String hoTen;
    private Date ngaySinh;
    private String gioiTinh;
    private String soDienThoai;
    private BigDecimal luong;
    private TaiKhoan maTK;
    private boolean isDeleted;

    public NhanVien() {}
    
    public NhanVien(String maNV) {
    	this.maNV = maNV;
    }

    public NhanVien(String maNV, String hoTen, Date ngaySinh, String gioiTinh, String soDienThoai,
                    BigDecimal luong, TaiKhoan maTK, boolean isDeleted) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.soDienThoai = soDienThoai;
        this.luong = luong;
        this.maTK = maTK;
        this.isDeleted = isDeleted;
    }

    public NhanVien(String maNV, String hoTen, Date ngaySinh, String gioiTinh, String soDienThoai,
                    BigDecimal luong, TaiKhoan maTK) {
        this(maNV, hoTen, ngaySinh, gioiTinh, soDienThoai, luong, maTK, false);
    }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public Date getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public BigDecimal getLuong() { return luong; }
    public void setLuong(BigDecimal luong) { this.luong = luong; }

    public TaiKhoan getMaTK() { return maTK; }
    public void setMaTK(TaiKhoan maTK) { this.maTK = maTK; }

    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }
}

