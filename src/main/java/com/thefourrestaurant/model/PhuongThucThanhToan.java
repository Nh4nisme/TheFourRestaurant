package com.thefourrestaurant.model;

public class PhuongThucThanhToan {
    private String maPTTT;
    private LoaiPTTT loaiPTTT;
    private String moTa;

    public enum LoaiPTTT {
        TIEN_MAT("Tiền mặt"),
        CHUYEN_KHOAN("Chuyển khoản");

        private final String tenHienThi;

        LoaiPTTT(String tenHienThi) {
            this.tenHienThi = tenHienThi;
        }

        public String getTenHienThi() {
            return tenHienThi;
        }

        @Override
        public String toString() {
            return tenHienThi;
        }
    }

    public PhuongThucThanhToan(String maPTTT, LoaiPTTT loaiPTTT, String moTa) {
        setMaPTTT(maPTTT);
        setLoaiPTTT(loaiPTTT);
        setMoTa(moTa);
    }

    public PhuongThucThanhToan(String maPTTT, LoaiPTTT loaiPTTT) {
        setMaPTTT(maPTTT);
        setLoaiPTTT(loaiPTTT);
    }

    public String getMaPTTT() {
        return maPTTT;
    }

    public void setMaPTTT(String maPTTT) {
        this.maPTTT = maPTTT;
    }

    public LoaiPTTT getLoaiPTTT() {
        return loaiPTTT;
    }

    public void setLoaiPTTT(LoaiPTTT loaiPTTT) {
        this.loaiPTTT = loaiPTTT;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
}
