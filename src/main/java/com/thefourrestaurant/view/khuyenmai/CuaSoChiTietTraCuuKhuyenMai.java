package com.thefourrestaurant.view.khuyenmai;

import com.thefourrestaurant.controller.KhuyenMaiController;
import com.thefourrestaurant.model.DieuKien_Mon;
import com.thefourrestaurant.model.DieuKien_MonTang;
import com.thefourrestaurant.model.KhungGio;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.model.KhuyenMai_DieuKien;
import com.thefourrestaurant.view.components.ButtonSample;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class CuaSoChiTietTraCuuKhuyenMai extends Stage {

    private final KhuyenMai khuyenMai;
    private final KhuyenMaiController controller;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public CuaSoChiTietTraCuuKhuyenMai(Stage owner, KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
        this.controller = new KhuyenMaiController();

        this.initOwner(owner);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Chi Tiết Khuyến Mãi");

        // Tải font Montserrat
        Font fontMontserrat = null;
        try (InputStream luongFont = getClass().getResourceAsStream("/com/thefourrestaurant/fonts/Montserrat-SemiBold.ttf")) {
            if (luongFont != null) {
                fontMontserrat = Font.loadFont(luongFont, 14);
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tải font: " + e.getMessage());
        }
        String kieuFontStyle = (fontMontserrat != null) ? "-fx-font-family: '" + fontMontserrat.getFamily() + "';" : "";

        BorderPane layoutChinh = new BorderPane();

        // Header
        Label nhanTieuDe = new Label("Thông tin chi tiết khuyến mãi");
        nhanTieuDe.setStyle(kieuFontStyle + "-fx-text-fill: #D4A017; -fx-font-size: 18px; -fx-font-weight: bold;");
        HBox hopTieuDe = new HBox(nhanTieuDe);
        hopTieuDe.setAlignment(Pos.CENTER_LEFT);
        hopTieuDe.setPadding(new Insets(15));
        hopTieuDe.setStyle("-fx-background-color: #1E424D;");

        // Center - Scrollable content
        VBox hopNoiDung = new VBox(20);
        hopNoiDung.setPadding(new Insets(20));
        hopNoiDung.setStyle("-fx-background-color: white;");

        // 1. Thông tin chung
        VBox sectionChung = createSection("Thông tin chung", kieuFontStyle);
        GridPane gridChung = createGridInfo(kieuFontStyle);

        addInfoRow(gridChung, "Mã Khuyến Mãi:", khuyenMai.getMaKM(), 0, kieuFontStyle);
        addInfoRow(gridChung, "Tên Khuyến Mãi:", khuyenMai.getTenKM(), 1, kieuFontStyle);
        addInfoRow(gridChung, "Kiểu Khuyến Mãi:", khuyenMai.laKieuMaGiamGia() ? "Mã giảm giá" : "Sự kiện", 2, kieuFontStyle);
        if (khuyenMai.laKieuMaGiamGia()) {
            addInfoRow(gridChung, "Mã Code:", khuyenMai.getMaCode(), 3, kieuFontStyle);
            addInfoRow(gridChung, "Số lượt sử dụng:", String.valueOf(khuyenMai.getSoLuotSuDung()), 4, kieuFontStyle);
        }
        addInfoRow(gridChung, "Loại Khuyến Mãi:", khuyenMai.getLoaiKhuyenMai() != null ? khuyenMai.getLoaiKhuyenMai().getTenLoaiKM() : "N/A", 5, kieuFontStyle);
        addInfoRow(gridChung, "Thời gian bắt đầu:", khuyenMai.getNgayBatDau() != null ? khuyenMai.getNgayBatDau().format(dateFormatter) : "N/A", 6, kieuFontStyle);
        addInfoRow(gridChung, "Thời gian kết thúc:", khuyenMai.getNgayKetThuc() != null ? khuyenMai.getNgayKetThuc().format(dateFormatter) : "N/A", 7, kieuFontStyle);

        Label lblMoTa = new Label(khuyenMai.getMoTa());
        lblMoTa.setWrapText(true);
        lblMoTa.setStyle(kieuFontStyle + "-fx-text-fill: #555555; -fx-italic: true;");
        VBox boxMoTa = new VBox(5, new Label("Mô tả:"), lblMoTa);

        sectionChung.getChildren().addAll(gridChung, boxMoTa);

        // 2. Điều kiện áp dụng
        VBox sectionDieuKien = createSection("Điều kiện áp dụng", kieuFontStyle);
        List<KhuyenMai_DieuKien> dsDieuKien = controller.layDieuKienTheoMaKM(khuyenMai.getMaKM());
        if (dsDieuKien.isEmpty()) {
            sectionDieuKien.getChildren().add(new Label("Không có điều kiện cụ thể."));
        } else {
            for (KhuyenMai_DieuKien dk : dsDieuKien) {
                sectionDieuKien.getChildren().add(createDieuKienNode(dk, kieuFontStyle));
            }
        }

        // 3. Khung giờ áp dụng
        VBox sectionKhungGio = createSection("Khung giờ áp dụng", kieuFontStyle);
        List<KhungGio> dsKhungGio = controller.layKhungGioTheoMaKM(khuyenMai.getMaKM());
        if (dsKhungGio.isEmpty()) {
            sectionKhungGio.getChildren().add(new Label("Áp dụng cho tất cả khung giờ."));
        } else {
            for (KhungGio kg : dsKhungGio) {
                String text = String.format("- Từ %s đến %s (%s)",
                        kg.getGioBatDau().toString(),
                        kg.getGioKetThuc().toString(),
                        kg.isLapLaiHangNgay() ? "Hàng ngày" : "Một lần");
                Label lblKG = new Label(text);
                lblKG.setStyle(kieuFontStyle + "-fx-text-fill: #333333;");
                sectionKhungGio.getChildren().add(lblKG);
            }
        }

        hopNoiDung.getChildren().addAll(sectionChung, sectionDieuKien, sectionKhungGio);

        ScrollPane cuonNoiDung = new ScrollPane(hopNoiDung);
        cuonNoiDung.setFitToWidth(true);
        cuonNoiDung.setStyle("-fx-background-color: transparent; -fx-background: white;");

        // Footer
        HBox hopChanTrang = new HBox(10);
        hopChanTrang.setPadding(new Insets(15));
        hopChanTrang.setAlignment(Pos.CENTER_RIGHT);
        hopChanTrang.setStyle("-fx-background-color: #F0F0F0; -fx-border-color: #E0E0E0; -fx-border-width: 1 0 0 0;");
        Button nutDong = new ButtonSample("Đóng", 35, 14, 2);
        nutDong.setOnAction(e -> this.close());
        hopChanTrang.getChildren().add(nutDong);

        layoutChinh.setTop(hopTieuDe);
        layoutChinh.setCenter(cuonNoiDung);
        layoutChinh.setBottom(hopChanTrang);

        Scene khungCanh = new Scene(layoutChinh, 550, 750);
        URL urlCSS = getClass().getResource("/com/thefourrestaurant/css/Application.css");
        if (urlCSS != null) {
            khungCanh.getStylesheets().add(urlCSS.toExternalForm());
        }
        this.setScene(khungCanh);
        this.setResizable(true);
        this.centerOnScreen();
    }

    private VBox createSection(String title, String kieuFont) {
        VBox section = new VBox(10);
        Label lblTitle = new Label(title);
        lblTitle.setStyle(kieuFont + "-fx-text-fill: #1E424D; -fx-font-size: 16px; -fx-font-weight: bold;");
        Separator sep = new Separator();
        section.getChildren().addAll(lblTitle, sep);
        return section;
    }

    private GridPane createGridInfo(String kieuFont) {
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(15);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setMinWidth(150);
        grid.getColumnConstraints().add(col1);
        return grid;
    }

    private void addInfoRow(GridPane grid, String label, String value, int row, String kieuFont) {
        Label lbl = new Label(label);
        lbl.setStyle(kieuFont + "-fx-text-fill: #777777; -fx-font-weight: bold;");
        Label val = new Label(value != null ? value : "N/A");
        val.setStyle(kieuFont + "-fx-text-fill: #333333;");
        grid.add(lbl, 0, row);
        grid.add(val, 1, row);
    }

    private Node createDieuKienNode(KhuyenMai_DieuKien dk, String kieuFont) {
        VBox box = new VBox(5);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: #F9F9F9; -fx-border-color: #E0E0E0; -fx-border-radius: 5; -fx-background-radius: 5;");

        Text title = new Text(dk.getMoTaDieuKien() != null ? dk.getMoTaDieuKien() : "Quy tắc áp dụng:");
        title.setFont(Font.font("System", FontWeight.BOLD, 14));

        TextFlow flow = new TextFlow();

        String loai = dk.getLoaiApDung();
        if ("GIAM_TRUC_TIEP".equals(loai)) {
            appendGiamGiaText(flow, dk);
            flow.getChildren().add(new Text(" cho các món: "));
            appendMonAnList(flow, dk.getDanhSachMonDieuKien());
        } else if ("THEO_COMBO".equals(loai)) {
            flow.getChildren().add(new Text("Khi mua Combo gồm: "));
            appendMonAnList(flow, dk.getDanhSachMonDieuKien());
            flow.getChildren().add(new Text(" => "));
            appendGiamGiaText(flow, dk);
        } else if ("MUA_X_GIAM_Y".equals(loai)) {
            flow.getChildren().add(new Text("Mua: "));
            appendMonAnList(flow, dk.getDanhSachMonDieuKien().stream().filter(m -> "MUA".equals(m.getVaiTro())).toList());
            flow.getChildren().add(new Text(" để được "));
            appendGiamGiaText(flow, dk);
            flow.getChildren().add(new Text(" cho: "));
            appendMonAnList(flow, dk.getDanhSachMonDieuKien().stream().filter(m -> "NHAN_GIAM".equals(m.getVaiTro())).toList());
        }

        if (dk.getDanhSachMonTang() != null && !dk.getDanhSachMonTang().isEmpty()) {
            flow.getChildren().add(new Text("\n+ Tặng kèm: "));
            if (dk.getSoLuongTang() != null && dk.getSoLuongTang() > 1) {
                flow.getChildren().add(new Text(dk.getSoLuongTang() + " x ("));
            }
            for (int i = 0; i < dk.getDanhSachMonTang().size(); i++) {
                DieuKien_MonTang mt = dk.getDanhSachMonTang().get(i);
                flow.getChildren().add(new Text(mt.getMonAnTang().getTenMon()));
                if (i < dk.getDanhSachMonTang().size() - 1) flow.getChildren().add(new Text(", "));
            }
            if (dk.getSoLuongTang() != null && dk.getSoLuongTang() > 1) {
                flow.getChildren().add(new Text(")"));
            }
        }

        box.getChildren().addAll(title, flow);
        return box;
    }

    private void appendGiamGiaText(TextFlow flow, KhuyenMai_DieuKien dk) {
        if (dk.getTyLeGiam() != null && dk.getTyLeGiam().compareTo(BigDecimal.ZERO) > 0) {
            Text t = new Text("Giảm " + dk.getTyLeGiam() + "%");
            t.setFill(javafx.scene.paint.Color.RED);
            t.setStyle("-fx-font-weight: bold;");
            flow.getChildren().add(t);
        } else if (dk.getSoTienGiam() != null && dk.getSoTienGiam().compareTo(BigDecimal.ZERO) > 0) {
            Text t = new Text("Giảm " + currencyFormatter.format(dk.getSoTienGiam()));
            t.setFill(javafx.scene.paint.Color.RED);
            t.setStyle("-fx-font-weight: bold;");
            flow.getChildren().add(t);
        }
    }

    private void appendMonAnList(TextFlow flow, List<DieuKien_Mon> list) {
        if (list == null || list.isEmpty()) {
            flow.getChildren().add(new Text("Tất cả các món"));
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            DieuKien_Mon dm = list.get(i);
            Text t = new Text(dm.getMonAn().getTenMon());
            if (dm.getSoLuong() > 1) {
                t.setText(t.getText() + " (x" + dm.getSoLuong() + ")");
            }
            flow.getChildren().add(t);
            if (i < list.size() - 1) flow.getChildren().add(new Text(", "));
        }
    }
}
