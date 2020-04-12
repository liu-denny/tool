package com.denny.DataStory.PDF;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Description
 * @auther denny
 * @create 2020-03-10 14:07
 */
public class PDFTest {
    public static void main(String[] args) {
        String imagePath1 = "D:\\work\\tool\\src\\main\\resources\\Html\\4.png";
        String imagePath2 = "https://oss.datastory.com.cn/channel/pdf/幻灯片1.PNG";
        String mOutputPdfFileName = "D:\\work\\tool\\src\\main\\resources\\PDF\\test_8.pdf";

        Document doc = new Document(new RectangleReadOnly(900,595));
//        Document doc = new Document(new RectangleReadOnly(1331,748));

        try {
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(mOutputPdfFileName));
            doc.open();
            doc.newPage();
            Image png1 = Image.getInstance(imagePath2);

            float heigth = png1.getHeight();
            float width = png1.getWidth();
            System.out.println(heigth);
            System.out.println(width);
//            int percent = getPercent2(heigth, width);
//            System.out.println(percent);
            png1.setAlignment(Image.MIDDLE);
//            png1.setAlignment(Image.TEXTWRAP);
            png1.scaleAbsolute(860,540);
//            png1.scaleAbsolute(1300,700);
            //宽度百分比
//            png1.setWidthPercentage(2100);
            //将图像缩放到一定比例
//            png1.scalePercent(percent + 3);
            doc.add(png1);

            doc.newPage();
            png1 = Image.getInstance(imagePath2);
            heigth = png1.getHeight();
            width = png1.getWidth();
//            percent = getPercent2(heigth, width);
            png1.setAlignment(Image.MIDDLE);
            png1.scaleAbsolute(860,540);
//            png1.setAlignment(Image.TEXTWRAP);
//            png1.setWidthPercentage(210);
//            png1.scalePercent(percent + 3);
            doc.add(png1);

            doc.close();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("结束");
    }

    private static int getPercent2(float h, float w) {
        int p = 0;
        float p2 = 0.0f;
        p2 = 530 / w * 100;
        p = Math.round(p2);
        return p;
    }
}
