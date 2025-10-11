package com.thefourrestaurant.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.HPos;
import javafx.beans.property.SimpleStringProperty;

public class PhieuGoiMon extends BorderPane {

    public PhieuGoiMon() {
        this.setStyle("-fx-background-color: white;");

        HBox topBar = createTopBar();
        BorderPane.setMargin(topBar, Insets.EMPTY);
        this.setTop(topBar);

        HBox mainContent = new HBox(20);
        mainContent.setAlignment(Pos.TOP_CENTER);

        VBox leftPanel = createMenuGrid();
        VBox rightPanel = createOrderPanel();

        leftPanel.setAlignment(Pos.TOP_CENTER);
        rightPanel.setAlignment(Pos.TOP_CENTER);
        HBox.setMargin(leftPanel, new Insets(10, 10, 10, 10));

        HBox.setHgrow(leftPanel, Priority.ALWAYS);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        mainContent.getChildren().addAll(leftPanel, rightPanel);
        this.setCenter(mainContent);
    }

    private HBox createTopBar() {
        HBox topBar = new HBox(15);
        topBar.setPadding(new Insets(15, 20, 15, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #1E424D;");

        MenuButton mealMenu = new MenuButton("Buổi Trưa");
        mealMenu.setStyle("-fx-background-color: #D4A84A; -fx-text-fill: #2C5F5F; "
                + "-fx-font-size: 14px; -fx-font-weight: bold; -fx-pref-width: 150px;");

        mealMenu.getItems().addAll(new MenuItem("Buổi Sáng"), new MenuItem("Buổi Trưa"), new MenuItem("Buổi Tối"));

        Label loaiMonLabel = new Label("Loại món:");
        loaiMonLabel.setTextFill(Color.web("#D4A84A"));
        loaiMonLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        TextField loaiMonField = new TextField();
        loaiMonField.setPromptText("Tìm loại món...");
        loaiMonField.setPrefWidth(200);

        Label tenMonLabel = new Label("Tên món:");
        tenMonLabel.setTextFill(Color.web("#D4A84A"));
        tenMonLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        TextField tenMonField = new TextField();
        tenMonField.setPromptText("Tìm tên món...");
        tenMonField.setPrefWidth(300);

        Button searchBtn = new Button("Tìm");
        Button refreshBtn = new Button("Làm mới");

        String btnStyle = "-fx-background-color: #D4A84A; -fx-text-fill: #2C5F5F; "
                + "-fx-font-weight: bold; -fx-font-size: 14px; -fx-pref-height: 35px;";
        searchBtn.setStyle(btnStyle);
        refreshBtn.setStyle(btnStyle);

        topBar.getChildren().addAll(mealMenu, loaiMonLabel, loaiMonField, tenMonLabel, tenMonField, searchBtn, refreshBtn);
        return topBar;
    }

    private VBox createMenuGrid() {
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(10));

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);

        for (int i = 0; i < 4; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(25);
            cc.setHalignment(HPos.CENTER);
            grid.getColumnConstraints().add(cc);
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 4; col++) {
                VBox menuItem = createMenuItem();
                grid.add(menuItem, col, row);
            }
        }

        HBox pagination = new HBox(10);
        pagination.setAlignment(Pos.CENTER);
        pagination.setPadding(new Insets(10));

        Button firstBtn = new Button("⏮");
        Button prevBtn = new Button("◀");
        Label pageLabel = new Label("1");
        pageLabel.setFont(Font.font(12));
        Button nextBtn = new Button("▶");
        Button lastBtn = new Button("⏭");

        String btnStyle = "-fx-background-color: white; -fx-text-fill: #2C5F5F; "
                + "-fx-font-weight: bold; -fx-pref-width: 40px;";
        firstBtn.setStyle(btnStyle);
        prevBtn.setStyle(btnStyle);
        nextBtn.setStyle(btnStyle);
        lastBtn.setStyle(btnStyle);

        pagination.getChildren().addAll(firstBtn, prevBtn, pageLabel, nextBtn, lastBtn);

