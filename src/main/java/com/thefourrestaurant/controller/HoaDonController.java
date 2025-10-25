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
        return FXCollections.observableArrayList(hoaDonDAO.getAll());
    }

    public List<ChiTietHoaDon> layCTHDTheoMa(String maHD){
        return chiTietHoaDonDAO.layCTHDTheoMa(maHD);
    }
}

