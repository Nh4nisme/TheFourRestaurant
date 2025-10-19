package com.thefourrestaurant.controller;

import java.util.Map;

import com.thefourrestaurant.view.monan.MonAnDialog;

public class MonAnController {

    public Map<String, Object> themMoiMonAn() {
        MonAnDialog themMonView = new MonAnDialog(null);
        themMonView.showAndWait();
        return themMonView.layKetQua();
    }
}
