package com.thefourrestaurant.controller;

import java.util.Map;

import com.thefourrestaurant.view.monan.MonAnThemMon;

public class MonAnController {

    public Map<String, Object> themMoiMonAn() {
        MonAnThemMon themMonView = new MonAnThemMon();
        themMonView.showAndWait();
        return themMonView.layKetQua();
    }
}
