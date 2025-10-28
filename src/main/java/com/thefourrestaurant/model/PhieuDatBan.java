package com.thefourrestaurant.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PhieuDatBan {
    private String maPDB;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayDat;
    private int soNguoi;
    private KhachHang khachHang;
    private NhanVien nhanVien;
    private Ban ban;
    private String trangThai;
    private boolean isDeleted;
    private List<ChiTietPDB> chiTietPDB = new ArrayList<>();

    public PhieuDatBan() {}

    public PhieuDatBan(String maPDB) {
        this.maPDB = maPDB;
    }

    public PhieuDatBan(String maPDB, LocalDateTime ngayDat, LocalDateTime ngayTao, int soNguoi,
                       KhachHang khachHang, NhanVien nhanVien, Ban ban, String trangThai, boolean isDeleted) {
        this.maPDB = maPDB;
        this.ngayDat = ngayDat;
        this.ngayTao = ngayTao;
        this.soNguoi = soNguoi;
        this.khachHang = khachHang;
        this.nhanVien = nhanVien;
        this.ban = ban;
        this.trangThai = trangThai;
        this.isDeleted = isDeleted;
    }

    public String getMaPDB() { return maPDB; }
    public void setMaPDB(String maPDB) { this.maPDB = maPDB; }

    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }

    public LocalDateTime getNgayDat() { return ngayDat; }
    public void setNgayDat(LocalDateTime ngayDat) { this.ngayDat = ngayDat; }

    public int getSoNguoi() { return soNguoi; }
    public void setSoNguoi(int soNguoi) { this.soNguoi = soNguoi; }

    public KhachHang getKhachHang() { return khachHang; }
    public void setKhachHang(KhachHang khachHang) { this.khachHang = khachHang; }

    public NhanVien getNhanVien() { return nhanVien; }
    public void setNhanVien(NhanVien nhanVien) { this.nhanVien = nhanVien; }

    public Ban getBan() { return ban; }
    public void setBan(Ban ban) { this.ban = ban; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }
    
    public List<ChiTietPDB> getChiTietPDB() {
        return chiTietPDB;
    }

    public void setChiTietPDB(List<ChiTietPDB> chiTietPDB) {
        this.chiTietPDB = chiTietPDB;
    }

    @Override
    public String toString() {
        return "PhieuDatBan{" +
               "maPDB='" + maPDB + '\'' +
               ", ngayDat=" + ngayDat +
               ", ban=" + (ban != null ? ban.getMaBan() : "null") +
               ", trangThai='" + trangThai + '\'' +
               '}';
    }
}
