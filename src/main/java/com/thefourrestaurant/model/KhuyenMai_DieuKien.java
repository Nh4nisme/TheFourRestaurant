package com.thefourrestaurant.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Đại diện cho một quy tắc hoặc một điều kiện cụ thể trong một chương trình khuyến mãi.
 * Ví dụ: "Giảm 10% cho các món cơm" là một điều kiện.
 * "Mua 1 Pizza tặng 1 Coca" là một điều kiện khác.
 */
public class KhuyenMai_DieuKien {
    private String maDieuKien;
    private KhuyenMai khuyenMai; // Chương trình khuyến mãi cha
    private String loaiApDung; // Phân loại cách áp dụng: GIAM_TRUC_TIEP, THEO_COMBO, MUA_X_GIAM_Y
    private BigDecimal tyLeGiam;
    private BigDecimal soTienGiam;
    private BigDecimal giaToiThieu;
    private Integer soLuongTang;
    private String moTaDieuKien;

    // Danh sách các món liên quan đến điều kiện này (mua, nhận giảm, v.v.)
    private List<DieuKien_Mon> danhSachMonDieuKien = new ArrayList<>();
    // Danh sách các món được tặng miễn phí khi thỏa điều kiện
    private List<DieuKien_MonTang> danhSachMonTang = new ArrayList<>();

    public KhuyenMai_DieuKien() {}

    // Getters and Setters
    public String getMaDieuKien() {
        return maDieuKien;
    }

    public void setMaDieuKien(String maDieuKien) {
        this.maDieuKien = maDieuKien;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
    }

    public String getLoaiApDung() {
        return loaiApDung;
    }

    public void setLoaiApDung(String loaiApDung) {
        this.loaiApDung = loaiApDung;
    }

    public BigDecimal getTyLeGiam() {
        return tyLeGiam;
    }

    public void setTyLeGiam(BigDecimal tyLeGiam) {
        this.tyLeGiam = tyLeGiam;
    }

    public BigDecimal getSoTienGiam() {
        return soTienGiam;
    }

    public void setSoTienGiam(BigDecimal soTienGiam) {
        this.soTienGiam = soTienGiam;
    }

    public BigDecimal getGiaToiThieu() {
        return giaToiThieu;
    }

    public void setGiaToiThieu(BigDecimal giaToiThieu) {
        this.giaToiThieu = giaToiThieu;
    }

    public Integer getSoLuongTang() {
        return soLuongTang;
    }

    public void setSoLuongTang(Integer soLuongTang) {
        this.soLuongTang = soLuongTang;
    }

    public String getMoTaDieuKien() {
        return moTaDieuKien;
    }

    public void setMoTaDieuKien(String moTaDieuKien) {
        this.moTaDieuKien = moTaDieuKien;
    }

    public List<DieuKien_Mon> getDanhSachMonDieuKien() {
        return danhSachMonDieuKien;
    }

    public void setDanhSachMonDieuKien(List<DieuKien_Mon> danhSachMonDieuKien) {
        this.danhSachMonDieuKien = danhSachMonDieuKien;
    }

    public List<DieuKien_MonTang> getDanhSachMonTang() {
        return danhSachMonTang;
    }

    public void setDanhSachMonTang(List<DieuKien_MonTang> danhSachMonTang) {
        this.danhSachMonTang = danhSachMonTang;
    }
}
