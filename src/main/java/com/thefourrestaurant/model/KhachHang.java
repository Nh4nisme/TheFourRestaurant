package com.thefourrestaurant.model;

import java.sql.Date;

public class KhachHang {
    private String maKH;
    private String hoTen;
    private Date ngaySinh;
    private String gioiTinh;
    private String soDT;
    private LoaiKhachHang loaiKH;
    private boolean isDeleted;

    public KhachHang() {}
    
    public KhachHang(String maKH) {
    	this.maKH = maKH;
    }

    public KhachHang(String maKH, String hoTen, Date ngaySinh, String gioiTinh,
                     String soDT, LoaiKhachHang loaiKH, boolean isDeleted) {
        this.maKH = maKH;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.soDT = soDT;
        this.loaiKH = loaiKH;
        this.isDeleted = isDeleted;
    }

    public KhachHang(String maKH, String hoTen, Date ngaySinh, String gioiTinh,
                     String soDT, LoaiKhachHang loaiKH) {
        this(maKH, hoTen, ngaySinh, gioiTinh, soDT, loaiKH, false);
    }

    // Getters và setters
    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public Date getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getSoDT() { return soDT; }
    public void setSoDT(String soDT) { this.soDT = soDT; }

    public LoaiKhachHang getMaLoaiKH() { return loaiKH; }
    public void setMaLoaiKH(LoaiKhachHang loaiKH) { this.loaiKH = loaiKH; }

    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }
}
