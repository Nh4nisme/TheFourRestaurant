package com.thefourrestaurant.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class KhuyenMai {
    public static final String KIEU_SU_KIEN = "SuKien";
    public static final String KIEU_MA_GIAM_GIA = "MaGiamGia";
    
    private String maKM;
    private LoaiKhuyenMai loaiKhuyenMai;
    private String tenKM;
    private String kieuKM;
    private String maCode;
    private Integer soLuotSuDung;
    private BigDecimal tyLe;
    private BigDecimal soTien;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;
    private String moTa;

    public KhuyenMai() {
        this.kieuKM = KIEU_SU_KIEN;
    }

    public KhuyenMai(String maKM, LoaiKhuyenMai loaiKhuyenMai, String tenKM, BigDecimal tyLe, BigDecimal soTien,
                     LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc, String moTa) {
        this.maKM = maKM;
        this.loaiKhuyenMai = loaiKhuyenMai;
        setTenKM(tenKM);
        this.kieuKM = KIEU_SU_KIEN;
        this.tyLe = tyLe;
        this.soTien = soTien;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.moTa = moTa;
    }

    public KhuyenMai(String maKM, String tenKM, BigDecimal kmTyLe, BigDecimal kmSoTien) {
        this.kieuKM = KIEU_SU_KIEN;
    }

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

    public String getTenKM() {return tenKM;}

    public void setTenKM(String tenKM) {this.tenKM = tenKM;}

    public String getKieuKM() {
        return kieuKM;
    }

    public void setKieuKM(String kieuKM) {
        this.kieuKM = kieuKM;
    }

    public String getMaCode() {
        return maCode;
    }

    public void setMaCode(String maCode) {
        this.maCode = maCode;
    }

    public Integer getSoLuotSuDung() {
        return soLuotSuDung;
    }

    public void setSoLuotSuDung(Integer soLuotSuDung) {
        this.soLuotSuDung = soLuotSuDung;
    }

    public boolean laKieuSuKien() {
        return KIEU_SU_KIEN.equals(kieuKM);
    }

    public boolean laKieuMaGiamGia() {
        return KIEU_MA_GIAM_GIA.equals(kieuKM);
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

    @Override
    public String toString() {
        return moTa != null ? moTa : maKM;
    }
}