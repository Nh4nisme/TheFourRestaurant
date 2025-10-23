package com.thefourrestaurant.model;

public class Tang {
    private String maTang;
    private String tenTang;
    private String moTa;
    private boolean isDeleted;

    public Tang() {}

    public Tang(String maTang) {
        setMaTang(maTang);
    }

    public Tang(String maTang, String tenTang) {
        setMaTang(maTang);
        setTenTang(tenTang);
    }

    public Tang(String maTang, String tenTang, String moTa, boolean isDeleted) {
        setMaTang(maTang);
        setTenTang(tenTang);
        setMoTa(moTa);
        setDeleted(isDeleted);
    }

    public String getMaTang() {
        return maTang;
    }

    public void setMaTang(String maTang) {
        this.maTang = maTang;
    }

    public String getTenTang() {
        return tenTang;
    }

    public void setTenTang(String tenTang) {
        this.tenTang = tenTang;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return tenTang; // hiển thị trong combobox
    }
}
