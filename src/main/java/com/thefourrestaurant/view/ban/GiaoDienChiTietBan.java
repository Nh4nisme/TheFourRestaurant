package com.thefourrestaurant.view.ban;

import com.thefourrestaurant.DAO.PhieuDatBanDAO;
import com.thefourrestaurant.model.Ban;
import com.thefourrestaurant.model.PhieuDatBan;
import com.thefourrestaurant.view.GiaoDienGoiMon;
import com.thefourrestaurant.view.ThanhToan;
import com.thefourrestaurant.view.components.ButtonSample2;
import com.thefourrestaurant.view.components.ButtonSample2.Variant;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class GiaoDienChiTietBan extends BorderPane {
	
	private StackPane mainContent;
	private Ban ban;
	private PhieuDatBan pdb;
	private final PhieuDatBanDAO pdbDAO = new PhieuDatBanDAO();
	
	public GiaoDienChiTietBan(StackPane mainContent, Ban ban) {
        this.mainContent = mainContent;
        this.ban = ban;

        setStyle("-fx-background-color: #F5F5F5;");
        setTop(buildHeader());
        setCenter(buildCenter());
        setBottom(buildFooter());

        pdb = pdbDAO.layPhieuDangHoatDongTheoBan(ban.getMaBan());
        if (pdb == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Bàn này hiện chưa có phiếu hoạt động.");
            alert.showAndWait();
        }
    }

	private HBox buildHeader() {
		HBox header = new HBox();
		header.setAlignment(Pos.CENTER);
		header.setPadding(new Insets(8, 20, 8, 20));
		header.setStyle("-fx-background-color: #1E424D ;");

		Label tieuDe = new Label("Chi tiết bàn");
		tieuDe.setStyle("-fx-text-fill: #DDB248; -fx-font-size: 22px; -fx-font-weight: bold;");
		header.getChildren().add(tieuDe);
		return header;
	}

	private HBox buildFooter() {
		HBox footer = new HBox();
		footer.setAlignment(Pos.CENTER);
		footer.setPadding(new Insets(12, 20, 12, 20));
		footer.setStyle("-fx-background-color: #1E424D ;");

		Button nutQuayLai = new ButtonSample2("Quay lại", Variant.YELLOW, 120);
		nutQuayLai.setOnAction(e -> mainContent.getChildren().setAll(new GiaoDienDatBan(mainContent)));

		Region dayCach = new Region();
		HBox.setHgrow(dayCach, Priority.ALWAYS);
		Button nutTinhTien = new ButtonSample2("Tính tiền", Variant.YELLOW, 120);
//		nutQuayLai.setOnAction(e -> mainContent.getChildren().setAll(new ThanhToan()));
		footer.getChildren().addAll(nutQuayLai, dayCach, nutTinhTien);
		return footer;
	}

	private HBox buildCenter() {
		HBox trungTam = new HBox(28);
		trungTam.setPadding(new Insets(20));

		VBox cotTrai = buildLeftColumn();
		VBox cotPhai = buildRightInvoice();

		HBox.setHgrow(cotPhai, Priority.ALWAYS);
		cotPhai.setMaxWidth(Double.MAX_VALUE);

		trungTam.getChildren().addAll(cotTrai, cotPhai);
		return trungTam;
	}

	private VBox buildLeftColumn() {
		// cột trái
		VBox trai = new VBox(16);
		trai.setPrefWidth(380);

		Label tieuDeTtb = new Label("Thông tin bàn");
		tieuDeTtb.setMaxWidth(Double.MAX_VALUE);
		tieuDeTtb.setAlignment(Pos.CENTER);
		tieuDeTtb.setStyle("-fx-text-fill: #DDB248; -fx-font-size: 18px; -fx-font-weight: bold;");
	VBox theTtb = buildInfoCard(new String[]{"Trạng Thái:", "Loại bàn:", "Số người:"}, true);

		Label tieuDePdb = new Label("Phiếu đặt bàn");
		tieuDePdb.setMaxWidth(Double.MAX_VALUE);
		tieuDePdb.setAlignment(Pos.CENTER);
		tieuDePdb.setStyle("-fx-text-fill: #DDB248; -fx-font-size: 18px; -fx-font-weight: bold;");
	VBox thePdb = buildInfoCard(new String[]{"Tên khách:", "Số điện thoại:", "Giờ nhận bàn:"}, true);

		trai.getChildren().addAll(tieuDeTtb, theTtb, tieuDePdb, thePdb);
		return trai;
	}

	private VBox buildInfoCard(String[] dong) {
		return buildInfoCard(dong, false);
	}

	private VBox buildInfoCard(String[] dong, boolean dam) {
		VBox the = new VBox(8);
		the.setPadding(new Insets(12));
		the.setStyle("-fx-background-color: white; -fx-border-color: #000000; -fx-border-radius: 10; -fx-background-radius: 10;");

		for (String s : dong) {
			Label nhan = new Label(s);
			nhan.setStyle("-fx-font-size: 16px; -fx-text-fill: #DDB248;" + (dam ? "-fx-font-weight: bold;" : ""));
			the.getChildren().add(nhan);
		}
		return the;
	}

	private VBox buildRightInvoice() {
		// cột phải.
		VBox phai = new VBox(10);
		phai.setPadding(new Insets(0, 6, 0, 6));

		VBox khungPhai = new VBox(12);
		khungPhai.setPadding(new Insets(16));
		khungPhai.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");
		VBox.setVgrow(khungPhai, Priority.ALWAYS);

		Label tieuDe = new Label("Hóa đơn tạm tính");
		tieuDe.setMaxWidth(Double.MAX_VALUE);
		tieuDe.setAlignment(Pos.CENTER);
		tieuDe.setStyle("-fx-text-fill: #DDB248; -fx-font-size: 20px; -fx-font-weight: bold;");

		GridPane thongTinNho = new GridPane();
		thongTinNho.setHgap(50);
		thongTinNho.setVgap(6);
		ColumnConstraints c1 = new ColumnConstraints(); c1.setPercentWidth(50);
		ColumnConstraints c2 = new ColumnConstraints(); c2.setPercentWidth(50);
		thongTinNho.getColumnConstraints().addAll(c1, c2);

	Label n1 = new Label("Mã CTPDB:"); n1.setStyle("-fx-text-fill: #DDB248; -fx-font-weight: bold;");
	Label n2 = new Label("Mã HD:"); n2.setStyle("-fx-text-fill: #DDB248; -fx-font-weight: bold;");
	Label n3 = new Label("SDT khách hàng:"); n3.setStyle("-fx-text-fill: #DDB248; -fx-font-weight: bold;");
	Label n4 = new Label("Tên khách hàng:"); n4.setStyle("-fx-text-fill: #DDB248; -fx-font-weight: bold;");
	Label n5 = new Label("Giờ vào:"); n5.setStyle("-fx-text-fill: #DDB248; -fx-font-weight: bold;");

		thongTinNho.add(n1, 0, 0);
		thongTinNho.add(n2, 1, 0);
		thongTinNho.add(n3, 0, 1);
		thongTinNho.add(n4, 1, 1);
		thongTinNho.add(n5, 0, 2);

		VBox hopDen = new VBox(0);
		hopDen.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1.5; -fx-border-radius: 6; -fx-background-radius: 6;");
		hopDen.setPadding(new Insets(8));
		VBox.setVgrow(hopDen, Priority.ALWAYS);

	HBox dongTieuDe = buildRow("STT", "Tên món", "Đơn giá", "Số lượng", "Thành tiền", "Hành động", true);

	VBox danhSachDong = new VBox(0);
	danhSachDong.getChildren().addAll(
		buildDataRow(1, "Cơm bò", "45,000 VND", "2", "90,000 VND"),
		buildDataRow(2, "Cơm bò", "45,000 VND", "2", "90,000 VND"),
		buildDataRow(3, "Cơm bò", "45,000 VND", "2", "90,000 VND"),
		buildDataRow(4, "Cơm bò", "45,000 VND", "2", "90,000 VND")
	);

		hopDen.getChildren().addAll(dongTieuDe, danhSachDong);

		HBox thanhMaGiamGia = new HBox(12);
		thanhMaGiamGia.setAlignment(Pos.CENTER_LEFT);
		thanhMaGiamGia.setPadding(new Insets(12, 0, 0, 0));
	Label lblMa = new Label("Mã giảm giá:");
	lblMa.setStyle("-fx-text-fill: #DDB248; -fx-font-weight: bold;");
		TextField txtMa = new TextField();
		txtMa.setPrefWidth(220);
		txtMa.setPrefHeight(40);
		txtMa.setStyle("-fx-background-color: white; -fx-border-color: #C9C9C9; -fx-background-radius: 10; -fx-border-radius: 10; -fx-padding: 0 12 0 12;");
	Button nutKiemTra = new ButtonSample2("Kiểm tra", Variant.YELLOW, 120);
	Button nutGoiMon = new ButtonSample2("Gọi thêm món", Variant.YELLOW, 120);
	nutGoiMon.setOnAction(e -> {
	    mainContent.getChildren().setAll(new GiaoDienGoiMon(mainContent, ban));
	});
		Region dayPhai = new Region();
		HBox.setHgrow(dayPhai, Priority.ALWAYS);
		thanhMaGiamGia.getChildren().addAll(lblMa, txtMa, nutKiemTra, dayPhai, nutGoiMon);

		GridPane tongKet = new GridPane();
		tongKet.setHgap(60);
		tongKet.setVgap(10);
		tongKet.setPadding(new Insets(8, 0, 0, 0));
		ColumnConstraints tk1 = new ColumnConstraints(); tk1.setPercentWidth(50);
		ColumnConstraints tk2 = new ColumnConstraints(); tk2.setPercentWidth(50);
		tongKet.getColumnConstraints().addAll(tk1, tk2);
	Label l1 = new Label("Chiết khấu:"); l1.setStyle("-fx-text-fill: #DDB248; -fx-font-weight: bold;");
	Label l2 = new Label("Thuế VAT:"); l2.setStyle("-fx-text-fill: #DDB248; -fx-font-weight: bold;");
	Label l3 = new Label("Tạm tính:"); l3.setStyle("-fx-text-fill: #DDB248; -fx-font-weight: bold;");
	Label r1 = new Label("Tiền đặt cọc trước:"); r1.setStyle("-fx-text-fill: #DDB248; -fx-font-weight: bold;");
	Label r2 = new Label("Tổng cộng:"); r2.setStyle("-fx-text-fill: #DDB248; -fx-font-weight: bold;");
		tongKet.add(l1, 0, 0);
		tongKet.add(l2, 0, 1);
		tongKet.add(l3, 0, 2);
		tongKet.add(r1, 1, 0);
		tongKet.add(r2, 1, 1);

		khungPhai.getChildren().addAll(tieuDe, thongTinNho, hopDen, thanhMaGiamGia, tongKet);
		phai.getChildren().add(khungPhai);
		return phai;
	}

	// headers
	private HBox buildRow(String c1, String c2, String c3, String c4, String c5, String c6, boolean laTieuDe) {
		HBox dong = new HBox();
		dong.setAlignment(Pos.CENTER_LEFT);
		dong.setStyle("-fx-background-color: " + (laTieuDe ? "#EFEFEF" : "white") + "; -fx-border-color: black transparent transparent transparent; -fx-border-width: 1 0 0 0;");
		dong.setPadding(new Insets(8, 10, 8, 10));

		Region o1 = cellText(c1, 50, Pos.CENTER_LEFT, false);
		Region o2 = cellText(c2, 300, Pos.CENTER_LEFT, false);
		Region o3 = cellText(c3, 130, Pos.CENTER_LEFT, false);
		Region o4 = cellText(c4, 110, Pos.CENTER_LEFT, false);
		Region o5 = cellText(c5, 140, Pos.CENTER_LEFT, false);
		Region o6 = cellText(c6, 140, Pos.CENTER, false);
		HBox.setHgrow(o2, Priority.ALWAYS);

		dong.getChildren().addAll(o1, o2, o3, o4, o5, o6);
		return dong;
	}

	// + -
	private HBox buildDataRow(int stt, String ten, String donGia, String soLuong, String thanhTien) {
		HBox dong = new HBox();
		dong.setAlignment(Pos.CENTER_LEFT);
		dong.setPadding(new Insets(8, 10, 8, 10));
		dong.setStyle("-fx-border-color: black transparent transparent transparent; -fx-border-width: 1 0 0 0;");

		Region o1 = cellText(String.valueOf(stt), 50, Pos.CENTER_LEFT, false);
		Region o2 = cellText(ten, 300, Pos.CENTER_LEFT, false);
		Region o3 = cellText(donGia, 130, Pos.CENTER_LEFT, false);
		Region o4 = cellText(soLuong, 110, Pos.CENTER_LEFT, false);
		Region o5 = cellText(thanhTien, 140, Pos.CENTER_LEFT, false);

		HBox hopHanhDong = new HBox(10);
		hopHanhDong.setAlignment(Pos.CENTER);
		Button nutTru = transparentIconButton("−");
		Label soLuongHienThi = new Label("0");
		soLuongHienThi.setStyle("-fx-font-size: 14px; -fx-text-fill: #000000;");
		Button nutCong = transparentIconButton("+");
		hopHanhDong.getChildren().addAll(nutTru, soLuongHienThi, nutCong);
		Region o6 = cellNode(hopHanhDong, 140, Pos.CENTER);

		HBox.setHgrow(o2, Priority.ALWAYS);
		dong.getChildren().addAll(o1, o2, o3, o4, o5, o6);
		return dong;
	}

	private Region cellText(String text, double width, Pos align, boolean dam) {
		Label nhan = new Label(text);
		nhan.setStyle("-fx-font-size: 14px; -fx-text-fill: #000000;" + (dam ? "-fx-font-weight: bold;" : ""));
		return cellNode(nhan, width, align);
	}

	private Region cellNode(javafx.scene.Node node, double width, Pos align) {
		StackPane p = new StackPane(node);
		p.setAlignment(align);
		p.setMinWidth(width);
		p.setPrefWidth(width);
		return p;
	}

	private Button transparentIconButton(String text) {
		Button b = new Button(text);
		b.setFocusTraversable(false);
		b.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-font-size: 16px; -fx-text-fill: #000000;");
		return b;
	}

    
}

