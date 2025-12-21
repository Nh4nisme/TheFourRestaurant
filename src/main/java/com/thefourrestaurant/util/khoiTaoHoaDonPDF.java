package com.thefourrestaurant.util;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.thefourrestaurant.model.*;
import javafx.collections.ObservableList;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class khoiTaoHoaDonPDF {

    public static void inHoaDon(String filePath,
                                String maHD,
                                String ngayNhan,
                                NhanVien nv,
                                KhachHang kh,
                                ObservableList<ChiTietPDB> chiTietList,
                                KhuyenMai khuyenMai,
                                BigDecimal tienCoc,
                                BigDecimal tienKhachDua,
                                BigDecimal tienThua) {
        try {
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font subTitleFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 12);

            DecimalFormat df = new DecimalFormat("#,###");

            // ===== HEADER =====
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new int[]{3, 5});

            // Logo + Tên nhà hàng
            PdfPCell leftCell = new PdfPCell();
            leftCell.setBorder(Rectangle.NO_BORDER);
            Paragraph shopName = new Paragraph("THE FOUR RESTAURANT", titleFont);
            leftCell.addElement(shopName);
            headerTable.addCell(leftCell);

            // Địa chỉ bên phải
            PdfPCell rightCell = new PdfPCell();
            rightCell.setBorder(Rectangle.NO_BORDER);
            Paragraph address = new Paragraph("12 đường Nguyễn Văn Bảo, Phường 1, Gò Vấp, TP. HCM 700000", normalFont);
            address.setAlignment(Element.ALIGN_RIGHT);
            rightCell.addElement(address);
            headerTable.addCell(rightCell);

            document.add(headerTable);
            document.add(new Paragraph(" ")); // khoảng cách

            // ===== THÔNG TIN HÓA ĐƠN =====
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingBefore(10);
            infoTable.setSpacingAfter(10);

            PdfPCell invoiceTitleCell = new PdfPCell(new Paragraph("HÓA ĐƠN THANH TOÁN", subTitleFont));
            invoiceTitleCell.setColspan(2);
            invoiceTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            invoiceTitleCell.setBorder(Rectangle.NO_BORDER);
            infoTable.addCell(invoiceTitleCell);

            infoTable.addCell(new Phrase("Mã hóa đơn: " + maHD, normalFont));
            infoTable.addCell(new Phrase("Ngày lập: " + ngayNhan, normalFont));
            infoTable.addCell(new Phrase("Khách hàng: " + (kh != null ? kh.getHoTen() : "Khách lẻ"), normalFont));
            infoTable.addCell(new Phrase("SĐT: " + (kh != null ? kh.getSoDT() : "-"), normalFont));
            infoTable.addCell(new Phrase("Nhân viên lập: " + (nv != null ? nv.getHoTen() : "-"), normalFont));
            infoTable.addCell(new Phrase(" ", normalFont)); // ô trống để cân bằng table

            document.add(infoTable);

            // ===== BẢNG MÓN =====
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{1, 5, 2, 3});
            table.setSpacingBefore(10);

            table.addCell("STT");
            table.addCell("Tên món");
            table.addCell("Đơn giá");
            table.addCell("Thành tiền");

            int stt = 1;
            BigDecimal tongTienChuaVAT = BigDecimal.ZERO;

            for (ChiTietPDB ct : chiTietList) {
                table.addCell(String.valueOf(stt++));
                table.addCell(ct.getMonAn().getTenMon());
                table.addCell(df.format(ct.getDonGia()) + " đ");
                BigDecimal thanhTien = BigDecimal.valueOf(ct.getDonGia()).multiply(BigDecimal.valueOf(ct.getSoLuong()));
                table.addCell(df.format(thanhTien) + " đ");
                tongTienChuaVAT = tongTienChuaVAT.add(thanhTien);
            }

            document.add(table);

            // ===== THÔNG TIN THANH TOÁN =====
            PdfPTable payTable = new PdfPTable(2);
            payTable.setWidthPercentage(50);
            payTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            payTable.setSpacingBefore(10);

            // Tổng tiền (chưa VAT)
            payTable.addCell("Thành tiền:");
            payTable.addCell(df.format(tongTienChuaVAT) + " đ");

            // Khuyến mãi
            if (khuyenMai != null && khuyenMai.getKhuyenMaiDieuKien() != null) {
                KhuyenMai_DieuKien dk = khuyenMai.getKhuyenMaiDieuKien();
                if (dk.getTyLeGiam() != null) payTable.addCell("Khuyến mãi:");
                else if (dk.getSoTienGiam() != null) payTable.addCell("Khuyến mãi:");
                else payTable.addCell("Khuyến mãi:");
            } else {
                payTable.addCell("Khuyến mãi:");
            }

            // Chiết khấu
            if (khuyenMai != null && khuyenMai.getKhuyenMaiDieuKien() != null) {
                KhuyenMai_DieuKien dk = khuyenMai.getKhuyenMaiDieuKien();
                if (dk.getTyLeGiam() != null) payTable.addCell(dk.getTyLeGiam() + "%");
                else if (dk.getSoTienGiam() != null) payTable.addCell(df.format(dk.getSoTienGiam()) + " đ");
                else payTable.addCell("0 đ");
            } else {
                payTable.addCell("0 đ");
            }

            // VAT
            BigDecimal VAT = tongTienChuaVAT.multiply(BigDecimal.valueOf(0.1));
            payTable.addCell("VAT 10%:");
            payTable.addCell(df.format(VAT) + " đ");

            // Tiền phải thanh toán
            BigDecimal thanhToan = tongTienChuaVAT.add(VAT);
            payTable.addCell("Tổng tiền phải thanh toán:");
            payTable.addCell(df.format(thanhToan) + " đ");

            // Tiền cọc
            payTable.addCell("Tiền cọc:");
            payTable.addCell(df.format(tienCoc != null ? tienCoc : BigDecimal.ZERO) + " đ");

            // Tiền khách đưa
            payTable.addCell("Tiền khách đưa:");
            payTable.addCell(df.format(tienKhachDua != null ? tienKhachDua : BigDecimal.ZERO) + " đ");

            // Tiền dư
            payTable.addCell("Tiền dư:");
            payTable.addCell(df.format(tienThua != null ? tienThua : BigDecimal.ZERO) + " đ");

            document.add(payTable);

            // ===== FOOTER =====
            Paragraph footer = new Paragraph("Cảm ơn quý khách!", normalFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(20);
            document.add(footer);

            document.close();
            System.out.println("In hóa đơn PDF thành công: " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
