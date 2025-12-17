package com.thefourrestaurant.view;

import java.util.Objects;
import com.thefourrestaurant.util.Session;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GiaoDienNhapTien {

   private static final String COLOR_TEAL = "#1E424D";
   private static final String COLOR_CARD = "#FFFFFF33";
   private static final String COLOR_CARD_INNER = "#B0BAC366";
   private static final String COLOR_GOLD = "#DDB248";

   private Font montserratSemibold;
   private Font montserratExtrabold;

   public void hienThi(Stage stage, Runnable onSuccess) {
       montserratSemibold = Font.loadFont(getClass().getResourceAsStream("/com/thefourrestaurant/fonts/Montserrat-SemiBold.ttf"), 20);
       montserratExtrabold = Font.loadFont(getClass().getResourceAsStream("/com/thefourrestaurant/fonts/Montserrat-ExtraBold.ttf"), 20);

       BorderPane root = new BorderPane();
       root.setStyle("-fx-background-color: " + COLOR_TEAL + ";");

       VBox centerContent = new VBox(20);
       centerContent.setAlignment(Pos.CENTER);
       centerContent.setPadding(new Insets(0, 20, 0, 20));
       VBox.setMargin(centerContent, new Insets(-80, 0, 0, 0));

       ImageView logo = new ImageView(getImage("/com/thefourrestaurant/images/Logo.png"));
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

       StackPane fieldContainer = createFloatingLabelField("Số tiền trong két", false);
       TextField txtSoTien = (TextField) ((StackPane) fieldContainer.getChildren().get(1)).getChildren().get(0);

       Button btnXacNhan = new Button("Xác nhận");
       btnXacNhan.setFont(montserratExtrabold);
       btnXacNhan.setPrefHeight(50);
       btnXacNhan.setPrefWidth(250);
       btnXacNhan.setStyle(
           "-fx-background-color: " + COLOR_GOLD + ";" +
           "-fx-background-radius: 10;" +
           "-fx-text-fill: #1E424D;"
       );

       addHoverAnimation(fieldContainer);

       btnXacNhan.setOnAction(e -> {
           try {
               double soTien = Double.parseDouble(txtSoTien.getText().trim());
               if (onSuccess != null) onSuccess.run();
           } catch (NumberFormatException ex) {
               txtSoTien.setStyle("-fx-border-color: red;");
           }
       });

       card.getChildren().addAll(fieldContainer, btnXacNhan);
       centerContent.getChildren().addAll(logo, card);

       StackPane rightPane = new StackPane();
       Image anhNenDangNhap = getImage("/com/thefourrestaurant/images/AnhNenDangNhap.png");
       BackgroundSize bgs = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
       rightPane.setBackground(new Background(new BackgroundImage(
           anhNenDangNhap, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bgs
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
       stage.setTitle("The Four - Nhập tiền két");
       stage.setScene(scene);
       stage.show();

       leftPane.prefWidthProperty().bind(scene.widthProperty().multiply(0.6));
       rightPane.prefWidthProperty().bind(scene.widthProperty().multiply(0.4));
   }

   private StackPane createFloatingLabelField(String labelText, boolean isPassword) {
       StackPane container = new StackPane();
       container.setPrefWidth(480);
       container.setPrefHeight(55);

       javafx.scene.control.Label floatingLabel = new javafx.scene.control.Label(labelText);
       floatingLabel.setFont(Font.font(montserratSemibold.getFamily(), 14));
       floatingLabel.setTextFill(Color.web(COLOR_GOLD));
       floatingLabel.setMouseTransparent(true);

       StackPane fieldWrapper = new StackPane();
       TextInputControl field = new TextField();

       field.setPrefHeight(45);
       field.setPrefWidth(480);
       field.setFont(montserratSemibold);
       field.setStyle(
           "-fx-background-color: " + COLOR_CARD_INNER + ";" +
           "-fx-background-radius: 20;" +
           "-fx-text-fill: white;" +
           "-fx-font-size: 14px;" +
           "-fx-padding: 0 14 0 14;"
       );

       fieldWrapper.getChildren().add(field);
       fieldWrapper.setAlignment(Pos.CENTER);

       container.getChildren().addAll(floatingLabel, fieldWrapper);
       StackPane.setAlignment(floatingLabel, Pos.CENTER_LEFT);
       StackPane.setMargin(floatingLabel, new Insets(0, 0, 0, 14));

       TranslateTransition labelUp = new TranslateTransition(Duration.millis(200), floatingLabel);
       labelUp.setToY(-35);

       TranslateTransition labelDown = new TranslateTransition(Duration.millis(200), floatingLabel);
       labelDown.setToY(0);

       field.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
           if (isFocused || !field.getText().isEmpty()) {
               labelUp.playFromStart();
           } else {
               labelDown.playFromStart();
           }
       });

       field.textProperty().addListener((obs, oldText, newText) -> {
           if (!newText.isEmpty() && floatingLabel.getTranslateY() == 0) {
               labelUp.playFromStart();
           } else if (newText.isEmpty() && !field.isFocused()) {
               labelDown.playFromStart();
           }
       });

       return container;
   }

   private void addHoverAnimation(StackPane fieldContainer) {
       ScaleTransition scaleUp = new ScaleTransition(Duration.millis(150), fieldContainer);
       scaleUp.setToX(1.05);
       scaleUp.setToY(1.05);

       ScaleTransition scaleDown = new ScaleTransition(Duration.millis(150), fieldContainer);
       scaleDown.setToX(1);
       scaleDown.setToY(1);

       fieldContainer.setOnMouseEntered(e -> scaleUp.playFromStart());
       fieldContainer.setOnMouseExited(e -> scaleDown.playFromStart());
   }

   private Image getImage(String path) {
       Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
       return img;
   }

}
