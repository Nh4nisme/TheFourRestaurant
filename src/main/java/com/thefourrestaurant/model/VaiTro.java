package com.thefourrestaurant.model;

public class VaiTro {
    private String maVT;
    private String tenVaiTro;

    public VaiTro(String maVT, String tenVaiTro) {
        setMaVT(maVT);
        setTenVaiTro(tenVaiTro);
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
}
