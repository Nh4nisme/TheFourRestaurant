package com.thefourrestaurant.view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MonAnBox extends BaseBox {
    public MonAnBox(String ten, String gia, String imagePath) {
        this.setPadding(new Insets(10));
        this.setPrefSize(150, 200);
        this.setAlignment(Pos.CENTER);

        ImageView imageView = createImageView(imagePath);

        Label tenMon = new Label(ten);
        tenMon.setFont(Font.font("System", FontWeight.BOLD, 13));
        tenMon.setTextFill(Color.web("#2C5F5F"));

        Label lblGia = new Label(gia + " VND");
        lblGia.setFont(Font.font(12));
        lblGia.setTextFill(Color.web("#2C5F5F"));

        this.getChildren().addAll(imageView, tenMon, lblGia);
    }

    private ImageView createImageView(String imagePath) {
        ImageView imageView = new ImageView();
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);

        try {
            if (imagePath != null && !imagePath.isEmpty()) {
                Image image = new Image(imagePath, true); // true để tải ảnh nền
                if (image.isError()) {
                    // Nếu có lỗi khi tải ảnh từ path, dùng ảnh mặc định
                    imageView.setImage(getDefaultImage());
                } else {
                    imageView.setImage(image);
                }
            } else {
                // Nếu path rỗng, dùng ảnh mặc định
                imageView.setImage(getDefaultImage());
            }
        } catch (Exception e) {
            //System.err.println("Không thể tải ảnh món ăn: " + imagePath);
            imageView.setImage(getDefaultImage());
        }
        return imageView;
    }

    private Image getDefaultImage() {
        // Trả về ảnh placeholder từ resources
        return new Image(getClass().getResourceAsStream("/com/thefourrestaurant/images/icon/ThayAnh.png"));
    }
}
