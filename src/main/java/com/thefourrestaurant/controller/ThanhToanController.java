package com.thefourrestaurant.controller;

import com.thefourrestaurant.model.PhieuDatBan;
import com.thefourrestaurant.view.hoadon.GiaoDienLapHoaDon;
import javafx.stage.Stage;

public class ThanhToanController {

    public void moManThanhToan(PhieuDatBan pdb) {
        if (pdb == null) return;

        Stage stage = new Stage();
        GiaoDienLapHoaDon gd = new GiaoDienLapHoaDon(stage);
        gd.hienThiThongTin(pdb);
    }
}
