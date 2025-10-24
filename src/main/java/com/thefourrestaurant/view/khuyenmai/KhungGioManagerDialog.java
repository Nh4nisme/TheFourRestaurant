package com.thefourrestaurant.view.khuyenmai;

import com.thefourrestaurant.DAO.KhungGioDAO;
import com.thefourrestaurant.DAO.KhungGio_KM_DAO;
import com.thefourrestaurant.model.KhungGio;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KhungGioManagerDialog extends Stage {

    private final String maKM;
    private final KhungGioDAO khungGioDAO = new KhungGioDAO();
    private final KhungGio_KM_DAO khungGio_KM_DAO = new KhungGio_KM_DAO();

    private final ObservableList<KhungGio> danhSachKhungGioHienCo = FXCollections.observableArrayList();
    private final ListView<KhungGio> listView = new ListView<>();

    public KhungGioManagerDialog(String maKM) {
        this.maKM = maKM;

        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Quản Lý Khung Giờ cho Khuyến Mãi: " + maKM);

        BorderPane layoutChinh = new BorderPane();

        // Top: Title
        Label lblTitle = new Label("Quản Lý Khung Giờ cho KM: " + maKM);
        lblTitle.setStyle("-fx-text-fill: #D4A017; -fx-font-size: 18px; -fx-font-weight: bold;");
        HBox titleBox = new HBox(lblTitle);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        titleBox.setPadding(new Insets(15));
        titleBox.setStyle("-fx-background-color: #1E424D;");
        layoutChinh.setTop(titleBox);

        // Center: List of existing KhungGio and a form to add new ones
        VBox centerBox = new VBox(15);
        centerBox.setPadding(new Insets(10));

        // List view for existing KhungGio
        listView.setItems(danhSachKhungGioHienCo);
        listView.setCellFactory(param -> new ListCell<KhungGio>() {
            @Override
            protected void updateItem(KhungGio item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("Từ %s đến %s - Lặp lại: %s",
                            item.getGioBatDau(), item.getGioKetThuc(), item.isLapLaiHangNgay() ? "Có" : "Không"));
                }
            }
        });
        centerBox.getChildren().add(new Label("Khung giờ đang áp dụng:"));
        centerBox.getChildren().add(listView);

        // Form to add a new KhungGio
        GridPane addForm = new GridPane();
        addForm.setHgap(10);
        addForm.setVgap(10);

        // --- Start Time ---
        ComboBox<String> cboGioBatDauGio = new ComboBox<>(FXCollections.observableArrayList(taoDanhSachGio()));
        cboGioBatDauGio.setPromptText("Giờ");
        cboGioBatDauGio.getStyleClass().add("combo-box");
        ComboBox<String> cboGioBatDauPhut = new ComboBox<>(FXCollections.observableArrayList(taoDanhSachPhut()));
        cboGioBatDauPhut.setPromptText("Phút");
        cboGioBatDauPhut.getStyleClass().add("combo-box");
        HBox boxGioBatDau = new HBox(5, cboGioBatDauGio, new Label(":"), cboGioBatDauPhut);
        boxGioBatDau.setAlignment(Pos.CENTER_LEFT);

        // --- End Time ---
        ComboBox<String> cboGioKetThucGio = new ComboBox<>(FXCollections.observableArrayList(taoDanhSachGio()));
        cboGioKetThucGio.setPromptText("Giờ");
        cboGioKetThucGio.getStyleClass().add("combo-box");
        ComboBox<String> cboGioKetThucPhut = new ComboBox<>(FXCollections.observableArrayList(taoDanhSachPhut()));
        cboGioKetThucPhut.setPromptText("Phút");
        cboGioKetThucPhut.getStyleClass().add("combo-box");
        HBox boxGioKetThuc = new HBox(5, cboGioKetThucGio, new Label(":"), cboGioKetThucPhut);
        boxGioKetThuc.setAlignment(Pos.CENTER_LEFT);

        CheckBox chkLapLai = new CheckBox("Lặp lại hàng ngày");
        ButtonSample btnThem = new ButtonSample("Thêm", 35, 14, 2);

        addForm.add(new Label("Giờ bắt đầu:"), 0, 0);
        addForm.add(boxGioBatDau, 1, 0);
        addForm.add(new Label("Giờ kết thúc:"), 0, 1);
        addForm.add(boxGioKetThuc, 1, 1);
        addForm.add(chkLapLai, 1, 2);
        addForm.add(btnThem, 1, 3);

        centerBox.getChildren().add(new Separator());
        centerBox.getChildren().add(new Label("Thêm khung giờ mới:"));
        centerBox.getChildren().add(addForm);

        layoutChinh.setCenter(centerBox);

        // Bottom: Close button
        HBox bottomBox = new HBox();
        bottomBox.setPadding(new Insets(10));
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        ButtonSample btnDong = new ButtonSample("Đóng", 35, 14, 2);
        bottomBox.getChildren().add(btnDong);
        layoutChinh.setBottom(bottomBox);

        // Event Handlers
        btnDong.setOnAction(e -> this.close());
        btnThem.setOnAction(e -> {
            try {
                String gioBD = cboGioBatDauGio.getValue();
                String phutBD = cboGioBatDauPhut.getValue();
                String gioKT = cboGioKetThucGio.getValue();
                String phutKT = cboGioKetThucPhut.getValue();

                if (gioBD == null || phutBD == null || gioKT == null || phutKT == null) {
                    new Alert(Alert.AlertType.WARNING, "Vui lòng chọn đầy đủ giờ và phút!").showAndWait();
                    return;
                }

                LocalTime gioBatDau = LocalTime.of(Integer.parseInt(gioBD), Integer.parseInt(phutBD));
                LocalTime gioKetThuc = LocalTime.of(Integer.parseInt(gioKT), Integer.parseInt(phutKT));
                boolean lapLai = chkLapLai.isSelected();

                if (gioKetThuc.isBefore(gioBatDau)) {
                    new Alert(Alert.AlertType.WARNING, "Giờ kết thúc không được trước giờ bắt đầu!").showAndWait();
                    return;
                }

                KhungGio kg = new KhungGio(khungGioDAO.taoMaKhungGioMoi(), gioBatDau, gioKetThuc, lapLai);
                if (khungGioDAO.themKhungGio(kg)) {
                    if (khungGio_KM_DAO.themKhungGio_KM(kg.getMaTG(), this.maKM)) {
                        loadKhungGioHienCo(); // Refresh the list
                    }
                }
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Đã có lỗi xảy ra!").showAndWait();
                ex.printStackTrace();
            }
        });

        // Context menu for deletion
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Xóa");
        deleteItem.setOnAction(e -> {
            KhungGio selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (khungGio_KM_DAO.xoaKhungGio_KM(selected.getMaTG(), this.maKM)) {
                    loadKhungGioHienCo(); // Refresh the list
                }
            }
        });
        contextMenu.getItems().add(deleteItem);
        listView.setContextMenu(contextMenu);

        // Initial data load
        loadKhungGioHienCo();

        Scene scene = new Scene(layoutChinh, 450, 500);
        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            scene.getStylesheets().add(urlCSS.toExternalForm());
        } else {
            System.err.println("Không tìm thấy tệp CSS.");
        }
        this.setScene(scene);
    }

    private void loadKhungGioHienCo() {
        List<KhungGio> danhSach = khungGio_KM_DAO.layKhungGioTheoMaKM(this.maKM);
        danhSachKhungGioHienCo.setAll(danhSach);
    }

    private List<String> taoDanhSachGio() {
        List<String> ds = new ArrayList<>();
        for (int i = 0; i <= 23; i++) {
            ds.add(String.format("%02d", i));
        }
        return ds;
    }

    private List<String> taoDanhSachPhut() {
        List<String> ds = new ArrayList<>();
        for (int i = 0; i <= 59; i++) {
            ds.add(String.format("%02d", i));
        }
        return ds;
    }
}
