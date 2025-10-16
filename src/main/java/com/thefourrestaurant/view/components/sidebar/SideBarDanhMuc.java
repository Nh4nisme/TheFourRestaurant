package com.thefourrestaurant.view.components.sidebar;

import com.thefourrestaurant.DAO.LoaiMonDAO;
import com.thefourrestaurant.DAO.TangDAO;
import com.thefourrestaurant.model.LoaiMon;
import com.thefourrestaurant.model.Tang;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class SideBarDanhMuc extends BaseSideBar {
	
	private HBox mainContainer;

    private final TangDAO tangDAO = new TangDAO();
    private final LoaiMonDAO loaiMonDAO = new LoaiMonDAO();

    private Consumer<String> tangClickListener;
    private Consumer<String> loaiMonClickListener;
    
    public SideBarDanhMuc(HBox mainContainer) {
        this(); // gọi constructor mặc định để tạo danh mục
        this.mainContainer = mainContainer;
    }

    // 🔹 Constructor mặc định
    public SideBarDanhMuc() {
        super("Quản Lý");
        // Tạo danh mục tầng và loại món
        khoiTaoDanhMuc("Loại món", loaiMonDAO.getAllLoaiMon(), LoaiMon::getTenLoai, LoaiMon::getMaLoai, () -> loaiMonClickListener);
        khoiTaoDanhMuc("Tầng và bàn", tangDAO.getAllTang(), Tang::getTenTang, Tang::getMaTang, () -> tangClickListener);
    }

    @Override
    protected void khoiTaoDanhMuc() {
        // Không cần
    }

    private <T> void khoiTaoDanhMuc(String tenDanhMuc, List<T> ds,
                                     Function<T, String> layTen,
                                     Function<T, String> layMa,
                                     java.util.function.Supplier<Consumer<String>> listenerSupplier) {
        Label nhanChinh = taoNhanClick(tenDanhMuc, null, "muc-chinh");
        VBox hopChua = new VBox(5);
        hopChua.setPadding(new Insets(5, 0, 5, 20));
        hopChua.setVisible(false);
        hopChua.setManaged(false);

        for (T item : ds) {
            Label nhanCon = taoNhanClick(layTen.apply(item), () -> {
                Consumer<String> listener = listenerSupplier.get();
                if (listener != null) {
                    listener.accept(layMa.apply(item));
                }
            }, "muc-con");
            hopChua.getChildren().add(nhanCon);
        }

        nhanChinh.setOnMouseClicked(e -> moHoacDongMucCon(hopChua));
        getChildren().addAll(nhanChinh, hopChua);
    }

    // 🔹 Public setters
    public void setTangClickListener(Consumer<String> listener) {
        this.tangClickListener = listener;
    }

    public void setLoaiMonClickListener(Consumer<String> listener) {
        this.loaiMonClickListener = listener;
    }
}
