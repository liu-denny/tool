package com.denny.DataStory.PDF;

import com.denny.Utils.PathUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @Description
 * @auther denny
 * @create 2020-03-09 18:29
 */
public class JavaToPdfHtml {
    private static final String DEST = "D:\\work\\tool\\src\\main\\resources\\PDF\\HelloWorld_CN_HTML_1.pdf";
    private static final String HTML = "D:\\work\\tool\\src\\main\\resources\\Html\\template.html";
    private static final String FONT = "https://oss.datastory.com.cn/channel/SIMHEI.ttf";


    public static void main(String[] args) throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(PathUtils.getFile(DEST)));
        // step 3
        document.open();
        // step 4
        XMLWorkerFontProvider fontImp = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
        fontImp.register(FONT);
        XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                new FileInputStream(HTML), null, Charset.forName("UTF-8"), fontImp);
        // step 5
        document.close();
    }
}
