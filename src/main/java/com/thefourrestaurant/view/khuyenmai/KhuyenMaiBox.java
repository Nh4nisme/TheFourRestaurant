package com.thefourrestaurant.view.khuyenmai;

import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.view.components.BaseBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class KhuyenMaiBox extends BaseBox {

    private final String defaultStyle = "-fx-background-color: #f5f5f5; -fx-background-radius: 15; -fx-border-color: #e0e0e0; -fx-border-radius: 15; -fx-border-width: 1;";
    private final String selectedStyle = "-fx-background-color: #ffffff; -fx-background-radius: 15; -fx-border-color: #673E1F; -fx-border-radius: 15; -fx-border-width: 2; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 8, 0, 0, 4);";
    private final String hoverStyle = "-fx-background-color: #ffffff; -fx-background-radius: 15; -fx-border-color: #d0d0d0; -fx-border-radius: 15; -fx-border-width: 1; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 8, 0, 0, 4);";

    private StackPane deleteButton;

    private KhuyenMaiBox() {
        super();
        setPrefSize(220, 200);
        setMaxSize(220, 200);
        setSpacing(0);
        setPadding(Insets.EMPTY);
        setAlignment(Pos.TOP_CENTER);
    }

    public KhuyenMaiBox(KhuyenMai khuyenMai) {
        this();
        setPadding(new Insets(15));
        getStyleClass().add("sukien-box");
        setStyle(defaultStyle);

        StackPane mainContainer = new StackPane();

        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPrefWidth(190);

        // Ten Khuyen Mai
        Label lblTenKM = new Label(khuyenMai.getTenKM());
        lblTenKM.setFont(Font.font("System", FontWeight.BOLD, 20));
        lblTenKM.setWrapText(true);
        lblTenKM.setTextAlignment(TextAlignment.CENTER);
        lblTenKM.setMaxWidth(190);
        lblTenKM.setStyle("-fx-text-fill: #CC3333;");

        // Loai Khuyen Mai
        String loaiKMStr = khuyenMai.getLoaiKhuyenMai() != null ? khuyenMai.getLoaiKhuyenMai().getTenLoaiKM() : "Khuyến mãi chung";
        Label lblLoaiKM = new Label(loaiKMStr);
        lblLoaiKM.setFont(Font.font("System", FontWeight.BOLD, 20));
        lblLoaiKM.setWrapText(true);
        lblLoaiKM.setTextAlignment(TextAlignment.CENTER);
        lblLoaiKM.setStyle("-fx-text-fill: #FF6600;");

        // Thoi gian ap dung
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        String thoiGianApDung = "";
        if (khuyenMai.getNgayBatDau() != null && khuyenMai.getNgayKetThuc() != null) {
            thoiGianApDung = khuyenMai.getNgayBatDau().format(dateFormatter) + " - " + khuyenMai.getNgayKetThuc().format(dateFormatter);
        }
        Label lblThoiGian = new Label(thoiGianApDung);
        lblThoiGian.setFont(Font.font("System", FontWeight.NORMAL, 14));
        lblThoiGian.setStyle("-fx-text-fill: #673E1F;");

        // Trang thai
        String status = "Chưa áp dụng";
        String statusStyle = "-fx-text-fill: #7f8c8d;";
        LocalDateTime now = LocalDateTime.now();
        if (khuyenMai.getNgayBatDau() != null && khuyenMai.getNgayKetThuc() != null) {
            if (now.isAfter(khuyenMai.getNgayKetThuc())) {
                status = "Đã hết hạn";
                statusStyle = "-fx-text-fill: #c0392b;";
            } else if (now.isBefore(khuyenMai.getNgayBatDau())) {
                status = "Sắp diễn ra";
                statusStyle = "-fx-text-fill: #2980b9;";
            } else {
                status = "Đang diễn ra";
                statusStyle = "-fx-text-fill: #27ae60;";
            }
        }
        Label lblTrangThai = new Label(status);
        lblTrangThai.setFont(Font.font("System", FontWeight.BOLD, 14));
        lblTrangThai.setStyle(statusStyle);

        contentBox.getChildren().addAll(
                lblTenKM,
                lblLoaiKM,
                lblThoiGian,
                lblTrangThai
        );

        deleteButton = new StackPane();
        deleteButton.setVisible(false);
        deleteButton.setPrefSize(32, 32);
        deleteButton.setMaxSize(32, 32);
        deleteButton.setMinSize(32, 32);
        deleteButton.setStyle("-fx-background-color: rgba(255, 0, 0, 0.8); -fx-background-radius: 16; -fx-cursor: hand;");
        StackPane.setAlignment(deleteButton, Pos.TOP_RIGHT);
        StackPane.setMargin(deleteButton, new Insets(0, 0, 0, 0));

        Label xLabel = new Label("×");
        xLabel.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
        xLabel.setAlignment(Pos.CENTER);
        deleteButton.getChildren().add(xLabel);

        deleteButton.setOnMouseEntered(e -> {
            deleteButton.setStyle("-fx-background-color: rgba(200, 0, 0, 0.9); -fx-background-radius: 16; -fx-cursor: hand;");
        });
        deleteButton.setOnMouseExited(e -> {
            deleteButton.setStyle("-fx-background-color: rgba(255, 0, 0, 0.8); -fx-background-radius: 16; -fx-cursor: hand;");
        });

        mainContainer.getChildren().addAll(contentBox, deleteButton);

        this.setOnMouseEntered(e -> {
            if (!getStyle().equals(selectedStyle)) {
                setStyle(hoverStyle);
            }
            deleteButton.setVisible(true);
        });
        this.setOnMouseExited(e -> {
            if (!getStyle().equals(selectedStyle)) {
                setStyle(defaultStyle);
            }
            deleteButton.setVisible(false);
        });

        this.getChildren().add(mainContainer);
    }

    public void setDefaultStyle() {
        setStyle(defaultStyle);
    }

    public void setSelectedStyle() {
        setStyle(selectedStyle);
    }

    public static KhuyenMaiBox createThemMoiBox() {
        KhuyenMaiBox hop = new KhuyenMaiBox();
        hop.setAlignment(Pos.CENTER);
        hop.setSpacing(10);
        hop.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 15; -fx-border-color: #e0e0e0; -fx-border-radius: 15; -fx-border-width: 2;");

        try {
            Image plusImage = new Image(Objects.requireNonNull(
                    KhuyenMaiBox.class.getResourceAsStream("/com/thefourrestaurant/images/icon/Them.png")));
            ImageView plusImageView = new ImageView(plusImage);
            plusImageView.setFitWidth(40);
            plusImageView.setFitHeight(40);
            hop.getChildren().add(plusImageView);
        } catch (Exception e) {
            Label plusLabel = new Label("+");
            plusLabel.setFont(Font.font("System", FontWeight.BOLD, 36));
            plusLabel.setStyle("-fx-text-fill: #888888;");
            hop.getChildren().add(plusLabel);
        }

        Label themMoiLabel = new Label("Thêm khuyến mãi");
        themMoiLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        themMoiLabel.setStyle("-fx-text-fill: #666666;");
        hop.getChildren().add(themMoiLabel);

        hop.setOnMouseEntered(e -> hop.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 15; -fx-border-color: #6A4C34; -fx-border-radius: 15; -fx-border-width: 2; -fx-cursor: hand;"));
        hop.setOnMouseExited(e -> hop.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 15; -fx-border-color: #e0e0e0; -fx-border-radius: 15; -fx-border-width: 2;"));

        return hop;
    }

    public StackPane getDeleteButton() {
        return deleteButton;
    }
}
