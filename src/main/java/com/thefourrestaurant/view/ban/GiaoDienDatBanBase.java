package com.thefourrestaurant.view.ban;

import com.thefourrestaurant.view.components.ButtonSample2;
import com.thefourrestaurant.view.components.ButtonSample2.Variant;
import com.thefourrestaurant.DAO.*;
import com.thefourrestaurant.model.*;
import com.thefourrestaurant.view.khachhang.GiaoDienThemKhachHang;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

// ==================== Lớp cha chung ====================
public abstract class GiaoDienDatBanBase extends VBox {
    protected List<Ban> dsBan;
    protected StackPane parentPane;
    protected QuanLiBan quanLiBan;

    protected Label lblTrangThaiStatus;
    protected Label lblLoaiBanValue;
    protected TextField txtSoNguoi;
    protected Label lblGiaTienValue;
    protected TextField txtSDTKhachDat;
    protected Label lblTenKhachDat;
    protected Button btnKiemTra;
    protected Button btnDatBan;
    protected Button btnQuayLai;

    protected KhachHang selectedKhachHang;
    protected final KhachHangDAO khachHangDAO = new KhachHangDAO();
    protected final PhieuDatBanDAO phieuDatBanDAO = new PhieuDatBanDAO();
	private BanDAO banDAO =  new BanDAO();

    public GiaoDienDatBanBase(List<Ban> dsBan, StackPane parentPane, QuanLiBan quanLiBan){
        this.dsBan = dsBan != null ? dsBan : new ArrayList<>();
        this.parentPane = parentPane;
        this.quanLiBan = quanLiBan;

        setSpacing(0);
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: #F5F5F5;");

        buildUI();
        wireCommonHandlers();
    }

    protected void buildUI(){
        Label lblTitle = new Label(getTitle());
        lblTitle.setStyle("-fx-font-size: 18px; -fx-text-fill: #E19E11; -fx-font-weight: bold;");
        HBox titleBar = new HBox(lblTitle);
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.setPadding(new Insets(10,20,10,20));
        titleBar.setStyle("-fx-background-color: #1E424D;");
        titleBar.setPrefHeight(50);

        VBox contentCard = new VBox(20);
        contentCard.setAlignment(Pos.TOP_CENTER);
        contentCard.setPadding(new Insets(30));
        contentCard.setMaxWidth(650);
        contentCard.setStyle("-fx-background-color: transparent;");

        Ban banHienTai = dsBan.isEmpty() ? null : dsBan.get(0);
        Label lblBanHeader = new Label(banHienTai != null ? banHienTai.getTenBan() : "");
        lblBanHeader.setStyle("-fx-font-size: 22px; -fx-text-fill: #DDB248; -fx-font-weight: bold;");

        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER_LEFT);

        // Row 1: trạng thái + loại bàn
        HBox row1 = new HBox(20);
        row1.setAlignment(Pos.CENTER_LEFT);
        Label lblTrangThai = createLabel("Trạng thái:");
        lblTrangThai.setPrefWidth(120);
        lblTrangThaiStatus = new Label(banHienTai != null ? banHienTai.getTrangThai() : "");
        lblTrangThaiStatus.setStyle("-fx-font-size:14px; -fx-text-fill:black;");
        lblTrangThaiStatus.setPrefWidth(230);

        Label lblLoaiBan = createLabel("Loại bàn:");
        lblLoaiBan.setPrefWidth(100);
        String tenLoaiBan = "";
        try{
            if(banHienTai != null && banHienTai.getMaBan() != null){
                tenLoaiBan = new LoaiBanDAO().layTenLoaiTheoBan(banHienTai.getMaBan());
                if((tenLoaiBan==null || tenLoaiBan.isEmpty()) && banHienTai.getLoaiBan()!=null)
                    tenLoaiBan = banHienTai.getLoaiBan().getTenLoaiBan();
            }
        }catch(Exception ignore){}
        lblLoaiBanValue = new Label(tenLoaiBan);
        lblLoaiBanValue.setStyle("-fx-font-size:14px; -fx-text-fill:black;");
        lblLoaiBanValue.setPrefWidth(230);

