package com.thefourrestaurant.view.nhanvien;

import com.thefourrestaurant.model.NhanVien;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;

public class GiaoDienChiTietNhanVien extends VBox {

    private TextField txtMaNV;
    private TextField txtHoTen;
    private DatePicker dtpNgaySinh;
    private ComboBox<String> cboGioiTinh;
    private TextField txtSDT;
    private TextField txtLuong;
    private TextField txtMaTK;
    private ImageView imageView;
    private File selectedImageFile = null;
    private ButtonSample btnThem;
    private ButtonSample btnLuu;
    private ButtonSample btnXoa;
    private Label lblTieuDe;
    private Label hintLabel;
    private boolean isEditMode = false;

    public File getSelectedImageFile() { return selectedImageFile; }
    public ButtonSample getBtnThem() { return btnThem; }
    public ButtonSample getBtnLuu() { return btnLuu; }
    public ButtonSample getBtnXoa() { return btnXoa; }
    public TextField getTxtMaNV() { return txtMaNV; }
    public TextField getTxtHoTen() { return txtHoTen; }
    public DatePicker getDtpNgaySinh() { return dtpNgaySinh; }
    public ComboBox<String> getCboGioiTinh() { return cboGioiTinh; }
    public TextField getTxtSDT() { return txtSDT; }
    public TextField getTxtLuong() { return txtLuong; }
    public TextField getTxtMaTK() { return txtMaTK; }
    public boolean isEditMode() { return isEditMode; }

    public GiaoDienChiTietNhanVien() {
        setPadding(new Insets(20));
        setSpacing(15);
        setAlignment(Pos.TOP_CENTER);

        lblTieuDe = new Label("Thông tin nhân viên");
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
        txtMaTK = taoTextField("Mã TK");

        StackPane imagePane = new StackPane();
        imagePane.setPrefSize(180, 240);
        imagePane.setMaxSize(180, 240); 
        imagePane.setMinSize(180, 240); 
        imagePane.setStyle("-fx-background-color: #F5F5F5; -fx-border-color: #E0E0E0; -fx-border-radius: 8; -fx-background-radius: 8;");
        VBox.setMargin(imagePane, new Insets(0, 0, 15, 0));
        imageView = new ImageView();
        imageView.setFitWidth(180);
        imageView.setFitHeight(240);
        imageView.setPreserveRatio(true);

        Label hint = new Label("Chọn ảnh");
        hint.setWrapText(true);
        hint.setStyle("-fx-text-fill: #666;");
        hint.managedProperty().bind(hint.visibleProperty());
        hintLabel = hint;
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
        form.add(new Label("Mã TK:"), 0, 6);
        form.add(txtMaTK, 1, 6);

        VBox mainContent = new VBox(15);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.getChildren().addAll(imagePane, form);

        btnThem = new ButtonSample("Thêm", 36, 16, 1);
        btnLuu = new ButtonSample("Lưu", 36, 16, 1);
        btnXoa = new ButtonSample("Xóa", 36, 16, 2);

        HBox footer = new HBox(10, btnThem, btnLuu, btnXoa);
        footer.setAlignment(Pos.CENTER_RIGHT);

        getChildren().addAll(lblTieuDe, mainContent, footer);
        setEditMode(false);
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
            Image img = new Image(f.toURI().toString(), 180, 240, true, true);
            imageView.setImage(img);
            hintLabel.setVisible(false);
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
            txtMaTK.clear();
            imageView.setImage(null);
            selectedImageFile = null;
            hintLabel.setVisible(true);
            setEditMode(false);
            return;
        }

        setEditMode(true);
        txtMaNV.setText(nv.getMaNV());
        txtHoTen.setText(nv.getHoTen());
        if (nv.getNgaySinh() != null) dtpNgaySinh.setValue(nv.getNgaySinh().toLocalDate());

        String gioiTinh = nv.getGioiTinh();
        if ("Nu".equals(gioiTinh)) {
            cboGioiTinh.getSelectionModel().select("Nữ");
        } else {
            cboGioiTinh.getSelectionModel().select(gioiTinh);
        }

        txtSDT.setText(nv.getSoDienThoai());
        if (nv.getLuong() != null) txtLuong.setText(nv.getLuong().toPlainString());
        if (nv.getMaTK() != null) txtMaTK.setText(nv.getMaTK().getMaTK());

        String dbPath = nv.getHinhAnh();
        boolean loaded = false;
        if (dbPath != null && !dbPath.trim().isEmpty()) {
            try {
                Image img = new Image(getClass().getResourceAsStream(dbPath));
                if (img != null && !img.isError() && img.getWidth() > 0) {
                    imageView.setImage(img);
                    hintLabel.setVisible(false);
                    loaded = true;
                }
            } catch (Exception e) { }
        }
        if (!loaded) loadEmployeeImage(nv.getMaNV());
    }

    private void loadEmployeeImage(String maNV) {
        String[] extensions = {".png", ".jpg", ".jpeg", ".gif"};

        for (String ext : extensions) {
            try {
                String imagePath = "/com/thefourrestaurant/images/NhanVien/" + maNV + ext;
                Image img = new Image(getClass().getResourceAsStream(imagePath));
                if (img != null && !img.isError() && img.getWidth() > 0) {
                    imageView.setImage(img);
                    hintLabel.setVisible(false);
                    return;
                }
            } catch (Exception e) { }
        }

        imageView.setImage(null);
        hintLabel.setVisible(true);
    }

    private void setEditMode(boolean editMode) {
        isEditMode = editMode;

        if (editMode) {
            txtMaNV.setEditable(false);
            txtMaNV.setStyle("-fx-opacity: 0.6;");
            txtMaTK.setEditable(false);
            txtMaTK.setStyle("-fx-opacity: 0.6;");
            txtLuong.setEditable(false);
            txtLuong.setStyle("-fx-opacity: 0.6;");

            btnThem.setVisible(false);
            btnThem.setManaged(false);
            btnLuu.setVisible(true);
            btnLuu.setManaged(true);
            btnXoa.setVisible(true);
            btnXoa.setManaged(true);
        } else {
            txtMaNV.setEditable(true);
            txtMaNV.setStyle("");
            txtMaTK.setEditable(true);
            txtMaTK.setStyle("");
            txtLuong.setEditable(true);
            txtLuong.setStyle("");

            btnThem.setVisible(true);
            btnThem.setManaged(true);
            btnLuu.setVisible(false);
            btnLuu.setManaged(false);
            btnXoa.setVisible(false);
            btnXoa.setManaged(false);
        }
    }

    public String getGioiTinhValue() {
        String selected = cboGioiTinh.getSelectionModel().getSelectedItem();
        if ("Nữ".equals(selected)) {
            return "Nu";
        }
        return selected;
    }

    public static String formatVaiTro(String vaiTro) {
        if (vaiTro == null) return "";
        switch (vaiTro) {
            case "QuanLy": return "Quản lý";
            case "ThuNgan": return "Thu ngân";
            case "LeTan": return "Lễ tân";
            default: return vaiTro;
        }
    }
}