package com.thefourrestaurant.view.hoadon;

import com.thefourrestaurant.model.HoaDon;
import com.thefourrestaurant.view.components.GiaoDienThucThe;

import javafx.scene.control.TableView;

public class    GiaoDienHoaDon extends GiaoDienThucThe {
    public GiaoDienHoaDon() {
        super("Hóa đơn", new GiaoDienChiTietHoaDon());
    }

    @Override
    protected TableView<?> taoBangChinh() {
        TableView<HoaDon> table = new TableView<>();




        return table;
    }
}
