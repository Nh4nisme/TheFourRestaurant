package com.thefourrestaurant.view.hoadon;

import com.thefourrestaurant.view.components.ButtonSample;
import com.thefourrestaurant.view.components.NavBar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class GiaoDienHoaDon extends VBox {
    public GiaoDienHoaDon() {

        setVgrow(this, Priority.ALWAYS);

        NavBar navBar = new NavBar(this);

        HBox toolbar = new HBox();
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(10));
        toolbar.setStyle("-fx-background-color: #1E424D;");
        Label toolBarTieuDe = new Label("Hóa đơn");
        toolBarTieuDe.setStyle("-fx-font-size: 20; -fx-text-fill: #DDB248; -fx-font-weight: Bold;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        ButtonSample btnTimKiem = new ButtonSample("Tìm kiếm", 45,16,3);
        toolbar.getChildren().addAll(toolBarTieuDe,spacer,btnTimKiem);


        //splitpane chia ra làm 2
        SplitPane splitPane = new SplitPane();
        splitPane.setPadding(new  Insets(10));
        splitPane.setDividerPosition(0, 0.6);

        VBox.setVgrow(splitPane, Priority.ALWAYS);

        VBox bangDuLieu = new VBox();
        TableView<Object> table = new TableView<>();
        TableColumn<Object, String> maHDCol = new TableColumn<>("Mã HD");
        TableColumn<Object, String> trangThaiCol = new TableColumn<>("Trạng thái");
        TableColumn<Object, String> phuongThucCol = new TableColumn<>("Phương thức thanh toán");
        TableColumn<Object, String> ngayCol = new TableColumn<>("Ngày lập hóa đơn");
        TableColumn<Object, String> tongTienCol = new TableColumn<>("Tổng tiền");
        table.getColumns().addAll(maHDCol, trangThaiCol, phuongThucCol, ngayCol, tongTienCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // auto-fit width
        VBox.setVgrow(table, Priority.ALWAYS);
        bangDuLieu.getChildren().addAll(table);


        VBox bangChiTietDuLieu = new VBox();
        bangChiTietDuLieu.getChildren().add(new GiaoDienChiTietHoaDon());
        VBox.setVgrow(bangChiTietDuLieu, Priority.ALWAYS);

        splitPane.getItems().addAll(bangDuLieu,bangChiTietDuLieu);


        getChildren().addAll(navBar,toolbar,splitPane);
    }
}
