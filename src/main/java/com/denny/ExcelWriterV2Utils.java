package com.denny;

import lombok.Data;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Excel工具类
 * @auther denny
 * @create 2019-12-10 14:32
 */
@Data
public class ExcelWriterV2Utils {
    private static int num;

    public ExcelWriterV2Utils(int num) {
        this.num = num;
    }

    public ExcelWriterV2Utils() {
        this.num = 0;
    }

    public static void createExcel(String fileName,String sheetName,String[] title) {
        createExcel(fileName,sheetName,title,null);
    }

    public static void createExcel(String fileName, String sheetName, List<String> titleList) {
        String[] title = new String[titleList.size()];
        for(int i=0;i<titleList.size();i++){
            title[i] = titleList.get(i);
        }
        createExcel(fileName,sheetName,title,null);
    }


    /**
     * 创建文件，写入
     */
    public static void createExcel(String fileName,String sheetName,String[] title,Object[][] values) {
        if (num == 0) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(new File(fileName)); // 若文件不存在则自动创建
                XSSFWorkbook workbook = new XSSFWorkbook();

                XSSFSheet sheet = workbook.getSheet(sheetName);
                if(sheet == null){
                    sheet = workbook.createSheet(sheetName);
                }
                XSSFRow row = sheet.createRow(num);  // 创建行，第一行

                //写入标题
                if(title != null){
                    for(int i=0;i<title.length;i++){
                        XSSFCell cell = row.createCell(i);
                        cell.setCellValue(title[i]);
                    }
                }else {
                    num = num -1;
                }

                //写入数据
                if(values != null){
                    for(int i=0;i<values.length;i++) {
                        num++;
                        XSSFRow row1 = sheet.createRow(num);
                        for (int j = 0; j < values[i].length; j++) {
                            //将内容按顺序赋给对应的列对象
                            String v = String.valueOf(values[i][j]).equals("null")? " ":String.valueOf(values[i][j]);
                            row1.createCell(j).setCellValue(v);

                        }

                    }
                }
                workbook.write(fos);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    //ignore
                }
            }
        }
    }


    /**
     * 文件存在，创建sheet
     * @param fileName
     * @param sheetName
     * @param title
     * @param values
     * @return rownum行数，为了追加写入
     */
    public static int createSheet(String fileName,String sheetName,String[] title,Object[][] values) {
        FileInputStream fis;
        FileOutputStream fos = null;
        int rownum = 0;
        try{
            fis = new FileInputStream(fileName);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheet(sheetName);
            if(sheet == null){
                sheet = workbook.createSheet(sheetName);
            }
            XSSFRow row = sheet.createRow(rownum);
            if(title != null){
                for(int i=0;i<title.length;i++){
                    XSSFCell cell = row.createCell(i);
                    cell.setCellValue(title[i]);
                }
            }else {
                rownum = rownum -1;
            }

            //写入数据
            if(values != null){
                for(int i=0;i<values.length;i++) {
                    rownum++;
                    XSSFRow row1 = sheet.createRow(rownum);
                    for (int j = 0; j < values[i].length; j++) {
                        //将内容按顺序赋给对应的列对象
                        String v = String.valueOf(values[i][j]).equals("null")? " ":String.valueOf(values[i][j]);
                        row1.createCell(j).setCellValue(v);
                    }
                }
            }
            fos = new FileOutputStream(fileName);
            workbook.write(fos);
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                //ignore
            }
        }
        return rownum;
    }

    /**
     * 追加写入
     */
    public static Integer appendToExcel(String fileName,String sheetName,String[][] values) {
        return appendToExcel(fileName,sheetName,values,num);
    }

    /**
     * 追加写入
     */
    public static Integer appendToExcel(String fileName,String sheetName,String[][] values,Integer rownum) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(fileName);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);

            XSSFSheet sheet = workbook.getSheet(sheetName);
            if(sheet == null){
                sheet = workbook.createSheet(sheetName);
            }
            if (sheet != null) {
                for(int i=0;i<values.length;i++) {
                    rownum++;
                    XSSFRow row = sheet.createRow(rownum);
                    if (values[i] != null) {
                        for (int j = 0; j < values[i].length; j++) {
                            //将内容按顺序赋给对应的列对象
                            row.createCell(j).setCellValue(values[i][j]);
                        }
                    } else {
                        continue;
                    }
                }

            }
            fos = new FileOutputStream(fileName);
            workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                //ignore
            }
        }
        return rownum;
    }
}
