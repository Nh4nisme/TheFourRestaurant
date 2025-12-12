package com.thefourrestaurant.model;

public class ChiTietPDB {
    private String maCT;
    private PhieuDatBan phieuDatBan;
    private MonAn monAn;
    private int soLuong;
    private double donGia;
    private String ghiChu;
    private double giaSauKhuyenMai;
    private String tenKhuyenMai;

    public ChiTietPDB() {}

    public ChiTietPDB(String maCT, PhieuDatBan phieuDatBan, MonAn monAn,
                      int soLuong, double donGia, String ghiChu) {
        this.maCT = maCT;
        this.phieuDatBan = phieuDatBan;
        this.monAn = monAn;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.ghiChu = ghiChu;
        this.giaSauKhuyenMai = donGia;
    }

    public String getMaCT() { return maCT; }
    public void setMaCT(String maCT) { this.maCT = maCT; }

    public PhieuDatBan getPhieuDatBan() { return phieuDatBan; }
    public void setPhieuDatBan(PhieuDatBan phieuDatBan) { this.phieuDatBan = phieuDatBan; }

    public MonAn getMonAn() { return monAn; }
    public void setMonAn(MonAn monAn) { this.monAn = monAn; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { 
        this.donGia = donGia; 
        if (this.giaSauKhuyenMai == 0) {
            this.giaSauKhuyenMai = donGia;
        }
    }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
    
    public double getGiaSauKhuyenMai() { 
        return giaSauKhuyenMai == 0 ? donGia : giaSauKhuyenMai; 
    }
    public void setGiaSauKhuyenMai(double giaSauKhuyenMai) { 
        this.giaSauKhuyenMai = giaSauKhuyenMai; 
    }
    
    public String getTenKhuyenMai() { return tenKhuyenMai; }
    public void setTenKhuyenMai(String tenKhuyenMai) { this.tenKhuyenMai = tenKhuyenMai; }
}