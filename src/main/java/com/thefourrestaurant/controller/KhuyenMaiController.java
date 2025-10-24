package com.thefourrestaurant.controller;

import com.thefourrestaurant.DAO.ChiTietKhuyenMaiDAO;
import com.thefourrestaurant.DAO.KhuyenMaiDAO;
import com.thefourrestaurant.DAO.LoaiKhuyenMaiDAO;
import com.thefourrestaurant.DAO.MonAnDAO;
import com.thefourrestaurant.model.ChiTietKhuyenMai;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.model.LoaiKhuyenMai;
import com.thefourrestaurant.model.MonAn;
import com.thefourrestaurant.view.khuyenmai.ChiTietKhuyenMaiManagerDialog;
import com.thefourrestaurant.view.khuyenmai.KhuyenMaiDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

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

    public boolean themKhuyenMaiMoi() {
        List<LoaiKhuyenMai> tatCaLoaiKM = loaiKhuyenMaiDAO.layTatCaLoaiKhuyenMai();
        String maKMMoi = khuyenMaiDAO.taoMaKhuyenMaiMoi();

        KhuyenMaiDialog dialog = new KhuyenMaiDialog(null, tatCaLoaiKM, maKMMoi, this);
        dialog.showAndWait();

        KhuyenMai ketQua = dialog.layKetQua();
        if (ketQua != null) {
            if (khuyenMaiDAO.themKhuyenMai(ketQua)) {
                new Alert(Alert.AlertType.INFORMATION, "Thêm khuyến mãi thành công!").showAndWait();
                return true;
            } else {
                new Alert(Alert.AlertType.ERROR, "Thêm khuyến mãi thất bại.").showAndWait();
            }
        }
        return false;
    }

    public boolean capNhatKhuyenMai(KhuyenMai km) {
        List<LoaiKhuyenMai> tatCaLoaiKM = loaiKhuyenMaiDAO.layTatCaLoaiKhuyenMai();

        KhuyenMaiDialog dialog = new KhuyenMaiDialog(km, tatCaLoaiKM, null, this);
        dialog.showAndWait();

        KhuyenMai ketQua = dialog.layKetQua();
        if (ketQua != null) {
            if (khuyenMaiDAO.capNhatKhuyenMai(ketQua)) {
                new Alert(Alert.AlertType.INFORMATION, "Cập nhật khuyến mãi thành công!").showAndWait();
                return true;
            } else {
                new Alert(Alert.AlertType.ERROR, "Cập nhật khuyến mãi thất bại.").showAndWait();
            }
        }
        return false;
    }

    public boolean xoaKhuyenMai(KhuyenMai km) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xóa khuyến mãi này không?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            if (chiTietKhuyenMaiDAO.xoaTheoMaKM(km.getMaKM())) {
                if (khuyenMaiDAO.xoaKhuyenMai(km.getMaKM())) {
                    new Alert(Alert.AlertType.INFORMATION, "Xóa khuyến mãi thành công!").showAndWait();
                    return true;
                } else {
                    new Alert(Alert.AlertType.ERROR, "Xóa khuyến mãi thất bại.").showAndWait();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Không thể xóa chi tiết khuyến mãi liên quan.").showAndWait();
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

    public boolean themChiTietKhuyenMaiMoi(ChiTietKhuyenMai ct) {
        if (chiTietKhuyenMaiDAO.themChiTiet(ct)) {
            new Alert(Alert.AlertType.INFORMATION, "Thêm chi tiết khuyến mãi thành công!").showAndWait();
            return true;
        } else {
            new Alert(Alert.AlertType.ERROR, "Thêm chi tiết khuyến mãi thất bại.").showAndWait();
            return false;
        }
    }

    public boolean capNhatChiTietKhuyenMai(ChiTietKhuyenMai ct) {
        if (chiTietKhuyenMaiDAO.capNhatChiTiet(ct)) {
            new Alert(Alert.AlertType.INFORMATION, "Cập nhật chi tiết khuyến mãi thành công!").showAndWait();
            return true;
        } else {
            new Alert(Alert.AlertType.ERROR, "Cập nhật chi tiết khuyến mãi thất bại.").showAndWait();
            return false;
        }
    }

    public boolean xoaChiTietKhuyenMai(ChiTietKhuyenMai ct) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xóa chi tiết khuyến mãi này không?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            if (chiTietKhuyenMaiDAO.xoaChiTiet(ct.getMaCTKM())) {
                new Alert(Alert.AlertType.INFORMATION, "Xóa chi tiết khuyến mãi thành công!").showAndWait();
                return true;
            } else {
                new Alert(Alert.AlertType.ERROR, "Xóa chi tiết khuyến mãi thất bại.").showAndWait();
                return false;
            }
        }
        return false;
    }
}
