package com.thefourrestaurant.controller;

import com.thefourrestaurant.DAO.KhachHangDAO;
import com.thefourrestaurant.DAO.LoaiKhachHangDAO;
import com.thefourrestaurant.model.KhachHang;
import com.thefourrestaurant.model.LoaiKhachHang;

import java.time.LocalDate;
import java.util.List;

public class KhachHangController {

    private final KhachHangDAO khachHangDAO;
    private final LoaiKhachHangDAO loaiKhachHangDAO;

    public KhachHangController() {
        this.khachHangDAO = new KhachHangDAO();
        this.loaiKhachHangDAO = new LoaiKhachHangDAO();
    }

    /** =========================
     * LẤY DANH SÁCH
     * ========================= */
    public List<KhachHang> layDanhSachKhachHang() {
        return khachHangDAO.layDanhSachKhachHang();
    }

    public List<LoaiKhachHang> layDanhSachLoaiKhachHang() {
        return loaiKhachHangDAO.layDanhSachLoaiKhachHang();
    }

    /** =========================
     * TẠO MỚI KHÁCH HÀNG
     * ========================= */
    public String taoKhachHang(String hoTen, LocalDate ngaySinh,
                               String gioiTinh, String soDT, LoaiKhachHang loaiKH) {

        // ==== Kiểm tra hợp lệ ====
        if (hoTen == null || hoTen.isEmpty())
            return "Họ tên khách hàng không được để trống!";
        if (ngaySinh == null)
            return "Vui lòng chọn ngày sinh!";
        if (gioiTinh == null || gioiTinh.isEmpty())
            return "Vui lòng chọn giới tính!";
        if (soDT == null || soDT.isEmpty())
            return "Số điện thoại không được để trống!";
        if (loaiKH == null)
            return "Vui lòng chọn loại khách hàng!";

        // ==== Kiểm tra trùng số điện thoại ====
        if (khachHangDAO.layKhachHangTheoSDT(soDT) != null)
            return "Số điện thoại này đã tồn tại!";

        // ==== Tạo đối tượng ====
        KhachHang kh = new KhachHang();
        kh.setHoTen(hoTen);
        kh.setNgaySinh(java.sql.Date.valueOf(ngaySinh));
        kh.setGioiTinh(gioiTinh);
        kh.setSoDT(soDT);
        kh.setLoaiKH(loaiKH);
        kh.setDeleted(false);

        boolean ok = khachHangDAO.themKhachHang(kh);
        return ok ? "OK" : "Không thể thêm khách hàng!";
    }

    /** =========================
     * CẬP NHẬT KHÁCH HÀNG
     * ========================= */
    public String capNhatKhachHang(String maKH, String hoTen, LocalDate ngaySinh,
                                   String gioiTinh, String soDT, LoaiKhachHang loaiKH) {

        if (maKH == null || maKH.isEmpty())
            return "Không tìm thấy mã khách hàng để cập nhật!";
        if (hoTen == null || hoTen.isEmpty())
            return "Họ tên không được để trống!";
        if (ngaySinh == null)
            return "Vui lòng chọn ngày sinh!";
        if (gioiTinh == null || gioiTinh.isEmpty())
            return "Vui lòng chọn giới tính!";
        if (soDT == null || soDT.isEmpty())
            return "Số điện thoại không được để trống!";
        if (loaiKH == null)
            return "Vui lòng chọn loại khách hàng!";

        KhachHang kh = khachHangDAO.layKhachHangTheoMa(maKH);
        if (kh == null)
            return "Không tìm thấy khách hàng!";

        kh.setHoTen(hoTen);
        kh.setNgaySinh(java.sql.Date.valueOf(ngaySinh));
        kh.setGioiTinh(gioiTinh);
        kh.setSoDT(soDT);
        kh.setLoaiKH(loaiKH);

        boolean ok = khachHangDAO.capNhatKhachHang(kh);
        return ok ? "OK" : "Cập nhật thất bại!";
    }
//
//    /** =========================
//     * XÓA KHÁCH HÀNG
//     * ========================= */
    public boolean xoaKhachHang(String maKH) {
        if (maKH == null || maKH.isEmpty())
            return false;
        return khachHangDAO.xoaKhachHang(maKH);
    }
}