package com.thefourrestaurant.controller;

import com.thefourrestaurant.DAO.ChiTietHoaDonDAO;
import com.thefourrestaurant.DAO.HoaDonDAO;
import com.thefourrestaurant.model.ChiTietHoaDon;
import com.thefourrestaurant.model.HoaDon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class HoaDonController {
    private HoaDonDAO hoaDonDAO  = new HoaDonDAO();
    private ChiTietHoaDonDAO chiTietHoaDonDAO = new ChiTietHoaDonDAO();

    public ObservableList<HoaDon> layDanhSachHoaDon(){
        return FXCollections.observableArrayList(hoaDonDAO.layDanhSachHoaDon());
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
}

