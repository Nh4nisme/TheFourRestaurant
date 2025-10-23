package com.thefourrestaurant.model;

public class LoaiBan {
    private String maLoaiBan;
    private String tenLoaiBan;

    public LoaiBan() {}

    public LoaiBan(String maLoaiBan) {
        setMaLoaiBan(maLoaiBan);
    }

    public LoaiBan(String maLoaiBan, String tenLoaiBan) {
        setMaLoaiBan(maLoaiBan);
        setTenLoaiBan(tenLoaiBan);
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
    
    public int getSoNguoi() {
        if (tenLoaiBan != null) {
            try {
                return Integer.parseInt(tenLoaiBan.replaceAll("\\D", ""));
            } catch (NumberFormatException e) {
                return 1;
            }
        }
        return 1;
    }

    @Override
    public String toString() {
        return tenLoaiBan;
    }
}
