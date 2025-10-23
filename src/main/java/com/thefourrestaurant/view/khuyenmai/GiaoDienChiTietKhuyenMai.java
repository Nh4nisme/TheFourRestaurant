package com.thefourrestaurant.view.khuyenmai;

import com.thefourrestaurant.model.ChiTietKhuyenMai;
import com.thefourrestaurant.model.KhuyenMai;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class GiaoDienChiTietKhuyenMai extends VBox {

    private final Label lblMaKM = new Label();
    private final Label lblLoaiKM = new Label();
    private final Label lblMoTa = new Label();
    private final Label lblGiaTri = new Label();
    private final Label lblNgayBatDau = new Label();
    private final Label lblNgayKetThuc = new Label();

    public GiaoDienChiTietKhuyenMai() {
        this.setPadding(new Insets(20));
        this.setSpacing(15);
        this.getStyleClass().add("chi-tiet-pane");

        Label title = new Label("Chi Tiết Khuyến Mãi");
        title.getStyleClass().add("chi-tiet-title");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(new Label("Mã khuyến mãi:"), 0, 0);
        grid.add(lblMaKM, 1, 0);

        grid.add(new Label("Loại khuyến mãi:"), 0, 1);
        grid.add(lblLoaiKM, 1, 1);

        grid.add(new Label("Mô tả:"), 0, 2);
        grid.add(lblMoTa, 1, 2);
        lblMoTa.setWrapText(true);

        grid.add(new Label("Giá trị:"), 0, 3);
        grid.add(lblGiaTri, 1, 3);
        lblGiaTri.setWrapText(true);

        grid.add(new Label("Ngày bắt đầu:"), 0, 4);
        grid.add(lblNgayBatDau, 1, 4);

        grid.add(new Label("Ngày kết thúc:"), 0, 5);
        grid.add(lblNgayKetThuc, 1, 5);

        this.getChildren().addAll(title, grid);
    }

    public void hienThiChiTiet(KhuyenMai km) {
        if (km == null) {
            lblMaKM.setText("");
            lblLoaiKM.setText("");
            lblMoTa.setText("");
            lblGiaTri.setText("");
            lblNgayBatDau.setText("");
            lblNgayKetThuc.setText("");
            return;
        }

        lblMaKM.setText(km.getMaKM());
        lblLoaiKM.setText(km.getLoaiKhuyenMai() != null ? km.getLoaiKhuyenMai().getTenLoaiKM() : "");
        lblMoTa.setText(km.getMoTa());

        // Build the detailed value string from ChiTietKhuyenMais
        StringBuilder sb = new StringBuilder();
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        if (km.getChiTietKhuyenMais() != null && !km.getChiTietKhuyenMais().isEmpty()) {
            for (ChiTietKhuyenMai ct : km.getChiTietKhuyenMais()) {
                if (ct.getMonApDung() != null) {
                    sb.append("Áp dụng: ").append(ct.getMonApDung().getTenMon());
                }
                if (ct.getTyLeGiam() != null && ct.getTyLeGiam().compareTo(java.math.BigDecimal.ZERO) > 0) {
                    sb.append(" | Giảm ").append(ct.getTyLeGiam()).append("%");
                } else if (ct.getSoTienGiam() != null && ct.getSoTienGiam().compareTo(java.math.BigDecimal.ZERO) > 0) {
                    sb.append(" | Giảm ").append(currencyFormatter.format(ct.getSoTienGiam()));
                }
                if (ct.getMonTang() != null) {
                    sb.append(" | Tặng ").append(ct.getMonTang().getTenMon());
                    if (ct.getSoLuongTang() != null) sb.append(" x").append(ct.getSoLuongTang());
                }
                sb.append("\n");
            }
        } else {
            // fall back to earlier fields if any (for backward compatibility)
            if (km.getTyLe() != null && km.getTyLe().compareTo(java.math.BigDecimal.ZERO) > 0) {
                sb.append("Giảm ").append(km.getTyLe()).append("%");
            } else if (km.getSoTien() != null && km.getSoTien().compareTo(java.math.BigDecimal.ZERO) > 0) {
                sb.append("Giảm ").append(currencyFormatter.format(km.getSoTien()));
            }
        }

        lblGiaTri.setText(sb.toString());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        lblNgayBatDau.setText(km.getNgayBatDau() != null ? km.getNgayBatDau().format(formatter) : "Không giới hạn");
        lblNgayKetThuc.setText(km.getNgayKetThuc() != null ? km.getNgayKetThuc().format(formatter) : "Không giới hạn");
    }
}
