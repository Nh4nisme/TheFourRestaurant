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
        setMaKH(maKH);
    }

    public KhachHang(String maKH, String hoTen, Date ngaySinh, String gioiTinh,
                     String soDT, LoaiKhachHang loaiKH, boolean isDeleted) {
        setMaKH(maKH);
        setHoTen(hoTen);
        setNgaySinh(ngaySinh);
        setGioiTinh(gioiTinh);
        setSoDT(soDT);
        setLoaiKH(loaiKH);
        setDeleted(isDeleted);
    }
    
    public KhachHang(String maKH, String hoTen, String soDT) {
    	setMaKH(maKH);
        setHoTen(hoTen);
        setSoDT(soDT);
    }

    public KhachHang(String maKH, String hoTen, Date ngaySinh, String gioiTinh,
                     String soDT, LoaiKhachHang loaiKH) {
        this(maKH, hoTen, ngaySinh, gioiTinh, soDT, loaiKH, false);
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getSoDT() {
        return soDT;
    }

    public void setSoDT(String soDT) {
        this.soDT = soDT;
    }

    public LoaiKhachHang getLoaiKH() {
        return loaiKH;
    }

    public void setLoaiKH(LoaiKhachHang loaiKH) {
        this.loaiKH = loaiKH;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "KhachHang{" +
                "maKH='" + maKH + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", ngaySinh=" + ngaySinh +
                ", gioiTinh='" + gioiTinh + '\'' +
                ", soDT='" + soDT + '\'' +
                ", loaiKH=" + (loaiKH != null ? loaiKH.getTenLoaiKH() : "null") +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
