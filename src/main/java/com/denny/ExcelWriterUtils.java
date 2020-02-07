package com.denny;

import lombok.Data;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Excel工具类
 * @auther denny
 * @create 2019-12-10 14:32
 */
public class ExcelWriterUtils {
    String filepath;
    String sheetName;

    //标题
    private LinkedList<String> title;

    FileOutputStream fos;
    FileInputStream fis;
    Workbook workbook;
    Sheet sheet;

    public ExcelWriterUtils(String filepath) {
        this(filepath,"sheet1");
    }

    public ExcelWriterUtils(String filepath, String sheetName) {
        try{
            this.fos = new FileOutputStream(new File(filepath));
            this.fis = new FileInputStream(filepath);
            this.workbook = this.getWorkbook(filepath);
            this.sheet = workbook.createSheet(sheetName);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
        this.sheet = workbook.getSheet(sheetName);
        if(sheet == null){
            sheet = workbook.createSheet(sheetName);
        }
        title = null;
    }


    private Workbook getWorkbook(String filepath){
        String[] split = filepath.split("\\.");
        Workbook workbook = null;
        if("xls".equals(split[1])){
            workbook = new HSSFWorkbook();
        }else if("xlsx".equals(split[1])){
            workbook = new XSSFWorkbook();
        }else {
            //可以修改成打印日志
            System.out.println("文件类型错误");
        }
        return workbook;
    }

    public void writer(List<Map<String,Object>> mapList){
        int num = sheet.getLastRowNum() +1;

        //map的key作为标题，即Excel的第一行
        if(title == null){
            if(sheet.getLastRowNum() != 0){
                return;
            }
            for (Map<String, Object> map : mapList) {
                Row row = sheet.createRow(0);
                writeHeaderRow(row,map.keySet());
                break;
            }
        }

        //写入数据
        for (Map<String, Object> map : mapList) {
            Row row = sheet.createRow(num);
            writerRow(row,map);
            num++;
        }
    }

    private void writeHeaderRow(Row row,Iterable<?> rowData){
        int i =0;
        Cell cell;
        title = new LinkedList<>();
        for (Object o : rowData) {
            cell = row.createCell(i);
            cell.setCellValue(o.toString());
            title.add(o.toString());
            i++;
        }
    }

    private void writerRow(Row row,Map<String, Object> rowData){
        int i = 0;
        Cell cell;
        for (String s : title) {
            Object value = rowData.get(s);
            cell = row.createCell(i);
            cell.setCellValue(value.toString());
//            writeCell(cell,value);
            i++;
        }
    }

//    private Object writeCell(Cell cell,Object o) {
//        if(o instanceof String){
//            cell.setCellValue(o.toString());
//        }
//        return null;
//    }

    public LinkedList<String> getTitle(){
        return title;
    }

    public void close() throws IOException {
        if (null != this.fos){
            workbook.write(fos);
        }
        fos.close();
        fis.close();

        fos = null;
        fis = null;
    }



}
