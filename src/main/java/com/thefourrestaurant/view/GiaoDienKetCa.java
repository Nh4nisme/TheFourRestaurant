package com.thefourrestaurant.view;

import java.util.Objects;
import com.thefourrestaurant.DAO.ThongKeDAO;
import com.thefourrestaurant.util.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

import java.math.BigDecimal;

public class GiaoDienKetCa {

   private static final String COLOR_TEAL = "#1E424D";
   private static final String COLOR_CARD = "#FFFFFF33";
   private static final String COLOR_CARD_INNER = "#B0BAC366";
   private static final String COLOR_GOLD = "#DDB248";

   private Font montserratSemibold;
   private Font montserratExtrabold;

   public void show(Stage stage) {
       montserratSemibold = Font.loadFont(getClass().getResourceAsStream("/com/thefourrestaurant/fonts/Montserrat-SemiBold.ttf"), 18);
       montserratExtrabold = Font.loadFont(getClass().getResourceAsStream("/com/thefourrestaurant/fonts/Montserrat-ExtraBold.ttf"), 20);

       BorderPane root = new BorderPane();
       root.setStyle("-fx-background-color: " + COLOR_TEAL + ";");

       VBox card = new VBox(20);
       card.setPadding(new Insets(30));
       card.setAlignment(Pos.CENTER);
       card.setMaxWidth(640);
       card.setStyle(
           "-fx-background-color: " + COLOR_CARD + ";" +
           "-fx-background-radius: 12;" +
           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 10, 0, 0, 4);"
       );

       Label title = new Label("KẾT CA");
       title.setFont(montserratExtrabold);
       title.setTextFill(Color.web(COLOR_GOLD));

       // calculate stats
       BigDecimal doanhThu = tinhDoanhThuCa();
       int soHoaDon = tinhSoHoaDonCa();

       Label lblDoanhThu = new Label("Doanh thu (tiền mặt): " + doanhThu + " VND");
       lblDoanhThu.setFont(montserratSemibold);
       lblDoanhThu.setTextFill(Color.WHITE);

       Label lblSoHD = new Label("Số hóa đơn: " + soHoaDon);
       lblSoHD.setFont(montserratSemibold);
       lblSoHD.setTextFill(Color.WHITE);

       Button btnXacNhan = new Button("Xác nhận kết ca");
       btnXacNhan.setFont(montserratExtrabold);
       btnXacNhan.setPrefHeight(48);
       btnXacNhan.setPrefWidth(260);
       btnXacNhan.setStyle("-fx-background-color: " + COLOR_GOLD + "; -fx-text-fill: #1E424D; -fx-background-radius: 8;");
       btnXacNhan.setOnAction(e -> {
           Session.setCurrentUser(null);
           Session.setLoginTime(null);
           new GiaoDienDangNhap().show(stage);
       });

       card.getChildren().addAll(title, lblDoanhThu, lblSoHD, btnXacNhan);

       StackPane center = new StackPane();
       center.getChildren().add(card);
       root.setCenter(center);

       Scene scene = new Scene(root, 900, 560);
       stage.setScene(scene);
       stage.setTitle("Kết ca - The Four");
       stage.show();
   }

   private BigDecimal tinhDoanhThuCa() {
       ThongKeDAO tk = new ThongKeDAO();
       java.time.LocalDate start = java.time.LocalDate.now();
       java.time.LocalDate end = java.time.LocalDate.now();
       BigDecimal val = tk.getTongDoanhThu(start, end);
       return val != null ? val : BigDecimal.ZERO;
   }

   private int tinhSoHoaDonCa() {
       ThongKeDAO tk = new ThongKeDAO();
       java.time.LocalDate start = java.time.LocalDate.now();
       java.time.LocalDate end = java.time.LocalDate.now();
       return tk.getSoHoaDon(start, end);
   }

}
