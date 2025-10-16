package com.thefourrestaurant.view.components.sidebar;

import com.thefourrestaurant.view.components.ButtonSample;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;


public class ThongKeContent extends VBox {

    private final ToolBar TKNavBar;
    private final GridPane gridBieuDo;

    public ThongKeContent() {
        TKNavBar = new ToolBar(
                new ButtonSample("Làm mới",45,16,1),
                new ButtonSample("Áp dụng",45,16,1),
                new ButtonSample("Xuất hóa đơn",45,16,1)
        );
        TKNavBar.getStyleClass().add("thongke-navbar");

        //gridPane chứa các biểu đồ
        gridBieuDo = new GridPane();
        VBox.setVgrow(gridBieuDo, Priority.ALWAYS);
        gridBieuDo.setPadding(new Insets(10));
        gridBieuDo.setHgap(10);
        gridBieuDo.setVgap(10);

        VBox box1 = new VBox(5);
        box1.setStyle("-fx-background-color: lightblue; -fx-padding: 10; -fx-border-radius: 5;");

        VBox box2 = new VBox(5);
        box2.setStyle("-fx-background-color: lightgreen; -fx-padding: 10;");

        VBox box3 = new VBox(5);
        box3.setStyle("-fx-background-color: lightcoral; -fx-padding: 10;");

        VBox box4 = new VBox(5);
        box4.setStyle("-fx-background-color: lightyellow; -fx-padding: 10;");

        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(50);
        gridBieuDo.getColumnConstraints().addAll(col, col);

        RowConstraints row = new RowConstraints();
        row.setPercentHeight(50);
        gridBieuDo.getRowConstraints().addAll(row, row);


        gridBieuDo.add(box1,0,0);
        gridBieuDo.add(box2,1,0);
        gridBieuDo.add(box3,0,1);
        gridBieuDo.add(box4,1,1);

        getChildren().addAll(TKNavBar,gridBieuDo);
    }
}