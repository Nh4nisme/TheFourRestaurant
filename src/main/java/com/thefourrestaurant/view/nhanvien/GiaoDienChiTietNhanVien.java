package com.thefourrestaurant.view.nhanvien;

import com.thefourrestaurant.model.NhanVien;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class GiaoDienChiTietNhanVien extends VBox {

    private TextField txtMaNV;
    private TextField txtHoTen;
    private DatePicker dtpNgaySinh;
    private ComboBox<String> cboGioiTinh;
    private TextField txtSDT;
    private TextField txtLuong;
    private ImageView imageView;
    private File selectedImageFile = null;
    private ButtonSample btnLuu;
    private ButtonSample btnClear;

    public GiaoDienChiTietNhanVien() {
        setPadding(new Insets(20));
        setSpacing(15);
        setAlignment(Pos.TOP_CENTER);

        Label lblTieuDe = new Label("Thông tin nhân viên");
        lblTieuDe.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #DDB248;");

        txtMaNV = taoTextField("Mã NV");
        txtHoTen = taoTextField("Họ tên");
        dtpNgaySinh = new DatePicker();
        dtpNgaySinh.setPrefWidth(300);
        cboGioiTinh = new ComboBox<>();
        cboGioiTinh.getItems().addAll("Nam", "Nữ", "Khác");
        cboGioiTinh.setPrefWidth(300);
        txtSDT = taoTextField("Số điện thoại");
        txtLuong = taoTextField("Lương");

        StackPane imagePane = new StackPane();
        imagePane.setPrefSize(180, 180);
        imagePane.setStyle("-fx-background-color: #F5F5F5; -fx-border-color: #E0E0E0; -fx-border-radius: 8; -fx-background-radius: 8;");
        imageView = new ImageView();
        imageView.setFitWidth(160);
        imageView.setFitHeight(160);
        imageView.setPreserveRatio(true);

        Label hint = new Label("[X] Kéo thả ảnh ở đây hoặc nhấp để chọn");
        hint.setWrapText(true);
        hint.setStyle("-fx-text-fill: #666;");
        imagePane.getChildren().addAll(imageView, hint);
        imagePane.setOnMouseClicked(e -> chonAnh());

        imagePane.setOnDragOver((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        imagePane.setOnDragDropped((DragEvent event) -> {
            boolean success = false;
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                File file = db.getFiles().get(0);
                setImageFile(file);
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.add(new Label("Mã NV:"), 0, 0);
        form.add(txtMaNV, 1, 0);
        form.add(new Label("Họ tên:"), 0, 1);
        form.add(txtHoTen, 1, 1);
        form.add(new Label("Ngày sinh:"), 0, 2);
        form.add(dtpNgaySinh, 1, 2);
        form.add(new Label("Giới tính:"), 0, 3);
        form.add(cboGioiTinh, 1, 3);
        form.add(new Label("SĐT:"), 0, 4);
        form.add(txtSDT, 1, 4);
        form.add(new Label("Lương:"), 0, 5);
        form.add(txtLuong, 1, 5);
        form.add(new Label("Ảnh:"), 0, 6);
        form.add(imagePane, 1, 6);

        btnLuu = new ButtonSample("Lưu", 36, 16, 1);
        btnClear = new ButtonSample("Xóa", 36, 16, 2);

        HBox footer = new HBox(10, btnLuu, btnClear);
        footer.setAlignment(Pos.CENTER_RIGHT);

        getChildren().addAll(lblTieuDe, form, footer);
    }

    private TextField taoTextField(String prompt) {
        TextField t = new TextField();
        t.setPromptText(prompt);
        t.setPrefWidth(300);
        return t;
    }

    private void chonAnh() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File f = fc.showOpenDialog(getScene() != null ? getScene().getWindow() : null);
        if (f != null) setImageFile(f);
    }

    private void setImageFile(File f) {
        try {
            selectedImageFile = f;
            Image img = new Image(f.toURI().toString(), 160, 160, true, true);
            imageView.setImage(img);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void hienThi(NhanVien nv) {
        if (nv == null) {
            txtMaNV.clear();
            txtHoTen.clear();
            dtpNgaySinh.setValue(null);
            cboGioiTinh.getSelectionModel().clearSelection();
            txtSDT.clear();
            txtLuong.clear();
            imageView.setImage(null);
            selectedImageFile = null;
            return;
        }
        txtMaNV.setText(nv.getMaNV());
        txtHoTen.setText(nv.getHoTen());
        if (nv.getNgaySinh() != null) dtpNgaySinh.setValue(nv.getNgaySinh().toLocalDate());
        cboGioiTinh.getSelectionModel().select(nv.getGioiTinh());
        txtSDT.setText(nv.getSoDienThoai());
        if (nv.getLuong() != null) txtLuong.setText(nv.getLuong().toPlainString());
    }

    public File getSelectedImageFile() { return selectedImageFile; }
    public ButtonSample getBtnLuu() { return btnLuu; }
    public ButtonSample getBtnClear() { return btnClear; }
    public TextField getTxtMaNV() { return txtMaNV; }
}
