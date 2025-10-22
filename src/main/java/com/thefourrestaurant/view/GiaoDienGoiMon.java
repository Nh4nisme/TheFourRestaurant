package com.thefourrestaurant.view;

import java.util.List;

import com.thefourrestaurant.DAO.MonAnDAO;
import com.thefourrestaurant.model.ChiTietPDB;
import com.thefourrestaurant.model.MonAn;
import com.thefourrestaurant.view.components.ButtonSample;
import com.thefourrestaurant.view.components.DropDownButton;
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

public class GiaoDienGoiMon extends BorderPane {

    private ButtonSample btnTim, btnLamMoi;
    private VBox mainContainer;
    private MonAnDAO monAnDAO = new MonAnDAO();
    
    private TableView<ChiTietPDB> bangPhieu;
    private Label lblTongTien;


	public GiaoDienGoiMon() {
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
	
	    // 🔹 Lấy danh sách món ăn từ DB
	    List<MonAn> danhSachMon = monAnDAO.layTatCaMonAn();
	
	    int col = 0;
	    int row = 0;
	    for (MonAn mon : danhSachMon) {
	        VBox monBox = new MonAnBox(
	            mon.getTenMon(),
	            String.format("%,.0f", mon.getDonGia().doubleValue()), // format giá
	            mon.getHinhAnh() != null ? mon.getHinhAnh() : "🍽️" // tạm icon nếu chưa có ảnh
	        );
	
	        grid.add(monBox, col, row);
	
	        col++;
	        if (col == 4) { // 4 cột
	            col = 0;
	            row++;
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
	
	    bangPhieu = new TableView<>();
	    bangPhieu.setPrefHeight(450);
	    bangPhieu.setStyle("-fx-background-color: white;");
	
	    TableColumn<ChiTietPDB, String> tenMonCol = new TableColumn<>("Tên món");
	    TableColumn<ChiTietPDB, String> donGiaCol = new TableColumn<>("Đơn giá");
	    TableColumn<ChiTietPDB, String> soLuongCol = new TableColumn<>("Số lượng");
	    TableColumn<ChiTietPDB, String> thanhTienCol = new TableColumn<>("Thành tiền");
	
	    tenMonCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMonAn().getTenMon()));
	    donGiaCol.setCellValueFactory(c -> new SimpleStringProperty(String.format("%,.0f", c.getValue().getDonGia())));
	    soLuongCol.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getSoLuong())));
	    thanhTienCol.setCellValueFactory(c -> new SimpleStringProperty(
	            String.format("%,.0f", c.getValue().getSoLuong() * c.getValue().getDonGia())
	    ));
	
	    bangPhieu.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	    bangPhieu.getColumns().addAll(tenMonCol, donGiaCol, soLuongCol, thanhTienCol);
	
	    lblTongTien = new Label("Tổng tiền: 0 VND");
	    lblTongTien.setFont(Font.font("System", FontWeight.BOLD, 16));
	    lblTongTien.setTextFill(Color.web("#2C5F5F"));
	
	    HBox tongTienBox = new HBox(lblTongTien);
	    tongTienBox.setAlignment(Pos.CENTER_RIGHT);
	
	    ButtonSample btnGuiBep = new ButtonSample("Gửi bếp", 45, 35, 14);
	    VBox boxDuoi = new VBox(10, tongTienBox, btnGuiBep);
	    boxDuoi.setAlignment(Pos.CENTER_RIGHT);
	
	    panel.getChildren().addAll(lblTieuDe, lblBan, bangPhieu, boxDuoi);
	    return panel;
	}


}
