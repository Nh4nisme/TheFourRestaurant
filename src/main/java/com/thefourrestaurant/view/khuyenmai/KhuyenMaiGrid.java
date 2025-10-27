package com.thefourrestaurant.view.khuyenmai;

import com.thefourrestaurant.controller.KhuyenMaiController;
import com.thefourrestaurant.model.KhuyenMai;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class KhuyenMaiGrid extends VBox {
    private final KhuyenMaiController boDieuKhien = new KhuyenMaiController();
    private FlowPane grid;

    public KhuyenMaiGrid(GiaoDienKhuyenMai mainView) {
        grid = new FlowPane(15, 15); // Horizontal and vertical gap
        grid.setPadding(new Insets(15));

        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        this.getChildren().add(scrollPane);
        this.setStyle("-fx-background-color: transparent;");

        refresh(mainView);
    }

    private void loadKhuyenMai(GiaoDienKhuyenMai mainView) {
        grid.getChildren().clear();

        // Add "Them moi" box
        KhuyenMaiBox themMoiBox = KhuyenMaiBox.createThemMoiBox();
        themMoiBox.setOnMouseClicked(event -> {
            Stage owner = (Stage) getScene().getWindow();
            if (boDieuKhien.themKhuyenMaiMoi(owner)) {
                mainView.lamMoiGiaoDien();
            }
        });
        grid.getChildren().add(themMoiBox);

        // Load existing KhuyenMai
        List<KhuyenMai> danhSachKhuyenMai = mainView.getDanhSachKhuyenMaiHienThi(); // Lấy danh sách đã lọc/hiển thị
        for (KhuyenMai km : danhSachKhuyenMai) {
            KhuyenMaiBox box = new KhuyenMaiBox(km);

            // --- START: Thêm Context Menu --- 
            ContextMenu contextMenu = new ContextMenu();
            MenuItem suaItem = new MenuItem("Sửa");
            suaItem.setOnAction(event -> {
                Stage owner = (Stage) getScene().getWindow();
                if (boDieuKhien.capNhatKhuyenMai(owner, km)) {
                    mainView.lamMoiGiaoDien();
                }
            });

            MenuItem xoaItem = new MenuItem("Xóa");
            xoaItem.setOnAction(event -> {
                Stage owner = (Stage) getScene().getWindow();
                if (boDieuKhien.xoaKhuyenMai(owner, km)) {
                    mainView.lamMoiGiaoDien();
                }
            });

            contextMenu.getItems().addAll(suaItem, xoaItem);
            // --- END: Thêm Context Menu ---

            box.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                if (event.getButton() == MouseButton.SECONDARY) { // Chuột phải
                    contextMenu.show(box, event.getScreenX(), event.getScreenY());
                } else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) { // Nhấn đúp chuột trái
                    Stage owner = (Stage) getScene().getWindow();
                    if (boDieuKhien.capNhatKhuyenMai(owner, km)) {
                        mainView.lamMoiGiaoDien();
                    }
                }
            });
            grid.getChildren().add(box);
        }
    }

    public void refresh(GiaoDienKhuyenMai mainView) {
        loadKhuyenMai(mainView);
    }
}
