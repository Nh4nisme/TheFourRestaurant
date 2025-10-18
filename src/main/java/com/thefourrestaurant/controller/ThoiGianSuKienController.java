package com.thefourrestaurant.controller;

import java.util.Map;

import com.thefourrestaurant.view.thoigiansukien.ThoiGianSuKienThem;

public class ThoiGianSuKienController {

    public Map<String, Object> themMoiSuKien() {
        ThoiGianSuKienThem themSuKienView = new ThoiGianSuKienThem();
        themSuKienView.showAndWait();
        return themSuKienView.layKetQua();
    }
}
