package com.thefourrestaurant.controller;

import com.thefourrestaurant.view.components.ButtonSample;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class LoaiMonAnController {

    private File selectedImageFile = null;

    public void themMoiLoaiMonAn() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Thêm Loại Món Ăn Mới");

        // --- Font ---
        Font montserratFont = Font.loadFont(
                getClass().getResourceAsStream("/com/thefourrestaurant/fonts/Montserrat-SemiBold.ttf"), 14);
        String fontStyle = "";
        if (montserratFont != null) {
            fontStyle = "-fx-font-family: '" + montserratFont.getFamily() + "';";
        } else {
            System.err.println("Không tìm thấy font Montserrat-SemiBold.ttf");
        }

        String labelStyle = fontStyle + "-fx-text-fill: #E19E11; -fx-font-size: 14px;";
        String inputStyle = fontStyle + "-fx-text-fill: #E19E11;";

        BorderPane mainLayout = new BorderPane();

        // --- Header ---
        Label titleLabel = new Label("Tùy chỉnh loại món ăn");
        titleLabel.setStyle(fontStyle + "-fx-text-fill: #D4A017; -fx-font-size: 18px; -fx-font-weight: bold;");
        HBox headerPart = new HBox(titleLabel);
        headerPart.setAlignment(Pos.CENTER_LEFT);
        headerPart.setPadding(new Insets(15));
        headerPart.setStyle("-fx-background-color: #1E424D;");

        // --- Phần Ảnh ---
        HBox topPart = new HBox();
        topPart.setAlignment(Pos.CENTER_LEFT); // căn giữa nhưng lệch trái
        topPart.setPadding(new Insets(20, 0, 10, 150)); // dịch sang trái 60px, bạn có thể tinh chỉnh số này

        VBox imageBox = new VBox(5);
        imageBox.setAlignment(Pos.CENTER);
        imageBox.setPrefSize(130, 130);
        imageBox.setMinSize(130, 130);
        imageBox.setMaxSize(130, 130);
        imageBox.setStyle("-fx-background-color: #F0F0F0; -fx-border-color: #CCCCCC; "
                + "-fx-border-radius: 10; -fx-background-radius: 10; -fx-border-style: dashed;");
        imageBox.setCursor(Cursor.HAND);

        ImageView anhImageView = new ImageView(
                new Image(getClass().getResourceAsStream("/com/thefourrestaurant/images/icon/ThayAnh.png")));
        anhImageView.setFitWidth(150);
        anhImageView.setFitHeight(150);

        //Label chonAnhLabel = new Label("Chọn Ảnh");
        //chonAnhLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #888888;");

        imageBox.getChildren().addAll(anhImageView);
        topPart.getChildren().add(imageBox);

        // --- Form ---
        VBox centerPart = new VBox(15);
        centerPart.setPadding(new Insets(10, 20, 20, 20)); // padding hợp lý
        centerPart.setAlignment(Pos.TOP_LEFT); // không căn giữa nữa, căn trên-trái

        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(15);
        formGrid.setAlignment(Pos.TOP_LEFT);
        ColumnConstraints col1 = new ColumnConstraints(80); // nhãn
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS); // cho phép fill chiều ngang
        formGrid.getColumnConstraints().addAll(col1, col2);

        Label tenLabel = new Label("Tên:");
        tenLabel.setStyle(labelStyle);
        TextField tenTextField = new TextField();
        tenTextField.setPromptText("Nhập tên loại món ăn");
        tenTextField.setStyle(inputStyle);
        tenTextField.setMaxWidth(Double.MAX_VALUE); // fill width
        tenTextField.getStyleClass().add("text-field");

        Label moTaLabel = new Label("Mô tả:");
        moTaLabel.setStyle(labelStyle);
        TextArea moTaTextArea = new TextArea();
        moTaTextArea.setPromptText("Nhập mô tả chi tiết");
        moTaTextArea.setPrefRowCount(3);
        moTaTextArea.setStyle(inputStyle);
        moTaTextArea.setMaxWidth(Double.MAX_VALUE);
        moTaTextArea.getStyleClass().add("text-area");

        Label hienLabel = new Label("Hiện:");
        hienLabel.setStyle(labelStyle);
        CheckBox hienCheckBox = new CheckBox();
        hienCheckBox.setSelected(true);
        hienCheckBox.getStyleClass().add("custom-checkbox");

        formGrid.add(tenLabel, 0, 0);
        formGrid.add(tenTextField, 1, 0);
        formGrid.add(moTaLabel, 0, 1);
        formGrid.add(moTaTextArea, 1, 1);
        formGrid.add(hienLabel, 0, 2);
        formGrid.add(hienCheckBox, 1, 2);

        centerPart.getChildren().add(formGrid);

        // --- Footer ---
        HBox footerPart = new HBox(10);
        footerPart.setPadding(new Insets(10));
        footerPart.setAlignment(Pos.CENTER_LEFT);
        footerPart.setStyle(
                "-fx-background-color: #F0F0F0; -fx-border-color: #E0E0E0; -fx-border-width: 1 0 0 0;");

        Button btnXoa = new ButtonSample("Xóa", null, 35, 14);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button btnOke = new ButtonSample("Oke", null, 35, 14);
        Button btnHuy = new ButtonSample("Hủy", null, 35, 14);
        footerPart.getChildren().addAll(btnXoa, spacer, btnOke, btnHuy);

        // --- Layout tổng ---
        VBox mainContent = new VBox();
        mainContent.getChildren().addAll(headerPart, topPart, centerPart);
        mainLayout.setCenter(mainContent);
        mainLayout.setBottom(footerPart);

        // --- Sự kiện ---
        imageBox.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Chọn Ảnh");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            File file = fileChooser.showOpenDialog(popupStage);
            if (file != null) {
                selectedImageFile = file;
                Image image = new Image(file.toURI().toString());
                anhImageView.setImage(image);
                anhImageView.setFitWidth(120);
                anhImageView.setFitHeight(120);
                imageBox.getChildren().clear();
                imageBox.getChildren().add(anhImageView);
                imageBox.setStyle(
                        "-fx-background-color: white; -fx-border-color: #CCCCCC; -fx-border-radius: 10; -fx-background-radius: 10;");
            }
        });

        btnHuy.setOnAction(e -> popupStage.close());

        btnXoa.setOnAction(e -> {
            System.out.println("Chức năng Xóa được gọi.");
        });

        btnOke.setOnAction(e -> {
            String tenLoaiMonAn = tenTextField.getText();
            if (tenLoaiMonAn == null || tenLoaiMonAn.trim().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Thiếu thông tin");
                alert.setHeaderText(null);
                alert.setContentText("Tên loại món ăn không được để trống!");
                alert.showAndWait();
            } else {
                System.out.println("Lưu loại món ăn mới:");
                System.out.println(" - Tên: " + tenLoaiMonAn);
                System.out.println(" - Mô tả: " + moTaTextArea.getText());
                System.out.println(" - Hiện: " + hienCheckBox.isSelected());
                if (selectedImageFile != null) {
                    System.out.println(" - Ảnh: " + selectedImageFile.getAbsolutePath());
                }
                popupStage.close();
            }
        });

        // --- Scene ---
        Scene popupScene = new Scene(mainLayout, 580, 415);
        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            popupScene.getStylesheets().add(urlCSS.toExternalForm());
        } else {
            System.err.println("Không tìm thấy tệp CSS: Application.css");
        }

        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
}
