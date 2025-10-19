package com.thefourrestaurant.model;

public class TaiKhoan {
    private String maTK;
    private String tenDN;
    private String matKhau;
    private String vaiTro;

    public TaiKhoan(String maTK, String tenDN, String matKhau, String vaiTro) {
        setMaTK(maTK);
        setTenDN(tenDN);
        setMatKhau(matKhau);
        setVaiTro(vaiTro);
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public String getMaTK() {
        return maTK;
    }

    public void setMaTK(String maTK) {
        this.maTK = maTK;
    }

    public String getTenDN() {
        return tenDN;
    }

    public void setTenDN(String tenDN) {
        this.tenDN = tenDN;
    }
}
