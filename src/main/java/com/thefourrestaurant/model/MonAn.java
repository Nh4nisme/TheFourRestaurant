package com.thefourrestaurant.model;

import com.thefourrestaurant.view.loaimonan.LoaiMonAn;

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
    private Boolean isDeleted;

    public MonAn() {}

    public MonAn(String maMonAn, String tenMon, BigDecimal donGia, String trangThai, LoaiMon loaiMon, String hinhAnh, Boolean isDeleted) {
        setMaMonAn(maMonAn);
        setTenMon(tenMon);
        setDonGia(donGia);
        setTrangThai(trangThai);
        setLoaiMon(loaiMon);
        setHinhAnh(hinhAnh);
        setDeleted(isDeleted);
    }

    public MonAn(String maMonAn) {
        this.maMonAn = maMonAn;
    }

    public MonAn(String maMonAn, String tenMon, BigDecimal donGia, String trangThai, LoaiMonAn loaiMon, String hinhAnh, boolean isDeleted) {
    }


    public String getMaMonAn() { return maMonAn; }
    public void setMaMonAn(String maMonAn) { this.maMonAn = maMonAn; }

    public String getTenMon() { return tenMon; }
    public void setTenMon(String tenMon) { this.tenMon = tenMon; }

    public BigDecimal getDonGia() { return donGia.setScale(0, RoundingMode.HALF_UP); }
    public void setDonGia(BigDecimal donGia) { this.donGia = donGia; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public LoaiMon getLoaiMon() { return loaiMon; }
    public void setLoaiMon(LoaiMon loaiMon) { this.loaiMon = loaiMon; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public Boolean getDeleted() {return isDeleted;}
    public void setDeleted(Boolean deleted) {isDeleted = deleted;}

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
