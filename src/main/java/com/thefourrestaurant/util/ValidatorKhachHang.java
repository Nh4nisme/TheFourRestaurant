package com.thefourrestaurant.util;

import com.thefourrestaurant.model.KhachHang;
import com.thefourrestaurant.model.LoaiKhachHang;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ValidatorKhachHang {
    private static final String regexMaKH = "^KH[0-9]{6}$";
    private static final String regexHoTen = "^[\\p{L} ]{1,50}$";
    private static final String regexGioiTinh = "^(Nam|Nu)$";
    private static final String regexSoDT = "^(\\+84|0)\\d{9,10}$";
    private static final String regexMaLoaiKH = "^LKH[A-Z0-9]{5}$";

    public static List<String> validate(String maKH, String hoTen, String gioiTinh, String soDT, LocalDate ngaySinh, LoaiKhachHang loai) {
        List<String> errors = new ArrayList<>();

        if (maKH == null || !maKH.matches(regexMaKH)) {
            errors.add("Mã khách hàng không hợp lệ (ví dụ: KH000001).");
        }

        if (hoTen == null || !hoTen.matches(regexHoTen)) {
            errors.add("Họ tên không hợp lệ (1-50 ký tự, chỉ chữ và khoảng trắng).");
        }

        if (gioiTinh == null || !gioiTinh.matches(regexGioiTinh)) {
            errors.add("Giới tính phải là 'Nam' hoặc 'Nu'.");
        }

        if (soDT != null && !soDT.isEmpty() && !soDT.matches(regexSoDT)) {
            errors.add("Số điện thoại không hợp lệ (ví dụ: 0912345678 hoặc +84912345678).");
        }

        if (ngaySinh == null || !ngaySinh.isBefore(LocalDate.now())) {
            errors.add("Ngày sinh phải nhỏ hơn ngày hiện tại.");
        }

        if (loai == null || loai.getMaLoaiKH() == null || !loai.getMaLoaiKH().matches(regexMaLoaiKH)) {
            errors.add("Vui lòng chọn loại khách hàng hợp lệ.");
        }

        return errors;
    }

    public static List<String> validate(KhachHang kh) {
        if (kh == null) return List.of("Khách hàng không được null");
        return validate(
                kh.getMaKH(),
                kh.getHoTen(),
                kh.getGioiTinh(),
                kh.getSoDT(),
                kh.getNgaySinh() != null ? kh.getNgaySinh().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null,
                kh.getLoaiKH()
        );
    }
}
