package com.thefourrestaurant.view.components.sidebar;

import com.thefourrestaurant.view.thongke.ThongKeBan;
import com.thefourrestaurant.view.thongke.ThongKeDoanhThu;
import com.thefourrestaurant.view.thongke.ThongKeMonAn;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SideBarThongKe extends BaseSideBar {

    private final Pane mainContent;
    private final GridPane luoiBieuDo;
    private final LinkedList<Node> hangDoiBieuDo = new LinkedList<>();
    private final List<int[]> viTriLuoi = Arrays.asList(new int[]{0, 0}, new int[]{1, 0}, new int[]{0, 1}, new int[]{1, 1});

    public SideBarThongKe(Pane mainContent) {
        super("Thống kê");
        this.mainContent = mainContent;

        luoiBieuDo = new GridPane();
        luoiBieuDo.setHgap(10);
        luoiBieuDo.setVgap(10);
        luoiBieuDo.setPadding(new Insets(10));
        luoiBieuDo.setStyle("-fx-background-color: white;");

        luoiBieuDo.prefWidthProperty().bind(mainContent.widthProperty());
        luoiBieuDo.prefHeightProperty().bind(mainContent.heightProperty());

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        luoiBieuDo.getColumnConstraints().addAll(col1, col2);

        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(50);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(50);
        luoiBieuDo.getRowConstraints().addAll(row1, row2);

        this.mainContent.getChildren().setAll(luoiBieuDo);

        khoiTaoDanhMuc();
        veLaiLuoi();
    }

    @Override
    protected void khoiTaoDanhMuc() {
        themDanhMuc("Doanh thu", List.of("Doanh thu theo ngày", "Doanh thu theo tháng", "Doanh thu theo năm"));
        themDanhMuc("Món ăn phổ biến", List.of("Món được đặt nhiều nhất trong ngày", "Món được đặt nhiều nhất trong tháng", "Món được đặt nhiều nhất trong năm", "Món ít đặt nhất trong tháng"));
        themDanhMuc("Bàn phổ biến", List.of("Bàn được đặt nhiều nhất trong ngày", "Bàn được đặt nhiều nhất trong tháng", "Bàn được đặt nhiều nhất trong năm", "Bàn ít đặt nhất trong tháng"));
    }

    private void themDanhMuc(String tenDanhMuc, List<String> danhSachCon) {
        Label nhanChinh = new Label(tenDanhMuc);
        nhanChinh.getStyleClass().add("muc-chinh");

        if (danhSachCon != null && !danhSachCon.isEmpty()) {
            VBox hopChua = new VBox(5);
            hopChua.setPadding(new Insets(5, 0, 5, 20));
            hopChua.getStyleClass().add("hop-chua-con");

            for (String mucCon : danhSachCon) {
                Label lblCon = taoNhanClick(mucCon, () -> xuLyChonMuc(mucCon), "muc-con");
                hopChua.getChildren().add(lblCon);
            }

            ScrollPane scrollMucCon = new ScrollPane(hopChua);
            scrollMucCon.setFitToWidth(true);
            scrollMucCon.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollMucCon.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollMucCon.setVisible(false);
            scrollMucCon.setManaged(false);
            scrollMucCon.getStyleClass().add("scroll-muc-con");

            nhanChinh.setOnMouseClicked(e -> {
                boolean hienThi = scrollMucCon.isVisible();
                scrollMucCon.setVisible(!hienThi);
                scrollMucCon.setManaged(!hienThi);
            });

            danhMucContainer.getChildren().addAll(nhanChinh, scrollMucCon);
        } else {
            nhanChinh.setOnMouseClicked(e -> xuLyChonMuc(tenDanhMuc));
            danhMucContainer.getChildren().add(nhanChinh);
        }
    }

    private void xuLyChonMuc(String tenMuc) {
        if (mainContent == null) return;

        for (Node node : lookupAll(".muc-con, .muc-chinh")) {
            node.setStyle("");
        }
        for (Node node : lookupAll(".muc-con, .muc-chinh")) {
            if (node instanceof Label lbl && lbl.getText().equals(tenMuc)) {
                lbl.setStyle("-fx-text-fill: #2b7cff; -fx-font-weight: bold; -fx-border-width: 0 0 0 4; -fx-border-color: #2b7cff; -fx-padding: 0 0 0 4;");
            }
        }

        Node bieuDo = null;
        switch (tenMuc) {
            case "Doanh thu theo ngày": bieuDo = ThongKeDoanhThu.taoBieuDo("ngày"); break;
            case "Doanh thu theo tháng": bieuDo = ThongKeDoanhThu.taoBieuDo("tháng"); break;
            case "Doanh thu theo năm": bieuDo = ThongKeDoanhThu.taoBieuDo("năm"); break;
            case "Món được đặt nhiều nhất trong ngày": bieuDo = ThongKeMonAn.taoBieuDo("ngày"); break;
            case "Món được đặt nhiều nhất trong tháng": bieuDo = ThongKeMonAn.taoBieuDo("tháng"); break;
            case "Món được đặt nhiều nhất trong năm": bieuDo = ThongKeMonAn.taoBieuDo("năm"); break;
            case "Bàn được đặt nhiều nhất trong ngày": bieuDo = ThongKeBan.taoBieuDo("ngày"); break;
            case "Bàn được đặt nhiều nhất trong tháng": bieuDo = ThongKeBan.taoBieuDo("tháng"); break;
            case "Bàn được đặt nhiều nhất trong năm": bieuDo = ThongKeBan.taoBieuDo("năm"); break;
        }

        if (bieuDo != null) {
            themBieuDo(bieuDo);
        }
    }

    private void themBieuDo(Node bieuDo) {
        hangDoiBieuDo.remove(bieuDo);

        if (hangDoiBieuDo.size() == 4) {
            hangDoiBieuDo.removeLast();
        }
        hangDoiBieuDo.addFirst(bieuDo);
        veLaiLuoi();
    }

    private void veLaiLuoi() {
        luoiBieuDo.getChildren().clear();
        if (hangDoiBieuDo.isEmpty()) {
            Label placeholder = new Label("Chọn một mục từ sidebar để xem thống kê");
            VBox container = new VBox(placeholder);
            container.setAlignment(Pos.CENTER);
            luoiBieuDo.add(container, 0, 0, 2, 2);
        } else {
            Iterator<Node> iterator = hangDoiBieuDo.iterator();
            int index = 0;
            while (iterator.hasNext()) {
                Node bieuDo = iterator.next();
                int[] pos = viTriLuoi.get(index);
                luoiBieuDo.add(bieuDo, pos[0], pos[1]);
                index++;
            }
        }
    }
}
