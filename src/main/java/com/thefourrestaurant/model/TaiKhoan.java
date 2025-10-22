package com.thefourrestaurant.model;

public class TaiKhoan {
    private String maTK;
    private String tenDN;
    private String matKhau;
    private VaiTro vaiTro;
    private Boolean isDeleted;

    public TaiKhoan() {}

    public TaiKhoan(String maTK, String tenDN, String matKhau, VaiTro vaiTro, boolean isDeleted) {
        setMaTK(maTK);
        setTenDN(tenDN);
        setMatKhau(matKhau);
        setVaiTro(vaiTro);
        setDeleted(isDeleted);
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

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public VaiTro getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(VaiTro vaiTro) {
        this.vaiTro = vaiTro;
    }

    public Boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
