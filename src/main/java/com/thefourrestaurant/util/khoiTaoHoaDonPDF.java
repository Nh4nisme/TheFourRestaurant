package com.thefourrestaurant.util;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.thefourrestaurant.model.*;
import javafx.collections.ObservableList;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class khoiTaoHoaDonPDF {

    public static void inHoaDon(
            String filePath,
            String maHD,
            String ngayNhan,
            NhanVien nv,
            KhachHang kh,
            ObservableList<ChiTietPDB> chiTietList,
            KhuyenMai khuyenMai,
            BigDecimal tienCoc,
            BigDecimal tienKhachDua,
            BigDecimal tienThua
    ) {
        try {
            // Tạo document
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Font

            Font titleFont = new Font(Font.TIMES_ROMAN, 18, Font.BOLD);
            Font subTitleFont = new Font(Font.TIMES_ROMAN, 14, Font.BOLD);
            Font normalFont = new Font(Font.TIMES_ROMAN, 12);
            DecimalFormat df = new DecimalFormat("#,###");

            // ===== HEADER =====
            Paragraph header = new Paragraph("THE FOUR RESTAURANT\n", titleFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

            Paragraph address = new Paragraph(
                    "12 Nguyễn Văn Bảo, Phường 1, Gò Vấp, TP.HCM 700000\n\n",
                    normalFont
            );
            address.setAlignment(Element.ALIGN_CENTER);
            document.add(address);

            // ===== THÔNG TIN HÓA ĐƠN =====
            Paragraph invoiceTitle = new Paragraph("HÓA ĐƠN THANH TOÁN\n\n", subTitleFont);
            invoiceTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(invoiceTitle);

            document.add(new Paragraph("Mã HĐ : " + maHD, normalFont));
            document.add(new Paragraph("Ngày lập : " + ngayNhan, normalFont));
            document.add(new Paragraph(
                    "Khách hàng: " + (kh != null ? kh.getHoTen() : "Khách lẻ"), normalFont
            ));
            document.add(new Paragraph(
                    "SĐT : " + (kh != null ? kh.getSoDT() : "-"), normalFont
            ));
            document.add(new Paragraph(
                    "Nhân viên : " + (nv != null ? nv.getHoTen() : "-") + "\n\n", normalFont
            ));

            // ===== DANH SÁCH MÓN =====
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setWidths(new float[]{1f, 5f, 2f, 3f, 3f}); // STT, Tên món, SL, Đơn giá, Thành tiền

            // Header bảng
            Font headerFont = new Font(Font.COURIER, 12, Font.BOLD);
            table.addCell(new PdfPCell(new Phrase("STT", headerFont)));
            table.addCell(new PdfPCell(new Phrase("Tên món", headerFont)));
            table.addCell(new PdfPCell(new Phrase("SL", headerFont)));
            table.addCell(new PdfPCell(new Phrase("Đơn giá", headerFont)));
            table.addCell(new PdfPCell(new Phrase("Thành tiền", headerFont)));

            // Nội dung món ăn
            BigDecimal tongTienChuaVAT = BigDecimal.ZERO;
            int stt = 1;
            for (ChiTietPDB ct : chiTietList) {
                BigDecimal thanhTien = BigDecimal.valueOf(ct.getDonGia())
                        .multiply(BigDecimal.valueOf(ct.getSoLuong()));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(stt++), normalFont)));
                table.addCell(new PdfPCell(new Phrase(ct.getMonAn().getTenMon(), normalFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(ct.getSoLuong()), normalFont)));
                table.addCell(new PdfPCell(new Phrase(df.format(ct.getDonGia()), normalFont)));
                table.addCell(new PdfPCell(new Phrase(df.format(thanhTien), normalFont)));
                tongTienChuaVAT = tongTienChuaVAT.add(thanhTien);
            }
            document.add(table);
            document.add(new Paragraph("\n"));

            // ===== TÍNH TOÁN =====
            BigDecimal VAT = tongTienChuaVAT.multiply(BigDecimal.valueOf(0.1));
            BigDecimal thanhToan = tongTienChuaVAT.add(VAT);

            // Khuyến mãi
            String khuyenMaiStr = "0 đ";
            if (khuyenMai != null && khuyenMai.getKhuyenMaiDieuKien() != null) {
                KhuyenMai_DieuKien dk = khuyenMai.getKhuyenMaiDieuKien();
                if (dk.getTyLeGiam() != null) {
                    khuyenMaiStr = dk.getTyLeGiam() + "%";
                } else if (dk.getSoTienGiam() != null) {
                    khuyenMaiStr = df.format(dk.getSoTienGiam()) + " đ";
                }
            }

            document.add(new Paragraph("------------------------------------------------------------------------", normalFont));

            // ===== THÔNG TIN THANH TOÁN =====
            document.add(new Paragraph(
                    String.format("%-25s : %12s", "Khuyến mãi", khuyenMaiStr), normalFont
            ));
            document.add(new Paragraph(
                    String.format("%-25s : %12s đ", "VAT 10%", df.format(VAT)), normalFont
            ));
            document.add(new Paragraph(
                    String.format("%-25s : %12s đ", "Tổng tiền phải thanh toán", df.format(thanhToan)), normalFont
            ));
            document.add(new Paragraph(
                    String.format("%-25s : %12s đ", "Tiền cọc", tienCoc != null ? df.format(tienCoc) : "0"), normalFont
            ));
            document.add(new Paragraph(
                    String.format("%-25s : %12s đ", "Tiền khách đưa", tienKhachDua != null ? df.format(tienKhachDua) : "0"), normalFont
            ));
            document.add(new Paragraph(
                    String.format("%-25s : %12s đ", "Tiền dư", tienThua != null ? df.format(tienThua) : "0"), normalFont
            ));

            document.add(new Paragraph("------------------------------------------------------------------------", normalFont));

            // ===== CHỮ KÝ =====
            PdfPTable signatureTable = new PdfPTable(2);
            signatureTable.setWidthPercentage(100);
            signatureTable.setSpacingBefore(30f);
            signatureTable.setWidths(new float[]{2.5f, 1f});

            PdfPCell buyerCell = new PdfPCell();
            buyerCell.setBorder(Rectangle.NO_BORDER);
            buyerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            buyerCell.addElement(new Paragraph("Người mua", normalFont));
            buyerCell.addElement(new Paragraph("(Ký và ghi rõ họ tên)", normalFont));
            buyerCell.addElement(new Paragraph("\n\n\n", normalFont));
            signatureTable.addCell(buyerCell);

            PdfPCell sellerCell = new PdfPCell();
            sellerCell.setBorder(Rectangle.NO_BORDER);
            sellerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            sellerCell.addElement(new Paragraph("Người bán", normalFont));
            sellerCell.addElement(new Paragraph("(Ký và ghi rõ họ tên)", normalFont));
            signatureTable.addCell(sellerCell);

            document.add(signatureTable);

            document.add(new Paragraph("------------------------------------------------------------------------", normalFont));

            // ===== FOOTER =====
            Paragraph footer = new Paragraph("\nCảm ơn quý khách!", normalFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            // ===== LỜI DẶN DÒ / THÂN THIỆN =====
            Paragraph note = new Paragraph(
                    "\nChúc quý khách một ngày vui vẻ! \n" +
                            "Hãy giữ hóa đơn này để thuận tiện cho các giao dịch sau.\n" +
                            "Mong được phục vụ quý khách lần sau!",
                    normalFont
            );
            note.setAlignment(Element.ALIGN_CENTER);
            document.add(note);

            document.close();
            System.out.println("In hóa đơn PDF thành công: " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}