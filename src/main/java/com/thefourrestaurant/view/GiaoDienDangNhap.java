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
    private static final String COLOR_CARD = "#FFFFFF33"; // #2A4C5A
    private static final String COLOR_CARD_INNER = "#B0BAC366"; // #2A4C5A
    private static final String COLOR_GOLD = "#DDB248";

    private Font montserratSemibold;
    private Font montserratExtrabold;

    public void show(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + COLOR_TEAL + ";");

        montserratSemibold = Font.loadFont(getClass().getResourceAsStream("/com/thefourrestaurant/fonts/Montserrat-SemiBold.ttf"), 20);
        montserratExtrabold = Font.loadFont(getClass().getResourceAsStream("/com/thefourrestaurant/fonts/Montserrat-ExtraBold.ttf"), 20);

        VBox centerContent = new VBox(20);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setPadding(new Insets(0, 20, 0, 20));

        ImageView logo = new ImageView(getImage("/com/thefourrestaurant/images/logo.png"));
        logo.setPreserveRatio(true);
        logo.setFitWidth(350);

        VBox card = new VBox(20);
        card.setPadding(new Insets(30));
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(500);
        card.setStyle(
            "-fx-background-color: " + COLOR_CARD + ";" +
            "-fx-background-radius: 50;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 4);"
        );

        TextField username = new TextField();
        username.setPromptText("Tài Khoản");
        styleField(username);
        hidePromptOnFocus(username);
        username.setPrefWidth(480);
        VBox.setMargin(username, new Insets(10, 0, 0, 0));

        PasswordField password = new PasswordField();
        password.setPromptText("Mật Khẩu");
        styleField(password);
        hidePromptOnFocus(password); 
        password.setPrefWidth(480);

        Button loginBtn = new Button("Đăng Nhập");
        loginBtn.setFont(montserratExtrabold);
        loginBtn.setPrefHeight(50);
        loginBtn.setPrefWidth(250);
        loginBtn.setStyle(
            "-fx-background-color: " + COLOR_GOLD + ";" +
            "-fx-background-radius: 10;" +
            "-fx-text-fill: #1E424D;"
        );
        VBox.setMargin(username, new Insets(15, 0, 0, 0));

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
        centerContent.getChildren().addAll(logo, card);

        StackPane rightPane = new StackPane();
        Image motif = getImage("/com/thefourrestaurant/images/motif.png");
        BackgroundSize bgs = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
        rightPane.setBackground(new Background(new BackgroundImage(
            motif, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bgs
        )));
        
        HBox mainContainer = new HBox();
        VBox leftPane = new VBox();
        leftPane.setAlignment(Pos.CENTER);
        leftPane.getChildren().add(centerContent);
        leftPane.setStyle("-fx-background-color: " + COLOR_TEAL + ";");
        
        mainContainer.getChildren().addAll(leftPane, rightPane);
        HBox.setHgrow(leftPane, Priority.ALWAYS);
        HBox.setHgrow(rightPane, Priority.ALWAYS);
        
        root.setCenter(mainContainer);

        Scene scene = new Scene(root, 1024, 768);
        stage.setTitle("The Four - Đăng Nhập");
        stage.setScene(scene);
        stage.show();

        leftPane.prefWidthProperty().bind(scene.widthProperty().multiply(0.6));
        rightPane.prefWidthProperty().bind(scene.widthProperty().multiply(0.4));
    }

    private void styleField(TextField field) {
        field.setPrefHeight(45);
        field.setFont(montserratSemibold);
        field.setStyle(
            "-fx-background-color: " + COLOR_CARD_INNER + ";" +
            "-fx-background-radius: 20;" +
            "-fx-prompt-text-fill: " + COLOR_GOLD + ";" + 
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 0 14 0 14;"
        );
    }

    private void hidePromptOnFocus(TextInputControl field) {
        final String originalPrompt = field.getPromptText();
        field.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (isFocused) {
                field.setPromptText(null);
            } else if (field.getText().isEmpty()) {
                field.setPromptText(originalPrompt);
            }
        });
    }

    private Image getImage(String path) {
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        return img;
    }
}