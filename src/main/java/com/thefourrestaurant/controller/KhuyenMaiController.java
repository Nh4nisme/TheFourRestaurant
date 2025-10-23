package com.thefourrestaurant.controller;

import com.thefourrestaurant.DAO.KhuyenMaiDAO;
import com.thefourrestaurant.DAO.LoaiKhuyenMaiDAO;
import com.thefourrestaurant.DAO.ChiTietKhuyenMaiDAO;
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
    private final ChiTietKhuyenMaiDAO chiTietKhuyenMaiDAO;
    private final MonAnDAO monAnDAO;

    public KhuyenMaiController() {
        this.khuyenMaiDAO = new KhuyenMaiDAO();
        this.loaiKhuyenMaiDAO = new LoaiKhuyenMaiDAO();
        this.chiTietKhuyenMaiDAO = new ChiTietKhuyenMaiDAO();
        this.monAnDAO = new MonAnDAO();
    }

    public List<KhuyenMai> layTatCaKhuyenMai() {
//        // Lấy danh sách khuyến mãi và nạp chi tiết vào từng cái
//        List<KhuyenMai> ds = khuyenMaiDAO.layTatCaKhuyenMai();
//        for (KhuyenMai km : ds) {
//            km.setChiTietKhuyenMais(chiTietKhuyenMaiDAO.layTheoMaKM(km.getMaKM()));
//        }
//        return ds;
        return null;
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

//        // Mở dialog nhập khuyến mãi
//        KhuyenMaiDialog dialog = new KhuyenMaiDialog(null, allLoaiKM, allMonAn);
//        dialog.showAndWait();
//
//        KhuyenMai ketQua = dialog.layKetQua();
//        if (ketQua != null) {
//            // Tạo mã KM mới
//            String newId = khuyenMaiDAO.taoMaKhuyenMaiMoi();
//            ketQua.setMaKM(newId);
//
//            boolean inserted = khuyenMaiDAO.themKhuyenMai(ketQua);
//
//            // Lưu chi tiết khuyến mãi nếu có
//            if (inserted && ketQua.getChiTietKhuyenMais() != null) {
//                ketQua.getChiTietKhuyenMais().forEach(ct -> {
//                    ct.setKhuyenMai(ketQua);
//                    chiTietKhuyenMaiDAO.themChiTiet(ct);
//                });
//            }
//            return inserted;
//        }
        return false;

    }

    public boolean tuyChinhKhuyenMai(KhuyenMai km) {
        List<LoaiKhuyenMai> allLoaiKM = layTatCaLoaiKhuyenMai();
        List<MonAn> allMonAn = layTatCaMonAn();

//        KhuyenMaiDialog dialog = new KhuyenMaiDialog(km, allLoaiKM, allMonAn);
//        dialog.showAndWait();
//
//        KhuyenMai ketQua = dialog.layKetQua();
//        if (ketQua != null) {
//            boolean updated = khuyenMaiDAO.capNhatKhuyenMai(ketQua);
//
//            // Cập nhật lại chi tiết
//            chiTietKhuyenMaiDAO.xoaTheoMaKM(ketQua.getMaKM());
//            if (ketQua.getChiTietKhuyenMais() != null) {
//                ketQua.getChiTietKhuyenMais().forEach(chiTietKhuyenMaiDAO::themChiTiet);
//            }
//            return updated;
//        }
        return false;
    }

    public boolean xoaKhuyenMai(KhuyenMai km) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xóa");
        confirmAlert.setHeaderText("Bạn có chắc chắn muốn xóa khuyến mãi: " + km.getMaKM() + "?");
        confirmAlert.setContentText(km.getMoTa());

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            chiTietKhuyenMaiDAO.xoaTheoMaKM(km.getMaKM());
            return khuyenMaiDAO.xoaKhuyenMai(km.getMaKM());
        }
        return false;
    }
}
