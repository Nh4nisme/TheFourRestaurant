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
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoaiMonAnController {

    private Map<String, Object> result = null;
    private File selectedImageFile = null;

    public Map<String, Object> themMoiLoaiMonAn() {
        this.result = null;
        this.selectedImageFile = null;

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Thêm Loại Món Ăn Mới");

        // --- Font ---
        Font montserratFont = null;
        try (InputStream fontStream = getClass().getResourceAsStream("/com/thefourrestaurant/fonts/Montserrat-SemiBold.ttf")) {
            if (fontStream != null) {
                montserratFont = Font.loadFont(fontStream, 14);
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tải font: " + e.getMessage());
        }

        String fontStyle = "";
        if (montserratFont != null) {
            fontStyle = "-fx-font-family: '" + montserratFont.getFamily() + "';";
        } else {
            System.err.println("Không tìm thấy font Montserrat-SemiBold.ttf. Sử dụng font mặc định.");
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
        topPart.setAlignment(Pos.CENTER_LEFT);
        topPart.setPadding(new Insets(20, 0, 10, 150));

        VBox imageBox = new VBox(5);
        imageBox.setAlignment(Pos.CENTER);
        imageBox.setPrefSize(130, 130);
        imageBox.setMinSize(130, 130);
        imageBox.setMaxSize(130, 130);
        imageBox.setStyle("-fx-background-color: #F0F0F0; -fx-border-color: #CCCCCC; "
                + "-fx-border-radius: 10; -fx-background-radius: 10; -fx-border-style: dashed;");
        imageBox.setCursor(Cursor.HAND);

        ImageView anhImageView = new ImageView();
        try (InputStream imageStream = getClass().getResourceAsStream("/com/thefourrestaurant/images/icon/ThayAnh.png")) {
            if (imageStream != null) {
                anhImageView.setImage(new Image(imageStream));
            } else {
                System.err.println("Không tìm thấy ảnh mặc định.");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tải ảnh mặc định: " + e.getMessage());
        }

        anhImageView.setFitWidth(150);
        anhImageView.setFitHeight(150);

        imageBox.getChildren().addAll(anhImageView);
        topPart.getChildren().add(imageBox);

        // --- Form ---
        VBox centerPart = new VBox(15);
        centerPart.setPadding(new Insets(10, 20, 20, 20));
        centerPart.setAlignment(Pos.TOP_LEFT);

        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(15);
        formGrid.setAlignment(Pos.TOP_LEFT);
        ColumnConstraints col1 = new ColumnConstraints(80);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        formGrid.getColumnConstraints().addAll(col1, col2);

        Label tenLabel = new Label("Tên:");
        tenLabel.setStyle(labelStyle);
        TextField tenTextField = new TextField();
        tenTextField.setPromptText("Nhập tên loại món ăn");
        tenTextField.setStyle(inputStyle);
        tenTextField.setMaxWidth(Double.MAX_VALUE);
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
        footerPart.setStyle("-fx-background-color: #F0F0F0; -fx-border-color: #E0E0E0; -fx-border-width: 1 0 0 0;");

        Button btnXoa = new ButtonSample("Xóa", 35, 14, 2);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button btnOke = new ButtonSample("Oke", 35, 14, 2);
        Button btnHuy = new ButtonSample("Hủy", 35, 14, 2);
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
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            File file = fileChooser.showOpenDialog(popupStage);
            if (file != null) {
                selectedImageFile = file;
                try {
                    Image image = new Image(file.toURI().toString());
                    anhImageView.setImage(image);
                    anhImageView.setFitWidth(120);
                    anhImageView.setFitHeight(120);
                    imageBox.getChildren().clear();
                    imageBox.getChildren().add(anhImageView);
                    imageBox.setStyle("-fx-background-color: white; -fx-border-color: #CCCCCC; -fx-border-radius: 10; -fx-background-radius: 10;");
                } catch (Exception ex) {
                    System.err.println("Lỗi khi tải ảnh đã chọn: " + ex.getMessage());
                }
            }
        });

        btnHuy.setOnAction(e -> popupStage.close());

        btnOke.setOnAction(e -> {
            String tenLoaiMonAn = tenTextField.getText();
            if (tenLoaiMonAn == null || tenLoaiMonAn.trim().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Tên loại món ăn không được để trống!", ButtonType.OK);
                alert.setTitle("Thiếu thông tin");
                alert.setHeaderText(null);
                alert.showAndWait();
            } else {
                result = new HashMap<>();
                result.put("name", tenLoaiMonAn);
                result.put("description", moTaTextArea.getText());
                result.put("is_shown", hienCheckBox.isSelected());
                if (selectedImageFile != null) {
                    result.put("imagePath", selectedImageFile.toURI().toString());
                }
                popupStage.close();
            }
        });

        // --- Scene ---
        Scene popupScene = new Scene(mainLayout, 580, 435);
        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            popupScene.getStylesheets().add(urlCSS.toExternalForm());
        } else {
            System.err.println("Không tìm thấy tệp CSS.");
        }

        popupStage.setScene(popupScene);
        popupStage.showAndWait();

        return result;
    }
}
