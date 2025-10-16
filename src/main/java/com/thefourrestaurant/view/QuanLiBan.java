package com.thefourrestaurant.view;

import com.thefourrestaurant.DAO.BanDAO;
import com.thefourrestaurant.model.Ban;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import java.util.List;

public class QuanLiBan extends Pane {

    private BanDAO banDAO = new BanDAO();

    public QuanLiBan() {
        this.setPadding(new Insets(20));
        this.setStyle("-fx-background-color: #F5F5F5;");
    }

    // 🔹 Hiển thị bàn theo mã tầng (ví dụ: "TANG01")
    public void hienThiBanTheoTang(String maTang) {
        this.getChildren().clear(); // Xóa bàn cũ

        List<Ban> dsBan = banDAO.getByTang(maTang);
        for (Ban b : dsBan) {
            taoBan(this, b);
        }
    }

    // 🪑 Tạo bàn từ model
    private void taoBan(Pane pane, Ban ban) {
        // Sử dụng link ảnh từ model
        Image img;
        try {
            img = new Image(getClass().getResourceAsStream(ban.getAnhBan()));
        } catch (Exception e) {
            // Nếu không tìm thấy ảnh, dùng ảnh mặc định
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
