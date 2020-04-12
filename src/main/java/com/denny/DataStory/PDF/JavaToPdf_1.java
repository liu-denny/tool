package com.denny.DataStory.PDF;

import com.denny.Utils.PathUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 最基础例子
 * @Description
 * @auther denny
 * @create 2020-03-09 17:16
 */
public class JavaToPdf_1 {
    private static final String DEST = "D:\\work\\tool\\src\\main\\resources\\PDF\\HelloWorld.pdf";

    public static void main(String[] args) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(PathUtils.getFile(DEST)));
        document.open();
        document.add(new Paragraph("hello world"));
        document.close();
        writer.close();
    }
}
