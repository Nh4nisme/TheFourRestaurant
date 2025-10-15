package com.thefourrestaurant.view;

import com.thefourrestaurant.view.components.ButtonSample;
import com.thefourrestaurant.view.components.DropDownButton;
import com.thefourrestaurant.view.components.LoaiMonAnBox;
import com.thefourrestaurant.view.components.MonAnBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GiaoDienTaoThucDon extends BorderPane {

    private final VBox rightContent;
    private final TextField txtTenThucDon;
    private final ComboBox<String> cbLoaiMonAn;
    private final FlowPane pnMonAnContainer;
    private final HBox bottomButtonPanel;
    
    public GiaoDienTaoThucDon() {
        setStyle("-fx-background-color: white;");
        
        VBox leftPanel = createLeftPanel();
        rightContent = new VBox(20);
        rightContent.setPadding(new Insets(20));
        rightContent.setAlignment(Pos.TOP_LEFT);
        
        setLeft(leftPanel);
        setCenter(rightContent);
        
        txtTenThucDon = new TextField();
        txtTenThucDon.setPrefHeight(40);
        txtTenThucDon.setStyle("-fx-background-radius: 5;");
        
        cbLoaiMonAn = new ComboBox<>();
        cbLoaiMonAn.getItems().addAll("Coffee", "Cơm", "Nước giải khát", "Đồ ăn nhanh");
        cbLoaiMonAn.setPromptText("Chọn loại món ăn");
        cbLoaiMonAn.setPrefWidth(Double.MAX_VALUE);
        cbLoaiMonAn.setPrefHeight(40);
        cbLoaiMonAn.setStyle("-fx-background-radius: 5;");
        
        pnMonAnContainer = new FlowPane();
        pnMonAnContainer.setPadding(new Insets(10));
        pnMonAnContainer.setHgap(15);
        pnMonAnContainer.setVgap(15);
        pnMonAnContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);
        
        addDemoMonAn();
        
        bottomButtonPanel = new HBox(20);
        bottomButtonPanel.setAlignment(Pos.CENTER);
        bottomButtonPanel.setPadding(new Insets(20));
        
        Button btnHuy = new ButtonSample("Hủy", 40, 14, 1);
        Button btnLuu = new ButtonSample("Lưu", 40, 14, 1);
        
        bottomButtonPanel.getChildren().addAll(btnHuy, btnLuu);
        
        rightContent.getChildren().addAll(
                createThucDonInfoSection(),
                createDanhSachMonAnSection(),
                bottomButtonPanel
        );
    }
    
    private VBox createLeftPanel() {
        VBox leftPanel = new VBox(10);
        leftPanel.setPadding(new Insets(20));
        leftPanel.setStyle("-fx-background-color: #F5F5F5;");
        leftPanel.setPrefWidth(220);
        leftPanel.setAlignment(Pos.TOP_CENTER);
        
        Label titleLabel = new Label("Tạo Thực Đơn");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.web("#1E424D"));
        
        VBox.setMargin(titleLabel, new Insets(20, 0, 30, 0));
        leftPanel.getChildren().add(titleLabel);
        
        return leftPanel;
    }
    
    private GridPane createThucDonInfoSection() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(10));
        
        VBox labelsBox = new VBox(55);
        
        Label lblMoTa = new Label("Mô tả thực đơn");
        lblMoTa.setFont(Font.font("System", FontWeight.BOLD, 14));
        lblMoTa.setTextFill(Color.web("#DDB248"));
        
        Label lblDienTen = new Label("Điền tên cho thực đơn mới");
        lblDienTen.setFont(Font.font("System", 12));
        lblDienTen.setTextFill(Color.web("#DDB248"));
        
        labelsBox.getChildren().addAll(lblMoTa, lblDienTen);
        
        VBox inputsBox = new VBox(10);
        
        Label lblTenThucDon = new Label("Tên thực đơn");
        lblTenThucDon.setFont(Font.font("System", 14));
        
        inputsBox.getChildren().addAll(lblTenThucDon, txtTenThucDon);
        
        grid.add(labelsBox, 0, 0);
        grid.add(inputsBox, 1, 0);
        
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(25);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(75);
        grid.getColumnConstraints().addAll(column1, column2);
        
        return grid;
    }
    
    private VBox createDanhSachMonAnSection() {
        VBox container = new VBox(15);
        
        HBox loaiMonAnHeader = new HBox();
        loaiMonAnHeader.setAlignment(Pos.CENTER_LEFT);
        
        VBox labelsBox = new VBox(10);
        
        Label lblChonLoai = new Label("Chọn loại món ăn");
        lblChonLoai.setFont(Font.font("System", FontWeight.BOLD, 14));
        lblChonLoai.setTextFill(Color.web("#DDB248"));
        
        Label lblMoTaLoai = new Label("Các loại món ăn được thêm sẽ xuất hiện khi mở thực đơn");
        lblMoTaLoai.setFont(Font.font("System", 12));
        lblMoTaLoai.setTextFill(Color.web("#DDB248"));
        
        labelsBox.getChildren().addAll(lblChonLoai, lblMoTaLoai);
        
        VBox dropdownBox = new VBox(10);
        
        Label lblLoaiMonAn = new Label("Loại Món ăn");
        lblLoaiMonAn.setFont(Font.font("System", 14));
        
        HBox dropdownContainer = new HBox();
        dropdownContainer.getChildren().add(cbLoaiMonAn);
        HBox.setHgrow(cbLoaiMonAn, Priority.ALWAYS);
        
        dropdownBox.getChildren().addAll(lblLoaiMonAn, dropdownContainer);
        
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(10));
        grid.add(labelsBox, 0, 0);
        grid.add(dropdownBox, 1, 0);
        
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(25);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(75);
        grid.getColumnConstraints().addAll(column1, column2);
        
        Label lblDanhSachMonAn = new Label("Danh sách món ăn đã chọn");
        lblDanhSachMonAn.setFont(Font.font("System", FontWeight.BOLD, 14));
        lblDanhSachMonAn.setPadding(new Insets(10, 0, 0, 0));
        
        container.getChildren().addAll(grid, lblDanhSachMonAn, pnMonAnContainer);
        
        return container;
    }
    
    private void addDemoMonAn() {
        pnMonAnContainer.getChildren().add(
                new MonAnBox("Coffee", "25000", "☕")
        );
    }
    
    public void addMonAn(String ten, String gia, String icon) {
        MonAnBox monAnBox = new MonAnBox(ten, gia, icon);
        pnMonAnContainer.getChildren().add(monAnBox);
    }
    
    public void show(Stage stage) {
        Scene scene = new Scene(this, 1000, 700);
        scene.getStylesheets().add(Objects.requireNonNull(
                getClass().getResource("/com/thefourrestaurant/css/Application.css")).toExternalForm());
        
        stage.setTitle("Tạo Thực Đơn");
        stage.setScene(scene);
    }
}
