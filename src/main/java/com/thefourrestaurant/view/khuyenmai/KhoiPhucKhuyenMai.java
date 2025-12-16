package com.thefourrestaurant.view.khuyenmai;

import com.thefourrestaurant.DAO.LoaiKhuyenMaiDAO;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.model.LoaiKhuyenMai;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class KhoiPhucKhuyenMai extends Stage {

    private final List<KhuyenMai> danhSachKMDaXoa;
    private List<KhuyenMai> danhSachKMHienThi;

    private final Set<KhuyenMai> cacKMDaChon = new HashSet<>();
    private final FlowPane gridViewPane = new FlowPane(15, 15);
    private final Label lblItemCount = new Label();
    private final ComboBox<String> cboLoaiKMFilter = new ComboBox<>();
    private final TextField txtTimKiem = new TextField();
    private final Label lblDaChon = new Label("Đã chọn: 0 khuyến mãi");

    private boolean daXacNhan = false;

    public KhoiPhucKhuyenMai(List<KhuyenMai> danhSachKMDaXoa) {
        this.danhSachKMDaXoa = new ArrayList<>(danhSachKMDaXoa);
        this.danhSachKMHienThi = new ArrayList<>(this.danhSachKMDaXoa);

        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Khôi phục khuyến mãi");

        BorderPane layoutChinh = new BorderPane();
        layoutChinh.setStyle("-fx-background-color: #F5F5F5;");

        VBox header = new VBox(10);
        header.setStyle("-fx-background-color: #1E424D;");
        header.setPadding(new Insets(15));

        Label tieuDe = new Label("Khôi phục khuyến mãi đã xóa");
        tieuDe.setStyle("-fx-text-fill: #D4A017; -fx-font-size: 18px; -fx-font-weight: bold;");

        lblDaChon.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        capNhatLabelDaChon();

        header.getChildren().addAll(tieuDe, lblDaChon);

        HBox khungGiua = createMiddleBar();

        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(20));
        VBox.setVgrow(contentBox, Priority.ALWAYS);

        VBox dsKMContainer = new VBox(20);
        dsKMContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        dsKMContainer.setAlignment(Pos.TOP_CENTER);
        dsKMContainer.setPadding(new Insets(20));
        VBox.setVgrow(dsKMContainer, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(gridViewPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        dsKMContainer.getChildren().add(scrollPane);
        contentBox.getChildren().add(dsKMContainer);

        HBox statusBar = new HBox();
        statusBar.setPadding(new Insets(5, 20, 5, 20));
        statusBar.setAlignment(Pos.CENTER_LEFT);
        lblItemCount.setStyle("-fx-text-fill: #333333; -fx-font-size: 12px;");
        statusBar.getChildren().add(lblItemCount);

        HBox footer = new HBox(10);
        footer.setPadding(new Insets(15));
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setStyle("-fx-background-color: #F0F0F0; -fx-border-color: #E0E0E0; -fx-border-width: 1 0 0 0;");

        ButtonSample btnKhoiPhuc = new ButtonSample("Khôi phục", 35, 14, 2);
        btnKhoiPhuc.setOnAction(e -> {
            daXacNhan = true;
            this.close();
        });

        ButtonSample btnHuy = new ButtonSample("Hủy", 35, 14, 2);
        btnHuy.setOnAction(e -> this.close());

        footer.getChildren().addAll(btnKhoiPhuc, btnHuy);

        layoutChinh.setTop(header);
        layoutChinh.setCenter(new VBox(khungGiua, contentBox, statusBar));
        layoutChinh.setBottom(footer);

        Scene scene = new Scene(layoutChinh, 900, 650);
        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            scene.getStylesheets().add(urlCSS.toExternalForm());
        }
        this.setScene(scene);

        locVaCapNhatKhuyenMai();
    }

    private HBox createMiddleBar() {
        HBox khungGiua = new HBox(10);
        khungGiua.setPadding(new Insets(10, 20, 10, 20));
        khungGiua.setAlignment(Pos.CENTER_LEFT);
        khungGiua.setStyle("-fx-background-color: #1E424D;");

        cboLoaiKMFilter.setPromptText("Lọc theo loại");
        LoaiKhuyenMaiDAO loaiKhuyenMaiDAO = new LoaiKhuyenMaiDAO();
        List<String> tenLoaiKM = loaiKhuyenMaiDAO.layTatCaLoaiKhuyenMai().stream()
                .map(LoaiKhuyenMai::getTenLoaiKM)
                .collect(Collectors.toList());
        cboLoaiKMFilter.getItems().add("Tất cả");
        cboLoaiKMFilter.getItems().addAll(tenLoaiKM);
        cboLoaiKMFilter.setValue("Tất cả");
        cboLoaiKMFilter.setOnAction(e -> locVaCapNhatKhuyenMai());

        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);

        txtTimKiem.setPromptText("Tìm...");
        txtTimKiem.setPrefWidth(300);

        ButtonSample btnTim = new ButtonSample("Tìm", "", 35, 13, 3);
        btnTim.setOnAction(event -> locVaCapNhatKhuyenMai());
        txtTimKiem.setOnAction(event -> locVaCapNhatKhuyenMai());

        khungGiua.getChildren().addAll(cboLoaiKMFilter, space, txtTimKiem, btnTim);
        return khungGiua;
    }

    private void locVaCapNhatKhuyenMai() {
        String tuKhoa = txtTimKiem.getText();
        String loaiKMFilter = cboLoaiKMFilter.getValue();

        List<KhuyenMai> filteredList = new ArrayList<>(danhSachKMDaXoa);

        if (loaiKMFilter != null && !loaiKMFilter.equals("Tất cả")) {
            filteredList = filteredList.stream()
                    .filter(km -> km.getLoaiKhuyenMai() != null && km.getLoaiKhuyenMai().getTenLoaiKM().equals(loaiKMFilter))
                    .collect(Collectors.toList());
        }

        if (tuKhoa != null && !tuKhoa.trim().isEmpty()) {
            String lowerCaseTuKhoa = tuKhoa.trim().toLowerCase();
            filteredList = filteredList.stream()
                    .filter(km -> km.getTenKM().toLowerCase().contains(lowerCaseTuKhoa) ||
                            km.getMaKM().toLowerCase().contains(lowerCaseTuKhoa))
                    .collect(Collectors.toList());
        }

        danhSachKMHienThi = filteredList;
        danhSachKMHienThi.sort(Comparator.comparing(KhuyenMai::getTenKM));
        updateGridView();
    }

    private void updateGridView() {
        gridViewPane.getChildren().clear();

        for (KhuyenMai item : danhSachKMHienThi) {
            VBox wrapper = new VBox();
            wrapper.setAlignment(Pos.CENTER);
            wrapper.setSpacing(5);

            KhuyenMaiBox hopKM = new KhuyenMaiBox(item);

            CheckBox checkbox = new CheckBox();
            checkbox.setSelected(cacKMDaChon.contains(item));
            checkbox.setStyle("-fx-font-size: 14px;");

            if (cacKMDaChon.contains(item)) {
                hopKM.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #DDB248; -fx-border-width: 3;");
            }

            checkbox.setOnAction(e -> {
                if (checkbox.isSelected()) {
                    cacKMDaChon.add(item);
                    hopKM.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #DDB248; -fx-border-width: 3;");
                } else {
                    cacKMDaChon.remove(item);
                    hopKM.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #e0e0e0; -fx-border-width: 1;");
                }
                capNhatLabelDaChon();
            });

            hopKM.setPickOnBounds(true);
            hopKM.setOnMouseClicked(event -> {
                checkbox.setSelected(!checkbox.isSelected());
                checkbox.fire();
            });

            wrapper.getChildren().addAll(hopKM, checkbox);
            gridViewPane.getChildren().add(wrapper);
        }

        int count = danhSachKMHienThi.size();
        lblItemCount.setText("Hiển thị " + count + " khuyến mãi đã xóa");
    }

    private void capNhatLabelDaChon() {
        lblDaChon.setText("Đã chọn: " + cacKMDaChon.size() + " khuyến mãi");
    }

    public Set<KhuyenMai> getCacKMDaChon() {
        return daXacNhan ? cacKMDaChon : null;
    }
}
