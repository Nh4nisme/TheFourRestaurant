package com.thefourrestaurant.view.components;

import com.thefourrestaurant.util.ClockText;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class SideBar extends VBox {
    public SideBar(){
        Font montserrat = Font.loadFont(getClass().getResourceAsStream("com/thefourrestaurant/fonts/Montserrat-SemiBold.ttf"),16);
        setPrefWidth(90);
        setStyle("-fx-background-color: #1E424D");
        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(14,17,14,17));
        setSpacing(50);


        // Phan nay la Logo cua sideBar
        ImageView logoImg = new ImageView(new Image(getClass().getResourceAsStream("/com/thefourrestaurant/images/icon/logoIcon.png")));
        logoImg.setFitWidth(100);
        logoImg.setFitHeight(100);
        logoImg.setPreserveRatio(true);

        // Phan Menu Button
        VBox groupButton =  new VBox(10);
        groupButton.setAlignment(Pos.CENTER);
        groupButton.setPadding(new Insets(10,9,10,9));

        ImageView thongKeIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/thefourrestaurant/images/icon/thongKeIcon.png")));
        thongKeIcon.setFitWidth(45);
        thongKeIcon.setFitHeight(45);
        Button btnThongKe = new Button();
        btnThongKe.setGraphic(thongKeIcon);
        btnThongKe.setStyle("-fx-background-color: transparent;");
        btnThongKe.setOnMouseEntered(e -> {thongKeIcon.setScaleX(1.2);thongKeIcon.setScaleY(1.2);});
        btnThongKe.setOnMouseExited(e -> {thongKeIcon.setScaleX(1);thongKeIcon.setScaleY(1);});

        ImageView hoaDonIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/thefourrestaurant/images/icon/hoaDonIcon.png")));
        hoaDonIcon.setFitWidth(45);
        hoaDonIcon.setFitHeight(45);
        Button btnHoaDon = new Button();
        btnHoaDon.setGraphic(hoaDonIcon);
        btnHoaDon.setStyle("-fx-background-color: transparent;");
        btnHoaDon.setOnMouseEntered(e -> {hoaDonIcon.setScaleX(1.2);hoaDonIcon.setScaleY(1.2);});
        btnHoaDon.setOnMouseExited(e -> {hoaDonIcon.setScaleX(1);hoaDonIcon.setScaleY(1);});

        ImageView taiKhoanIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/thefourrestaurant/images/icon/taiKhoanIcon.png")));
        taiKhoanIcon.setFitWidth(45);
        taiKhoanIcon.setFitHeight(45);
        Button btnTaiKhoan = new Button();
        btnTaiKhoan.setGraphic(taiKhoanIcon);
        btnTaiKhoan.setStyle("-fx-background-color: transparent;");
        btnTaiKhoan.setOnMouseEntered(e -> {taiKhoanIcon.setScaleX(1.2);taiKhoanIcon.setScaleY(1.2);});
        btnTaiKhoan.setOnMouseExited(e -> {taiKhoanIcon.setScaleX(1);taiKhoanIcon.setScaleY(1);});

        ImageView khachHangIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/thefourrestaurant/images/icon/khachHangIcon.png")));
        khachHangIcon.setFitWidth(45);
        khachHangIcon.setFitHeight(45);
        Button btnKhachHang = new Button();
        btnKhachHang.setGraphic(khachHangIcon);
        btnKhachHang.setStyle("-fx-background-color: transparent;");
        btnKhachHang.setOnMouseEntered(e -> {khachHangIcon.setScaleX(1.2);khachHangIcon.setScaleY(1.2);});
        btnKhachHang.setOnMouseExited(e -> {khachHangIcon.setScaleX(1);khachHangIcon.setScaleY(1);});

        ImageView danhMucIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/thefourrestaurant/images/icon/danhMucIcon.png")));
        danhMucIcon.setFitWidth(45);
        danhMucIcon.setFitHeight(45);
        Button btnDanhMuc = new Button();
        btnDanhMuc.setGraphic(danhMucIcon);
        btnDanhMuc.setStyle("-fx-background-color: transparent;");
        btnDanhMuc.setOnMouseEntered(e -> {danhMucIcon.setScaleX(1.2);danhMucIcon.setScaleY(1.2);});
        btnDanhMuc.setOnMouseExited(e -> {danhMucIcon.setScaleX(1);danhMucIcon.setScaleY(1);});

        ImageView caiDatIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/thefourrestaurant/images/icon/caiDatIcon.png")));
        caiDatIcon.setFitWidth(45);
        caiDatIcon.setFitHeight(45);
        Button btnCaiDat = new Button();
        btnCaiDat.setGraphic(caiDatIcon);
        btnCaiDat.setStyle("-fx-background-color: transparent;");
        btnCaiDat.setOnMouseEntered(e -> {caiDatIcon.setScaleX(1.2);caiDatIcon.setScaleY(1.2);});
        btnCaiDat.setOnMouseExited(e -> {caiDatIcon.setScaleX(1);caiDatIcon.setScaleY(1);});

        groupButton.getChildren().addAll(btnThongKe,btnHoaDon,btnTaiKhoan,btnKhachHang, btnCaiDat);

        // Tao Vbox rong
        VBox BoDemGio = new VBox();
        BoDemGio.setAlignment(Pos.BOTTOM_CENTER);
        BoDemGio.setPadding(new Insets(10,10,10,10));
        BoDemGio.setPrefHeight(500);
        ClockText boDemGioText = new ClockText();
        boDemGioText.setFont(montserrat);
        boDemGioText.setStyle("-fx-fill: #DDB248; -fx-font-size: 15px; -fx-font-weight: bold;");
        BoDemGio.getChildren().add(boDemGioText);



        //Them vao VBox sidebar chinh
        getChildren().addAll(logoImg,groupButton,BoDemGio);
    }
}



























