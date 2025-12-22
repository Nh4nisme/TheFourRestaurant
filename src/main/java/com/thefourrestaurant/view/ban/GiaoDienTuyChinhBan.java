package com.thefourrestaurant.view.ban;

import com.thefourrestaurant.view.components.ButtonSample2;
import com.thefourrestaurant.view.components.ButtonSample2.Variant;
import com.thefourrestaurant.DAO.BanDAO;
import com.thefourrestaurant.DAO.LoaiBanDAO;
import com.thefourrestaurant.DAO.TangDAO;
import com.thefourrestaurant.model.Ban;
import com.thefourrestaurant.model.LoaiBan;
import com.thefourrestaurant.model.Tang;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.List;

public class GiaoDienTuyChinhBan extends VBox {

	private ComboBox<String> cbLoaiBan;
	private ComboBox<String> cbTrangThai; // trạng thái hoặc tầng nếu thêm bàn
	private Hyperlink linkAttachImage;
	private Label lblImageFileName;
	private Button btnXoa, btnDiChuyen, btnOK, btnHuy;
	private ComboBox<Tang> cboTang;
	private final TangDAO tangDAO = new TangDAO();

	private Ban ban;
	private final LoaiBanDAO loaiBanDAO = new LoaiBanDAO();
	private File selectedImageFile;
	private Runnable onDiChuyen;

	public GiaoDienTuyChinhBan(Ban ban) {
		this.ban = ban;

		setStyle("-fx-background-color: #F5F5F5;");
		setSpacing(0);
		setAlignment(Pos.TOP_CENTER);
		setPrefSize(370, 200);

		// Title bar
		Label lblTitle = new Label("Tùy chỉnh bàn");
		lblTitle.setStyle("-fx-font-size: 16px; -fx-text-fill: #E19E11; -fx-font-weight: bold;");
		HBox titleBar = new HBox(lblTitle);
		titleBar.setAlignment(Pos.CENTER_LEFT);
		titleBar.setPadding(new Insets(10, 20, 10, 20));
		titleBar.setStyle("-fx-background-color: #1E424D;");
		titleBar.setPrefHeight(45);

		// Content
		VBox contentBox = new VBox(12);
		contentBox.setPadding(new Insets(20, 25, 15, 25));
		contentBox.setStyle("-fx-background-color: white;");
		contentBox.setAlignment(Pos.TOP_LEFT);

		// Loại bàn
		HBox row1 = new HBox(15);
		row1.setAlignment(Pos.CENTER_LEFT);
		Label lblLoaiBan = createLabel("Loại bàn:");
		lblLoaiBan.setPrefWidth(80);
		cbLoaiBan = createComboBox();
		cbLoaiBan.setPromptText("Chọn loại bàn");
		HBox.setHgrow(cbLoaiBan, Priority.ALWAYS);
		row1.getChildren().addAll(lblLoaiBan, cbLoaiBan);

		// Trạng thái / tầng
		HBox row2 = new HBox(15);
		row2.setAlignment(Pos.CENTER_LEFT);
		Label lblTrangThai = createLabel("Trạng Thái:");
		lblTrangThai.setPrefWidth(80);

		if (ban != null) {
			// Chỉnh sửa bàn → hiển thị cbTrangThai
			cbTrangThai = createComboBox();
			cbTrangThai.setPromptText("Chọn trạng thái");
			HBox.setHgrow(cbTrangThai, Priority.ALWAYS);
			row2.getChildren().addAll(lblTrangThai, cbTrangThai);
		} else {
			// Thêm bàn mới → hiển thị cboTang
			cboTang = new ComboBox<>();
			cboTang.setPromptText("Chọn tầng");
			HBox.setHgrow(cboTang, Priority.ALWAYS);
			row2.getChildren().addAll(lblTrangThai, cboTang);
		}

		// Hình ảnh
		HBox row3 = new HBox(15);
		row3.setAlignment(Pos.CENTER_LEFT);
		Label lblHinhAnh = createLabel("Hình ảnh:");
		lblHinhAnh.setPrefWidth(80);
		VBox imageBox = new VBox(5);
		linkAttachImage = new Hyperlink(ban != null ? ban.getAnhBan() : "");
		linkAttachImage.setStyle("-fx-text-fill: #0066CC; -fx-font-size: 13px; -fx-underline: true;");
		linkAttachImage.setPadding(new Insets(0));
		lblImageFileName = new Label("");
		lblImageFileName.setStyle("-fx-font-size: 12px; -fx-text-fill: #666666;");
		imageBox.getChildren().addAll(linkAttachImage, lblImageFileName);
		HBox.setHgrow(imageBox, Priority.ALWAYS);
		row3.getChildren().addAll(lblHinhAnh, imageBox);

		contentBox.getChildren().addAll(row1, row2, row3);

		// Button bar
		HBox buttonBar = new HBox(12);
		buttonBar.setAlignment(Pos.CENTER_LEFT);
		buttonBar.setPadding(new Insets(15, 25, 15, 25));
		buttonBar.setStyle("-fx-background-color: #2A4A56;");
		buttonBar.setPrefHeight(55);
		btnXoa = new ButtonSample2("Xóa", Variant.YELLOW, 90);
		btnDiChuyen = new ButtonSample2("Di chuyển", Variant.YELLOW, 100);
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		btnOK = new ButtonSample2("OK", Variant.YELLOW, 80);
		btnHuy = new ButtonSample2("Hủy", Variant.YELLOW, 80);
		buttonBar.getChildren().addAll(btnXoa, btnDiChuyen, spacer, btnOK, btnHuy);

		getChildren().addAll(titleBar, contentBox, buttonBar);

		loadData();
		wireHandlers();
	}

