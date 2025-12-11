package com.thefourrestaurant.view.components;

import java.util.LinkedHashMap;
import java.util.Objects;

import com.thefourrestaurant.controller.HelpController;
import com.thefourrestaurant.model.TaiKhoan;
import com.thefourrestaurant.util.Session;
import com.thefourrestaurant.view.*;
import com.thefourrestaurant.view.ban.*;
import com.thefourrestaurant.view.hoadon.GiaoDienHoaDon;
import com.thefourrestaurant.view.khachhang.GiaoDienKhachHang;
import com.thefourrestaurant.view.khuyenmai.GiaoDienKhuyenMai;
import com.thefourrestaurant.view.loaimonan.LoaiMonAn;
import com.thefourrestaurant.view.monan.GiaoDienMonAn;
import com.thefourrestaurant.view.taikhoan.GiaoDienTaiKhoan;
import com.thefourrestaurant.view.thongke.ThongKeGiaoDienChinh;
import com.thefourrestaurant.view.nhanvien.GiaoDienNhanVien;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class NavBar extends HBox {

    public enum MapDieuHuong {
        // Danh mục
        DM_THUC_DON,
        DM_MON_AN,
        DM_LOAI_MON,
        DM_KHUYEN_MAI,
        DM_HOA_DON,
        DM_KHACH_HANG,
        DM_TAI_KHOAN,
        DM_NHAN_VIEN, // <--- Add this line
        DM_TANG_BAN,

        // Xử lý
        XL_DAT_BAN,

        // Tra cứu
        TC_MON_AN,
        TC_KHACH_HANG,
        TC_KHUYEN_MAI,
        TC_PHIEU_DAT_BAN,

        // Hệ thống
        HT_TRANG_CHU,
        HT_TRO_GIUP,
        HT_THOAT,
        HT_DANG_XUAT,

        THONG_KE
    }

    private final Pane mainContent;
    private final Pane sideBar;
    private final Pane sideBarExtended;

    private final HelpController helpController = new HelpController();

    private DropDownButtonMap<MapDieuHuong> btnDanhMuc;
    private DropDownButtonMap<MapDieuHuong> btnXuLy;
    private DropDownButtonMap<MapDieuHuong> btnTimKiem;
    private DropDownButtonMap<MapDieuHuong> btnHeThong;
    private ButtonSample btnThongKe;

    private static final String ICON_DANH_MUC = "/com/thefourrestaurant/images/icon/danhMucNavIcon.png";
    private static final String ICON_XU_LY = "/com/thefourrestaurant/images/icon/xuLyIcon.png";
    private static final String ICON_TIM_KIEM = "/com/thefourrestaurant/images/icon/timKiemIcon.png";
    private static final String ICON_HE_THONG = "/com/thefourrestaurant/images/icon/heThongIcon.png";
    private static final String ICON_THONG_KE = "/com/thefourrestaurant/images/icon/thongKeIcon.png";
    private static final String ICON_TAI_KHOAN = "/com/thefourrestaurant/images/icon/accountIcon.png";

    public NavBar(Pane mainContent, Pane sideBar, Pane sideBarExtended) {
        this.mainContent = mainContent;
        this.sideBar = sideBar;
        this.sideBarExtended = sideBarExtended;

        khoiTaoLayout();
        khoiTaoButtons();
        khoiTaoSuKien();
    }

    private void khoiTaoLayout() {
        Font.loadFont(
                Objects.requireNonNull(getClass().getResourceAsStream(
                        "/com/thefourrestaurant/fonts/Montserrat-SemiBold.ttf")),
                16
        );

        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(0, 30, 0, 30));
        setPrefHeight(80);
        setSpacing(10);
        setStyle("-fx-background-color: #E5D595");
    }

    private void khoiTaoButtons() {

        ButtonSample btnTaiKhoan = taoButtonTaiKhoan();

        btnDanhMuc = taoDropDown("Danh mục", taoMenuDanhMuc(), ICON_DANH_MUC);
        btnXuLy    = taoDropDown("Xử lý", taoMenuXuLy(), ICON_XU_LY);
        btnTimKiem = taoDropDown("Tra cứu thông tin", taoMenuTraCuu(), ICON_TIM_KIEM);
        btnHeThong = taoDropDown("Hệ thống", taoMenuHeThong(), ICON_HE_THONG);

        btnThongKe = new ButtonSample("Thống kê", ICON_THONG_KE, 45, 16, 1);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        getChildren().addAll(
                btnDanhMuc,
                btnHeThong,
                btnXuLy,
                btnTimKiem,
                btnThongKe,
                spacer,
                btnTaiKhoan
        );
    }

    private void khoiTaoSuKien() {
        btnDanhMuc.setOnItemSelected(this::xuLyDieuHuong);
        btnXuLy.setOnItemSelected(this::xuLyDieuHuong);
        btnTimKiem.setOnItemSelected(this::xuLyDieuHuong);
        btnHeThong.setOnItemSelected(this::xuLyDieuHuong);

        btnThongKe.setOnMouseClicked(e ->
                xuLyDieuHuong(MapDieuHuong.THONG_KE)
        );
    }


    private LinkedHashMap<String, MapDieuHuong> taoMenuDanhMuc() {
        LinkedHashMap<String, MapDieuHuong> map = new LinkedHashMap<>();
        map.put("Thực đơn", MapDieuHuong.DM_THUC_DON);
        map.put("Món ăn", MapDieuHuong.DM_MON_AN);
        map.put("Loại món ăn", MapDieuHuong.DM_LOAI_MON);
        map.put("Khuyến mãi", MapDieuHuong.DM_KHUYEN_MAI);
        map.put("Hóa đơn", MapDieuHuong.DM_HOA_DON);
        map.put("Khách hàng", MapDieuHuong.DM_KHACH_HANG);
        map.put("Tài khoản", MapDieuHuong.DM_TAI_KHOAN);
        map.put("Nhân viên", MapDieuHuong.DM_NHAN_VIEN); // <--- Add this line
        map.put("Tầng và bàn", MapDieuHuong.DM_TANG_BAN);
        return map;
    }

    private LinkedHashMap<String, MapDieuHuong> taoMenuXuLy() {
        LinkedHashMap<String, MapDieuHuong> map = new LinkedHashMap<>();
        map.put("Đặt bàn", MapDieuHuong.XL_DAT_BAN);
        return map;
    }

    private LinkedHashMap<String, MapDieuHuong> taoMenuTraCuu() {
        LinkedHashMap<String, MapDieuHuong> map = new LinkedHashMap<>();
        map.put("Phiếu đặt bàn", MapDieuHuong.TC_PHIEU_DAT_BAN);
        map.put("Món ăn", MapDieuHuong.TC_MON_AN);
        map.put("Khuyến mãi", MapDieuHuong.TC_KHUYEN_MAI);
        map.put("Khách hàng", MapDieuHuong.TC_KHACH_HANG);
        return map;
    }

    private LinkedHashMap<String, MapDieuHuong> taoMenuHeThong() {
        LinkedHashMap<String, MapDieuHuong> map = new LinkedHashMap<>();
        map.put("Trang chủ", MapDieuHuong.HT_TRANG_CHU);
        map.put("Trợ giúp", MapDieuHuong.HT_TRO_GIUP);
        map.put("Thoát", MapDieuHuong.HT_THOAT);
        map.put("Đăng xuất", MapDieuHuong.HT_DANG_XUAT);
        return map;
    }


    private DropDownButtonMap<MapDieuHuong> taoDropDown(
            String title,
            LinkedHashMap<String, MapDieuHuong> items,
            String icon
    ) {
        return new DropDownButtonMap<>(title, items, icon, 45, 16, 1);
    }

    private ButtonSample taoButtonTaiKhoan() {
        TaiKhoan current = Session.getCurrentUser();
        String label = (current == null)
                ? "Tài khoản: --"
                : chuyenVaiTro(current);

        return new ButtonSample(label, ICON_TAI_KHOAN, 45, 16, 1);
    }

    private String chuyenVaiTro(TaiKhoan tk) {
        String role = tk.getVaiTro().getTenVaiTro();
        return switch (role == null ? "" : role.toLowerCase()) {
            case "quanly" -> "Quản lý: " + tk.getTenDN();
            case "thungan" -> "Thu ngân: " + tk.getTenDN();
            default -> role + ": " + tk.getTenDN();
        };
    }


    private void xuLyDieuHuong(MapDieuHuong huong) {

        if (huong == null) return;

        boolean isDatBan = huong == MapDieuHuong.XL_DAT_BAN;
        sideBar.setVisible(!isDatBan);
        sideBar.setManaged(!isDatBan);
        sideBarExtended.setVisible(!isDatBan);
        sideBarExtended.setManaged(!isDatBan);

        Node newContent = switch (huong) {
            case DM_THUC_DON -> new GiaoDienThucDon();
            case DM_MON_AN -> new GiaoDienMonAn(null, "Tất cả Món ăn");
            case DM_LOAI_MON -> new LoaiMonAn();
            case DM_KHUYEN_MAI -> new GiaoDienKhuyenMai();
            case DM_HOA_DON -> new GiaoDienHoaDon();
            case DM_KHACH_HANG -> new GiaoDienKhachHang();
            case DM_TAI_KHOAN -> new GiaoDienTaiKhoan();
            case DM_NHAN_VIEN -> new GiaoDienNhanVien(); // <--- Add this line
            case DM_TANG_BAN -> new QuanLiBan((StackPane) mainContent, "QUAN_LY_BAN");

            case XL_DAT_BAN -> new GiaoDienDatBan((StackPane) mainContent);
            case TC_PHIEU_DAT_BAN -> new GiaoDienPhieuDatBan();
            case TC_MON_AN -> new GiaoDienMonAn(null, "Tra cứu Món ăn");
            case TC_KHUYEN_MAI -> new GiaoDienKhuyenMai();
            case TC_KHACH_HANG -> null;

            case HT_TRANG_CHU -> {
                Stage s = (Stage) mainContent.getScene().getWindow();
                new GiaoDienChinh().show(s);
                s.setFullScreen(true);
                yield null;
            }
            case HT_TRO_GIUP -> {
                helpController.openHelpFile((Stage) getScene().getWindow());
                yield null;
            }
            case HT_THOAT -> {
                ((Stage) getScene().getWindow()).close();
                yield null;
            }
            case HT_DANG_XUAT -> null;
            case THONG_KE -> new ThongKeGiaoDienChinh();
        };

        if (newContent != null) {
            Region r = (Region) newContent;
            r.prefWidthProperty().bind(mainContent.widthProperty());
            r.prefHeightProperty().bind(mainContent.heightProperty());
            mainContent.getChildren().setAll(newContent);
        }
    }
}