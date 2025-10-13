package com.thefourrestaurant.view.components.sidebar;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SideBarHoaDon extends VBox {
    public SideBarHoaDon() {
        setPrefWidth(400);
        setStyle("-fx-background-color: #2E5C6E;");
        setPadding(new Insets(20));
        setSpacing(15);

        Label lblTitle = new Label("Hóa đơn");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");

        Button btnNgay = new Button("Theo ngày");
        Button btnThang = new Button("Theo tháng");
        Button btnNam = new Button("Theo năm");

        for (Button btn : new Button[]{btnNgay, btnThang, btnNam}) {
            btn.setStyle("-fx-background-color: #DDB248; -fx-text-fill: #1E424D; -fx-font-weight: bold;");
        }

        getChildren().addAll(lblTitle, btnNgay, btnThang, btnNam);
        setUserData("Hóa đơn");
    }
}
