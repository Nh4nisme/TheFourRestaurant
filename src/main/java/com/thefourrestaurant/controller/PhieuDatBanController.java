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

    public PhieuDatBan layPhieuTheoBan(String maBan) {
        PhieuDatBan pdb = phieuDatBanDAO.layPhieuDangHoatDongTheoBan(maBan);
        if (pdb != null) {
            pdb.setChiTietPDB(chiTietPDBDAO.layTheoPhieu(pdb.getMaPDB()));
        }
        return pdb;
    }

    public boolean xoaPhieuDatBan(String maPDB) {return phieuDatBanDAO.xoaPhieu(maPDB);}

}
