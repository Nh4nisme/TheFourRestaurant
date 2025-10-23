package com.thefourrestaurant.view.khuyenmai;

import com.thefourrestaurant.model.ChiTietKhuyenMai;
import com.thefourrestaurant.model.MonAn;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ChiTietKhuyenMaiDialog extends Dialog<Optional<ChiTietKhuyenMai>> {

    private final ChiTietKhuyenMai chiTiet;
    private final boolean isEdit;

    private final ComboBox<MonAn> cboMonApDung = new ComboBox<>();
    private final TextField txtTyLeGiam = new TextField();
    private final TextField txtSoTienGiam = new TextField();
    private final ComboBox<MonAn> cboMonTang = new ComboBox<>();
    private final TextField txtSoLuongTang = new TextField();

    public ChiTietKhuyenMaiDialog(ChiTietKhuyenMai chiTiet, List<MonAn> allMonAn) {
        this.isEdit = chiTiet != null;
        this.chiTiet = isEdit ? chiTiet : new ChiTietKhuyenMai();

        setTitle(isEdit ? "Sửa Chi Tiết Khuyến Mãi" : "Thêm Chi Tiết Khuyến Mãi");

        // Setup UI
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Converters and ComboBox setup
        StringConverter<MonAn> monAnConverter = new StringConverter<>() {
            @Override
            public String toString(MonAn monAn) {
                return monAn == null ? "(Không có)" : monAn.getTenMon();
            }
            @Override
            public MonAn fromString(String string) {
                return null;
            }
        };

        cboMonApDung.setItems(FXCollections.observableArrayList(allMonAn));
        cboMonApDung.setConverter(monAnConverter);

        // Add a null option for monTang
        List<MonAn> monTangOptions = FXCollections.observableArrayList(allMonAn);
        monTangOptions.add(0, null);
        cboMonTang.setItems(FXCollections.observableArrayList(monTangOptions));
        cboMonTang.setConverter(monAnConverter);

        // Layout
        grid.add(new Label("Món áp dụng:"), 0, 0);
        grid.add(cboMonApDung, 1, 0);
        grid.add(new Label("Tỷ lệ giảm (%):"), 0, 1);
        grid.add(txtTyLeGiam, 1, 1);
        grid.add(new Label("Số tiền giảm (VND):"), 0, 2);
        grid.add(txtSoTienGiam, 1, 2);
        grid.add(new Label("Món tặng:"), 0, 3);
        grid.add(cboMonTang, 1, 3);
        grid.add(new Label("Số lượng tặng:"), 0, 4);
        grid.add(txtSoLuongTang, 1, 4);

        getDialogPane().setContent(grid);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Enable/disable fields based on input
        cboMonTang.valueProperty().addListener((obs, old, aNew) -> txtSoLuongTang.setDisable(aNew == null));
        txtSoLuongTang.setDisable(cboMonTang.getValue() == null);

        if (isEdit) {
            populateData();
        }

        this.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                if (validateAndBuild()) {
                    return Optional.of(this.chiTiet);
                }
            }
            return Optional.empty();
        });
    }

    private void populateData() {
        cboMonApDung.setValue(chiTiet.getMonAnApDung());
        cboMonTang.setValue(chiTiet.getMonAnTang());
        if (chiTiet.getTyLeGiam() != null) txtTyLeGiam.setText(chiTiet.getTyLeGiam().toPlainString());
        if (chiTiet.getSoTienGiam() != null) txtSoTienGiam.setText(chiTiet.getSoTienGiam().toPlainString());
        if (chiTiet.getSoLuongTang() != null) txtSoLuongTang.setText(chiTiet.getSoLuongTang().toString());
    }

    private boolean validateAndBuild() {
        if (cboMonApDung.getValue() == null) {
            showAlert("Món áp dụng không được để trống.");
            return false;
        }
        chiTiet.setMonAnApDung(cboMonApDung.getValue());

        try {
            chiTiet.setTyLeGiam(parseBigDecimal(txtTyLeGiam.getText()));
            chiTiet.setSoTienGiam(parseBigDecimal(txtSoTienGiam.getText()));

            MonAn monTang = cboMonTang.getValue();
            chiTiet.setMonAnTang(monTang);

            if (monTang != null) {
                chiTiet.setSoLuongTang(parseInteger(txtSoLuongTang.getText()));
                if (chiTiet.getSoLuongTang() == null || chiTiet.getSoLuongTang() <= 0) {
                    showAlert("Số lượng tặng phải là một số nguyên dương.");
                    return false;
                }
            } else {
                chiTiet.setSoLuongTang(null);
            }

            return true;

        } catch (NumberFormatException e) {
            showAlert("Các giá trị số (tỷ lệ, số tiền, số lượng) không hợp lệ.");
            return false;
        }
    }

    private BigDecimal parseBigDecimal(String text) {
        return (text == null || text.isEmpty()) ? null : new BigDecimal(text);
    }

    private Integer parseInteger(String text) {
        return (text == null || text.isEmpty()) ? null : Integer.parseInt(text);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Dữ liệu không hợp lệ");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Optional<ChiTietKhuyenMai> getResult() {
        return showAndWait().orElse(Optional.empty());
    }
}
