package com.denny.Utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @Description
 * @auther denny
 * @create 2020-01-03 15:41
 */
public class ExcelReader {
    private static DecimalFormat df = new DecimalFormat("0");

    private String filepath;
    private int sheetNum;
    private FileInputStream inputStream;
    private Sheet sheet;
    private LinkedList<String> title;

    public ExcelReader(String filepath, int sheetNum) throws IOException {
        this.filepath = filepath;
        this.sheetNum = sheetNum;

        File file = new File(filepath);
        if(!file.isFile() || !file.exists()){
            return;
        }
        try{
            this.inputStream = new FileInputStream(file);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Workbook workbook = this.getWorkbook(filepath);
        this.sheet = workbook.getSheetAt(sheetNum);
    }

    private Workbook getWorkbook(String filepath) throws IOException {
        String[] split = filepath.split("\\.");
        Workbook workbook = null;
        if("xls".equals(split[1])){
            workbook = new HSSFWorkbook(inputStream);
        }else if("xlsx".equals(split[1])){
            workbook = new XSSFWorkbook(inputStream);
        }else {
            //可以修改成打印日志
            System.out.println("文件类型错误");
        }
        return workbook;
    }

    public LinkedList<Map<String,Object>> getAllData(){
        if(title == null){
            this.addTitle();
        }
        LinkedList<Map<String,Object>> linkedList = new LinkedList<>();
        int n = sheet.getLastRowNum();
        String value ;
        for(int i =1;i<=n;i++){
            Row row = sheet.getRow(i);
            Map<String,Object> resultMap = new HashMap<>();
            if(row != null){
                int num = 0;
                for(int cellNum = row.getFirstCellNum();cellNum<row.getLastCellNum();cellNum++){
                    Cell cell = row.getCell(cellNum);
                    if(cell != null){
                        value = String.valueOf(getValue(cell));
                    }else {
                        value = "";
                    }
                    resultMap.put(title.get(num),value);
                    num++;
                }
            }
            linkedList.add(resultMap);
        }
        return linkedList;
    }

    private void addTitle(){
        if(title == null){
            title = new LinkedList<>();
            Row row = sheet.getRow(0);
            for(int i = row.getFirstCellNum();i<row.getLastCellNum();i++){
                title.add(String.valueOf(getValue(row.getCell(i))));
            }
        }
    }


    public LinkedList<String> getTitle() {
        if(title == null){
            this.addTitle();
        }
        return title;
    }

    /**
     * NUMERIC　　数值型　　
     * STRING　　 字符串型　
     * FORMULA　　公式型
     * BLANK　　  空值　　　
     * BOOLEAN　　布尔型　　
     * @param cell
     * @return
     */
    private Object getValue(Cell cell){
        Object value = null;
        if(cell == null){
            return value;
        }else {
            String style = cell.getCellStyle().getDataFormatString();
            switch (cell.getCellType()) {
                case NUMERIC:
                    double numeric = cell.getNumericCellValue();
                    if("@".equals(style)){
                        value =  df.format(numeric);
                    } else if ("General".equals(style)) {
                        value = numeric;
                    } else if("0.00%".equals(style)) {
                        value = numeric;
                    } else {
                        value = numeric;
                    }
                    break;
                case STRING:
                    value = cell.getStringCellValue();
                    break;
                case FORMULA:
                    value = cell.toString();
                    break;
                case BLANK:
                    value = "";
                    break;
                case BOOLEAN:
                    value = cell.getBooleanCellValue();
            }
            return value;
        }
    }
}
