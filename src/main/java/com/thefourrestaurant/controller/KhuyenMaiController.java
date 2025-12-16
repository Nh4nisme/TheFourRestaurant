package com.thefourrestaurant.controller;

import com.thefourrestaurant.DAO.*;
import com.thefourrestaurant.model.ChiTietKhuyenMai;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.model.LoaiKhuyenMai;
import com.thefourrestaurant.model.MonAn;
import com.thefourrestaurant.view.khuyenmai.KhuyenMaiDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class KhuyenMaiController {

    private final KhuyenMaiDAO khuyenMaiDAO;
    private final LoaiKhuyenMaiDAO loaiKhuyenMaiDAO;
    private final ChiTietKhuyenMaiDAO chiTietKhuyenMaiDAO;
    private final MonAnDAO monAnDAO;
    private final KhungGio_KM_DAO khungGio_KM_DAO;

    public KhuyenMaiController() {
        this.khuyenMaiDAO = new KhuyenMaiDAO();
        this.loaiKhuyenMaiDAO = new LoaiKhuyenMaiDAO();
        this.chiTietKhuyenMaiDAO = new ChiTietKhuyenMaiDAO();
        this.monAnDAO = new MonAnDAO();
        this.khungGio_KM_DAO = new KhungGio_KM_DAO();
    }

    public List<KhuyenMai> layDanhSachKhuyenMai() {
        return khuyenMaiDAO.layDanhSachKhuyenMai();
    }

    public String taoMaKhuyenMaiMoi() {
        return khuyenMaiDAO.taoMaKhuyenMaiMoi();
    }

    public List<ChiTietKhuyenMai> layChiTietKhuyenMaiTheoMaKM(String maKM) {
        return chiTietKhuyenMaiDAO.layTheoMaKM(maKM);
    }

    public boolean themKhuyenMaiMoi(Stage owner) {
        List<LoaiKhuyenMai> tatCaLoaiKM = loaiKhuyenMaiDAO.layTatCaLoaiKhuyenMai();
        String maKMMoi = khuyenMaiDAO.taoMaKhuyenMaiMoi();

        KhuyenMaiDialog dialog = new KhuyenMaiDialog(null, tatCaLoaiKM, maKMMoi, this);
        dialog.initOwner(owner);
        dialog.showAndWait();

        KhuyenMai ketQua = dialog.layKetQua();
        if (ketQua != null) {
            if (khuyenMaiDAO.themKhuyenMai(ketQua)) {
                showAlert(owner, Alert.AlertType.INFORMATION, "Thêm khuyến mãi thành công!");
                return true;
            } else {
                showAlert(owner, Alert.AlertType.ERROR, "Thêm khuyến mãi thất bại.");
            }
        }
        return false;
    }

    public boolean capNhatKhuyenMai(Stage owner, KhuyenMai km) {
        List<LoaiKhuyenMai> tatCaLoaiKM = loaiKhuyenMaiDAO.layTatCaLoaiKhuyenMai();

        KhuyenMaiDialog dialog = new KhuyenMaiDialog(km, tatCaLoaiKM, null, this);
        dialog.initOwner(owner);
        dialog.showAndWait();

        KhuyenMai ketQua = dialog.layKetQua();
        if (ketQua != null) {
            if (khuyenMaiDAO.capNhatKhuyenMai(ketQua)) {
                showAlert(owner, Alert.AlertType.INFORMATION, "Cập nhật khuyến mãi thành công!");
                return true;
            } else {
                showAlert(owner, Alert.AlertType.ERROR, "Cập nhật khuyến mãi thất bại.");
            }
        }
        return false;
    }

    public boolean xoaKhuyenMai(Stage owner, KhuyenMai km) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xóa khuyến mãi '" + km.getTenKM() + "' không?", ButtonType.YES, ButtonType.NO);
        confirm.initOwner(owner);
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            if (khuyenMaiDAO.xoaKhuyenMai(km.getMaKM())) {
                showAlert(owner, Alert.AlertType.INFORMATION, "Xóa khuyến mãi thành công!");
                return true;
            } else {
                showAlert(owner, Alert.AlertType.ERROR, "Xóa khuyến mãi thất bại.");
                return false;
            }
        }
        return false;
    }

    public List<MonAn> layDanhSachMonAn() {
        return monAnDAO.layTatCaMonAn();
    }

    public String taoMaChiTietKhuyenMaiMoi() {
        return chiTietKhuyenMaiDAO.taoMaChiTietKhuyenMaiMoi();
    }

    public boolean themChiTietKhuyenMaiMoi(Stage owner, ChiTietKhuyenMai ct) {
        if (chiTietKhuyenMaiDAO.themChiTiet(ct)) {
            String tenMon = (ct.getMonApDung() != null) ? ct.getMonApDung().getTenMon() : "";
            showAlert(owner, Alert.AlertType.INFORMATION, "Thêm món " + tenMon + " vào khuyến mãi thành công!");
            return true;
        } else {
            showAlert(owner, Alert.AlertType.ERROR, "Thêm chi tiết khuyến mãi thất bại.");
            return false;
        }
    }

    public boolean capNhatChiTietKhuyenMai(Stage owner, ChiTietKhuyenMai ct) {
        if (chiTietKhuyenMaiDAO.capNhatChiTiet(ct)) {
            showAlert(owner, Alert.AlertType.INFORMATION, "Cập nhật chi tiết khuyến mãi thành công!");
            return true;
        } else {
            showAlert(owner, Alert.AlertType.ERROR, "Cập nhật chi tiết khuyến mãi thất bại.");
            return false;
        }
    }

    public boolean xoaChiTietKhuyenMai(Stage owner, ChiTietKhuyenMai ct) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xóa chi tiết khuyến mãi này không?", ButtonType.YES, ButtonType.NO);
        confirm.initOwner(owner);
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            if (chiTietKhuyenMaiDAO.xoaChiTiet(ct.getMaCTKM())) {
                showAlert(owner, Alert.AlertType.INFORMATION, "Xóa chi tiết khuyến mãi thành công!");
                return true;
            } else {
                showAlert(owner, Alert.AlertType.ERROR, "Xóa chi tiết khuyến mãi thất bại.");
                return false;
            }
        }
        return false;
    }

    public List<KhuyenMai> layKhuyenMaiDaXoa() {
        return khuyenMaiDAO.layKhuyenMaiDaXoa();
    }

    public boolean khoiPhucKhuyenMai(Stage owner, Set<KhuyenMai> cacKMCamKhoiPhuc) {
        if (cacKMCamKhoiPhuc == null || cacKMCamKhoiPhuc.isEmpty()) {
            showAlert(owner, Alert.AlertType.WARNING, "Không có khuyến mãi nào được chọn để khôi phục.");
            return false;
        }

        int thanhCong = 0;
        int thatBai = 0;

        for (KhuyenMai km : cacKMCamKhoiPhuc) {
            if (khuyenMaiDAO.khoiPhucKhuyenMai(km.getMaKM())) {
                thanhCong++;
            } else {
                thatBai++;
            }
        }

        if (thanhCong > 0 && thatBai == 0) {
            showAlert(owner, Alert.AlertType.INFORMATION, "Khôi phục thành công " + thanhCong + " khuyến mãi!");
            return true;
        } else if (thanhCong > 0 && thatBai > 0) {
            showAlert(owner, Alert.AlertType.WARNING, "Khôi phục thành công " + thanhCong + " khuyến mãi, thất bại " + thatBai + " khuyến mãi.");
            return true;
        } else {
            showAlert(owner, Alert.AlertType.ERROR, "Khôi phục thất bại tất cả các khuyến mãi.");
            return false;
        }
    }

    private void showAlert(Stage owner, Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message);
        alert.initOwner(owner);
        alert.showAndWait();
    }
}
