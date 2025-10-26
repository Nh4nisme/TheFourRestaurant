package com.thefourrestaurant.view.khuyenmai;

import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.view.components.BaseBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public class KhuyenMaiBox extends BaseBox {

    private final String defaultStyle = "-fx-background-color: #f5f5f5; -fx-background-radius: 15; -fx-border-color: #e0e0e0; -fx-border-radius: 15; -fx-border-width: 1;";
    private final String selectedStyle = "-fx-background-color: #ffffff; -fx-background-radius: 15; -fx-border-color: #673E1F; -fx-border-radius: 15; -fx-border-width: 2; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 8, 0, 0, 4);";
    private final String hoverStyle = "-fx-background-color: #ffffff; -fx-background-radius: 15; -fx-border-color: #d0d0d0; -fx-border-radius: 15; -fx-border-width: 1; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 8, 0, 0, 4);";

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

        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPrefWidth(190);

        // Ten Khuyen Mai
        Label lblTenKM = new Label(khuyenMai.getTenKM());
        lblTenKM.setFont(Font.font("System", FontWeight.BOLD, 20));
        lblTenKM.setWrapText(true);
        lblTenKM.setTextAlignment(TextAlignment.CENTER);
        lblTenKM.setMaxWidth(190);
        lblTenKM.setStyle("-fx-text-fill: #000000;");

        // Giam gia
        String giamGiaStr = "";
        if (khuyenMai.getTyLe() != null && khuyenMai.getTyLe().compareTo(BigDecimal.ZERO) > 0) {
            giamGiaStr = khuyenMai.getTyLe().stripTrailingZeros().toPlainString() + "%";
        } else if (khuyenMai.getSoTien() != null && khuyenMai.getSoTien().compareTo(BigDecimal.ZERO) > 0) {
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            giamGiaStr = currencyFormatter.format(khuyenMai.getSoTien());
        } else {
            giamGiaStr = "Tặng món";
        }
        Label lblGiamGia = new Label(giamGiaStr);
        lblGiamGia.setFont(Font.font("System", FontWeight.BOLD, 20)); // Reduced size
        lblGiamGia.setWrapText(true);
        lblGiamGia.setTextAlignment(TextAlignment.CENTER);
        lblGiamGia.setStyle("-fx-text-fill: #d35400;"); // A nice orange color for discount

        // Thoi gian ap dung
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        String thoiGianApDung = "";
        if (khuyenMai.getNgayBatDau() != null && khuyenMai.getNgayKetThuc() != null) {
            thoiGianApDung = khuyenMai.getNgayBatDau().format(dateFormatter) + " - " + khuyenMai.getNgayKetThuc().format(dateFormatter);
        }
        Label lblThoiGian = new Label(thoiGianApDung);
        lblThoiGian.setFont(Font.font("System", FontWeight.NORMAL, 14));

        // Trang thai
        String status = "Chưa áp dụng";
        String statusStyle = "-fx-text-fill: #7f8c8d;"; // gray
        LocalDateTime now = LocalDateTime.now();
        if (khuyenMai.getNgayBatDau() != null && khuyenMai.getNgayKetThuc() != null) {
            if (now.isAfter(khuyenMai.getNgayKetThuc())) {
                status = "Đã hết hạn";
                statusStyle = "-fx-text-fill: #c0392b;"; // red
            } else if (now.isBefore(khuyenMai.getNgayBatDau())) {
                status = "Sắp diễn ra";
                statusStyle = "-fx-text-fill: #2980b9;"; // blue
            } else {
                status = "Đang diễn ra";
                statusStyle = "-fx-text-fill: #27ae60;"; // green
            }
        }
        Label lblTrangThai = new Label(status);
        lblTrangThai.setFont(Font.font("System", FontWeight.BOLD, 14));
        lblTrangThai.setStyle(statusStyle);


        contentBox.getChildren().addAll(
                lblTenKM,
                lblGiamGia,
                lblThoiGian,
                lblTrangThai
        );

        this.setOnMouseEntered(e -> {
            if (!getStyle().equals(selectedStyle)) {
                setStyle(hoverStyle);
            }
        });
        this.setOnMouseExited(e -> {
            if (!getStyle().equals(selectedStyle)) {
                setStyle(defaultStyle);
            }
        });

        this.getChildren().add(contentBox);
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
}
