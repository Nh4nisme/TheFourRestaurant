package com.thefourrestaurant.view.thucdon;

import com.thefourrestaurant.DAO.ThucDonDAO;
import com.thefourrestaurant.view.components.CuaSoKhoiPhuc;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class KhoiPhucThucDon extends CuaSoKhoiPhuc<ThucDonDAO.ThucDonView> {

    private FlowPane flowPane;

    public KhoiPhucThucDon(List<ThucDonDAO.ThucDonView> danhSachDaXoa) {
        super(danhSachDaXoa, "Khôi phục thực đơn đã xóa", "thực đơn");
    }

    @Override
    protected Pane createViewPane() {
        flowPane = new FlowPane(15, 15);
        flowPane.setPadding(new Insets(15));
        flowPane.setAlignment(Pos.TOP_LEFT);
        return flowPane;
    }

    @Override
    protected List<String> layDanhSachLoai() {
        return danhSachDaXoa.stream()
                .flatMap(v -> Arrays.stream((v.loaiMon == null ? "" : v.loaiMon).split(",")))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    protected boolean locTheoLoai(ThucDonDAO.ThucDonView item, String loai) {
        if (item.loaiMon == null) return false;
        return Arrays.stream(item.loaiMon.split(",")).map(String::trim).anyMatch(s -> s.equals(loai));
    }

    @Override
    protected boolean timKiem(ThucDonDAO.ThucDonView item, String tuKhoa) {
        String key = tuKhoa.toLowerCase(Locale.ROOT);
        return (item.tenTD != null && item.tenTD.toLowerCase().contains(key)) ||
                (item.maTD != null && item.maTD.toLowerCase().contains(key));
    }

    @Override
    protected Comparator<ThucDonDAO.ThucDonView> getComparator() {
        return Comparator.comparing(v -> v.tenTD == null ? "" : v.tenTD);
    }

    @Override
    protected void capNhatView() {
        flowPane.getChildren().clear();
        for (ThucDonDAO.ThucDonView v : danhSachHienThi) {
            VBox box = new VBox(6);
            box.setPrefWidth(220);
            box.setPadding(new Insets(10));
            Label lblTen = new Label(v.tenTD != null ? v.tenTD : "(không tên)");
            lblTen.setStyle("-fx-font-weight: bold; -fx-text-fill: #673E1F;");
            Label lblLoai = new Label(v.loaiMon == null ? "" : v.loaiMon);
            box.getChildren().addAll(lblTen, lblLoai);
            flowPane.getChildren().add(createItemWrapper(box, v, "thực đơn"));
        }
        int count = danhSachHienThi.size();
        lblItemCount.setText("Hiển thị " + count + " thực đơn đã xóa");
    }

    public java.util.Set<ThucDonDAO.ThucDonView> getCacThucDonDaChon() {
        return getCacItemDaChon();
    }
}
