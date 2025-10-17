package com.thefourrestaurant.controller;

import com.thefourrestaurant.view.loaimonan.LoaiMonAnThemLoai;

import java.util.Map;

public class LoaiMonAnController {

    public Map<String, Object> themMoiLoaiMonAn() {
        LoaiMonAnThemLoai themLoaiView = new LoaiMonAnThemLoai();
        themLoaiView.showAndWait();
        return themLoaiView.layKetQua();
    }
}
