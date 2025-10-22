package com.thefourrestaurant.controller;

import com.thefourrestaurant.DAO.KhuyenMaiDAO;
import com.thefourrestaurant.DAO.LoaiKhuyenMaiDAO;
import com.thefourrestaurant.DAO.MonAnDAO;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.model.LoaiKhuyenMai;
import com.thefourrestaurant.model.MonAn;
import com.thefourrestaurant.view.khuyenmai.KhuyenMaiDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.List;
import java.util.Optional;

public class KhuyenMaiController {

    private final KhuyenMaiDAO khuyenMaiDAO;
    private final LoaiKhuyenMaiDAO loaiKhuyenMaiDAO;
    private final MonAnDAO monAnDAO;

    public KhuyenMaiController() {
        this.khuyenMaiDAO = new KhuyenMaiDAO();
        this.loaiKhuyenMaiDAO = new LoaiKhuyenMaiDAO();
        this.monAnDAO = new MonAnDAO();
    }

    public List<KhuyenMai> layTatCaKhuyenMai() {
        return khuyenMaiDAO.layTatCaKhuyenMai();
    }

    public List<LoaiKhuyenMai> layTatCaLoaiKhuyenMai() {
        return loaiKhuyenMaiDAO.layTatCaLoaiKhuyenMai();
    }

    public List<MonAn> layTatCaMonAn() {
        return monAnDAO.layTatCaMonAn();
    }

    public boolean themMoiKhuyenMai() {
        List<LoaiKhuyenMai> allLoaiKM = layTatCaLoaiKhuyenMai();
        List<MonAn> allMonAn = layTatCaMonAn();

        KhuyenMaiDialog dialog = new KhuyenMaiDialog(null, allLoaiKM, allMonAn);
        dialog.showAndWait();

        KhuyenMai ketQua = dialog.layKetQua();
        if (ketQua != null) {
            ketQua.setMaKM(khuyenMaiDAO.taoMaKhuyenMaiMoi());
            return khuyenMaiDAO.themKhuyenMai(ketQua);
        }
        return false;
    }

    public boolean tuyChinhKhuyenMai(KhuyenMai km) {
        List<LoaiKhuyenMai> allLoaiKM = layTatCaLoaiKhuyenMai();
        List<MonAn> allMonAn = layTatCaMonAn();

        KhuyenMaiDialog dialog = new KhuyenMaiDialog(km, allLoaiKM, allMonAn);
        dialog.showAndWait();

        KhuyenMai ketQua = dialog.layKetQua();
        if (ketQua != null) {
            return khuyenMaiDAO.capNhatKhuyenMai(ketQua);
        }
        return false;
    }

    public boolean xoaKhuyenMai(KhuyenMai km) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xóa");
        confirmAlert.setHeaderText("Bạn có chắc chắn muốn xóa khuyến mãi: " + km.getMaKM() + "?");
        confirmAlert.setContentText(km.getMoTa());

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            return khuyenMaiDAO.xoaKhuyenMai(km.getMaKM());
        }
        return false;
    }
}
