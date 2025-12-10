package com.thefourrestaurant.view.thongke;

import com.thefourrestaurant.DAO.KhuyenMaiDAO;
import com.thefourrestaurant.DAO.LoaiBanDAO;
import com.thefourrestaurant.DAO.LoaiMonDAO;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.model.LoaiBan;
import com.thefourrestaurant.model.LoaiMon;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ThongKeView extends BorderPane {

    // All controls
    private final DatePicker datePickerBatDau;
    private final DatePicker datePickerKetThuc;
    private final ComboBox<String> comboBoxLoaiThongKe;
    private final ComboBox<String> comboBoxLoaiBieuDo;
    private final ButtonSample btnXemThongKe;
    private final VBox khuVucBieuDo;
    private final CheckBox chkSoSanh;
    private final ComboBox<String> comboBoxTheo;
    private final ComboBox<String> comboBoxTuyChon;
    private final GridPane summaryGrid;

    // New controls for dynamic date selection
    private final HBox dateSelectionBox;
    private final ComboBox<Integer> cboThang;
    private final ComboBox<Integer> cboNam;
    private final ComboBox<String> cboQuy;

    public ThongKeView() {
        this.setStyle("-fx-background-color: white;");

        // --- Header ---
        Label lblTitle = new Label("Báo Cáo & Thống Kê");
        lblTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #D4A017;");
        HBox header = new HBox(lblTitle);
        header.setPadding(new Insets(15));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #1E424D;");
        this.setTop(header);

        // --- Control Panel ---
        HBox controlPanelContainer = new HBox(20);
        controlPanelContainer.setPadding(new Insets(20));
        controlPanelContainer.setAlignment(Pos.CENTER_LEFT);

        // --- Left Panel: Options ---
        VBox optionsVBox = new VBox(15);

        // --- Initialize all controls ---
        // Main selectors
        comboBoxTheo = new ComboBox<>();
        comboBoxLoaiThongKe = new ComboBox<>();
        comboBoxTuyChon = new ComboBox<>();
        comboBoxLoaiBieuDo = new ComboBox<>();
        
        // Date/Time selectors
        datePickerBatDau = new DatePicker(LocalDate.now().withDayOfMonth(1));
        datePickerKetThuc = new DatePicker(LocalDate.now());
        cboThang = new ComboBox<>();
        cboNam = new ComboBox<>();
        cboQuy = new ComboBox<>();

        // Style all inputs
        comboBoxTheo.getStyleClass().add("thongke-input");
        datePickerBatDau.getStyleClass().add("thongke-input");
        datePickerKetThuc.getStyleClass().add("thongke-input");
        comboBoxLoaiThongKe.getStyleClass().add("thongke-input");
        comboBoxTuyChon.getStyleClass().add("thongke-input");
        comboBoxLoaiBieuDo.getStyleClass().add("thongke-input");
        cboThang.getStyleClass().add("thongke-input");
        cboNam.getStyleClass().add("thongke-input");
        cboQuy.getStyleClass().add("thongke-input");

        // Populate ComboBoxes
        comboBoxTheo.getItems().addAll("Ngày", "Tháng", "Quý", "Năm");
        comboBoxTheo.setValue("Ngày");
        
        comboBoxLoaiThongKe.getItems().addAll("Doanh thu", "Món ăn", "Bàn");
        comboBoxLoaiThongKe.setValue("Doanh thu");
        
        comboBoxLoaiBieuDo.getItems().addAll("Biểu đồ cột", "Biểu đồ đường", "Biểu đồ tròn");
        comboBoxLoaiBieuDo.setValue("Biểu đồ cột");

        cboThang.getItems().addAll(IntStream.rangeClosed(1, 12).boxed().collect(Collectors.toList()));
        cboThang.setValue(LocalDate.now().getMonthValue());

        int currentYear = LocalDate.now().getYear();
        cboNam.getItems().addAll(IntStream.rangeClosed(currentYear - 10, currentYear).boxed().sorted((a, b) -> b - a).toList());
        cboNam.setValue(currentYear);

        cboQuy.getItems().addAll("Quý 1", "Quý 2", "Quý 3", "Quý 4");
        cboQuy.setValue("Quý 1");

        // --- Row 1: Time selection ---
        HBox timeSelectionRow = new HBox(10);
        timeSelectionRow.setAlignment(Pos.CENTER_LEFT);
        Label lblTheo = new Label("Theo:");
        lblTheo.getStyleClass().add("thongke-label");
        
        dateSelectionBox = new HBox(10);
        dateSelectionBox.setAlignment(Pos.CENTER_LEFT);
        
        timeSelectionRow.getChildren().addAll(lblTheo, comboBoxTheo, dateSelectionBox);
        
        // --- Row 2: Stat type selection ---
        HBox statSelectionRow = new HBox(10);
        statSelectionRow.setAlignment(Pos.CENTER_LEFT);
        Label lblLoaiThongKe = new Label("Thống kê theo:");
        Label lblTuyChon = new Label("Tùy chọn:");
        Label lblLoaiBieuDo = new Label("Loại biểu đồ:");
        
        lblLoaiThongKe.getStyleClass().add("thongke-label");
        lblTuyChon.getStyleClass().add("thongke-label");
        lblLoaiBieuDo.getStyleClass().add("thongke-label");
        
        statSelectionRow.getChildren().addAll(lblLoaiThongKe, comboBoxLoaiThongKe, lblTuyChon, comboBoxTuyChon, lblLoaiBieuDo, comboBoxLoaiBieuDo);

        optionsVBox.getChildren().addAll(timeSelectionRow, statSelectionRow);

        // Listener to change controls
        comboBoxTheo.valueProperty().addListener((obs, oldVal, newVal) -> updateDateSelectionControls(newVal));
        updateDateSelectionControls(comboBoxTheo.getValue()); // Initial setup

        comboBoxLoaiThongKe.valueProperty().addListener((obs, oldVal, newVal) -> updateTuyChonComboBox(newVal));
        updateTuyChonComboBox(comboBoxLoaiThongKe.getValue()); // Initial load for default selection

        // --- Right Panel: Buttons and Checkbox ---
        btnXemThongKe = new ButtonSample("Xem Thống Kê", 50, 25, 14);
        btnXemThongKe.getStyleClass().add("button_sampleGamboge");
        
        chkSoSanh = new CheckBox("So sánh");
        chkSoSanh.getStyleClass().add("thongke-label");

        VBox rightControls = new VBox(10, chkSoSanh, btnXemThongKe);
        rightControls.setAlignment(Pos.CENTER_LEFT);

        controlPanelContainer.getChildren().addAll(optionsVBox, rightControls);
        HBox.setHgrow(optionsVBox, Priority.ALWAYS);

        // --- Chart Area ---
        khuVucBieuDo = new VBox();
        khuVucBieuDo.setAlignment(Pos.CENTER);
        khuVucBieuDo.setPadding(new Insets(20));
        khuVucBieuDo.setPrefHeight(600); // Set a fixed preferred height
        
        Label lblPlaceholder = new Label("Chọn các tùy chọn và nhấn 'Xem Thống Kê' để tạo báo cáo.");
        lblPlaceholder.setStyle("-fx-font-size: 16px; -fx-text-fill: #888;");
        khuVucBieuDo.getChildren().add(lblPlaceholder);

        // --- Summary Grid ---
        summaryGrid = new GridPane();
        summaryGrid.setHgap(20);
        summaryGrid.setVgap(10);
        summaryGrid.setPadding(new Insets(10, 20, 20, 20));
        summaryGrid.setVisible(false); // Initially hidden

        // --- Main Layout ---
        VBox centerLayout = new VBox(20, controlPanelContainer, khuVucBieuDo, summaryGrid);
        this.setCenter(centerLayout);

        // --- Initialize Controller ---
        new ThongKeController(this);
    }

    private void updateDateSelectionControls(String selection) {
        dateSelectionBox.getChildren().clear();
        if (selection == null) return;

        switch (selection) {
            case "Ngày":
                dateSelectionBox.getChildren().addAll(
                        new Label("Từ ngày:"), datePickerBatDau,
                        new Label("Đến ngày:"), datePickerKetThuc
                );
                break;
            case "Tháng":
                dateSelectionBox.getChildren().addAll(
                        new Label("Tháng:"), cboThang,
                        new Label("Năm:"), cboNam
                );
                break;
            case "Năm":
                dateSelectionBox.getChildren().addAll(
                        new Label("Năm:"), cboNam
                );
                break;
            case "Quý":
                dateSelectionBox.getChildren().addAll(
                        new Label("Quý:"), cboQuy,
                        new Label("Năm:"), cboNam
                );
                break;
        }
        // Apply style to new labels
        for (Node node : dateSelectionBox.getChildren()) {
            if (node instanceof Label) {
                node.getStyleClass().add("thongke-label");
            }
        }
    }

    private void updateTuyChonComboBox(String loaiThongKe) {
        if (loaiThongKe == null) {
            comboBoxTuyChon.getItems().clear();
            return;
        }

        switch (loaiThongKe) {
            case "Món ăn":
                LoaiMonDAO loaiMonDAO = new LoaiMonDAO();
                List<String> tenLoaiMon = loaiMonDAO.layTatCaLoaiMon().stream()
                        .map(LoaiMon::getTenLoaiMon)
                        .collect(Collectors.toList());
                comboBoxTuyChon.getItems().setAll(tenLoaiMon);
                comboBoxTuyChon.getItems().add(0, "Tất cả Món Ăn");
                comboBoxTuyChon.setValue("Tất cả Món Ăn");
                break;
            case "Bàn":
                LoaiBanDAO loaiBanDAO = new LoaiBanDAO();
                List<String> tenLoaiBan = loaiBanDAO.layTatCa().stream()
                        .map(LoaiBan::getTenLoaiBan)
                        .collect(Collectors.toList());
                comboBoxTuyChon.getItems().setAll(tenLoaiBan);
                comboBoxTuyChon.getItems().add(0, "Tất cả Bàn");
                comboBoxTuyChon.setValue("Tất cả Bàn");
                break;
            case "Doanh thu":
                KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAO();
                List<String> tenKhuyenMai = khuyenMaiDAO.layDanhSachKhuyenMai().stream()
                        .map(KhuyenMai::getTenKM)
                        .collect(Collectors.toList());
                comboBoxTuyChon.getItems().setAll(tenKhuyenMai);
                comboBoxTuyChon.getItems().add(0, "Tất cả Khuyến Mãi");
                comboBoxTuyChon.setValue("Tất cả Khuyến Mãi");
                break;
            default:
                comboBoxTuyChon.getItems().clear();
                break;
        }
    }

    // Getters
    public DatePicker getDatePickerBatDau() { return datePickerBatDau; }
    public DatePicker getDatePickerKetThuc() { return datePickerKetThuc; }
    public ComboBox<String> getComboBoxLoaiThongKe() { return comboBoxLoaiThongKe; }
    public ComboBox<String> getComboBoxLoaiBieuDo() { return comboBoxLoaiBieuDo; }
    public ButtonSample getBtnXemThongKe() { return btnXemThongKe; }
    public VBox getKhuVucBieuDo() { return khuVucBieuDo; }
    public CheckBox getChkSoSanh() { return chkSoSanh; }
    public ComboBox<String> getComboBoxTheo() { return comboBoxTheo; }
    public ComboBox<String> getComboBoxTuyChon() { return comboBoxTuyChon; }
    public ComboBox<Integer> getCboThang() { return cboThang; }
    public ComboBox<Integer> getCboNam() { return cboNam; }
    public ComboBox<String> getCboQuy() { return cboQuy; }
    public GridPane getSummaryGrid() { return summaryGrid; }
}
