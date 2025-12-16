package com.thefourrestaurant.controller;

import com.thefourrestaurant.DAO.PhieuDatBanDAO;
import com.thefourrestaurant.model.PhieuDatBan;
import com.thefourrestaurant.view.hoadon.GiaoDienLapHoaDon;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class ThanhToanController {

    private final PhieuDatBanDAO phieuDatBanDAO = new PhieuDatBanDAO();

    public void moManThanhToan(PhieuDatBan pdb) {
        if (pdb == null) return;

        Stage stage = new Stage();
        GiaoDienLapHoaDon gd = new GiaoDienLapHoaDon(stage);
        gd.hienThiThongTin(pdb);
    }

    public void moManThanhToanTheoMaBan(String maBan) {
        PhieuDatBan pdb = phieuDatBanDAO.layPhieuDangHoatDongTheoBan(maBan);
        if (pdb == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Bàn chưa có phiếu hoạt động");
            alert.showAndWait();
            return;
        }
        moManThanhToan(pdb);
    }
}
