package com.thefourrestaurant.view;

import com.thefourrestaurant.model.Ban;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import java.util.ArrayList;
import java.util.List;

public class QuanLiBan extends Pane {

    public QuanLiBan() {
        this.setPrefSize(1200, 700);
        this.setPadding(new Insets(20));
        this.setStyle("-fx-background-color: #F5F5F5;");
    }

    public void hienThiBanTheoTang(String maTang) {
        this.getChildren().clear();

        Platform.runLater(() -> {
            if (this.getWidth() > 0 && this.getHeight() > 0) {
                setBackgroundTheoTang(maTang);
            } else {
                this.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> setBackgroundTheoTang(maTang));
            }
        });


        List<Ban> dsBan = taoDanhSachBanCung(maTang);
        for (Ban b : dsBan) {
            taoBan(this, b);
        }
    }

    private void setBackgroundTheoTang(String maTang) {
        String path = switch (maTang) {
            case "TG000001" -> "/com/thefourrestaurant/images/BG_Tang1.png";
            case "TG000002" -> "/com/thefourrestaurant/images/BG_Tang2.png";
            case "TG000003" -> "/com/thefourrestaurant/images/BG_Tang3.png";
            case "TG000004" -> "/com/thefourrestaurant/images/BG_Tang4.png";
            case "TG000005" -> "/com/thefourrestaurant/images/BG_Tang2.png";
            case "TG000006" -> "/com/thefourrestaurant/images/BG_Tang6.png";
            case "TG000007" -> "/com/thefourrestaurant/images/BG_Tang7.png";
            default -> "/com/thefourrestaurant/images/background/bg_default.jpg";
        };

        try {
            System.out.println("Load background path: " + path);
            System.out.println("URL: " + getClass().getResource(path));

            Image anhNen = new Image(getClass().getResource(path).toExternalForm());

            // 🔹 Hàm tạo background để tái sử dụng
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

            // 🔹 Cập nhật lần đầu
            updateBackground.run();

            // 🔹 Theo dõi thay đổi kích thước
            this.widthProperty().addListener((obs, oldVal, newVal) -> updateBackground.run());
            this.heightProperty().addListener((obs, oldVal, newVal) -> updateBackground.run());

            System.out.println("✅ Background set thành công: " + path);
        } catch (Exception e) {
            e.printStackTrace();
            this.setStyle("-fx-background-color: lightgray;");
        }
    }


    private List<Ban> taoDanhSachBanCung(String maTang) {
        List<Ban> list = new ArrayList<>();
        if (maTang.equals("TG000001")) {
            list.add(new Ban("BA000001", "Bàn 1-T1", "Trống", 100, 100, "TG000001", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_8.png"));
            list.add(new Ban("BA000002", "Bàn 2-T1", "Trống", 100, 300, "TG000001", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_8.png"));
            list.add(new Ban("BA000003", "Bàn 3-T1", "Trống", 100, 500, "TG000001", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_8.png"));
            list.add(new Ban("BA000004", "Bàn 4-T1", "Trống", 400, 100, "TG000001", "LB000002", "/com/thefourrestaurant/images/Ban/Ban_6.png"));
            list.add(new Ban("BA000005", "Bàn 5-T1", "Trống", 400, 300, "TG000001", "LB000002", "/com/thefourrestaurant/images/Ban/Ban_6.png"));
            list.add(new Ban("BA000006", "Bàn 6-T1", "Trống", 400, 500, "TG000001", "LB000002", "/com/thefourrestaurant/images/Ban/Ban_6.png"));
            list.add(new Ban("BA000007", "Bàn 7-T1", "Trống", 700, 150, "TG000001", "LB000003", "/com/thefourrestaurant/images/Ban/Ban_4.png"));
            list.add(new Ban("BA000008", "Bàn 8-T1", "Trống", 700, 350, "TG000001", "LB000003", "/com/thefourrestaurant/images/Ban/Ban_4.png"));
        }
        else if(maTang.equals("TG000002")) {
            list.add(new Ban("BA000001", "Bàn 1-T1", "Trống", 50, 100, "TG000002", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_4.png"));
            list.add(new Ban("BA000002", "Bàn 2-T1", "Trống", 250, 100, "TG000002", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_4.png"));
            list.add(new Ban("BA000003", "Bàn 3-T1", "Trống", 650, 100, "TG000002", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_4.png"));
            list.add(new Ban("BA000001", "Bàn 1-T1", "Trống", 50, 300, "TG000002", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_6.png"));
            list.add(new Ban("BA000002", "Bàn 2-T1", "Trống", 250, 300, "TG000002", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_6.png"));
            list.add(new Ban("BA000003", "Bàn 3-T1", "Trống", 650, 300, "TG000002", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_6.png"));
            
            list.add(new Ban("BA000002", "Bàn 2-T1", "Trống", 50, 550, "TG000002", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_2.png"));
            list.add(new Ban("BA000003", "Bàn 3-T1", "Trống", 250, 550, "TG000002", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_2.png"));
            list.add(new Ban("BA000001", "Bàn 1-T1", "Trống", 450, 550, "TG000002", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_2.png"));
            list.add(new Ban("BA000002", "Bàn 2-T1", "Trống", 650, 550, "TG000002", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_2.png"));
            list.add(new Ban("BA000003", "Bàn 3-T1", "Trống", 850, 550, "TG000002", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_2.png"));
        }
        else if(maTang.equals("TG000003")) {
            list.add(new Ban("BA000001", "Bàn 1-T1", "Trống", 100, 100, "TG000003", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_6.png"));
            list.add(new Ban("BA000002", "Bàn 2-T1", "Trống", 400, 100, "TG000003", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_6.png"));
            list.add(new Ban("BA000003", "Bàn 3-T1", "Trống", 700, 100, "TG000003", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_6.png"));
            list.add(new Ban("BA000004", "Bàn 4-T1", "Trống", 300, 300, "TG000003", "LB000002", "/com/thefourrestaurant/images/Ban/Ban_8.png"));
            list.add(new Ban("BA000001", "Bàn 5-T1", "Trống", 600, 300, "TG000003", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_8.png"));
            list.add(new Ban("BA000002", "Bàn 2-T1", "Trống", 100, 500, "TG000003", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_6.png"));
            list.add(new Ban("BA000003", "Bàn 3-T1", "Trống", 400, 500, "TG000003", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_6.png"));
            list.add(new Ban("BA000004", "Bàn 4-T1", "Trống", 700, 500, "TG000003", "LB000002", "/com/thefourrestaurant/images/Ban/Ban_6.png"));
        }
        else if(maTang.equals("TG000004")) {
            list.add(new Ban("BA000001", "Bàn 1-T1", "Trống", 200, 300, "TG000004", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_8_Tron.png"));
            list.add(new Ban("BA000002", "Bàn 2-T1", "Trống", 500, 300, "TG000004", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_8_Tron.png"));
        }
        else if(maTang.equals("TG000005")) {
            list.add(new Ban("BA000001", "Bàn 1-T1", "Trống", 50, 100, "TG000005", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_4.png"));
            list.add(new Ban("BA000002", "Bàn 2-T1", "Trống", 250, 100, "TG000005", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_4.png"));
            list.add(new Ban("BA000003", "Bàn 3-T1", "Trống", 650, 100, "TG000005", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_4.png"));
            list.add(new Ban("BA000001", "Bàn 1-T1", "Trống", 50, 300, "TG000005", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_6.png"));
            list.add(new Ban("BA000002", "Bàn 2-T1", "Trống", 250, 300, "TG000005", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_6.png"));
            list.add(new Ban("BA000003", "Bàn 3-T1", "Trống", 650, 300, "TG000005", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_6.png"));
            
            list.add(new Ban("BA000002", "Bàn 2-T1", "Trống", 50, 550, "TG000005", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_2.png"));
            list.add(new Ban("BA000003", "Bàn 3-T1", "Trống", 250, 550, "TG000005", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_2.png"));
            list.add(new Ban("BA000001", "Bàn 1-T1", "Trống", 450, 550, "TG000005", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_2.png"));
            list.add(new Ban("BA000002", "Bàn 2-T1", "Trống", 650, 550, "TG000005", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_2.png"));
            list.add(new Ban("BA000003", "Bàn 3-T1", "Trống", 850, 550, "TG000005", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_2.png"));
        }
        else if(maTang.equals("TG000007")) {
            list.add(new Ban("BA000001", "Bàn 1-T1", "Trống", 100, 100, "TG000007", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_8_Doc.png"));
            list.add(new Ban("BA000002", "Bàn 2-T1", "Trống", 300, 100, "TG000007", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_8_Doc.png"));
            list.add(new Ban("BA000003", "Bàn 3-T1", "Trống", 500, 100, "TG000007", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_8_Doc.png"));
            list.add(new Ban("BA000001", "Bàn 1-T1", "Trống", 100, 300, "TG000007", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_8_Doc.png"));
            list.add(new Ban("BA000002", "Bàn 2-T1", "Trống", 300, 300, "TG000007", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_8_Doc.png"));
            list.add(new Ban("BA000003", "Bàn 3-T1", "Trống", 500, 300, "TG000007", "LB000001", "/com/thefourrestaurant/images/Ban/Ban_8_Doc.png"));
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
