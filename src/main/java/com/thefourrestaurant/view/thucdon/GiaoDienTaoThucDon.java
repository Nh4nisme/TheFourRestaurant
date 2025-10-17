package com.thefourrestaurant.view.thucdon;

import com.thefourrestaurant.view.components.ButtonSample;
import com.thefourrestaurant.view.monan.MonAnBox;
import com.thefourrestaurant.view.components.NavBar;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GiaoDienTaoThucDon extends VBox {
    private final TextField txtTenThucDon;
    private final ComboBox<String> cbLoaiMonAn;
    private final VBox boxChonThucAn;
    private final List<FoodItem> selectedFoods = new ArrayList<>();

    public GiaoDienTaoThucDon() {
        setStyle("-fx-background-color: #FAFAFA;");
        setSpacing(0);

        // NavBar vàng
        NavBar navBar = new NavBar(this);
        navBar.setPrefHeight(80);

        // Bar vị trí
        Label duongDan = new Label("Quản Lý > Thực Đơn > Tạo Thực Đơn");
        duongDan.setStyle("-fx-text-fill: #E5D595; -fx-font-size: 18px; -fx-font-weight: bold;");
        VBox khungDuongDan = new VBox(duongDan);
        khungDuongDan.setStyle("-fx-background-color: #673E1F;");
        khungDuongDan.setAlignment(Pos.CENTER_LEFT);
        khungDuongDan.setPadding(new Insets(10, 20, 10, 20));
        khungDuongDan.setPrefHeight(40);

        VBox contentArea = new VBox(32);
        contentArea.setStyle("-fx-background-color: #FAFAFA;");
        contentArea.setPadding(new Insets(32, 32, 32, 32));
        VBox.setVgrow(contentArea, Priority.ALWAYS);

        // Section: Đặt tên thực đơn
        GridPane paneTenThucDon = new GridPane();
        paneTenThucDon.setHgap(24);
        paneTenThucDon.setVgap(0);
        paneTenThucDon.setPadding(new Insets(0, 0, 0, 0));

        VBox leftLabelBox = new VBox(2);
        Label lblMoTa = new Label("Mô tả thực đơn");
        lblMoTa.setStyle("-fx-text-fill: #E19E11; -fx-font-size: 15px; -fx-font-weight: bold;");
        Label lblMoTaDesc = new Label("Điền tên cho thực đơn mới");
        lblMoTaDesc.setStyle("-fx-text-fill: #444; -fx-font-size: 13px;");
        leftLabelBox.getChildren().addAll(lblMoTa, lblMoTaDesc);

        VBox rightInputBox = new VBox(4);
        Label lblTenThucDon = new Label("Tên thực đơn");
        lblTenThucDon.setStyle("-fx-text-fill: #E19E11; -fx-font-size: 14px; -fx-font-weight: bold;");
        txtTenThucDon = new TextField();
        txtTenThucDon.setPromptText("");
        txtTenThucDon.setStyle("-fx-background-color: #D9DEE2; -fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #B0B0B0; -fx-font-size: 15px; -fx-padding: 10 16; -fx-text-fill: #444;");
        rightInputBox.getChildren().addAll(lblTenThucDon, txtTenThucDon);

        paneTenThucDon.add(leftLabelBox, 0, 0);
        paneTenThucDon.add(rightInputBox, 1, 0);
        paneTenThucDon.setAlignment(Pos.TOP_LEFT);
        paneTenThucDon.setPrefWidth(900);
        paneTenThucDon.getColumnConstraints().addAll(
                new ColumnConstraints(220),
                new ColumnConstraints(600)
        );

        // Section: Chọn loại món ăn
        GridPane paneChonMonAn = new GridPane();
        paneChonMonAn.setHgap(24);
        paneChonMonAn.setVgap(0);
        paneChonMonAn.setPadding(new Insets(0, 0, 0, 0));

        VBox leftLabelBox2 = new VBox(2);
        Label lblChonLoai = new Label("Chọn loại món ăn");
        lblChonLoai.setStyle("-fx-text-fill: #E19E11; -fx-font-size: 15px; -fx-font-weight: bold;");
        Label lblChonLoaiDesc = new Label("Các loại món ăn được thêm sẽ xuất hiện khi mở thực đơn");
        lblChonLoaiDesc.setWrapText(true);
        lblChonLoaiDesc.setStyle("-fx-text-fill: #444; -fx-font-size: 13px;");
        leftLabelBox2.getChildren().addAll(lblChonLoai, lblChonLoaiDesc);

        VBox rightInputBox2 = new VBox(4);
        Label lblLoaiMonAn = new Label("Loại Món ăn");
        lblLoaiMonAn.setStyle("-fx-text-fill: #E19E11; -fx-font-size: 14px; -fx-font-weight: bold;");
        cbLoaiMonAn = new ComboBox<>();
        cbLoaiMonAn.getItems().addAll("Coffee", "Cơm", "Nước giải khát", "Đồ ăn nhanh");
        cbLoaiMonAn.setPromptText("Chọn loại món ăn");
        cbLoaiMonAn.setStyle("-fx-background-color: #D9DEE2; -fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #B0B0B0; -fx-font-size: 15px; -fx-padding: 10 16; -fx-text-fill: #444;");
        cbLoaiMonAn.setPrefWidth(400);
        rightInputBox2.getChildren().addAll(lblLoaiMonAn, cbLoaiMonAn);

        boxChonThucAn = new VBox(0);
        boxChonThucAn.setSpacing(0);
        boxChonThucAn.setPrefWidth(700);
        capNhatBoxChonThucAn();

        cbLoaiMonAn.setOnAction(e -> {
            String selected = cbLoaiMonAn.getValue();
            if (selected != null) {
                Optional<FoodItem> existingItem = selectedFoods.stream()
                        .filter(f -> f.name.equals(selected))
                        .findFirst();

                if (existingItem.isPresent()) {
                    existingItem.get().quantity++;
                } else {
                    selectedFoods.add(new FoodItem(selected, getFoodIcon(selected), 1));
                }
                capNhatBoxChonThucAn();
                Platform.runLater(() -> cbLoaiMonAn.getSelectionModel().clearSelection());
            }
        });

        GridPane mainGrid = new GridPane();
        mainGrid.setHgap(24);
        mainGrid.setVgap(20);
        mainGrid.getColumnConstraints().addAll(
                new ColumnConstraints(220),
                new ColumnConstraints(600)
        );

        mainGrid.add(leftLabelBox, 0, 0);
        mainGrid.add(rightInputBox, 1, 0);
        mainGrid.add(leftLabelBox2, 0, 1);
        mainGrid.add(rightInputBox2, 1, 1);
        mainGrid.add(boxChonThucAn, 1, 2);

        contentArea.getChildren().add(mainGrid);

        // Add all sections to main VBox
        getChildren().addAll(navBar, khungDuongDan, contentArea);
    }

    private void capNhatBoxChonThucAn() {
        boxChonThucAn.getChildren().clear();
        if (selectedFoods.isEmpty()) return;

        HBox header = new HBox();
        header.setStyle("-fx-background-color: #F5F5F5; -fx-border-color: #E0E0E0; -fx-border-width: 1 1 0 1; -fx-border-radius: 8 8 0 0;");
        header.setPadding(new Insets(8, 16, 8, 16));
        header.setSpacing(0);
        header.setPrefHeight(36);
        Label lblTen = new Label("Tên");
        lblTen.setStyle("-fx-font-weight: bold; -fx-text-fill: #444; -fx-font-size: 15px;");
        lblTen.setPrefWidth(300);
        Label lblSoLuong = new Label("Số lượng");
        lblSoLuong.setStyle("-fx-font-weight: bold; -fx-text-fill: #444; -fx-font-size: 15px;");
        lblSoLuong.setPrefWidth(120);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(lblTen, lblSoLuong, spacer);
        boxChonThucAn.getChildren().add(header);

        for (FoodItem item : selectedFoods) {
            HBox row = new HBox();
            row.setStyle("-fx-background-color: #FFF; -fx-border-color: #E0E0E0; -fx-border-width: 0 1 1 1;");
            row.setPadding(new Insets(8, 16, 8, 16));
            row.setSpacing(0);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPrefHeight(36);

            HBox boxTenThucAn;
            if (item.icon.startsWith("/")) {
                try {
                    ImageView iconThucAn = new ImageView(new Image(getClass().getResourceAsStream(item.icon)));
                    iconThucAn.setFitHeight(24);
                    iconThucAn.setFitWidth(24);
                    iconThucAn.setPreserveRatio(true);
                    
                    Label name = new Label(item.name);
                    name.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #444;");
                    name.setPrefWidth(220);
                    
                    boxTenThucAn = new HBox(iconThucAn, name);
                } catch (Exception e) {
                    Label icon = new Label("□");
                    icon.setStyle("-fx-font-size: 18px; -fx-padding: 0 8 0 0;");
                    Label name = new Label(item.name);
                    name.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #444;");
                    name.setPrefWidth(220);
                    boxTenThucAn = new HBox(icon, name);
                    System.err.println("Failed to load icon: " + item.icon + " - " + e.getMessage());
                }
            } else {
                Label icon = new Label(item.icon);
                icon.setStyle("-fx-font-size: 18px; -fx-padding: 0 8 0 0;");
                Label name = new Label(item.name);
                name.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #444;");
                name.setPrefWidth(220);
                boxTenThucAn = new HBox(icon, name);
            }
            
            boxTenThucAn.setSpacing(6);
            boxTenThucAn.setPrefWidth(300);
            boxTenThucAn.setAlignment(Pos.CENTER_LEFT);

            Label soLuong = new Label(String.valueOf(item.quantity));
            soLuong.setStyle("-fx-font-size: 15px; -fx-text-fill: #444;");
            soLuong.setPrefWidth(120);

            Button btnDelete = new ButtonSample("Xóa", 28, 13, 1);
            btnDelete.setStyle("-fx-font-size: 13px; -fx-padding: 2 10; -fx-background-radius: 8; -fx-border-radius: 8;");
            btnDelete.setOnAction(e -> {
                selectedFoods.remove(item);
                capNhatBoxChonThucAn();
            });

            Region rowSpacer = new Region();
            HBox.setHgrow(rowSpacer, Priority.ALWAYS);
            row.getChildren().addAll(boxTenThucAn, soLuong, rowSpacer, btnDelete);
            boxChonThucAn.getChildren().add(row);
        }
    }

    private String getFoodIcon(String name) {
        return switch (name) {
            case "Coffee" -> "/com/thefourrestaurant/images/icon/food/coffee.png";
            case "Cơm" -> "/com/thefourrestaurant/images/icon/food/rice.png";
            case "Nước giải khát" -> "/com/thefourrestaurant/images/icon/food/coffee.png"; 
            case "Đồ ăn nhanh" -> "/com/thefourrestaurant/images/icon/food/rice.png";
            default -> "/com/thefourrestaurant/images/icon/food/coffee.png";
        };
    }

    private static class FoodItem {
        String name;
        String icon;
        int quantity;
        FoodItem(String name, String icon, int quantity) {
            this.name = name;
            this.icon = icon;
            this.quantity = quantity;
        }
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
    
    private GridPane createPaneThucDonInfo() {
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
        
        container.getChildren().addAll(grid, lblDanhSachMonAn);
        
        return container;
    }
    
    public void addMonAn(String ten, String gia, String icon) {
        MonAnBox monAnBox = new MonAnBox(ten, gia, icon);
    }
    
    public void show(Stage stage) {
        Scene scene = new Scene(this, 1000, 700);
        scene.getStylesheets().add(Objects.requireNonNull(
                getClass().getResource("/com/thefourrestaurant/css/Application.css")).toExternalForm());
        
        stage.setTitle("Tạo Thực Đơn");
        stage.setScene(scene);
    }
}
