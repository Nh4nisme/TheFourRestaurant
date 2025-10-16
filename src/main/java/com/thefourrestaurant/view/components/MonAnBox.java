package com.thefourrestaurant.view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

public class MonAnBox extends BaseBox {

    public MonAnBox(String ten, String gia, String imagePath) {
        // Cấu hình chung cho ô món ăn
        this.setPrefSize(150, 200);
        this.setMaxSize(150, 200);
        this.setSpacing(0);
        this.setPadding(Insets.EMPTY);
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("mon-an-box");

        // --- Panel trên: hiển thị ảnh bằng background ---
        StackPane topPane = new StackPane();
        topPane.setPrefHeight(130);
        topPane.setMaxWidth(Double.MAX_VALUE);
        topPane.setAlignment(Pos.CENTER);

        // Cài đặt background ảnh (nếu có)
        try {
            if (imagePath != null && !imagePath.isEmpty()) {
                Image image = new Image(imagePath, true);
                if (!image.isError()) {
                    topPane.setBackground(new Background(
                            new BackgroundImage(image,
                                    BackgroundRepeat.NO_REPEAT,
                                    BackgroundRepeat.NO_REPEAT,
                                    BackgroundPosition.CENTER,
                                    new BackgroundSize(
                                            100, 100, true, true, false, true))
                    ));
                } else {
                    setDefaultBackground(topPane);
                }
            } else {
                setDefaultBackground(topPane);
            }
        } catch (Exception e) {
            setDefaultBackground(topPane);
        }

        // --- Panel dưới: hiển thị tên và giá ---
        VBox bottomPane = new VBox(3);
        bottomPane.setPrefHeight(70);
        bottomPane.setAlignment(Pos.CENTER);
        bottomPane.setPadding(new Insets(8, 5, 8, 5));
        bottomPane.setBackground(new Background(
                new BackgroundFill(Paint.valueOf("#f5f5f5"), new CornerRadii(0, 0, 5, 5, false), Insets.EMPTY)
        ));

        Label tenMon = new Label(ten);
        tenMon.getStyleClass().add("monan-ten");

        Label lblGia = new Label(gia + " VND");
        lblGia.getStyleClass().add("monan-gia");

        bottomPane.getChildren().addAll(tenMon, lblGia);

        // --- Hiệu ứng hover ---
        this.setOnMouseEntered(e -> {
            topPane.setOpacity(0.85);
            this.setStyle("-fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 6, 0, 0, 3);");
        });
        this.setOnMouseExited(e -> {
            topPane.setOpacity(1.0);
            this.setStyle("-fx-effect: none;");
        });

        // Gộp phần trên và dưới vào box chính
        this.getChildren().addAll(topPane, bottomPane);
    }

    // --- Màu nền mặc định khi không có ảnh ---
    private void setDefaultBackground(StackPane topPane) {
        topPane.setBackground(new Background(
                new BackgroundFill(Paint.valueOf("#6A4C34"), new CornerRadii(5, 5, 0, 0, false), Insets.EMPTY)
        ));
    }
}
