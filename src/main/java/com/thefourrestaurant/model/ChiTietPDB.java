package com.thefourrestaurant.model;

public class ChiTietPDB {
    private String maCT;
    private PhieuDatBan phieuDatBan;
    private MonAn monAn;
    private int soLuong;
    private double donGia;
    private String ghiChu;

    public ChiTietPDB() {}

    public ChiTietPDB(String maCT, PhieuDatBan phieuDatBan, MonAn monAn,
                      int soLuong, double donGia, String ghiChu) {
        this.maCT = maCT;
        this.phieuDatBan = phieuDatBan;
        this.monAn = monAn;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.ghiChu = ghiChu;
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
    public void setDonGia(double donGia) { this.donGia = donGia; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}
