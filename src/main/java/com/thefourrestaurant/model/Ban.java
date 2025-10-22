package com.thefourrestaurant.model;

public class Ban {
    private String maBan;
    private String tenBan;
    private String trangThai; // 'Trống', 'Đang sử dụng', 'Đặt trước'
    private int toaDoX;
    private int toaDoY;
    private Tang tang;           // Đối tượng Tang
    private LoaiBan loaiBan;     // Đối tượng LoaiBan
    private String anhBan;       // Link ảnh bàn

    public Ban() {
    }

    // 🔹 Constructor đầy đủ
    public Ban(String maBan, String tenBan, String trangThai, int toaDoX, int toaDoY,
               Tang tang, LoaiBan loaiBan, String anhBan) {
        this.maBan = maBan;
        this.tenBan = tenBan;
        this.trangThai = trangThai;
        this.toaDoX = toaDoX;
        this.toaDoY = toaDoY;
        this.tang = tang;
        this.loaiBan = loaiBan;
        this.anhBan = anhBan;
    }

    // 🔹 Constructor rút gọn (chỉ cần mã)
    public Ban(String maBan) {
        this.maBan = maBan;
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

    public Tang getTang() {
        return tang;
    }

    public void setTang(Tang tang) {
        this.tang = tang;
    }

    public LoaiBan getLoaiBan() {
        return loaiBan;
    }

    public void setLoaiBan(LoaiBan loaiBan) {
        this.loaiBan = loaiBan;
    }

    public String getAnhBan() {
        return anhBan;
    }

    public void setAnhBan(String anhBan) {
        this.anhBan = anhBan;
    }

    @Override
    public String toString() {
        return tenBan + " - " + (tang != null ? tang.getTenTang() : "Chưa có tầng") + " (" + trangThai + ")";
    }
}
