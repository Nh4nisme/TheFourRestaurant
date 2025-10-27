package com.thefourrestaurant.view.ban;

import com.thefourrestaurant.view.components.ButtonSample2;
import com.thefourrestaurant.view.components.ButtonSample2.Variant;
import com.thefourrestaurant.DAO.LoaiBanDAO;
import com.thefourrestaurant.model.Ban;
import com.thefourrestaurant.model.LoaiBan;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.List;

public class GiaoDienTuyChinhBan extends VBox {

    private ComboBox<String> cbLoaiBan;
    private ComboBox<String> cbTrangThai;
    private Hyperlink linkAttachImage;
    private Label lblImageFileName;
    private Button btnXoa;
    private Button btnDiChuyen;
    private Button btnOK;
    private Button btnHuy;
    
    private Ban ban;
    private final LoaiBanDAO loaiBanDAO = new LoaiBanDAO();
    private File selectedImageFile;
    private Runnable onDiChuyen;

    public GiaoDienTuyChinhBan(Ban ban) {
        this.ban = ban;
        
        setStyle("-fx-background-color: #F5F5F5;");
        setSpacing(0);
        setAlignment(Pos.TOP_CENTER);
        setPrefSize(370, 200);

        // Title bar
        Label lblTitle = new Label("Tùy chỉnh bàn");
        lblTitle.setStyle("-fx-font-size: 16px; -fx-text-fill: #E19E11; -fx-font-weight: bold;");
        HBox titleBar = new HBox(lblTitle);
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.setPadding(new Insets(10, 20, 10, 20));
        titleBar.setStyle("-fx-background-color: #1E424D;");
        titleBar.setPrefHeight(45);

        // Content area
        VBox contentBox = new VBox(12);
        contentBox.setPadding(new Insets(20, 25, 15, 25));
        contentBox.setStyle("-fx-background-color: white;");
        contentBox.setAlignment(Pos.TOP_LEFT);

        // Row 1: Loại bàn
        HBox row1 = new HBox(15);
        row1.setAlignment(Pos.CENTER_LEFT);
        
        Label lblLoaiBan = createLabel("Loại bàn:");
        lblLoaiBan.setPrefWidth(80);
        
        cbLoaiBan = createComboBox();
        cbLoaiBan.setPromptText("Chọn loại bàn");
        HBox.setHgrow(cbLoaiBan, Priority.ALWAYS);
        
        row1.getChildren().addAll(lblLoaiBan, cbLoaiBan);

        // Row 2: Trạng thái
        HBox row2 = new HBox(15);
        row2.setAlignment(Pos.CENTER_LEFT);
        
        Label lblTrangThai = createLabel("Trạng Thái:");
        lblTrangThai.setPrefWidth(80);
        
        cbTrangThai = createComboBox();
        cbTrangThai.setPromptText("Chọn trạng thái");
        cbTrangThai.getItems().addAll("Trống", "Đang sử dụng", "Đã đặt trước");
        HBox.setHgrow(cbTrangThai, Priority.ALWAYS);
        
        row2.getChildren().addAll(lblTrangThai, cbTrangThai);

        // Row 3: Hình ảnh
        HBox row3 = new HBox(15);
        row3.setAlignment(Pos.CENTER_LEFT);
        
        Label lblHinhAnh = createLabel("Hình ảnh:");
        lblHinhAnh.setPrefWidth(80);
        
        VBox imageBox = new VBox(5);
        linkAttachImage = new Hyperlink("attach an image");
        linkAttachImage.setStyle("-fx-text-fill: #0066CC; -fx-font-size: 13px; -fx-underline: true;");
        linkAttachImage.setPadding(new Insets(0));
        
        lblImageFileName = new Label("");
        lblImageFileName.setStyle("-fx-font-size: 12px; -fx-text-fill: #666666;");
        
        imageBox.getChildren().addAll(linkAttachImage, lblImageFileName);
        HBox.setHgrow(imageBox, Priority.ALWAYS);
        
        row3.getChildren().addAll(lblHinhAnh, imageBox);

        contentBox.getChildren().addAll(row1, row2, row3);

        // Button bar
        HBox buttonBar = new HBox(12);
        buttonBar.setAlignment(Pos.CENTER_LEFT);
        buttonBar.setPadding(new Insets(15, 25, 15, 25));
        buttonBar.setStyle("-fx-background-color: #2A4A56;");
        buttonBar.setPrefHeight(55);

        btnXoa = new ButtonSample2("Xóa", Variant.YELLOW, 90);
        btnDiChuyen = new ButtonSample2("Di chuyển", Variant.YELLOW, 100);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        btnOK = new ButtonSample2("OK", Variant.YELLOW, 80);
        btnHuy = new ButtonSample2("Hủy", Variant.YELLOW, 80);

        buttonBar.getChildren().addAll(btnXoa, btnDiChuyen, spacer, btnOK, btnHuy);

        getChildren().addAll(titleBar, contentBox, buttonBar);

        loadData();
        wireHandlers();
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 13px; -fx-text-fill: #E19E11; -fx-font-weight: bold;");
        label.setMinWidth(Region.USE_PREF_SIZE);
        return label;
    }

