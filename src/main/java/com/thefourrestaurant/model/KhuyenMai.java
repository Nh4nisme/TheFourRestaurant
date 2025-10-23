package com.thefourrestaurant.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class KhuyenMai {
    private String maKM;
    private LoaiKhuyenMai loaiKhuyenMai; // Changed from String
    private BigDecimal tyLe;
    private BigDecimal soTien;
    private MonAn monAnTang; // Changed from String
    private MonAn monAnApDung; // Added for LKM00003
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private String moTa;

    public KhuyenMai() {
    }

    // Getters and Setters
    public String getMaKM() {
        return maKM;
    }

    public void setMaKM(String maKM) {
        this.maKM = maKM;
    }

    public LoaiKhuyenMai getLoaiKhuyenMai() {
        return loaiKhuyenMai;
    }

    public void setLoaiKhuyenMai(LoaiKhuyenMai loaiKhuyenMai) {
        this.loaiKhuyenMai = loaiKhuyenMai;
    }

    public BigDecimal getTyLe() {
        return tyLe;
    }

    public void setTyLe(BigDecimal tyLe) {
        this.tyLe = tyLe;
    }

    public BigDecimal getSoTien() {
        return soTien;
    }

    public void setSoTien(BigDecimal soTien) {
        this.soTien = soTien;
    }

    public MonAn getMonAnTang() {
        return monAnTang;
    }

    public void setMonAnTang(MonAn monAnTang) {
        this.monAnTang = monAnTang;
    }

    public MonAn getMonAnApDung() {
        return monAnApDung;
    }

    public void setMonAnApDung(MonAn monAnApDung) {
        this.monAnApDung = monAnApDung;
    }

    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    @Override
    public String toString() {
        return this.moTa; // For display in ComboBox
    }
}
