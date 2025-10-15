package com.thefourrestaurant.view.components;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.VBox;

public abstract class BaseBox extends VBox {

    public BaseBox() {
        this.setPrefSize(120, 120);
        this.setStyle("-fx-background-color: white; -fx-border-color: #CCCCCC; -fx-border-radius: 10; -fx-background-radius: 10;");
        this.setAlignment(Pos.BOTTOM_CENTER);
        this.setCursor(Cursor.HAND);

        this.setOnMouseEntered(e -> this.setStyle("-fx-background-color: #E0E0E0; -fx-border-color: #673E1F;-fx-border-radius: 10; -fx-background-radius: 10;"));
        this.setOnMouseExited(e -> this.setStyle("-fx-background-color: white; -fx-border-color: #CCCCCC; -fx-border-radius: 10; -fx-background-radius: 10;"));
    }
}