        row1.getChildren().addAll(lblTrangThai, lblTrangThaiStatus, lblLoaiBan, lblLoaiBanValue);

        // Row 2: số người + giá tiền
        HBox row2 = new HBox(20);
        row2.setAlignment(Pos.CENTER_LEFT);
        Label lblSoNguoi = createLabel("Số người:");
        lblSoNguoi.setPrefWidth(120);
        txtSoNguoi = createNumericTextField(Pattern.compile("\\d{0,3}"));
        txtSoNguoi.setPromptText("Chỉ nhập số");
        txtSoNguoi.setPrefWidth(230);

        Label lblGiaTien = createLabel("Giá tiền:");
        lblGiaTien.setPrefWidth(100);
        lblGiaTienValue = new Label("");
        lblGiaTienValue.setStyle("-fx-font-size:14px; -fx-text-fill:black;");
        lblGiaTienValue.setPrefWidth(230);

        row2.getChildren().addAll(txtSoNguoi != null ? lblSoNguoi : null, txtSoNguoi, lblGiaTien, lblGiaTienValue);

        formBox.getChildren().addAll(row1, row2);

        // Row SDT + tên khách
        HBox rowSDT = new HBox(10);
        rowSDT.setAlignment(Pos.CENTER_LEFT);
        Label lblSDT = createLabel("SDT khách đặt:");
        lblSDT.setPrefWidth(120);
        txtSDTKhachDat = createNumericTextField(Pattern.compile("\\d{0,11}"));
        txtSDTKhachDat.setPromptText("Chỉ nhập số (10-11 chữ số)");
        HBox.setHgrow(txtSDTKhachDat, Priority.ALWAYS);
        btnKiemTra = new ButtonSample2("Kiểm tra", Variant.YELLOW, 100);
        rowSDT.getChildren().addAll(lblSDT, txtSDTKhachDat, btnKiemTra);

        HBox rowTen = new HBox(10);
        rowTen.setAlignment(Pos.CENTER_LEFT);
        Label lblTenKhach = createLabel("Tên khách đặt:");
        lblTenKhach.setPrefWidth(120);
        lblTenKhachDat = new Label("");
        lblTenKhachDat.setStyle("-fx-font-size:14px; -fx-text-fill:#333333;");
        rowTen.getChildren().addAll(lblTenKhach, lblTenKhachDat);

        formBox.getChildren().addAll(rowSDT,rowTen);

        // Row đặc biệt (do lớp con override)
        Node specialRow = createSpecialRow();
        if(specialRow != null) formBox.getChildren().add(specialRow);

        // Button bar
        HBox buttonBar = new HBox(20);
        buttonBar.setAlignment(Pos.CENTER_LEFT);
        buttonBar.setPadding(new Insets(20,0,0,0));
        btnQuayLai = new ButtonSample2("Quay lại", Variant.YELLOW, 100);
        btnDatBan = new ButtonSample2("Đặt bàn", Variant.YELLOW, 100);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        buttonBar.getChildren().addAll(btnQuayLai, spacer, btnDatBan);

        contentCard.getChildren().addAll(lblBanHeader, formBox, buttonBar);

        VBox centerWrapper = new VBox(contentCard);
        centerWrapper.setAlignment(Pos.CENTER);
        centerWrapper.setPadding(new Insets(40));
        VBox.setVgrow(centerWrapper, Priority.ALWAYS);

        getChildren().addAll(titleBar, centerWrapper);

