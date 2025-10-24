package com.thefourrestaurant.model;

import java.time.LocalTime;

public class KhungGio {
    private String maTG;
    private LocalTime gioBatDau;
    private LocalTime gioKetThuc;
    private boolean lapLaiHangNgay;

    public KhungGio() {}

    public KhungGio(String maTG, LocalTime gioBatDau, LocalTime gioKetThuc, boolean lapLaiHangNgay) {
        this.maTG = maTG;
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
        this.lapLaiHangNgay = lapLaiHangNgay;
    }

    public String getMaTG() {
        return maTG;
    }

    public void setMaTG(String maTG) {
        this.maTG = maTG;
    }

    public LocalTime getGioBatDau() {
        return gioBatDau;
    }

    public void setGioBatDau(LocalTime gioBatDau) {
        this.gioBatDau = gioBatDau;
    }

    public LocalTime getGioKetThuc() {
        return gioKetThuc;
    }

    public void setGioKetThuc(LocalTime gioKetThuc) {
        this.gioKetThuc = gioKetThuc;
    }

    public boolean isLapLaiHangNgay() {
        return lapLaiHangNgay;
    }

    public void setLapLaiHangNgay(boolean lapLaiHangNgay) {
        this.lapLaiHangNgay = lapLaiHangNgay;
    }
}
