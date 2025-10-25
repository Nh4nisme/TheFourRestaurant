package com.thefourrestaurant.controller;

import com.thefourrestaurant.DAO.ChiTietPDBDAO;
import com.thefourrestaurant.DAO.PhieuDatBanDAO;
import com.thefourrestaurant.model.ChiTietPDB;
import com.thefourrestaurant.model.PhieuDatBan;

import java.util.List;

public class PhieuDatBanController {
    private final PhieuDatBanDAO phieuDatBanDAO = new PhieuDatBanDAO();
    private final ChiTietPDBDAO chiTietPDBDAO = new ChiTietPDBDAO();

    public PhieuDatBanController() {

    }

    public List<PhieuDatBan> layDanhSachPDB() {return phieuDatBanDAO.layTatCaPhieu();}

    public List<ChiTietPDB> layChiTietPDB(String maPDB) {return chiTietPDBDAO.layTheoPhieu(maPDB);}

}
