package com.thefourrestaurant.view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Optional;

public abstract class GiaoDienThucThe extends VBox {

    protected TableView<?> tableChinh;
    protected Node chiTietNode;
    protected Label lblTieuDe;

    protected HBox khuVucBoLoc;
    protected TextField txtTimKiem;
    protected DatePicker dpNgayCuThe, dpTuNgay, dpDenNgay;
    protected ButtonSample btnTimKiem, btnLamMoi;

    private final String tieuDe;

    public GiaoDienThucThe(String tieuDe, Node chiTietNode) {
        this.tieuDe = tieuDe;
        this.chiTietNode = chiTietNode;

        getStyleClass().add("giao-dien-co-chi-tiet");
        setVgrow(this, Priority.ALWAYS);
        getStylesheets().add(
                getClass().getResource("/com/thefourrestaurant/css/Application.css").toExternalForm()
        );
    }

    protected void khoiTaoGiaoDien() {
        HBox toolbar = taoToolbar();
        SplitPane splitPane = taoSplitPane();

        VBox.setVgrow(splitPane, Priority.ALWAYS);
        getChildren().addAll(toolbar, splitPane);
    }

    private HBox taoToolbar() {
        HBox toolbar = new HBox(10);
        toolbar.getStyleClass().add("toolbar");
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(10));

        lblTieuDe = new Label(tieuDe);
        lblTieuDe.getStyleClass().add("toolbar-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        khuVucBoLoc = new HBox(10);
        khuVucBoLoc.setAlignment(Pos.CENTER_RIGHT);

        toolbar.getChildren().addAll(lblTieuDe, spacer, khuVucBoLoc);
        return toolbar;
    }

    private SplitPane taoSplitPane() {
        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.6);

        tableChinh = taoBangChinh();
        VBox bang = new VBox(tableChinh);
        VBox.setVgrow(tableChinh, Priority.ALWAYS);

        VBox chiTiet = new VBox(chiTietNode);
        VBox.setVgrow(chiTiet, Priority.ALWAYS);

        splitPane.getItems().addAll(bang, chiTiet);
        return splitPane;
    }


    // Tìm kiếm theo từ khóa
    protected void khoiTaoBoLocTimKiem() {
        txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Tìm kiếm...");
        txtTimKiem.setPrefWidth(220);

        btnTimKiem = new ButtonSample("Tìm", 32, 14, 3);
        btnLamMoi = new ButtonSample("Làm mới", 32, 14, 3);

        btnTimKiem.setOnAction(e ->
                thucHienTimKiem(txtTimKiem.getText().trim())
        );

        btnLamMoi.setOnAction(e -> {
            txtTimKiem.clear();
            lamMoiDuLieu();
        });

        khuVucBoLoc.getChildren().addAll(txtTimKiem, btnTimKiem, btnLamMoi);
    }

    // Lọc theo ngày cụ thể
    protected void khoiTaoBoLocNgayCuThe() {
        dpNgayCuThe = new DatePicker();
        dpNgayCuThe.setPromptText("Chọn ngày");

        ButtonSample btnLoc = new ButtonSample("Lọc", 32, 14, 3);
        btnLoc.setOnAction(e ->
                locTheoNgay(dpNgayCuThe.getValue(), dpNgayCuThe.getValue())
        );
        Label ten = new Label("Ngày:");
        ten.setStyle("-fx-font-weight: bold; -fx-text-fill: #DDB248");

        khuVucBoLoc.getChildren().addAll(ten    , dpNgayCuThe, btnLoc
        );
    }

    /* ================= HỖ TRỢ ================= */

    protected void hienThongBao(Stage stage, String noiDung) {
        hienThongBao(stage, noiDung, Alert.AlertType.INFORMATION);
    }

    protected void hienThongBao(Stage stage, String noiDung, Alert.AlertType loai) {
        Alert alert = new Alert(loai);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(noiDung);
        alert.initOwner(stage);
        alert.show();
    }

    protected boolean xacNhan(Stage stage, String noiDung) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận");
        confirm.setHeaderText(null);
        confirm.setContentText(noiDung);
        confirm.initOwner(stage);

        Optional<ButtonType> rs = confirm.showAndWait();
        return rs.isPresent() && rs.get() == ButtonType.OK;
    }

    /* ================= ABSTRACT ================= */

    protected abstract TableView<?> taoBangChinh();

    protected void thucHienTimKiem(String tuKhoa) {}

    protected void locTheoNgay(LocalDate tuNgay, LocalDate denNgay) {}

    protected abstract void lamMoiDuLieu();

    public Node getChiTietNode() {
        return chiTietNode;
    }
}


