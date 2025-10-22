package com.thefourrestaurant.controller;

import com.thefourrestaurant.DAO.TaiKhoanDAO;
import com.thefourrestaurant.DAO.VaiTroDAO;
import com.thefourrestaurant.model.TaiKhoan;
import com.thefourrestaurant.model.VaiTro;
import com.thefourrestaurant.view.taikhoan.GiaoDienChiTietTaiKhoan;

import java.util.List;

public class TaiKhoanController {

    private final VaiTroDAO vaiTroDAO;

    public TaiKhoanController() {
        vaiTroDAO = new VaiTroDAO();
    }

    public List<TaiKhoan> layDanhSachTaiKhoan() {
        return TaiKhoanDAO.layDanhSachTaiKhoan();
    }

    public List<VaiTro> layDanhSachVaiTro() {
        return vaiTroDAO.layDanhSachVaiTro();
    }

    public TaiKhoan layTaiKhoanTheoMa(String maTK) {
        return TaiKhoanDAO.layTaiKhoanTheoMa(maTK);
    }

    public TaiKhoan layTaiKhoanTheoTenDN(String tenDN) {
        return TaiKhoanDAO.layTaiKhoanTheoTenDangNhap(tenDN);
    }

    // Tạo mới tài khoản, trả về thông báo lỗi hoặc "OK"
    public String taoTaiKhoan(String maTK, String tenDN, String matKhau, VaiTro vaiTro) {
        if (maTK == null || maTK.isEmpty() ||
                tenDN == null || tenDN.isEmpty() ||
                matKhau == null || matKhau.isEmpty() ||
                vaiTro == null) {
            return "Vui lòng điền đầy đủ thông tin!";
        }

        // Kiểm tra trùng mã TK
        if (layTaiKhoanTheoMa(maTK) != null) {
            return "Mã tài khoản đã tồn tại!";
        }

        // Kiểm tra trùng tên đăng nhập
        if (layTaiKhoanTheoTenDN(tenDN) != null) {
            return "Tên đăng nhập đã tồn tại!";
        }

        TaiKhoan tk = new TaiKhoan();
        tk.setMaTK(maTK);
        tk.setTenDN(tenDN);
        tk.setMatKhau(matKhau);
        tk.setVaiTro(vaiTro);
        tk.setDeleted(false); // mặc định 0

        boolean ok = TaiKhoanDAO.themTaiKhoan(tk);
        return ok ? "OK" : "Tạo tài khoản thất bại!";
    }

    // Cập nhật tài khoản, trả về thông báo lỗi hoặc "OK"
    public String capNhatTaiKhoan(String maTK, String tenDN, String matKhau, VaiTro vaiTro) {
        if (maTK == null || maTK.isEmpty() ||
                tenDN == null || tenDN.isEmpty() ||
                matKhau == null || matKhau.isEmpty() ||
                vaiTro == null) {
            return "Vui lòng điền đầy đủ thông tin!";
        }

        TaiKhoan tk = new TaiKhoan();
        tk.setMaTK(maTK);
        tk.setTenDN(tenDN);
        tk.setMatKhau(matKhau);
        tk.setVaiTro(vaiTro);

        boolean ok = TaiKhoanDAO.capNhatTaiKhoan(tk);
        return ok ? "OK" : "Cập nhật thất bại!";
    }

    public boolean xoaTaiKhoan(String maTK) {
        return TaiKhoanDAO.xoaTaiKhoan(maTK);
    }
}
