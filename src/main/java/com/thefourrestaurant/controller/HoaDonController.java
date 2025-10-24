package com.thefourrestaurant.controller;

import com.thefourrestaurant.DAO.HoaDonDAO;
import com.thefourrestaurant.model.HoaDon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class HoaDonController {
    private final HoaDonDAO hoaDonDAO;

    public HoaDonController() {
        this.hoaDonDAO = new HoaDonDAO();
    }

    public ObservableList<HoaDon> layDanhSachHoaDon() {
        List<HoaDon> danhSach = hoaDonDAO.layTatCaHoaDon();
        return FXCollections.observableArrayList(danhSach);
    }
}
