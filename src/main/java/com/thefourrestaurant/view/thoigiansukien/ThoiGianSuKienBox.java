package com.thefourrestaurant.view.thoigiansukien;

import java.util.Objects;

import com.thefourrestaurant.view.components.BaseBox;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ThoiGianSuKienBox extends BaseBox {

    private ThoiGianSuKienBox() {
        super();
        setPrefSize(220, 220);
        setMaxSize(220, 220);
        setSpacing(0);
        setPadding(Insets.EMPTY);
        setAlignment(Pos.TOP_CENTER);
    }

    public ThoiGianSuKienBox(String tenSuKien, String giamGia, String thoiGianMa, String thoiGianBatDau,
                             String thoiGianKetThuc, String[] ngayApDung, String gioApDung) {
        this.setPrefSize(220, 220);
        this.setMaxSize(220, 220);
        this.setSpacing(0);
        this.setPadding(new Insets(15));
        this.setAlignment(Pos.TOP_CENTER);
        this.getStyleClass().add("sukien-box");
        this.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 15; -fx-border-color: #e0e0e0; -fx-border-radius: 15; -fx-border-width: 1;");

        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPrefWidth(190);

        // Tên sự kiện (Header)
        Label lblTenSuKien = new Label(tenSuKien);
        lblTenSuKien.setFont(Font.font("System", FontWeight.BOLD, 20));
        lblTenSuKien.setWrapText(true);
        lblTenSuKien.setAlignment(Pos.CENTER);
        lblTenSuKien.setMaxWidth(190);
        lblTenSuKien.setStyle("-fx-text-fill: #000000;");

        // Row giảm giá và mã thời gian
        HBox rowGiamGiaVaMa = new HBox(10);
        rowGiamGiaVaMa.setAlignment(Pos.CENTER);

        // Giảm giá
        Label lblGiamGia = new Label(giamGia);
        lblGiamGia.setFont(Font.font("System", FontWeight.BOLD, 22));
        lblGiamGia.setStyle("-fx-text-fill: #000000;");

        // Mã thời gian (thoiGianMa)
        HBox maBox = new HBox(4);
        maBox.setAlignment(Pos.CENTER_LEFT);
        try {
            Image tagIcon = new Image(Objects.requireNonNull(
                    ThoiGianSuKienBox.class.getResourceAsStream("/com/thefourrestaurant/images/icon/tag.png")));
            ImageView tagView = new ImageView(tagIcon);
            tagView.setFitWidth(20);
            tagView.setFitHeight(20);
            maBox.getChildren().add(tagView);
        } catch (Exception e) {
            Label tagLabel = new Label("#");
            tagLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
            maBox.getChildren().add(tagLabel);
        }

        Label lblThoiGianMa = new Label(thoiGianMa);
        lblThoiGianMa.setFont(Font.font("System", FontWeight.BOLD, 16));
        maBox.getChildren().add(lblThoiGianMa);
        lblThoiGianMa.setStyle("-fx-text-fill: #000000;");
        rowGiamGiaVaMa.getChildren().addAll(lblGiamGia, maBox);

        // Row ngày áp dụng với icon lịch
        HBox rowNgayApDung = new HBox(8);
        rowNgayApDung.setAlignment(Pos.CENTER_LEFT);

        // Icon lịch
        try {
            Image calendarIcon = new Image(Objects.requireNonNull(
                    ThoiGianSuKienBox.class.getResourceAsStream("/com/thefourrestaurant/images/icon/calendar.png")));
            ImageView calendarView = new ImageView(calendarIcon);
            calendarView.setFitWidth(24);
            calendarView.setFitHeight(24);
            rowNgayApDung.getChildren().add(calendarView);
        } catch (Exception e) {
            Label calIcon = new Label("📅");
            calIcon.setFont(Font.font(20));
            rowNgayApDung.getChildren().add(calIcon);
        }

        Label lblNgayApDung = new Label(thoiGianBatDau + "        -        " + thoiGianKetThuc);
        lblNgayApDung.setFont(Font.font("System", FontWeight.NORMAL, 14));
        lblNgayApDung.setStyle("-fx-text-fill: #000000;");

        rowNgayApDung.getChildren().add(lblNgayApDung);

        // Row các ngày trong tuần
        HBox rowNgayTrongTuan = new HBox(4);
        rowNgayTrongTuan.setAlignment(Pos.CENTER);
        rowNgayTrongTuan.setPrefHeight(40);

        String[] allDays = {"T2", "T3", "T4", "T5", "T6", "T7", "CN"};
        for (String day : allDays) {
            Label lblDay = new Label(day);
            lblDay.setFont(Font.font("System", FontWeight.BOLD, 14));
            lblDay.setAlignment(Pos.CENTER);
            lblDay.setPrefSize(24, 24);
            lblDay.setMinSize(24, 24);
            lblDay.setMaxSize(24, 24);

            boolean isActive = false;
            if (ngayApDung != null) {
                for (String activeDay : ngayApDung) {
                    if (day.equals(activeDay)) {
                        isActive = true;
                        break;
                    }
                }
            }

            if (isActive) {
                lblDay.setStyle("-fx-text-fill: #000000; -fx-background-color: transparent;");
            } else {
                lblDay.setStyle("-fx-text-fill: #cccccc; -fx-background-color: transparent;");
            }

            rowNgayTrongTuan.getChildren().add(lblDay);
        }

        // Row giờ áp dụng với icon đồng hồ
        HBox rowGioApDung = new HBox(8);
        rowGioApDung.setAlignment(Pos.CENTER_LEFT);

        // Icon đồng hồ
        try {
            Image clockIcon = new Image(Objects.requireNonNull(
                    ThoiGianSuKienBox.class.getResourceAsStream("/com/thefourrestaurant/images/icon/clock.png")));
            ImageView clockView = new ImageView(clockIcon);
            clockView.setFitWidth(24);
            clockView.setFitHeight(24);
            rowGioApDung.getChildren().add(clockView);
        } catch (Exception e) {
            Label clockLabel = new Label("🕐");
            clockLabel.setFont(Font.font(20));
            rowGioApDung.getChildren().add(clockLabel);
        }

        Label lblGioApDung = new Label(gioApDung);
        lblGioApDung.setFont(Font.font("System", FontWeight.NORMAL, 14));
        lblGioApDung.setStyle("-fx-text-fill: #000000;");

        rowGioApDung.getChildren().add(lblGioApDung);

        contentBox.getChildren().addAll(
                lblTenSuKien,
                rowGiamGiaVaMa,
                rowNgayApDung,
                rowNgayTrongTuan,
                rowGioApDung
        );

        this.setOnMouseEntered(e -> {
            this.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 15; " +
                    "-fx-border-color: #d0d0d0; -fx-border-radius: 15; -fx-border-width: 1; " +
                    "-fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 8, 0, 0, 4);");
        });
        this.setOnMouseExited(e -> {
            this.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 15; " +
                    "-fx-border-color: #e0e0e0; -fx-border-radius: 15; -fx-border-width: 1;");
        });

        this.getChildren().add(contentBox);
    }

    // Box "Thêm sự kiện mới"
    public static ThoiGianSuKienBox createThemMoiBox() {
        ThoiGianSuKienBox hop = new ThoiGianSuKienBox();
        hop.setAlignment(Pos.CENTER);
        hop.setSpacing(10);
        hop.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 15; " +
                "-fx-border-color: #e0e0e0; -fx-border-radius: 15; -fx-border-width: 2;");

        try {
            Image plusImage = new Image(Objects.requireNonNull(
                    ThoiGianSuKienBox.class.getResourceAsStream("/com/thefourrestaurant/images/icon/Them.png")));
            ImageView plusImageView = new ImageView(plusImage);
            plusImageView.setFitWidth(40);
            plusImageView.setFitHeight(40);
            hop.getChildren().add(plusImageView);
        } catch (Exception e) {
            Label plusLabel = new Label("+");
            plusLabel.setFont(Font.font("System", FontWeight.BOLD, 36));
            plusLabel.setStyle("-fx-text-fill: #888888;");
            hop.getChildren().add(plusLabel);
        }

        Label themMoiLabel = new Label("Thêm sự kiện mới");
        themMoiLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        themMoiLabel.setStyle("-fx-text-fill: #666666;");

        hop.getChildren().add(themMoiLabel);

        hop.setOnMouseEntered(e -> {
            hop.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 15; " +
                    "-fx-border-color: #6A4C34; -fx-border-radius: 15; -fx-border-width: 2; " +
                    "-fx-cursor: hand;");
        });
        hop.setOnMouseExited(e -> {
            hop.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 15; " +
                    "-fx-border-color: #e0e0e0; -fx-border-radius: 15; -fx-border-width: 2;");
        });

        return hop;
    }
}
