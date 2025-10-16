package com.thefourrestaurant.controller;

import com.thefourrestaurant.view.monan.MonAnThemMon;

import java.util.Map;

public class MonAnController {

    public Map<String, Object> themMoiMonAn() {
        MonAnThemMon themMonView = new MonAnThemMon();
        themMonView.showAndWait();
        return themMonView.layKetQua();
    }
}
