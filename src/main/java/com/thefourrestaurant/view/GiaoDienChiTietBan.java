package com.thefourrestaurant.view;

import com.thefourrestaurant.view.components.ButtonSample;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

public class GiaoDienChiTietBan extends BorderPane {

	private static final String MAU_THANH = "#1E424D";
	private static final String MAU_VANG = "#DDB248";
	private static final String MAU_VIEN_DEN = "#000000";

	public GiaoDienChiTietBan() {
		setStyle("-fx-background-color: #F5F5F5;");

		setTop(taoHeader());
		setCenter(taoNoiDung());
		setBottom(taoFooter());

		ChangeListener<javafx.scene.Parent> ganKichThuoc = (obs, cu, moi) -> {
			if (moi instanceof Region r) {
				this.prefWidthProperty().bind(r.widthProperty());
				this.prefHeightProperty().bind(r.heightProperty());
			}
		};
		parentProperty().addListener(ganKichThuoc);
	}

	private HBox taoHeader() {
		HBox header = new HBox();
		header.setAlignment(Pos.CENTER);
		header.setPadding(new Insets(8, 20, 8, 20));
		header.setStyle("-fx-background-color: " + MAU_THANH + ";");

		Label tieuDe = new Label("Chi tiết bàn");
		tieuDe.setStyle("-fx-text-fill: " + MAU_VANG + "; -fx-font-size: 22px; -fx-font-weight: bold;");
		header.getChildren().add(tieuDe);
		return header;
	}

	private HBox taoFooter() {
		HBox footer = new HBox();
		footer.setAlignment(Pos.CENTER);
		footer.setPadding(new Insets(12, 20, 12, 20));
		footer.setStyle("-fx-background-color: " + MAU_THANH + ";");

		ButtonSample nutQuayLai = new ButtonSample("Quay lại", 45, 16, 1);
		Region dayCach = new Region();
		HBox.setHgrow(dayCach, Priority.ALWAYS);
		ButtonSample nutTinhTien = new ButtonSample("Tính tiền", 45, 16, 1);
		footer.getChildren().addAll(nutQuayLai, dayCach, nutTinhTien);
		return footer;
	}

	private HBox taoNoiDung() {
		HBox trungTam = new HBox(28);
		trungTam.setPadding(new Insets(20));

		VBox cotTrai = taoCotTraiThongTin();
		VBox cotPhai = taoCotPhaiHoaDon();

		HBox.setHgrow(cotPhai, Priority.ALWAYS);
		cotPhai.setMaxWidth(Double.MAX_VALUE);

		trungTam.getChildren().addAll(cotTrai, cotPhai);
		return trungTam;
	}

	private VBox taoCotTraiThongTin() {
	// cột trái chứa thẻ thông tin.
	VBox trai = new VBox(18);
	trai.setPrefWidth(360);

	Label tieuDeTtb = new Label("Thông tin bàn");
	tieuDeTtb.setStyle("-fx-text-fill: " + MAU_VANG + "; -fx-font-size: 20px; -fx-font-weight: bold;");
	tieuDeTtb.setPadding(new Insets(6, 0, 6, 6));
	VBox theTtb = taoTheThongTin(new String[]{"Trạng Thái:", "Loại bàn:", "Số người:"});

	Label tieuDePdb = new Label("Phiếu đặt bàn");
	tieuDePdb.setStyle("-fx-text-fill: " + MAU_VANG + "; -fx-font-size: 20px; -fx-font-weight: bold;");
	tieuDePdb.setPadding(new Insets(6, 0, 6, 6));
	VBox thePdb = taoTheThongTin(new String[]{"Tên khách:", "Số điện thoại:", "Giờ nhận bàn:"});

	trai.getChildren().addAll(tieuDeTtb, theTtb, tieuDePdb, thePdb);
	return trai;
	}

