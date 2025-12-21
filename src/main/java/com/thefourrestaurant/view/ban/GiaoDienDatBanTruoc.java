package com.thefourrestaurant.view.ban;
import com.thefourrestaurant.model.Ban;
import com.thefourrestaurant.model.KhachHang;
import com.thefourrestaurant.model.NhanVien;
import com.thefourrestaurant.model.PhieuDatBan;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import com.thefourrestaurant.DAO.NhanVienDAO;
import com.thefourrestaurant.util.Session;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

class GiaoDienDatBanTruoc extends GiaoDienDatBanBase {
    private DatePicker dtpNgayNhanBan;
    private ComboBox<String> cboGioNhanBan;

    public GiaoDienDatBanTruoc(List<Ban> dsBan, StackPane parentPane, QuanLiBan quanLiBan){
        super(dsBan, parentPane, quanLiBan);
    }

    @Override
    protected String getTitle(){ return "Đặt bàn trước"; }

    @Override
    protected Node createSpecialRow(){
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);

        Label lblNgayNhanBan = createLabel("Ngày nhận bàn:");
        lblNgayNhanBan.setPrefWidth(120);
        dtpNgayNhanBan = new DatePicker();
        dtpNgayNhanBan.setDayCellFactory(p -> new DateCell(){
            @Override
            public void updateItem(LocalDate date, boolean empty){
                super.updateItem(date,empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
        dtpNgayNhanBan.setPrefWidth(230);

        Label lblGioNhanBan = createLabel("Giờ nhận bàn:");
        lblGioNhanBan.setPrefWidth(100);
        cboGioNhanBan = new ComboBox<>();
        cboGioNhanBan.setPrefWidth(230);
        // Thêm các khung giờ từ 10:00 đến 22:00, bước 15 phút
        for (int h = 10; h <= 22; h++) {
            for (int m = 0; m < 60; m += 15) {
                cboGioNhanBan.getItems().add(String.format("%02d:%02d", h, m));
            }
        }
        cboGioNhanBan.setValue("10:00"); // mặc định

        // Khi chọn giờ hoặc ngày thì kiểm tra trùng giờ
        cboGioNhanBan.setOnAction(e -> checkTrungGio());
        dtpNgayNhanBan.setOnAction(e -> checkTrungGio());

        row.getChildren().addAll(lblNgayNhanBan, dtpNgayNhanBan, lblGioNhanBan, cboGioNhanBan);
        return row;
    }

    // Lấy giờ từ ComboBox
    private LocalTime getGioNhanBan() {
        String gioStr = cboGioNhanBan.getValue();
        if (gioStr == null || gioStr.isBlank()) return null;
        return LocalTime.parse(gioStr);
    }

    // Kiểm tra trùng giờ
    private void checkTrungGio() {
        if (dsBan == null || dsBan.isEmpty()) return;

        LocalDate ngay = dtpNgayNhanBan.getValue();
        LocalTime gio = getGioNhanBan();
        if (ngay == null || gio == null) return;

        LocalDateTime ngayGioMoi = LocalDateTime.of(ngay, gio);

        for (Ban ban : dsBan) {
            if (phieuDatBanDAO.kiemTraTrungGioDatTruoc(ban.getMaBan(), ngayGioMoi)) {
                showDatBanLoi(
                    "Bàn " + ban.getTenBan() +
                    " đã có người đặt vào " + gio + " ngày " + ngay
                );
                return; // ❗ chỉ cần 1 bàn trùng là dừng
            }
        }
    }

    @Override
    protected void wireDatBanHandler() {
        try {
            KhachHang kh = validateAllCommon();
            if (kh == null) return;

            int soNguoi = Integer.parseInt(txtSoNguoi.getText().trim());

            LocalDate ngay = dtpNgayNhanBan.getValue();
            LocalTime gio = getGioNhanBan();

            if (ngay == null || ngay.isBefore(LocalDate.now())) {
                showDatBanLoi("Vui lòng chọn ngày nhận bàn!");
                return;
            }

            if (gio == null) {
                showDatBanLoi("Vui lòng chọn giờ nhận bàn!");
                return;
            }
            
            if (ngay.equals(LocalDate.now())) {
                LocalTime gioHienTai = LocalTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                String gioHienTaiStr = gioHienTai.format(formatter);
                if (!gio.isAfter(gioHienTai)) {
                    showDatBanLoi("Giờ nhận bàn phải sau: " + gioHienTaiStr);
                    return;
                }
            }

            if (dsBan.isEmpty()) return;

            Ban banChinh = dsBan.get(0);
            LocalDateTime ngayGioMoi = LocalDateTime.of(ngay, gio);

            if (phieuDatBanDAO.kiemTraTrungGioDatTruoc(banChinh.getMaBan(), ngayGioMoi)) {
                showDatBanLoi("Bàn " + banChinh.getTenBan() + " đã có người đặt vào giờ này!");
                return;
            }

            PhieuDatBan pdb = new PhieuDatBan();
            pdb.setDanhSachBan(dsBan);
            pdb.setSoNguoi(soNguoi);
            pdb.setKhachHang(kh);
            pdb.setNgayDat(ngayGioMoi);
            pdb.setNgayTao(LocalDateTime.now());

            NhanVien assigned = Session.getCurrentUser() != null
                    ? new NhanVienDAO().layDanhSachNhanVien().stream()
                            .filter(nv -> nv.getMaTK() != null 
                                    && nv.getMaTK().getMaTK().equals(Session.getCurrentUser().getMaTK()))
                            .findFirst().orElse(new NhanVien("NV000001"))
                    : new NhanVien("NV000001");

            pdb.setNhanVien(assigned);

            boolean ok = phieuDatBanDAO.themPhieu(pdb, "DAT_TRUOC", dsBan);

            if (ok) {
                showDatBanThanhCong(parentPane, banChinh, pdb);
            } else {
                showDatBanLoi("Đặt bàn trước thất bại. Vui lòng thử lại!");
            }

        } catch (Exception ex) {
            showDatBanLoi("Có lỗi khi lưu!");
            ex.printStackTrace();
        }
    }


}