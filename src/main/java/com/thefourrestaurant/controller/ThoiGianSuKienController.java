package com.thefourrestaurant.controller;

import com.thefourrestaurant.view.thoigiansukien.ThoiGianSuKienThem;

import java.util.Map;

public class ThoiGianSuKienController {

    public Map<String, Object> themMoiSuKien() {
        ThoiGianSuKienThem themSuKienView = new ThoiGianSuKienThem();
        themSuKienView.showAndWait();
        return themSuKienView.layKetQua();
    }
}
