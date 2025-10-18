package com.thefourrestaurant.controller;

import java.util.Map;

import com.thefourrestaurant.view.loaimonan.LoaiMonAnThemLoai;
import com.thefourrestaurant.view.loaimonan.LoaiMonAnTuyChinh;

public class LoaiMonAnController {

    public Map<String, Object> themMoiLoaiMonAn() {
        LoaiMonAnThemLoai themLoaiView = new LoaiMonAnThemLoai();
        themLoaiView.showAndWait();
        return themLoaiView.layKetQua();
    }

    public Map<String, Object> tuyChinhLoaiMonAn() {
        LoaiMonAnTuyChinh tuyChinhView = new LoaiMonAnTuyChinh();
        tuyChinhView.showAndWait();
        return tuyChinhView.layKetQua();
    }
}
