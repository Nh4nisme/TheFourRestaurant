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

        TableColumn<HoaDon, String> colMaHD =  new TableColumn<>("Mã hóa đơn");
        colMaHD.setCellValueFactory(cell ->
            new SimpleStringProperty(cell.getValue().getMaHD())
        );

        TableColumn<HoaDon, String> colPTTT = new TableColumn<>("Phương thức thanh toán");
        colPTTT.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getMaPTTT())
        );

        TableColumn<HoaDon, String> colNgayLap = new TableColumn<>("Ngày lập");
        colNgayLap.setCellValueFactory(cell -> {
            LocalDateTime ngayLap = cell.getValue().getNgayLap();
            String formatted = ngayLap != null ? ngayLap.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "";
            return new SimpleStringProperty(formatted);
        });

        table.getColumns().addAll(colMaHD, colPTTT, colNgayLap);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.getItems().addAll(HoaDonDAO.getAllHoaDon());

        return table;
    }
}
