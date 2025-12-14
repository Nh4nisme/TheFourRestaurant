package com.thefourrestaurant.view.monan;

import com.thefourrestaurant.view.components.BaseBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class MonAnBox extends BaseBox {

    private MonAnBox() {
        super();
        setPrefSize(150, 200);
        setMaxSize(150, 200);
        setSpacing(0);
        setPadding(Insets.EMPTY);
        setAlignment(Pos.CENTER);
    }

    public MonAnBox(String ten, String gia, String imagePath) {
        // Cấu hình chung cho ô món ăn
        this.setPrefSize(150, 200);
        this.setMaxSize(150, 200);
        this.setSpacing(0);
        this.setPadding(Insets.EMPTY);
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("mon-an-box");

        // --- Panel trên: hiển thị ảnh ---
        StackPane topPane = new StackPane();
        topPane.setPrefHeight(130);
        topPane.setMaxWidth(Double.MAX_VALUE);
        topPane.setAlignment(Pos.CENTER);
        // Bo góc trên cho topPane
        topPane.setStyle("-fx-background-radius: 15 15 0 0;");

        // --- ImageView để chứa ảnh ---
        ImageView imageView = new ImageView();
        imageView.setFitHeight(130);
        imageView.setFitWidth(150);
        imageView.setPreserveRatio(false);

        // Cài đặt ảnh
        try {
            if (imagePath != null && !imagePath.isEmpty()) {
                Image image = loadImage(imagePath);
                if (image != null && !image.isError()) {
                    imageView.setImage(image);
                } else {
                    setDefaultBackground(topPane);
                }
            } else {
                setDefaultBackground(topPane);
            }
        } catch (Exception e) {
            setDefaultBackground(topPane);
            e.printStackTrace();
        }

        // Đặt ImageView vào trong topPane
        topPane.getChildren().add(imageView);
        // Clip ảnh theo bo góc của topPane
        topPane.setClip(createTopCornersClip(topPane));

        // --- Panel dưới: hiển thị tên và giá ---
        VBox bottomPane = new VBox(3);
        bottomPane.setPrefHeight(70);
        bottomPane.setAlignment(Pos.CENTER);
        bottomPane.setPadding(new Insets(8, 5, 8, 5));
        bottomPane.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 0 0 15 15;");

        Label tenMon = new Label(ten);
        tenMon.getStyleClass().add("monan-ten");

        Label lblGia = new Label(gia);
        lblGia.getStyleClass().add("monan-gia");

        bottomPane.getChildren().addAll(tenMon, lblGia);

        // --- Hiệu ứng hover ---
        this.setOnMouseEntered(e -> {
            this.setStyle("-fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 8, 0, 0, 4);");
        });
        this.setOnMouseExited(e -> {
            this.setStyle("-fx-effect: none;");
        });

        // Gộp phần trên và dưới vào box chính
        this.getChildren().addAll(topPane, bottomPane);
        this.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #e0e0e0; -fx-border-width: 1;");
    }

    public MonAnBox(String ten, String gia, String imagePath, int soLuong) {
        this(ten, gia, imagePath);
        Label lblSoLuong = new Label("Số lượng: " + soLuong);
        lblSoLuong.setStyle("-fx-text-fill: #666666; -fx-font-size: 11px;");
        if (!this.getChildren().isEmpty()) {
            Region bottom = (Region) this.getChildren().get(1);
            if (bottom instanceof VBox) {
                ((VBox) bottom).getChildren().add(lblSoLuong);
            }
        }
    }

    private Image loadImage(String imagePath) {
        Image image = null;
        
        if (imagePath.startsWith("/")) {
            // Thử load từ classpath resource trước
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl != null) {
                image = new Image(imageUrl.toExternalForm(), false);
                if (!image.isError()) {
                    return image;
                }
            }
            
            // Nếu không tìm thấy trong classpath, thử load từ file trực tiếp
            String projectDir = System.getProperty("user.dir");
            
            // Thử từ target/classes
            Path targetPath = Paths.get(projectDir, "target/classes", imagePath);
            if (Files.exists(targetPath)) {
                image = new Image(targetPath.toUri().toString(), false);
                if (!image.isError()) {
                    return image;
                }
            }
            
            // Thử từ src/main/resources
            Path srcPath = Paths.get(projectDir, "src/main/resources", imagePath);
            if (Files.exists(srcPath)) {
                image = new Image(srcPath.toUri().toString(), false);
                if (!image.isError()) {
                    return image;
                }
            }
        } else {
            // File URI hoặc full URL
            image = new Image(imagePath, false);
        }
        
        return image;
    }

    // --- Tạo Box "Thêm món mới" ---
    public static MonAnBox createThemMoiBox() {
        MonAnBox hop = new MonAnBox();
        hop.setAlignment(Pos.CENTER);
        hop.setSpacing(5);
        hop.getStyleClass().add("add-item-box");

        Image plusImage = new Image(Objects.requireNonNull(
                MonAnBox.class.getResourceAsStream("/com/thefourrestaurant/images/icon/Them.png")));
        ImageView plusImageView = new ImageView(plusImage);
        plusImageView.setFitWidth(50);
        plusImageView.setFitHeight(50);

        Label themMoiLabel = new Label("Thêm món mới");
        themMoiLabel.getStyleClass().add("monan-ten");

        hop.getChildren().addAll(plusImageView, themMoiLabel);
        return hop;
    }

    // --- Màu nền mặc định khi không có ảnh ---
    private void setDefaultBackground(StackPane topPane) {
        topPane.setStyle("-fx-background-color: #6A4C34; -fx-background-radius: 15 15 0 0;");
    }

    // --- Tạo clip để bo góc ---
    private static javafx.scene.shape.Rectangle createTopCornersClip(Region region) {
        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle();
        clip.widthProperty().bind(region.widthProperty());
        clip.heightProperty().bind(region.heightProperty());
        clip.setArcWidth(30); // Bán kính bo góc ngang (15*2)
        clip.setArcHeight(30); // Bán kính bo góc dọc (15*2)
        return clip;
    }
}