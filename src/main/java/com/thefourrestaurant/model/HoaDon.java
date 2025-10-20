package com.thefourrestaurant.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class HoaDon {
    private String maHD;
    private LocalDateTime ngayLap;
    private String maNV;
    private String maKH;
    private String maPDB;
    private String maKM;
    private String maThue;
    private BigDecimal tienKhachDua;
    private BigDecimal tienThua;
    private String maPTTT;
    private List<ChiTietHoaDon> chiTietHDs;

    public HoaDon(String maHD, LocalDateTime ngayLap, String maNV, String maKH, String maPDB, String maKM, String maThue, BigDecimal tienKhachDua, BigDecimal tienThua, String maPTTT,  List<ChiTietHoaDon> chiTietHDs) {
        setMaHD(maHD);
        setNgayLap(ngayLap);
        setMaNV(maNV);
        setMaKH(maKH);
        setMaPDB(maPDB);
        setMaKM(maKM);
        setMaThue(maThue);
        setTienKhachDua(tienKhachDua);
        setTienThua(tienThua);
        setMaPTTT(maPTTT);
        setChiTietHDs(chiTietHDs);
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public LocalDateTime getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDateTime ngayLap) {
        this.ngayLap = ngayLap;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getMaPDB() {
        return maPDB;
    }

    public void setMaPDB(String maPDB) {
        this.maPDB = maPDB;
    }

    public String getMaKM() {
        return maKM;
    }

    public void setMaKM(String maKM) {
        this.maKM = maKM;
    }

    public String getMaThue() {
        return maThue;
    }

    public void setMaThue(String maThue) {
        this.maThue = maThue;
    }

    public BigDecimal getTienKhachDua() {
        return tienKhachDua;
    }

    public void setTienKhachDua(BigDecimal tienKhachDua) {
        this.tienKhachDua = tienKhachDua;
    }

    public BigDecimal getTienThua() {
        return tienThua;
    }

    public void setTienThua(BigDecimal tienThua) {
        this.tienThua = tienThua;
    }

    public String getMaPTTT() {
        return maPTTT;
    }

    public void setMaPTTT(String maPTTT) {
        this.maPTTT = maPTTT;
    }

    public List<ChiTietHoaDon> getChiTietHDs() {
        return chiTietHDs;
    }

    public void setChiTietHDs(List<ChiTietHoaDon> chiTietHDs) {
        this.chiTietHDs = chiTietHDs;
    }
}
