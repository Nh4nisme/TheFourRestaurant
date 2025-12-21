package com.thefourrestaurant.view.ban;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thefourrestaurant.DAO.BanDAO;
import com.thefourrestaurant.DAO.PhieuDatBanDAO;
import com.thefourrestaurant.DAO.TangDAO;
import com.thefourrestaurant.connect.ConnectSQL;
import com.thefourrestaurant.controller.CountdownController;
import com.thefourrestaurant.model.Ban;
import com.thefourrestaurant.model.PhieuDatBan;
import com.thefourrestaurant.model.Tang;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class QuanLiBan extends VBox {

	private final BanDAO banDAO = new BanDAO();
	private PhieuDatBanDAO pdbDAO = new PhieuDatBanDAO();
	private final Pane khuVucBan = new Pane(); // nơi hiển thị bàn
	private final Label lblBreadcrumb = new Label();
	private PhieuDatBan phieuDangThaoTac;
	private Ban banCu;
	private ComboBox<Tang> cboSoTang;

	private StackPane mainContent;
	private String context;
	private boolean choPhepDiChuyen = false;
	private final List<Ban> dsBanDangChon = new ArrayList<>();
	private static final Map<String, Image> cacheAnhBan = new HashMap<>();
	private final Map<String, PhieuDatBan> mapDangPhucVuToanBo = new HashMap<>();
	private final Map<String, Color> mapMauChoPhieu = new HashMap<>();
	private final List<Color> danhSachMau = List.of(Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.PURPLE,
			Color.BROWN, Color.PINK);
	private int indexMau = 0;

	private String maTangHienTai = "TG000001";
	private TangDAO tangDAO = new TangDAO();

	public QuanLiBan(StackPane mainContent, String context) {
		this.mainContent = mainContent;
		this.context = context;
		this.setPrefSize(1200, 700);
		this.setSpacing(0);
		this.setAlignment(Pos.TOP_CENTER);
		this.setStyle("-fx-background-color: #F5F5F5;");

		Platform.runLater(() -> hienThiBanTheoTang(maTangHienTai));

		// === Toolbar ===
		ButtonSample btnThemBan = new ButtonSample(context.equals("QUAN_LY_BAN") ? "Thêm bàn"
				: context.equals("GOP_BAN") ? "Xác nhận gộp"
						: context.equals("CHUYEN_BAN") ? "Xác nhận chuyển" : "Thao tác",
				45, 16, 3);
		btnThemBan.setOnAction(e -> {
			switch (context) {
			case "QUAN_LY_BAN" -> moPopupTuyChinhBan(null);
			case "GOP_BAN" -> xuLyXacNhanGopBan();
			case "CHUYEN_BAN" -> xuLyXacNhanChuyenBan(banCu);
			}
		});

		// Button Lưu sơ đồ
		ButtonSample btnLuuSoDo = new ButtonSample("Lưu sơ đồ", 45, 16, 3);
		btnLuuSoDo.setOnAction(e -> {
			this.choPhepDiChuyen = false;
			Alert alert = new Alert(Alert.AlertType.INFORMATION, "Đã lưu sơ đồ! Chế độ di chuyển đã tắt.");
			alert.initOwner(this.getScene().getWindow());
			alert.showAndWait();
		});

		cboSoTang = new ComboBox<>();

		// --- ComboBox tầng ---
		List<Tang> dsTang = tangDAO.layTatCaTang();
		cboSoTang.getItems().addAll(dsTang);
		if (!dsTang.isEmpty())
			cboSoTang.setValue(dsTang.get(0));

		cboSoTang.setCellFactory(param -> new ListCell<>() {
			@Override
			protected void updateItem(Tang item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty || item == null ? null : item.getTenTang());
			}
		});
		cboSoTang.setButtonCell(new ListCell<>() {
			@Override
			protected void updateItem(Tang item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty || item == null ? null : item.getTenTang());
			}
		});
		cboSoTang.setPrefHeight(45);
		cboSoTang.setOnAction(e -> {
			Tang tangChon = cboSoTang.getValue();
			if (tangChon != null) {
				hienThiBanTheoTang(tangChon.getMaTang());
			}
		});

		// ToolBar gồm: nút Thêm bàn, ComboBox tầng, nút Lưu sơ đồ
		ToolBar toolBar = new ToolBar(btnThemBan, btnLuuSoDo, cboSoTang);
		toolBar.setStyle("-fx-background-color: #1E424D");
		toolBar.setPadding(new Insets(10, 10, 10, 10));

		VBox thanhTren = new VBox(toolBar);
		thanhTren.setSpacing(0);
		thanhTren.setAlignment(Pos.CENTER_LEFT);
		thanhTren.setPrefWidth(Double.MAX_VALUE);
		HBox.setHgrow(thanhTren, Priority.ALWAYS);

		// === Khu vực hiển thị bàn ===
		khuVucBan.setPadding(new Insets(20));
		khuVucBan.setPrefSize(1000, 600);
		khuVucBan.setStyle("-fx-background-color: #F5F5F5;");
		VBox.setVgrow(khuVucBan, Priority.ALWAYS);

		this.getChildren().addAll(thanhTren, khuVucBan);
	}

	// Hiển thị bàn theo tầng
	public void hienThiBanTheoTang(String maTang) {
		this.maTangHienTai = maTang;

		mapMauChoPhieu.clear();
		indexMau = 0;

		Map<String, PhieuDatBan> mapDatTruoc = pdbDAO.layTatCaPhieuDatTruoc();

		mapDangPhucVuToanBo.clear();
		mapDangPhucVuToanBo.putAll(pdbDAO.layTatCaPhieuDangPhucVu());

		for (Node n : khuVucBan.getChildren()) {
			if (n instanceof StackPane sp) {
				for (Node child : sp.getChildren()) {
					if (child instanceof Label lbl && lbl.getUserData() != null) {
						CountdownController.getInstance().unregisterLabel(lbl.getUserData().toString(), lbl);
					}
				}
			}
		}

		khuVucBan.getChildren().clear();

		lblBreadcrumb.setText("Trang chủ / Quản lý bàn / Tầng " + maTang.replace("TG00000", ""));

		Platform.runLater(() -> setBackgroundTheoTang(maTang));

		List<Ban> dsBan = banDAO.layTheoTang(maTang);
		if (dsBan.isEmpty())
			return;

		for (Ban b : dsBan) {
			taoBan(khuVucBan, b, mapDatTruoc);
		}
	}

	public void refresh() {
		Platform.runLater(() -> hienThiBanTheoTang(maTangHienTai));
	}

	// 🔹 Đặt background theo tầng
	private void setBackgroundTheoTang(String maTang) {
		String path = switch (maTang) {
		case "TG000001" -> "/com/thefourrestaurant/images/Tang/BG_Tang1.png";
		case "TG000002" -> "/com/thefourrestaurant/images/Tang/BG_Tang2.png";
		case "TG000003" -> "/com/thefourrestaurant/images/Tang/BG_Tang3.png";
		case "TG000004" -> "/com/thefourrestaurant/images/Tang/BG_Tang4.png";
		case "TG000005" -> "/com/thefourrestaurant/images/Tang/BG_Tang5.png";
		case "TG000006" -> "/com/thefourrestaurant/images/Tang/BG_Tang6.png";
		case "TG000007" -> "/com/thefourrestaurant/images/Tang/BG_Tang7.png";
		default -> "/com/thefourrestaurant/images/Tang/BG_Tang1.png";
		};

		try {
			Image img = new Image(getClass().getResource(path).toExternalForm());
			BackgroundSize bgs = new BackgroundSize(khuVucBan.getWidth(), khuVucBan.getHeight(), false, false, false,
					false);
			BackgroundImage bgImg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
					BackgroundPosition.CENTER, bgs);
			khuVucBan.setBackground(new Background(bgImg));
		} catch (Exception e) {
			khuVucBan.setStyle("-fx-background-color: lightgray;");
		}
	}

	void taoBan(Pane pane, Ban ban, Map<String, PhieuDatBan> mapDatTruoc) {

		// Cache ảnh bàn
		Image img;
		try {
			img = cacheAnhBan.computeIfAbsent(ban.getAnhBan(), path -> new Image(getClass().getResourceAsStream(path)));
		} catch (Exception e) {
			img = new Image(getClass().getResourceAsStream("/com/thefourrestaurant/images/Ban/Ban_8.png"));
		}

		ImageView imgBan = new ImageView(img);
		imgBan.setFitWidth(180);
		imgBan.setFitHeight(150);
		imgBan.setPreserveRatio(true);

		Label lblTenBan = new Label(ban.getTenBan());
		lblTenBan.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333;");

		StackPane khungBan = new StackPane(imgBan, lblTenBan);
		khungBan.setLayoutX(ban.getToaDoX());
		khungBan.setLayoutY(ban.getToaDoY());

		String borderStyle = getBorderStyle(ban, context, mapDatTruoc);
		khungBan.setStyle(borderStyle);

		// ✅ Hover
		final String hoverStyle = borderStyle + "-fx-effect: dropshadow(gaussian, gray, 10, 0, 0, 0);";
		khungBan.setOnMouseEntered(e -> khungBan.setStyle(hoverStyle));
		khungBan.setOnMouseExited(e -> khungBan.setStyle(borderStyle));

		final double[] offset = new double[2]; // dùng mảng thay vì biến riêng

		khungBan.setOnMousePressed(e -> {
			offset[0] = e.getSceneX() - khungBan.getLayoutX();
			offset[1] = e.getSceneY() - khungBan.getLayoutY();
		});

		khungBan.setOnMouseDragged(e -> {
			if (!choPhepDiChuyen)
				return;
			khungBan.setLayoutX(e.getSceneX() - offset[0]);
			khungBan.setLayoutY(e.getSceneY() - offset[1]);
		});

		khungBan.setOnMouseReleased(e -> {
			if (!choPhepDiChuyen)
				return;
			banDAO.capNhatToaDo(ban.getMaBan(), (int) khungBan.getLayoutX(), (int) khungBan.getLayoutY());
		});

		khungBan.setOnMouseClicked(e -> {

			if ("GOP_BAN".equals(context) || "CHUYEN_BAN".equals(context)) {

				// Chỉ cho chọn bàn TRỐNG
				if (!"Trống".equals(ban.getTrangThai())) {
					return;
				}

				if ("CHUYEN_BAN".equals(context)) {
					dsBanDangChon.clear();
					dsBanDangChon.add(ban);

					for (Node n : khuVucBan.getChildren()) {
						if (n instanceof StackPane sp && sp.getUserData() != null) {
							String maBanSP = (String) sp.getUserData();
							if (ban.getMaBan().equals(maBanSP)) {
								sp.setBackground(new Background(new BackgroundFill(Color.rgb(100, 200, 255, 0.6),
										new CornerRadii(10), Insets.EMPTY)));
							} else {
								sp.setBackground(null);
							}
						}
					}
				} else {
					// Gộp bàn vẫn cho chọn nhiều
					if (dsBanDangChon.contains(ban)) {
						dsBanDangChon.remove(ban);
						khungBan.setBackground(null);
					} else {
						dsBanDangChon.add(ban);
						khungBan.setBackground(new Background(
								new BackgroundFill(Color.rgb(100, 200, 255, 0.6), new CornerRadii(10), Insets.EMPTY)));
					}
				}

				return;
			}

			// Phần click 1 hoặc 2 lần vẫn giữ nguyên
			if (e.getClickCount() == 1) {
				if (dsBanDangChon.contains(ban)) {
					dsBanDangChon.remove(ban);
					khungBan.setBackground(null);
				} else {
					dsBanDangChon.add(ban);
					khungBan.setBackground(new Background(
							new BackgroundFill(Color.rgb(255, 200, 100, 0.6), new CornerRadii(10), Insets.EMPTY)));
				}
			} else if (e.getClickCount() == 2) {
				if ("QUAN_LY_BAN".equals(context)) {
					moPopupTuyChinhBan(ban);
				} else if ("DAT_BAN".equals(context)) {
					PhieuDatBan pdbDayDu = pdbDAO.layPhieuDangHoatDongTheoBan(ban.getMaBan());

					if (pdbDayDu == null) {
	                    showKhongCoPhieu(ban);
	                    return;
	                }

					mainContent.getChildren().setAll(new GiaoDienChiTietBan(mainContent, ban, pdbDayDu));
				}
			}
		});

		pane.getChildren().add(khungBan);
		khungBan.setUserData(ban.getMaBan());

		if ("Đang phục vụ".equals(ban.getTrangThai())) {
			PhieuDatBan pdb = mapDangPhucVuToanBo.get(ban.getMaBan());
			if (pdb != null) {
				themMarkerChoPhieu(khungBan, pdb);

				Label lblCountDown = new Label();
				lblCountDown.setUserData(pdb.getMaPDB());
				lblCountDown.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;"
						+ "-fx-text-fill: white; -fx-background-color: rgba(0,0,0,0.6);"
						+ "-fx-padding: 2 8; -fx-background-radius: 6;");
				StackPane.setAlignment(lblCountDown, Pos.TOP_CENTER);
				StackPane.setMargin(lblCountDown, new Insets(-35, 0, 0, 0));
				khungBan.getChildren().add(lblCountDown);
				CountdownController.getInstance().startDangPhucVu(pdb, lblCountDown);
			}
		}
	}

