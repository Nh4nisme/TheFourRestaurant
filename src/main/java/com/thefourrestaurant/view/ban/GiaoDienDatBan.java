package com.thefourrestaurant.view.ban;

import com.thefourrestaurant.DAO.BanDAO;
import com.thefourrestaurant.DAO.PhieuDatBanDAO;
import com.thefourrestaurant.DAO.TangDAO;
import com.thefourrestaurant.controller.PhieuDatBanController;
import com.thefourrestaurant.controller.ThanhToanController;
import com.thefourrestaurant.model.Ban;
import com.thefourrestaurant.model.PhieuDatBan;
import com.thefourrestaurant.model.Tang;
import com.thefourrestaurant.util.ClockText;
import com.thefourrestaurant.view.monan.GiaoDienGoiMon;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GiaoDienDatBan extends BorderPane {

    private static final String COLOR_BACKGROUND_MAIN = "#f0f0f0";
    private static final String COLOR_BACKGROUND_SIDE = "#1E424D";
    private static final String COLOR_TEXT = "#DDB248";
    private ThanhToanController thanhToanController = new ThanhToanController();
    
    private StackPane mainContent;
    private QuanLiBan quanLiBan;
    
    private TangDAO tangDAO = new TangDAO();
	private PhieuDatBanDAO phieuDAO = new PhieuDatBanDAO();
	private BanDAO banDAO = new BanDAO();
	private PhieuDatBan phieuDatTruoc;
    private PhieuDatBanController  phieuDatBanController = new PhieuDatBanController();
    
    private ComboBox<Tang> cboSoTang;
    private ComboBox<String> cboBanDatTruoc = new ComboBox<>();
    private ComboBox<String> cboLoaiBan = new ComboBox<>();
    private ComboBox<String> cboSoGhe = new ComboBox<>();
    
    private TextField txtMaBan = new TextField();
	

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
        ClockText boDemGio = ClockText.getInstance();
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

        cboBanDatTruoc.setPrefHeight(45);
        List<String> trangThaiList = banDAO.layDanhSachTrangThaiTuCSDL();

        List<String> trangThaiFullList = new ArrayList<>();
        trangThaiFullList.add("Tất cả");
        trangThaiFullList.addAll(trangThaiList);

        cboBanDatTruoc.getItems().addAll(trangThaiFullList);
        cboBanDatTruoc.setValue("Tất cả");
        styleComboBox(cboBanDatTruoc, 150);

        cboBanDatTruoc.setOnAction(e -> locBanTheoTatCaTieuChi());


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

        cboSoTang.setOnAction(e -> locBanTheoTatCaTieuChi());
        
        Label lblMaBan = taoLabel("Mã bàn:", 16, true);
        lblMaBan.setPrefWidth(70);

        txtMaBan.setPrefWidth(300);

        ButtonSample2 btnTim = new ButtonSample2("Tìm", ButtonSample2.Variant.YELLOW, 120, 45);
        btnTim.setOnAction(e -> {
            String maBan = txtMaBan.getText().trim();
            if (maBan.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng nhập mã bàn cần tìm!");
                alert.showAndWait();
                return;
            }
            timBanTheoMaBan();
        });

        hang1.getChildren().addAll(cboBanDatTruoc, taoSpacerH(60), lblSoTang, cboSoTang, taoSpacerH(60), lblMaBan, txtMaBan, taoSpacerH(60), btnTim);
        return hang1;
    }

    private HBox taoHang2() {
        HBox hang2 = new HBox(10);
        hang2.setPadding(new Insets(0,0,0,20));
        hang2.setAlignment(Pos.CENTER_LEFT);


        Label lblLoaiBan = taoLabel("Loại bàn:", 16, true);
        lblLoaiBan.setPrefWidth(70);

        cboLoaiBan.getItems().addAll("Tất cả", "Bàn tròn", "Bàn vuông");
        cboLoaiBan.setPromptText("Chọn");
        cboLoaiBan.setPrefWidth(200);
        
        cboLoaiBan.setOnAction(e -> locBanTheoTatCaTieuChi());

        Label lblSoGhe = taoLabel("Số ghế:", 16, true);
        lblSoGhe.setPrefWidth(70);

        cboSoGhe.setPrefHeight(45);

        // Danh sách số ghế từ tất cả bàn
        List<String> dsSoGhe = new ArrayList<>();
        dsSoGhe.add("Tất cả"); // mặc định hiển thị tất cả

        for (Ban ban : banDAO.layTatCaBan()) {
            String soGheStr = ban.getLoaiBan().getSoChoNgoi() + " ghế";
            if (!dsSoGhe.contains(soGheStr)) {
                dsSoGhe.add(soGheStr);
            }
        }

        cboSoGhe.getItems().addAll(dsSoGhe);
        cboSoGhe.setValue("Tất cả");
        styleComboBox(cboSoGhe, 100);

        cboSoGhe.setOnAction(e -> locBanTheoTatCaTieuChi());

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
        List<Ban> dsChon = layDsBanDangChonHoacThongBao();
        if (dsChon == null || dsChon.isEmpty()) return;

        // Kiểm tra trạng thái bàn
        for (Ban ban : dsChon) {
            PhieuDatBan pdbDangSuDung = phieuDAO.layPhieuDangHoatDongTheoBan(ban.getMaBan());
            if (pdbDangSuDung != null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Bàn đang sử dụng");
                alert.setHeaderText(null);
                alert.setContentText("Bàn " + ban.getTenBan() + " đang phục vụ, không thể đặt ngay!");
                alert.showAndWait();
                return;
            }
        }

        String tenBanStr = dsChon.stream()
                                 .map(Ban::getTenBan)
                                 .reduce((a, b) -> a + ", " + b)
                                 .orElse("");

        Stage st = new Stage();
        st.initOwner(getScene() != null ? getScene().getWindow() : null);
        st.initModality(Modality.APPLICATION_MODAL);
        st.setTitle("Đặt bàn ngay - " + tenBanStr);
        st.setScene(new Scene(new GiaoDienDatBanNgay(dsChon, mainContent, quanLiBan)));
        st.showAndWait();
    }

    private void datBanTruoc() {
        List<Ban> dsChon = layDsBanDangChonHoacThongBao();
        if (dsChon == null || dsChon.isEmpty()) return;

        String tenBanStr = dsChon.stream()
                                 .map(Ban::getTenBan)
                                 .reduce((a, b) -> a + ", " + b)
                                 .orElse("");

        Stage st = new Stage();
        st.initOwner(getScene() != null ? getScene().getWindow() : null);
        st.initModality(Modality.APPLICATION_MODAL);
        st.setTitle("Đặt bàn trước - " + tenBanStr);
        st.setScene(new Scene(new GiaoDienDatBanTruoc(dsChon, mainContent, quanLiBan)));
        st.showAndWait();
    }

    private void nhanBan() {
        List<Ban> dsChon = layDsBanDangChonHoacThongBao();
        if (dsChon == null || dsChon.isEmpty()) return;

        phieuDatTruoc = phieuDAO.layPhieuDatTruocTheoBan(dsChon.get(0).getMaBan());
        if (phieuDatTruoc == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Bàn " + dsChon.get(0).getTenBan() + " không có phiếu đặt trước nào!");
            alert.showAndWait();
            return;
        }

        boolean capNhatPhieu = phieuDAO.capNhatTrangThai(phieuDatTruoc.getMaPDB(), "Đang phục vụ");

        int soBanCapNhat = banDAO.capNhatTrangThaiDanhSach(dsChon, "Đang sử dụng");

        if (soBanCapNhat == dsChon.size() && capNhatPhieu) { // kiểm tra tất cả bàn đã được cập nhật
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thành công");
            alert.setHeaderText(null);
            String tenBanStr = dsChon.stream().map(Ban::getTenBan).reduce((a, b) -> a + ", " + b).orElse("");
            alert.setContentText("Các bàn " + tenBanStr + " đã được nhận và chuyển sang trạng thái 'Đang sử dụng'.");
            alert.showAndWait();

            if (cboSoTang.getValue() != null) {
                quanLiBan.hienThiBanTheoTang(cboSoTang.getValue().getMaTang());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Không thể nhận bàn. Vui lòng thử lại.");
            alert.showAndWait();
        }
    }

    private void huyBanDatTruoc() {
        List<Ban> dsChon = layDsBanDangChonHoacThongBao();
        if (dsChon == null || dsChon.isEmpty()) return;

        String tenBanStr = dsChon.stream()
                                 .map(Ban::getTenBan)
                                 .reduce((a, b) -> a + ", " + b)
                                 .orElse("");

        Stage stage = new Stage();
        stage.initOwner(getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Danh sách phiếu đặt trước - " + tenBanStr);

        GiaoDienHuyBanDatTruoc giaoDien = new GiaoDienHuyBanDatTruoc(dsChon.get(0), quanLiBan);
        stage.setScene(new Scene(giaoDien, 700, 400));
        stage.showAndWait();
    }

    private void datMon() {
        List<Ban> dsChon = layDsBanDangChonHoacThongBao();
        if (dsChon == null) return;
        
        PhieuDatBan pdbHienCo = phieuDAO.layPhieuDangHoatDongTheoBan(dsChon.get(0).getMaBan());
        if (pdbHienCo == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Bàn này chưa có phiếu đặt hoặc đang trống, không thể gọi món!");
            alert.showAndWait();
            return;
        }
        mainContent.getChildren().clear();
        mainContent.getChildren().add(new GiaoDienGoiMon(mainContent, dsChon.get(0), pdbHienCo));
    }

    private void tinhTien() {
        List<Ban> dsChon = layDsBanDangChonHoacThongBao();
        if (dsChon == null) return;

        PhieuDatBan pdb = phieuDatBanController
                .layPhieuTheoBan(dsChon.get(0).getMaBan());

        thanhToanController.moManThanhToan(pdb);
    }

    private void tangTruoc() {
        int currentIndex = cboSoTang.getSelectionModel().getSelectedIndex();
        if (currentIndex > 0) {
            cboSoTang.getSelectionModel().select(currentIndex - 1);
            Tang tangMoi = cboSoTang.getSelectionModel().getSelectedItem();
            if (tangMoi != null) {
                quanLiBan.hienThiBanTheoTang(tangMoi.getMaTang());
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
    
    private List<Ban> layDsBanDangChonHoacThongBao() {
        List<Ban> dsChon = quanLiBan.getDsBanDangChon();
        if (dsChon.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn ít nhất 1 bàn trước khi thao tác!");
            alert.showAndWait();
            return null;
        }
        return dsChon;
    }
    
    private void locBanTheoTatCaTieuChi() {
        Tang tang = cboSoTang.getValue();
        String maTang = (tang != null) ? tang.getMaTang() : null;

        String trangThai = cboBanDatTruoc.getValue();
        if ("Tất cả".equals(trangThai)) trangThai = null;

        String loaiBan = cboLoaiBan.getValue();
        if ("Tất cả".equals(loaiBan)) loaiBan = null;

        int soGhe = 0;
        String soGheStr = cboSoGhe.getValue();
        if (soGheStr != null && !soGheStr.equals("Tất cả")) {
            try {
                soGhe = Integer.parseInt(soGheStr.replace(" ghế", ""));
            } catch (NumberFormatException ignored) {}
        }

        quanLiBan.hienThiBanTheoDieuKien(maTang, trangThai, loaiBan, soGhe);
    }

    private void timBanTheoMaBan() {
        String maBan = txtMaBan.getText().trim();
        if (maBan.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập mã bàn cần tìm!");
            alert.showAndWait();
            return;
        }

        Ban ban = banDAO.layTheoMa(maBan);
        if (ban == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Không tìm thấy bàn có mã: " + maBan);
            alert.showAndWait();
            return;
        }

        // Chuyển sang tầng chứa bàn
        Tang tangHienTai = null;
        for (Tang t : cboSoTang.getItems()) {
            if (t.getMaTang().equals(ban.getTang().getMaTang())) {
                tangHienTai = t;
                break;
            }
        }

        if (tangHienTai != null) {
            cboSoTang.setValue(tangHienTai);
        }

        // Hiển thị chỉ bàn này
        quanLiBan.clearAllBan();
        quanLiBan.taoBan(quanLiBan.getKhuVucBan(), ban, new HashMap<>(), new HashMap<>());

    }

}