package com.thefourrestaurant.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GiaoDienNhapTien {
    private Stage stage;
    private double soTien;
    private boolean daNhapTien = false;

    public GiaoDienNhapTien(Stage stage) {
        this.stage = stage;
    }

    public void hienThi(Runnable onSuccess) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setMaxWidth(500);
        root.setStyle("-fx-background-color: #fff; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10,0,0,2);");

        Label lblTitle = new Label("Nhập số tiền hiện có trong két");
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField txtSoTien = new TextField();
        txtSoTien.setPromptText("Số tiền trong két");
        txtSoTien.setMaxWidth(250);

        Button btnXacNhan = new Button("Xác nhận");
        btnXacNhan.setPrefHeight(50);
        btnXacNhan.setPrefWidth(250);
        btnXacNhan.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 10;");

        btnXacNhan.setOnAction(e -> {
            try {
                soTien = Double.parseDouble(txtSoTien.getText().trim());
                daNhapTien = true;
                if (onSuccess != null) onSuccess.run();
            } catch (NumberFormatException ex) {
                txtSoTien.setStyle("-fx-border-color: red;");
            }
        });

        root.getChildren().addAll(lblTitle, txtSoTien, btnXacNhan);
        Scene scene = new Scene(root, 500, 300);
        stage.setScene(scene);
        stage.show();
    }

    public double getSoTien() {
        return soTien;
    }

    public boolean isDaNhapTien() {
        return daNhapTien;
    }
}
