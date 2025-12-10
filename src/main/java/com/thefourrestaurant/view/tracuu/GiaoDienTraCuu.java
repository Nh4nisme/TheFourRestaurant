package com.thefourrestaurant.view.tracuu;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class GiaoDienTraCuu<T> extends VBox {

    protected List<T> danhSachGoc = new ArrayList<>();
    protected List<T> danhSachHienThi = new ArrayList<>();

    protected final TextField txtTimKiem = new TextField();
    protected final ComboBox<String> cboFilter = new ComboBox<>();
    protected final Label lblSoLuong = new Label();

    protected final VBox ketQuaContainer = new VBox();

    public GiaoDienTraCuu(String breadcrumb) {
        setAlignment(Pos.TOP_CENTER);
        setSpacing(0);

        getChildren().addAll(taoTopBar(breadcrumb), taoMiddleBar(), taoContent(), taoStatusBar());
        taiDuLieu();
        apDungLoc();
    }

    /* ================= ABSTRACT ================= */

    protected abstract void taiDuLieu();
    protected abstract boolean thoaManTimKiem(T item, String keyword);
    protected abstract Comparator<T> comparatorMacDinh();
    protected abstract Node taoNodeKetQua(T item);

    /* ================= UI ================= */

    private Node taoTopBar(String breadcrumb) {
        Label lbl = new Label(breadcrumb);
        lbl.setTextFill(Color.web("#E5D595"));

        HBox bar = new HBox(lbl);
        bar.setPadding(new Insets(5, 20, 5, 20));
        bar.setStyle("-fx-background-color: #673E1F;");
        return bar;
    }

    private Node taoMiddleBar() {
        HBox bar = new HBox(10);
        bar.setPadding(new Insets(10, 20, 10, 20));
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setStyle("-fx-background-color: #1E424D;");

        txtTimKiem.setPromptText("Tìm kiếm...");
        txtTimKiem.setOnAction(e -> apDungLoc());

        Button btnTim = new Button("Tìm");
        btnTim.setOnAction(e -> apDungLoc());

        bar.getChildren().addAll(txtTimKiem, btnTim);
        return bar;
    }

    private Node taoContent() {
        ketQuaContainer.setPadding(new Insets(20));
        ScrollPane scroll = new ScrollPane(ketQuaContainer);
        scroll.setFitToWidth(true);
        VBox.setVgrow(scroll, Priority.ALWAYS);
        return scroll;
    }

    private Node taoStatusBar() {
        lblSoLuong.setPadding(new Insets(5, 20, 5, 20));
        return lblSoLuong;
    }

    /* ================= LOGIC ================= */

    protected void apDungLoc() {
        String keyword = txtTimKiem.getText();

        danhSachHienThi = danhSachGoc.stream()
                .filter(item -> keyword == null || keyword.isBlank() || thoaManTimKiem(item, keyword))
                .sorted(comparatorMacDinh())
                .toList();

        capNhatKetQua();
    }

    protected void capNhatKetQua() {
        ketQuaContainer.getChildren().clear();

        danhSachHienThi.forEach(item ->
                ketQuaContainer.getChildren().add(taoNodeKetQua(item))
        );

        lblSoLuong.setText("Hiển thị " + danhSachHienThi.size() + " kết quả");
    }
}
