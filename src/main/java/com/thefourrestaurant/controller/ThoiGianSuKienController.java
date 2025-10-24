package com.thefourrestaurant.controller;

import java.util.Map;

import com.thefourrestaurant.view.thoigiansukien.ThoiGianSuKienDialog;

public class ThoiGianSuKienController {

    public Map<String, Object> themMoiSuKien() {
        ThoiGianSuKienDialog themSuKienView = new ThoiGianSuKienDialog(null);
        themSuKienView.showAndWait();
        return themSuKienView.layKetQua();
    }

    public Map<String, Object> tuyChinhSuKien(Map<String, Object> suKien) {
        ThoiGianSuKienDialog tuyChinhView = new ThoiGianSuKienDialog(suKien);
        tuyChinhView.showAndWait();
        return tuyChinhView.layKetQua();
    }
}
