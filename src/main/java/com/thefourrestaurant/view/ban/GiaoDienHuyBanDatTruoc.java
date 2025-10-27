package com.thefourrestaurant.view.ban;

import com.thefourrestaurant.DAO.PhieuDatBanDAO;
import com.thefourrestaurant.model.PhieuDatBan;
import com.thefourrestaurant.model.Ban;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class GiaoDienHuyBanDatTruoc extends BorderPane {

    private TableView<PhieuDatBan> table;
    private PhieuDatBanDAO phieuDAO = new PhieuDatBanDAO();

    public GiaoDienHuyBanDatTruoc(Ban ban) {
        setPadding(new Insets(20));
        Label lblTitle = new Label("Danh sách phiếu đặt trước của " + ban.getTenBan());
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        table = new TableView<>();
        taoCotChoBang();

        List<PhieuDatBan> danhSach = phieuDAO.layDanhSachPhieuDatTruocTheoBan(ban.getMaBan());
        table.setItems(FXCollections.observableArrayList(danhSach));

        Button btnHuy = new Button("Hủy phiếu đã chọn");
        btnHuy.setOnAction(e -> huyPhieu());

        Button btnDong = new Button("Đóng");
        btnDong.setOnAction(e -> ((Stage) getScene().getWindow()).close());

        HBox bottom = new HBox(10, btnHuy, btnDong);
        bottom.setAlignment(Pos.CENTER_RIGHT);
        bottom.setPadding(new Insets(10));

        VBox content = new VBox(10, lblTitle, table, bottom);
        setCenter(content);
    }

    private void taoCotChoBang() {
        TableColumn<PhieuDatBan, String> colMaPhieu = new TableColumn<>("Mã phiếu");
        colMaPhieu.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getMaPDB()));

        TableColumn<PhieuDatBan, String> colNgay = new TableColumn<>("Ngày đặt");
        colNgay.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getNgayDat().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));

        TableColumn<PhieuDatBan, String> colNguoi = new TableColumn<>("Người đặt");
        colNguoi.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getKhachHang().getHoTen()));


        table.getColumns().addAll(colMaPhieu, colNgay, colNguoi);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void huyPhieu() {
        PhieuDatBan phieu = table.getSelectionModel().getSelectedItem();
        if (phieu == null) {
            new Alert(Alert.AlertType.WARNING, "Vui lòng chọn phiếu cần hủy!").showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc muốn hủy phiếu " + phieu.getMaPDB() + " không?",
                ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Xác nhận hủy");
        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            boolean thanhCong = phieuDAO.huyPhieuDatBan(phieu.getMaPDB());
            if (thanhCong) {
                table.getItems().remove(phieu);
                new Alert(Alert.AlertType.INFORMATION, "Đã hủy thành công phiếu " + phieu.getMaPDB()).showAndWait();
            } else {
                new Alert(Alert.AlertType.ERROR, "Không thể hủy phiếu!").showAndWait();
            }
        }
    }
}
