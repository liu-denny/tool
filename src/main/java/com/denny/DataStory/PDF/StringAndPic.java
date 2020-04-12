package com.denny.DataStory.PDF;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

/**
 * @Description
 * @auther denny
 * @create 2020-03-25 14:19
 */
public class StringAndPic {
    private static final String FONT = "https://oss.datastory.com.cn/channel/SIMHEI.ttf";

    public static void main(String[] args) throws Exception {
        String imagePath1 = "D:\\work\\tool\\src\\main\\resources\\Html\\4.png";
        String mOutputPdfFileName = "D:\\work\\tool\\src\\main\\resources\\PDF\\test_9.pdf";
        Document doc = new Document(new RectangleReadOnly(900,595));
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(mOutputPdfFileName));
        doc.open();
//        doc.newPage();
//        Image png1 = Image.getInstance(imagePath1);
//        png1.setAlignment(Image.MIDDLE);
//        png1.scaleAbsolute(860,540);
//        doc.add(png1);

        String base64="5ZCN5a2X77ya5pWw6K+05pWF5LqL5ZGo6L65CuWcsOS6p+eUteivne+8muaaguaXoArlnLDlnYDvvJrmmoLml6AK56ef6YeR77ya5pqC5pegCumdouenr++8muaaguaXoArnsbvlnovvvJrlhoXooZfpk7oK54K55L2N6K+E5Lyw5b6X5YiG77yaMjcK";
        String result;
        try {
            byte[] decode = Base64Util.decode(base64);
            result = new String(decode);
        }catch (Exception e){
            throw new Exception(base64+"：格式错误！");
        }
        Font f1 = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        doc.add(new Paragraph(result,f1));
        Image png1 = Image.getInstance(imagePath1);
        png1.setAlignment(Image.MIDDLE);
        png1.scaleAbsolute(860,540);
        doc.add(png1);
        doc.close();
        writer.close();
    }
}
