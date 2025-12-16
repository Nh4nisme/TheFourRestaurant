package com.thefourrestaurant.view.monan;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.ComboBox;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.thefourrestaurant.DAO.ChiTietPDBDAO;
import com.thefourrestaurant.DAO.ChiTietKhuyenMaiDAO;
import com.thefourrestaurant.model.ChiTietKhuyenMai;
import com.thefourrestaurant.DAO.KhuyenMaiDAO;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.DAO.MonAnDAO;
import com.thefourrestaurant.DAO.LoaiMonDAO;
import com.thefourrestaurant.model.Ban;
import com.thefourrestaurant.model.LoaiMon;
import com.thefourrestaurant.model.ChiTietPDB;
import com.thefourrestaurant.model.MonAn;
import com.thefourrestaurant.model.PhieuDatBan;
import com.thefourrestaurant.view.ban.GiaoDienDatBan;
import com.thefourrestaurant.view.components.ButtonSample;
import com.thefourrestaurant.view.components.DropDownButton;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GiaoDienGoiMon extends BorderPane {
    private ButtonSample btnTim, btnLamMoi;
    private StackPane mainContent;
    private MonAnDAO monAnDAO = new MonAnDAO();
    private LoaiMonDAO loaiMonDAO = new LoaiMonDAO();
    private ChiTietKhuyenMaiDAO chiTietKhuyenMaiDAO = new ChiTietKhuyenMaiDAO();
    private KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAO();
    private Ban ban;
    private ComboBox<LoaiMon> cboLoaiMon;
    private List<MonAn> allMonAn = new java.util.ArrayList<>();
    private GridPane gridMon;
    
    private TableView<ChiTietPDB> bangPhieu;
    private Label lblTongTien;
    private ObservableList<ChiTietPDB> danhSachChiTiet = FXCollections.observableArrayList();
    private PhieuDatBan pdb;
    private Map<String, MonAnBox> monBoxMap = new HashMap<>();


	public GiaoDienGoiMon(StackPane mainContent, Ban ban, PhieuDatBan pdb) {
        this.setStyle("-fx-background-color: white;");
        this.ban = ban;
        this.pdb = pdb;
        this.mainContent = mainContent;
        
        getStylesheets().add(getClass().getResource("/com/thefourrestaurant/css/Application.css").toExternalForm());
        
        HBox thanhTren = taoThanhTren();

        VBox topContainer = new VBox(thanhTren);
        this.setTop(topContainer);

        HBox noiDungChinh = new HBox(20);

        VBox khungTrai = taoLuoiMonAn();
        VBox khungPhai = taoKhungPhieuGoiMon();

        khungTrai.setAlignment(Pos.TOP_CENTER);
        khungPhai.setAlignment(Pos.TOP_CENTER);

        HBox.setHgrow(khungTrai, Priority.ALWAYS);
        HBox.setHgrow(khungPhai, Priority.ALWAYS);

        noiDungChinh.getChildren().addAll(khungTrai, khungPhai);
        this.setCenter(noiDungChinh);
    }

    private HBox taoThanhTren() {

        HBox thanhTren = new HBox(15);
        thanhTren.setPadding(new Insets(15, 20, 15, 20));
        thanhTren.setAlignment(Pos.CENTER_LEFT);
        thanhTren.setStyle("-fx-background-color: #1E424D;");

        DropDownButton menuThucDon = new DropDownButton(
                "Thực đơn  ▼",
                List.of("Buổi Sáng  ▼","Buổi Trưa  ▼","Buổi Tối  ▼","Khai Vị  ▼"),
                null,
                45,
                16,
                3
        );

        Label lblLoaiMon = new Label("Loại món:");
        lblLoaiMon.setTextFill(Color.web("#D4A84A"));
        lblLoaiMon.setFont(Font.font("System", FontWeight.BOLD, 14));

        // ComboBox chọn loại món để lọc
        cboLoaiMon = new ComboBox<>();
        cboLoaiMon.setPromptText("Chọn loại món...");
        cboLoaiMon.setPrefWidth(200);
        // Nạp dữ liệu loại món (thêm tùy chọn Tất cả)
        LoaiMon allItem = new LoaiMon("ALL", "Tất cả", null);
        List<LoaiMon> loaiList = loaiMonDAO.layTatCaLoaiMon();
        cboLoaiMon.getItems().add(allItem);
        if (loaiList != null && !loaiList.isEmpty()) cboLoaiMon.getItems().addAll(loaiList);
        cboLoaiMon.setValue(allItem);
        cboLoaiMon.setOnAction(evt -> {
            LoaiMon sel = cboLoaiMon.getValue();
            if (sel == null || "ALL".equals(sel.getMaLoaiMon())) refreshMonGrid(null);
            else refreshMonGrid(sel.getMaLoaiMon());
        });

        Label lblTenMon = new Label("Tên món:");
        lblTenMon.setTextFill(Color.web("#D4A84A"));
        lblTenMon.setFont(Font.font("System", FontWeight.BOLD, 14));

        TextField txtTenMon = new TextField();
        txtTenMon.setPromptText("Tìm tên món...");
        txtTenMon.setPrefWidth(300);

        btnTim = new ButtonSample("Tìm kiếm", "", 35, 14,3);
        btnLamMoi = new ButtonSample("Làm mới", "", 35, 14,3);

        thanhTren.getChildren().addAll(menuThucDon, lblLoaiMon, cboLoaiMon, lblTenMon, txtTenMon, btnTim, btnLamMoi);
        return thanhTren;
    }

    private VBox taoLuoiMonAn() {
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(10));

        // GridPane chứa các món ăn (dùng lại để refresh khi lọc)
        gridMon = new GridPane();
        gridMon.setHgap(15);
        gridMon.setVgap(15);
        gridMon.setAlignment(Pos.TOP_CENTER);

        for (int i = 0; i < 4; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(25);
            cc.setHalignment(HPos.CENTER);
            gridMon.getColumnConstraints().add(cc);
        }

        // Lấy danh sách món ăn (chỉ những món HIỂN THỊ) từ DB và hiển thị
        allMonAn = monAnDAO.layTatCaMonAnHienThi();
        populateGrid(allMonAn);

        // ScrollPane bọc GridPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(gridMon);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPrefHeight(500); // bạn có thể chỉnh chiều cao scrollPane

        // Thanh phân trang (nếu muốn sử dụng)
        HBox phanTrang = new HBox(10);
        phanTrang.setAlignment(Pos.CENTER);
        phanTrang.setPadding(new Insets(10));

        container.getChildren().addAll(scrollPane, phanTrang);
        VBox.setVgrow(scrollPane, Priority.ALWAYS); // ScrollPane mở rộng theo VBox

        return container;
    }

    private void populateGrid(List<MonAn> danhSachMon) {
        gridMon.getChildren().clear();
        monBoxMap.clear();

        int col = 0;
        int row = 0;
        if (danhSachMon == null) return;
        for (MonAn mon : danhSachMon) {
            MonAnBox monBox = new MonAnBox(
                mon.getTenMon(),
                mon.getDonGia() != null ? String.format("%,.0f", mon.getDonGia().doubleValue()) : "0",
                mon.getHinhAnh() != null ? mon.getHinhAnh() : "🍽️"
            );
            monBox.updateSoLuong(mon.getSoLuong());
            monBox.updateDaBan(mon.getDaBan());
            monBox.setOnMouseClicked(e -> themMonVaoPhieu(mon));

            monBoxMap.put(mon.getMaMonAn(), monBox);
            gridMon.add(monBox, col, row);

            col++;
            if (col == 4) { col = 0; row++; }
        }
    }

    private void refreshMonGrid(String maLoai) {
        if (maLoai == null || maLoai.isEmpty()) {
            populateGrid(allMonAn);
            return;
        }
        List<MonAn> filtered = new java.util.ArrayList<>();
        for (MonAn m : allMonAn) {
            if (m.getLoaiMon() != null && maLoai.equals(m.getLoaiMon().getMaLoaiMon())) filtered.add(m);
        }
        populateGrid(filtered);
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
	private VBox taoKhungPhieuGoiMon() {
	    VBox panel = new VBox(15);
	    panel.setPadding(new Insets(10));
	    panel.setStyle("-fx-background-color: #E8E8E8; -fx-background-radius: 8;");
	    panel.setPrefWidth(650);
	    VBox.setVgrow(panel, Priority.ALWAYS);
	
	    Label lblTieuDe = new Label("PHIẾU GỌI MÓN");
	    lblTieuDe.setFont(Font.font("System", FontWeight.BOLD, 28));
	    lblTieuDe.setTextFill(Color.web("#D4A84A"));
	    lblTieuDe.setAlignment(Pos.CENTER);
	    lblTieuDe.setMaxWidth(Double.MAX_VALUE);
	
	    Label lblBan = new Label("Bàn: " + ban.getTenBan());
	    lblBan.setFont(Font.font("System", FontWeight.BOLD, 18));
	    lblBan.setTextFill(Color.web("#D4A84A"));
	
	    bangPhieu = new TableView<>();
	    bangPhieu.setPrefHeight(450);
	    bangPhieu.setStyle("-fx-background-color: white;");
	    bangPhieu.setItems(danhSachChiTiet);
	    bangPhieu.setEditable(true);
	
        TableColumn<ChiTietPDB, String> tenMonCol = new TableColumn<>("Tên món");
        TableColumn<ChiTietPDB, String> donGiaCol = new TableColumn<>("Đơn giá");
        TableColumn<ChiTietPDB, String> khuyenMaiCol = new TableColumn<>("Khuyến mãi");
        TableColumn<ChiTietPDB, String> giaKhuyenMaiCol = new TableColumn<>("Giá khuyến mãi");
        TableColumn<ChiTietPDB, String> soLuongCol = new TableColumn<>("Số lượng");
        TableColumn<ChiTietPDB, String> thanhTienCol = new TableColumn<>("Thành tiền");
	    TableColumn<ChiTietPDB, String> ghiChuCol = new TableColumn<>("Ghi chú");
	    TableColumn<ChiTietPDB, Void> xoaCol = new TableColumn<>("Xóa");
	    xoaCol.setCellFactory(col -> new javafx.scene.control.TableCell<>() {
	        private final Button btnXoa = new Button("❌");

	        {
	            btnXoa.setStyle(
	                "-fx-background-color: transparent; " +
	                "-fx-cursor: hand; -fx-font-size: 16px;"
	            );

                btnXoa.setOnAction(e -> {
                    ChiTietPDB chiTiet = getTableView().getItems().get(getIndex());
                    // Trả lại số lượng cho món (client-side)
                    MonAn mon = chiTiet.getMonAn();
                    if (mon != null) {
                        mon.setSoLuong(mon.getSoLuong() + chiTiet.getSoLuong());
                        MonAnBox box = monBoxMap.get(mon.getMaMonAn());
                        if (box != null) box.updateSoLuong(mon.getSoLuong());
                    }
                    danhSachChiTiet.remove(chiTiet);
                    capNhatTongTien();
                });
	        }

	        @Override
	        protected void updateItem(Void item, boolean empty) {
	            super.updateItem(item, empty);
	            if (empty) {
	                setGraphic(null);
	            } else {
	                setGraphic(btnXoa);
	                setAlignment(Pos.CENTER);
	            }
	        }
	    });
	    
        ghiChuCol.setCellValueFactory(c ->
            new SimpleStringProperty(c.getValue().getGhiChu() != null ? c.getValue().getGhiChu() : "")
        );
	    ghiChuCol.setCellFactory(TextFieldTableCell.forTableColumn());
	    ghiChuCol.setOnEditCommit(e -> e.getRowValue().setGhiChu(e.getNewValue()));
	
            tenMonCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMonAn().getTenMon()));
            donGiaCol.setCellValueFactory(c -> new SimpleStringProperty(String.format("%,.0f", c.getValue().getDonGia())));

            khuyenMaiCol.setCellValueFactory(c -> new SimpleStringProperty(getTenKhuyenMaiTotNhat(c.getValue())));
            giaKhuyenMaiCol.setCellValueFactory(c -> new SimpleStringProperty(formatCurrency(getGiaKhuyenMaiTotNhat(c.getValue()))));

            soLuongCol.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getSoLuong())));

            thanhTienCol.setCellValueFactory(c -> new SimpleStringProperty(
                String.format("%,.0f", getGiaKhuyenMaiTotNhat(c.getValue()) * c.getValue().getSoLuong())
            ));
	
        bangPhieu.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        bangPhieu.getColumns().addAll(tenMonCol, donGiaCol, khuyenMaiCol, giaKhuyenMaiCol, soLuongCol, thanhTienCol, ghiChuCol, xoaCol);
	
	    lblTongTien = new Label("Tổng tiền: 0 VND");
	    lblTongTien.setFont(Font.font("System", FontWeight.BOLD, 16));
	    lblTongTien.setTextFill(Color.web("#2C5F5F"));
	
	    HBox tongTienBox = new HBox(lblTongTien);
	    tongTienBox.setAlignment(Pos.CENTER_RIGHT);
	
	    ButtonSample btnGuiBep = new ButtonSample("Gửi bếp", 40, 20, 3);
	    VBox boxDuoi = new VBox(10, tongTienBox, btnGuiBep);
	    boxDuoi.setAlignment(Pos.CENTER_RIGHT);
	
	    panel.getChildren().addAll(lblTieuDe, lblBan, bangPhieu, boxDuoi);
	    btnGuiBep.setOnAction(e -> xuLyGuiBep());
	    return panel;
	}

    private void themMonVaoPhieu(MonAn mon) {
        // Không cho đặt nếu hiện tại số lượng bằng 0 (client-side)
        if (mon.getSoLuong() <= 0) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.WARNING,
                "Món " + mon.getTenMon() + " hiện đã hết."
            );
            alert.showAndWait();
            return;
        }

        // Trừ 1 ở client-side và cập nhật hiển thị số lượng trên box
        mon.setSoLuong(Math.max(0, mon.getSoLuong() - 1));
        MonAnBox box = monBoxMap.get(mon.getMaMonAn());
        if (box != null) box.updateSoLuong(mon.getSoLuong());

        // Kiểm tra xem món đã có trong danh sách chưa; nếu có tăng số lượng, nếu chưa thêm mới
        for (ChiTietPDB ct : danhSachChiTiet) {
            if (ct.getMonAn().getMaMonAn().equals(mon.getMaMonAn())) {
                ct.setSoLuong(ct.getSoLuong() + 1);
                bangPhieu.refresh();
                capNhatTongTien();
                return;
            }
        }

        ChiTietPDB chiTietMoi = new ChiTietPDB();
        chiTietMoi.setMonAn(mon);
        chiTietMoi.setDonGia(mon.getDonGia().doubleValue());
        chiTietMoi.setSoLuong(1);

        danhSachChiTiet.add(chiTietMoi);
        capNhatTongTien();
    }
    
    private void capNhatTongTien() {
        double tong = 0;
        for (ChiTietPDB ct : danhSachChiTiet) {
            double giaApDung = getGiaKhuyenMaiTotNhat(ct);
            tong += giaApDung * ct.getSoLuong();
        }
        lblTongTien.setText(String.format("Tổng tiền: %,.0f VND", tong));
    }

    private String formatCurrency(double value) {
        try {
            java.text.NumberFormat nf = java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("vi", "VN"));
            return nf.format(Math.max(0, value));
        } catch (Exception e) {
            return String.format("%,.0f VND", value);
        }
    }

    private double getGiaKhuyenMaiTotNhat(ChiTietPDB ct) {
        if (ct == null || ct.getMonAn() == null) return ct != null ? ct.getDonGia() : 0.0;
        double base = ct.getDonGia();
        try {
            List<ChiTietKhuyenMai> promos = chiTietKhuyenMaiDAO.layActiveTheoMonApDung(ct.getMonAn().getMaMonAn());
            List<KhuyenMai> globalPromos = khuyenMaiDAO.layDanhSachKhuyenMaiSuKienHieuLuc();
            java.math.BigDecimal baseBD = java.math.BigDecimal.valueOf(base);
            java.math.BigDecimal best = baseBD;
            if (promos != null) {
                for (ChiTietKhuyenMai p : promos) {
                    java.math.BigDecimal candidate = baseBD;
                    if (p.getTyLeGiam() != null && p.getTyLeGiam().compareTo(java.math.BigDecimal.ZERO) > 0) {
                        candidate = baseBD.multiply(java.math.BigDecimal.ONE.subtract(p.getTyLeGiam().divide(java.math.BigDecimal.valueOf(100))));
                    } else if (p.getSoTienGiam() != null && p.getSoTienGiam().compareTo(java.math.BigDecimal.ZERO) > 0) {
                        candidate = baseBD.subtract(p.getSoTienGiam());
                    } else if (p.getSoLuongTang() != null && p.getSoLuongTang() > 0) {
                        candidate = baseBD; 
                    }
                    if (candidate.compareTo(best) < 0) best = candidate;
                }
            }
            if (globalPromos != null) {
                for (KhuyenMai g : globalPromos) {
                    java.math.BigDecimal candidate = baseBD;
                    if (g.getTyLe() != null && g.getTyLe().compareTo(java.math.BigDecimal.ZERO) > 0) {
                        candidate = baseBD.multiply(java.math.BigDecimal.ONE.subtract(g.getTyLe().divide(java.math.BigDecimal.valueOf(100))));
                    } else if (g.getSoTien() != null && g.getSoTien().compareTo(java.math.BigDecimal.ZERO) > 0) {
                        candidate = baseBD.subtract(g.getSoTien());
                    }
                    if (candidate.compareTo(best) < 0) best = candidate;
                }
            }
            double result = best.doubleValue();
            return result < 0 ? 0.0 : result;
        } catch (Exception e) {
            e.printStackTrace();
            return base;
        }
    }

    private String getTenKhuyenMaiTotNhat(ChiTietPDB ct) {
        if (ct == null || ct.getMonAn() == null) return "";
        try {
            List<ChiTietKhuyenMai> promos = chiTietKhuyenMaiDAO.layActiveTheoMonApDung(ct.getMonAn().getMaMonAn());
            List<KhuyenMai> globalPromos = khuyenMaiDAO.layDanhSachKhuyenMaiSuKienHieuLuc();
            double base = ct.getDonGia();
            double bestPrice = base;
            ChiTietKhuyenMai bestPromo = null;
            KhuyenMai bestGlobal = null;
            if (promos != null) {
                for (ChiTietKhuyenMai p : promos) {
                    double candidate = base;
                    if (p.getTyLeGiam() != null && p.getTyLeGiam().compareTo(java.math.BigDecimal.ZERO) > 0) {
                        candidate = base * (1 - p.getTyLeGiam().doubleValue() / 100.0);
                    } else if (p.getSoTienGiam() != null && p.getSoTienGiam().compareTo(java.math.BigDecimal.ZERO) > 0) {
                        candidate = base - p.getSoTienGiam().doubleValue();
                    } else if (p.getSoLuongTang() != null && p.getSoLuongTang() > 0) {
                        candidate = base;
                    }
                    if (candidate < bestPrice || bestPromo == null) {
                        bestPrice = candidate;
                        bestPromo = p;
                    }
                }
            }
            if (globalPromos != null) {
                for (KhuyenMai g : globalPromos) {
                    double candidate = base;
                    if (g.getTyLe() != null && g.getTyLe().compareTo(java.math.BigDecimal.ZERO) > 0) {
                        candidate = base * (1 - g.getTyLe().doubleValue() / 100.0);
                    } else if (g.getSoTien() != null && g.getSoTien().compareTo(java.math.BigDecimal.ZERO) > 0) {
                        candidate = base - g.getSoTien().doubleValue();
                    }
                    if (candidate < bestPrice) {
                        bestPrice = candidate;
                        bestGlobal = g;
                        bestPromo = null;
                    }
                }
            }
            if (bestPromo != null && bestPromo.getKhuyenMai() != null) {
                String name = bestPromo.getKhuyenMai().getTenKM();
                if (name == null || name.isBlank()) name = bestPromo.getKhuyenMai().getMaKM();
                if ((bestPromo.getTyLeGiam() == null || bestPromo.getTyLeGiam().compareTo(java.math.BigDecimal.ZERO) == 0)
                        && (bestPromo.getSoTienGiam() == null || bestPromo.getSoTienGiam().compareTo(java.math.BigDecimal.ZERO) == 0)
                        && bestPromo.getSoLuongTang() != null && bestPromo.getSoLuongTang() > 0) {
                    return name + " (Tặng món)";
                }
                return name;
            }
            if (bestGlobal != null) {
                String name = bestGlobal.getTenKM();
                return name != null ? name : bestGlobal.getMaKM();
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    
    private void xuLyGuiBep() {
	    try {
	        if (danhSachChiTiet.isEmpty()) {
	            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
	                javafx.scene.control.Alert.AlertType.WARNING, 
	                "Chưa có món nào trong phiếu!"
	            );
	            alert.showAndWait();
	            return;
	        }
	
	        ChiTietPDBDAO chiTietDAO = new ChiTietPDBDAO();
	
	        for (ChiTietPDB ct : danhSachChiTiet) {
	            ct.setPhieuDatBan(pdb);
	            chiTietDAO.them(ct);
	        }
	
	        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
	            javafx.scene.control.Alert.AlertType.INFORMATION,
	            "Đã gửi bếp thành công!"
	        );
	        alert.showAndWait();
	
	        danhSachChiTiet.clear();
	        bangPhieu.refresh();
	        capNhatTongTien();
	        
	        mainContent.getChildren().clear();
	        mainContent.getChildren().add(new GiaoDienDatBan(mainContent));
	
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
	            javafx.scene.control.Alert.AlertType.ERROR,
	            "Lỗi khi gửi bếp: " + ex.getMessage()
	        );
	        alert.showAndWait();
	    }
	}


}
