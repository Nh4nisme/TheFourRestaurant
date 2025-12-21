package com.thefourrestaurant.view.monan;

import com.thefourrestaurant.DAO.LoaiMonDAO;
import com.thefourrestaurant.model.LoaiMon;
import com.thefourrestaurant.model.MonAn;
import com.thefourrestaurant.view.components.CuaSoKhoiPhuc;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

public class KhoiPhucMonAn extends CuaSoKhoiPhuc<MonAn> {

    private GridPane gridViewPane;
    private final int soCotMoiHang = 5;

    public KhoiPhucMonAn(List<MonAn> danhSachMonAnDaXoa) {
        super(danhSachMonAnDaXoa, "Khôi phục món ăn đã xóa", "món");
    }

    @Override
    protected Pane createViewPane() {
        gridViewPane = new GridPane();
        gridViewPane.setAlignment(Pos.TOP_LEFT);
        gridViewPane.setHgap(20);
        gridViewPane.setVgap(20);
        return gridViewPane;
    }

    @Override
    protected List<String> layDanhSachLoai() {
        LoaiMonDAO loaiMonDAO = new LoaiMonDAO();
        return loaiMonDAO.layTatCaLoaiMon().stream()
                .map(LoaiMon::getTenLoaiMon)
                .collect(Collectors.toList());
    }

    @Override
    protected boolean locTheoLoai(MonAn item, String loai) {
        return item.getLoaiMon() != null && item.getLoaiMon().getTenLoaiMon().equals(loai);
    }

    @Override
    protected boolean timKiem(MonAn item, String tuKhoa) {
        return item.getTenMon().toLowerCase().contains(tuKhoa) ||
                item.getMaMonAn().toLowerCase().contains(tuKhoa);
    }

    @Override
    protected Comparator<MonAn> getComparator() {
        return Comparator.comparing(MonAn::getTenMon);
    }

    @Override
    protected void capNhatView() {
        gridViewPane.getChildren().clear();
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        for (int i = 0; i < danhSachHienThi.size(); i++) {
            MonAn item = danhSachHienThi.get(i);
            MonAnBox hopMonAn = new MonAnBox(item);

            int col = i % soCotMoiHang;
            int row = i / soCotMoiHang;
            gridViewPane.add(createItemWrapper(hopMonAn, item, "món"), col, row);
        }

        int count = danhSachHienThi.size();
        lblItemCount.setText("Hiển thị " + count + " món ăn đã xóa");
    }

    public Set<MonAn> getCacMonDaChon() {
        return getCacItemDaChon();
    }
}
