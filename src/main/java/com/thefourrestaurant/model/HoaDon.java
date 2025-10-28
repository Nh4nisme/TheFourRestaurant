package com.thefourrestaurant.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HoaDon {
    private String maHD;
    private LocalDateTime ngayLap;
    private NhanVien nhanVien;
    private KhachHang khachHang;
    private PhieuDatBan phieuDatBan;
    private KhuyenMai khuyenMai;
    private Thue thue;
    private BigDecimal tienKhachDua;
    private BigDecimal tienThua;
    private PhuongThucThanhToan phuongThucThanhToan;
    private boolean isDeleted;

    private List<ChiTietHoaDon> chiTietHoaDon = new ArrayList<>();

    public HoaDon() {}

    public HoaDon(String maHD, LocalDateTime ngayLap, NhanVien nhanVien, KhachHang khachHang, PhieuDatBan phieuDatBan, KhuyenMai khuyenMai, Thue thue, BigDecimal tienKhachDua, BigDecimal tienThua, PhuongThucThanhToan phuongThucThanhToan, boolean isDeleted) {
        setMaHD(maHD);
        setNgayLap(ngayLap);
        setNhanVien(nhanVien);
        setKhachHang(khachHang);
        setPhieuDatBan(phieuDatBan);
        setKhuyenMai(khuyenMai);
        setThue(thue);
        setTienKhachDua(tienKhachDua);
        setTienThua(tienThua);
        setPhuongThucThanhToan(phuongThucThanhToan);
        setDeleted(isDeleted);
    }

    public List<ChiTietHoaDon> getChiTietHoaDon() {return chiTietHoaDon;}
    public void setChiTietHoaDon(List<ChiTietHoaDon> chiTietHoaDon) {
        this.chiTietHoaDon = chiTietHoaDon;
    }

    public BigDecimal getTongTienGoc() {
        // Tổng tiền trước KM + thuế
        if (chiTietHoaDon == null || chiTietHoaDon.isEmpty()) return BigDecimal.ZERO;

        BigDecimal tong = BigDecimal.ZERO;
        for (ChiTietHoaDon c : chiTietHoaDon) {
            tong = tong.add(c.getThanhTien()); // donGia * soLuong
        }

        return tong.setScale(0, RoundingMode.HALF_UP);
    }

    public BigDecimal getTongTien() {
        if (chiTietHoaDon == null || chiTietHoaDon.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal tong = BigDecimal.ZERO;
        for (ChiTietHoaDon c : chiTietHoaDon) {
            tong = tong.add(c.getThanhTien());
        }

        if (khuyenMai != null && khuyenMai.getLoaiKhuyenMai() != null) {
            String loaiKM = khuyenMai.getLoaiKhuyenMai().getTenLoaiKM();

            if (loaiKM.equalsIgnoreCase("Giảm giá theo tỷ lệ") && khuyenMai.getTyLe() != null) {
                BigDecimal tyLeKM = khuyenMai.getTyLe().divide(BigDecimal.valueOf(100));
                tong = tong.subtract(tong.multiply(tyLeKM));

            } else if (loaiKM.equalsIgnoreCase("Giảm giá theo số tiền") && khuyenMai.getSoTien() != null) {
                tong = tong.subtract(khuyenMai.getSoTien());

            } else if (loaiKM.equalsIgnoreCase("Tặng món")) {
                // Không trừ tiền, chỉ hiển thị bên UI
            }
        }

        if (thue != null) {
            BigDecimal tyLeThue = BigDecimal.valueOf(thue.getTyLe()).divide(BigDecimal.valueOf(100));
            tong = tong.add(tong.multiply(tyLeThue));
        }

        tong = tong.setScale(0, RoundingMode.HALF_UP);

        return tong.max(BigDecimal.ZERO);
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

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public PhieuDatBan getPhieuDatBan() {
        return phieuDatBan;
    }

    public void setPhieuDatBan(PhieuDatBan phieuDatBan) {
        this.phieuDatBan = phieuDatBan;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
    }

    public Thue getThue() {
        return thue;
    }

    public void setThue(Thue thue) {
        this.thue = thue;
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

    public PhuongThucThanhToan getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(PhuongThucThanhToan phuongThucThanhToan) {
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
