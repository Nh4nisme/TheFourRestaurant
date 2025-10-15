package com.thefourrestaurant.model;

public class Ban {
    private String maBan;
    private String tenBan;
    private String trangThai; // 'Trống', 'Đang sử dụng', 'Đặt trước'
    private int toaDoX;
    private int toaDoY;
    private String maTang;     // Khóa ngoại đến bảng Tang
    private String maLoaiBan;  // Khóa ngoại đến bảng LoaiBan

    public Ban() {
    }

    public Ban(String maBan, String tenBan, String trangThai, int toaDoX, int toaDoY, String maTang, String maLoaiBan) {
        this.maBan = maBan;
        this.tenBan = tenBan;
        this.trangThai = trangThai;
        this.toaDoX = toaDoX;
        this.toaDoY = toaDoY;
        this.maTang = maTang;
        this.maLoaiBan = maLoaiBan;
    }

    // Getters & Setters
    public String getMaBan() {
        return maBan;
    }

    public void setMaBan(String maBan) {
        this.maBan = maBan;
    }

    public String getTenBan() {
        return tenBan;
    }

    public void setTenBan(String tenBan) {
        this.tenBan = tenBan;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public int getToaDoX() {
        return toaDoX;
    }

    public void setToaDoX(int toaDoX) {
        this.toaDoX = toaDoX;
    }

    public int getToaDoY() {
        return toaDoY;
    }

    public void setToaDoY(int toaDoY) {
        this.toaDoY = toaDoY;
    }

    public String getMaTang() {
        return maTang;
    }

    public void setMaTang(String maTang) {
        this.maTang = maTang;
    }

    public String getMaLoaiBan() {
        return maLoaiBan;
    }

    public void setMaLoaiBan(String maLoaiBan) {
        this.maLoaiBan = maLoaiBan;
    }

    @Override
    public String toString() {
        return tenBan + " (" + trangThai + ")";
    }
}
