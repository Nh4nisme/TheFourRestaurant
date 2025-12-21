package com.thefourrestaurant.controller;

import com.thefourrestaurant.DAO.*;
import com.thefourrestaurant.model.*;
import com.thefourrestaurant.view.khuyenmai.KhuyenMaiDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class KhuyenMaiController {

    private final KhuyenMaiDAO khuyenMaiDAO;
    private final LoaiKhuyenMaiDAO loaiKhuyenMaiDAO;
    private final KhuyenMai_DieuKienDAO dieuKienDAO;
    private final DieuKien_MonDAO dieuKienMonDAO;
    private final DieuKien_MonTangDAO dieuKienMonTangDAO;
    private final MonAnDAO monAnDAO;
    private final KhungGio_KM_DAO khungGio_KM_DAO;

    public KhuyenMaiController() {
        this.khuyenMaiDAO = new KhuyenMaiDAO();
        this.loaiKhuyenMaiDAO = new LoaiKhuyenMaiDAO();
        this.dieuKienDAO = new KhuyenMai_DieuKienDAO();
        this.dieuKienMonDAO = new DieuKien_MonDAO();
        this.dieuKienMonTangDAO = new DieuKien_MonTangDAO();
        this.monAnDAO = new MonAnDAO();
        this.khungGio_KM_DAO = new KhungGio_KM_DAO();
    }

    public void capNhatKhuyenMaiChoDanhSachMonAn(List<MonAn> dsMonAn) {
        if (dsMonAn == null) return;

        // Reset thông tin khuyến mãi cũ
        for (MonAn ma : dsMonAn) {
            ma.setGiaSauGiam(ma.getDonGia());
            ma.setTenKhuyenMai(null);
        }

        List<KhuyenMai> dsKMSukien = khuyenMaiDAO.layDanhSachKhuyenMaiTheoKieu("SuKien");
        LocalDateTime bayGio = LocalDateTime.now();
        LocalTime gioHienTai = LocalTime.now();

        for (KhuyenMai km : dsKMSukien) {
            // Check ngày áp dụng
            if (km.getNgayBatDau() != null && bayGio.isBefore(km.getNgayBatDau())) continue;
            if (km.getNgayKetThuc() != null && bayGio.isAfter(km.getNgayKetThuc())) continue;

            // Check khung giờ
            List<KhungGio> dsKhungGio = layKhungGioTheoMaKM(km.getMaKM());
            if (dsKhungGio != null && !dsKhungGio.isEmpty()) {
                boolean trongKhungGio = false;
                for (KhungGio kg : dsKhungGio) {
                    if (gioHienTai.isAfter(kg.getGioBatDau()) && gioHienTai.isBefore(kg.getGioKetThuc())) {
                        trongKhungGio = true;
                        break;
                    }
                }
                if (!trongKhungGio) continue;
            }

            List<KhuyenMai_DieuKien> dsDieuKien = layDieuKienTheoMaKM(km.getMaKM());
            for (KhuyenMai_DieuKien dk : dsDieuKien) {
                if ("GIAM_TRUC_TIEP".equals(dk.getLoaiApDung())) {
                    for (DieuKien_Mon dkm : dk.getDanhSachMonDieuKien()) {
                        for (MonAn ma : dsMonAn) {
                            if (ma.getMaMonAn().equals(dkm.getMonAn().getMaMonAn())) {
                                BigDecimal giaMoi = tinhToanGiamGia(ma.getRawDonGia(), dk.getTyLeGiam(), dk.getSoTienGiam());
                                // Nếu chưa có khuyến mãi hoặc khuyến mãi này giảm sâu hơn
                                if (ma.getTenKhuyenMai() == null || giaMoi.compareTo(ma.getGiaSauGiam()) < 0) {
                                    ma.setGiaSauGiam(giaMoi);
                                    ma.setTenKhuyenMai(km.getTenKM());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public List<KhuyenMai> layDanhSachKhuyenMai() {
        return khuyenMaiDAO.layDanhSachKhuyenMai();
    }

    public List<KhuyenMai> layDanhSachKhuyenMaiTheoKieu(String kieuKM) {
        return khuyenMaiDAO.layDanhSachKhuyenMaiTheoKieu(kieuKM);
    }

    public List<KhuyenMai> layKhuyenMaiConHieuLucTheoKieu(String kieuKM) {
        return khuyenMaiDAO.layDanhSachKhuyenMaiConHieuLucTheoKieu(kieuKM);
    }


    public String taoMaKhuyenMaiMoi() {
        return khuyenMaiDAO.taoMaKhuyenMaiMoi();
    }

    public List<KhuyenMai_DieuKien> layDieuKienTheoMaKM(String maKM) {
        List<KhuyenMai_DieuKien> dsDieuKien = dieuKienDAO.layDieuKienTheoMaKM(maKM);
        for (KhuyenMai_DieuKien dk : dsDieuKien) {
            dk.setDanhSachMonDieuKien(dieuKienMonDAO.layMonTheoMaDieuKien(dk.getMaDieuKien()));
            dk.setDanhSachMonTang(dieuKienMonTangDAO.layMonTangTheoMaDieuKien(dk.getMaDieuKien()));
        }
        return dsDieuKien;
    }

    public List<KhungGio> layKhungGioTheoMaKM(String maKM) {
        return khungGio_KM_DAO.layKhungGioTheoMaKM(maKM);
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
                hienThiThongBao(owner, Alert.AlertType.INFORMATION, "Thêm khuyến mãi thành công!");
                return true;
            } else {
                hienThiThongBao(owner, Alert.AlertType.ERROR, "Thêm khuyến mãi thất bại.");
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
                hienThiThongBao(owner, Alert.AlertType.INFORMATION, "Cập nhật khuyến mãi thành công!");
                return true;
            } else {
                hienThiThongBao(owner, Alert.AlertType.ERROR, "Cập nhật khuyến mãi thất bại.");
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
                hienThiThongBao(owner, Alert.AlertType.INFORMATION, "Xóa khuyến mãi thành công!");
                return true;
            } else {
                hienThiThongBao(owner, Alert.AlertType.ERROR, "Xóa khuyến mãi thất bại.");
                return false;
            }
        }
        return false;
    }

    public List<MonAn> layDanhSachMonAn() {
        return monAnDAO.layTatCaMonAn();
    }

    public String taoMaDieuKienMoi() {
        return dieuKienDAO.taoMaDieuKienMoi();
    }

    public boolean themDieuKienKhuyenMai(Stage owner, KhuyenMai_DieuKien dk) {
        try {
            dk.setMaDieuKien(taoMaDieuKienMoi());
            if (dieuKienDAO.themDieuKien(dk)) {
                hienThiThongBao(owner, Alert.AlertType.INFORMATION, "Thêm điều kiện thành công!");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            hienThiThongBao(owner, Alert.AlertType.ERROR, "Lỗi khi thêm điều kiện: " + e.getMessage());
        }
        return false;
    }

    public boolean capNhatDieuKienKhuyenMai(Stage owner, KhuyenMai_DieuKien dk) {
        try {
            if (dieuKienDAO.capNhatDieuKien(dk)) {
                hienThiThongBao(owner, Alert.AlertType.INFORMATION, "Cập nhật điều kiện thành công!");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            hienThiThongBao(owner, Alert.AlertType.ERROR, "Lỗi khi cập nhật điều kiện: " + e.getMessage());
        }
        return false;
    }

    public boolean xoaDieuKienKhuyenMai(Stage owner, KhuyenMai_DieuKien dk) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc muốn xóa điều kiện này không?", ButtonType.YES, ButtonType.NO);
        confirm.initOwner(owner);
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                if (dieuKienDAO.xoaDieuKien(dk.getMaDieuKien())) {
                    hienThiThongBao(owner, Alert.AlertType.INFORMATION, "Xóa điều kiện thành công!");
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                hienThiThongBao(owner, Alert.AlertType.ERROR, "Lỗi khi xóa điều kiện: " + e.getMessage());
            }
        }
        return false;
    }

    public List<KhuyenMai> layKhuyenMaiDaXoa() {
        return khuyenMaiDAO.layKhuyenMaiDaXoa();
    }

    public boolean khoiPhucKhuyenMai(Stage owner, Set<KhuyenMai> cacKMCamKhoiPhuc) {
        if (cacKMCamKhoiPhuc == null || cacKMCamKhoiPhuc.isEmpty()) {
            hienThiThongBao(owner, Alert.AlertType.WARNING, "Không có khuyến mãi nào được chọn để khôi phục.");
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
            hienThiThongBao(owner, Alert.AlertType.INFORMATION, "Khôi phục thành công " + thanhCong + " khuyến mãi!");
            return true;
        } else if (thanhCong > 0 && thatBai > 0) {
            hienThiThongBao(owner, Alert.AlertType.WARNING, "Khôi phục thành công " + thanhCong + " khuyến mãi, thất bại " + thatBai + " khuyến mãi.");
            return true;
        } else {
            hienThiThongBao(owner, Alert.AlertType.ERROR, "Khôi phục thất bại tất cả các khuyến mãi.");
            return false;
        }
    }

    public void tinhGiaSauKhuyenMai(List<ChiTietPDB> danhSachMonTrongGio) {
        // Remove old gifted items first
        danhSachMonTrongGio.removeIf(ct -> ct.getDonGia() == 0.0 && ct.getKhuyenMaiApDung() != null && !ct.getKhuyenMaiApDung().getTenKhuyenMai().isEmpty());

        for (ChiTietPDB ct : danhSachMonTrongGio) {
            ct.setKhuyenMaiApDung(new KhuyenMaiApDung("", BigDecimal.valueOf(ct.getDonGia())));
        }

        List<KhuyenMai> khuyenMaiSuKien = khuyenMaiDAO.layDanhSachKhuyenMaiConHieuLucTheoKieu("SuKien");

        for (KhuyenMai km : khuyenMaiSuKien) {
            List<KhuyenMai_DieuKien> dieuKiens = layDieuKienTheoMaKM(km.getMaKM());
            for (KhuyenMai_DieuKien dk : dieuKiens) {
                if (dk.getLoaiApDung() == null) continue;

                switch (dk.getLoaiApDung().toUpperCase()) {
                    case "GIAM_TRUC_TIEP":
                        apDungGiamGiaTrucTiep(danhSachMonTrongGio, km, dk);
                        break;
                    case "THEO_COMBO":
                        apDungGiamGiaCombo(danhSachMonTrongGio, km, dk);
                        break;
                    case "MUA_X_GIAM_Y":
                        apDungGiamGiaMuaXTangY(danhSachMonTrongGio, km, dk);
                        break;
                    case "TANG_MON":
                        apDungTangMon(danhSachMonTrongGio, km, dk);
                        break;
                }
            }
        }
    }

    private void apDungTangMon(List<ChiTietPDB> cart, KhuyenMai km, KhuyenMai_DieuKien dk) {
        // Check if condition is met
        boolean conditionMet = true;
        if (dk.getDanhSachMonDieuKien() != null && !dk.getDanhSachMonDieuKien().isEmpty()) {
            Map<String, Integer> cartInventory = cart.stream()
                    .collect(Collectors.groupingBy(ct -> ct.getMonAn().getMaMonAn(), Collectors.summingInt(ChiTietPDB::getSoLuong)));

            for (DieuKien_Mon dkm : dk.getDanhSachMonDieuKien()) {
                int required = dkm.getSoLuong();
                int current = cartInventory.getOrDefault(dkm.getMonAn().getMaMonAn(), 0);
                if (current < required) {
                    conditionMet = false;
                    break;
                }
            }
        }

        if (conditionMet) {
            if (dk.getDanhSachMonTang() != null) {
                for (DieuKien_MonTang dmt : dk.getDanhSachMonTang()) {
                    // One gift item per condition met? Usually it's just add it once or for each set.
                    // Simplified: Add it once.
                    boolean alreadyGifted = cart.stream()
                            .anyMatch(ct -> ct.getMonAn().getMaMonAn().equals(dmt.getMonAnTang().getMaMonAn())
                                    && ct.getDonGia() == 0.0);

                    if (!alreadyGifted) {
                        ChiTietPDB ctTang = new ChiTietPDB();
                        ctTang.setMonAn(dmt.getMonAnTang());
                        ctTang.setSoLuong(dk.getSoLuongTang() != null ? dk.getSoLuongTang() : 1);
                        ctTang.setDonGia(0.0);
                        ctTang.setKhuyenMaiApDung(new KhuyenMaiApDung(km.getTenKM(), BigDecimal.ZERO));
                        cart.add(ctTang);
                    }
                }
            }
        }
    }

    private void apDungGiamGiaTrucTiep(List<ChiTietPDB> cart, KhuyenMai km, KhuyenMai_DieuKien dk) {
        for (DieuKien_Mon dkm : dk.getDanhSachMonDieuKien()) {
            for (ChiTietPDB ct : cart) {
                if (dkm.getMonAn().getMaMonAn().equals(ct.getMonAn().getMaMonAn())) {
                    BigDecimal originalPrice = BigDecimal.valueOf(ct.getDonGia());
                    BigDecimal newPrice = tinhToanGiamGia(originalPrice, dk.getTyLeGiam(), dk.getSoTienGiam());

                    if (newPrice.compareTo(ct.getKhuyenMaiApDung().getGiaSauGiam()) < 0) {
                        ct.setKhuyenMaiApDung(new KhuyenMaiApDung(km.getTenKM(), newPrice));
                    }
                }
            }
        }
    }

    private void apDungGiamGiaCombo(List<ChiTietPDB> cart, KhuyenMai km, KhuyenMai_DieuKien dk) {
        Map<String, Integer> cartInventory = cart.stream()
                .collect(Collectors.groupingBy(ct -> ct.getMonAn().getMaMonAn(), Collectors.summingInt(ChiTietPDB::getSoLuong)));

        List<DieuKien_Mon> comboItems = dk.getDanhSachMonDieuKien();
        if (comboItems.isEmpty()) return;

        int maxCombos = Integer.MAX_VALUE;
        for (DieuKien_Mon item : comboItems) {
            int requiredQty = item.getSoLuong();
            int cartQty = cartInventory.getOrDefault(item.getMonAn().getMaMonAn(), 0);
            maxCombos = Math.min(maxCombos, cartQty / requiredQty);
        }

        if (maxCombos > 0) {
            BigDecimal comboOriginalPrice = BigDecimal.ZERO;
            for (DieuKien_Mon item : comboItems) {
                MonAn monAn = monAnDAO.layMonAnTheoMa(item.getMonAn().getMaMonAn());
                comboOriginalPrice = comboOriginalPrice.add(monAn.getDonGia().multiply(BigDecimal.valueOf(item.getSoLuong())));
            }

            if (comboOriginalPrice.compareTo(BigDecimal.ZERO) == 0) return;

            BigDecimal comboDiscountAmount;
            if (dk.getTyLeGiam() != null) {
                comboDiscountAmount = comboOriginalPrice.multiply(dk.getTyLeGiam().divide(BigDecimal.valueOf(100)));
            } else if (dk.getSoTienGiam() != null) {
                comboDiscountAmount = dk.getSoTienGiam();
            } else {
                return;
            }

            BigDecimal comboFinalPrice = comboOriginalPrice.subtract(comboDiscountAmount);

            for (int i = 0; i < maxCombos; i++) {
                for (DieuKien_Mon comboItem : comboItems) {
                    for (ChiTietPDB cartItem : cart) {
                        if (cartItem.getMonAn().getMaMonAn().equals(comboItem.getMonAn().getMaMonAn())) {
                            BigDecimal originalPrice = BigDecimal.valueOf(cartItem.getDonGia());
                            BigDecimal proportionalPrice = originalPrice.multiply(comboFinalPrice).divide(comboOriginalPrice, 2, RoundingMode.HALF_UP);

                            if (proportionalPrice.compareTo(cartItem.getKhuyenMaiApDung().getGiaSauGiam()) < 0) {
                                cartItem.setKhuyenMaiApDung(new KhuyenMaiApDung(km.getTenKM(), proportionalPrice));
                            }
                        }
                    }
                }
            }
        }
    }

    private void apDungGiamGiaMuaXTangY(List<ChiTietPDB> cart, KhuyenMai km, KhuyenMai_DieuKien dk) {
        List<DieuKien_Mon> buyItems = dk.getDanhSachMonDieuKien().stream().filter(d -> "MUA".equals(d.getVaiTro())).collect(Collectors.toList());
        List<DieuKien_Mon> getItems = dk.getDanhSachMonDieuKien().stream().filter(d -> "NHAN_GIAM".equals(d.getVaiTro())).collect(Collectors.toList());

        if (buyItems.isEmpty() || getItems.isEmpty()) return;

        Map<String, Integer> cartInventory = cart.stream()
                .collect(Collectors.groupingBy(ct -> ct.getMonAn().getMaMonAn(), Collectors.summingInt(ChiTietPDB::getSoLuong)));

        int buyCount = 0;
        for (DieuKien_Mon item : buyItems) {
            buyCount += cartInventory.getOrDefault(item.getMonAn().getMaMonAn(), 0);
        }

        int requiredToBuy = buyItems.stream().mapToInt(DieuKien_Mon::getSoLuong).sum();
        if (requiredToBuy == 0) return;

        int numEligibleDiscounts = buyCount / requiredToBuy;

        for (DieuKien_Mon getItem : getItems) {
            for (ChiTietPDB cartItem : cart) {
                if (numEligibleDiscounts > 0 && cartItem.getMonAn().getMaMonAn().equals(getItem.getMonAn().getMaMonAn())) {
                    BigDecimal originalPrice = BigDecimal.valueOf(cartItem.getDonGia());
                    BigDecimal newPrice = tinhToanGiamGia(originalPrice, dk.getTyLeGiam(), dk.getSoTienGiam());

                    if (newPrice.compareTo(cartItem.getKhuyenMaiApDung().getGiaSauGiam()) < 0) {
                        cartItem.setKhuyenMaiApDung(new KhuyenMaiApDung(km.getTenKM(), newPrice));
                        numEligibleDiscounts--;
                    }
                }
            }
        }
    }

    private BigDecimal tinhToanGiamGia(BigDecimal originalPrice, BigDecimal percent, BigDecimal amount) {
        BigDecimal discountedPrice = originalPrice;
        if (percent != null && percent.compareTo(BigDecimal.ZERO) > 0) {
            discountedPrice = originalPrice.multiply(BigDecimal.ONE.subtract(percent.divide(BigDecimal.valueOf(100))));
        } else if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            discountedPrice = originalPrice.subtract(amount);
        }
        return discountedPrice.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : discountedPrice;
    }

    private void hienThiThongBao(Stage owner, Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message);
        alert.initOwner(owner);
        alert.showAndWait();
    }
}