        loadLoaiBan();
        loadGiaTienTheoLoaiBan();
    }

    protected abstract String getTitle();
    protected abstract Node createSpecialRow();
    protected abstract void wireDatBanHandler();

    // ==================== Helpers chung ====================
    protected Label createLabel(String text){
        Label lbl = new Label(text);
        lbl.setStyle("-fx-font-size:14px;-fx-text-fill:#E19E11;-fx-font-weight:bold;");
        lbl.setMinWidth(Region.USE_PREF_SIZE);
        return lbl;
    }

    protected TextField createTextField(){
        TextField tf = new TextField();
        tf.setStyle("-fx-background-color:white;-fx-border-color:#CCCCCC;-fx-border-radius:10;-fx-background-radius:10;");
        tf.setPrefHeight(35);
        return tf;
    }

    protected TextField createNumericTextField(Pattern pattern){
        TextField tf = createTextField();
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            return pattern.matcher(newText).matches()?change:null;
        };
        tf.setTextFormatter(new TextFormatter<>(filter));
        return tf;
    }

    protected void loadLoaiBan(){
        try{
            String tenLoai = null;
            if(!dsBan.isEmpty()){
                Ban b = dsBan.get(0);
                if(b.getMaBan()!=null) tenLoai = new LoaiBanDAO().layTenLoaiTheoBan(b.getMaBan());
                if((tenLoai==null || tenLoai.isEmpty()) && b.getLoaiBan()!=null)
                    tenLoai = b.getLoaiBan().getTenLoaiBan();
            }
            lblLoaiBanValue.setText(tenLoai != null ? tenLoai : "");
        }catch(Exception ignore){
            lblLoaiBanValue.setText("");
        }
    }

    @SuppressWarnings("deprecation")
	protected void loadGiaTienTheoLoaiBan(){
        try{
            BigDecimal gia = null;
            if(!dsBan.isEmpty()){
                Ban b = dsBan.get(0);
                if(b.getLoaiBan()!=null && b.getLoaiBan().getGiaTien()!=null)
                    gia = b.getLoaiBan().getGiaTien();
                else if(b.getMaBan()!=null){
                    Ban ref = new BanDAO().layTheoMa(b.getMaBan());
                    if(ref!=null && ref.getLoaiBan()!=null) gia = ref.getLoaiBan().getGiaTien();
                }
            }
            if(gia==null || gia.compareTo(BigDecimal.ZERO)<=0){
                String tenLoai = lblLoaiBanValue != null ? lblLoaiBanValue.getText() : null;
                if(tenLoai != null){
                    if(tenLoai.contains("8")) gia = new BigDecimal(500000);
                    else if(tenLoai.contains("6")) gia = new BigDecimal(400000);
                    else if(tenLoai.contains("4")) gia = new BigDecimal(300000);
                    else if(tenLoai.contains("2")) gia = new BigDecimal(200000);
                }
            }
            lblGiaTienValue.setText(gia != null ? NumberFormat.getInstance(new Locale("vi","VN")).format(gia)+" VNĐ" : "");
        }catch(Exception e){lblGiaTienValue.setText("");}
    }

    private void wireCommonHandlers(){
        // Kiểm tra SDT khách
        btnKiemTra.setOnAction(e->{
            String sdt = txtSDTKhachDat.getText()==null?"":txtSDTKhachDat.getText().trim();
            if(sdt.length()<10){ lblTenKhachDat.setText("SDT không hợp lệ"); return; }
            KhachHang kh = khachHangDAO.layKhachHangTheoSDT(sdt);
            if(kh!=null){
                selectedKhachHang = kh;
                lblTenKhachDat.setText(kh.getHoTen());
            }else{
                Stage st = new Stage();
                GiaoDienThemKhachHang view = new GiaoDienThemKhachHang(sdt, khMoi->{
                    selectedKhachHang = khMoi;
                    txtSDTKhachDat.setText(khMoi.getSoDT());
                    lblTenKhachDat.setText(khMoi.getHoTen());
                });
                st.setScene(new Scene(view));
                st.initOwner(getScene()!=null?getScene().getWindow():null);
                st.initModality(Modality.APPLICATION_MODAL);
                st.setTitle("Thêm khách hàng");
                st.showAndWait();
            }
        });

        btnDatBan.setOnAction(e->wireDatBanHandler());

        btnQuayLai.setOnAction(e->{
            Stage st = (Stage)getScene().getWindow();
            if(st!=null) st.close();
        });
    }
    
    protected void xuLySauKhiLuu(boolean ok, PhieuDatBan pdb, List<Ban> dsBan, boolean isDatNgay) {
        if (!ok) {
            showMessage("Lỗi khi lưu phiếu!", Alert.AlertType.ERROR);
            return;
        }

        showMessage("Đặt bàn thành công!", Alert.AlertType.INFORMATION);

        if (isDatNgay && quanLiBan != null && dsBan != null && !dsBan.isEmpty()) {
            Ban banChinh = dsBan.get(0);

            quanLiBan.hienThiBanTheoTang(banChinh.getTang().getMaTang());
        }

        parentPane.getChildren().remove(this);
    }
    
    protected void showDatBanThanhCong(List<Ban> dsBan) {
        showMessage("Đã đặt bàn thành công!", Alert.AlertType.INFORMATION);

        if(dsBan == null || dsBan.isEmpty()) return;
        try {
            if(quanLiBan != null){
                banDAO.capNhatTrangThaiDanhSach(dsBan, "Đang phục vụ");
                quanLiBan.hienThiBanTheoTang(dsBan.get(0).getTang().getMaTang());
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        if(parentPane != null){
            parentPane.getChildren().remove(this);
        }
        if(btnDatBan != null){
            btnDatBan.setDisable(true);
        }
    }

//     Hiển thị thông báo lỗi.
    protected void showDatBanLoi(String message){
        showMessage(message, Alert.AlertType.ERROR);
    }
    
	 protected boolean checkSoNguoiNotEmpty() {
	     String soNguoiStr = txtSoNguoi.getText();
	     if (soNguoiStr == null || soNguoiStr.isBlank()) {
	         showDatBanLoi("Vui lòng nhập số người!");
	         return false;
	     }
	     return true;
	 }
	
	 protected Integer checkSoNguoiValid() {
	     try {
	         int soNguoi = Integer.parseInt(txtSoNguoi.getText().trim());
	         if (soNguoi <= 0) {
	             showDatBanLoi("Số người phải lớn hơn 0!");
	             return null;
	         }
	         return soNguoi;
	     } catch (NumberFormatException ex) {
	         showDatBanLoi("Số người phải là số hợp lệ!");
	         return null;
	     }
	 }
	
	 protected boolean checkSucChua(int soNguoi) {
	     if (dsBan == null || dsBan.isEmpty()) {
	         showDatBanLoi("Bạn chưa chọn bàn!");
	         return false;
	     }
	
	     int tong = 0;
	     for (Ban b : dsBan) {
	         try { tong += b.getLoaiBan().getSoChoNgoi(); } catch (Exception ignored) {}
	     }
	
	     if (soNguoi > tong) {
	         showDatBanLoi("Số người vượt quá sức chứa (" + tong + ") của bàn đã chọn!");
	         return false;
	     }
	     return true;
	 }
	
	 protected boolean checkSDT() {
	     String sdt = txtSDTKhachDat.getText();
	     if (sdt == null || sdt.isBlank()) {
	         showDatBanLoi("Vui lòng nhập số điện thoại!");
	         return false;
	     }
	     if (!sdt.matches("0\\d{9}")) {
	         showDatBanLoi("SDT không hợp lệ! (phải 10 số, bắt đầu bằng 0)");
	         return false;
	     }
	     return true;
	 }
	
	 protected KhachHang checkKhachHang() {
	     String sdt = txtSDTKhachDat.getText();
	     
	     KhachHang kh = selectedKhachHang != null
	             ? selectedKhachHang
	             : khachHangDAO.layKhachHangTheoSDT(sdt);
	
	     if (kh == null) {
	         showDatBanLoi("Khách hàng chưa tồn tại!");
	         return null;
	     }
	     return kh;
	 }
	
	
	 protected KhachHang validateAllCommon() {
	
	     if (!checkSoNguoiNotEmpty()) return null;
	
	     Integer soNguoi = checkSoNguoiValid();
	     if (soNguoi == null) return null;
	
	     if (!checkSucChua(soNguoi)) return null;
	
	     if (!checkSDT()) return null;
	
	     KhachHang kh = checkKhachHang();
	     if (kh == null) return null;
	
	     return kh;
	 }
	 
	 protected void showMessage(String message, Alert.AlertType type) {
		    Alert alert = new Alert(type);
		    alert.setTitle(type == Alert.AlertType.ERROR ? "Lỗi" : "Thông báo");
		    alert.setHeaderText(null);
		    alert.setContentText(message);

		    if (this.getScene() != null && this.getScene().getWindow() != null) {
		        alert.initOwner(this.getScene().getWindow());
		    }

		    alert.showAndWait();
		}

}