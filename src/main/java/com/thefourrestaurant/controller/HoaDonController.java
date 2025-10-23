package com.thefourrestaurant.controller;

import com.thefourrestaurant.DAO.HoaDonDAO;
import com.thefourrestaurant.model.HoaDon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class HoaDonController {
    private HoaDonDAO hoaDonDAO  = new HoaDonDAO();

    public ObservableList<HoaDon> layDanhSachHoaDon(){
        return FXCollections.observableArrayList(hoaDonDAO.getAll());
    }
}
