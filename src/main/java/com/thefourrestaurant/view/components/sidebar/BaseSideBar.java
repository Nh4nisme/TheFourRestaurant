package com.thefourrestaurant.view.components.sidebar;

import java.util.Objects;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public abstract class BaseSideBar extends VBox {

    protected final Label tieuDe;
    protected final VBox danhMucContainer;

    public BaseSideBar(String tenTieuDe) {
        getStyleClass().add("base-sidebar");
        getStylesheets().add(Objects.requireNonNull(
                getClass().getResource("/com/thefourrestaurant/css/Application.css")).toExternalForm());

        // Padding & spacing
        setPadding(new Insets(20));
        setSpacing(10);
        VBox.setVgrow(this, Priority.ALWAYS);

        // Header tiêu đề
        tieuDe = new Label(tenTieuDe);
        tieuDe.getStyleClass().add("sidebar-title");
        tieuDe.setAlignment(Pos.TOP_LEFT);
        tieuDe.setMinHeight(120);

        // Container danh mục chiếm phần còn lại
        danhMucContainer = new VBox();
        danhMucContainer.setSpacing(10);

        // Thêm header + danh mục vào sidebar
        getChildren().addAll(tieuDe, danhMucContainer);

        khoiTaoDanhMuc();
    }

    // Các class con override để thêm danh mục
    protected abstract void khoiTaoDanhMuc();

    protected Label taoNhanClick(String noiDung, Runnable hanhDong, String tenLopCSS) {
        Label nhan = new Label(noiDung);
        nhan.getStyleClass().add(tenLopCSS);
        nhan.setOnMouseClicked(e -> {
            if (hanhDong != null) {
                hanhDong.run();
            }
        });
        return nhan;
    }

    // Mở hoặc đóng VBox chứa các mục con
    protected void moHoacDongMucCon(VBox hopChua) {
        boolean hienThi = hopChua.isVisible();
        hopChua.setVisible(!hienThi);
        hopChua.setManaged(!hienThi);

        double chieuCao = hienThi ? 0 : hopChua.getChildren().size() * 30;
        Timeline chuyenDong = new Timeline(
                new KeyFrame(Duration.millis(150),
                        new KeyValue(hopChua.prefHeightProperty(), chieuCao))
        );
        chuyenDong.play();
    }

    // Thêm khoảng cách trong sidebar
    protected Region taoKhoangCach(double chieuCao) {
        Region khoang = new Region();
        khoang.setPrefHeight(chieuCao);
        return khoang;
    }

    // Thêm node vào danh mục
    protected void themVaoDanhMuc(Node node) {
        danhMucContainer.getChildren().add(node);
    }
}