    private ComboBox<String> createComboBox() {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setStyle("-fx-background-color: white; " +
                         "-fx-border-color: #CCCCCC; " +
                         "-fx-border-radius: 5; " +
                         "-fx-background-radius: 5;");
        comboBox.setPrefHeight(30);
        comboBox.setMaxWidth(Double.MAX_VALUE);
        return comboBox;
    }

    private void loadData() {
        // Load loại bàn
        try {
            List<LoaiBan> dsLoaiBan = loaiBanDAO.layTatCa();
            for (LoaiBan lb : dsLoaiBan) {
                cbLoaiBan.getItems().add(lb.getTenLoaiBan());
            }
            
            // Set giá trị hiện tại
            if (ban != null) {
                if (ban.getLoaiBan() != null) {
                    cbLoaiBan.setValue(ban.getLoaiBan().getTenLoaiBan());
                }
                if (ban.getTrangThai() != null) {
                    cbTrangThai.setValue(ban.getTrangThai());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void wireHandlers() {
        // Xử lý chọn hình ảnh
        linkAttachImage.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Chọn hình ảnh");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );
            
            Stage stage = (Stage) getScene().getWindow();
            selectedImageFile = fileChooser.showOpenDialog(stage);
            
            if (selectedImageFile != null) {
                lblImageFileName.setText(selectedImageFile.getName());
            }
        });

        // Nút Xóa
        btnXoa.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc chắn muốn xóa bàn này?",
                ButtonType.YES, ButtonType.NO);
            confirm.setTitle("Xác nhận xóa");
            confirm.setHeaderText(null);
            confirm.initOwner(getScene().getWindow());
            
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    // TODO: Xử lý xóa bàn
                    Stage stage = (Stage) getScene().getWindow();
                    if (stage != null) stage.close();
                }
            });
        });

        // Nút Di chuyển
        btnDiChuyen.setOnAction(e -> {
            if (onDiChuyen != null) {
                onDiChuyen.run(); // gọi callback
            }
            Stage stage = (Stage) getScene().getWindow();
            if (stage != null) stage.close();
        });

        // Nút OK
        btnOK.setOnAction(e -> {
            try {
                String loaiBan = cbLoaiBan.getValue();
                String trangThai = cbTrangThai.getValue();
                
                if (loaiBan == null || loaiBan.isEmpty()) {
                    showAlert("Vui lòng chọn loại bàn", Alert.AlertType.WARNING);
                    return;
                }
                
                if (trangThai == null || trangThai.isEmpty()) {
                    showAlert("Vui lòng chọn trạng thái", Alert.AlertType.WARNING);
                    return;
                }
                
                // TODO: Cập nhật thông tin bàn vào database
                // ban.setLoaiBan(...);
                // ban.setTrangThai(trangThai);
                // banDAO.capNhat(ban);
                
                showAlert("Cập nhật thông tin bàn thành công!", Alert.AlertType.INFORMATION);
                
                Stage stage = (Stage) getScene().getWindow();
                if (stage != null) stage.close();
                
            } catch (Exception ex) {
                showAlert("Có lỗi xảy ra: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        // Nút Hủy
        btnHuy.setOnAction(e -> {
            Stage stage = (Stage) getScene().getWindow();
            if (stage != null) stage.close();
        });
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type, message);
        alert.setTitle(type == Alert.AlertType.ERROR ? "Lỗi" : 
                      type == Alert.AlertType.WARNING ? "Cảnh báo" : "Thông báo");
        alert.setHeaderText(null);
        alert.initOwner(getScene().getWindow());
        alert.showAndWait();
    }

    // Getters
    public ComboBox<String> getCbLoaiBan() { return cbLoaiBan; }
    public ComboBox<String> getCbTrangThai() { return cbTrangThai; }
    public Button getBtnXoa() { return btnXoa; }
    public Button getBtnDiChuyen() { return btnDiChuyen; }
    public Button getBtnOK() { return btnOK; }
    public Button getBtnHuy() { return btnHuy; }
    public File getSelectedImageFile() { return selectedImageFile; }
    public void setOnDiChuyen(Runnable onDiChuyen) {
        this.onDiChuyen = onDiChuyen;
    }
}