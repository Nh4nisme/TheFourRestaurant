package com.thefourrestaurant.view.components;

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

public abstract class CuaSoKhoiPhuc<T> extends Stage {

    protected List<T> danhSachDaXoa;
    protected List<T> danhSachHienThi;
    protected final Set<T> cacItemDaChon = new HashSet<>();

    protected final Pane viewPane;
    protected final Label lblItemCount = new Label();
    protected final ComboBox<String> cboLoaiFilter = new ComboBox<>();
    protected final TextField txtTimKiem = new TextField();
    protected final Label lblDaChon = new Label();

    protected boolean daXacNhan = false;

    public CuaSoKhoiPhuc(List<T> danhSachDaXoa, String tieuDe, String loaiItem) {
        this.danhSachDaXoa = new ArrayList<>(danhSachDaXoa);
        this.danhSachHienThi = new ArrayList<>(this.danhSachDaXoa);
        this.viewPane = createViewPane();

        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle(tieuDe);

        BorderPane layoutChinh = new BorderPane();
        layoutChinh.setStyle("-fx-background-color: #F5F5F5;");

        VBox header = new VBox(10);
        header.setStyle("-fx-background-color: #1E424D;");
        header.setPadding(new Insets(15));

        Label tieuDeLabel = new Label(tieuDe);
        tieuDeLabel.setStyle("-fx-text-fill: #D4A017; -fx-font-size: 18px; -fx-font-weight: bold;");

        lblDaChon.setText("Đã chọn: 0 " + loaiItem);
        lblDaChon.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        header.getChildren().addAll(tieuDeLabel, lblDaChon);

        HBox khungGiua = createMiddleBar(loaiItem);

        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(20));
        VBox.setVgrow(contentBox, Priority.ALWAYS);

        VBox container = new VBox(20);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        container.setAlignment(Pos.TOP_CENTER);
        container.setPadding(new Insets(20));
        VBox.setVgrow(container, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(viewPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        container.getChildren().add(scrollPane);
        contentBox.getChildren().add(container);

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

        locVaCapNhat();
    }

    private HBox createMiddleBar(String loaiItem) {
        HBox khungGiua = new HBox(10);
        khungGiua.setPadding(new Insets(10, 20, 10, 20));
        khungGiua.setAlignment(Pos.CENTER_LEFT);
        khungGiua.setStyle("-fx-background-color: #1E424D;");

        cboLoaiFilter.setPromptText("Lọc theo loại");
        List<String> danhSachLoai = layDanhSachLoai();
        cboLoaiFilter.getItems().add("Tất cả");
        cboLoaiFilter.getItems().addAll(danhSachLoai);
        cboLoaiFilter.setValue("Tất cả");
        cboLoaiFilter.setOnAction(e -> locVaCapNhat());

        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);

        txtTimKiem.setPromptText("Tìm...");
        txtTimKiem.setPrefWidth(300);

        ButtonSample btnTim = new ButtonSample("Tìm", "", 35, 13, 3);
        btnTim.setOnAction(event -> locVaCapNhat());
        txtTimKiem.setOnAction(event -> locVaCapNhat());

        khungGiua.getChildren().addAll(cboLoaiFilter, space, txtTimKiem, btnTim);
        return khungGiua;
    }

    protected void locVaCapNhat() {
        String tuKhoa = txtTimKiem.getText();
        String loaiFilter = cboLoaiFilter.getValue();

        List<T> filteredList = new ArrayList<>(danhSachDaXoa);

        if (loaiFilter != null && !loaiFilter.equals("Tất cả")) {
            filteredList = filteredList.stream()
                    .filter(item -> locTheoLoai(item, loaiFilter))
                    .collect(Collectors.toList());
        }

        if (tuKhoa != null && !tuKhoa.trim().isEmpty()) {
            String lowerCaseTuKhoa = tuKhoa.trim().toLowerCase();
            filteredList = filteredList.stream()
                    .filter(item -> timKiem(item, lowerCaseTuKhoa))
                    .collect(Collectors.toList());
        }

        danhSachHienThi = filteredList;
        danhSachHienThi.sort(getComparator());
        capNhatView();
    }

    protected void capNhatLabelDaChon(String loaiItem) {
        lblDaChon.setText("Đã chọn: " + cacItemDaChon.size() + " " + loaiItem);
    }

    protected VBox createItemWrapper(Pane itemBox, T item, String loaiItem) {
        VBox wrapper = new VBox();
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setSpacing(5);

        CheckBox checkbox = new CheckBox();
        checkbox.setSelected(cacItemDaChon.contains(item));
        checkbox.setStyle("-fx-font-size: 14px;");

        if (cacItemDaChon.contains(item)) {
            itemBox.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #DDB248; -fx-border-width: 3;");
        }

        checkbox.setOnAction(e -> {
            if (checkbox.isSelected()) {
                cacItemDaChon.add(item);
                itemBox.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #DDB248; -fx-border-width: 3;");
            } else {
                cacItemDaChon.remove(item);
                itemBox.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #e0e0e0; -fx-border-width: 1;");
            }
            capNhatLabelDaChon(loaiItem);
        });

        itemBox.setPickOnBounds(true);
        itemBox.setOnMouseClicked(event -> {
            checkbox.setSelected(!checkbox.isSelected());
            checkbox.fire();
        });

        wrapper.getChildren().addAll(itemBox, checkbox);
        return wrapper;
    }

    protected abstract Pane createViewPane();
    protected abstract List<String> layDanhSachLoai();
    protected abstract boolean locTheoLoai(T item, String loai);
    protected abstract boolean timKiem(T item, String tuKhoa);
    protected abstract Comparator<T> getComparator();
    protected abstract void capNhatView();

    public Set<T> getCacItemDaChon() {
        return daXacNhan ? cacItemDaChon : null;
    }
}
