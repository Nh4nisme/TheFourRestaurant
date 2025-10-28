package com.thefourrestaurant.controller;

import com.thefourrestaurant.DAO.PhuongThucThanhToanDAO;
import com.thefourrestaurant.model.PhuongThucThanhToan;

import java.util.List;

public class PhuongThucThanhToanController {
    private PhuongThucThanhToanDAO phuongThucThanhToanDAO =  new PhuongThucThanhToanDAO();

    public List<PhuongThucThanhToan> layPhuongThucThanhToan(){
        return phuongThucThanhToanDAO.layDanhSachPhuongThucThanhToan();
    }

    public PhuongThucThanhToan layPTTTTheoMa(String maPTTT) {
        return phuongThucThanhToanDAO.layPTTTTheoMa(maPTTT);
    }
}
