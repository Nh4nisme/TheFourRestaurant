package com.thefourrestaurant.controller;

import com.thefourrestaurant.DAO.BanDAO;
import com.thefourrestaurant.model.Ban;

import java.util.List;

public class BanController {

    private final BanDAO banDAO = new BanDAO();

    public boolean capNhatTrangThaiDanhSach(List<Ban> dsBan, String trangThaiMoi) {
        if (dsBan == null || dsBan.isEmpty()) {
            return false;
        }

        int soBanCapNhat = banDAO.capNhatTrangThaiDanhSach(dsBan, trangThaiMoi);
        return soBanCapNhat > 0;
    }
}
