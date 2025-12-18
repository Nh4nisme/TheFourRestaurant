package com.thefourrestaurant.view.khuyenmai;

import com.thefourrestaurant.controller.KhuyenMaiController;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.model.KhuyenMai_DieuKien;
import com.thefourrestaurant.model.DieuKien_Mon;
import com.thefourrestaurant.model.DieuKien_MonTang;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class DieuKienKhuyenMaiManagerDialog extends Stage {

    private final KhuyenMai khuyenMaiCha;
    private final KhuyenMaiController boDieuKhien;
    private final TableView<KhuyenMai_DieuKien> bangDieuKien = new TableView<>();

    public DieuKienKhuyenMaiManagerDialog(KhuyenMai khuyenMaiCha, KhuyenMaiController boDieuKhien) {
        this.khuyenMaiCha = khuyenMaiCha;
        this.boDieuKhien = boDieuKhien;

        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Quản lý Điều Kiện cho: " + khuyenMaiCha.getTenKM());

        BorderPane layoutChinh = new BorderPane();

        // Header
        Label nhanTieuDe = new Label("Điều kiện Khuyến mãi: " + khuyenMaiCha.getTenKM());
        nhanTieuDe.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #D4A017;");
        HBox hopTieuDe = new HBox(nhanTieuDe);
        hopTieuDe.setAlignment(Pos.CENTER_LEFT);
        hopTieuDe.setPadding(new Insets(15));
        hopTieuDe.setStyle("-fx-background-color: #1E424D;");
        layoutChinh.setTop(hopTieuDe);

        // Content
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        VBox.setVgrow(bangDieuKien, Priority.ALWAYS);

        caiDatBangDieuKien();
        content.getChildren().add(bangDieuKien);

        HBox actionButtons = new HBox(10);
        actionButtons.setAlignment(Pos.CENTER_RIGHT);
        ButtonSample btnThem = new ButtonSample("Thêm", 35, 14, 2);
        btnThem.setOnAction(e -> themDieuKien());
        ButtonSample btnSua = new ButtonSample("Sửa", 35, 14, 2);
        btnSua.setOnAction(e -> suaDieuKien());
        ButtonSample btnXoa = new ButtonSample("Xóa", 35, 14, 2);
        btnXoa.setOnAction(e -> xoaDieuKien());
        actionButtons.getChildren().addAll(btnThem, btnSua, btnXoa);
        content.getChildren().add(actionButtons);

        layoutChinh.setCenter(content);

        // Footer
        HBox hopChanTrang = new HBox(10);
        hopChanTrang.setPadding(new Insets(15));
        hopChanTrang.setAlignment(Pos.CENTER_RIGHT);
        hopChanTrang.setStyle("-fx-background-color: #F0F0F0; -fx-border-color: #E0E0E0; -fx-border-width: 1 0 0 0;");
        ButtonSample nutDong = new ButtonSample("Đóng", 35, 14, 2);
        nutDong.setOnAction(e -> this.close());
        hopChanTrang.getChildren().add(nutDong);
        layoutChinh.setBottom(hopChanTrang);

        Scene khungCanh = new Scene(layoutChinh, 900, 600);
        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            khungCanh.getStylesheets().add(urlCSS.toExternalForm());
        }
        this.setScene(khungCanh);
        lamMoiBang();
    }

    private void caiDatBangDieuKien() {
        TableColumn<KhuyenMai_DieuKien, String> moTaCol = new TableColumn<>("Mô tả Điều kiện");
        moTaCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMoTaDieuKien()));
        moTaCol.setPrefWidth(250);

        TableColumn<KhuyenMai_DieuKien, String> loaiApDungCol = new TableColumn<>("Loại Áp Dụng");
        loaiApDungCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLoaiApDung()));

        TableColumn<KhuyenMai_DieuKien, String> monApDungCol = new TableColumn<>("Món Áp Dụng/Mua");
        monApDungCol.setCellValueFactory(cell -> new SimpleStringProperty(
            cell.getValue().getDanhSachMonDieuKien().stream()
                .map(dkm -> dkm.getMonAn().getTenMon() + " (x" + dkm.getSoLuong() + ")")
                .collect(Collectors.joining(", "))
        ));
        monApDungCol.setPrefWidth(200);
        
        TableColumn<KhuyenMai_DieuKien, String> monTangCol = new TableColumn<>("Món Tặng");
        monTangCol.setCellValueFactory(cell -> new SimpleStringProperty(
            cell.getValue().getDanhSachMonTang().stream()
                .map(dkmt -> dkmt.getMonAnTang().getTenMon())
                .collect(Collectors.joining(", "))
        ));
        monTangCol.setPrefWidth(200);

        bangDieuKien.getColumns().addAll(moTaCol, loaiApDungCol, monApDungCol, monTangCol);
        bangDieuKien.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void lamMoiBang() {
        List<KhuyenMai_DieuKien> dsDieuKien = boDieuKhien.layDieuKienTheoMaKM(khuyenMaiCha.getMaKM());
        bangDieuKien.setItems(FXCollections.observableArrayList(dsDieuKien));
        bangDieuKien.refresh();
    }

    private void themDieuKien() {
        DieuKienKhuyenMaiDialog dialog = new DieuKienKhuyenMaiDialog(null, khuyenMaiCha, boDieuKhien);
        dialog.initOwner(this);
        dialog.showAndWait();

        if (dialog.daLuu()) {
            KhuyenMai_DieuKien dieuKienMoi = dialog.getDieuKien();
            if (boDieuKhien.themDieuKienKhuyenMai(this, dieuKienMoi)) {
                lamMoiBang();
            }
        }
    }

    private void suaDieuKien() {
        KhuyenMai_DieuKien selected = bangDieuKien.getSelectionModel().getSelectedItem();
        if (selected == null) {
            hienThiThongBao(Alert.AlertType.WARNING, "Vui lòng chọn một điều kiện để sửa.");
            return;
        }
        
        DieuKienKhuyenMaiDialog dialog = new DieuKienKhuyenMaiDialog(selected, khuyenMaiCha, boDieuKhien);
        dialog.initOwner(this);
        dialog.showAndWait();

        if (dialog.daLuu()) {
            KhuyenMai_DieuKien dieuKienDaSua = dialog.getDieuKien();
            if (boDieuKhien.capNhatDieuKienKhuyenMai(this, dieuKienDaSua)) {
                lamMoiBang();
            }
        }
    }

    private void xoaDieuKien() {
        KhuyenMai_DieuKien selected = bangDieuKien.getSelectionModel().getSelectedItem();
        if (selected == null) {
            hienThiThongBao(Alert.AlertType.WARNING, "Vui lòng chọn một điều kiện để xóa.");
            return;
        }
        if (boDieuKhien.xoaDieuKienKhuyenMai(this, selected)) {
            lamMoiBang();
        }
    }
    
    private void hienThiThongBao(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(type == Alert.AlertType.ERROR ? "Lỗi" : "Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(this);
        alert.showAndWait();
    }
}
