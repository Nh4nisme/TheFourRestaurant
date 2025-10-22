package com.thefourrestaurant.model;

public class ChiTietPDB {
    private String maCT;
    private PhieuDatBan phieuDatBan;
    private Ban ban;
    private MonAn monAn;
    private int soLuong;
    private double donGia;

    public ChiTietPDB() {}

    public ChiTietPDB(String maCT, PhieuDatBan phieuDatBan, Ban ban,
                      MonAn monAn, int soLuong, double donGia) {
        this.maCT = maCT;
        this.phieuDatBan = phieuDatBan;
        this.ban = ban;
        this.monAn = monAn;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    // Getters và Setters
    public String getMaCT() { return maCT; }
    public void setMaCT(String maCT) { this.maCT = maCT; }

    public PhieuDatBan getPhieuDatBan() { return phieuDatBan; }
    public void setPhieuDatBan(PhieuDatBan phieuDatBan) { this.phieuDatBan = phieuDatBan; }

    public Ban getBan() { return ban; }
    public void setBan(Ban ban) { this.ban = ban; }

    public MonAn getMonAn() { return monAn; }
    public void setMonAn(MonAn monAn) { this.monAn = monAn; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }
}

