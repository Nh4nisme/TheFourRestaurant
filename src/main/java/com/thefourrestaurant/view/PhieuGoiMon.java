package com.thefourrestaurant.view;

import java.util.List;

import com.thefourrestaurant.view.components.ButtonSample;
import com.thefourrestaurant.view.components.DropDownButton;
import com.thefourrestaurant.view.components.NavBar;
import com.thefourrestaurant.view.monan.MonAnBox;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PhieuGoiMon extends BorderPane {

    private ButtonSample btnTim, btnLamMoi;
    private VBox mainContainer;

	public PhieuGoiMon() {
        this.setStyle("-fx-background-color: white;");
        HBox thanhTren = taoThanhTren();

        VBox topContainer = new VBox(thanhTren);
        this.setTop(topContainer);

        HBox noiDungChinh = new HBox(20);

        VBox khungTrai = taoLuoiMonAn();
        VBox khungPhai = taoKhungPhieuGoiMon();

        khungTrai.setAlignment(Pos.TOP_CENTER);
        khungPhai.setAlignment(Pos.TOP_CENTER);

        HBox.setHgrow(khungTrai, Priority.ALWAYS);
        HBox.setHgrow(khungPhai, Priority.ALWAYS);

        noiDungChinh.getChildren().addAll(khungTrai, khungPhai);
        this.setCenter(noiDungChinh);
    }

    private HBox taoThanhTren() {

        HBox thanhTren = new HBox(15);
        thanhTren.setPadding(new Insets(15, 20, 15, 20));
        thanhTren.setAlignment(Pos.CENTER_LEFT);
        thanhTren.setStyle("-fx-background-color: #1E424D;");

        DropDownButton menuThucDon = new DropDownButton(
                "Thực đơn  ▼",
                List.of("Buổi Sáng  ▼","Buổi Trưa  ▼","Buổi Tối  ▼","Khai Vị  ▼"),
                null,
                45,
                16,
                3
        );

        Label lblLoaiMon = new Label("Loại món:");
        lblLoaiMon.setTextFill(Color.web("#D4A84A"));
        lblLoaiMon.setFont(Font.font("System", FontWeight.BOLD, 14));

        TextField txtLoaiMon = new TextField();
        txtLoaiMon.setPromptText("Tìm loại món...");
        txtLoaiMon.setPrefWidth(200);

        Label lblTenMon = new Label("Tên món:");
        lblTenMon.setTextFill(Color.web("#D4A84A"));
        lblTenMon.setFont(Font.font("System", FontWeight.BOLD, 14));

        TextField txtTenMon = new TextField();
        txtTenMon.setPromptText("Tìm tên món...");
        txtTenMon.setPrefWidth(300);

        btnTim = new ButtonSample("Tìm kiếm", "", 35, 14,3);
        btnLamMoi = new ButtonSample("Làm mới", "", 35, 14,3);

        thanhTren.getChildren().addAll(menuThucDon, lblLoaiMon, txtLoaiMon, lblTenMon, txtTenMon, btnTim, btnLamMoi);
        return thanhTren;
    }

    private VBox taoLuoiMonAn() {
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

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                VBox menuItem = taoMonAn();
                grid.add(menuItem, col, row);
            }
        }

        HBox phanTrang = new HBox(10);
        phanTrang.setAlignment(Pos.CENTER);
        phanTrang.setPadding(new Insets(10));

        Button btnDau = new Button("⏮");
        Button btnTruoc = new Button("◀");
        Label lblTrang = new Label("1");
        lblTrang.setFont(Font.font(12));
        Button btnSau = new Button("▶");
        Button btnCuoi = new Button("⏭");

        String kieuBtn = "-fx-background-color: white; -fx-text-fill: #2C5F5F; "
                + "-fx-font-weight: bold; -fx-pref-width: 40px;";
        btnDau.setStyle(kieuBtn);
        btnTruoc.setStyle(kieuBtn);
        btnSau.setStyle(kieuBtn);
        btnCuoi.setStyle(kieuBtn);

        phanTrang.getChildren().addAll(btnDau, btnTruoc, lblTrang, btnSau, btnCuoi);

        container.getChildren().addAll(grid, phanTrang);
        return container;
    }

    private VBox taoMonAn() {
        return new MonAnBox("Cơm gà cay", "45,000", "🍲");
    }

    private VBox taoKhungPhieuGoiMon() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-background-color: #E8E8E8; -fx-background-radius: 8;");
        panel.setPrefWidth(650);

        Label lblTieuDe = new Label("PHIẾU GỌI MÓN");
        lblTieuDe.setFont(Font.font("System", FontWeight.BOLD, 28));
        lblTieuDe.setTextFill(Color.web("#D4A84A"));
        lblTieuDe.setAlignment(Pos.CENTER);
        lblTieuDe.setMaxWidth(Double.MAX_VALUE);

        Label lblBan = new Label("Bàn: B101V");
        lblBan.setFont(Font.font("System", FontWeight.BOLD, 18));
        lblBan.setTextFill(Color.web("#D4A84A"));

        TableView<OrderItem> bang = new TableView<>();
        bang.setPrefHeight(450);
        bang.setStyle("-fx-background-color: white;");

        TableColumn<OrderItem, String> sttCol = new TableColumn<>("STT");
        TableColumn<OrderItem, String> tenMonCol = new TableColumn<>("Tên món");
        TableColumn<OrderItem, String> donGiaCol = new TableColumn<>("Đơn giá");
        TableColumn<OrderItem, String> soLuongCol = new TableColumn<>("Số lượng");
        TableColumn<OrderItem, String> thanhTienCol = new TableColumn<>("Thành tiền");
        TableColumn<OrderItem, Void> hanhDongCol = new TableColumn<>("Thay đổi");
        TableColumn<OrderItem, String> ghiChuCol = new TableColumn<>("Ghi chú");

        sttCol.setCellValueFactory(c -> c.getValue().stt);
        tenMonCol.setCellValueFactory(c -> c.getValue().name);
        donGiaCol.setCellValueFactory(c -> c.getValue().price);
        soLuongCol.setCellValueFactory(c -> c.getValue().qty);
        thanhTienCol.setCellValueFactory(c -> c.getValue().total);
        ghiChuCol.setCellValueFactory(c -> c.getValue().note);

        hanhDongCol.setCellFactory(col -> new TableCell<>() {
            private final Label btnTru = new Label("-");
            private final Label btnCong = new Label("+");
            private final Label lblSL = new Label();

            {
                btnTru.setStyle("-fx-font-size: 18px; -fx-cursor: hand;");
                btnCong.setStyle("-fx-font-size: 18px; -fx-cursor: hand;");
                lblSL.setStyle("-fx-font-size: 14px; -fx-padding: 0 8;");

                btnTru.setOnMouseClicked(e -> {
                    OrderItem item = getTableView().getItems().get(getIndex());
                    int q = Integer.parseInt(item.getQty());
                    if (q > 1) {
						item.setQty(String.valueOf(q - 1));
					}
                    lblSL.setText(item.getQty());
                    getTableView().refresh();
                });

                btnCong.setOnMouseClicked(e -> {
                    OrderItem item = getTableView().getItems().get(getIndex());
                    int q = Integer.parseInt(item.getQty());
                    item.setQty(String.valueOf(q + 1));
                    lblSL.setText(item.getQty());
                    getTableView().refresh();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    OrderItem mon = getTableView().getItems().get(getIndex());
                    lblSL.setText(mon.getQty());
                    HBox box = new HBox(8, btnTru, lblSL, btnCong);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });

        bang.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        bang.getColumns().addAll(sttCol, tenMonCol, donGiaCol, soLuongCol, thanhTienCol, hanhDongCol, ghiChuCol);

        bang.getItems().addAll(
                new OrderItem("1", "Cơm bò", "45,000", "2", "90,000", "Ít cay"),
                new OrderItem("2", "Phở gà", "40,000", "1", "40,000", ""),
                new OrderItem("3", "Bún chả", "50,000", "3", "150,000", "Không hành"),
                new OrderItem("4", "Trà đào", "25,000", "2", "50,000", "Ít đá")
        );

        HBox tongTienBox = new HBox();
        tongTienBox.setAlignment(Pos.CENTER_RIGHT);
        Label lblTong = new Label("Tổng tiền: 330,000 VND");
        lblTong.setFont(Font.font("System", FontWeight.BOLD, 16));
        lblTong.setTextFill(Color.web("#2C5F5F"));
        tongTienBox.getChildren().add(lblTong);

        ButtonSample btnGuiBep = new ButtonSample("Gửi bếp", 45, 35, 14);

        VBox boxDuoi = new VBox(10, tongTienBox, btnGuiBep);
        boxDuoi.setAlignment(Pos.CENTER_RIGHT);

        panel.getChildren().addAll(lblTieuDe, lblBan, bang, boxDuoi);
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
