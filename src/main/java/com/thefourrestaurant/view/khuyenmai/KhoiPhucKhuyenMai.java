package com.thefourrestaurant.view.khuyenmai;

import com.thefourrestaurant.DAO.LoaiKhuyenMaiDAO;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.model.LoaiKhuyenMai;
import com.thefourrestaurant.view.components.CuaSoKhoiPhuc;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class KhoiPhucKhuyenMai extends CuaSoKhoiPhuc<KhuyenMai> {

    private FlowPane gridViewPane;

    public KhoiPhucKhuyenMai(List<KhuyenMai> danhSachKMDaXoa) {
        super(danhSachKMDaXoa, "Khôi phục khuyến mãi đã xóa", "khuyến mãi");
    }

    @Override
    protected Pane createViewPane() {
        gridViewPane = new FlowPane(15, 15);
        gridViewPane.setAlignment(Pos.TOP_LEFT);
        return gridViewPane;
    }

    @Override
    protected List<String> layDanhSachLoai() {
        LoaiKhuyenMaiDAO dao = new LoaiKhuyenMaiDAO();
        return dao.layTatCaLoaiKhuyenMai().stream()
                .map(LoaiKhuyenMai::getTenLoaiKM)
                .collect(Collectors.toList());
    }

    @Override
    protected boolean locTheoLoai(KhuyenMai item, String loai) {
        return item.getLoaiKhuyenMai() != null && item.getLoaiKhuyenMai().getTenLoaiKM().equals(loai);
    }

    @Override
    protected boolean timKiem(KhuyenMai item, String tuKhoa) {
        return item.getTenKM().toLowerCase().contains(tuKhoa) ||
                item.getMaKM().toLowerCase().contains(tuKhoa);
    }

    @Override
    protected Comparator<KhuyenMai> getComparator() {
        return Comparator.comparing(KhuyenMai::getTenKM);
    }

    @Override
    protected void capNhatView() {
        gridViewPane.getChildren().clear();

        for (KhuyenMai item : danhSachHienThi) {
            KhuyenMaiBox hopKM = new KhuyenMaiBox(item);
            VBox wrapper = createItemWrapper(hopKM, item, "khuyến mãi");
            gridViewPane.getChildren().add(wrapper);
        }

        int count = danhSachHienThi.size();
        lblItemCount.setText("Hiển thị " + count + " khuyến mãi đã xóa");
    }

    public Set<KhuyenMai> getCacKMDaChon() {
        return getCacItemDaChon();
    }
}
