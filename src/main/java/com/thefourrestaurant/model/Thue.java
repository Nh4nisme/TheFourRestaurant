package com.thefourrestaurant.model;

public class Thue {
    private String maThue;
    private int tyLe;
    private String ghiChu;
    private LoaiThue loaiThue;

    public Thue(String maThue, int tyLe, String ghiChu, LoaiThue loaiThue) {
        setMaThue(maThue);
        setTyLe(tyLe);
        setGhiChu(ghiChu);
        setLoaiThue(loaiThue);
    }

    public String getMaThue() {
        return maThue;
    }

    public void setMaThue(String maThue) {
        this.maThue = maThue;
    }

    public int getTyLe() {
        return tyLe;
    }

    public void setTyLe(int tyLe) {
        this.tyLe = tyLe;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public LoaiThue getLoaiThue() {
        return loaiThue;
    }

    public void setLoaiThue(LoaiThue loaiThue) {
        this.loaiThue = loaiThue;
    }
}