// Hàm phụ để gom logic border style
	private String getBorderStyle(Ban ban, String context, Map<String, PhieuDatBan> mapDatTruoc) {
		if ("QUAN_LY_BAN".equals(context)) {
			return "-fx-border-color: lightgray; -fx-border-width: 3; -fx-border-radius: 12;";
		}

		// ƯU TIÊN PHIẾU ĐẶT TRƯỚC
		PhieuDatBan pdb = mapDatTruoc.get(ban.getMaBan());
		if (pdb != null && pdb.getNgayDat() != null) {
			long minutes = java.time.Duration.between(LocalDateTime.now(), pdb.getNgayDat()).toMinutes();

			if (minutes >= 0 && minutes <= 90) {
				return "-fx-border-color: deepskyblue; -fx-border-width: 3; -fx-border-radius: 12;";
			}
		}

		// SAU ĐÓ MỚI XÉT TRẠNG THÁI BÀN
		if ("Đang phục vụ".equals(ban.getTrangThai())) {
			return "-fx-border-color: orange; -fx-border-width: 3; -fx-border-radius: 12;";
		}

		if ("Bảo trì".equals(ban.getTrangThai())) {
			return "-fx-border-color: green; -fx-border-width: 3; -fx-border-radius: 12;";
		}

		return "-fx-border-color: lightgray; -fx-border-width: 3; -fx-border-radius: 12;";
	}

	private void moPopupTuyChinhBan(Ban ban) {
		GiaoDienTuyChinhBan giaoDien = new GiaoDienTuyChinhBan(ban);

		Stage popup = new Stage();
		popup.setTitle(ban != null ? "Chỉnh sửa bàn" : "Thêm bàn mới");
		popup.setScene(new javafx.scene.Scene(giaoDien, 500, 270));
		popup.initOwner(this.getScene().getWindow()); // Gắn với cửa sổ cha
		popup.setResizable(false);
		popup.centerOnScreen();

		giaoDien.getBtnDiChuyen().setOnAction(e -> {
			this.choPhepDiChuyen = true;
			popup.close(); // Đóng popup
			Alert alert = new Alert(Alert.AlertType.INFORMATION,
					"Chế độ di chuyển đã bật! Bạn có thể kéo bàn để thay đổi vị trí.");
			alert.initOwner(this.getScene().getWindow());
			alert.showAndWait();
		});

		// Hiển thị popup
		popup.showAndWait();
	}

	public Pane getKhuVucBan() {
		return khuVucBan;
	}

	public List<Ban> getDsBanDangChon() {
		return dsBanDangChon;
	}

	public void clearAllBan() {
		khuVucBan.getChildren().clear();
	}

	public void hienThiBanTheoDieuKien(String maTang, String trangThai, String loaiBan, int soGhe) {
		clearAllBan();

		if (maTang != null) {
			this.maTangHienTai = maTang;
		}

		// Breadcrumb
		String tangText = (maTang != null) ? maTang.replace("TG00000", "") : "?";
		lblBreadcrumb.setText("Trang chủ / Quản lý bàn / Tầng " + tangText);

		// Background
		if (maTang != null) {
			Platform.runLater(() -> {
				if (khuVucBan.getWidth() > 0 && khuVucBan.getHeight() > 0) {
					setBackgroundTheoTang(maTang);
				} else {
					khuVucBan.layoutBoundsProperty()
							.addListener((obs, oldVal, newVal) -> setBackgroundTheoTang(maTang));
				}
			});
		}

		List<Ban> dsBan = (maTang != null) ? banDAO.layTheoTang(maTang) : banDAO.layTatCaBan();

		if (dsBan.isEmpty()) {
			Label lblThongBao = new Label("Không có bàn nào thỏa điều kiện.");
			lblThongBao.setStyle("-fx-font-size: 18px; -fx-text-fill: #666;");
			khuVucBan.getChildren().add(lblThongBao);
			return;
		}

		for (Ban b : dsBan) {
			boolean thoaDieuKien = true;

			if (trangThai != null && !trangThai.equals("Tất cả") && !b.getTrangThai().equals(trangThai)) {
				thoaDieuKien = false;
			}

			if (loaiBan != null && !loaiBan.equals("Tất cả") && !b.getLoaiBan().equals(loaiBan)) {
				thoaDieuKien = false;
			}

			if (soGhe > 0 && b.getLoaiBan().getSoChoNgoi() != soGhe) {
				thoaDieuKien = false;
			}

			if (thoaDieuKien) {
				taoBan(khuVucBan, b, new HashMap<>());
			}
		}
	}

	public StackPane timKhungBanTheoMa(String maBan) {
		for (Node n : khuVucBan.getChildren()) {
			if (n instanceof StackPane sp) {
				if (sp.getUserData() != null && sp.getUserData().equals(maBan)) {
					return sp;
				}
			}
		}
		return null;
	}

	private void themMarkerChoPhieu(StackPane khungBan, PhieuDatBan pdb) {
		if (pdb == null)
			return;

		Color color = mapMauChoPhieu.computeIfAbsent(pdb.getMaPDB(), k -> {
			Color c = danhSachMau.get(indexMau % danhSachMau.size());
			indexMau++;
			return c;
		});

		Circle marker = new Circle(8, color);
		StackPane.setAlignment(marker, Pos.TOP_RIGHT);
		marker.setTranslateX(-5);
		marker.setTranslateY(5);

		khungBan.getChildren().add(marker);
	}

	public void clearBanDangChon() {
		dsBanDangChon.clear();
	}

	public void setPhieuDangThaoTac(PhieuDatBan pdb) {
		this.phieuDangThaoTac = pdb;
	}

	private void xuLyXacNhanGopBan() {

		if (phieuDangThaoTac == null) {
			new Alert(Alert.AlertType.ERROR, "Không xác định được phiếu đang thao tác!").show();
			return;
		}

		if (dsBanDangChon.isEmpty()) {
			new Alert(Alert.AlertType.WARNING, "Vui lòng chọn ít nhất 1 bàn trống để gộp!").show();
			return;
		}

		try (Connection conn = ConnectSQL.getConnection()) {
			conn.setAutoCommit(false);

			// 2️⃣ INSERT BÀN + isBanChinh
			String sql = """
					    INSERT INTO PhieuDatBan_Ban(maPDB, maBan, isBanChinh)
					    VALUES (?, ?, 0)
					""";

			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				for (Ban ban : dsBanDangChon) {
					ps.setString(1, phieuDangThaoTac.getMaPDB());
					ps.setString(2, ban.getMaBan());
					ps.addBatch();
				}
				ps.executeBatch();
			}

			// 3️⃣ CẬP NHẬT TRẠNG THÁI BÀN
			banDAO.capNhatTrangThaiDanhSach(dsBanDangChon, "Đang phục vụ");

			conn.commit();

			Alert alert = new Alert(Alert.AlertType.INFORMATION, "Gộp bàn thành công");
			alert.initOwner(this.getScene().getWindow());
			alert.show();

			dsBanDangChon.clear();
			refresh();

			Ban banChinh = layBanChinhCuaPhieuDangThaoTac();
			
			// 4️⃣ QUAY VỀ CHI TIẾT BÀN → DÙNG BÀN CHÍNH
			mainContent.getChildren().setAll(new GiaoDienChiTietBan(mainContent, banChinh, phieuDangThaoTac));

		} catch (Exception e) {
			e.printStackTrace();
			new Alert(Alert.AlertType.ERROR, "Gộp bàn thất bại!").show();
		}
	}

	private void xuLyXacNhanChuyenBan(Ban banCu) {
		if (phieuDangThaoTac == null) {
			new Alert(Alert.AlertType.ERROR, "Không xác định được phiếu đang thao tác!").show();
			return;
		}

		if (dsBanDangChon.isEmpty()) {
			new Alert(Alert.AlertType.WARNING, "Vui lòng chọn 1 bàn trống để chuyển!").show();
			return;
		}

		Ban banDich = dsBanDangChon.get(0);

		if (banCu.getMaBan().equals(banDich.getMaBan())) {
			new Alert(Alert.AlertType.INFORMATION, "Bàn bạn chọn đã là bàn hiện tại!").show();
			return;
		}

		try (Connection conn = ConnectSQL.getConnection()) {
			conn.setAutoCommit(false);

			// ✅ Update đúng bàn đã truyền vào
			String sqlUpdate = "UPDATE PhieuDatBan_Ban SET maBan = ? WHERE maPDB = ? AND maBan = ?";
			try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
				ps.setString(1, banDich.getMaBan()); // bàn mới
				ps.setString(2, phieuDangThaoTac.getMaPDB()); // phiếu
				ps.setString(3, banCu.getMaBan()); // bàn cũ
				ps.executeUpdate();
			}

			// ✅ Cập nhật trạng thái bàn
			banDAO.capNhatTrangThaiDanhSach(List.of(banCu), "Trống");
			banDAO.capNhatTrangThaiDanhSach(List.of(banDich), "Đang phục vụ");

			conn.commit();

			Alert alert = new Alert(Alert.AlertType.INFORMATION, "Chuyển bàn thành công!");
			alert.initOwner(this.getScene().getWindow());
			alert.show();
			
			dsBanDangChon.clear();
			refresh();
			
			Ban banChinh = layBanChinhCuaPhieuDangThaoTac();

			// 4️⃣ QUAY VỀ CHI TIẾT BÀN → DÙNG BÀN CHÍNH
			mainContent.getChildren().setAll(new GiaoDienChiTietBan(mainContent, banChinh, phieuDangThaoTac));

		} catch (Exception e) {
			e.printStackTrace();
			new Alert(Alert.AlertType.ERROR, "Chuyển bàn thất bại!").show();
		}
	}

	private Ban layBanChinhCuaPhieuDangThaoTac() {
		if (phieuDangThaoTac == null)
			return null;

		return new PhieuDatBanDAO().layBanChinhCuaPhieu(phieuDangThaoTac.getMaPDB());
	}

	public void setBanCu(Ban ban) {
		this.banCu = ban;
	}
	
	protected void showKhongCoPhieu(Ban ban) {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle("Thông báo");
	    alert.setHeaderText(null);
	    alert.setContentText("Bàn \"" + ban.getTenBan() + "\" hiện chưa có phiếu hoạt động.");
	    
	    // Set owner để luôn hiển thị trên cùng
	    if (this.getScene() != null && this.getScene().getWindow() != null) {
	        alert.initOwner(this.getScene().getWindow());
	    }

	    alert.initModality(Modality.APPLICATION_MODAL);
	    alert.showAndWait();
	}

}