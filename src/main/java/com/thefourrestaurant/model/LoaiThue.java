package com.thefourrestaurant.model;

public class LoaiThue {
    private String maLoaiThue;
    private String tenLoaiThue;

    public LoaiThue(String maLoaiThue, String tenLoaiThue) {
        setMaLoaiThue(maLoaiThue);
        setTenLoaiThue(tenLoaiThue);
    }

    public String getMaLoaiThue() {
        return maLoaiThue;
    }

    public void setMaLoaiThue(String maLoaiThue) {
        this.maLoaiThue = maLoaiThue;
    }

    public String getTenLoaiThue() {
        return tenLoaiThue;
    }

    public void setTenLoaiThue(String tenLoaiThue) {
        this.tenLoaiThue = tenLoaiThue;
    }
}
