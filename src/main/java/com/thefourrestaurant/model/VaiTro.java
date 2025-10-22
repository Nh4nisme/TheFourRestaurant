package com.thefourrestaurant.model;

public class VaiTro {
    private String maVT;
    private String tenVaiTro;
    private boolean isDeleted;

    public VaiTro(){}

    public VaiTro(String maVT, String tenVaiTro,  boolean isDeleted) {
        setMaVT(maVT);
        setTenVaiTro(tenVaiTro);
        this.isDeleted = isDeleted;
    }

    public String getTenVaiTro() {
        return tenVaiTro;
    }

    public void setTenVaiTro(String tenVaiTro) {
        this.tenVaiTro = tenVaiTro;
    }

    public String getMaVT() {
        return maVT;
    }

    public void setMaVT(String maVT) {
        this.maVT = maVT;
    }

    public boolean isDeleted() {
        return isDeleted;
    }
    public void setDeleted(boolean deleted) {}
}
