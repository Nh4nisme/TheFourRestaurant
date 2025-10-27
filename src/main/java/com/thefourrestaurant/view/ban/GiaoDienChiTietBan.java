package com.thefourrestaurant.view.ban;

import com.thefourrestaurant.DAO.PhieuDatBanDAO;
import com.thefourrestaurant.model.Ban;
import com.thefourrestaurant.model.ChiTietPDB;
import com.thefourrestaurant.model.PhieuDatBan;
import com.thefourrestaurant.view.GiaoDienGoiMon;
import com.thefourrestaurant.view.components.ButtonSample2;
import com.thefourrestaurant.view.components.ButtonSample2.Variant;
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

public class GiaoDienChiTietBan extends BorderPane {
	
	
	private StackPane mainContent;
	private Ban ban;
	private PhieuDatBan pdb;
	private final PhieuDatBanDAO pdbDAO = new PhieuDatBanDAO();
	
	public GiaoDienChiTietBan(StackPane mainContent, Ban ban) {
        this.mainContent = mainContent;
        this.ban = ban;
        pdb = pdbDAO.layPhieuDangHoatDongTheoBan(ban.getMaBan());

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
	    VBox trai = new VBox(16);
	    trai.setPrefWidth(380);

	    // Tiêu đề cột "Thông tin bàn"
	    Label tieuDeTtb = new Label("Thông tin bàn");
	    tieuDeTtb.setMaxWidth(Double.MAX_VALUE);
	    tieuDeTtb.setAlignment(Pos.CENTER);
	    tieuDeTtb.setStyle("-fx-text-fill: #DDB248; -fx-font-size: 18px; -fx-font-weight: bold;");

	    // Dữ liệu bàn
	    String[] thongTinBan = new String[]{
	        ban.getTrangThai(), 
	        ban.getLoaiBan().getTenLoaiBan(), 
	        String.valueOf(ban.getLoaiBan().getSoNguoi())
	    };

	    VBox theTtb = buildInfoCardWithData(
	        new String[]{"Trạng Thái:", "Loại bàn:", "Số người:"}, thongTinBan
	    );

	    // Tiêu đề cột "Phiếu đặt bàn"
	    Label tieuDePdb = new Label("Phiếu đặt bàn");
	    tieuDePdb.setMaxWidth(Double.MAX_VALUE);
	    tieuDePdb.setAlignment(Pos.CENTER);
	    tieuDePdb.setStyle("-fx-text-fill: #DDB248; -fx-font-size: 18px; -fx-font-weight: bold;");

	    // Dữ liệu phiếu đặt bàn
	    String[] thongTinPdb = new String[]{
	        pdb != null && pdb.getKhachHang() != null ? pdb.getKhachHang().getHoTen() : "",
	        pdb != null && pdb.getKhachHang() != null ? pdb.getKhachHang().getSoDT() : "",
	        pdb != null && pdb.getNgayDat() != null ? pdb.getNgayDat().toString() : ""
	    };

	    VBox thePdb = buildInfoCardWithData(
	        new String[]{"Tên khách:", "Số điện thoại:", "Giờ nhận bàn:"}, thongTinPdb
	    );

	    // Thêm tất cả vào VBox cột trái
	    trai.getChildren().addAll(tieuDeTtb, theTtb, tieuDePdb, thePdb);

	    return trai;
	}

	/**
	 * Xây dựng thẻ thông tin nhãn + giá trị
	 */
	private VBox buildInfoCardWithData(String[] nhanText, String[] giaTri) {
	    VBox card = new VBox(8);
	    card.setPadding(new Insets(12));
	    card.setStyle("-fx-background-color: white; -fx-border-color: #000000; -fx-border-radius: 10; -fx-background-radius: 10;");

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

		HBox dongTieuDe = buildRow("STT", "Tên món", "Đơn giá", "Số lượng", "Thành tiền", "Ghi chú", true);

	VBox danhSachDong = new VBox(0);
	if (pdb != null && pdb.getChiTietPDB() != null) {
	    int stt = 1;
	    for (ChiTietPDB ct : pdb.getChiTietPDB()) {
	        String tenMon = ct.getMonAn().getTenMon();
	        String donGia = String.format("%,.0f VND", ct.getDonGia());
	        String soLuong = String.valueOf(ct.getSoLuong());
	        String thanhTien = String.format("%,.0f VND", ct.getDonGia() * ct.getSoLuong());
	        String ghiChu = ct.getGhiChu() != null ? ct.getGhiChu() : "";
	        danhSachDong.getChildren().add(buildDataRow(stt++, tenMon, donGia, soLuong, thanhTien, ghiChu));
	    }
	}

		hopDen.getChildren().addAll(dongTieuDe, danhSachDong);

		HBox thanhChucNang = new HBox();
		thanhChucNang.setAlignment(Pos.CENTER_RIGHT);
		thanhChucNang.setPadding(new Insets(12, 0, 0, 0));

		Button nutGoiMon = new ButtonSample2("Gọi thêm món", Variant.YELLOW, 120);
		nutGoiMon.setOnAction(e -> {
		    mainContent.getChildren().setAll(new GiaoDienGoiMon(mainContent, ban, pdb));
		});

		thanhChucNang.getChildren().add(nutGoiMon);

		khungPhai.getChildren().addAll(tieuDe, thongTinNho, hopDen, thanhChucNang);
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

    
}