	private VBox taoTheThongTin(String[] dong) {
		VBox the = new VBox(8);
		the.setPadding(new Insets(14));
		the.setPrefHeight(160);
		the.setStyle("-fx-background-color: white; -fx-border-color: " + MAU_VIEN_DEN + "; -fx-border-radius: 10; -fx-background-radius: 10;");

		for (String s : dong) {
			Label nhan = new Label(s);
			nhan.setStyle("-fx-font-size: 15px; -fx-text-fill: #333333;");
			the.getChildren().add(nhan);
		}
		return the;
	}

	private VBox taoCotPhaiHoaDon() {
	// cột phải
	VBox phai = new VBox(10);
	phai.setPadding(new Insets(0, 6, 0, 6));

	VBox khungPhai = new VBox(12);
	khungPhai.setPadding(new Insets(16));
	khungPhai.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");
	VBox.setVgrow(khungPhai, Priority.ALWAYS);

	Label tieuDe = new Label("Hóa đơn tạm tính");
	tieuDe.setStyle("-fx-text-fill: " + MAU_VANG + "; -fx-font-size: 20px; -fx-font-weight: bold;");

	GridPane thongTinNho = new GridPane();
	thongTinNho.setHgap(50);
	thongTinNho.setVgap(6);
	ColumnConstraints c1 = new ColumnConstraints(); c1.setPercentWidth(50);
	ColumnConstraints c2 = new ColumnConstraints(); c2.setPercentWidth(50);
	thongTinNho.getColumnConstraints().addAll(c1, c2);
	thongTinNho.add(new Label("Mã CTPDB:"), 0, 0);
	thongTinNho.add(new Label("Mã HD:"), 1, 0);
	thongTinNho.add(new Label("SDT khách hàng:"), 0, 1);
	thongTinNho.add(new Label("Tên khách hàng:"), 1, 1);
	thongTinNho.add(new Label("Giờ vào:"), 0, 2);

	VBox hopDen = new VBox(0);
	hopDen.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1.5; -fx-border-radius: 6; -fx-background-radius: 6;");
	hopDen.setPadding(new Insets(8));
	VBox.setVgrow(hopDen, Priority.ALWAYS);

	HBox dongTieuDe = taoDong("STT", "Tên món", "Đơn giá", "Số lượng", "Thành tiền", "Hành động", true);

	VBox danhSachDong = new VBox(0);
	danhSachDong.getChildren().addAll(
		taoDongDuLieu(1, "Cơm bò", "45,000 VND", "2", "90,000 VND"),
		taoDongDuLieu(2, "Cơm bò", "45,000 VND", "2", "90,000 VND"),
		taoDongDuLieu(3, "Cơm bò", "45,000 VND", "2", "90,000 VND"),
		taoDongDuLieu(4, "Cơm bò", "45,000 VND", "2", "90,000 VND")
	);

	hopDen.getChildren().addAll(dongTieuDe, danhSachDong);

	HBox thanhMaGiamGia = new HBox(10);
	thanhMaGiamGia.setPadding(new Insets(12, 0, 0, 0));
	Label lblMa = new Label("Mã giảm giá:");
	TextField txtMa = new TextField();
	txtMa.setPrefWidth(180);
	ButtonSample nutKiemTra = new ButtonSample("Kiểm tra", 45, 16, 1);
	ButtonSample nutGoiMon = new ButtonSample("Gọi thêm món", 45, 16, 1);
	Region dayPhai = new Region();
	HBox.setHgrow(dayPhai, Priority.ALWAYS);
	thanhMaGiamGia.getChildren().addAll(lblMa, txtMa, nutKiemTra, dayPhai, nutGoiMon);

	GridPane tongKet = new GridPane();
	tongKet.setHgap(40);
	tongKet.setVgap(6);
	tongKet.setPadding(new Insets(8, 0, 0, 0));
	tongKet.add(new Label("Chiết khấu:"), 0, 0);
	tongKet.add(new Label("Thuế VAT:"), 0, 1);
	tongKet.add(new Label("Tạm tính:"), 0, 2);
	tongKet.add(new Label("Tiền đặt cọc trước:"), 1, 0);
	tongKet.add(new Label("Tổng cộng:"), 1, 1);

	khungPhai.getChildren().addAll(tieuDe, thongTinNho, hopDen, thanhMaGiamGia, tongKet);
	phai.getChildren().add(khungPhai);
	return phai;
	}

