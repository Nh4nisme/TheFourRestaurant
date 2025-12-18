package com.thefourrestaurant.model;

/**
 * Lớp này đại diện cho một món ăn được tặng miễn phí khi một điều kiện khuyến mãi được thỏa mãn.
 */
public class DieuKien_MonTang {
    private KhuyenMai_DieuKien dieuKien; // Điều kiện cha
    private MonAn monAnTang; // Món ăn được tặng

    public DieuKien_MonTang() {}

    // Getters and Setters
    public KhuyenMai_DieuKien getDieuKien() {
        return dieuKien;
    }

    public void setDieuKien(KhuyenMai_DieuKien dieuKien) {
        this.dieuKien = dieuKien;
    }

    public MonAn getMonAnTang() {
        return monAnTang;
    }

    public void setMonAnTang(MonAn monAnTang) {
        this.monAnTang = monAnTang;
    }
}
