package com.thefourrestaurant.util;

import java.time.LocalDateTime;

import com.thefourrestaurant.model.TaiKhoan;

/**
 * Simple in-memory session holder for the currently logged-in user.
 */
public final class Session {
    private static TaiKhoan currentUser;
    private static LocalDateTime loginTime;

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

    public static void clear() {
        currentUser = null;
        loginTime = null;
    }
}
