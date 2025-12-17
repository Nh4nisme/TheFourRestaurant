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

public class GiaoDienNhanBan extends BorderPane {

    // ===== MÀU =====
    private static final String COLOR_BG_MAIN = "#f0f0f0";
    private static final String COLOR_BG_HEADER = "#1E424D";
    private static final String COLOR_TEXT_GOLD = "#DDB248";

    private final PhieuDatBanDAO phieuDAO = new PhieuDatBanDAO();
    private final BanDAO banDAO = new BanDAO();
    private final QuanLiBan quanLiBan;

    private TableView<PhieuDatBan> table;
    private TextField txtSoDT;

    private GiaoDienChiTietPhieuDatBan chiTietPane;
    private PhieuDatBan phieuDangChon;

    public GiaoDienNhanBan(QuanLiBan quanLiBan) {
        this.quanLiBan = quanLiBan;

        setStyle("-fx-background-color: " + COLOR_BG_MAIN + ";");

        setTop(taoHeader());
        setCenter(taoNoiDungChinh());
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

    // ================= NỘI DUNG CHÍNH =================
    private SplitPane taoNoiDungChinh() {
        SplitPane split = new SplitPane();
        split.setDividerPositions(0.45);

        VBox bangPhieu = taoBangPhieu();

        chiTietPane = new GiaoDienChiTietPhieuDatBan();
        VBox rightBox = new VBox(10, chiTietPane, taoNutHanhDong());
        rightBox.setPadding(new Insets(10));

        split.getItems().addAll(bangPhieu, rightBox);
        return split;
    }

    // ================= TABLE PHIẾU =================
    private VBox taoBangPhieu() {
        table = new TableView<>();
        table.setPrefHeight(500);

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

        refreshTable();

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            phieuDangChon = selected;
            if (selected != null) {
                chiTietPane.hienThiThongTin(selected);
            }
        });

        VBox box = new VBox(table);
        box.setPadding(new Insets(10));
        return box;
    }

    // ================= NÚT HÀNH ĐỘNG =================
    private HBox taoNutHanhDong() {
        ButtonSample2 btnNhanBan = new ButtonSample2("Nhận bàn", ButtonSample2.Variant.YELLOW, 150, 45);
        ButtonSample2 btnHuyPhieu = new ButtonSample2("Hủy phiếu", ButtonSample2.Variant.YELLOW, 150, 45);

        btnNhanBan.setOnAction(e -> xuLyNhanBan());
        btnHuyPhieu.setOnAction(e -> xuLyHuyPhieu());

        HBox box = new HBox(15, btnHuyPhieu, btnNhanBan);
        box.setAlignment(Pos.CENTER_RIGHT);
        return box;
    }

    // ================= LOGIC =================
    private void xuLyNhanBan() {
        if (phieuDangChon == null) {
            new Alert(Alert.AlertType.WARNING, "Vui lòng chọn phiếu!").showAndWait();
            return;
        }

        phieuDAO.capNhatTrangThai(phieuDangChon.getMaPDB(), "Đang phục vụ");
        banDAO.capNhatTrangThaiDanhSach(phieuDangChon.getDanhSachBan(), "Đang phục vụ");

        refreshTable();
        quanLiBan.refresh();

        new Alert(Alert.AlertType.INFORMATION, "Nhận bàn thành công!").showAndWait();
    }

    private void xuLyHuyPhieu() {
        if (phieuDangChon == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc muốn hủy phiếu này?",
                ButtonType.YES, ButtonType.NO);

        if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            phieuDAO.capNhatTrangThai(phieuDangChon.getMaPDB(), "Đã hủy");
            banDAO.capNhatTrangThaiDanhSach(phieuDangChon.getDanhSachBan(), "Trống");

            refreshTable();
            quanLiBan.refresh();
            phieuDangChon = null;
        }
    }

    private void refreshTable() {
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
    }
}
