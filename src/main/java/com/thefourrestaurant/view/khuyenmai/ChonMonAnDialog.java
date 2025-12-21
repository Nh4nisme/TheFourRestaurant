package com.thefourrestaurant.view.khuyenmai;

import com.thefourrestaurant.DAO.LoaiMonDAO;
import com.thefourrestaurant.model.LoaiMon;
import com.thefourrestaurant.model.MonAn;
import com.thefourrestaurant.view.components.ButtonSample;
import com.thefourrestaurant.view.monan.MonAnBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ChonMonAnDialog extends Stage {

    private final List<MonAn> danhSachMonAnGoc;
    private List<MonAn> danhSachMonAnHienThi;

    private final Set<MonAn> cacMonDaChon = new HashSet<>();
    private final GridPane gridViewPane = new GridPane();
    private final int soCotMoiHang = 5;
    private final Label lblItemCount = new Label();
    private final ComboBox<String> cboLoaiMonFilter = new ComboBox<>();
    private final TextField txtTimKiem = new TextField();
    private final Label lblDaChon = new Label("Đã chọn: 0 món");

    private boolean daXacNhan = false;

    public ChonMonAnDialog(List<MonAn> danhSachMonAn, Set<MonAn> cacMonDaChonTruoc, boolean choPhepChonNhieu) {
        this.danhSachMonAnGoc = new ArrayList<>(danhSachMonAn);
        this.danhSachMonAnHienThi = new ArrayList<>(danhSachMonAnGoc);

        if (cacMonDaChonTruoc != null) {
            this.cacMonDaChon.addAll(cacMonDaChonTruoc);
        }

        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Chọn món ăn");

        BorderPane layoutChinh = new BorderPane();
        layoutChinh.setStyle("-fx-background-color: #F5F5F5;");

        // Header
        VBox header = new VBox(10);
        header.setStyle("-fx-background-color: #1E424D;");
        header.setPadding(new Insets(15));

        Label tieuDe = new Label("Chọn món ăn áp dụng khuyến mãi");
        tieuDe.setStyle("-fx-text-fill: #D4A017; -fx-font-size: 18px; -fx-font-weight: bold;");

        lblDaChon.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        capNhatLabelDaChon();

        header.getChildren().addAll(tieuDe, lblDaChon);

        // Middle bar với các nút lọc và tìm kiếm
        HBox khungGiua = createMiddleBar();

        // Content
        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(20));
        VBox.setVgrow(contentBox, Priority.ALWAYS);

        VBox dsMonAnContainer = new VBox(20);
        dsMonAnContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        dsMonAnContainer.setAlignment(Pos.TOP_CENTER);
        dsMonAnContainer.setPadding(new Insets(20));
        VBox.setVgrow(dsMonAnContainer, Priority.ALWAYS);

        setupGridView();

        ScrollPane scrollPane = new ScrollPane(gridViewPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        dsMonAnContainer.getChildren().add(scrollPane);
        contentBox.getChildren().add(dsMonAnContainer);

        // Status bar
        HBox statusBar = new HBox();
        statusBar.setPadding(new Insets(5, 20, 5, 20));
        statusBar.setAlignment(Pos.CENTER_LEFT);
        lblItemCount.setStyle("-fx-text-fill: #333333; -fx-font-size: 12px;");
        statusBar.getChildren().add(lblItemCount);

        // Footer
        HBox footer = new HBox(10);
        footer.setPadding(new Insets(15));
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setStyle("-fx-background-color: #F0F0F0; -fx-border-color: #E0E0E0; -fx-border-width: 1 0 0 0;");

        ButtonSample btnXacNhan = new ButtonSample("Xác nhận", 35, 14, 2);
        btnXacNhan.setOnAction(e -> {
            daXacNhan = true;
            this.close();
        });

        ButtonSample btnHuy = new ButtonSample("Hủy", 35, 14, 2);
        btnHuy.setOnAction(e -> this.close());

        footer.getChildren().addAll(btnXacNhan, btnHuy);

        layoutChinh.setTop(header);
        layoutChinh.setCenter(new VBox(khungGiua, contentBox, statusBar));
        layoutChinh.setBottom(footer);

        Scene scene = new Scene(layoutChinh, 900, 650);
        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            scene.getStylesheets().add(urlCSS.toExternalForm());
        }
        this.setScene(scene);

        locVaCapNhatMonAn();
    }

    private HBox createMiddleBar() {
        HBox khungGiua = new HBox(10);
        khungGiua.setPadding(new Insets(10, 20, 10, 20));
        khungGiua.setAlignment(Pos.CENTER_LEFT);
        khungGiua.setStyle("-fx-background-color: #1E424D;");

        // Filter cho các Loại Món Ăn
        cboLoaiMonFilter.setPromptText("Lọc theo loại");
        LoaiMonDAO loaiMonDAO = new LoaiMonDAO();
        List<String> tenLoaiMon = loaiMonDAO.layTatCaLoaiMon().stream()
                .map(LoaiMon::getTenLoaiMon)
                .collect(Collectors.toList());
        cboLoaiMonFilter.getItems().add("Tất cả");
        cboLoaiMonFilter.getItems().addAll(tenLoaiMon);
        cboLoaiMonFilter.setValue("Tất cả");
        cboLoaiMonFilter.setOnAction(e -> locVaCapNhatMonAn());

        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);

        txtTimKiem.setPromptText("Tìm...");
        txtTimKiem.setPrefWidth(300);

        ButtonSample btnTim = new ButtonSample("Tìm", "", 35, 13, 3);
        btnTim.setOnAction(event -> locVaCapNhatMonAn());
        txtTimKiem.setOnAction(event -> locVaCapNhatMonAn());

        khungGiua.getChildren().addAll(cboLoaiMonFilter, space, txtTimKiem, btnTim);
        return khungGiua;
    }

    private void setupGridView() {
        gridViewPane.setAlignment(Pos.TOP_LEFT);
        gridViewPane.setHgap(20);
        gridViewPane.setVgap(20);
    }

    private void locVaCapNhatMonAn() {
        String tuKhoa = txtTimKiem.getText();
        String loaiMonFilter = cboLoaiMonFilter.getValue();

        List<MonAn> filteredList = new ArrayList<>(danhSachMonAnGoc);

        // Filter by LoaiMon
        if (loaiMonFilter != null && !loaiMonFilter.equals("Tất cả")) {
            filteredList = filteredList.stream()
                    .filter(monAn -> monAn.getLoaiMon() != null && monAn.getLoaiMon().getTenLoaiMon().equals(loaiMonFilter))
                    .collect(Collectors.toList());
        }

        // Filter by từ khóa tìm kiếm
        if (tuKhoa != null && !tuKhoa.trim().isEmpty()) {
            String lowerCaseTuKhoa = tuKhoa.trim().toLowerCase();
            filteredList = filteredList.stream()
                    .filter(monAn -> monAn.getTenMon().toLowerCase().contains(lowerCaseTuKhoa) ||
                            monAn.getMaMonAn().toLowerCase().contains(lowerCaseTuKhoa))
                    .collect(Collectors.toList());
        }

        danhSachMonAnHienThi = filteredList;
        danhSachMonAnHienThi.sort(Comparator.comparing(MonAn::getTenMon));
        updateGridView();
    }

    private void updateGridView() {
        gridViewPane.getChildren().clear();
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        for (int i = 0; i < danhSachMonAnHienThi.size(); i++) {
            MonAn item = danhSachMonAnHienThi.get(i);

            VBox wrapper = new VBox();
            wrapper.setAlignment(Pos.CENTER);
            wrapper.setSpacing(5);

            MonAnBox hopMonAn = new MonAnBox(item);

            // Checkbox để chọn món
            CheckBox checkbox = new CheckBox();
            checkbox.setSelected(cacMonDaChon.contains(item));
            checkbox.setStyle("-fx-font-size: 14px;");

            // Đổi màu nền nếu đã chọn
            if (cacMonDaChon.contains(item)) {
                hopMonAn.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #DDB248; -fx-border-width: 3;");
            }

            checkbox.setOnAction(e -> {
                if (checkbox.isSelected()) {
                    cacMonDaChon.add(item);
                    hopMonAn.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #DDB248; -fx-border-width: 3;");
                } else {
                    cacMonDaChon.remove(item);
                    hopMonAn.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #e0e0e0; -fx-border-width: 1;");
                }
                capNhatLabelDaChon();
            });

            hopMonAn.setPickOnBounds(true);
            hopMonAn.setOnMouseClicked(event -> {
                checkbox.setSelected(!checkbox.isSelected());
                checkbox.fire();
            });

            wrapper.getChildren().addAll(hopMonAn, checkbox);

            int col = i % soCotMoiHang;
            int row = i / soCotMoiHang;
            gridViewPane.add(wrapper, col, row);
        }

        int count = danhSachMonAnHienThi.size();
        lblItemCount.setText("Hiển thị " + count + " món ăn");
    }

    private void capNhatLabelDaChon() {
        lblDaChon.setText("Đã chọn: " + cacMonDaChon.size() + " món");
    }

    public Set<MonAn> getCacMonDaChon() {
        return daXacNhan ? cacMonDaChon : null;
    }
}
