package com.thefourrestaurant.model;

import java.math.BigDecimal;

public class KhuyenMaiApDung {
    private String tenKhuyenMai;
    private BigDecimal giaSauGiam;

    public KhuyenMaiApDung(String tenKhuyenMai, BigDecimal giaSauGiam) {
        this.tenKhuyenMai = tenKhuyenMai;
        this.giaSauGiam = giaSauGiam;
    }

    public String getTenKhuyenMai() {
        return tenKhuyenMai;
    }

    public BigDecimal getGiaSauGiam() {
        return giaSauGiam;
    }
}
