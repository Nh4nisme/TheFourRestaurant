package com.thefourrestaurant.view.thongke;

import com.thefourrestaurant.view.components.ButtonSample;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.time.LocalDate;

public class ThongKeView extends BorderPane {

    private final DatePicker datePickerBatDau;
    private final DatePicker datePickerKetThuc;
    private final ComboBox<String> comboBoxLoaiThongKe;
    private final ComboBox<String> comboBoxLoaiBieuDo;
    private final ButtonSample btnXemThongKe;
    private final VBox khuVucBieuDo;

    public ThongKeView() {
        this.setStyle("-fx-background-color: white;");

        // --- Phần đầu (Header) ---
        Label lblTitle = new Label("Báo Cáo & Thống Kê");
        lblTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #D4A017;");
        HBox header = new HBox(lblTitle);
        header.setPadding(new Insets(15));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #1E424D;");
        this.setTop(header);

        // --- Bảng điều khiển (Control Panel) ---
        // Layout chính cho control panel
        HBox controlPanelContainer = new HBox(30);
        controlPanelContainer.setPadding(new Insets(20));
        controlPanelContainer.setAlignment(Pos.CENTER_LEFT);

        // --- Panel Trái: Các tùy chọn ---
        GridPane optionsGrid = new GridPane();
        optionsGrid.setHgap(10);
        optionsGrid.setVgap(15);

        // Labels
        Label lblTuNgay = new Label("Từ ngày:");
        Label lblDenNgay = new Label("Đến ngày:");
        Label lblLoaiThongKe = new Label("Thống kê theo:");
        Label lblLoaiBieuDo = new Label("Loại biểu đồ:");
        
        lblTuNgay.getStyleClass().add("thongke-label");
        lblDenNgay.getStyleClass().add("thongke-label");
        lblLoaiThongKe.getStyleClass().add("thongke-label");
        lblLoaiBieuDo.getStyleClass().add("thongke-label");

        // Inputs
        datePickerBatDau = new DatePicker(LocalDate.now().withDayOfMonth(1));
        datePickerKetThuc = new DatePicker(LocalDate.now());
        comboBoxLoaiThongKe = new ComboBox<>();
        comboBoxLoaiBieuDo = new ComboBox<>();

        datePickerBatDau.getStyleClass().add("thongke-input");
        datePickerKetThuc.getStyleClass().add("thongke-input");
        comboBoxLoaiThongKe.getStyleClass().add("thongke-input");
        comboBoxLoaiBieuDo.getStyleClass().add("thongke-input");
        
        comboBoxLoaiThongKe.getItems().addAll("Doanh thu", "Món ăn", "Bàn");
        comboBoxLoaiThongKe.setValue("Doanh thu");
        
        comboBoxLoaiBieuDo.getItems().addAll("Biểu đồ cột", "Biểu đồ đường", "Biểu đồ tròn");
        comboBoxLoaiBieuDo.setValue("Biểu đồ cột");

        // Add to optionsGrid
        optionsGrid.add(lblTuNgay, 0, 0);
        optionsGrid.add(datePickerBatDau, 1, 0);
        optionsGrid.add(lblDenNgay, 2, 0);
        optionsGrid.add(datePickerKetThuc, 3, 0);
        optionsGrid.add(lblLoaiThongKe, 0, 1);
        optionsGrid.add(comboBoxLoaiThongKe, 1, 1);
        optionsGrid.add(lblLoaiBieuDo, 2, 1);
        optionsGrid.add(comboBoxLoaiBieuDo, 3, 1);

        // --- Panel Phải: Nút bấm ---
        btnXemThongKe = new ButtonSample("Xem Thống Kê", 50, 20, 14);
        btnXemThongKe.getStyleClass().add("button_sampleGamboge");
        
        // Đưa nút vào một HBox để căn giữa
        HBox buttonContainer = new HBox(btnXemThongKe);
        buttonContainer.setAlignment(Pos.CENTER);

        // Thêm panel trái và phải vào container chính
        controlPanelContainer.getChildren().addAll(optionsGrid, buttonContainer);
        HBox.setHgrow(optionsGrid, Priority.ALWAYS); // Cho panel trái mở rộng

        // --- Khu vực hiển thị biểu đồ ---
        khuVucBieuDo = new VBox();
        khuVucBieuDo.setAlignment(Pos.CENTER);
        khuVucBieuDo.setPadding(new Insets(20));
        
        Label lblPlaceholder = new Label("Chọn các tùy chọn và nhấn 'Xem Thống Kê' để tạo báo cáo.");
        lblPlaceholder.setStyle("-fx-font-size: 16px; -fx-text-fill: #888;");
        khuVucBieuDo.getChildren().add(lblPlaceholder);

        // --- Bố cục chính ---
        VBox centerLayout = new VBox(20, controlPanelContainer, khuVucBieuDo);
        this.setCenter(centerLayout);
    }

    // Getters cho các thành phần để Controller có thể truy cập
    public DatePicker getDatePickerBatDau() { return datePickerBatDau; }
    public DatePicker getDatePickerKetThuc() { return datePickerKetThuc; }
    public ComboBox<String> getComboBoxLoaiThongKe() { return comboBoxLoaiThongKe; }
    public ComboBox<String> getComboBoxLoaiBieuDo() { return comboBoxLoaiBieuDo; }
    public ButtonSample getBtnXemThongKe() { return btnXemThongKe; }
    public VBox getKhuVucBieuDo() { return khuVucBieuDo; }
}
