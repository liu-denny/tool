package com.denny.DataStory.PDF;

import com.denny.Utils.PathUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

/**
 * @Description
 * @auther denny
 * @create 2020-03-09 18:14
 */
public class JavaToPdfCN {
    private static final String DEST = "D:\\work\\tool\\src\\main\\resources\\PDF\\HelloWorld_CN.pdf";
    private static final String FONT = "C:\\Windows\\Fonts\\SIMHEI.ttf";


    public static void main(String[] args) throws Exception {
        String base64="5ZCN5a2X77ya5pWw6K+05pWF5LqL5ZGo6L65CuWcsOS6p+eUteivne+8muaaguaXoArlnLDlnYDvvJrmmoLml6AK56ef6YeR77ya5pqC5pegCumdouenr++8muaaguaXoArnsbvlnovvvJrlhoXooZfpk7oK54K55L2N6K+E5Lyw5b6X5YiG77yaMjcK";
        String result;
        try {
            byte[] decode = Base64Util.decode(base64);
            result = new String(decode);
        }catch (Exception e){
            throw new Exception(base64+"：格式错误！");
        }



        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(PathUtils.getFile(DEST)));
        document.open();
        Font f1 = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        document.add(new Paragraph(result, f1));


        document.close();
        writer.close();

    }
}
