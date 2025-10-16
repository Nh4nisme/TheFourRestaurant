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

public class MonAnController {

    private Map<String, Object> result = null;
    private File selectedImageFile = null;

    public Map<String, Object> themMoiMonAn() {
        this.result = null;
        this.selectedImageFile = null;

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Thêm Món Ăn");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #FDFCF9;");

        // ===== HEADER =====
        Label headerLabel = new Label("Thêm món ăn");
        headerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #F5C84C;");
        HBox header = new HBox(headerLabel);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 20, 10, 20));
        header.setStyle("-fx-background-color: #1E424D;");

        // ===== FORM =====
        GridPane form = new GridPane();
        form.setPadding(new Insets(20));
        form.setVgap(12);
        form.setHgap(15);

        String labelStyle = "-fx-text-fill: #E19E11; -fx-font-size: 14px;";
        String inputStyle = "-fx-text-fill: #1E424D; -fx-background-radius: 8; -fx-border-color: #CFCFCF; -fx-border-radius: 8;";

        Label tenLabel = new Label("Tên:");
        tenLabel.setStyle(labelStyle);
        TextField tenField = new TextField();
        tenField.setStyle(inputStyle);

        Label maLabel = new Label("Mã Món Ăn:");
        maLabel.setStyle(labelStyle);
        TextField maField = new TextField("tạo tự động");
        maField.setEditable(false);
        maField.setStyle(inputStyle);

        Label giaLabel = new Label("Giá:");
        giaLabel.setStyle(labelStyle);
        TextField giaField = new TextField();
        giaField.setStyle(inputStyle);

        Label hinhLabel = new Label("Hình ảnh:");
        hinhLabel.setStyle(labelStyle);
        Hyperlink attachLink = new Hyperlink("attach an image");
        attachLink.setStyle("-fx-text-fill: blue; -fx-underline: true;");

        Label hienThiLabel = new Label("Hiển thị:");
        hienThiLabel.setStyle(labelStyle);
        CheckBox hienThiCheck = new CheckBox();

        // Thêm các trường vào form
        form.add(tenLabel, 0, 0); form.add(tenField, 1, 0);
        form.add(maLabel, 0, 1); form.add(maField, 1, 1);
        form.add(giaLabel, 0, 2); form.add(giaField, 1, 2);
        form.add(hinhLabel, 0, 3); form.add(attachLink, 1, 3);
        form.add(hienThiLabel, 0, 4); form.add(hienThiCheck, 1, 4);

        // ===== TÙY CHỈNH NÂNG CAO =====
        TitledPane advancedPane = new TitledPane();
        advancedPane.setText("Tùy chỉnh nâng cao");
        advancedPane.setCollapsible(true);
        advancedPane.setExpanded(false);

        GridPane advForm = new GridPane();
        advForm.setVgap(10);
        advForm.setHgap(15);
        advForm.setPadding(new Insets(10));

        Label vatLabel = new Label("VAT:");
        vatLabel.setStyle(labelStyle);
        TextField vatField = new TextField();
        vatField.setStyle(inputStyle);

        Label moTaLabel = new Label("Mô tả:");
        moTaLabel.setStyle(labelStyle);
        TextField moTaField = new TextField();
        moTaField.setStyle(inputStyle);

        Label kmLabel = new Label("Khuyến mãi:");
        kmLabel.setStyle(labelStyle);
        Hyperlink kmLink = new Hyperlink("+ Chọn khuyến mãi");
        kmLink.setStyle("-fx-text-fill: blue;");

        Label soLuongLabel = new Label("Số lượng:");
        soLuongLabel.setStyle(labelStyle);
        TextField soLuongField = new TextField();
        soLuongField.setStyle(inputStyle);

        advForm.add(vatLabel, 0, 0); advForm.add(vatField, 1, 0);
        advForm.add(moTaLabel, 0, 1); advForm.add(moTaField, 1, 1);
        advForm.add(kmLabel, 0, 2); advForm.add(kmLink, 1, 2);
        advForm.add(soLuongLabel, 0, 3); advForm.add(soLuongField, 1, 3);

        advancedPane.setContent(advForm);

        VBox centerBox = new VBox(10, form, advancedPane);
        centerBox.setPadding(new Insets(10, 20, 20, 20));

        // ===== FOOTER =====
        Button btnOK = new Button("OK");
        Button btnCancel = new Button("Hủy");

        btnOK.setStyle("-fx-background-color: #F5C84C; -fx-text-fill: #1E424D; -fx-font-weight: bold; "
                + "-fx-background-radius: 20; -fx-pref-width: 80;");
        btnCancel.setStyle("-fx-background-color: #E0E0E0; -fx-text-fill: #1E424D; "
                + "-fx-background-radius: 20; -fx-pref-width: 80;");

        HBox footer = new HBox(15, btnOK, btnCancel);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(15, 0, 20, 0));

        // ===== EVENT =====
        attachLink.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Chọn Ảnh");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
            File file = fileChooser.showOpenDialog(popupStage);
            if (file != null) selectedImageFile = file;
        });

        btnCancel.setOnAction(e -> popupStage.close());

        btnOK.setOnAction(e -> {
            if (tenField.getText().trim().isEmpty() || giaField.getText().trim().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng nhập tên và giá món ăn!", ButtonType.OK);
                alert.showAndWait();
            } else {
                result = new HashMap<>();
                result.put("ten", tenField.getText());
                result.put("gia", giaField.getText());
                result.put("vat", vatField.getText());
                result.put("moTa", moTaField.getText());
                result.put("soLuong", soLuongField.getText());
                result.put("hienThi", hienThiCheck.isSelected());
                if (selectedImageFile != null) result.put("imagePath", selectedImageFile.toURI().toString());
                popupStage.close();
            }
        });

        advancedPane.expandedProperty().addListener((obs, wasExpanded, isNowExpanded) -> {
            if (isNowExpanded) {
                popupStage.setHeight(580);
            } else {
                popupStage.setHeight(480);
            }
        });

        // ===== SCENE =====
        root.setTop(header);
        root.setCenter(centerBox);
        root.setBottom(footer);

        Scene scene = new Scene(root, 500, 380);
        popupStage.setScene(scene);
        popupStage.showAndWait();

        return result;
    }

}
