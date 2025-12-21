package com.thefourrestaurant.view.ban;
import com.thefourrestaurant.DAO.PhieuDatBanDAO;
import com.thefourrestaurant.controller.CountdownController;
import com.thefourrestaurant.DAO.BanDAO;
import com.thefourrestaurant.DAO.NhanVienDAO;
import com.thefourrestaurant.util.Session;
import com.thefourrestaurant.model.Ban;
import com.thefourrestaurant.model.KhachHang;
import com.thefourrestaurant.model.NhanVien;
import com.thefourrestaurant.model.PhieuDatBan;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import java.util.List;
import java.time.LocalDateTime;

class GiaoDienDatBanNgay extends GiaoDienDatBanBase {

	public GiaoDienDatBanNgay(List<Ban> dsBan, StackPane parentPane, QuanLiBan quanLiBan){
        super(dsBan, parentPane, quanLiBan);
    }

    @Override
    protected String getTitle(){ return "Đặt bàn ngay"; }

    @Override
    protected Node createSpecialRow(){
        // Đặt bàn ngay: không cần chọn ngày/giờ -> có thể hiển thị giờ hiện tại
        return null;
    }

    @Override
    protected void wireDatBanHandler(){
        try {
        	KhachHang kh = validateAllCommon();
            if (kh == null) return;

            int soNguoi = Integer.parseInt(txtSoNguoi.getText().trim());

            PhieuDatBan pdb = new PhieuDatBan();
            pdb.setDanhSachBan(dsBan);
            pdb.setNgayDat(LocalDateTime.now());
            pdb.setSoNguoi(soNguoi);
            pdb.setKhachHang(kh);
            NhanVien assigned = Session.getCurrentUser()!=null ? new NhanVienDAO().layDanhSachNhanVien().stream()
                    .filter(nv->nv.getMaTK()!=null && nv.getMaTK().getMaTK().equals(Session.getCurrentUser().getMaTK()))
                    .findFirst().orElse(new NhanVien("NV000001")) : new NhanVien("NV000001");
            pdb.setNhanVien(assigned);

            PhieuDatBanDAO dao = new PhieuDatBanDAO();
            boolean ok = dao.themPhieu(pdb, "DAT_NGAY", dsBan);

            if (ok) {
                Ban banChinh = dsBan.get(0);
                showDatBanThanhCong(parentPane, banChinh, pdb);
            } else {
                showDatBanLoi("Đặt bàn thất bại. Vui lòng thử lại!");
            }
        } catch(Exception ex){ lblTenKhachDat.setText("Có lỗi khi lưu"); ex.printStackTrace();}
    }
}