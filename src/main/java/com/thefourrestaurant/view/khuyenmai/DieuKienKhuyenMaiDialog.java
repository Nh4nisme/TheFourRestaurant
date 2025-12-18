package com.thefourrestaurant.view.khuyenmai;

import com.thefourrestaurant.controller.KhuyenMaiController;
import com.thefourrestaurant.model.DieuKien_Mon;
import com.thefourrestaurant.model.DieuKien_MonTang;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.model.KhuyenMai_DieuKien;
import com.thefourrestaurant.model.MonAn;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DieuKienKhuyenMaiDialog extends Stage {

    private boolean daLuu = false;
    private final KhuyenMai_DieuKien dieuKien;
    private final boolean isEditMode;
    private final KhuyenMaiController boDieuKhien;
    private final List<MonAn> tatCaMonAn;
    private final KhuyenMai khuyenMaiCha;

    private ComboBox<String> cboLoaiApDung;
    private TextField txtMoTa;
    private TextField txtMonMua;
    private TextField txtMonNhanGiam;
    private TextField txtMonTang;
    private TextField txtTyLeGiam;
    private TextField txtSoTienGiam;
    private TextField txtSoLuongTang;
    private Spinner<Integer> spinnerSoLuongMua;

    private Set<MonAn> dsMonMuaChon = new HashSet<>();
    private Set<MonAn> dsMonNhanGiamChon = new HashSet<>();
    private Set<MonAn> dsMonTangChon = new HashSet<>();
    
    private Node rowMonMua, rowMonNhanGiam, rowMonTang, rowTyLe, rowSoTien, rowSoLuongTang;
    private Label lblMonMua, lblMonNhanGiam, lblMonTang, lblTyLe, lblSoTien, lblSoLuongTang;

    public DieuKienKhuyenMaiDialog(KhuyenMai_DieuKien dieuKien, KhuyenMai khuyenMaiCha, KhuyenMaiController boDieuKhien) {
        this.isEditMode = (dieuKien != null);
        this.dieuKien = isEditMode ? dieuKien : new KhuyenMai_DieuKien();
        this.khuyenMaiCha = khuyenMaiCha;
        if (!isEditMode) {
            this.dieuKien.setKhuyenMai(khuyenMaiCha);
        }
        this.boDieuKhien = boDieuKhien;
        this.tatCaMonAn = boDieuKhien.layDanhSachMonAn();

        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle(isEditMode ? "Sửa Điều Kiện Khuyến Mãi" : "Thêm Điều Kiện Khuyến Mãi");

        BorderPane layoutChinh = new BorderPane();
        layoutChinh.setPadding(new Insets(20));
        
        layoutChinh.setCenter(taoFormChinh());
        layoutChinh.setBottom(taoChanTrang());

        if (isEditMode) {
            dienDuLieuHienCo();
        }
        
        if (khuyenMaiCha.laKieuMaGiamGia()) {
            cboLoaiApDung.setValue("GIAM_TRUC_TIEP");
            cboLoaiApDung.setDisable(true);
        } else if (!isEditMode) {
            cboLoaiApDung.getSelectionModel().selectFirst();
        }
        
        capNhatFormTheoLoai();
        apDungRangBuocKhuyenMaiCha();

        Scene scene = new Scene(layoutChinh, 650, 500);
        this.setScene(scene);
    }

    private GridPane taoFormChinh() {
        GridPane form = new GridPane();
        form.setVgap(12);
        form.setHgap(10);

        cboLoaiApDung = new ComboBox<>();
        cboLoaiApDung.getItems().addAll("GIAM_TRUC_TIEP", "THEO_COMBO", "MUA_X_GIAM_Y");
        cboLoaiApDung.valueProperty().addListener((obs, oldVal, newVal) -> capNhatFormTheoLoai());

        txtMoTa = new TextField();
        txtMonMua = taoTruongChiDoc("Chọn món mua/áp dụng (để trống nếu áp dụng toàn hóa đơn)");
        txtMonNhanGiam = taoTruongChiDoc("Chọn món được giảm giá...");
        txtMonTang = taoTruongChiDoc("Chọn món được tặng...");
        txtTyLeGiam = new TextField();
        txtSoTienGiam = new TextField();
        txtSoLuongTang = new TextField();
        spinnerSoLuongMua = new Spinner<>(1, 100, 1);

        int row = 0;
        form.add(new Label("Loại áp dụng:"), 0, row);
        form.add(cboLoaiApDung, 1, row++);
        
        form.add(new Label("Mô tả điều kiện:"), 0, row);
        form.add(txtMoTa, 1, row++);

        lblMonMua = new Label("Món mua/áp dụng:");
        rowMonMua = taoBoxChonMon(txtMonMua, () -> moDialogChonMon(dsMonMuaChon, txtMonMua));
        form.add(lblMonMua, 0, row);
        form.add(rowMonMua, 1, row++);

        lblMonNhanGiam = new Label("Món nhận giảm:");
        rowMonNhanGiam = taoBoxChonMon(txtMonNhanGiam, () -> moDialogChonMon(dsMonNhanGiamChon, txtMonNhanGiam));
        form.add(lblMonNhanGiam, 0, row);
        form.add(rowMonNhanGiam, 1, row++);

        lblMonTang = new Label("Món tặng:");
        rowMonTang = taoBoxChonMon(txtMonTang, () -> moDialogChonMon(dsMonTangChon, txtMonTang));
        form.add(lblMonTang, 0, row);
        form.add(rowMonTang, 1, row++);

        lblTyLe = new Label("Tỷ lệ giảm (%):");
        rowTyLe = txtTyLeGiam;
        form.add(lblTyLe, 0, row);
        form.add(rowTyLe, 1, row++);

        lblSoTien = new Label("Số tiền giảm:");
        rowSoTien = txtSoTienGiam;
        form.add(lblSoTien, 0, row);
        form.add(rowSoTien, 1, row++);
        
        lblSoLuongTang = new Label("Số lượng tặng:");
        rowSoLuongTang = txtSoLuongTang;
        form.add(lblSoLuongTang, 0, row);
        form.add(rowSoLuongTang, 1, row++);

        return form;
    }
    
    private TextField taoTruongChiDoc(String prompt) {
        TextField tf = new TextField();
        tf.setEditable(false);
        tf.setPromptText(prompt);
        return tf;
    }

    private HBox taoBoxChonMon(TextField textField, Runnable action) {
        Button btnChon = new ButtonSample("Chọn", 35, 13, 2);
        btnChon.setOnAction(e -> action.run());
        HBox hbox = new HBox(5, textField, btnChon);
        HBox.setHgrow(textField, Priority.ALWAYS);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    private void capNhatFormTheoLoai() {
        String loai = cboLoaiApDung.getValue();
        if (loai == null) return;

        quanLyHienThi(true, lblMonMua, rowMonMua, lblTyLe, rowTyLe, lblSoTien, rowSoTien, lblMonTang, rowMonTang, lblSoLuongTang, rowSoLuongTang, lblMonNhanGiam, rowMonNhanGiam);
        
        switch (loai) {
            case "GIAM_TRUC_TIEP":
                quanLyHienThi(false, lblMonTang, rowMonTang, lblSoLuongTang, rowSoLuongTang, lblMonNhanGiam, rowMonNhanGiam);
                break;
            case "THEO_COMBO":
                quanLyHienThi(false, lblMonTang, rowMonTang, lblSoLuongTang, rowSoLuongTang, lblMonNhanGiam, rowMonNhanGiam);
                break;
            case "MUA_X_GIAM_Y":
                quanLyHienThi(true, lblMonNhanGiam, rowMonNhanGiam);
                quanLyHienThi(false, lblMonTang, rowMonTang, lblSoLuongTang, rowSoLuongTang);
                break;
            default:
                break;
        }
        
        apDungRangBuocKhuyenMaiCha();
    }
    
    private void apDungRangBuocKhuyenMaiCha() {
        if (khuyenMaiCha != null && khuyenMaiCha.getLoaiKhuyenMai() != null) {
            String tenLoaiKM = khuyenMaiCha.getLoaiKhuyenMai().getTenLoaiKM();
            
            boolean isGiamGiaTyLe = "Giảm giá theo tỷ lệ".equals(tenLoaiKM);
            boolean isGiamGiaSoTien = "Giảm giá theo số tiền".equals(tenLoaiKM);
            boolean isTangMon = "Tặng món".equals(tenLoaiKM);

            txtTyLeGiam.setDisable(!isGiamGiaTyLe);
            txtSoTienGiam.setDisable(!isGiamGiaSoTien);
            
            boolean monTangEnabled = isTangMon && rowMonTang.isVisible();
            txtSoLuongTang.setDisable(!monTangEnabled);
            if (rowMonTang instanceof HBox) {
                ((HBox) rowMonTang).getChildren().forEach(node -> node.setDisable(!monTangEnabled));
            }
        }
    }
    
    private void quanLyHienThi(boolean visible, Node... nodes) {
        for (Node node : nodes) {
            node.setManaged(visible);
            node.setVisible(visible);
        }
    }

    private HBox taoChanTrang() {
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        ButtonSample btnLuu = new ButtonSample("Lưu", 35, 14, 2);
        btnLuu.setOnAction(e -> luuDieuKien());
        ButtonSample btnHuy = new ButtonSample("Hủy", 35, 14, 2);
        btnHuy.setOnAction(e -> close());
        buttonBox.getChildren().addAll(btnLuu, btnHuy);
        return buttonBox;
    }
    
    private void dienDuLieuHienCo() {
        cboLoaiApDung.setValue(dieuKien.getLoaiApDung());
        txtMoTa.setText(dieuKien.getMoTaDieuKien());
        
        dieuKien.getDanhSachMonDieuKien().forEach(dkm -> {
            if ("MUA".equals(dkm.getVaiTro()) || "GIAM_TRUC_TIEP".equals(dkm.getVaiTro())) {
                dsMonMuaChon.add(dkm.getMonAn());
            } else if ("NHAN_GIAM".equals(dkm.getVaiTro())) {
                dsMonNhanGiamChon.add(dkm.getMonAn());
            }
        });
        
        dieuKien.getDanhSachMonTang().forEach(dkmt -> dsMonTangChon.add(dkmt.getMonAnTang()));

        capNhatHienThiMon(txtMonMua, dsMonMuaChon);
        capNhatHienThiMon(txtMonNhanGiam, dsMonNhanGiamChon);
        capNhatHienThiMon(txtMonTang, dsMonTangChon);
        
        if (dieuKien.getTyLeGiam() != null) txtTyLeGiam.setText(dieuKien.getTyLeGiam().toPlainString());
        if (dieuKien.getSoTienGiam() != null) txtSoTienGiam.setText(dieuKien.getSoTienGiam().toPlainString());
        if (dieuKien.getSoLuongTang() != null) txtSoLuongTang.setText(dieuKien.getSoLuongTang().toString());
    }

    private void moDialogChonMon(Set<MonAn> dsHienTai, TextField truongHienThi) {
        ChonMonAnDialog dialog = new ChonMonAnDialog(tatCaMonAn, dsHienTai, true);
        dialog.initOwner(this);
        dialog.showAndWait();

        Set<MonAn> ketQua = dialog.getCacMonDaChon();
        if (ketQua != null) {
            dsHienTai.clear();
            dsHienTai.addAll(ketQua);
            capNhatHienThiMon(truongHienThi, dsHienTai);
        }
    }
    
    private void capNhatHienThiMon(TextField truongHienThi, Set<MonAn> dsMon) {
        String danhSachTen = dsMon.stream()
                .map(MonAn::getTenMon)
                .sorted()
                .collect(Collectors.joining(", "));
        truongHienThi.setText(danhSachTen);
    }

    private void luuDieuKien() {
        if (cboLoaiApDung.getValue() == null || txtMoTa.getText().trim().isEmpty()) {
            hienThiThongBao(Alert.AlertType.WARNING, "Vui lòng chọn Loại áp dụng và nhập Mô tả.");
            return;
        }

        // For MaGiamGia, it's okay to have no items selected (applies to whole bill)
        // For other types, at least one item must be selected.
        if (!khuyenMaiCha.laKieuMaGiamGia() && dsMonMuaChon.isEmpty()) {
            hienThiThongBao(Alert.AlertType.WARNING, "Vui lòng chọn ít nhất một món áp dụng cho loại khuyến mãi này.");
            return;
        }

        dieuKien.setLoaiApDung(cboLoaiApDung.getValue());
        dieuKien.setMoTaDieuKien(txtMoTa.getText().trim());

        try {
            dieuKien.setTyLeGiam(txtTyLeGiam.getText().trim().isEmpty() ? null : new BigDecimal(txtTyLeGiam.getText().trim()));
            dieuKien.setSoTienGiam(txtSoTienGiam.getText().trim().isEmpty() ? null : new BigDecimal(txtSoTienGiam.getText().trim()));
            dieuKien.setSoLuongTang(txtSoLuongTang.getText().trim().isEmpty() ? null : Integer.parseInt(txtSoLuongTang.getText().trim()));
        } catch (NumberFormatException e) {
            hienThiThongBao(Alert.AlertType.ERROR, "Giá trị số không hợp lệ.");
            return;
        }
        
        List<DieuKien_Mon> monDKList = new ArrayList<>();
        String vaiTroMua = "GIAM_TRUC_TIEP".equals(dieuKien.getLoaiApDung()) ? "GIAM_TRUC_TIEP" : "MUA";
        for (MonAn mon : dsMonMuaChon) {
            DieuKien_Mon dkm = new DieuKien_Mon();
            dkm.setMonAn(mon);
            dkm.setSoLuong(1);
            dkm.setVaiTro(vaiTroMua);
            monDKList.add(dkm);
        }
        for (MonAn mon : dsMonNhanGiamChon) {
            DieuKien_Mon dkm = new DieuKien_Mon();
            dkm.setMonAn(mon);
            dkm.setSoLuong(1);
            dkm.setVaiTro("NHAN_GIAM");
            monDKList.add(dkm);
        }
        dieuKien.setDanhSachMonDieuKien(monDKList);

        List<DieuKien_MonTang> monTangList = dsMonTangChon.stream().map(mon -> {
            DieuKien_MonTang dkmt = new DieuKien_MonTang();
            dkmt.setMonAnTang(mon);
            return dkmt;
        }).collect(Collectors.toList());
        dieuKien.setDanhSachMonTang(monTangList);

        this.daLuu = true;
        close();
    }

    public boolean daLuu() { return daLuu; }
    public KhuyenMai_DieuKien getDieuKien() { return dieuKien; }
    
    private void hienThiThongBao(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(type == Alert.AlertType.ERROR ? "Lỗi" : "Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(this);
        alert.showAndWait();
    }
}
