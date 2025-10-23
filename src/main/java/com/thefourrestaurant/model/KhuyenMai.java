package com.thefourrestaurant.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class KhuyenMai {
    private String maKM;
    private LoaiKhuyenMai loaiKhuyenMai;
    private BigDecimal tyLe;      // Có thể null
    private BigDecimal soTien;    // Có thể null
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;
    private String moTa;
    private List<ChiTietKhuyenMai> chiTietKhuyenMais; // Danh sách chi tiết KM

    public KhuyenMai() {}

    public KhuyenMai(String maKM, LoaiKhuyenMai loaiKhuyenMai, BigDecimal tyLe, BigDecimal soTien,
                     LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc, String moTa) {
        this.maKM = maKM;
        this.loaiKhuyenMai = loaiKhuyenMai;
        this.tyLe = tyLe;
        this.soTien = soTien;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.moTa = moTa;
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

    public LocalDateTime getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDateTime ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDateTime getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDateTime ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public List<ChiTietKhuyenMai> getChiTietKhuyenMais() {
        return chiTietKhuyenMais;
    }

    public void setChiTietKhuyenMais(List<ChiTietKhuyenMai> chiTietKhuyenMais) {
        this.chiTietKhuyenMais = chiTietKhuyenMais;
    }

    @Override
    public String toString() {
        return moTa != null ? moTa : maKM;
    }
}
