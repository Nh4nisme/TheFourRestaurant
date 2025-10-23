package com.thefourrestaurant.view.hoadon;

import com.thefourrestaurant.DAO.HoaDonDAO;
import com.thefourrestaurant.model.HoaDon;
import com.thefourrestaurant.view.components.GiaoDienThucThe;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
