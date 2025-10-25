package com.thefourrestaurant.model;

public class KhungGio_KM {
    private String maTG;
    private String maKM;

    public KhungGio_KM() {}

    public KhungGio_KM(String maTG, String maKM) {
        this.maTG = maTG;
        this.maKM = maKM;
    }

    public String getMaTG() {
        return maTG;
    }

    public void setMaTG(String maTG) {
        this.maTG = maTG;
    }

    public String getMaKM() {
        return maKM;
    }

    public void setMaKM(String maKM) {
        this.maKM = maKM;
    }
}
