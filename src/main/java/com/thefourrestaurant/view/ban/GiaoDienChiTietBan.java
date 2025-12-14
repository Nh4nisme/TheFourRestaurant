package com.thefourrestaurant.view.ban;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javafx.scene.paint.Color;
import com.thefourrestaurant.DAO.ChiTietPDBDAO;
import com.thefourrestaurant.controller.ThanhToanController;
import com.thefourrestaurant.model.Ban;
import com.thefourrestaurant.model.ChiTietPDB;
import com.thefourrestaurant.model.PhieuDatBan;
import com.thefourrestaurant.view.monan.GiaoDienGoiMon;
import com.thefourrestaurant.view.components.ButtonSample2;
import com.thefourrestaurant.view.components.ButtonSample2.Variant;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
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
	private Label lblConLai;

	public GiaoDienChiTietBan(StackPane mainContent, Ban ban, PhieuDatBan pdb) {
		this.mainContent = mainContent;
		this.ban = ban;
		this.pdb = pdb;

		setStyle("-fx-background-color: #F5F5F5;");
		setTop(buildHeader());
		setCenter(buildCenter());
		setBottom(buildFooter());

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
		nutQuayLai.setOnAction(e -> {
		    Object ud = lblConLai.getUserData();
		    if (ud instanceof Timeline tl) {
		        tl.stop();
		    }
		    mainContent.getChildren().setAll(new GiaoDienDatBan(mainContent));
		});

		Region dayCach = new Region();
		HBox.setHgrow(dayCach, Priority.ALWAYS);
		Button nutTinhTien = new ButtonSample2("Tính tiền", Variant.YELLOW, 120);

		nutTinhTien.setOnAction(e -> {
		    // stop countdown nếu có
		    Object ud = lblConLai.getUserData();
		    if (ud instanceof Timeline tl) tl.stop();

		    new ThanhToanController().moManThanhToan(pdb);
		});

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
		VBox trai = new VBox(16);
		trai.setPrefWidth(380);

		// Tiêu đề cột "Thông tin bàn"
		Label tieuDeTtb = new Label("Thông tin bàn");
		tieuDeTtb.setMaxWidth(Double.MAX_VALUE);
		tieuDeTtb.setAlignment(Pos.CENTER);
		tieuDeTtb.setStyle("-fx-text-fill: #DDB248; -fx-font-size: 18px; -fx-font-weight: bold;");

		// Dữ liệu bàn
		String[] thongTinBan = new String[] { ban.getTrangThai(), ban.getLoaiBan().getTenLoaiBan(),
				String.valueOf(ban.getLoaiBan().getSoChoNgoi()),
				ban.getLoaiBan().getMoTa() != null ? ban.getLoaiBan().getMoTa() : "",
				ban.getLoaiBan().getGiaTien().toPlainString() };

		VBox theTtb = buildInfoCardWithData(new String[] { "Trạng Thái:", "Loại bàn:", "Số người:" }, thongTinBan);

		// Thêm tất cả vào VBox cột trái
		trai.getChildren().addAll(tieuDeTtb, theTtb);

		return trai;
	}

	/**
	 * Xây dựng thẻ thông tin nhãn + giá trị
	 */
	private VBox buildInfoCardWithData(String[] nhanText, String[] giaTri) {
		VBox card = new VBox(8);
		card.setPadding(new Insets(12));
		card.setStyle(
				"-fx-background-color: white; -fx-border-color: #000000; -fx-border-radius: 10; -fx-background-radius: 10;");

		for (int i = 0; i < nhanText.length; i++) {
			HBox row = new HBox(8);

			Label lblNhan = new Label(nhanText[i]);
			lblNhan.setStyle("-fx-font-size: 16px; -fx-text-fill: #DDB248; -fx-font-weight: bold;");

			Label lblGiaTri = new Label(giaTri[i]);
			lblGiaTri.setStyle("-fx-font-size: 16px; -fx-text-fill: #000000;");

			row.getChildren().addAll(lblNhan, lblGiaTri);
			card.getChildren().add(row);
		}

		return card;
	}

	private VBox buildRightInvoice() {
		VBox phai = new VBox(10);
		phai.setPadding(new Insets(0, 6, 0, 6));

		VBox khungPhai = new VBox(12);
		khungPhai.setPadding(new Insets(16));
		khungPhai.setStyle(
				"-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");
		VBox.setVgrow(khungPhai, Priority.ALWAYS);

		Label tieuDe = new Label("Hóa đơn tạm tính");
		tieuDe.setMaxWidth(Double.MAX_VALUE);
		tieuDe.setAlignment(Pos.CENTER);
		tieuDe.setStyle("-fx-text-fill: #DDB248; -fx-font-size: 20px; -fx-font-weight: bold;");

		// Thông tin khách hàng
		HBox thongTinNho = new HBox(40);
		thongTinNho.setPadding(new Insets(8));
		thongTinNho.setAlignment(Pos.CENTER_LEFT);

		Label n1 = new Label("Mã Phiếu Đặt Bàn:"), n2 = new Label("SDT khách hàng:"), n3 = new Label("Giờ vào:"),
				n4 = new Label("Tên khách hàng:"), n5 = new Label("Còn lại:");

		Label lblMaPDB = new Label(), lblSDT = new Label(), lblGioVao = new Label(), lblHoTen = new Label();
		
		lblConLai = new Label();
		startCountdownChiTiet(lblConLai);

		Label[] headers = { n1, n2, n3, n4, n5 };
		for (Label lbl : headers) {
			lbl.setStyle("-fx-text-fill: #DDB248; -fx-font-weight: bold;");
			lbl.setMinWidth(120);
			lbl.setPrefWidth(120);
		}

		VBox vboxTrai = new VBox(6, new HBox(6, n1, lblMaPDB), new HBox(6, n3, lblGioVao), new HBox(6, n5, lblConLai) // NEW
		);

		VBox vboxPhai = new VBox(6, new HBox(6, n2, lblSDT), new HBox(6, n4, lblHoTen));
		thongTinNho.getChildren().addAll(vboxTrai, vboxPhai);

		// Hóa đơn chi tiết món
		VBox hopDen = new VBox(0);
		hopDen.setStyle(
				"-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1.5; -fx-border-radius: 6; -fx-background-radius: 6;");
		hopDen.setPadding(new Insets(8));
		VBox.setVgrow(hopDen, Priority.ALWAYS);

		HBox dongTieuDe = buildRow("STT", "Tên món", "Đơn giá", "Số lượng", "Thành tiền", "Ghi chú", true);
		VBox danhSachDong = new VBox(0);

		double tongTien = 0;
		if (pdb != null) {
			// Load chi tiết từ DAO
			ChiTietPDBDAO ctDAO = new ChiTietPDBDAO();
			List<ChiTietPDB> chiTietList = ctDAO.layTheoPhieu(pdb.getMaPDB());

			int stt = 1;
			for (ChiTietPDB ct : chiTietList) {
				String tenMon = ct.getMonAn().getTenMon();
				double thanhTienSo = ct.getDonGia() * ct.getSoLuong();
				tongTien += thanhTienSo;
				String donGia = String.format("%,.0f VND", ct.getDonGia());
				String soLuong = String.valueOf(ct.getSoLuong());
				String thanhTien = String.format("%,.0f VND", thanhTienSo);
				String ghiChu = ct.getGhiChu() != null ? ct.getGhiChu() : "";
				danhSachDong.getChildren().add(buildDataRow(stt++, tenMon, donGia, soLuong, thanhTien, ghiChu));
			}

			lblMaPDB.setText(pdb.getMaPDB());
			if (pdb.getKhachHang() != null) {
			    lblSDT.setText(pdb.getKhachHang().getSoDT());
			    lblHoTen.setText(pdb.getKhachHang().getHoTen());
			}

			if (pdb.getNgayDat() != null) {
			    lblGioVao.setText(pdb.getNgayDat().format(
			        DateTimeFormatter.ofPattern("HH:mm")
			    ));
			}
		}

		hopDen.getChildren().addAll(dongTieuDe, danhSachDong);

		// Thêm tổng tiền
		HBox tongTienBox = new HBox();
		tongTienBox.setAlignment(Pos.CENTER_RIGHT);
		tongTienBox.setPadding(new Insets(8, 10, 8, 10));
		Label lblTong = new Label("Tổng tiền: " + String.format("%,.0f VND", tongTien));
		lblTong.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #DDB248;");
		tongTienBox.getChildren().add(lblTong);
		hopDen.getChildren().add(tongTienBox);

		// Nút chức năng
		HBox thanhChucNang = new HBox();
		thanhChucNang.setAlignment(Pos.CENTER_RIGHT);
		thanhChucNang.setPadding(new Insets(12, 0, 0, 0));
		Button nutGoiMon = new ButtonSample2("Gọi thêm món", Variant.YELLOW, 120);
		nutGoiMon.setOnAction(e -> mainContent.getChildren().setAll(new GiaoDienGoiMon(mainContent, ban, pdb)));
		thanhChucNang.getChildren().add(nutGoiMon);

		khungPhai.getChildren().addAll(tieuDe, thongTinNho, hopDen, thanhChucNang);
		phai.getChildren().add(khungPhai);
		return phai;
	}

	// headers
	private HBox buildRow(String c1, String c2, String c3, String c4, String c5, String c6, boolean laTieuDe) {
		HBox dong = new HBox();
		dong.setAlignment(Pos.CENTER_LEFT);
		dong.setStyle("-fx-background-color: " + (laTieuDe ? "#EFEFEF" : "white")
				+ "; -fx-border-color: black transparent transparent transparent; -fx-border-width: 1 0 0 0;");
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
	private HBox buildDataRow(int stt, String ten, String donGia, String soLuong, String thanhTien, String ghiChu) {
		HBox dong = new HBox();
		dong.setAlignment(Pos.CENTER_LEFT);
		dong.setPadding(new Insets(8, 10, 8, 10));
		dong.setStyle("-fx-border-color: black transparent transparent transparent; -fx-border-width: 1 0 0 0;");

		Region o1 = cellText(String.valueOf(stt), 50, Pos.CENTER_LEFT, false);
		Region o2 = cellText(ten, 300, Pos.CENTER_LEFT, false);
		Region o3 = cellText(donGia, 130, Pos.CENTER_LEFT, false);
		Region o4 = cellText(soLuong, 110, Pos.CENTER_LEFT, false);
		Region o5 = cellText(thanhTien, 140, Pos.CENTER_LEFT, false);

		Label lblGhiChu = new Label(ghiChu);
		lblGhiChu.setStyle("-fx-font-size: 14px; -fx-text-fill: #000000;");
		Region o6 = cellNode(lblGhiChu, 140, Pos.CENTER);

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

	private void startCountdownChiTiet(Label lblConLai) {
		if (pdb == null || pdb.getNgayDat() == null) {
			lblConLai.setText("");
			return;
		}

		LocalDateTime end = pdb.getNgayDat().plusHours(2);

		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {

			long seconds = java.time.Duration.between(LocalDateTime.now(), end).getSeconds();

			if (seconds <= 0) {
				lblConLai.setText("Hết giờ");
				lblConLai.setTextFill(Color.RED);
				((Timeline) e.getSource()).stop();
				return;
			}

			long h = seconds / 3600;
			long m = (seconds % 3600) / 60;
			long s = seconds % 60;

			lblConLai.setText(String.format("%02d:%02d:%02d", h, m, s));

			lblConLai.setTextFill(seconds <= 900 ? Color.RED : Color.BLACK);
		}));

		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();

		lblConLai.setUserData(timeline);
	}

}
