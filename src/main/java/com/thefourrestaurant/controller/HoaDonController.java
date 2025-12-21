package com.thefourrestaurant.controller;

import com.thefourrestaurant.DAO.ChiTietHoaDonDAO;
import com.thefourrestaurant.DAO.ChiTietPDBDAO;
import com.thefourrestaurant.DAO.HoaDonDAO;
import com.thefourrestaurant.DAO.KhachHangDAO;
import com.thefourrestaurant.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HoaDonController {
    private HoaDonDAO hoaDonDAO  = new HoaDonDAO();
    private ChiTietHoaDonDAO chiTietHoaDonDAO = new ChiTietHoaDonDAO();
    private final ChiTietPDBDAO chiTietDAO = new ChiTietPDBDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();

    private static final BigDecimal MOC_VIP = new BigDecimal("10000000");
    private static final String MA_LOAI_VIP = "LKH00002";

    public ObservableList<HoaDon> layDanhSachHoaDon(){
        return FXCollections.observableArrayList(hoaDonDAO.layDanhSachHoaDon());
    }

    public List<ChiTietPDB> layChiTietHienThi(String maPDB) {
        List<ChiTietPDB> danhSachGoc = chiTietDAO.layTheoPhieu(maPDB);
        return gopChiTietPhieu(danhSachGoc);
    }

    public List<ChiTietHoaDon> layCTHDTheoMa(String maHD){
        return chiTietHoaDonDAO.layCTHDTheoMa(maHD);
    }

    public boolean themHoaDon(HoaDon hoaDon){
        return hoaDonDAO.themHoaDon(hoaDon);
    }

    public String taoMaHD() {
        return hoaDonDAO.taoMaHDMoi();
    }

    public boolean xoaHoaDon(String maHD) {return hoaDonDAO.xoaHoaDon(maHD);}

    private List<ChiTietPDB> gopChiTietPhieu(List<ChiTietPDB> danhSachGoc) {
        Map<String, ChiTietPDB> danhSachGop = new LinkedHashMap<>();

        for (ChiTietPDB ct : danhSachGoc) {
            String khoa = ct.getMonAn() != null
                    ? ct.getMonAn().getMaMonAn()
                    : ct.getMaCT();

            if (!danhSachGop.containsKey(khoa)) {
                ChiTietPDB banSao = new ChiTietPDB(
                        ct.getMaCT(),
                        ct.getPhieuDatBan(),
                        ct.getMonAn(),
                        ct.getSoLuong(),
                        ct.getDonGia(),
                        ct.getGhiChu()
                );
                danhSachGop.put(khoa, banSao);
            } else {
                ChiTietPDB daCo = danhSachGop.get(khoa);
                daCo.setSoLuong(daCo.getSoLuong() + ct.getSoLuong());
            }
        }
        return new ArrayList<>(danhSachGop.values());
    }

    public void xuLyVIPSauThanhToan(HoaDon hoaDonMoi) {

        KhachHang kh = hoaDonMoi.getKhachHang();
        if (kh == null) return;

        if (kh.getLoaiKH() != null
                && MA_LOAI_VIP.equals(kh.getLoaiKH().getMaLoaiKH())) {
            return;
        }

        BigDecimal tongCu =
                hoaDonDAO.tongTienThuanTheoKhach(kh.getMaKH());

        BigDecimal tongSau =
                tongCu.add(hoaDonMoi.getTongTien());

        if (tongSau.compareTo(MOC_VIP) >= 0) {

            khachHangDAO.capNhatLoaiKhachHang(
                    kh.getMaKH(),
                    MA_LOAI_VIP
            );

            LoaiKhachHang vip = new LoaiKhachHang(MA_LOAI_VIP);
            vip.setTenLoaiKH("VIP"); // optional

            kh.setLoaiKH(vip);
        }
    }

}

