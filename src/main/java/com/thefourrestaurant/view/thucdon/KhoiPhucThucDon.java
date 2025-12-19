package com.thefourrestaurant.view.thucdon;

import com.thefourrestaurant.DAO.ThucDonDAO;
import com.thefourrestaurant.view.components.CuaSoKhoiPhuc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class KhoiPhucThucDon extends CuaSoKhoiPhuc<ThucDonDAO.ThucDonView> {

    private TableView<ThucDonDAO.ThucDonView> table;

    public KhoiPhucThucDon(List<ThucDonDAO.ThucDonView> danhSachDaXoa) {
        super(danhSachDaXoa, "Khôi phục thực đơn đã xóa", "thực đơn");
    }

    @Override
    protected Pane createViewPane() {
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ThucDonDAO.ThucDonView, String> maCol = new TableColumn<>();
        javafx.scene.control.Label maHeader = new javafx.scene.control.Label("Mã TD");
        maHeader.setStyle("-fx-text-fill: black; -fx-font-size: 14px; -fx-font-weight: bold;");
        maCol.setGraphic(maHeader);
        maCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
            cell.getValue() == null ? "" : (cell.getValue().maTD == null ? "" : cell.getValue().maTD)));

        TableColumn<ThucDonDAO.ThucDonView, String> tenCol = new TableColumn<>();
        javafx.scene.control.Label tenHeader = new javafx.scene.control.Label("Tên thực đơn");
        tenHeader.setStyle("-fx-text-fill: black; -fx-font-size: 14px; -fx-font-weight: bold;");
        tenCol.setGraphic(tenHeader);
        tenCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
            cell.getValue() == null ? "" : (cell.getValue().tenTD == null ? "" : cell.getValue().tenTD)));

        TableColumn<ThucDonDAO.ThucDonView, String> loaiCol = new TableColumn<>();
        javafx.scene.control.Label loaiHeader = new javafx.scene.control.Label("Các loại món");
        loaiHeader.setStyle("-fx-text-fill: black; -fx-font-size: 14px; -fx-font-weight: bold;");
        loaiCol.setGraphic(loaiHeader);
        loaiCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
            cell.getValue() == null ? "" : (cell.getValue().loaiMon == null ? "" : cell.getValue().loaiMon)));

        table.getColumns().addAll(maCol, tenCol, loaiCol);
        table.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);

        VBox wrapper = new VBox(table);
        wrapper.setPadding(new Insets(12));
        return wrapper;
    }

    @Override
    protected List<String> layDanhSachLoai() {
        return danhSachDaXoa.stream()
                .flatMap(v -> Arrays.stream((v.loaiMon == null ? "" : v.loaiMon).split(",")))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    protected boolean locTheoLoai(ThucDonDAO.ThucDonView item, String loai) {
        if (item.loaiMon == null) return false;
        return Arrays.stream(item.loaiMon.split(",")).map(String::trim).anyMatch(s -> s.equals(loai));
    }

    @Override
    protected boolean timKiem(ThucDonDAO.ThucDonView item, String tuKhoa) {
        String key = tuKhoa.toLowerCase(Locale.ROOT);
        return (item.tenTD != null && item.tenTD.toLowerCase().contains(key)) ||
                (item.maTD != null && item.maTD.toLowerCase().contains(key));
    }

    @Override
    protected Comparator<ThucDonDAO.ThucDonView> getComparator() {
        return Comparator.comparing(v -> v.tenTD == null ? "" : v.tenTD);
    }

    @Override
    protected void capNhatView() {
        ObservableList<ThucDonDAO.ThucDonView> items = FXCollections.observableArrayList(danhSachHienThi);
        table.setItems(items);
        int count = danhSachHienThi.size();
        lblItemCount.setText("Hiển thị " + count + " thực đơn đã xóa");
    }

    public java.util.Set<ThucDonDAO.ThucDonView> getCacThucDonDaChon() {
        if (table == null) return new HashSet<>();
        return new HashSet<>(table.getSelectionModel().getSelectedItems());
    }
}
