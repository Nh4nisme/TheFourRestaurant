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

	// mau dong bo voi sidebar.
	private static final String MAU_THANH = "#1E424D"; // mau xanh cua sidebar.
	private static final String MAU_VANG = "#DDB248"; // mau vang tieu de.
	private static final String MAU_VIEN_DEN = "#000000"; // mau vien den.

	public GiaoDienChiTietBan() {
		// nen tong.
		setStyle("-fx-background-color: #F5F5F5;");

		// header.
		setTop(taoHeader());

		// noi dung trung tam.
		setCenter(taoNoiDung());

		// footer o day de luon nam duoi cung.
		setBottom(taoFooter());

		// rang buoc kich thuoc voi parent de footer luon cham day.
		ChangeListener<javafx.scene.Parent> ganKichThuoc = (obs, cu, moi) -> {
			if (moi instanceof Region r) {
				this.prefWidthProperty().bind(r.widthProperty());
				this.prefHeightProperty().bind(r.heightProperty());
			}
		};
		parentProperty().addListener(ganKichThuoc);
	}

	// tao header mau giong sidebar.
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

	// tao footer dong bo mau va boc sat day.
	private HBox taoFooter() {
		HBox footer = new HBox();
		footer.setAlignment(Pos.CENTER);
		footer.setPadding(new Insets(12, 20, 12, 20));
		footer.setStyle("-fx-background-color: " + MAU_THANH + ";");

		// nut trai.
		ButtonSample nutQuayLai = new ButtonSample("Quay lại", 45, 16, 1);

		// khoang day 2 nut ve 2 ben.
		Region dayCach = new Region();
		HBox.setHgrow(dayCach, Priority.ALWAYS);

		// nut phai.
		ButtonSample nutTinhTien = new ButtonSample("Tính tiền", 45, 16, 1);

		footer.getChildren().addAll(nutQuayLai, dayCach, nutTinhTien);
		return footer;
	}

	// khu vuc trung tam: trai thong tin, phai hoa don tam tinh.
	private HBox taoNoiDung() {
		HBox trungTam = new HBox(24);
		trungTam.setPadding(new Insets(18, 20, 14, 20));

		VBox cotTrai = taoCotTraiThongTin();
		VBox cotPhai = taoCotPhaiHoaDon();

		HBox.setHgrow(cotPhai, Priority.ALWAYS);
		cotPhai.setMaxWidth(Double.MAX_VALUE);

		trungTam.getChildren().addAll(cotTrai, cotPhai);
		return trungTam;
	}

	// cot trai: thong tin ban va phieu dat ban.
	private VBox taoCotTraiThongTin() {
		VBox trai = new VBox(16);
		trai.setPrefWidth(520);

		Label tieuDeTtb = new Label("Thông tin bàn");
		tieuDeTtb.setStyle("-fx-text-fill: " + MAU_VANG + "; -fx-font-size: 20px; -fx-font-weight: bold;");
		VBox theTtb = taoTheThongTin(new String[]{
				"Trạng Thái:",
				"Loại bàn:",
				"Số người:"
		});

		Label tieuDePdb = new Label("Phiếu đặt bàn");
		tieuDePdb.setStyle("-fx-text-fill: " + MAU_VANG + "; -fx-font-size: 20px; -fx-font-weight: bold;");
		VBox thePdb = taoTheThongTin(new String[]{
				"Tên khách:",
				"Số điện thoại:",
				"Giờ nhận bàn:"
		});

		trai.getChildren().addAll(tieuDeTtb, theTtb, tieuDePdb, thePdb);
		return trai;
	}

	// tao the trang thong tin.
	private VBox taoTheThongTin(String[] dong) {
		VBox the = new VBox(10);
		the.setPadding(new Insets(16));
		the.setPrefHeight(180);
		the.setStyle(
				"-fx-background-color: white;" +
					"-fx-border-color: " + MAU_VIEN_DEN + ";" +
					"-fx-border-radius: 10;" +
					"-fx-background-radius: 10;"
		);

		for (String s : dong) {
			Label nhan = new Label(s);
			nhan.setStyle("-fx-font-size: 16px; -fx-text-fill: #333333;");
			the.getChildren().add(nhan);
		}
		return the;
	}

	// cot phai: hoa don tam tinh + bang trong hop vien den.
	private VBox taoCotPhaiHoaDon() {
		VBox phai = new VBox(10);
		phai.setPadding(new Insets(0, 6, 0, 6));

		Label tieuDe = new Label("Hóa đơn tạm tính");
		tieuDe.setStyle("-fx-text-fill: " + MAU_VANG + "; -fx-font-size: 22px; -fx-font-weight: bold;");

		GridPane thongTinNho = new GridPane();
		thongTinNho.setHgap(60);
		thongTinNho.setVgap(6);
		ColumnConstraints c1 = new ColumnConstraints(); c1.setPercentWidth(50);
		ColumnConstraints c2 = new ColumnConstraints(); c2.setPercentWidth(50);
		thongTinNho.getColumnConstraints().addAll(c1, c2);
		thongTinNho.add(new Label("Mã CTPDB:"), 0, 0);
		thongTinNho.add(new Label("Mã HD:"), 1, 0);
		thongTinNho.add(new Label("SDT khách hàng:"), 0, 1);
		thongTinNho.add(new Label("Tên khách hàng:"), 1, 1);
		thongTinNho.add(new Label("Giờ vào:"), 0, 2);

		// hop vien den bao quanh bang.
		VBox hopDen = new VBox(0);
		hopDen.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");
		hopDen.setPadding(new Insets(8));
		VBox.setVgrow(hopDen, Priority.ALWAYS);

		// dong tieu de cot.
		HBox dongTieuDe = taoDong("STT", "Tên món", "Đơn giá", "Số lượng", "Thành tiền", "Hành động", true);

		// cac dong du lieu mau.
		VBox danhSachDong = new VBox(0);
		danhSachDong.getChildren().addAll(
				taoDongDuLieu(1, "Cơm bò", "45,000 VND", "2", "90,000 VND"),
				taoDongDuLieu(2, "Cơm bò", "45,000 VND", "2", "90,000 VND"),
				taoDongDuLieu(3, "Cơm bò", "45,000 VND", "2", "90,000 VND"),
				taoDongDuLieu(4, "Cơm bò", "45,000 VND", "2", "90,000 VND")
		);

		hopDen.getChildren().addAll(dongTieuDe, danhSachDong);

		// khu ma giam gia + nut.
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

		// khu tong ket trai/phai.
		GridPane tongKet = new GridPane();
		tongKet.setHgap(40);
		tongKet.setVgap(6);
		tongKet.setPadding(new Insets(10, 0, 0, 0));
		tongKet.add(new Label("Chiết khấu:"), 0, 0);
		tongKet.add(new Label("Thuế VAT:"), 0, 1);
		tongKet.add(new Label("Tạm tính:"), 0, 2);
		tongKet.add(new Label("Tiền đặt cọc trước:"), 1, 0);
		tongKet.add(new Label("Tổng cộng:"), 1, 1);

		phai.getChildren().addAll(tieuDe, thongTinNho, hopDen, thanhMaGiamGia, tongKet);
		return phai;
	}

	// tao 1 dong header hoac data voi 6 cot, ke duong ngang giong bang.
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

	// tao dong du lieu co nut + - trong suot.
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

	// tao 1 o text.
	private Region oChu(String text, double width, Pos align, boolean dam) {
		Label nhan = new Label(text);
		nhan.setStyle("-fx-font-size: 14px; -fx-text-fill: #000000;" + (dam ? "-fx-font-weight: bold;" : ""));
		return oNode(nhan, width, align);
	}

	// tao 1 o voi node bat ky.
	private Region oNode(javafx.scene.Node node, double width, Pos align) {
		StackPane p = new StackPane(node);
		p.setAlignment(align);
		p.setMinWidth(width);
		p.setPrefWidth(width);
		return p;
	}

	// nut trong suot cho hanh dong.
	private Button taoNutTrongSot(String text) {
		Button b = new Button(text);
		b.setFocusTraversable(false);
		b.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-font-size: 16px; -fx-text-fill: #000000;");
		return b;
	}
}

