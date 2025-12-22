package com.thefourrestaurant.view.ban;

import java.time.LocalDateTime;

import com.thefourrestaurant.DAO.BanDAO;
import com.thefourrestaurant.DAO.PhieuDatBanDAO;
import com.thefourrestaurant.model.Ban;
import com.thefourrestaurant.model.PhieuDatBan;
import com.thefourrestaurant.util.ClockText;
import com.thefourrestaurant.view.components.ButtonSample2;
import com.thefourrestaurant.view.monan.GiaoDienGoiMon;

import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

public class GiaoDienNhanBan extends BorderPane {

	// ===== MÀU =====
	private static final String COLOR_BG_HEADER = "#1E424D";
	private static final String COLOR_TEXT_GOLD = "#DDB248";

	private final PhieuDatBanDAO phieuDAO = new PhieuDatBanDAO();
	private final BanDAO banDAO = new BanDAO();
	private final StackPane parentPane;
	private final QuanLiBan quanLiBan;

	private TableView<PhieuDatBan> table;
	private TextField txtSoDT;

	private GiaoDienChiTietPhieuDatBan chiTietPane;
	private PhieuDatBan phieuDangChon;

	private ObservableList<PhieuDatBan> masterList;
	private FilteredList<PhieuDatBan> filtered;

	public GiaoDienNhanBan(StackPane parentPane, QuanLiBan quanLiBan) {
		this.parentPane = parentPane;
		this.quanLiBan = quanLiBan;

		setStyle("-fx-background-color: #f0f0f0;");
		setTop(taoHeader());
		setCenter(taoNoiDungChinh());
		setBottom(taoThanhNutDuoi());
		
		Timeline autoCancelTimeline = new Timeline(new KeyFrame(Duration.minutes(1), e -> checkAutoCancel()));
		autoCancelTimeline.setCycleCount(Timeline.INDEFINITE);
		autoCancelTimeline.play();
	}

	// ================= HEADER =================
	private VBox taoHeader() {
		VBox header = new VBox(10);
		header.setPadding(new Insets(15));
		header.setStyle("-fx-background-color: " + COLOR_BG_HEADER + ";");

		Label lblTitle = new Label("NHẬN BÀN – PHIẾU ĐẶT TRƯỚC");
		lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + COLOR_TEXT_GOLD);

		ClockText clock = ClockText.getInstance();
		clock.setStyle("-fx-fill: " + COLOR_TEXT_GOLD + "; -fx-font-size: 14px; -fx-font-weight: bold;");

		HBox searchBox = new HBox(10);
		searchBox.setAlignment(Pos.CENTER_LEFT);

		Label lblSoDT = new Label("SĐT khách hàng:");
		lblSoDT.setStyle("-fx-text-fill: " + COLOR_TEXT_GOLD + "; -fx-font-weight: bold;");

		txtSoDT = new TextField();
		txtSoDT.setPromptText("Nhập số điện thoại...");
		txtSoDT.setPrefWidth(250);

		searchBox.getChildren().addAll(lblSoDT, txtSoDT);
		header.getChildren().addAll(lblTitle, clock, searchBox);

