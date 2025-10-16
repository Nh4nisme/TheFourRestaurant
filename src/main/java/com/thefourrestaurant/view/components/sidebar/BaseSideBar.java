package com.thefourrestaurant.view.components.sidebar;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.Objects;

public abstract class BaseSideBar extends VBox {
    protected final Label tieuDe;

    public BaseSideBar(String tenTieuDe) {
        getStyleClass().add("base-sidebar");
        getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/thefourrestaurant/css/Application.css")).toExternalForm());

        setPadding(new Insets(20));
        setSpacing(10);

        tieuDe = new Label(tenTieuDe);
        tieuDe.getStyleClass().add("sidebar-title");

        getChildren().addAll(tieuDe, taoKhoangCach(50));

        khoiTaoDanhMuc();
    }

    protected abstract void khoiTaoDanhMuc();

    protected Label taoNhanClick(String noiDung, Runnable hanhDong, String tenLopCSS) {
        Label nhan = new Label(noiDung);
        nhan.getStyleClass().add(tenLopCSS);
        nhan.setOnMouseClicked(e -> { if (hanhDong != null) hanhDong.run(); });
        return nhan;
    }

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

    private Region taoKhoangCach(double chieuCao) {
        Region khoang = new Region();
        khoang.setPrefHeight(chieuCao);
        return khoang;
    }
}

