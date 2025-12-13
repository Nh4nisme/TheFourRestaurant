package com.thefourrestaurant.view.ban;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thefourrestaurant.DAO.BanDAO;
import com.thefourrestaurant.DAO.PhieuDatBanDAO;
import com.thefourrestaurant.model.Ban;
import com.thefourrestaurant.model.PhieuDatBan;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.paint.Color;

public class QuanLiBan extends VBox {

    private final BanDAO banDAO = new BanDAO();
    private PhieuDatBanDAO pdbDAO = new PhieuDatBanDAO();
    private final Pane khuVucBan = new Pane(); // nơi hiển thị bàn
    private final Label lblBreadcrumb = new Label();
    
    private StackPane mainContent;
    private String context;
    private boolean choPhepDiChuyen = false;
    private final List<Ban> dsBanDangChon = new ArrayList<>();
    private static final Map<String, Image> cacheAnhBan = new HashMap<>();

    public QuanLiBan(StackPane mainContent, String context) {
    	this.mainContent = mainContent;
    	this.context = context;
        // === Cấu hình chính cho layout ===
        this.setPrefSize(1200, 700);
        this.setSpacing(0);
        this.setAlignment(Pos.TOP_CENTER);
        this.setStyle("-fx-background-color: #F5F5F5;");
        
        Platform.runLater(() -> hienThiBanTheoTang("TG000001"));

        //Toolbar
        ButtonSample btnThemBan = new ButtonSample("Thêm bàn", 45, 16, 3);
        btnThemBan.setOnAction(e -> moPopupTuyChinhBan(null));
        ButtonSample btnLuuSoDo = new ButtonSample("Lưu sơ đồ", 45, 16, 3);
        btnLuuSoDo.setOnAction(e -> {
            this.choPhepDiChuyen = false;
            Alert alert = new Alert(Alert.AlertType.INFORMATION, 
                    "Đã lưu sơ đồ! Chế độ di chuyển đã tắt.");
            alert.initOwner(this.getScene().getWindow());
            alert.showAndWait();
        });

        ToolBar toolBar = new ToolBar(btnThemBan, btnLuuSoDo);
        toolBar.setStyle("-fx-background-color: #1E424D");
        toolBar.setPadding(new Insets(10, 10, 10, 10));

        VBox thanhTren = new VBox(toolBar);
        thanhTren.setSpacing(0);
        thanhTren.setAlignment(Pos.CENTER_LEFT);
        thanhTren.setPrefWidth(Double.MAX_VALUE);
        HBox.setHgrow(thanhTren, Priority.ALWAYS);

        // === Khu vực hiển thị bàn ===
        khuVucBan.setPadding(new Insets(20));
        khuVucBan.setPrefSize(1000, 600);
        khuVucBan.setStyle("-fx-background-color: #F5F5F5;");
        VBox.setVgrow(khuVucBan, Priority.ALWAYS);

        // === Gắn vào layout ===
        this.getChildren().addAll(thanhTren, khuVucBan);
    }

    // Hiển thị bàn theo tầng
    public void hienThiBanTheoTang(String maTang) {
        Map<String, PhieuDatBan> mapDangPhucVu = pdbDAO.layTatCaPhieuDangPhucVuTheoTang();
        Map<String, PhieuDatBan> mapDatTruoc = pdbDAO.layTatCaPhieuDatTruocTheoTang();

        khuVucBan.getChildren().clear();

        lblBreadcrumb.setText("Trang chủ / Quản lý bàn / Tầng " + maTang.replace("TG00000", ""));

        Platform.runLater(() -> {
            if (khuVucBan.getWidth() > 0 && khuVucBan.getHeight() > 0) {
                setBackgroundTheoTang(maTang);
            } else {
                khuVucBan.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> setBackgroundTheoTang(maTang));
            }
        });

        List<Ban> dsBan = banDAO.layTheoTang(maTang);

        if (dsBan.isEmpty()) {
            Label lblThongBao = new Label("Không có bàn nào trong tầng này.");
            lblThongBao.setStyle("-fx-font-size: 18px; -fx-text-fill: #666;");
            khuVucBan.getChildren().add(lblThongBao);
            return;
        }

