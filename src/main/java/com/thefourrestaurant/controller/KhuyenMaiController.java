package com.thefourrestaurant.controller;

import com.thefourrestaurant.DAO.ChiTietKhuyenMaiDAO;
import com.thefourrestaurant.DAO.KhuyenMaiDAO;
import com.thefourrestaurant.DAO.LoaiKhuyenMaiDAO;
import com.thefourrestaurant.DAO.MonAnDAO;
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

public class KhuyenMaiController {

    private final KhuyenMaiDAO khuyenMaiDAO;
    private final LoaiKhuyenMaiDAO loaiKhuyenMaiDAO;
    private final ChiTietKhuyenMaiDAO chiTietKhuyenMaiDAO;
    private final MonAnDAO monAnDAO;

    public KhuyenMaiController() {
        this.khuyenMaiDAO = new KhuyenMaiDAO();
        this.loaiKhuyenMaiDAO = new LoaiKhuyenMaiDAO();
        this.chiTietKhuyenMaiDAO = new ChiTietKhuyenMaiDAO();
        this.monAnDAO = new MonAnDAO();
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
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xóa khuyến mãi này không?", ButtonType.YES, ButtonType.NO);
        confirm.initOwner(owner);
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            if (chiTietKhuyenMaiDAO.xoaTheoMaKM(km.getMaKM())) {
                if (khuyenMaiDAO.xoaKhuyenMai(km.getMaKM())) {
                    showAlert(owner, Alert.AlertType.INFORMATION, "Xóa khuyến mãi thành công!");
                    return true;
                } else {
                    showAlert(owner, Alert.AlertType.ERROR, "Xóa khuyến mãi thất bại.");
                }
            } else {
                showAlert(owner, Alert.AlertType.ERROR, "Không thể xóa chi tiết khuyến mãi liên quan.");
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
            showAlert(owner, Alert.AlertType.INFORMATION, "Thêm chi tiết khuyến mãi thành công!");
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

    private void showAlert(Stage owner, Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message);
        alert.initOwner(owner);
        alert.showAndWait();
    }
}