	// headers
	private HBox taoDong(String c1, String c2, String c3, String c4, String c5, String c6, boolean laTieuDe) {
		HBox dong = new HBox();
		dong.setAlignment(Pos.CENTER_LEFT);
		dong.setStyle("-fx-background-color: " + (laTieuDe ? "#EFEFEF" : "white") + "; -fx-border-color: black transparent transparent transparent; -fx-border-width: 1 0 0 0;");
		dong.setPadding(new Insets(8, 10, 8, 10));

		Region o1 = oChu(c1, 50, Pos.CENTER_LEFT, laTieuDe);
		Region o2 = oChu(c2, 300, Pos.CENTER_LEFT, laTieuDe);
		Region o3 = oChu(c3, 130, Pos.CENTER_LEFT, laTieuDe);
		Region o4 = oChu(c4, 110, Pos.CENTER_LEFT, laTieuDe);
		Region o5 = oChu(c5, 140, Pos.CENTER_LEFT, laTieuDe);
		Region o6 = oChu(c6, 140, Pos.CENTER, laTieuDe);
		HBox.setHgrow(o2, Priority.ALWAYS);

		dong.getChildren().addAll(o1, o2, o3, o4, o5, o6);
		return dong;
	}

	// + -
	private HBox taoDongDuLieu(int stt, String ten, String donGia, String soLuong, String thanhTien) {
		HBox dong = new HBox();
		dong.setAlignment(Pos.CENTER_LEFT);
		dong.setPadding(new Insets(8, 10, 8, 10));
		dong.setStyle("-fx-border-color: black transparent transparent transparent; -fx-border-width: 1 0 0 0;");

		Region o1 = oChu(String.valueOf(stt), 50, Pos.CENTER_LEFT, false);
		Region o2 = oChu(ten, 300, Pos.CENTER_LEFT, false);
		Region o3 = oChu(donGia, 130, Pos.CENTER_LEFT, false);
		Region o4 = oChu(soLuong, 110, Pos.CENTER_LEFT, false);
		Region o5 = oChu(thanhTien, 140, Pos.CENTER_LEFT, false);

		HBox hopHanhDong = new HBox(10);
		hopHanhDong.setAlignment(Pos.CENTER);
		Button nutTru = taoNutTrongSot("−");
		Label soLuongHienThi = new Label("0");
		soLuongHienThi.setStyle("-fx-font-size: 14px; -fx-text-fill: #000000;");
		Button nutCong = taoNutTrongSot("+");
		hopHanhDong.getChildren().addAll(nutTru, soLuongHienThi, nutCong);
		Region o6 = oNode(hopHanhDong, 140, Pos.CENTER);

		HBox.setHgrow(o2, Priority.ALWAYS);
		dong.getChildren().addAll(o1, o2, o3, o4, o5, o6);
		return dong;
	}

	private Region oChu(String text, double width, Pos align, boolean dam) {
		Label nhan = new Label(text);
		nhan.setStyle("-fx-font-size: 14px; -fx-text-fill: #000000;" + (dam ? "-fx-font-weight: bold;" : ""));
		return oNode(nhan, width, align);
	}

	private Region oNode(javafx.scene.Node node, double width, Pos align) {
		StackPane p = new StackPane(node);
		p.setAlignment(align);
		p.setMinWidth(width);
		p.setPrefWidth(width);
		return p;
	}

	private Button taoNutTrongSot(String text) {
		Button b = new Button(text);
		b.setFocusTraversable(false);
		b.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-font-size: 16px; -fx-text-fill: #000000;");
		return b;
	}
}

