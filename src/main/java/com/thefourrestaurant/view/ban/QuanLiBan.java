package com.thefourrestaurant.view.ban;

import java.util.ArrayList;
import java.util.List;

import com.thefourrestaurant.model.Ban;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class QuanLiBan extends Pane {

    public QuanLiBan() {
        this.setPadding(new Insets(20));
        this.setStyle("-fx-background-color: #F5F5F5;");
    }

    public void hienThiBanTheoTang(String maTang) {
        this.getChildren().clear();
        List<Ban> dsBan = taoDanhSachBanCung(maTang);

        for (Ban b : dsBan) {
            taoBan(this, b);
        }
    }

    private List<Ban> taoDanhSachBanCung(String maTang) {
        List<Ban> list = new ArrayList<>();

        if (maTang.equals("TG000001")) { // 🔸 Tầng 1
            list.add(new Ban("BA000001", "Bàn 1-T1", "Trống", 100, 100, "TG000001", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_8.png"));
            list.add(new Ban("BA000002", "Bàn 2-T1", "Trống", 100, 300, "TG000001", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_8.png"));
            list.add(new Ban("BA000003", "Bàn 3-T1", "Trống", 100, 500, "TG000001", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_8.png"));
            list.add(new Ban("BA000004", "Bàn 4-T1", "Trống", 400, 100, "TG000001", "LB000002", "/com/thefourrestaurant/images/Ban/Ban_6.png"));
            list.add(new Ban("BA000005", "Bàn 5-T1", "Trống", 400, 300, "TG000001", "LB000002", "/com/thefourrestaurant/images/Ban/Ban_6.png"));
            list.add(new Ban("BA000006", "Bàn 6-T1", "Trống", 400, 500, "TG000001", "LB000002", "/com/thefourrestaurant/images/Ban/Ban_6.png"));
            list.add(new Ban("BA000007", "Bàn 7-T1", "Trống", 700, 150, "TG000001", "LB000003", "/com/thefourrestaurant/images/Ban/Ban_4.png"));
            list.add(new Ban("BA000008", "Bàn 8-T1", "Trống", 700, 350, "TG000001", "LB000003", "/com/thefourrestaurant/images/Ban/Ban_4.png"));
        }


        return list;
    }

    private void taoBan(Pane pane, Ban ban) {
        Image img;
        try {
            img = new Image(getClass().getResourceAsStream(ban.getAnhBan()));
        } catch (Exception e) {
            img = new Image(getClass().getResourceAsStream("/com/thefourrestaurant/images/Ban/Ban_8.png"));
        }

        ImageView imgBan = new ImageView(img);
        imgBan.setFitWidth(200);
        imgBan.setFitHeight(150);
        imgBan.setPreserveRatio(true);

        Label lblTenBan = new Label(ban.getTenBan());
        lblTenBan.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333;");

        StackPane khungBan = new StackPane(imgBan, lblTenBan);
        khungBan.setLayoutX(ban.getToaDoX());
        khungBan.setLayoutY(ban.getToaDoY());

        khungBan.setOnMouseEntered(e -> khungBan.setStyle("-fx-effect: dropshadow(gaussian, gray, 10, 0, 0, 0);"));
        khungBan.setOnMouseExited(e -> khungBan.setStyle(""));

        pane.getChildren().add(khungBan);
    }
}
