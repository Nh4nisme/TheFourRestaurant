package com.thefourrestaurant.view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class LoaiMonAnBox extends BaseBox {

    private LoaiMonAnBox() {
        super(); // Gọi BaseBox
        setPrefSize(145, 180);
        setMaxSize(145, 180);
        // VBox sẽ chỉ chứa một StackPane, vì vậy không cần khoảng cách và padding
        setSpacing(0);
        setPadding(Insets.EMPTY);
    }

    public static LoaiMonAnBox createLoaiMonAnBox(String ten) {
        return createLoaiMonAnBox(ten, null);
    }

    public static LoaiMonAnBox createLoaiMonAnBox(String ten, String imagePath) {
        LoaiMonAnBox hopLoaiMonAn = new LoaiMonAnBox();
        hopLoaiMonAn.getStyleClass().add("loai-mon-an-box");

        ImageView imageView;
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                imageView = new ImageView(new Image(imagePath));
            } catch (Exception e) {
                System.err.println("Không thể tải ảnh: " + imagePath);
                imageView = createDefaultIcon();
            }
        } else {
            imageView = createDefaultIcon();
        }

        // Cho ảnh lấp đầy toàn bộ box
        imageView.setFitWidth(145);
        imageView.setFitHeight(180);

        Label nhanTen = new Label(ten);
        nhanTen.getStyleClass().add("loai-mon-an-name");
        nhanTen.setMaxWidth(Double.MAX_VALUE); // Cho nhãn rộng tối đa
        nhanTen.setAlignment(Pos.CENTER);
        // Thêm style để chữ dễ đọc trên nền ảnh
        nhanTen.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 8px;");
        nhanTen.setTextFill(Color.WHITE);

        // Dùng StackPane để xếp chữ lên trên ảnh
        StackPane stack = new StackPane();
        stack.getChildren().addAll(imageView, nhanTen);
        StackPane.setAlignment(nhanTen, Pos.BOTTOM_CENTER);

        // Bo tròn góc của nội dung để khớp với viền
        Rectangle clip = new Rectangle(145, 180);
        clip.setArcWidth(20); // Tương ứng với radius 10
        clip.setArcHeight(20); // Tương ứng với radius 10
        stack.setClip(clip);

        hopLoaiMonAn.getChildren().add(stack);

        return hopLoaiMonAn;
    }

    public static LoaiMonAnBox createThemMoiBox() {
        LoaiMonAnBox hop = new LoaiMonAnBox();
        hop.setAlignment(Pos.CENTER);
        hop.setSpacing(5);
        hop.getStyleClass().add("add-item-box");

        Image plusImage = new Image(LoaiMonAnBox.class.getResourceAsStream("/com/thefourrestaurant/images/icon/Them.png"));
        ImageView plusImageView = new ImageView(plusImage);
        plusImageView.setFitWidth(50);
        plusImageView.setFitHeight(50);

        Label themMoiLabel = new Label("Thêm loại mới");

        hop.getChildren().addAll(plusImageView, themMoiLabel);
        return hop;
    }

    private static ImageView createDefaultIcon() {
        Image anhMacDinh = new Image(LoaiMonAnBox.class.getResourceAsStream("/com/thefourrestaurant/images/icon/ThayAnh.png"));
        ImageView anhHienThi = new ImageView(anhMacDinh);
        return anhHienThi;
    }
}
