package com.thefourrestaurant.model;

/**
 * Lớp này đại diện cho một món ăn cụ thể trong một điều kiện khuyến mãi.
 * Nó xác định vai trò và số lượng của món ăn đó.
 */
public class DieuKien_Mon {
    private KhuyenMai_DieuKien dieuKien; // Điều kiện cha
    private MonAn monAn; // Món ăn được tham chiếu
    private int soLuong; // Số lượng yêu cầu của món ăn này
    private String vaiTro; // Vai trò của món ăn: 'MUA', 'NHAN_GIAM', 'GIAM_TRUC_TIEP'

    public DieuKien_Mon() {}

    // Getters and Setters
    public KhuyenMai_DieuKien getDieuKien() {
        return dieuKien;
    }

    public void setDieuKien(KhuyenMai_DieuKien dieuKien) {
        this.dieuKien = dieuKien;
    }

    public MonAn getMonAn() {
        return monAn;
    }

    public void setMonAn(MonAn monAn) {
        this.monAn = monAn;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }
}
