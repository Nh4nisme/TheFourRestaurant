package com.thefourrestaurant.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class KhuyenMai {
    private String maKM;
    private LoaiKhuyenMai loaiKhuyenMai;
    private BigDecimal tyLe;      // Có thể null
    private BigDecimal soTien;    // Có thể null
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private String moTa;
    private MonAn monAnApDung;
    private MonAn monAnTang;

    public KhuyenMai() {}

    public KhuyenMai(String maKM, LoaiKhuyenMai loaiKhuyenMai, BigDecimal tyLe, BigDecimal soTien,
                     LocalDate ngayBatDau, LocalDate ngayKetThuc, String moTa, MonAn monAnApDung, MonAn monAnTang) {
        this.maKM = maKM;
        this.loaiKhuyenMai = loaiKhuyenMai;
        this.tyLe = tyLe;
        this.soTien = soTien;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.moTa = moTa;
        this.monAnApDung = monAnApDung;
        this.monAnTang = monAnTang;
    }


    // Getters & Setters
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

    public MonAn getMonAnApDung() {
        return monAnApDung;
    }

    public void setMonAnApDung(MonAn monAnApDung) {
        this.monAnApDung = monAnApDung;
    }

    public MonAn getMonAnTang() {
        return monAnTang;
    }

    public void setMonAnTang(MonAn monAnTang) {
        this.monAnTang = monAnTang;
    }

    @Override
    public String toString() {
        return moTa != null ? moTa : maKM;
    }
}