	private Label createLabel(String text) {
		Label label = new Label(text);
		label.setStyle("-fx-font-size: 13px; -fx-text-fill: #E19E11; -fx-font-weight: bold;");
		label.setMinWidth(Region.USE_PREF_SIZE);
		return label;
	}

	private ComboBox<String> createComboBox() {
		ComboBox<String> comboBox = new ComboBox<>();
		comboBox.setStyle(
				"-fx-background-color: white; -fx-border-color: #CCCCCC; -fx-border-radius: 5; -fx-background-radius: 5;");
		comboBox.setPrefHeight(30);
		comboBox.setMaxWidth(Double.MAX_VALUE);
		return comboBox;
	}

	private void loadData() {
		try {
			// Loại bàn
			List<LoaiBan> dsLoaiBan = loaiBanDAO.layTatCa();
			for (LoaiBan lb : dsLoaiBan)
				cbLoaiBan.getItems().add(lb.getTenLoaiBan());

			if (ban != null) {
				// Chỉnh sửa bàn
				if (ban.getLoaiBan() != null)
					cbLoaiBan.setValue(ban.getLoaiBan().getTenLoaiBan());

				// Hiển thị trạng thái bàn
				cbTrangThai.getItems().addAll("Trống", "Đang sử dụng", "Đã đặt trước");
				if (ban.getTrangThai() != null)
					cbTrangThai.setValue(ban.getTrangThai());
			} else {
				// Thêm bàn mới -> chọn tầng từ DAO
				List<Tang> dsTang = tangDAO.layTatCaTang();
				cboTang.getItems().addAll(dsTang);
				if (!dsTang.isEmpty())
					cboTang.setValue(dsTang.get(0));

				cboTang.setCellFactory(param -> new ListCell<>() {
					@Override
					protected void updateItem(Tang item, boolean empty) {
						super.updateItem(item, empty);
						setText(empty || item == null ? null : item.getTenTang());
					}
				});

				cboTang.setButtonCell(new ListCell<>() {
					@Override
					protected void updateItem(Tang item, boolean empty) {
						super.updateItem(item, empty);
						setText(empty || item == null ? null : item.getTenTang());
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void wireHandlers() {
		// Chọn hình ảnh
		linkAttachImage.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Chọn hình ảnh");
			fileChooser.getExtensionFilters()
					.addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
			Stage stage = (Stage) getScene().getWindow();
			selectedImageFile = fileChooser.showOpenDialog(stage);
			if (selectedImageFile != null)
				lblImageFileName.setText(selectedImageFile.getName());
		});

		// Chọn loại bàn -> gán hình ảnh mặc định nếu thêm bàn mới
		cbLoaiBan.setOnAction(e -> {
			if (ban == null) {
				String loaiBanChon = cbLoaiBan.getValue();
				if (loaiBanChon == null)
					return;
				String path = switch (loaiBanChon) {
				case "Bàn thường 2 ghế" -> "/com/thefourrestaurant/images/Ban/Ban_2.png";
				case "Bàn thường 4 ghế" -> "/com/thefourrestaurant/images/Ban/Ban_4.png";
				case "Bàn thường 6 ghế" -> "/com/thefourrestaurant/images/Ban/Ban_6.png";
				case "Bàn thường 8 ghế" -> "/com/thefourrestaurant/images/Ban/Ban_8.png";
				case "Bàn VIP 4 ghế" -> "/com/thefourrestaurant/images/Ban/Ban_4.png";
				case "Bàn VIP 6 ghế" -> "/com/thefourrestaurant/images/Ban/Ban_6.png";
				case "Bàn VIP 8 ghế" -> "/com/thefourrestaurant/images/Ban/Ban_8.png";
				default -> "";
				};
				if (!path.isEmpty()) {
					lblImageFileName.setText(path);
					selectedImageFile = new File(path);
				}
			}
		});

		// Xóa bàn
		btnXoa.setOnAction(e -> {
			Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xóa bàn này?",
					ButtonType.YES, ButtonType.NO);
			confirm.setTitle("Xác nhận xóa");
			confirm.setHeaderText(null);
			confirm.initOwner(getScene().getWindow());
			confirm.showAndWait().ifPresent(response -> {
				if (response == ButtonType.YES && ban != null) {
					new BanDAO().xoaBan(ban.getMaBan());
					Stage stage = (Stage) getScene().getWindow();
					if (stage != null)
						stage.close();
				}
			});
		});

		// Di chuyển
		btnDiChuyen.setOnAction(e -> {
			if (onDiChuyen != null)
				onDiChuyen.run();
			Stage stage = (Stage) getScene().getWindow();
			if (stage != null)
				stage.close();
		});

		// OK
		btnOK.setOnAction(e -> luuBan());

		// Hủy
		btnHuy.setOnAction(e -> {
			Stage stage = (Stage) getScene().getWindow();
			if (stage != null)
				stage.close();
		});
	}

	private void luuBan() {
		try {
			String loaiBanChon = cbLoaiBan.getValue();

			if (loaiBanChon == null || loaiBanChon.isEmpty()) {
				showAlert("Vui lòng chọn loại bàn", Alert.AlertType.WARNING);
				return;
			}

			LoaiBan loaiBan = null;
			List<LoaiBan> dsLoaiBan = loaiBanDAO.layLoaiBanTheoTen(loaiBanChon);
			if (!dsLoaiBan.isEmpty()) {
				loaiBan = dsLoaiBan.get(0);
			}

			if (loaiBan == null) {
				showAlert("Không tìm thấy loại bàn phù hợp!", Alert.AlertType.ERROR);
				return;
			}

			BanDAO banDAO = new BanDAO();
			boolean isNew = (ban == null);

			if (isNew) {
				ban = new Ban();
				ban.setMaBan(banDAO.sinhMaBanMoi());
				ban.setTenBan("Bàn " + ban.getMaBan());
				ban.setTrangThai("Trống"); // trạng thái mặc định

				if (cboTang != null) {
					Tang tangChon = cboTang.getValue();
					if (tangChon != null) {
						ban.setTang(tangChon);
					}
				}

				// --- Đặt tọa độ mặc định giữa màn hình ---
				// dùng runLater để chắc chắn pane đã được layout
				javafx.application.Platform.runLater(() -> {
					if (getParent() instanceof QuanLiBan parentPane) {
						double paneWidth = parentPane.getWidth();
						double paneHeight = parentPane.getHeight();

						double banWidth = 100; // giả sử kích thước bàn hiển thị
						double banHeight = 60;

						ban.setToaDoX((int) ((paneWidth - banWidth) / 2));
						ban.setToaDoY((int) ((paneHeight - banHeight) / 2));

						// Sau khi set x/y mới, cập nhật database
						new BanDAO().capNhatBan(ban);

						// Refresh UI
						parentPane.refresh();
					}
				});
			} else {
				// Chỉnh sửa bàn
				if (cbTrangThai != null) {
					String trangThaiChon = cbTrangThai.getValue();
					if (trangThaiChon != null && !trangThaiChon.isEmpty()) {
						ban.setTrangThai(trangThaiChon);
					}
				}
			}

			ban.setLoaiBan(loaiBan);

			if (selectedImageFile != null) {
				ban.setAnhBan(selectedImageFile.getAbsolutePath());
			}

			// Lưu bàn: nếu bàn mới thì thêm, nếu bàn cũ thì cập nhật
			boolean ok = isNew ? banDAO.themBan(ban) : banDAO.capNhatBan(ban);

			if (ok) {
				showAlert("Lưu bàn thành công!", Alert.AlertType.INFORMATION);
				if (getParent() instanceof QuanLiBan parent) {
					parent.refresh();
				}
				Stage stage = (Stage) btnOK.getScene().getWindow();
				if (stage != null) {
					stage.close();
				}
			} else {
				showAlert("Lưu bàn thất bại!", Alert.AlertType.ERROR);
			}

		} catch (Exception e) {
			e.printStackTrace();
			showAlert("Có lỗi xảy ra: " + e.getMessage(), Alert.AlertType.ERROR);
		}
	}

	private void showAlert(String message, Alert.AlertType type) {
		Alert alert = new Alert(type, message);
		alert.setTitle(
				type == Alert.AlertType.ERROR ? "Lỗi" : type == Alert.AlertType.WARNING ? "Cảnh báo" : "Thông báo");
		alert.setHeaderText(null);
		alert.initOwner(getScene().getWindow());
		alert.showAndWait();
	}

	// Getters
	public ComboBox<String> getCbLoaiBan() {
		return cbLoaiBan;
	}

	public ComboBox<String> getCbTrangThai() {
		return cbTrangThai;
	}

	public Button getBtnXoa() {
		return btnXoa;
	}

	public Button getBtnDiChuyen() {
		return btnDiChuyen;
	}

	public Button getBtnOK() {
		return btnOK;
	}

	public Button getBtnHuy() {
		return btnHuy;
	}

	public File getSelectedImageFile() {
		return selectedImageFile;
	}

	public void setOnDiChuyen(Runnable onDiChuyen) {
		this.onDiChuyen = onDiChuyen;
	}
}
