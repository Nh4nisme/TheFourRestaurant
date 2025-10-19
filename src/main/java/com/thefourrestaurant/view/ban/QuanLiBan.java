package com.thefourrestaurant.view.ban;

import java.util.List;

import com.thefourrestaurant.DAO.BanDAO;
import com.thefourrestaurant.model.Ban;
import com.thefourrestaurant.view.components.ButtonSample;
import com.thefourrestaurant.view.components.NavBar;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class QuanLiBan extends VBox {

    private final BanDAO banDAO = new BanDAO();
    private final Pane khuVucBan = new Pane(); // nơi hiển thị bàn
    private final Label lblBreadcrumb = new Label();

    public QuanLiBan() {
        // === Cấu hình chính cho layout ===
        this.setPrefSize(1200, 700);
        this.setSpacing(0);
        this.setAlignment(Pos.TOP_CENTER);
        this.setStyle("-fx-background-color: #F5F5F5;");

        // === Breadcrumb ===
        lblBreadcrumb.setText("Trang chủ / Quản lý bàn");
        lblBreadcrumb.setStyle("-fx-text-fill: #E5D595; -fx-font-size: 18px; -fx-font-weight: bold;");
        VBox khungDuongDan = new VBox(lblBreadcrumb);
        khungDuongDan.setStyle("-fx-background-color: #673E1F;");
        khungDuongDan.setAlignment(Pos.CENTER_LEFT);
        khungDuongDan.setPadding(new Insets(10, 20, 10, 20));
        khungDuongDan.setPrefHeight(40);
        khungDuongDan.setMaxWidth(Double.MAX_VALUE);

        //Toolbar
        ToolBar toolBar = new ToolBar(
                new ButtonSample("Thêm bàn",45,16,3),
                new ButtonSample("Lưu sơ đồ",45,16,3)
        );
        toolBar.setStyle("-fx-background-color: #1E424D");
        toolBar.setPadding(new Insets(10, 10, 10, 10));

        VBox thanhTren = new VBox(khungDuongDan,toolBar);
        thanhTren.setSpacing(0);
        thanhTren.setAlignment(Pos.CENTER_LEFT);
        thanhTren.setPrefWidth(Double.MAX_VALUE);
        HBox.setHgrow(thanhTren, Priority.ALWAYS);

        // === Khu vực hiển thị bàn ===
        khuVucBan.setPadding(new Insets(20));
        khuVucBan.setPrefSize(1000, 600);
        khuVucBan.setStyle("-fx-background-color: #F5F5F5;");
        VBox.setVgrow(khuVucBan, Priority.ALWAYS);

        // === Gắn vào layout ===
        this.getChildren().addAll(thanhTren, khuVucBan);
    }

    // Hiển thị bàn theo tầng
    public void hienThiBanTheoTang(String maTang) {
        khuVucBan.getChildren().clear();

        lblBreadcrumb.setText("Trang chủ / Quản lý bàn / Tầng " + maTang.replace("TG00000", ""));

        Platform.runLater(() -> {
            if (khuVucBan.getWidth() > 0 && khuVucBan.getHeight() > 0) {
                setBackgroundTheoTang(maTang);
            } else {
                khuVucBan.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> setBackgroundTheoTang(maTang));
            }
        });

        List<Ban> dsBan = banDAO.getByTang(maTang);

        if (dsBan.isEmpty()) {
            Label lblThongBao = new Label("⚠️ Không có bàn nào trong tầng này.");
            lblThongBao.setStyle("-fx-font-size: 18px; -fx-text-fill: #666;");
            khuVucBan.getChildren().add(lblThongBao);
            return;
        }

        for (Ban b : dsBan) {
            taoBan(khuVucBan, b);
        }
    }

    // 🔹 Đặt background theo tầng
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
            Image anhNen = new Image(getClass().getResource(path).toExternalForm());

            Runnable updateBackground = () -> {
                BackgroundSize bgs = new BackgroundSize(
                        khuVucBan.getWidth(), khuVucBan.getHeight(), false, false, false, false
                );
                BackgroundImage bgImg = new BackgroundImage(
                        anhNen,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        bgs
                );
                khuVucBan.setBackground(new Background(bgImg));
            };

            updateBackground.run();
            khuVucBan.widthProperty().addListener((obs, oldVal, newVal) -> updateBackground.run());
            khuVucBan.heightProperty().addListener((obs, oldVal, newVal) -> updateBackground.run());

        } catch (Exception e) {
            e.printStackTrace();
            khuVucBan.setStyle("-fx-background-color: lightgray;");
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