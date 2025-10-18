package com.thefourrestaurant.view;

import java.util.List;

import com.thefourrestaurant.DAO.BanDAO;
import com.thefourrestaurant.model.Ban;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class QuanLiBan extends Pane {

    private final BanDAO banDAO = new BanDAO();

    public QuanLiBan() {
        this.setPrefSize(1200, 700);
        this.setPadding(new Insets(20));
        this.setStyle("-fx-background-color: #F5F5F5;");
    }

    // 🔹 Hiển thị bàn theo tầng (lấy từ SQL)
    public void hienThiBanTheoTang(String maTang) {
        this.getChildren().clear();

        Platform.runLater(() -> {
            if (this.getWidth() > 0 && this.getHeight() > 0) {
                setBackgroundTheoTang(maTang);
            } else {
                this.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> setBackgroundTheoTang(maTang));
            }
        });

        // 🔹 Lấy danh sách bàn từ cơ sở dữ liệu
        List<Ban> dsBan = banDAO.getByTang(maTang);

        if (dsBan.isEmpty()) {
            Label lblThongBao = new Label("⚠️ Không có bàn nào trong tầng này.");
            lblThongBao.setStyle("-fx-font-size: 18px; -fx-text-fill: #666;");
            this.getChildren().add(lblThongBao);
            return;
        }

        for (Ban b : dsBan) {
            taoBan(this, b);
        }
    }

    // 🔹 Đặt background tương ứng với tầng
    private void setBackgroundTheoTang(String maTang) {
        String path = switch (maTang) {
            case "TG000001" -> "/com/thefourrestaurant/images/BG_Tang1.png";
            case "TG000002" -> "/com/thefourrestaurant/images/BG_Tang2.png";
            case "TG000003" -> "/com/thefourrestaurant/images/BG_Tang3.png";
            case "TG000004" -> "/com/thefourrestaurant/images/BG_Tang4.png";
            case "TG000005" -> "/com/thefourrestaurant/images/BG_Tang5.png";
            case "TG000006" -> "/com/thefourrestaurant/images/BG_Tang6.png";
            case "TG000007" -> "/com/thefourrestaurant/images/BG_Tang7.png";
            default -> "/com/thefourrestaurant/images/background/bg_default.jpg";
        };

        try {
            System.out.println("Load background path: " + path);
            Image anhNen = new Image(getClass().getResource(path).toExternalForm());

            Runnable updateBackground = () -> {
                BackgroundSize bgs = new BackgroundSize(
                        this.getWidth(), this.getHeight(), false, false, false, false
                );
                BackgroundImage bgImg = new BackgroundImage(
                        anhNen,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        bgs
                );
                this.setBackground(new Background(bgImg));
            };

            updateBackground.run();

            this.widthProperty().addListener((obs, oldVal, newVal) -> updateBackground.run());
            this.heightProperty().addListener((obs, oldVal, newVal) -> updateBackground.run());

            System.out.println("✅ Background set thành công: " + path);
        } catch (Exception e) {
            e.printStackTrace();
            this.setStyle("-fx-background-color: lightgray;");
        }
    }

    // 🔹 Tạo từng bàn
    private void taoBan(Pane pane, Ban ban) {
        Image img;
        try {
            img = new Image(getClass().getResourceAsStream(ban.getAnhBan()));
        } catch (Exception e) {
            img = new Image(getClass().getResourceAsStream("/com/thefourrestaurant/images/Ban/Ban_8.png"));
        }

        ImageView imgBan = new ImageView(img);
        imgBan.setFitWidth(180);
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
