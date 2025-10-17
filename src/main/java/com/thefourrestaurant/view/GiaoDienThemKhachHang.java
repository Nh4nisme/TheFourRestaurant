package com.thefourrestaurant.view;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class GiaoDienThemKhachHang extends VBox {

    private TextField txtTenKhachHang;
    private TextField txtSoDT;
    private DatePicker dpNgaySinh;
    private TextField txtGioiTinh;
    private Button btnLamMoi;
    private Button btnThem;
    private Button btnQuayLai;

    public GiaoDienThemKhachHang() {
        setStyle("-fx-background-color: #F5F5F5;");
        setSpacing(0);
        setAlignment(Pos.TOP_CENTER);

        Label lblTitle = new Label("Thêm khách hàng");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-text-fill: #E19E11; -fx-font-weight: bold;");
        HBox titleBar = new HBox(lblTitle);
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.setPadding(new Insets(10, 20, 10, 20));
        titleBar.setStyle("-fx-background-color: #1E424D;");
        titleBar.setPrefHeight(50);

        VBox contentCard = new VBox(25);
        contentCard.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-border-color: #CCCCCC; -fx-border-radius: 15;");
        contentCard.setPadding(new Insets(40));
        contentCard.setMaxWidth(650);
        contentCard.setAlignment(Pos.TOP_CENTER);

        Label lblBanHeader = new Label("Bàn B101");
        lblBanHeader.setStyle("-fx-font-size: 22px; -fx-text-fill: #DDB248; -fx-font-weight: bold;");

        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER_LEFT);

        HBox row1 = new HBox(5);
        row1.setAlignment(Pos.CENTER_LEFT);
        Label lblTenKhachHang = createLabel("Tên khách hàng:");
        txtTenKhachHang = createTextField();
        txtTenKhachHang.setPrefWidth(450);
        row1.getChildren().addAll(lblTenKhachHang, txtTenKhachHang);

        HBox row2 = new HBox(5);
        row2.setAlignment(Pos.CENTER_LEFT);
        Label lblSoDT = createLabel("Số ĐT:");
        txtSoDT = createTextField();
        txtSoDT.setPrefWidth(450);
        row2.getChildren().addAll(lblSoDT, txtSoDT);

        HBox row3 = new HBox(5);
        row3.setAlignment(Pos.CENTER_LEFT);
        Label lblNgaySinh = createLabel("Ngày sinh:");
        dpNgaySinh = new DatePicker();
        dpNgaySinh.setStyle("-fx-background-color: white; -fx-border-color: #CCCCCC; -fx-border-radius: 10; -fx-background-radius: 10;");
        dpNgaySinh.setPrefHeight(35);
        dpNgaySinh.setPrefWidth(180);
        dpNgaySinh.setEditable(false);
        
        Label lblGioiTinh = createLabel("Giới tính:");
        txtGioiTinh = createTextField();
        txtGioiTinh.setPrefWidth(180);
        row3.getChildren().addAll(lblNgaySinh, dpNgaySinh, createSpacer(20), lblGioiTinh, txtGioiTinh);

        formBox.getChildren().addAll(row1, row2, row3);

        HBox buttonBar = new HBox(20);
        buttonBar.setAlignment(Pos.CENTER_LEFT);
        buttonBar.setPadding(new Insets(20, 0, 0, 0));
        
        btnQuayLai = createButton("Quay lại");
        btnQuayLai.setOnAction(e -> {
            if (getParent() != null && getParent().getParent() instanceof VBox) {
                VBox parent = (VBox) getParent().getParent();
                parent.getChildren().clear();
            }
        });
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        btnLamMoi = createButton("Làm mới");
        btnThem = createButton("Thêm");
        
        buttonBar.getChildren().addAll(btnQuayLai, spacer, btnLamMoi, btnThem);

        contentCard.getChildren().addAll(lblBanHeader, formBox, buttonBar);

        VBox centerWrapper = new VBox(contentCard);
        centerWrapper.setAlignment(Pos.CENTER);
        centerWrapper.setPadding(new Insets(40));
        VBox.setVgrow(centerWrapper, Priority.ALWAYS);

        getChildren().addAll(titleBar, centerWrapper);
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: #E19E11; -fx-font-weight: bold;");
        label.setMinWidth(Region.USE_PREF_SIZE);
        return label;
    }

    private TextField createTextField() {
        TextField textField = new TextField();
        textField.setStyle("-fx-background-color: white; -fx-border-color: #CCCCCC; -fx-border-radius: 10; -fx-background-radius: 10;");
        textField.setPrefHeight(35);
        return textField;
    }

    private Region createSpacer(double width) {
        Region spacer = new Region();
        spacer.setPrefWidth(width);
        return spacer;
    }

    private Button createButton(String text) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: #DDB248; " +
            "-fx-text-fill: #1E424D; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 14px; " +
            "-fx-background-radius: 10; " +
            "-fx-cursor: hand;"
        );
        button.setPrefHeight(40);
        button.setPrefWidth(100);
        
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(4);
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(4);
        dropShadow.setColor(Color.rgb(0, 0, 0, 0.25));
        button.setEffect(dropShadow);
        
        button.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });
        
        button.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
        
        return button;
    }

    public TextField getTxtTenKhachHang() { return txtTenKhachHang; }
    public TextField getTxtSoDT() { return txtSoDT; }
    public DatePicker getDpNgaySinh() { return dpNgaySinh; }
    public TextField getTxtGioiTinh() { return txtGioiTinh; }
    public Button getBtnLamMoi() { return btnLamMoi; }
    public Button getBtnThem() { return btnThem; }
    public Button getBtnQuayLai() { return btnQuayLai; }
}