        container.getChildren().addAll(grid, pagination);
        return container;
    }

    private VBox createMenuItem() {
        VBox item = new VBox(5);
        item.setAlignment(Pos.CENTER);
        item.setPadding(new Insets(10));
        item.setStyle("-fx-background-color: white; -fx-border-color: #D4A84A; "
                + "-fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8;");
        item.setPrefSize(150, 200);

        Label foodLabel = new Label("🍲");
        foodLabel.setFont(Font.font(40));

        Label nameLabel = new Label("Cơm gà cay");
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 13));
        nameLabel.setTextFill(Color.web("#2C5F5F"));

        Label priceLabel = new Label("45,000 VND");
        priceLabel.setFont(Font.font(12));
        priceLabel.setTextFill(Color.web("#2C5F5F"));

        item.getChildren().addAll(foodLabel, nameLabel, priceLabel);
        return item;
    }

    private VBox createOrderPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-background-color: #E8E8E8; -fx-background-radius: 8;");
        panel.setPrefWidth(650);

        Label titleLabel = new Label("Phiếu gọi món");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#D4A84A"));
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setMaxWidth(Double.MAX_VALUE);

        Label tableLabel = new Label("Bàn: B101V");
        tableLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        tableLabel.setTextFill(Color.web("#D4A84A"));

        TableView<OrderItem> table = new TableView<>();
        table.setPrefHeight(450);
        table.setStyle("-fx-background-color: white;");

        TableColumn<OrderItem, String> sttCol = new TableColumn<>("STT");
        TableColumn<OrderItem, String> nameCol = new TableColumn<>("Tên món");
        TableColumn<OrderItem, String> priceCol = new TableColumn<>("Đơn giá");
        TableColumn<OrderItem, String> qtyCol = new TableColumn<>("Số lượng");
        TableColumn<OrderItem, String> totalCol = new TableColumn<>("Thành tiền");
        TableColumn<OrderItem, Void> actionCol = new TableColumn<>("Hành động");
        TableColumn<OrderItem, String> noteCol = new TableColumn<>("Ghi chú");

        sttCol.setCellValueFactory(cellData -> cellData.getValue().stt);
        nameCol.setCellValueFactory(cellData -> cellData.getValue().name);
        priceCol.setCellValueFactory(cellData -> cellData.getValue().price);
        qtyCol.setCellValueFactory(cellData -> cellData.getValue().qty);
        totalCol.setCellValueFactory(cellData -> cellData.getValue().total);
        noteCol.setCellValueFactory(cellData -> cellData.getValue().note);

        actionCol.setCellFactory(col -> new TableCell<OrderItem, Void>() {
            private final Label minus = new Label("-");
            private final Label plus = new Label("+");
            private final Label qtyLabel = new Label();

            {
                minus.setStyle("-fx-font-size: 18px; -fx-cursor: hand;");
                plus.setStyle("-fx-font-size: 18px; -fx-cursor: hand;");
                qtyLabel.setStyle("-fx-font-size: 14px; -fx-padding: 0 8;");

                minus.setOnMouseClicked(e -> {
                    OrderItem item = getTableView().getItems().get(getIndex());
                    int q = Integer.parseInt(item.getQty());
                    if (q > 1) item.setQty(String.valueOf(q - 1));
                    qtyLabel.setText(item.getQty());
                    getTableView().refresh();
                });

                plus.setOnMouseClicked(e -> {
                    OrderItem item = getTableView().getItems().get(getIndex());
                    int q = Integer.parseInt(item.getQty());
                    item.setQty(String.valueOf(q + 1));
                    qtyLabel.setText(item.getQty());
                    getTableView().refresh();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {  // ✅ đổi String → Void
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    OrderItem orderItem = getTableView().getItems().get(getIndex());
                    qtyLabel.setText(orderItem.getQty());
                    HBox box = new HBox(8, minus, qtyLabel, plus);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(sttCol, nameCol, priceCol, qtyCol, totalCol, actionCol, noteCol);

        table.getItems().addAll(
            new OrderItem("1", "Cơm bò", "45,000", "2", "90,000", "Ít cay"),
            new OrderItem("2", "Phở gà", "40,000", "1", "40,000", ""),
            new OrderItem("3", "Bún chả", "50,000", "3", "150,000", "Không hành"),
            new OrderItem("4", "Trà đào", "25,000", "2", "50,000", "Ít đá")
        );

        HBox totalBox = new HBox();
        totalBox.setAlignment(Pos.CENTER_RIGHT);
        Label totalLabel = new Label("Tổng tiền: 330,000 VND");
        totalLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        totalLabel.setTextFill(Color.web("#2C5F5F"));
        totalBox.getChildren().add(totalLabel);

        Button orderBtn = new Button("Gửi bếp");
        orderBtn.setStyle("-fx-background-color: #D4A84A; -fx-text-fill: #2C5F5F; "
                + "-fx-font-weight: bold; -fx-font-size: 16px; -fx-pref-width: 150px; "
                + "-fx-pref-height: 40px; -fx-background-radius: 8;");

        VBox rightBottomBox = new VBox(10, totalBox, orderBtn);
        rightBottomBox.setAlignment(Pos.CENTER_RIGHT);

        panel.getChildren().addAll(titleLabel, tableLabel, table, rightBottomBox);

        return panel;
    }

    public static class OrderItem {
        private final SimpleStringProperty stt;
        private final SimpleStringProperty name;
        private final SimpleStringProperty price;
        private final SimpleStringProperty qty;
        private final SimpleStringProperty total;
        private final SimpleStringProperty note;

        public OrderItem(String stt, String name, String price, String qty, String total, String note) {
            this.stt = new SimpleStringProperty(stt);
            this.name = new SimpleStringProperty(name);
            this.price = new SimpleStringProperty(price);
            this.qty = new SimpleStringProperty(qty);
            this.total = new SimpleStringProperty(total);
            this.note = new SimpleStringProperty(note);
        }

        public String getStt() { return stt.get(); }
        public String getName() { return name.get(); }
        public String getPrice() { return price.get(); }
        public String getQty() { return qty.get(); }
        public String getTotal() { return total.get(); }
        public String getNote() { return note.get(); }
        public void setQty(String qty) { this.qty.set(qty); }
    }
}
