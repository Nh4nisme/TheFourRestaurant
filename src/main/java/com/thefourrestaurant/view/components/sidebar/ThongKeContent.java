package com.thefourrestaurant.view.components.sidebar;

import com.thefourrestaurant.view.components.ButtonSample;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;

public class ThongKeContent extends VBox {
    private final ToolBar navBar;
    private final GridPane grid;

    public ThongKeContent(String tenMucCon) {
        setSpacing(10);
        getStyleClass().add("thongke-root"); // CSS class gốc

        // === NavBarSecondary ===
        navBar = new ToolBar(
                new ButtonSample("Làm mới",45,16,1),
                new ButtonSample("Áp dụng",45,16,1),
                new ButtonSample("Xuất hóa đơn",45,16,1)
        );
        navBar.getStyleClass().add("thongke-navbar");

        // === GridPane (vùng hiển thị nội dung) ===
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);
        grid.setGridLinesVisible(true);
        grid.getStyleClass().add("thongke-grid");

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        grid.getColumnConstraints().addAll(col1, col2);

        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(50);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(50);
        grid.getRowConstraints().addAll(row1, row2);

        VBox.setVgrow(grid, Priority.ALWAYS);
        HBox.setHgrow(grid, Priority.ALWAYS);

        // tạo 4 ô mẫu (2x2)
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                VBox box = new VBox();
                box.getStyleClass().add("thongke-box");
                grid.add(box, j, i);
            }
        }

        getChildren().addAll(navBar, grid);

        // === Nhúng CSS ===
        getStylesheets().add(getClass().getResource("/com/thefourrestaurant/css/Application.css").toExternalForm());
    }

    // === Các phương thức chỉnh nội dung ===
    public ToolBar getNavBar() {
        return navBar;
    }

    public GridPane getGrid() {
        return grid;
    }

    /** Xóa toàn bộ nội dung grid và thêm mới */
    public void setContent(VBox... panes) {
        grid.getChildren().clear();
        int cols = 2;
        for (int i = 0; i < panes.length; i++) {
            int row = i / cols;
            int col = i % cols;
            grid.add(panes[i], col, row);
        }
    }

    /** Cập nhật nhãn tiêu đề NavBar */
    public void setTitle(String title) {
        ((Label) navBar.getItems().get(0)).setText(title);
    }
}

