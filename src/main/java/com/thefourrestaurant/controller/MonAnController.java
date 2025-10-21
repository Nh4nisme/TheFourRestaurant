package com.thefourrestaurant.controller;

import com.thefourrestaurant.DAO.KhuyenMaiDAO;
import com.thefourrestaurant.DAO.LoaiMonAnDAO;
import com.thefourrestaurant.DAO.MonAnDAO;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.model.LoaiMon;
import com.thefourrestaurant.model.MonAn;
import com.thefourrestaurant.view.monan.MonAnDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.List;
import java.util.Optional;

public class MonAnController {

    private final MonAnDAO monAnDAO;
    private final LoaiMonAnDAO loaiMonAnDAO;
    private final KhuyenMaiDAO khuyenMaiDAO; // Added DAO for promotions

    public MonAnController() {
        this.monAnDAO = new MonAnDAO();
        this.loaiMonAnDAO = new LoaiMonAnDAO();
        this.khuyenMaiDAO = new KhuyenMaiDAO(); // Instantiate it
    }

    public List<MonAn> getMonAnByLoai(String maLoaiMon) {
        return monAnDAO.getMonAnByLoai(maLoaiMon);
    }

    public List<LoaiMon> getAllLoaiMonAn() {
        return loaiMonAnDAO.getAllLoaiMonAn();
    }

    public List<KhuyenMai> getAllKhuyenMai() {
        return khuyenMaiDAO.getAllKhuyenMai();
    }

    public boolean themMoiMonAn(String maLoaiMonDefault) {
        List<LoaiMon> allLoaiMon = getAllLoaiMonAn();
        List<KhuyenMai> allKhuyenMai = getAllKhuyenMai(); // Get promotions

        if (allLoaiMon.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Không có loại món ăn nào trong CSDL. Vui lòng thêm loại món ăn trước.").showAndWait();
            return false;
        }
        // Pass promotions to the dialog
        MonAnDialog dialog = new MonAnDialog(null, allLoaiMon, maLoaiMonDefault, allKhuyenMai);
        dialog.showAndWait();

        MonAn ketQua = dialog.layKetQua();
        if (ketQua != null) {
            ketQua.setMaMonAn(monAnDAO.generateNewMaMonAn());
            return monAnDAO.addMonAn(ketQua);
        }
        return false;
    }

    public boolean tuyChinhMonAn(MonAn monAn) {
        List<LoaiMon> allLoaiMon = getAllLoaiMonAn();
        List<KhuyenMai> allKhuyenMai = getAllKhuyenMai(); // Get promotions

        // Pass promotions to the dialog
        MonAnDialog dialog = new MonAnDialog(monAn, allLoaiMon, monAn.getMaLoaiMon(), allKhuyenMai);
        dialog.showAndWait();

        MonAn ketQua = dialog.layKetQua();
        if (ketQua != null) {
            return monAnDAO.updateMonAn(ketQua);
        }
        return false;
    }

    public boolean xoaMonAn(MonAn monAn) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xóa");
        confirmAlert.setHeaderText("Bạn có chắc chắn muốn xóa món: " + monAn.getTenMon() + "?");
        confirmAlert.setContentText("Hành động này không thể hoàn tác.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            return monAnDAO.deleteMonAn(monAn.getMaMonAn());
        }
        return false;
    }
}
