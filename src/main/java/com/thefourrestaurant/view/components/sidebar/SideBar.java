    package com.thefourrestaurant.view.components.sidebar;

    import com.thefourrestaurant.util.ClockText;

    import javafx.animation.ScaleTransition;
    import javafx.geometry.Insets;
    import javafx.geometry.Pos;
    import javafx.scene.control.Button;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.layout.VBox;
    import javafx.scene.text.Font;
    import javafx.util.Duration;

    import java.util.HashMap;
    import java.util.Map;
    import java.util.Objects;

    public class SideBar extends VBox {

        public final VBox groupButton;
        private final Map<String, Button> buttons = new HashMap<>();

        public SideBar(){
            Font montserrat = Font.loadFont(getClass().getResourceAsStream("com/thefourrestaurant/fonts/Montserrat-SemiBold.ttf"),16);
            setPrefWidth(50);
            setStyle("-fx-background-color: #1E424D");
            setAlignment(Pos.TOP_CENTER);
            setPadding(new Insets(-10,17,14,17));
            setSpacing(50);


            // Phan nay la Logo cua sideBar
            ImageView logoImg = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/thefourrestaurant/images/icon/logoIcon.png"))));
            logoImg.setFitWidth(100);
            logoImg.setFitHeight(100);
            logoImg.setPreserveRatio(true);

            // Phan Menu Button
            groupButton =  new VBox(10);
            groupButton.setAlignment(Pos.CENTER);
            groupButton.setPadding(new Insets(10,9,10,9));

            // Array các nút: {tên biến, đường dẫn icon}
            String[][] buttonData = {
                    {"ThongKe", "/com/thefourrestaurant/images/icon/thongKeIcon.png"},
                    {"HoaDon", "/com/thefourrestaurant/images/icon/hoaDonIcon.png"},
                    {"TaiKhoan", "/com/thefourrestaurant/images/icon/taiKhoanIcon.png"},
                    {"KhachHang", "/com/thefourrestaurant/images/icon/khachHangIcon.png"},
                    {"DanhMuc", "/com/thefourrestaurant/images/icon/danhMucIcon.png"},
                    {"CaiDat", "/com/thefourrestaurant/images/icon/caiDatIcon.png"}
            };

            for (String[] b : buttonData) {
                Button btn = createIconButton(b[1], 45, 45);
                buttons.put(b[0], btn);
                groupButton.getChildren().add(btn);
            }

            // Tao Vbox rong
            VBox BoDemGio = new VBox();
            BoDemGio.setAlignment(Pos.BOTTOM_CENTER);
            BoDemGio.setPadding(new Insets(10,10,10,10));
            BoDemGio.setPrefHeight(500);
            ClockText boDemGioText = new ClockText();
            boDemGioText.setFont(montserrat);
            boDemGioText.setStyle("-fx-fill: #DDB248; -fx-font-size: 15px; -fx-font-weight: bold;");
            BoDemGio.getChildren().add(boDemGioText);



            //Them vao VBox sidebar chinh
            getChildren().addAll(logoImg,groupButton,BoDemGio);
        }

        private Button createIconButton(String imagePath, double width, double height) {
            ImageView icon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath))));
            icon.setFitWidth(width);
            icon.setFitHeight(height);

            Button button = new Button();
            button.setGraphic(icon);

            addHoverEffect(button);
            return button;
        }

        private void addHoverEffect(Button btn) {
            btn.setStyle("-fx-background-color: transparent;");
            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(150), btn);
            scaleUp.setToX(1.08);
            scaleUp.setToY(1.08);

            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(150), btn);
            scaleDown.setToX(1);
            scaleDown.setToY(1);

            btn.setOnMouseEntered(e -> scaleUp.playFromStart());
            btn.setOnMouseExited(e -> scaleDown.playFromStart());
        }

        public Button getButton(String name) {
            return buttons.get(name);
        }
    }