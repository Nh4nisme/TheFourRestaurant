package com.thefourrestaurant.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PhieuDatBan {
    private String maPDB;
    private LocalDateTime ngayTao;
    private LocalDate ngayDat;
    private int soNguoi;
    private KhachHang khachHang;
    private NhanVien nhanVien;
    private boolean isDeleted;

    public PhieuDatBan() {}

    public PhieuDatBan(String maPDB) {
        setMaPDB(maPDB);
    }

    public PhieuDatBan(String maPDB, LocalDate ngayDat, LocalDateTime ngayTao, int soNguoi,
                       KhachHang khachHang, NhanVien nhanVien, boolean isDeleted) {
        setMaPDB(maPDB);
        setNgayDat(ngayDat);
        setNgayTao(ngayTao);
        setSoNguoi(soNguoi);
        setKhachHang(khachHang);
        setNhanVien(nhanVien);
        setDeleted(isDeleted);
    }

    public String getMaPDB() { return maPDB; }
    public void setMaPDB(String maPDB) { this.maPDB = maPDB; }

    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }

    public LocalDate getNgayDat() { return ngayDat; }
    public void setNgayDat(LocalDate ngayDat) { this.ngayDat = ngayDat; }

    public int getSoNguoi() { return soNguoi; }
    public void setSoNguoi(int soNguoi) { this.soNguoi = soNguoi; }

    public KhachHang getKhachHang() { return khachHang; }
    public void setKhachHang(KhachHang khachHang) { this.khachHang = khachHang; }

    public NhanVien getNhanVien() { return nhanVien; }
    public void setNhanVien(NhanVien nhanVien) { this.nhanVien = nhanVien; }

    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }
}
