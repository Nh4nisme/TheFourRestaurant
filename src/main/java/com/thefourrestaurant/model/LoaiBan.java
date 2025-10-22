package com.thefourrestaurant.model;

public class LoaiBan {
    private String maLoaiBan;
    private String tenLoaiBan;

    public LoaiBan() {}

    public LoaiBan(String maLoaiBan) {
        this.maLoaiBan = maLoaiBan;
    }

    public LoaiBan(String maLoaiBan, String tenLoaiBan) {
        this.maLoaiBan = maLoaiBan;
        this.tenLoaiBan = tenLoaiBan;
    }

    public String getMaLoaiBan() {
        return maLoaiBan;
    }

    public void setMaLoaiBan(String maLoaiBan) {
        this.maLoaiBan = maLoaiBan;
    }

    public String getTenLoaiBan() {
        return tenLoaiBan;
    }

    public void setTenLoaiBan(String tenLoaiBan) {
        this.tenLoaiBan = tenLoaiBan;
    }

    @Override
    public String toString() {
        return tenLoaiBan;
    }
}
