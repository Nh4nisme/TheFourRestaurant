package com.thefourrestaurant.view.khuyenmai;

import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.model.LoaiKhuyenMai;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.util.List;

public class KhuyenMaiDialog extends Dialog<KhuyenMai> {

    private final KhuyenMai resultKm;

    private final TextField txtMoTa = new TextField();
    private final ComboBox<LoaiKhuyenMai> cboLoaiKM = new ComboBox<>();
    private final DatePicker dpNgayBatDau = new DatePicker();
    private final DatePicker dpNgayKetThuc = new DatePicker();
    private final TextField txtTyLe = new TextField();
    private final TextField txtSoTien = new TextField();

    public KhuyenMaiDialog(KhuyenMai km, List<LoaiKhuyenMai> allLoaiKM) {
        this.setTitle(km == null ? "Thêm Khuyến Mãi Mới" : "Sửa Khuyến Mãi");

        boolean isEdit = km != null;
        this.resultKm = isEdit ? km : new KhuyenMai();

        // Setup UI
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        cboLoaiKM.setItems(FXCollections.observableArrayList(allLoaiKM));

        grid.add(new Label("Mô tả:"), 0, 0);
        grid.add(txtMoTa, 1, 0);
        grid.add(new Label("Loại KM:"), 0, 1);
        grid.add(cboLoaiKM, 1, 1);
        grid.add(new Label("Ngày bắt đầu:"), 0, 2);
        grid.add(dpNgayBatDau, 1, 2);
        grid.add(new Label("Ngày kết thúc:"), 0, 3);
        grid.add(dpNgayKetThuc, 1, 3);
        grid.add(new Label("Tỷ lệ giảm (%):"), 0, 4);
        grid.add(txtTyLe, 1, 4);
        grid.add(new Label("Số tiền giảm (VND):"), 0, 5);
        grid.add(txtSoTien, 1, 5);

        content.getChildren().add(grid);
        getDialogPane().setContent(content);

        // Add buttons
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Populate data if editing
        if (isEdit) {
            populateData();
        }

        // Convert result when OK is clicked
        this.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return buildResult();
            }
            return null;
        });
    }

    private void populateData() {
        txtMoTa.setText(resultKm.getMoTa());
        cboLoaiKM.setValue(resultKm.getLoaiKhuyenMai());
        dpNgayBatDau.setValue(resultKm.getNgayBatDau());
        dpNgayKetThuc.setValue(resultKm.getNgayKetThuc());
        if (resultKm.getTyLe() != null) txtTyLe.setText(resultKm.getTyLe().toPlainString());
        if (resultKm.getSoTien() != null) txtSoTien.setText(resultKm.getSoTien().toPlainString());
    }

    private KhuyenMai buildResult() {
        resultKm.setMoTa(txtMoTa.getText());
        resultKm.setLoaiKhuyenMai(cboLoaiKM.getValue());
        resultKm.setNgayBatDau(dpNgayBatDau.getValue());
        resultKm.setNgayKetThuc(dpNgayKetThuc.getValue());
        try {
            if (txtTyLe.getText() != null && !txtTyLe.getText().isEmpty()) {
                resultKm.setTyLe(new BigDecimal(txtTyLe.getText()));
            } else {
                resultKm.setTyLe(null);
            }
            if (txtSoTien.getText() != null && !txtSoTien.getText().isEmpty()) {
                resultKm.setSoTien(new BigDecimal(txtSoTien.getText()));
            } else {
                resultKm.setSoTien(null);
            }
        } catch (NumberFormatException e) {
            // Handle error appropriately - maybe show an alert
            System.err.println("Invalid number format");
            return null; // Prevent closing with invalid data
        }
        return resultKm;
    }

    public KhuyenMai layKetQua() {
        return resultKm;
    }
}
