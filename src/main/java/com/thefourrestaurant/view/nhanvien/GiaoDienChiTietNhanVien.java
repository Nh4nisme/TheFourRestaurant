package com.thefourrestaurant.view.nhanvien;

import com.thefourrestaurant.model.NhanVien;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import com.thefourrestaurant.controller.NhanVienController;
import com.thefourrestaurant.DAO.NhanVienDAO;
import com.thefourrestaurant.DAO.TaiKhoanDAO;
import com.thefourrestaurant.DAO.VaiTroDAO;
import com.thefourrestaurant.model.TaiKhoan;
import com.thefourrestaurant.model.VaiTro;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;

public class GiaoDienChiTietNhanVien extends VBox {

    private TextField txtMaNV;
    private TextField txtHoTen;
    private DatePicker dtpNgaySinh;
    private ComboBox<String> cboGioiTinh;
    private TextField txtSDT;
    
    private ComboBox<VaiTro> cboVaiTro;
    private TextField txtMaTK;
    private ImageView imageView;
    private File selectedImageFile = null;
    private ButtonSample btnThem;
    private ButtonSample btnLuu;
    private ButtonSample btnXoa;
    private Label lblTieuDe;
    private Label hintLabel;
    private boolean isEditMode = false;
    private final NhanVienController controller = new NhanVienController();
    private final NhanVienDAO nhanVienDAO = new NhanVienDAO();

    public File getSelectedImageFile() { return selectedImageFile; }
    public ButtonSample getBtnThem() { return btnThem; }
    public ButtonSample getBtnLuu() { return btnLuu; }
    public ButtonSample getBtnXoa() { return btnXoa; }
    public TextField getTxtMaNV() { return txtMaNV; }
    public TextField getTxtHoTen() { return txtHoTen; }
    public DatePicker getDtpNgaySinh() { return dtpNgaySinh; }
    public ComboBox<String> getCboGioiTinh() { return cboGioiTinh; }
    public TextField getTxtSDT() { return txtSDT; }
    public TextField getTxtMaTK() { return txtMaTK; }
    public boolean isEditMode() { return isEditMode; }

