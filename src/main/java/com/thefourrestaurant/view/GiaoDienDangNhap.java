package com.thefourrestaurant.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Objects;

public class GiaoDienDangNhap {

    private static final String COLOR_TEAL = "#1E424D";
    private static final String COLOR_CARD = "#768894";
    private static final String COLOR_CARD_INNER = "#6E808C";
    private static final String COLOR_GOLD = "#DDB248";
    private static final String FONT_PATH = "/com/thefourrestaurant/fonts/Montserrat-SemiBold.ttf";

    public void show(Stage stage) {
        HBox root = new HBox();

        Font montserrat = Font.loadFont(getClass().getResourceAsStream(FONT_PATH), 16);

        StackPane leftPane = new StackPane();
        leftPane.setStyle("-fx-background-color: " + COLOR_TEAL + ";");
        leftPane.setPadding(new Insets(24));

        VBox leftContent = new VBox(20);
        leftContent.setAlignment(Pos.TOP_CENTER);

        ImageView logo = new ImageView(getImage("/com/thefourrestaurant/images/logo.png"));
        logo.setPreserveRatio(true);
        logo.setFitWidth(420);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox card = new VBox(16);
        card.setPadding(new Insets(24));
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(520);
        card.setStyle(
            "-fx-background-color: " + COLOR_CARD + ";" +
            "-fx-background-radius: 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 16, 0, 0, 4);"
        );

        TextField username = new TextField();
        username.setPromptText("Tài Khoản");
        styleField(username);

        PasswordField password = new PasswordField();
        password.setPromptText("Mật Khẩu");
        styleField(password);

        Button loginBtn = new Button("Đăng Nhập");
        loginBtn.setDefaultButton(true);
        loginBtn.setPrefHeight(44);
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setStyle(
                "-fx-background-color: " + COLOR_GOLD + ";" +
                "-fx-background-radius: 10;" +
                "-fx-text-fill: #2a2a2a;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 16px;"
        );
        if (montserrat != null) loginBtn.setFont(montserrat);

        Runnable tryLogin = () -> {
            if (username.getText().trim().isEmpty() || password.getText().trim().isEmpty()) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Vui lòng nhập đầy đủ Tài Khoản và Mật Khẩu.", ButtonType.OK);
                a.initOwner(stage);
                a.showAndWait();
                return;
            }
            new GiaoDienChinh().show(stage);
        };
        loginBtn.setOnAction(e -> tryLogin.run());
        password.setOnAction(e -> tryLogin.run());

        card.getChildren().addAll(username, password, loginBtn);

        leftContent.getChildren().addAll(logo, spacer, card);
        leftPane.getChildren().add(leftContent);

        StackPane rightPane = new StackPane();
        Image motif = getImage("/com/thefourrestaurant/images/motif.png");
        BackgroundSize bgs = new BackgroundSize(100, 100, true, true, true, false);
        rightPane.setBackground(new Background(new BackgroundImage(
            motif, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bgs
        )));

        root.getChildren().addAll(leftPane, rightPane);

        Scene scene = new Scene(root, 1024, 768);
        stage.setTitle("The Four - Đăng Nhập");
        stage.setScene(scene);
        stage.show();

        leftPane.prefWidthProperty().bind(scene.widthProperty().multiply(0.6));
        rightPane.prefWidthProperty().bind(scene.widthProperty().multiply(0.4));
        leftPane.prefHeightProperty().bind(scene.heightProperty());
        rightPane.prefHeightProperty().bind(scene.heightProperty());
    }

    private void styleField(TextField field) {
        field.setPrefHeight(44);
        field.setStyle(
            "-fx-background-color: " + COLOR_CARD_INNER + ";" +
            "-fx-background-radius: 12;" +
            "-fx-prompt-text-fill: rgba(255,255,255,0.8);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 0 14 0 14;"
        );
        field.setBorder(new Border(new BorderStroke(
                Color.rgb(229,213,149, 0.35),
                BorderStrokeStyle.SOLID,
                new CornerRadii(12),
                new BorderWidths(2)
        )));
    }

    private Image getImage(String path) {
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        if (img.isError()) throw new RuntimeException();
        return img;
    }
}