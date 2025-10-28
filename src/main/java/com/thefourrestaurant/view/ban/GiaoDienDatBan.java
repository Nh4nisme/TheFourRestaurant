package com.thefourrestaurant.view.ban;

import com.thefourrestaurant.DAO.PhieuDatBanDAO;
import com.thefourrestaurant.DAO.TangDAO;
import com.thefourrestaurant.controller.PhieuDatBanController;
import com.thefourrestaurant.model.Ban;
import com.thefourrestaurant.model.PhieuDatBan;
import com.thefourrestaurant.model.Tang;
import com.thefourrestaurant.util.ClockText;
import com.thefourrestaurant.view.GiaoDienGoiMon;
import javafx.util.StringConverter;
import com.thefourrestaurant.view.hoadon.GiaoDienLapHoaDon;
import com.thefourrestaurant.view.components.ButtonSample2;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GiaoDienDatBan extends BorderPane {

    private static final String COLOR_BACKGROUND_MAIN = "#f0f0f0";
    private static final String COLOR_BACKGROUND_SIDE = "#1E424D";
    private static final String COLOR_TEXT = "#DDB248";
    
    private StackPane mainContent;
    private QuanLiBan quanLiBan;
    private ComboBox<Tang> cboSoTang;
    private final List<Ban> dsBanDangChon = new ArrayList<>();
    
    private TangDAO tangDAO = new TangDAO();
	private PhieuDatBanDAO phieuDAO;
    private PhieuDatBanController  phieuDatBanController = new PhieuDatBanController();

	public GiaoDienDatBan(StackPane mainContent) {
	    this.mainContent = mainContent;
	
	    this.setLeft(taoThanhBen());
	    this.setCenter(taoNoiDungChinh());
	
	    this.sceneProperty().addListener((obs, oldScene, newScene) -> {
	        if (newScene != null) {
	            newScene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
	                switch (event.getCode()) {
	                    case F1 -> datBanNgay();
	                    case F2 -> datBanTruoc();
	                    case F3 -> nhanBan();
	                    case F4 -> huyBanDatTruoc();
	                    case F5 -> datMon();
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
        ClockText boDemGio = new ClockText();
        boDemGio.setStyle("-fx-fill: #DDB248; -fx-font-size: 15px; -fx-font-weight: bold;");

        header.getChildren().addAll(lblDanhSachBan,boDemGio);
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

        quanLiBan = new QuanLiBan(mainContent, "DAT_BAN");
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

        ComboBox<String> cboBanDatTruoc = new ComboBox<>();
        cboBanDatTruoc.setPrefHeight(45);
        cboBanDatTruoc.getItems().add("Bàn đặt trước");
        cboBanDatTruoc.setValue("Bàn đặt trước");
        styleComboBox(cboBanDatTruoc, 150);

        Label lblSoTang = taoLabel("Số tầng:", 16, true);
        lblSoTang.setPrefWidth(70);

        cboSoTang = new ComboBox<>();
        List<Tang> dsTang = tangDAO.layTatCaTang();

        for (Tang tang : dsTang) {
            cboSoTang.getItems().add(tang);
        }
        
        if (!dsTang.isEmpty()) {
            cboSoTang.setValue(dsTang.get(0));
        }

        cboSoTang.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Tang item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTenTang());
            }
        });

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

        hang1.getChildren().addAll(cboBanDatTruoc, taoSpacerH(60), lblSoTang, cboSoTang, taoSpacerH(60), lblMaBan, txtMaBan, taoSpacerH(60), btnTim);
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
        cboLoaiBan.setPrefWidth(200);

        Label lblSoGhe = taoLabel("Số ghế:", 16, true);
        lblSoGhe.setPrefWidth(70);

        ComboBox<String> cboSoGhe = new ComboBox<>();
        cboSoGhe.getItems().addAll("Tất cả", "Có ghi chú", "Không ghi chú");
        cboSoGhe.setPromptText("Chọn");
        cboSoGhe.setPrefWidth(100);

        ButtonSample2 btnLamMoi = new ButtonSample2("Làm mới", ButtonSample2.Variant.YELLOW, 120, 45);

        hang2.getChildren().addAll(taoSpacerH(220),lblLoaiBan, cboLoaiBan, taoSpacerH(60),lblSoGhe, cboSoGhe, taoSpacerH(185),btnLamMoi);
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
        Ban banDuocChon = layBanDangChonHoacThongBao();
        if (banDuocChon == null) return;

        System.out.println("Đặt bàn ngay cho bàn: " + banDuocChon.getTenBan());
        Stage st = new Stage();
        st.initOwner(getScene() != null ? getScene().getWindow() : null);
        st.initModality(Modality.APPLICATION_MODAL);
        st.setTitle("Đặt bàn ngay - " + banDuocChon.getTenBan());
        st.setScene(new Scene(new GiaoDienDatBanNgay(banDuocChon, mainContent)));
        st.showAndWait();
    }


    private void datBanTruoc() {
        Ban banDuocChon = layBanDangChonHoacThongBao();
        if (banDuocChon == null) return;

        System.out.println("Đặt bàn trước cho bàn: " + banDuocChon.getTenBan());
        Stage st = new Stage();
        st.initOwner(getScene() != null ? getScene().getWindow() : null);
        st.initModality(Modality.APPLICATION_MODAL);
        st.setTitle("Đặt bàn trước - " + banDuocChon.getTenBan());
        st.setScene(new Scene(new GiaoDienDatBanTruoc(banDuocChon, mainContent)));
        st.showAndWait();
    }

    private void nhanBan() {
        System.out.println("Nhận bàn (F3)");
    }

    private void huyBanDatTruoc() {
        Ban banDuocChon = layBanDangChonHoacThongBao();
        if (banDuocChon == null) return;

        Stage stage = new Stage();
        stage.initOwner(getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Danh sách phiếu đặt trước - " + banDuocChon.getTenBan());

        GiaoDienHuyBanDatTruoc giaoDien = new GiaoDienHuyBanDatTruoc(banDuocChon);
        stage.setScene(new Scene(giaoDien, 700, 400));
        stage.showAndWait();
    }

    private void datMon() {
        Ban banDuocChon = layBanDangChonHoacThongBao();
        if (banDuocChon == null) return;
        
        phieuDAO = new PhieuDatBanDAO();
        System.out.println(banDuocChon.getMaBan());
        PhieuDatBan pdbHienCo = phieuDAO.layPhieuDangHoatDongTheoBan(banDuocChon.getMaBan());

        System.out.println("🍽️ Đặt món cho bàn: " + banDuocChon.getTenBan());
        System.out.println(pdbHienCo.toString());
        mainContent.getChildren().clear();
        mainContent.getChildren().add(new GiaoDienGoiMon(mainContent, banDuocChon, pdbHienCo));
    }

    private void tinhTien() {
        Ban banDuocChon = layBanDangChonHoacThongBao();
        if (banDuocChon == null) return;

        // Lấy phiếu đặt bàn tương ứng
        PhieuDatBan pdb = phieuDatBanController.layPhieuTheoBan(banDuocChon.getMaBan());

        System.out.println("💰 Tính tiền cho bàn: " + banDuocChon.getTenBan());
        Stage stageThanhToan = new Stage();
        GiaoDienLapHoaDon thanhToan = new GiaoDienLapHoaDon(stageThanhToan);

        thanhToan.hienThiThongTin(pdb);
    }

    private void tangTruoc() {
        int currentIndex = cboSoTang.getSelectionModel().getSelectedIndex();
        if (currentIndex > 0) {
            cboSoTang.getSelectionModel().select(currentIndex - 1);
            Tang tangMoi = cboSoTang.getSelectionModel().getSelectedItem();
            if (tangMoi != null) {
                quanLiBan.hienThiBanTheoTang(tangMoi.getMaTang());
                System.out.println("🔼 Chuyển đến " + tangMoi.getTenTang());
            }
        } else {
            System.out.println("Đang ở tầng thấp nhất!");
        }
    }

    private void tangSau() {
        int currentIndex = cboSoTang.getSelectionModel().getSelectedIndex();
        int maxIndex = cboSoTang.getItems().size() - 1;

        if (currentIndex < maxIndex) {
            cboSoTang.getSelectionModel().select(currentIndex + 1);
            Tang tangMoi = cboSoTang.getSelectionModel().getSelectedItem();
            if (tangMoi != null) {
                quanLiBan.hienThiBanTheoTang(tangMoi.getMaTang());
                System.out.println("Chuyển đến " + tangMoi.getTenTang());
            }
        } else {
            System.out.println("Đang ở tầng cao nhất!");
        }
    }
    
    private Ban layBanDangChonHoacThongBao() {
        Ban banDuocChon = quanLiBan.getBanDangChon();
        if (banDuocChon == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn bàn trước khi thao tác!");
            alert.showAndWait();
            return null;
        }
        return banDuocChon;
    }

}