package com.thefourrestaurant.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class    PhieuDatBan {
    private String maPDB;
    private LocalDateTime ngayTao;
    private LocalDate ngayDat;
    private int soNguoi;
    private KhachHang khachHang;
    private NhanVien nhanVien;
    private String trangThai;
    private boolean isDeleted;
    private List<ChiTietPDB> chiTietPDB = new ArrayList<>();
    private List<Ban> danhSachBan =  new ArrayList<>();

    public PhieuDatBan() {}

    public PhieuDatBan(String maPDB) {
        this.maPDB = maPDB;
    }

    public PhieuDatBan(String maPDB, LocalDate ngayDat, LocalDateTime ngayTao, int soNguoi,
                       KhachHang khachHang, NhanVien nhanVien, String trangThai, boolean isDeleted) {
        this.maPDB = maPDB;
        this.ngayDat = ngayDat;
        this.ngayTao = ngayTao;
        this.soNguoi = soNguoi;
        this.khachHang = khachHang;
        this.nhanVien = nhanVien;
        this.trangThai = trangThai;
        this.isDeleted = isDeleted;
    }

    public List<ChiTietPDB> getChiTietPDB() { return chiTietPDB; }
    public void setChiTietPDB(List<ChiTietPDB> chiTietPDB) { this.chiTietPDB = chiTietPDB; }

    public List<Ban> getDanhSachBan() { return danhSachBan; }
    public void setDanhSachBan(List<Ban> danhSachBan) { this.danhSachBan = danhSachBan; }

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

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }
}
