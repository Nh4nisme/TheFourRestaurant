package com.thefourrestaurant.view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

import java.util.Objects;

public class LoaiMonAnBox extends BaseBox {

    private LoaiMonAnBox() {
        super();
        setPrefSize(150, 200);
        setMaxSize(150, 200);
        setSpacing(0);
        setPadding(Insets.EMPTY);
        setAlignment(Pos.CENTER);
    }

    // --- Tạo Box hiển thị loại món ăn ---
    public static LoaiMonAnBox createLoaiMonAnBox(String ten, String imagePath) {
        LoaiMonAnBox hopLoaiMonAn = new LoaiMonAnBox();
        hopLoaiMonAn.getStyleClass().add("loai-mon-an-box");


        // ----- Panel trên: chứa background ảnh hoặc màu -----
        StackPane topPane = new StackPane();
        topPane.setPrefHeight(150);
        topPane.setMaxWidth(Double.MAX_VALUE);
        topPane.setAlignment(Pos.CENTER);

        // Bo góc trên
        topPane.setBackground(new Background(
                new BackgroundFill(Paint.valueOf("#5E3A1C"), new CornerRadii(5, 5, 0, 0, false), Insets.EMPTY)
        ));
        // Set background ảnh nếu có
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



        // ----- Panel dưới: chứa label -----
        StackPane bottomPane = new StackPane();
        bottomPane.setPrefHeight(50);
        bottomPane.setAlignment(Pos.CENTER);
        bottomPane.setBackground(new Background(
                new BackgroundFill(Paint.valueOf("#f5f5f5"), new CornerRadii(0, 0, 5, 5, false), Insets.EMPTY)
        ));

        Label tenLoaiMon = new Label(ten);
        tenLoaiMon.getStyleClass().add("monan-ten");
        bottomPane.getChildren().add(tenLoaiMon);

        // ----- Hiệu ứng hover -----
        hopLoaiMonAn.setOnMouseEntered(e -> {
            topPane.setOpacity(0.85);
            hopLoaiMonAn.setStyle("-fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 6, 0, 0, 3);");
        });
        hopLoaiMonAn.setOnMouseExited(e -> {
            topPane.setOpacity(1.0);
            hopLoaiMonAn.setStyle("-fx-effect: none;");
        });

        hopLoaiMonAn.getChildren().addAll(topPane, bottomPane);
        return hopLoaiMonAn;
    }

    // --- Tạo Box "Thêm loại mới" ---
    public static LoaiMonAnBox createThemMoiBox() {
        LoaiMonAnBox hop = new LoaiMonAnBox();
        hop.setAlignment(Pos.CENTER);
        hop.setSpacing(5);
        hop.getStyleClass().add("add-item-box");

        Image plusImage = new Image(Objects.requireNonNull(
                LoaiMonAnBox.class.getResourceAsStream("/com/thefourrestaurant/images/icon/Them.png")));
        ImageView plusImageView = new ImageView(plusImage);
        plusImageView.setFitWidth(50);
        plusImageView.setFitHeight(50);

        Label themMoiLabel = new Label("Thêm loại mới");
        themMoiLabel.getStyleClass().add("monan-ten");

        hop.getChildren().addAll(plusImageView, themMoiLabel);
        return hop;
    }

    // --- Hàm phụ: set background mặc định ---
    private static void setDefaultBackground(StackPane topPane) {
        topPane.setBackground(new Background(
                new BackgroundFill(Paint.valueOf("#5E3A1C"), new CornerRadii(5, 5, 0, 0, false), Insets.EMPTY)
        ));
    }

    // --- Hàm phụ: ảnh mặc định (nếu cần dùng cho các component khác) ---
    private static Image getDefaultImage() {
        return new Image(LoaiMonAnBox.class.getResourceAsStream("/com/thefourrestaurant/images/icon/ThayAnh.png"));
    }
}
