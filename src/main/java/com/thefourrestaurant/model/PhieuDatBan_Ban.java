package com.thefourrestaurant.model;

public class PhieuDatBan_Ban {
    private String maPDB;
    private String maBan;

    private PhieuDatBan phieuDatBan;
    private Ban ban;

    public PhieuDatBan_Ban() {}

    public PhieuDatBan_Ban(String maPDB, String maBan) {
        this.maPDB = maPDB;
        this.maBan = maBan;
    }

    public String getMaPDB() {
        return maPDB;
    }

    public void setMaPDB(String maPDB) {
        this.maPDB = maPDB;
    }

    public String getMaBan() {
        return maBan;
    }

    public void setMaBan(String maBan) {
        this.maBan = maBan;
    }

    public PhieuDatBan getPhieuDatBan() {
        return phieuDatBan;
    }

    public void setPhieuDatBan(PhieuDatBan phieuDatBan) {
        this.phieuDatBan = phieuDatBan;
    }

    public Ban getBan() {
        return ban;
    }

    public void setBan(Ban ban) {
        this.ban = ban;
    }
}
