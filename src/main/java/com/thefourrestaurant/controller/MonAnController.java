package com.thefourrestaurant.controller;

import com.thefourrestaurant.DAO.KhuyenMaiDAO;
import com.thefourrestaurant.DAO.LoaiMonDAO;
import com.thefourrestaurant.DAO.MonAnDAO;
import com.thefourrestaurant.model.KhuyenMai;
import com.thefourrestaurant.model.LoaiMon;
import com.thefourrestaurant.model.MonAn;
import com.thefourrestaurant.view.monan.MonAnDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.List;
import java.util.Optional;

public class MonAnController {

    private final MonAnDAO monAnDAO;
    private final LoaiMonDAO loaiMonDAO;
    private final KhuyenMaiDAO khuyenMaiDAO;

    public MonAnController() {
        this.monAnDAO = new MonAnDAO();
        this.loaiMonDAO = new LoaiMonDAO();
        this.khuyenMaiDAO = new KhuyenMaiDAO();
    }

    public List<MonAn> layMonAnTheoLoai(String maLoaiMon) {
        return monAnDAO.layMonAnTheoLoai(maLoaiMon);
    }

    public List<LoaiMon> layTatCaLoaiMonAn() {
        return loaiMonDAO.layTatCaLoaiMon();
    }

    public List<KhuyenMai> layTatCaKhuyenMai() {
//        return khuyenMaiDAO.layTatCaKhuyenMai();
        return null;
    }

    public boolean themMoiMonAn(String maLoaiMonDefault) {
        List<LoaiMon> allLoaiMon = layTatCaLoaiMonAn();
        List<KhuyenMai> allKhuyenMai = layTatCaKhuyenMai();

        if (allLoaiMon.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Không có loại món ăn nào trong CSDL. Vui lòng thêm loại món ăn trước.").showAndWait();
            return false;
        }

        LoaiMon defaultLoaiMon = allLoaiMon.stream()
                .filter(lm -> lm.getMaLoaiMon().equals(maLoaiMonDefault))
                .findFirst()
                .orElse(null);

        MonAnDialog dialog = new MonAnDialog(null, allLoaiMon, defaultLoaiMon, allKhuyenMai);
        dialog.showAndWait();

        MonAn ketQua = dialog.layKetQua();
        if (ketQua != null) {
            ketQua.setMaMonAn(monAnDAO.taoMaMonAnMoi());
            return monAnDAO.themMonAn(ketQua);
        }
        return false;
    }

    public boolean tuyChinhMonAn(MonAn monAn) {
//        List<LoaiMon> allLoaiMon = layTatCaLoaiMonAn();
////        List<KhuyenMai> allKhuyenMai = layTatCaKhuyenMai();
//
//        MonAnDialog dialog = new MonAnDialog(monAn, allLoaiMon, monAn.getLoaiMon(), allKhuyenMai);
//        dialog.showAndWait();
//
//        MonAn ketQua = dialog.layKetQua();
//        if (ketQua != null) {
//            return monAnDAO.capNhatMonAn(ketQua);
//        }
        return false;
    }

    public boolean xoaMonAn(MonAn monAn) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xóa");
        confirmAlert.setHeaderText("Bạn có chắc chắn muốn xóa món: " + monAn.getTenMon() + "?");
        confirmAlert.setContentText("Hành động này không thể hoàn tác.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            return monAnDAO.xoaMonAn(monAn.getMaMonAn());
        }
        return false;
    }
}