        for (Ban b : dsBan) {
            taoBan(khuVucBan, b, mapDangPhucVu, mapDatTruoc);
        }
    }

    // 🔹 Đặt background theo tầng
    private void setBackgroundTheoTang(String maTang) {
        String path = switch (maTang) {
            case "TG000001" -> "/com/thefourrestaurant/images/Tang/BG_Tang1.png";
            case "TG000002" -> "/com/thefourrestaurant/images/Tang/BG_Tang2.png";
            case "TG000003" -> "/com/thefourrestaurant/images/Tang/BG_Tang3.png";
            case "TG000004" -> "/com/thefourrestaurant/images/Tang/BG_Tang4.png";
            case "TG000005" -> "/com/thefourrestaurant/images/Tang/BG_Tang5.png";
            case "TG000006" -> "/com/thefourrestaurant/images/Tang/BG_Tang6.png";
            case "TG000007" -> "/com/thefourrestaurant/images/Tang/BG_Tang7.png";
            default -> "/com/thefourrestaurant/images/Tang/BG_Tang1.png";
        };

        try {
            Image anhNen = new Image(getClass().getResource(path).toExternalForm());

            Runnable updateBackground = () -> {
                BackgroundSize bgs = new BackgroundSize(
                        khuVucBan.getWidth(), khuVucBan.getHeight(), false, false, false, false
                );
                BackgroundImage bgImg = new BackgroundImage(
                        anhNen,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        bgs
                );
                khuVucBan.setBackground(new Background(bgImg));
            };

            updateBackground.run();
            khuVucBan.widthProperty().addListener((obs, oldVal, newVal) -> updateBackground.run());
            khuVucBan.heightProperty().addListener((obs, oldVal, newVal) -> updateBackground.run());

        } catch (Exception e) {
            e.printStackTrace();
            khuVucBan.setStyle("-fx-background-color: lightgray;");
        }
    }

    void taoBan(Pane pane, Ban ban,
            Map<String, PhieuDatBan> mapDangPhucVu,
            Map<String, PhieuDatBan> mapDatTruoc) {

	    // ✅ Cache ảnh bàn
	    Image img;
	    try {
	        img = cacheAnhBan.computeIfAbsent(
	                ban.getAnhBan(),
	                path -> new Image(getClass().getResourceAsStream(path))
	        );
	    } catch (Exception e) {
	        img = new Image(getClass().getResourceAsStream("/com/thefourrestaurant/images/Ban/Ban_8.png"));
	    }
	
	    ImageView imgBan = new ImageView(img);
	    imgBan.setFitWidth(180);
	    imgBan.setFitHeight(150);
	    imgBan.setPreserveRatio(true);
	
	    Label lblTenBan = new Label(ban.getTenBan());
	    lblTenBan.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333;");
	
	    StackPane khungBan = new StackPane(imgBan, lblTenBan);
	    khungBan.setLayoutX(ban.getToaDoX());
	    khungBan.setLayoutY(ban.getToaDoY());
	
	    // ✅ Lấy phiếu đang phục vụ từ map (không truy vấn SQL)
	    PhieuDatBan pdbDangPhucVu = mapDangPhucVu.get(ban.getMaBan());
	    if (pdbDangPhucVu != null) {
	    	showCountdown(ban, pdbDangPhucVu, khungBan);
	    }
	
	    // ✅ Border theo trạng thái
	    String borderStyle = switch (ban.getTrangThai().trim()) {
	        case "Bảo trì" -> "-fx-border-color: green; -fx-border-width: 3; -fx-border-radius: 12;";
	        case "Đang phục vụ" -> "-fx-border-color: orange; -fx-border-width: 3; -fx-border-radius: 12;";
	        case "Đặt trước" -> {
	            String style = "-fx-border-color: lightgray; -fx-border-width: 3; -fx-border-radius: 12;";
	
	            // ✅ Lấy phiếu đặt trước từ map (không truy vấn SQL)
	            PhieuDatBan pdbDatTruoc = mapDatTruoc.get(ban.getMaBan());
	            if (pdbDatTruoc != null && pdbDatTruoc.getNgayDat() != null) {
	                long hours = java.time.Duration.between(LocalDateTime.now(), pdbDatTruoc.getNgayDat()).toHours();
	                if (hours >= 0 && hours < 2) {
	                    style = "-fx-border-color: deepskyblue; -fx-border-width: 3; -fx-border-radius: 12;";
	                }
	            }
	
	            yield style;
	        }
	        default -> "-fx-border-color: lightgray; -fx-border-width: 3; -fx-border-radius: 12;";
	    };
	
	    khungBan.setStyle(borderStyle);
	
	    // ✅ Hover
	    final String hoverStyle = borderStyle + "-fx-effect: dropshadow(gaussian, gray, 10, 0, 0, 0);";
	    khungBan.setOnMouseEntered(e -> khungBan.setStyle(hoverStyle));
	    khungBan.setOnMouseExited(e -> khungBan.setStyle(borderStyle));
	
	    // ✅ Kéo thả bàn
	    final double[] offset = new double[2];
	    khungBan.setOnMousePressed(e -> {
	        offset[0] = e.getSceneX() - khungBan.getLayoutX();
	        offset[1] = e.getSceneY() - khungBan.getLayoutY();
	    });
	
	    khungBan.setOnMouseDragged(e -> {
	        if (!choPhepDiChuyen) return;
	        khungBan.setLayoutX(e.getSceneX() - offset[0]);
	        khungBan.setLayoutY(e.getSceneY() - offset[1]);
	    });
	
	    khungBan.setOnMouseReleased(e -> {
	        if (!choPhepDiChuyen) return;
	        banDAO.capNhatToaDo(ban.getMaBan(), (int) khungBan.getLayoutX(), (int) khungBan.getLayoutY());
	    });
	
	    // ✅ Click chọn bàn / mở popup
	    khungBan.setOnMouseClicked(e -> {
	        PauseTransition delay = new PauseTransition(Duration.millis(200));
	
	        if (e.getClickCount() == 1) {
	            delay.setOnFinished(ev -> {
	                if (dsBanDangChon.contains(ban)) {
	                    dsBanDangChon.remove(ban);
	                    khungBan.setBackground(null);
	                } else {
	                    dsBanDangChon.add(ban);
	                    khungBan.setBackground(new Background(
	                            new BackgroundFill(Color.rgb(255, 200, 100, 0.6), new CornerRadii(10), Insets.EMPTY)
	                    ));
	                }
	            });
	            delay.playFromStart();
	        }
	
	        else if (e.getClickCount() == 2) {
	            delay.stop();
	
	            if ("QUAN_LY_BAN".equals(context)) {
	                moPopupTuyChinhBan(ban);
	            } else if ("DAT_BAN".equals(context)) {
	                if (pdbDangPhucVu == null) {
	                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Bàn \"" + ban.getTenBan() + "\" hiện chưa có phiếu hoạt động.");
	                    alert.show();
	                    return;
	                }
	                mainContent.getChildren().setAll(new GiaoDienChiTietBan(mainContent, ban, pdbDangPhucVu));
	            }
	        }
	    });
	
	    khungBan.setUserData(ban.getMaBan());
	    pane.getChildren().add(khungBan);
	}

    
    private void moPopupTuyChinhBan(Ban ban) {
        GiaoDienTuyChinhBan giaoDien = new GiaoDienTuyChinhBan(ban);
        
        Stage popup = new Stage();
        popup.setTitle(ban != null ? "Chỉnh sửa bàn" : "Thêm bàn mới");
        popup.setScene(new javafx.scene.Scene(giaoDien, 500, 270));
        popup.initOwner(this.getScene().getWindow()); // Gắn với cửa sổ cha
        popup.setResizable(false);
        popup.centerOnScreen();
        
        giaoDien.getBtnDiChuyen().setOnAction(e -> {
            this.choPhepDiChuyen = true;
            popup.close(); // Đóng popup
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Chế độ di chuyển đã bật! Bạn có thể kéo bàn để thay đổi vị trí.");
            alert.initOwner(this.getScene().getWindow());
            alert.showAndWait();
        });
        

        
        // Hiển thị popup
        popup.showAndWait();
    }

    public Pane getKhuVucBan() {
        return khuVucBan;
    }
    
    public List<Ban> getDsBanDangChon() {
        return dsBanDangChon;
    }
    
    public void clearAllBan() {
        khuVucBan.getChildren().clear();
    }
    
    public void hienThiBanTheoDieuKien(String maTang, String trangThai, String loaiBan, int soGhe) {
        clearAllBan();

        // Breadcrumb
        String tangText = (maTang != null) ? maTang.replace("TG00000", "") : "?";
        lblBreadcrumb.setText("Trang chủ / Quản lý bàn / Tầng " + tangText);

        // Background
        if (maTang != null) {
            Platform.runLater(() -> {
                if (khuVucBan.getWidth() > 0 && khuVucBan.getHeight() > 0) {
                    setBackgroundTheoTang(maTang);
                } else {
                    khuVucBan.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> setBackgroundTheoTang(maTang));
                }
            });
        }

        List<Ban> dsBan = (maTang != null) ? banDAO.layTheoTang(maTang) : banDAO.layTatCaBan();

        if (dsBan.isEmpty()) {
            Label lblThongBao = new Label("Không có bàn nào thỏa điều kiện.");
            lblThongBao.setStyle("-fx-font-size: 18px; -fx-text-fill: #666;");
            khuVucBan.getChildren().add(lblThongBao);
            return;
        }

        for (Ban b : dsBan) {
            boolean thoaDieuKien = true;

            if (trangThai != null && !trangThai.equals("Tất cả") && !b.getTrangThai().equals(trangThai)) {
                thoaDieuKien = false;
            }

            if (loaiBan != null && !loaiBan.equals("Tất cả") && !b.getLoaiBan().equals(loaiBan)) {
                thoaDieuKien = false;
            }

            if (soGhe > 0 && b.getLoaiBan().getSoChoNgoi() != soGhe) {
                thoaDieuKien = false;
            }

            if (thoaDieuKien) {
                taoBan(khuVucBan, b, new HashMap<>(), new HashMap<>());
            }
        }
    }
    
    public void showCountdown(Ban ban, PhieuDatBan pdb, StackPane khungBan) {
        LocalDateTime start = pdb.getNgayDat();
        LocalDateTime end = start.plusHours(2);
        LocalDateTime now = LocalDateTime.now();

        // Nếu hết giờ
        if (now.isAfter(end)) {
            banDAO.capNhatTrangThai(ban.getMaBan(), "Trống");
            return;
        }

        // Tạo label countdown
        Label lbl = new Label();
        lbl.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: red;" +
            "-fx-background-color: rgba(0,0,0,0.6);" +
            "-fx-padding: 2 8;" +
            "-fx-background-radius: 6;"
        );

        StackPane.setAlignment(lbl, Pos.TOP_CENTER);
        StackPane.setMargin(lbl, new Insets(-35, 0, 0, 0));
        khungBan.getChildren().add(lbl);

        startCountdownForDangPhucVu(ban, pdb, lbl);
    }
    
    public void startCountdownForDangPhucVu(Ban ban, PhieuDatBan pdb, Label countdownLabel) {
        LocalDateTime start = pdb.getNgayDat();             // thời điểm phục vụ
        LocalDateTime end = start.plusHours(2);             // khách ngồi tối đa 2h

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> {
                LocalDateTime now = LocalDateTime.now();
                java.time.Duration remaining = java.time.Duration.between(now, end);

                long seconds = remaining.getSeconds();

                if (seconds <= 0) {
                    countdownLabel.setText("Hết giờ");
                    countdownLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    ban.setTrangThai("Hết giờ");
                    return;
                }

                long h = seconds / 3600;
                long m = (seconds % 3600) / 60;
                long s = seconds % 60;

                countdownLabel.setText(String.format("%02d:%02d:%02d", h, m, s));

                // 15 phút cuối đổi màu đỏ
                if (seconds <= 900) {
                    countdownLabel.setStyle("-fx-text-fill: red;");
                } else {
                    countdownLabel.setStyle("-fx-text-fill: black;");
                }
            })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public StackPane timKhungBanTheoMa(String maBan) {
        for (Node n : khuVucBan.getChildren()) {
            if (n instanceof StackPane sp) {
                if (sp.getUserData() != null && sp.getUserData().equals(maBan)) {
                    return sp;
                }
            }
        }
        return null;
    }
    




}