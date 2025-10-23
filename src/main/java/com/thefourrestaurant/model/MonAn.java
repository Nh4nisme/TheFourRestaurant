package com.thefourrestaurant.model;

import java.math.BigDecimal;

public class MonAn {
    private String maMonAn;
    private String tenMon;
    private BigDecimal donGia;
    private String trangThai;
    private LoaiMonAn loaiMonAn;
    private String hinhAnh;

    public MonAn() {}

    public MonAn(String maMonAn, String tenMon, BigDecimal donGia, String trangThai,
                 LoaiMonAn loaiMonAn, String hinhAnh) {
        this.maMonAn = maMonAn;
        this.tenMon = tenMon;
        this.donGia = donGia;
        this.trangThai = trangThai;
        this.loaiMonAn = loaiMonAn;
        this.hinhAnh = hinhAnh;
    }

    public MonAn(String maMonAn) {
        this.maMonAn = maMonAn;
    }

    public String getMaMonAn() { return maMonAn; }
    public void setMaMonAn(String maMonAn) { this.maMonAn = maMonAn; }

    public String getTenMon() { return tenMon; }
    public void setTenMon(String tenMon) { this.tenMon = tenMon; }

    public BigDecimal getDonGia() { return donGia; }
    public void setDonGia(BigDecimal donGia) { this.donGia = donGia; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public LoaiMonAn getLoaiMonAn() { return loaiMonAn; }
    public void setLoaiMonAn(LoaiMonAn loaiMonAn) { this.loaiMonAn = loaiMonAn; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    @Override
    public String toString() {
        return tenMon;
    }
}
