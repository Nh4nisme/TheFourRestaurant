package com.thefourrestaurant.view.ban;

import com.thefourrestaurant.DAO.BanDAO;
import com.thefourrestaurant.DAO.PhieuDatBanDAO;
import com.thefourrestaurant.model.Ban;
import com.thefourrestaurant.model.PhieuDatBan;
import com.thefourrestaurant.util.ClockText;
import com.thefourrestaurant.view.components.ButtonSample2;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Giao diện NHẬN BÀN – tìm phiếu đặt trước theo SĐT
 * Đồng bộ màu sắc với GiaoDienDatBan
 */
public class GiaoDienNhanBan extends BorderPane {

    // ==== MÀU ĐỒNG BỘ ==== 
    private static final String COLOR_BG_MAIN = "#f0f0f0";
    private static final String COLOR_BG_HEADER = "#1E424D";
    private static final String COLOR_TEXT_GOLD = "#DDB248";

    private final PhieuDatBanDAO phieuDAO = new PhieuDatBanDAO();
    private final BanDAO banDAO = new BanDAO();
    private final QuanLiBan quanLiBan;

    private TableView<PhieuDatBan> table;
    private TextField txtSoDT;

    public GiaoDienNhanBan(QuanLiBan quanLiBan) {
        this.quanLiBan = quanLiBan;

        setStyle("-fx-background-color: " + COLOR_BG_MAIN + ";");

        setTop(taoHeader());
        setCenter(taoBangPhieu());
        setBottom(taoFooter());
    }

    // ================= HEADER =================
    private VBox taoHeader() {
        VBox header = new VBox(10);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: " + COLOR_BG_HEADER + ";");

        Label lblTitle = new Label("NHẬN BÀN – PHIẾU ĐẶT TRƯỚC");
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + COLOR_TEXT_GOLD);

        ClockText clock = ClockText.getInstance();
        clock.setStyle("-fx-fill: " + COLOR_TEXT_GOLD + "; -fx-font-size: 14px; -fx-font-weight: bold;");

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        Label lblSoDT = new Label("SĐT khách hàng:");
        lblSoDT.setStyle("-fx-text-fill: " + COLOR_TEXT_GOLD + "; -fx-font-weight: bold;");

        txtSoDT = new TextField();
        txtSoDT.setPromptText("Nhập số điện thoại...");
        txtSoDT.setPrefWidth(250);

        searchBox.getChildren().addAll(lblSoDT, txtSoDT);

        header.getChildren().addAll(lblTitle, clock, searchBox);
        return header;
    }

    // ================= TABLE =================
    private VBox taoBangPhieu() {
        table = new TableView<>();
        table.setPrefHeight(400);

        TableColumn<PhieuDatBan, String> colMa = new TableColumn<>("Mã PĐB");
        colMa.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getMaPDB()));

        TableColumn<PhieuDatBan, String> colKH = new TableColumn<>("Khách hàng");
        colKH.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getKhachHang() != null ? c.getValue().getKhachHang().getHoTen() : ""
        ));

        TableColumn<PhieuDatBan, String> colSDT = new TableColumn<>("SĐT");
        colSDT.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getKhachHang() != null ? c.getValue().getKhachHang().getSoDT() : ""
        ));

        TableColumn<PhieuDatBan, String> colNgay = new TableColumn<>("Giờ đặt");
        colNgay.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getNgayDat() != null ? c.getValue().getNgayDat().toString() : ""
        ));

        TableColumn<PhieuDatBan, String> colBan = new TableColumn<>("Bàn");
        colBan.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getDanhSachBan().stream()
                        .map(Ban::getTenBan)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("")
        ));

        table.getColumns().addAll(colMa, colKH, colSDT, colNgay, colBan);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Load data
        ObservableList<PhieuDatBan> dsPhieu =
        	    FXCollections.observableArrayList(
        	        phieuDAO.layPhieuTheoTrangThai("Đặt trước")
        	    );

        FilteredList<PhieuDatBan> filtered = new FilteredList<>(dsPhieu, p -> true);
        txtSoDT.textProperty().addListener((obs, old, val) -> {
            filtered.setPredicate(pdb -> {
                if (val == null || val.isBlank()) return true;
                return pdb.getKhachHang() != null
                        && pdb.getKhachHang().getSoDT() != null
                        && pdb.getKhachHang().getSoDT().contains(val.trim());
            });
        });

        table.setItems(filtered);

        VBox box = new VBox(10, table);
        box.setPadding(new Insets(15));
        return box;
    }

    // ================= FOOTER =================
    private HBox taoFooter() {
        HBox footer = new HBox(15);
        footer.setPadding(new Insets(15));
        footer.setAlignment(Pos.CENTER_RIGHT);

        ButtonSample2 btnNhanBan = new ButtonSample2("Nhận bàn", ButtonSample2.Variant.YELLOW, 160, 45);
        ButtonSample2 btnDong = new ButtonSample2("Đóng", ButtonSample2.Variant.YELLOW, 120, 45);

        btnNhanBan.setOnAction(e -> xuLyNhanBan());
        btnDong.setOnAction(e -> ((Stage) getScene().getWindow()).close());

        footer.getChildren().addAll(btnDong, btnNhanBan);
        return footer;
    }

    // ================= LOGIC =================
    private void xuLyNhanBan() {
        PhieuDatBan pdb = table.getSelectionModel().getSelectedItem();
        if (pdb == null) {
            new Alert(Alert.AlertType.WARNING, "Vui lòng chọn một phiếu đặt trước!").showAndWait();
            return;
        }

        // 1. đổi trạng thái phiếu
        phieuDAO.capNhatTrangThai(pdb.getMaPDB(), "Đang phục vụ");

        // 2. đổi trạng thái tất cả bàn
        banDAO.capNhatTrangThaiDanhSach(pdb.getDanhSachBan(), "Đang phục vụ");

        // 3. refresh giao diện bàn
        quanLiBan.refresh();

        new Alert(Alert.AlertType.INFORMATION, "Nhận bàn thành công!").showAndWait();
        ((Stage) getScene().getWindow()).close();
    }
}
