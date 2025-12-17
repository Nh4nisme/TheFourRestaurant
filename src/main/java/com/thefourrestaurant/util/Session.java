package com.thefourrestaurant.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.thefourrestaurant.model.TaiKhoan;

public final class Session {
    private static TaiKhoan currentUser;
    private static LocalDateTime loginTime;
    private static BigDecimal startingCash;

    private Session() {}

    public static void setCurrentUser(TaiKhoan user) {
        currentUser = user;
    }

    public static TaiKhoan getCurrentUser() {
        return currentUser;
    }

    public static void setLoginTime(LocalDateTime time) {
        loginTime = time;
    }

    public static LocalDateTime getLoginTime() {
        return loginTime;
    }

    public static void setStartingCash(BigDecimal amount) {
        startingCash = amount;
    }

    public static BigDecimal getStartingCash() {
        return startingCash;
    }

    public static void clear() {
        currentUser = null;
        loginTime = null;
        startingCash = null;
    }
}