		return header;
	}

	// ================= NỘI DUNG CHÍNH =================
	private SplitPane taoNoiDungChinh() {
		SplitPane split = new SplitPane();
		split.setDividerPositions(0.45);

		chiTietPane = new GiaoDienChiTietPhieuDatBan();

		VBox bangPhieu = taoBangPhieu();

		VBox rightBox = new VBox(10, chiTietPane);
		rightBox.setPadding(new Insets(10));

		split.getItems().addAll(bangPhieu, rightBox);
		return split;
	}

	// ================= TABLE PHIẾU =================
	private VBox taoBangPhieu() {
		table = new TableView<>();
		table.setPrefHeight(500);

		TableColumn<PhieuDatBan, String> colMa = new TableColumn<>("Mã PĐB");
		colMa.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getMaPDB()));

		TableColumn<PhieuDatBan, String> colKH = new TableColumn<>("Khách hàng");
		colKH.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
				c.getValue().getKhachHang() != null ? c.getValue().getKhachHang().getHoTen() : ""));

		TableColumn<PhieuDatBan, String> colSDT = new TableColumn<>("SĐT");
		colSDT.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
				c.getValue().getKhachHang() != null ? c.getValue().getKhachHang().getSoDT() : ""));

		TableColumn<PhieuDatBan, String> colNgay = new TableColumn<>("Giờ đặt");
		colNgay.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
				c.getValue().getNgayDat() != null ? c.getValue().getNgayDat().toString() : ""));

		TableColumn<PhieuDatBan, String> colBan = new TableColumn<>("Bàn");
		colBan.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
				c.getValue().getDanhSachBan().stream().map(Ban::getTenBan).reduce((a, b) -> a + ", " + b).orElse("")));

		table.getColumns().addAll(colMa, colKH, colSDT, colNgay, colBan);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		refreshTable();

		table.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
			phieuDangChon = selected;
			if (selected != null) {
				chiTietPane.hienThiThongTin(selected);
			}
		});

		VBox box = new VBox(table);
		box.setPadding(new Insets(10));
		return box;
	}

	private HBox taoThanhNutDuoi() {

		// ===== NÚT =====
		ButtonSample2 btnQuayLai = new ButtonSample2("Quay lại", ButtonSample2.Variant.YELLOW, 150, 45);

		ButtonSample2 btnGoiMon = new ButtonSample2("Gọi món", ButtonSample2.Variant.YELLOW, 150, 45);

		ButtonSample2 btnHuyPhieu = new ButtonSample2("Hủy phiếu", ButtonSample2.Variant.YELLOW, 150, 45);

		ButtonSample2 btnNhanBan = new ButtonSample2("Nhận bàn", ButtonSample2.Variant.YELLOW, 150, 45);

		btnQuayLai.setOnAction(e -> {
		    parentPane.getChildren().remove(this);
		    quanLiBan.refresh();
		});

		btnNhanBan.setOnAction(e -> xuLyNhanBan());
		btnHuyPhieu.setOnAction(e -> xuLyHuyPhieu());

		btnGoiMon.setOnAction(e -> xuLyGoiMon());

		// ===== BỐ CỤC =====
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);

		HBox bar = new HBox(15, btnQuayLai, spacer, btnGoiMon, btnHuyPhieu, btnNhanBan);

		bar.setPadding(new Insets(10, 20, 10, 20));
		bar.setAlignment(Pos.CENTER);

		bar.setStyle("""
				    -fx-background-color: #f0f0f0;
				    -fx-border-color: #d0d0d0;
				    -fx-border-width: 1 0 0 0;
				""");

		return bar;
	}

	private void xuLyNhanBan() {

		if (phieuDangChon == null) {
			new Alert(Alert.AlertType.WARNING, "Vui lòng chọn phiếu!").showAndWait();
			return;
		}

		if (!"Đặt trước".equals(phieuDangChon.getTrangThai())) {
			new Alert(Alert.AlertType.WARNING, "Chỉ được nhận bàn với phiếu ĐẶT TRƯỚC!").showAndWait();
			return;
		}

		LocalDateTime gioDat = phieuDangChon.getNgayDat();
		LocalDateTime hienTai = LocalDateTime.now();

		LocalDateTime choPhepNhanTu = gioDat.minusMinutes(10);

		if (hienTai.isBefore(choPhepNhanTu)) {
			new Alert(Alert.AlertType.WARNING,
					"Chưa đến thời gian nhận bàn!\n" + "Chỉ được nhận bàn từ " + choPhepNhanTu.toLocalTime())
					.showAndWait();
			return;
		}

		phieuDAO.capNhatTrangThai(phieuDangChon.getMaPDB(), "Đang phục vụ");
		banDAO.capNhatTrangThaiDanhSach(phieuDangChon.getDanhSachBan(), "Đang phục vụ");

		refreshTable();
		quanLiBan.refresh();
		phieuDangChon = null;

		new Alert(Alert.AlertType.INFORMATION, "Nhận bàn thành công!").showAndWait();
	}

	private void xuLyHuyPhieu() {
	    if (phieuDangChon == null) {
	        new Alert(Alert.AlertType.WARNING, "Vui lòng chọn phiếu!").showAndWait();
	        return;
	    }

	    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
	            "Bạn có chắc chắn muốn HỦY phiếu này?\nHành động này không thể hoàn tác.",
	            ButtonType.YES, ButtonType.NO);

	    if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {

	        // Cập nhật trạng thái phiếu thành "Đã hủy"
	        phieuDAO.capNhatTrangThai(phieuDangChon.getMaPDB(), "Đã hủy");

	        // Cập nhật trạng thái bàn liên quan về "Trống"
	        banDAO.capNhatTrangThaiDanhSach(phieuDangChon.getDanhSachBan(), "Trống");

	        // Load lại TableView và refresh giao diện quản lý bàn
	        refreshTable();
	        quanLiBan.refresh();

	        // Xóa phiếu đang chọn và clear thông tin chi tiết
	        phieuDangChon = null;
	        chiTietPane.clearThongTin();

	        // Hiển thị thông báo thành công
	        new Alert(Alert.AlertType.INFORMATION, "Hủy phiếu thành công!").showAndWait();
	    }
	}

	private void refreshTable() {

		if (masterList == null) {
			masterList = FXCollections.observableArrayList();
			filtered = new FilteredList<>(masterList, p -> true);

			txtSoDT.textProperty().addListener((obs, old, val) -> {
				filtered.setPredicate(pdb -> {
					if (val == null || val.isBlank())
						return true;
					return pdb.getKhachHang() != null && pdb.getKhachHang().getSoDT() != null
							&& pdb.getKhachHang().getSoDT().contains(val.trim());
				});
			});

			table.setItems(filtered);
		}

		masterList.setAll(phieuDAO.layPhieuTheoTrangThai("Đặt trước"));

		table.getSelectionModel().clearSelection();
		phieuDangChon = null;

	    chiTietPane.clearThongTin();
	}

	private void xuLyGoiMon() {
		if (phieuDangChon == null) {
			new Alert(Alert.AlertType.WARNING, "Vui lòng chọn phiếu!").showAndWait();
			return;
		}

		parentPane.getChildren().clear();
		parentPane.getChildren()
				.add(new GiaoDienGoiMon(parentPane, phieuDangChon.getDanhSachBan().get(0), phieuDangChon));
	}
	
	private void checkAutoCancel() {
	    LocalDateTime now = LocalDateTime.now();

	    // Lấy danh sách phiếu đang "Đặt trước"
	    ObservableList<PhieuDatBan> phieuList = FXCollections.observableArrayList(phieuDAO.layPhieuTheoTrangThai("Đặt trước"));

	    for (PhieuDatBan pdb : phieuList) {
	        LocalDateTime gioDat = pdb.getNgayDat();
	        if (gioDat.plusMinutes(15).isBefore(now)) { // Quá 15 phút
	            phieuDAO.capNhatTrangThai(pdb.getMaPDB(), "Khách không đến"); // Hủy phiếu
	            banDAO.capNhatTrangThaiDanhSach(pdb.getDanhSachBan(), "Trống"); // Trả bàn
	            if (phieuDangChon != null && phieuDangChon.equals(pdb)) {
	                phieuDangChon = null;
	                chiTietPane.clearThongTin();
	            }
	        }
	    }

	    refreshTable(); // Cập nhật TableView
	}


}
