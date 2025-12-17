package com.thefourrestaurant.controller;

import com.thefourrestaurant.DAO.PhieuDatBanDAO;
import com.thefourrestaurant.model.PhieuDatBan;
import com.thefourrestaurant.view.hoadon.GiaoDienLapHoaDon;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;

public class ThanhToanController {

    private final PhieuDatBanDAO phieuDatBanDAO = new PhieuDatBanDAO();

    // Mở màn thanh toán từ 1 phiếu đặt bàn
    public void moManThanhToan(PhieuDatBan pdb, StackPane mainContent) {
        if (pdb == null) return;

        Stage stage = new Stage();

        // truyền mainContent xuống
        GiaoDienLapHoaDon gd =
                new GiaoDienLapHoaDon(stage, mainContent);

        gd.hienThiThongTinPhieuDatBan(pdb);
    }

    // Mở màn thanh toán theo mã bàn
    public void moManThanhToanTheoMaBan(String maBan, StackPane mainContent) {
        PhieuDatBan pdb =
                phieuDatBanDAO.layPhieuDangHoatDongTheoBan(maBan);

        if (pdb == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Bàn chưa có phiếu hoạt động");
            alert.showAndWait();
            return;
        }

        moManThanhToan(pdb, mainContent);
    }
}
