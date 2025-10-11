package com.thefourrestaurant.view.components;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MonAnBox extends BaseBox {
    public MonAnBox(String ten, String gia, String icon) {
        this.setPadding(new Insets(10));
        this.setPrefSize(150, 200);

        Label bieuTuong = new Label(icon);
        bieuTuong.setFont(Font.font(40));

        Label tenMon = new Label(ten);
        tenMon.setFont(Font.font("System", FontWeight.BOLD, 13));
        tenMon.setTextFill(Color.web("#2C5F5F"));

        Label lblGia = new Label(gia + " VND");
        lblGia.setFont(Font.font(12));
        lblGia.setTextFill(Color.web("#2C5F5F"));

        this.getChildren().addAll(bieuTuong, tenMon, lblGia);
    }
}
