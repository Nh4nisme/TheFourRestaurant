package com.thefourrestaurant.view.ban;

import com.thefourrestaurant.DAO.TangDAO;
import com.thefourrestaurant.model.Tang;
import com.thefourrestaurant.view.GiaoDienGoiMon;
import com.thefourrestaurant.view.components.ButtonSample2;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.List;

public class GiaoDienDatBan extends BorderPane {

    private static final String COLOR_BACKGROUND_MAIN = "#f0f0f0";
    private static final String COLOR_BACKGROUND_SIDE = "#1E424D";
    private static final String COLOR_TEXT = "#DDB248";
    
    private StackPane mainContent;
    
    private QuanLiBan quanLiBan;
    TangDAO tangDAO = new TangDAO();

	public GiaoDienDatBan(StackPane mainContent) {
	    this.mainContent = mainContent;
	
	    this.setLeft(taoThanhBen());
	    this.setCenter(taoNoiDungChinh());
	
	    // Lắng nghe phím F1–F8
	    this.sceneProperty().addListener((obs, oldScene, newScene) -> {
	        if (newScene != null) {
	            newScene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
	                switch (event.getCode()) {
	                    case F1 -> datBanNgay();
	                    case F2 -> datBanTruoc();
	                    case F3 -> nhanBan();
	                    case F4 -> huyBanDatTruoc();
	                    case F5 -> datMon();     // Sửa phần này
	                    case F6 -> tinhTien();
	                    case F7 -> tangTruoc();
	                    case F8 -> tangSau();
	                    default -> {}
	                }
	            });
	        }
	    });
	
	    this.setFocusTraversable(true);
	
	    // Tự động focus khi hiển thị
	    this.sceneProperty().addListener((obs, oldScene, newScene) -> {
	        if (newScene != null) {
	            newScene.windowProperty().addListener((o, oldWin, newWin) -> {
	                if (newWin != null) {
	                    newWin.focusedProperty().addListener((ob, was, isNow) -> {
	                        if (isNow) this.requestFocus();
	                    });
	                }
	            });
	        }
	    });
	}


    private VBox taoThanhBen() {
        VBox thanhBen = new VBox();
        thanhBen.setPrefWidth(250);
        thanhBen.setStyle("-fx-background-color: " + COLOR_BACKGROUND_SIDE + ";");

        thanhBen.getChildren().addAll(
                taoHeaderThanhBen(),
                taoNutChucNang(),
                taoSpacer(),
                taoChuThichThanhBen()
        );

        return thanhBen;
    }

    private VBox taoHeaderThanhBen() {
        VBox header = new VBox(5);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: " + COLOR_BACKGROUND_SIDE + ";");

        Label lblDanhSachBan = taoLabel("Danh sách bàn", 20, true);
        Label lblNgay = taoLabel("Thứ hai - 03/06/2025", 16, false);
        Label lblGio = taoLabel("12:36:36", 16, false);

        header.getChildren().addAll(lblDanhSachBan, lblNgay, lblGio);
        return header;
    }

    private VBox taoNutChucNang() {
        VBox cacNut = new VBox(15);
        cacNut.setPadding(new Insets(20, 10, 10, 10));

        // Danh sách nút: [Text chính, Phím tắt]
        List<String[]> nutTitles = List.of(
                new String[]{"Đặt bàn ngay", "F1"},
                new String[]{"Đặt bàn trước", "F2"},
                new String[]{"Nhận bàn", "F3"},
                new String[]{"Hủy bàn đặt trước", "F4"},
                new String[]{"Đặt món", "F5"},
                new String[]{"Tính tiền", "F6"},
                new String[]{"Tầng trước", "F7"},
                new String[]{"Tầng sau", "F8"}
        );

        for (String[] title : nutTitles) {
		    String tenNut = title[0];
		    String phimTat = title[1];
		    
		    ButtonSample2 btn = new ButtonSample2("", ButtonSample2.Variant.YELLOW, 220, 55);
		
		    HBox content = new HBox();
		    content.setPadding(new Insets(0, 10, 0, 10));
		    content.setAlignment(Pos.CENTER_LEFT);
		
		    Label lblText = new Label(tenNut);
		    lblText.setStyle("-fx-font-weight: bold; -fx-text-fill: #1E424D;");
		
		    Label lblShortcut = new Label(phimTat);
		    lblShortcut.setStyle("-fx-font-weight: bold; -fx-text-fill: #1E424D;");
		
		    Region spacer = new Region();
		    HBox.setHgrow(spacer, Priority.ALWAYS);
		
		    content.getChildren().addAll(lblText, spacer, lblShortcut);
		    btn.setGraphic(content);
		
		    // Gán hành động cho từng nút tương ứng phím tắt
		    switch (phimTat) {
		        case "F1" -> btn.setOnAction(e -> datBanNgay());
		        case "F2" -> btn.setOnAction(e -> datBanTruoc());
		        case "F3" -> btn.setOnAction(e -> nhanBan());
		        case "F4" -> btn.setOnAction(e -> huyBanDatTruoc());
		        case "F5" -> btn.setOnAction(e -> datMon());
		        case "F6" -> btn.setOnAction(e -> tinhTien());
		        case "F7" -> btn.setOnAction(e -> tangTruoc());
		        case "F8" -> btn.setOnAction(e -> tangSau());
		    }
		
		    cacNut.getChildren().add(btn);
		}

        return cacNut;
    }

    private Region taoSpacer() {
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    private Region taoSpacerH(double width) {
        Region spacer = new Region();
        spacer.setPrefWidth(width);
        HBox.setHgrow(spacer, Priority.NEVER); // spacer không giãn thêm
        return spacer;
    }

    private VBox taoChuThichThanhBen() {
        VBox chuThich = new VBox(10);
        chuThich.setPadding(new Insets(30, 10, 10, 10));

        List<LegendItem> items = List.of(
                new LegendItem("Bàn trống", Color.WHITE),
                new LegendItem("Bàn đã được đặt trước", Color.web("#87CEEB")),
                new LegendItem("Bàn đang sử dụng", Color.web("#FFB347"))
        );

        for (LegendItem item : items) {
            chuThich.getChildren().add(taoChuThich(item.text, item.color));
        }

        return chuThich;
    }

    private HBox taoChuThich(String text, Color color) {
        HBox legend = new HBox(10);
        legend.setAlignment(Pos.CENTER_LEFT);

        Circle circle = new Circle(8, color);
        Label lbl = taoLabel(text, 16, false);

        legend.getChildren().addAll(circle, lbl);
        return legend;
    }

    private VBox taoNoiDungChinh() {
        VBox noiDungChinh = new VBox();
        noiDungChinh.setStyle("-fx-background-color: " + COLOR_BACKGROUND_MAIN + ";");

        VBox thanhDieuHuong = taoThanhDieuHuong();

        quanLiBan = new QuanLiBan();
        quanLiBan.hienThiBanTheoTang("TG000001");

        Pane khuVucBan = quanLiBan.getKhuVucBan();
        VBox.setVgrow(khuVucBan, Priority.ALWAYS);

        noiDungChinh.getChildren().addAll(thanhDieuHuong, khuVucBan);
        return noiDungChinh;
    }

    private VBox taoThanhDieuHuong() {
        VBox thanhDieuHuong = new VBox(10);
        thanhDieuHuong.setPadding(new Insets(10));
        thanhDieuHuong.setStyle(
                "-fx-background-color: " + COLOR_BACKGROUND_SIDE + ";" +
                        "-fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;"
        );

        thanhDieuHuong.getChildren().addAll(taoHang1(), taoHang2());
        return thanhDieuHuong;
    }

    private HBox taoHang1() {
        HBox hang1 = new HBox(10);
        hang1.setPadding(new Insets(0,0,0,20));
        hang1.setAlignment(Pos.CENTER_LEFT);

        Label lblBanDatTruoc = taoLabel("Bàn đặt trước:", 16, true);
        lblBanDatTruoc.setPrefWidth(120);

        ComboBox<String> cboBanDatTruoc = new ComboBox<>();
        cboBanDatTruoc.setPrefHeight(45);
        cboBanDatTruoc.getItems().add("Bàn đặt trước");
        cboBanDatTruoc.setValue("Bàn đặt trước");
        styleComboBox(cboBanDatTruoc, 150);

        Label lblSoTang = taoLabel("Số tầng:", 16, true);
        lblSoTang.setPrefWidth(70);

        ComboBox<Tang> cboSoTang = new ComboBox<>();
        List<Tang> dsTang = tangDAO.layTatCaTang();

        for (Tang tang : dsTang) {
            cboSoTang.getItems().add(tang);
        }

        cboSoTang.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Tang item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTenTang());
            }
        });
        cboSoTang.setPromptText("Chọn tầng");

        cboSoTang.setOnAction(e -> {
            Tang selectedTang = cboSoTang.getValue();
            if (selectedTang != null) {
                quanLiBan.hienThiBanTheoTang(selectedTang.getMaTang());
            }
        });
        
        Label lblMaBan = taoLabel("Mã bàn:", 16, true);
        lblMaBan.setPrefWidth(70);

        TextField txtMaBan = new TextField();
        txtMaBan.setPrefWidth(300);

        ButtonSample2 btnTim = new ButtonSample2("Tìm", ButtonSample2.Variant.YELLOW, 120, 45);

        hang1.getChildren().addAll(lblBanDatTruoc, cboBanDatTruoc, taoSpacerH(60), lblSoTang, cboSoTang, taoSpacerH(60), lblMaBan, txtMaBan, taoSpacerH(60), btnTim);
        return hang1;
    }

    private HBox taoHang2() {
        HBox hang2 = new HBox(10);
        hang2.setPadding(new Insets(0,0,0,20));
        hang2.setAlignment(Pos.CENTER_LEFT);


        Label lblLoaiBan = taoLabel("Loại bàn:", 16, true);
        lblLoaiBan.setPrefWidth(70);

        ComboBox<String> cboLoaiBan = new ComboBox<>();
        cboLoaiBan.getItems().addAll("Tất cả", "Bàn tròn", "Bàn vuông");
        cboLoaiBan.setPromptText("Chọn");
        cboLoaiBan.setPrefWidth(250);

        Label lblSoGhe = taoLabel("Số ghế:", 16, true);
        lblSoGhe.setPrefWidth(70);

        ComboBox<String> cboSoGhe = new ComboBox<>();
        cboSoGhe.getItems().addAll("Tất cả", "Có ghi chú", "Không ghi chú");
        cboSoGhe.setPromptText("Chọn");
        cboSoGhe.setPrefWidth(300);

        ButtonSample2 btnLamMoi = new ButtonSample2("Làm mới", ButtonSample2.Variant.YELLOW, 120, 45);

        hang2.getChildren().addAll(taoSpacerH(350),lblLoaiBan, cboLoaiBan, taoSpacerH(60),lblSoGhe, cboSoGhe, taoSpacerH(60),btnLamMoi);
        return hang2;
    }

    private void styleComboBox(ComboBox<String> comboBox, double width) {
        comboBox.setStyle("-fx-background-color: " + COLOR_TEXT + "; -fx-text-fill: " + COLOR_BACKGROUND_SIDE + "; -fx-font-weight: bold;");
        comboBox.setPrefWidth(width);
        HBox.setHgrow(comboBox, Priority.ALWAYS);
    }

    private Label taoLabel(String text, int fontSize, boolean bold) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-text-fill: " + COLOR_TEXT + ";" + (bold ? "-fx-font-weight: bold;" : "") + "-fx-font-size: " + fontSize + "px;");
        return lbl;
    }

    private static class LegendItem {
        String text;
        Color color;

        LegendItem(String text, Color color) {
            this.text = text;
            this.color = color;
        }
    }
    
    private void datBanNgay() {
        System.out.println("Đặt bàn ngay (F1)");
    }

    private void datBanTruoc() {
        System.out.println("Đặt bàn trước (F2)");
    }

    private void nhanBan() {
        System.out.println("Nhận bàn (F3)");
    }

    private void huyBanDatTruoc() {
        System.out.println("Hủy bàn đặt trước (F4)");
    }

    private void datMon() {
    	mainContent.getChildren().clear();                // Xóa giao diện hiện tại
        mainContent.getChildren().add(new GiaoDienGoiMon(mainContent));
    }

    private void tinhTien() {
        System.out.println("Tính tiền (F6)");
    }

    private void tangTruoc() {
        System.out.println("Chuyển tầng trước (F7)");
    }

    private void tangSau() {
        System.out.println("Chuyển tầng sau (F8)");
    }
}