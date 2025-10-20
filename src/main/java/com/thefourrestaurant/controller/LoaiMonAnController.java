package com.thefourrestaurant.controller;

import com.thefourrestaurant.DAO.LoaiMonAnDAO;
import com.thefourrestaurant.model.LoaiMon;
import com.thefourrestaurant.view.loaimonan.LoaiMonAnDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.List;
import java.util.Optional;

public class LoaiMonAnController {

    private final LoaiMonAnDAO loaiMonAnDAO;

    public LoaiMonAnController() {
        this.loaiMonAnDAO = new LoaiMonAnDAO();
    }

    public List<LoaiMon> getAllLoaiMonAn() {
        return loaiMonAnDAO.getAllLoaiMonAn();
    }

    public boolean themMoiLoaiMonAn() {
        LoaiMonAnDialog dialog = new LoaiMonAnDialog(null);
        dialog.showAndWait();

        LoaiMon ketQua = dialog.layKetQua();
        if (ketQua != null) {
            ketQua.setMaLoaiMon(loaiMonAnDAO.generateNewMaLoaiMon());
            return loaiMonAnDAO.addLoaiMonAn(ketQua);
        }
        return false;
    }

    public boolean tuyChinhLoaiMonAn(LoaiMon loaiMon) {
        LoaiMonAnDialog dialog = new LoaiMonAnDialog(loaiMon);
        dialog.showAndWait();

        LoaiMon ketQua = dialog.layKetQua();
        if (ketQua != null) {
            return loaiMonAnDAO.updateLoaiMonAn(ketQua);
        }
        return false;
    }

    public boolean xoaLoaiMonAn(LoaiMon loaiMon) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xóa");
        confirmAlert.setHeaderText("Bạn có chắc chắn muốn xóa loại món ăn: " + loaiMon.getTenLoaiMon() + "?");
        confirmAlert.setContentText("Hành động này không thể hoàn tác.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            return loaiMonAnDAO.deleteLoaiMonAn(loaiMon.getMaLoaiMon());
        }
        return false;
    }
}
