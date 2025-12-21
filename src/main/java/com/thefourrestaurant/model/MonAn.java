package com.thefourrestaurant.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class MonAn {
    private String maMonAn;
    private String tenMon;
    private BigDecimal donGia;
    private String trangThai;
    private LoaiMon loaiMon;
    private String hinhAnh;
    private int soLuong;
    private int daBan;
    private Boolean isDeleted;
    private Boolean isVisible;
    private BigDecimal giaSauGiam;
    private String tenKhuyenMai;

    public MonAn() {
    }

    public MonAn(String maMonAn) {
        this.maMonAn = maMonAn;
    }

    public String getMaMonAn() {
        return maMonAn;
    }

    public void setMaMonAn(String maMonAn) {
        this.maMonAn = maMonAn;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public BigDecimal getDonGia() {
        return donGia != null ? donGia.setScale(0, RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }

    public BigDecimal getRawDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public LoaiMon getLoaiMon() {
        return loaiMon;
    }

    public void setLoaiMon(LoaiMon loaiMon) {
        this.loaiMon = loaiMon;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
        capNhatTrangThaiTheoSoLuong();
    }

    public void capNhatTrangThaiTheoSoLuong() {
        if (this.soLuong == 0) {
            this.trangThai = "Hết";
        } else {
            this.trangThai = "Còn";
        }
    }

    public int getDaBan() {
        return daBan;
    }

    public void setDaBan(int daBan) {
        this.daBan = daBan;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Boolean getVisible() {
        return isVisible;
    }

    public void setVisible(Boolean visible) {
        this.isVisible = visible;
    }

    public BigDecimal getGiaSauGiam() {
        return giaSauGiam != null ? giaSauGiam.setScale(0, RoundingMode.HALF_UP) : getDonGia();
    }

    public void setGiaSauGiam(BigDecimal giaSauGiam) {
        this.giaSauGiam = giaSauGiam;
    }

    public String getTenKhuyenMai() {
        return tenKhuyenMai;
    }

    public void setTenKhuyenMai(String tenKhuyenMai) {
        this.tenKhuyenMai = tenKhuyenMai;
    }

    @Override
    public String toString() {
        return tenMon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonAn monAn = (MonAn) o;
        return Objects.equals(maMonAn, monAn.maMonAn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maMonAn);
    }
}
