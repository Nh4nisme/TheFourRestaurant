package com.thefourrestaurant.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PhieuGoiMon extends VBox {
	private TableView<OrderItem> orderTable;
    private ObservableList<OrderItem> orderItems = FXCollections.observableArrayList();
    private Label totalLabel;

    public PhieuGoiMon() {
        setStyle("-fx-background-color: #2c4f5e;");
        setPadding(new Insets(20));

        HBox mainContent = new HBox(20);
        mainContent.setPadding(new Insets(20));

        VBox leftSide = createMenuGrid();
        VBox rightSide = createOrderSummary();

        mainContent.getChildren().addAll(leftSide, rightSide);
        HBox.setHgrow(leftSide, Priority.ALWAYS);

        getChildren().addAll(createTopBar(), mainContent);
    }

    // ====== TẠO THANH TRÊN ======
    private HBox createTopBar() {
        HBox topBar = new HBox(15);
        topBar.setPadding(new Insets(15));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #2c4f5e;");

        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll("Buổi Sáng", "Buổi Trưa", "Buổi Tối");
        categoryCombo.setValue("Buổi Trưa");
        categoryCombo.setStyle("-fx-background-color: #d4a855; -fx-font-size: 14px;");
        categoryCombo.setPrefWidth(150);

        Label lblLoaiMon = new Label("Loại món:");
        lblLoaiMon.setTextFill(Color.WHITE);
        TextField txtLoaiMon = new TextField();
        txtLoaiMon.setPrefWidth(180);

        Label lblTenMon = new Label("Tên món:");
        lblTenMon.setTextFill(Color.WHITE);
        TextField txtTenMon = new TextField();
        txtTenMon.setPrefWidth(250);

        Button btnTim = new Button("Tìm");
        btnTim.setStyle("-fx-background-color: #d4a855; -fx-text-fill: black; -fx-font-weight: bold;");
        btnTim.setPrefWidth(80);

        Button btnLamMoi = new Button("Làm mới");
        btnLamMoi.setStyle("-fx-background-color: #d4a855; -fx-text-fill: black; -fx-font-weight: bold;");
        btnLamMoi.setPrefWidth(80);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Circle avatar = new Circle(20);
        avatar.setFill(Color.LIGHTGRAY);

        topBar.getChildren().addAll(categoryCombo, lblLoaiMon, txtLoaiMon,
                lblTenMon, txtTenMon, btnTim, btnLamMoi, spacer, avatar);

        return topBar;
    }

    // ====== GRID MÓN ĂN ======
    private VBox createMenuGrid() {
        VBox container = new VBox(15);

        GridPane menuGrid = new GridPane();
        menuGrid.setHgap(15);
        menuGrid.setVgap(15);
        menuGrid.setPadding(new Insets(10));

        // 12 ô món ăn (3 hàng x 4 cột)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 4; col++) {
                VBox menuItem = createMenuItem("Cơm gà cay", 45000);
                menuGrid.add(menuItem, col, row);
            }
        }

        HBox pagination = createPagination();

        container.getChildren().addAll(menuGrid, pagination);
        return container;
    }

    // ====== Ô MÓN ĂN ======
    private VBox createMenuItem(String tenMon, int gia) {
        VBox item = new VBox(8);
        item.setAlignment(Pos.CENTER);
        item.setPadding(new Insets(10));
        item.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; "
                + "-fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
        item.setPrefSize(140, 180);

        Label vegIcon = new Label("🌿");
        vegIcon.setAlignment(Pos.TOP_RIGHT);
        vegIcon.setFont(Font.font(16));

        Circle imagePlaceholder = new Circle(50);
        imagePlaceholder.setFill(Color.web("#8B4513"));
        imagePlaceholder.setStroke(Color.web("#654321"));
        imagePlaceholder.setStrokeWidth(2);

        Label name = new Label(tenMon);
        name.setFont(Font.font("System", FontWeight.BOLD, 13));
        name.setWrapText(true);
        name.setAlignment(Pos.CENTER);

        Label price = new Label(String.format("%,d VNĐ", gia));
        price.setFont(Font.font(12));
        price.setTextFill(Color.DARKGREEN);

        item.setOnMouseClicked(e -> addToOrder(tenMon, gia));

        item.getChildren().addAll(vegIcon, imagePlaceholder, name, price);
        return item;
    }

    // ====== THANH PHÂN TRANG ======
    private HBox createPagination() {
        HBox pagination = new HBox(10);
        pagination.setAlignment(Pos.CENTER);
        pagination.setPadding(new Insets(10));

        Button btnFirst = new Button("<<");
        Button btnPrev = new Button("<");
        Label pageLabel = new Label("1");
        pageLabel.setTextFill(Color.WHITE);
        Button btnNext = new Button(">");
        Button btnLast = new Button(">>");

        String buttonStyle = "-fx-background-color: white; -fx-border-color: #cccccc;";
        btnFirst.setStyle(buttonStyle);
        btnPrev.setStyle(buttonStyle);
        btnNext.setStyle(buttonStyle);
        btnLast.setStyle(buttonStyle);

        pagination.getChildren().addAll(btnFirst, btnPrev, pageLabel, btnNext, btnLast);
        return pagination;
    }

    // ====== BẢNG PHIẾU GỌI MÓN ======
    private VBox createOrderSummary() {
        VBox rightPanel = new VBox(15);
        rightPanel.setPrefWidth(450);
        rightPanel.setStyle("-fx-background-color: #e8e8e8; -fx-padding: 15;");

        Label title = new Label("Phiếu gọi món");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#d4a855"));

        Label tableInfo = new Label("Bàn: B10IV");
        tableInfo.setFont(Font.font(14));
        tableInfo.setPadding(new Insets(10, 0, 10, 0));

        orderTable = new TableView<>();
        orderTable.setItems(orderItems);
        orderTable.setPrefHeight(350);

        TableColumn<OrderItem, Integer> sttCol = new TableColumn<>("STT");
        sttCol.setCellValueFactory(new PropertyValueFactory<>("stt"));
        sttCol.setPrefWidth(40);

        TableColumn<OrderItem, String> nameCol = new TableColumn<>("Tên món");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(100);

        TableColumn<OrderItem, String> priceCol = new TableColumn<>("Đơn giá");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setPrefWidth(90);

        TableColumn<OrderItem, Integer> qtyCol = new TableColumn<>("Số lượng");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        qtyCol.setPrefWidth(70);

        TableColumn<OrderItem, String> totalCol = new TableColumn<>("Thành tiền");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        totalCol.setPrefWidth(90);

        TableColumn<OrderItem, Void> actionCol = new TableColumn<>("Hành động");
        actionCol.setPrefWidth(80);
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final HBox actionButtons = new HBox(5);
            private final Button btnMinus = new Button("−");
            private final Button btnDelete = new Button("🗑");
            private final Button btnPlus = new Button("+");

            {
                btnMinus.setStyle("-fx-font-size: 10px; -fx-padding: 2 8;");
                btnPlus.setStyle("-fx-font-size: 10px; -fx-padding: 2 8;");
                btnDelete.setStyle("-fx-font-size: 10px; -fx-padding: 2 8;");

                btnMinus.setOnAction(e -> {
                    OrderItem item = getTableView().getItems().get(getIndex());
                    updateQuantity(item, -1);
                });

                btnPlus.setOnAction(e -> {
                    OrderItem item = getTableView().getItems().get(getIndex());
                    updateQuantity(item, 1);
                });

                btnDelete.setOnAction(e -> {
                    OrderItem item = getTableView().getItems().get(getIndex());
                    orderItems.remove(item);
                    updateTotal();
                    updateSTT();
                });

                actionButtons.getChildren().addAll(btnMinus, btnDelete, btnPlus);
                actionButtons.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : actionButtons);
            }
        });

        orderTable.getColumns().addAll(sttCol, nameCol, priceCol, qtyCol, totalCol, actionCol);

        HBox totalBox = new HBox();
        totalBox.setAlignment(Pos.CENTER_RIGHT);
        totalBox.setPadding(new Insets(10));
        totalLabel = new Label("Tổng tiền: 0 VNĐ");
        totalLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        totalBox.getChildren().add(totalLabel);

        Button btnSubmit = new Button("Gửi bếp");
        btnSubmit.setStyle("-fx-background-color: #d4a855; -fx-text-fill: black; "
                + "-fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 30;");
        btnSubmit.setMaxWidth(Double.MAX_VALUE);

        rightPanel.getChildren().addAll(title, tableInfo, orderTable, totalBox, btnSubmit);
        return rightPanel;
    }

    // ====== CÁC HÀM XỬ LÝ ======
    private void addToOrder(String itemName, int price) {
        for (OrderItem item : orderItems) {
            if (item.getName().equals(itemName)) {
                updateQuantity(item, 1);
                return;
            }
        }
        int stt = orderItems.size() + 1;
        OrderItem newItem = new OrderItem(stt, itemName, price, 1);
        orderItems.add(newItem);
        updateTotal();
    }

    private void updateQuantity(OrderItem item, int delta) {
        int newQty = item.getQuantity() + delta;
        if (newQty > 0) {
            item.setQuantity(newQty);
            orderTable.refresh();
            updateTotal();
        } else {
            orderItems.remove(item);
            updateSTT();
            updateTotal();
        }
    }

    private void updateSTT() {
        for (int i = 0; i < orderItems.size(); i++) {
            orderItems.get(i).setStt(i + 1);
        }
        orderTable.refresh();
    }

    private void updateTotal() {
        int total = 0;
        for (OrderItem item : orderItems) {
            total += item.getQuantity() * item.getPriceValue();
        }
        totalLabel.setText(String.format("Tổng tiền: %,d VNĐ", total));
    }

    // ====== CLASS OrderItem ======
    public static class OrderItem {
        private int stt;
        private String name;
        private int priceValue;
        private int quantity;

        public OrderItem(int stt, String name, int price, int quantity) {
            this.stt = stt;
            this.name = name;
            this.priceValue = price;
            this.quantity = quantity;
        }

        public int getStt() { return stt; }
        public void setStt(int stt) { this.stt = stt; }
        public String getName() { return name; }
        public String getPrice() { return String.format("%,d VNĐ", priceValue); }
        public int getPriceValue() { return priceValue; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public String getTotal() { return String.format("%,d VNĐ", priceValue * quantity); }
    }
}
