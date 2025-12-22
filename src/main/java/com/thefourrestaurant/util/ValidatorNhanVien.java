package com.thefourrestaurant.util;

import javafx.scene.control.Alert;
import javafx.stage.Window;

import java.util.regex.Pattern;

public class ValidatorNhanVien {
    private static final Pattern HO_TEN_PATTERN = Pattern.compile("^[a-zA-ZÀ-ỹ\\s]+$");
    private static final Pattern SDT_PATTERN = Pattern.compile("^0\\d{9,10}$");

    public static boolean validateHoTen(String hoTen, Window owner) {
        if (hoTen == null || hoTen.trim().isEmpty()) {
            showAlert(owner, Alert.AlertType.ERROR, "Lỗi nhập liệu", "Trường 'Họ tên' không được để trống.");
            return false;
        }
        if (!HO_TEN_PATTERN.matcher(hoTen.trim()).matches()) {
            showAlert(owner, Alert.AlertType.ERROR, "Lỗi nhập liệu", "Họ tên chỉ được chứa chữ cái và khoảng trắng.");
            return false;
        }
        return true;
    }

    public static boolean validateSDT(String sdt, Window owner) {
        if (sdt == null || sdt.trim().isEmpty()) {
            return true; // cho phép để trống
        }
        if (!SDT_PATTERN.matcher(sdt.trim()).matches()) {
            showAlert(owner, Alert.AlertType.ERROR, "Lỗi nhập liệu", "Số điện thoại không hợp lệ. Phải bắt đầu bằng 0 và gồm 10 hoặc 11 chữ số.");
            return false;
        }
        return true;
    }

    public static void showAlert(Window owner, Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.initOwner(owner);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
