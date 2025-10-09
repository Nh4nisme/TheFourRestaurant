package com.thefourrestaurant.view.components;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoaiMonAnBox extends BaseBox {

    private LoaiMonAnBox() {
        super(); // Gọi BaseBox
        setPrefSize(145, 180);
        setMaxSize(145, 180);
    }

    // Hàm tạo một hộp gắn style
    public static LoaiMonAnBox createLoaiMonAnBox(String ten) {
        LoaiMonAnBox hopLoaiMonAn = new LoaiMonAnBox();
        hopLoaiMonAn.getStyleClass().add("loai-mon-an-box");

        Label nhanTen = new Label(ten);
        nhanTen.getStyleClass().add("loai-mon-an-name");

        hopLoaiMonAn.getChildren().add(nhanTen);
        return hopLoaiMonAn;
    }

    // Hàm thêm loại món
    public static LoaiMonAnBox createThemMoiBox() {
        LoaiMonAnBox hop = new LoaiMonAnBox();
        hop.setSpacing(5); // Thêm khoảng cách giữa ảnh và chữ
        hop.getStyleClass().add("add-item-box");

        Image plusImage = new Image(LoaiMonAnBox.class.getResourceAsStream("/com/thefourrestaurant/images/icon/Them.png"));
        ImageView plusImageView = new ImageView(plusImage);
        plusImageView.setFitWidth(50);
        plusImageView.setFitHeight(50);

        Label themMoiLabel = new Label("Thêm loại mới");

        hop.getChildren().addAll(plusImageView, themMoiLabel);
        return hop;
    }

}
