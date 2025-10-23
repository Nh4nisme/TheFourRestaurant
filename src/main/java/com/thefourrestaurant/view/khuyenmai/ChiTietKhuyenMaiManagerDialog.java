package com.thefourrestaurant.view.khuyenmai;

import com.thefourrestaurant.controller.KhuyenMaiController;
import com.thefourrestaurant.model.ChiTietKhuyenMai;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.model.MonAn;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ChiTietKhuyenMaiManagerDialog extends Stage {

    private final KhuyenMai khuyenMaiCha; // The parent KhuyenMai
    private final KhuyenMaiController controller; // To handle ChiTietKhuyenMai operations
    private final TableView<ChiTietKhuyenMai> chiTietTableView = new TableView<>();

    public ChiTietKhuyenMaiManagerDialog(KhuyenMai khuyenMaiCha, KhuyenMaiController controller) {
        this.khuyenMaiCha = khuyenMaiCha;
        this.controller = controller;

        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Quản lý Chi tiết Khuyến mãi cho: " + khuyenMaiCha.getMoTa());

        BorderPane layoutChinh = new BorderPane();

        Font fontMontserrat = null;
        try (InputStream luongFont = getClass().getResourceAsStream("/com/thefourrestaurant/fonts/Montserrat-SemiBold.ttf")) {
            if (luongFont != null) {
                fontMontserrat = Font.loadFont(luongFont, 14);
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tải font: " + e.getMessage());
        }

        String kieuFontStyle = (fontMontserrat != null) ? "-fx-font-family: '" + fontMontserrat.getFamily() + "';" : "";

        // Header
        Label nhanTieuDe = new Label("Chi tiết Khuyến mãi: " + khuyenMaiCha.getMoTa());
        nhanTieuDe.setStyle(kieuFontStyle + "-fx-text-fill: #D4A017; -fx-font-size: 18px; -fx-font-weight: bold;");
        HBox hopTieuDe = new HBox(nhanTieuDe);
        hopTieuDe.setAlignment(Pos.CENTER_LEFT);
        hopTieuDe.setPadding(new Insets(15));
        hopTieuDe.setStyle("-fx-background-color: #1E424D;");
        layoutChinh.setTop(hopTieuDe);

        // Main Content (Table and Buttons)
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(chiTietTableView, Priority.ALWAYS);

        setupChiTietTableView();
        content.getChildren().add(chiTietTableView);

        // Action Buttons
        HBox actionButtons = new HBox(10);
        actionButtons.setAlignment(Pos.CENTER_RIGHT);
        actionButtons.setPadding(new Insets(10, 0, 0, 0));

        ButtonSample btnThem = new ButtonSample("Thêm", 35, 14, 2);
        btnThem.setOnAction(e -> themChiTiet());

        ButtonSample btnSua = new ButtonSample("Sửa", 35, 14, 2);
        btnSua.setOnAction(e -> suaChiTiet());

        ButtonSample btnXoa = new ButtonSample("Xóa", 35, 14, 2);
        btnXoa.setOnAction(e -> xoaChiTiet());

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

        Scene khungCanh = new Scene(layoutChinh, 800, 600);
        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            khungCanh.getStylesheets().add(urlCSS.toExternalForm());
        }
        this.setScene(khungCanh);
        refreshTableView();
    }

    private void setupChiTietTableView() {
        // maCTKM
        TableColumn<ChiTietKhuyenMai, String> maCTKMCol = new TableColumn<>("Mã CTKM");
        maCTKMCol.setCellValueFactory(new PropertyValueFactory<>("maCTKM"));
        maCTKMCol.setPrefWidth(80);

        // MonApDung
        TableColumn<ChiTietKhuyenMai, String> monApDungCol = new TableColumn<>("Món áp dụng");
        monApDungCol.setCellValueFactory(cellData -> {
            MonAn mon = cellData.getValue().getMonApDung();
            return new SimpleStringProperty(mon != null ? mon.getTenMon() : "");
        });
        monApDungCol.setPrefWidth(120);

        // MonTang
        TableColumn<ChiTietKhuyenMai, String> monTangCol = new TableColumn<>("Món tặng");
        monTangCol.setCellValueFactory(cellData -> {
            MonAn mon = cellData.getValue().getMonTang();
            return new SimpleStringProperty(mon != null ? mon.getTenMon() : "");
        });
        monTangCol.setPrefWidth(120);

        // GiaTriGiam (combined)
        TableColumn<ChiTietKhuyenMai, String> giaTriGiamCol = new TableColumn<>("Giá trị giảm");
        giaTriGiamCol.setCellValueFactory(cellData -> {
            ChiTietKhuyenMai ct = cellData.getValue();
            BigDecimal tyLe = ct.getTyLeGiam();
            BigDecimal soTien = ct.getSoTienGiam();
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String result = "";

            if (tyLe != null && tyLe.compareTo(BigDecimal.ZERO) >= 0) {
                result = tyLe.stripTrailingZeros().toPlainString() + "%";
            } else if (soTien != null && soTien.compareTo(BigDecimal.ZERO) >= 0) {
                result = currencyFormatter.format(soTien);
            }
            return new SimpleStringProperty(result);
        });
        giaTriGiamCol.setPrefWidth(120);

        // SoLuongTang
        TableColumn<ChiTietKhuyenMai, String> soLuongTangCol = new TableColumn<>("SL tặng");
        soLuongTangCol.setCellValueFactory(cellData -> {
            Integer soLuong = cellData.getValue().getSoLuongTang();
            return new SimpleStringProperty(soLuong != null && soLuong >= 0 ? String.valueOf(soLuong) : "");
        });
        soLuongTangCol.setPrefWidth(60);

        chiTietTableView.getColumns().addAll(maCTKMCol, monApDungCol, monTangCol, giaTriGiamCol, soLuongTangCol);
        chiTietTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void refreshTableView() {
        chiTietTableView.setItems(FXCollections.observableArrayList(controller.layChiTietKhuyenMaiTheoMaKM(khuyenMaiCha.getMaKM())));
        chiTietTableView.refresh();
    }

    private void themChiTiet() {
        List<MonAn> tatCaMonAn = controller.layTatCaMonAn();
        String maCTKMMoi = controller.taoMaChiTietKhuyenMaiMoi();

        ChiTietKhuyenMaiDialog dialog = new ChiTietKhuyenMaiDialog(null, khuyenMaiCha, tatCaMonAn, maCTKMMoi);
        dialog.showAndWait();

        ChiTietKhuyenMai ketQua = dialog.layKetQua();
        if (ketQua != null) {
            if (controller.themMoiChiTietKhuyenMai(ketQua)) {
                refreshTableView();
            }
        }
    }

    private void suaChiTiet() {
        ChiTietKhuyenMai selected = chiTietTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Vui lòng chọn một chi tiết khuyến mãi để sửa.").showAndWait();
            return;
        }

        List<MonAn> tatCaMonAn = controller.layTatCaMonAn();
        ChiTietKhuyenMaiDialog dialog = new ChiTietKhuyenMaiDialog(selected, khuyenMaiCha, tatCaMonAn, null);
        dialog.showAndWait();

        ChiTietKhuyenMai ketQua = dialog.layKetQua();
        if (ketQua != null) {
            if (controller.tuyChinhChiTietKhuyenMai(ketQua)) {
                refreshTableView();
            }
        }
    }

    private void xoaChiTiet() {
        ChiTietKhuyenMai selected = chiTietTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Vui lòng chọn một chi tiết khuyến mãi để xóa.").showAndWait();
            return;
        }

        if (controller.xoaChiTietKhuyenMai(selected)) {
            refreshTableView();
        }
    }
}
