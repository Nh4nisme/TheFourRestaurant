package com.thefourrestaurant.view.monan;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MonAnComList extends VBox {

    private TableView<Object> table;

    public MonAnComList() {
        this.table = taoBangChinh();
        VBox.setVgrow(table, Priority.ALWAYS);
        this.getChildren().add(table);
        // Áp dụng CSS nếu cần
        getStylesheets().add(getClass().getResource("/com/thefourrestaurant/css/Application.css").toExternalForm());
    }

    private TableView<Object> taoBangChinh() {
        TableView<Object> table = new TableView<>();

        TableColumn<Object, Boolean> checkBoxCol = new TableColumn<>("☑️");
        TableColumn<Object, String> danhMucCol = new TableColumn<>("🍚 Danh mục");
        TableColumn<Object, String> maMonCol = new TableColumn<>("Mã món");
        TableColumn<Object, String> tenMonAnCol = new TableColumn<>("Tên món ăn");
        TableColumn<Object, Double> donGiaCol = new TableColumn<>("Đơn giá (VND)");
        TableColumn<Object, Double> thueCol = new TableColumn<>("Thuế (%)");
        TableColumn<Object, String> trangThaiCol = new TableColumn<>("Trạng thái");
        TableColumn<Object, Integer> soLuongCol = new TableColumn<>("Số lượng");

        table.getColumns().addAll(
                checkBoxCol,
                danhMucCol,
                maMonCol,
                tenMonAnCol,
                donGiaCol,
                thueCol,
                trangThaiCol,
                soLuongCol
        );

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return table;
    }

    public TableView<Object> getTable() {
        return table;
    }
}
