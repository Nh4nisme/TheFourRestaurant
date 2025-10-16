package com.thefourrestaurant.model;

public class Tang {
    private String maTang;
    private String tenTang;
    private String moTa;

    public Tang() {}

    public Tang(String maTang, String tenTang, String moTa) {
        this.maTang = maTang;
        this.tenTang = tenTang;
        this.moTa = moTa;
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

    @Override
    public String toString() {
        return tenTang; // Khi hiển thị trong combobox hoặc list
    }
}