    public GiaoDienChiTietNhanVien() {
        setPadding(new Insets(12));
        setSpacing(8);
        setAlignment(Pos.TOP_CENTER);

        lblTieuDe = new Label("Thông tin nhân viên");
        lblTieuDe.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #DDB248;");

        txtMaNV = taoTextField("Mã NV");
        txtHoTen = taoTextField("Họ tên");
        dtpNgaySinh = new DatePicker();
        dtpNgaySinh.setPrefWidth(300);
        // >= 18
        LocalDate maxAllowed = LocalDate.now().minusYears(18);
        dtpNgaySinh.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) return;
                setDisable(date.isAfter(maxAllowed));
            }
        });
        dtpNgaySinh.setValue(java.time.LocalDate.of(2001, 1, 1));
        cboGioiTinh = new ComboBox<>();
        cboGioiTinh.getItems().addAll("Nam", "Nữ", "Khác");
        cboGioiTinh.setPrefWidth(300);
        txtSDT = taoTextField("Số điện thoại");
        cboVaiTro = new ComboBox<>();
        cboVaiTro.setPrefWidth(300);
        try {
            java.util.List<VaiTro> roles = new VaiTroDAO().layDanhSachVaiTro();
            if (roles != null && !roles.isEmpty()) {
                cboVaiTro.getItems().addAll(roles);
            } else {
                cboVaiTro.getItems().addAll(new VaiTro("VT000001", "QuanLy", false), new VaiTro("VT000002", "ThuNgan", false));
            }
        } catch (Exception ex) {
            cboVaiTro.getItems().addAll(new VaiTro("VT000001", "QuanLy", false), new VaiTro("VT000002", "ThuNgan", false));
        }
        cboVaiTro.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(VaiTro item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatVaiTro(item.getTenVaiTro()));
            }
        });
        cboVaiTro.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(VaiTro item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatVaiTro(item.getTenVaiTro()));
            }
        });
        cboVaiTro.setConverter(new StringConverter<>() {
            @Override
            public String toString(VaiTro object) {
                return object == null ? null : formatVaiTro(object.getTenVaiTro());
            }

            @Override
            public VaiTro fromString(String string) {
                return null;
            }
        });
        txtMaTK = taoTextField("Mã TK");

        StackPane imagePane = new StackPane();
        imagePane.setPrefSize(180, 240);
        imagePane.setMaxSize(180, 240); 
        imagePane.setMinSize(180, 240); 
        imagePane.setStyle("-fx-background-color: #F5F5F5; -fx-border-color: #E0E0E0; -fx-border-radius: 8; -fx-background-radius: 8;");
        VBox.setMargin(imagePane, new Insets(0, 0, 8, 0));
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
        form.setVgap(6);
        ColumnConstraints formC0 = new ColumnConstraints();
        formC0.setMinWidth(110);
        ColumnConstraints formC1 = new ColumnConstraints();
        formC1.setMinWidth(220);
        form.getColumnConstraints().addAll(formC0, formC1);
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
        form.add(new Label("Vai trò:"), 0, 5);
        form.add(cboVaiTro, 1, 5);
        form.add(new Label("Mã TK:"), 0, 6);
        form.add(txtMaTK, 1, 6);

        VBox mainContent = new VBox(8);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.getChildren().addAll(imagePane, form);

        btnThem = new ButtonSample("Thêm", 36, 16, 1);
        btnLuu = new ButtonSample("Lưu", 36, 16, 1);
        btnXoa = new ButtonSample("Xóa trắng", 36, 16, 2);

        // Xóa trắng: xóa các trường nhập trừ Mã NV và Mã TK
        btnXoa.setOnAction(ev -> {
            txtHoTen.clear();
            txtSDT.clear();
            cboGioiTinh.getSelectionModel().clearSelection();
            cboVaiTro.getSelectionModel().clearSelection();
            dtpNgaySinh.setValue(java.time.LocalDate.of(2001, 1, 1));
            imageView.setImage(null);
            selectedImageFile = null;
            hintLabel.setVisible(true);
            setEditMode(false);
        });

        // tự tạo id
        txtMaNV.setEditable(false);
        txtMaTK.setEditable(false);
        try {
            txtMaNV.setText(nhanVienDAO.taoMaNhanVienMoi());
            txtMaTK.setText(TaiKhoanDAO.taoMaTaiKhoanMoi());
        } catch (Exception ex) { }

        btnThem.setOnAction(e -> {
            String hoTen = txtHoTen.getText().trim();
            LocalDate ngaySinh = dtpNgaySinh.getValue();
            String gioiTinh = cboGioiTinh.getSelectionModel().getSelectedItem();

            if (hoTen.isEmpty()) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Trường 'Họ tên' không được để trống.");
                a.showAndWait();
                return;
            }
            if (ngaySinh == null) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Trường 'Ngày sinh' không được để trống.");
                a.showAndWait();
                return;
            }
            if (gioiTinh == null || gioiTinh.isEmpty()) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Vui lòng chọn giới tính.");
                a.showAndWait();
                return;
            }
            VaiTro selectedRole = cboVaiTro.getSelectionModel().getSelectedItem();
            if (selectedRole == null) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Vui lòng chọn vai trò.");
                a.showAndWait();
                return;
            }
            // Regex
            String sdtVal = txtSDT.getText().trim();
            if (!sdtVal.isEmpty()) {
                if (!sdtVal.matches("^0\\d{9,10}$")) {
                    Alert a = new Alert(Alert.AlertType.ERROR, "Số điện thoại không hợp lệ. Phải bắt đầu bằng 0 và gồm 10 hoặc 11 chữ số.");
                    a.showAndWait();
                    return;
                }
                try {
                    if (nhanVienDAO.layNhanVienTheoSDT(sdtVal) != null) {
                        Alert a = new Alert(Alert.AlertType.ERROR, "Số điện thoại đã tồn tại trong hệ thống.");
                        a.showAndWait();
                        return;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            String maNV = nhanVienDAO.taoMaNhanVienMoi();
            String maTK = TaiKhoanDAO.taoMaTaiKhoanMoi();
            txtMaNV.setText(maNV);
            txtMaTK.setText(maTK);

            // Tài khoản dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(getScene() != null ? getScene().getWindow() : null);
            dialog.setTitle("Thông tin tài khoản");
            DialogPane dp = dialog.getDialogPane();
            dp.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                Label title = new Label("Thông tin tài khoản");
                title.setStyle("-fx-text-fill: #DDB248; -fx-font-size: 24px; -fx-font-weight: bold;");
                title.setMaxWidth(Double.MAX_VALUE);
                title.setAlignment(Pos.CENTER);
                VBox.setMargin(title, new Insets(30,0,30,0));

                TextField txtMaTKDlg = new TextField(maTK);
                txtMaTKDlg.setEditable(false);
                txtMaTKDlg.setPrefWidth(250);
                txtMaTKDlg.setStyle("-fx-control-inner-background: #F5F5F5; -fx-opacity: 1;");

                TextField txtTenDN = new TextField();
                txtTenDN.setPromptText("Tài khoản");
                txtTenDN.setPrefWidth(250);
                PasswordField txtMatKhau = new PasswordField();
                txtMatKhau.setPromptText("Mật khẩu");
                txtMatKhau.setPrefWidth(250);
                PasswordField txtMatKhau2 = new PasswordField();
                txtMatKhau2.setPromptText("Nhập lại mật khẩu");
                txtMatKhau2.setPrefWidth(250);

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.add(new Label("Mã TK:"), 0, 0);
                grid.add(txtMaTKDlg, 1, 0);
                grid.add(new Label("Tài khoản:"), 0, 1);
                grid.add(txtTenDN, 1, 1);
                grid.add(new Label("Mật khẩu:"), 0, 2);
                grid.add(txtMatKhau, 1, 2);
                grid.add(new Label("Nhập lại mật khẩu:"), 0, 3);
                grid.add(txtMatKhau2, 1, 3);
                ColumnConstraints c0 = new ColumnConstraints();
                c0.setMinWidth(140);
                ColumnConstraints c1 = new ColumnConstraints();
                c1.setMinWidth(250);
                grid.getColumnConstraints().addAll(c0, c1);

                VBox content = new VBox(8, title, grid);
                content.setPrefWidth(700);
                content.setPrefHeight(500);
                dp.setContent(content);
                dp.setPrefSize(450, 500);

                Button okBtn = (Button) dp.lookupButton(ButtonType.OK);
                if (okBtn != null) {
                    okBtn.setText("Thêm");
                    okBtn.getStyleClass().add("button_sampleGamboge");
                    okBtn.setPrefHeight(36);
                    okBtn.setPrefWidth(100);
                    try {
                        javafx.scene.text.Font mont = javafx.scene.text.Font.loadFont(
                                getClass().getResourceAsStream("/com/thefourrestaurant/fonts/Montserrat-Bold.ttf"),
                                16);
                        if (mont != null) okBtn.setFont(mont);
                    } catch (Exception ignored) {}
                    // Cảnh báo lỗi không đóng dialog tài khoản
                    okBtn.addEventFilter(ActionEvent.ACTION, ev -> {
                        String tenDNVal = txtTenDN.getText().trim();
                        String mkVal = txtMatKhau.getText();
                        String mk2Val = txtMatKhau2.getText();
                        if (txtMaTKDlg.getText().trim().isEmpty()) {
                            Alert a = new Alert(Alert.AlertType.ERROR, "Mã tài khoản bị lỗi. Vui lòng thử lại.");
                            a.getDialogPane().setPrefSize(560, 200);
                            a.initOwner(dp.getScene() != null ? dp.getScene().getWindow() : null);
                            a.showAndWait();
                            ev.consume();
                            return;
                        }
                        if (tenDNVal.isEmpty()) {
                            Alert a = new Alert(Alert.AlertType.ERROR, "Trường 'Tài khoản' không được để trống.");
                            a.getDialogPane().setPrefSize(560, 200);
                            a.initOwner(dp.getScene() != null ? dp.getScene().getWindow() : null);
                            a.showAndWait();
                            ev.consume();
                            return;
                        }
                        try {
                            if (TaiKhoanDAO.layTaiKhoanTheoTenDangNhap(tenDNVal) != null) {
                                Alert a = new Alert(Alert.AlertType.ERROR);
                                a.setTitle("Lỗi");
                                a.setHeaderText("Tài khoản đã tồn tại");
                                a.setContentText("Tài khoản đã tồn tại. Vui lòng chọn tên khác.");
                                a.getDialogPane().setPrefSize(560, 200);
                                a.initOwner(dp.getScene() != null ? dp.getScene().getWindow() : null);
                                a.showAndWait();
                                ev.consume();
                                return;
                            }
                        } catch (Exception ex) { ex.printStackTrace(); }
                        if (mkVal.isEmpty() || mk2Val.isEmpty()) {
                            Alert a = new Alert(Alert.AlertType.ERROR, "Trường 'Mật khẩu' và 'Nhập lại mật khẩu' không được để trống.");
                            a.getDialogPane().setPrefSize(560, 200);
                            a.initOwner(dp.getScene() != null ? dp.getScene().getWindow() : null);
                            a.showAndWait();
                            ev.consume();
                            return;
                        }
                        if (!mkVal.equals(mk2Val)) {
                            Alert a = new Alert(Alert.AlertType.ERROR, "Mật khẩu nhập lại không khớp.");
                            a.getDialogPane().setPrefSize(560, 200);
                            a.initOwner(dp.getScene() != null ? dp.getScene().getWindow() : null);
                            a.showAndWait();
                            ev.consume();
                            return;
                        }
                        if (mkVal.length() < 6) {
                            Alert a = new Alert(Alert.AlertType.ERROR, "Mật khẩu phải có ít nhất 6 ký tự.");
                            a.getDialogPane().setPrefSize(560, 200);
                            a.initOwner(dp.getScene() != null ? dp.getScene().getWindow() : null);
                            a.showAndWait();
                            ev.consume();
                        }
                    });
                }

            dialog.setOnShown(evt -> {
                Button b = (Button) dp.lookupButton(ButtonType.OK);
                if (b != null) {
                    b.addEventFilter(ActionEvent.ACTION, event -> {
                        String tenDNVal2 = txtTenDN.getText().trim();
                        try {
                            if (TaiKhoanDAO.layTaiKhoanTheoTenDangNhap(tenDNVal2) != null) {
                                Alert a = new Alert(Alert.AlertType.ERROR);
                                a.setTitle("Lỗi");
                                a.setHeaderText("Tài khoản đã tồn tại");
                                a.setContentText("Tài khoản đã tồn tại.");
                                a.getDialogPane().setPrefSize(420, 180);
                                a.initOwner(dp.getScene() != null ? dp.getScene().getWindow() : null);
                                a.showAndWait();
                                event.consume();
                            }
                        } catch (Exception ex) { ex.printStackTrace(); }
                    });
                }
            });

            Optional<ButtonType> res = dialog.showAndWait();
            if (res.isPresent() && res.get().getButtonData() == ButtonData.OK_DONE) {
                String tenDN = txtTenDN.getText().trim();
                String mk = txtMatKhau.getText();
                String mk2 = txtMatKhau2.getText();

                if (tenDN.isEmpty()) {
                    Alert a = new Alert(Alert.AlertType.ERROR, "Trường 'Tài khoản' không được để trống.");
                    a.getDialogPane().setPrefSize(560, 200);
                    a.initOwner(getScene() != null ? getScene().getWindow() : null);
                    a.showAndWait();
                    return;
                }
                if (mk.isEmpty()) {
                    Alert a = new Alert(Alert.AlertType.ERROR, "Trường 'Mật khẩu' không được để trống.");
                    a.getDialogPane().setPrefSize(560, 200);
                    a.initOwner(getScene() != null ? getScene().getWindow() : null);
                    a.showAndWait();
                    return;
                }
                if (!mk.equals(mk2)) {
                    Alert a = new Alert(Alert.AlertType.ERROR, "Mật khẩu nhập lại không khớp.");
                    a.getDialogPane().setPrefSize(560, 200);
                    a.initOwner(getScene() != null ? getScene().getWindow() : null);
                    a.showAndWait();
                    return;
                }

                // Kiểm tra trùng số điện thoại
                String sdtCheck = txtSDT.getText().trim();
                if (!sdtCheck.isEmpty()) {
                    try {
                        if (nhanVienDAO.layNhanVienTheoSDT(sdtCheck) != null) {
                            Alert a = new Alert(Alert.AlertType.ERROR, "Số điện thoại đã tồn tại trong hệ thống.");
                            a.getDialogPane().setPrefSize(560, 200);
                            a.initOwner(getScene() != null ? getScene().getWindow() : null);
                            a.showAndWait();
                            return;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                try {
                    if (TaiKhoanDAO.layTaiKhoanTheoTenDangNhap(tenDN) != null) {
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setTitle("Lỗi tạo tài khoản");
                        a.setHeaderText("Tài khoản đã tồn tại");
                        a.setContentText("Tài khoản '" + tenDN + "' đã tồn tại. Vui lòng chọn tên khác.");
                        a.getDialogPane().setPrefSize(560, 200);
                        a.initOwner(getScene() != null ? getScene().getWindow() : null);
                        a.showAndWait();
                        return;
                    }
                } catch (Exception ex) { ex.printStackTrace(); }

                TaiKhoan tk = new TaiKhoan(maTK, tenDN, mk, selectedRole, false);
                boolean tkOk = false;
                try {
                    tkOk = TaiKhoanDAO.themTaiKhoan(tk);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (!tkOk) {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Lỗi");
                    a.setHeaderText("Tạo tài khoản thất bại");
                    a.setContentText("Không thể tạo tài khoản. Vui lòng kiểm tra tên đăng nhập và thử lại.");
                    a.getDialogPane().setPrefSize(520, 180);
                    a.initOwner(getScene() != null ? getScene().getWindow() : null);
                    a.showAndWait();
                    return;
                }

                java.sql.Date sqlDate = java.sql.Date.valueOf(ngaySinh);
                BigDecimal luong = BigDecimal.ZERO;

                NhanVien nv = new NhanVien(maNV, hoTen, sqlDate, getGioiTinhValue(), txtSDT.getText().trim(), luong, tk);
                boolean nvOk = controller.themNhanVien(nv, selectedImageFile);

                if (tkOk && nvOk) {
                    Alert a = new Alert(Alert.AlertType.INFORMATION, "Thêm nhân viên thành công.");
                    a.showAndWait();
                    hienThi(null);
                    // Refresh parent list view if present
                    try { refreshParentList(); } catch (Exception ignored) {}
                } else {
                    try {
                        TaiKhoanDAO.xoaTaiKhoan(maTK);
                    } catch (Exception ex) { ex.printStackTrace(); }

                    Alert a = new Alert(Alert.AlertType.ERROR, "Thêm thất bại. Tài khoản đã bị xóa để tránh dữ liệu không nhất quán.");
                    a.getDialogPane().setPrefSize(560, 200);
                    a.initOwner(getScene() != null ? getScene().getWindow() : null);
                    a.showAndWait();
                }
            }
        });

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
            dtpNgaySinh.setValue(java.time.LocalDate.of(2001, 1, 1));
            cboGioiTinh.getSelectionModel().clearSelection();
            txtSDT.clear();
            txtMaTK.clear();
            cboVaiTro.getSelectionModel().clearSelection();
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
        if (nv.getMaTK() != null) txtMaTK.setText(nv.getMaTK().getMaTK());
        if (nv.getMaTK() != null && nv.getMaTK().getVaiTro() != null) {
            String maVT = nv.getMaTK().getVaiTro().getMaVT();
            for (VaiTro vt : cboVaiTro.getItems()) {
                if (vt != null && vt.getMaVT() != null && vt.getMaVT().equals(maVT)) {
                    cboVaiTro.getSelectionModel().select(vt);
                    break;
                }
            }
        } else {
            cboVaiTro.getSelectionModel().clearSelection();
        }

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

            btnThem.setVisible(false);
            btnThem.setManaged(false);
            btnLuu.setVisible(true);
            btnLuu.setManaged(true);
            btnXoa.setVisible(true);
            btnXoa.setManaged(true);
        } else {
            txtMaNV.setEditable(false);
            txtMaNV.setStyle("-fx-opacity: 0.6;");
            txtMaTK.setEditable(false);
            txtMaTK.setStyle("-fx-opacity: 0.6;");

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

    private void refreshParentList() {
        javafx.scene.Parent p = this.getParent();
        while (p != null) {
            if (p instanceof GiaoDienNhanVien) {
                try {
                    ((GiaoDienNhanVien) p).lamMoiDuLieu();
                } catch (Exception ignored) {}
                break;
            }
            p = p.getParent();
        }
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