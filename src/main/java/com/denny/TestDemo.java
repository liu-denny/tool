package com.denny;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.denny.Test.T;
import com.denny.Utils.ExcelReader;
import com.denny.Utils.ExcelReaderUtils;
import com.denny.Utils.ExcelWriterUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description
 * @auther denny
 * @create 2020-01-02 16:59
 */
public class TestDemo {
    public static void main(String[] args) throws IOException {
//        ExcelWriteUtils excelUtils = new ExcelWriteUtils();
//        String path = "D:\\tool\\src\\main\\resources\\" + "test.xlsx";
//        String[] title = new String[]{"t11","t22"};
////        excelUtils.createExcel(path,"Sheet1",title);
//
//        String[][] values = new String[2][2];
//        values[0][0] = "t101";
//        values[0][1] = "t111";
//        values[1][0] = "t201";
//        values[1][1] = "t211";
//
////        excelUtils.appendToExcel(path,"Sheet1",values);
//
//        int num = excelUtils.createSheet(path,"Sheet2",title,values);
//        values[0][0] = "t1012";
//        values[0][1] = "t1112";
//        values[1][0] = "t2012";
//        values[1][1] = "t2112";
//        excelUtils.appendToExcel(path,"Sheet2",values,num);
//        TestDemo testDemo = new TestDemo();
//        testDemo.TestExcelReader();
//        testDemo.TestExcelWrite();
//        testDemo.TestExcelReaderV1();
//        List<String> portraitCrowd = Arrays.asList("点位基础信息|评估得分","点位基础信息|常驻人口");
//        System.out.println(portraitCrowd.contains("点位基础信息|评估得分11"));
//        cn.hutool.poi.excel.ExcelReader reader = ExcelUtil.getReader("C:\\Users\\20190327R2\\Desktop\\aa.xlsx",0);
//        List<Map<String,Object>> readAll = reader.readAll();
//        TestDemo testDemo = new TestDemo();
//        System.out.println(testDemo.A());
//        String s = "22.798281288568, 113.57383699997051&22.798281288568, 113.57383699997051";
//        List<String> l = Arrays.asList(s.split("&"));
//        List<List<String>> r = new ArrayList<>();
//        l.forEach(i -> r.add(Arrays.asList(i)));
//        r.forEach(item -> System.out.println(item.toString()));

//        String s = "(2,t_stat_resident_profile_resident,值,大于,0,0&(3,t_stat_resident_profile_resident,值,大于,0,0&33,t_stat_resident_profile_resident,值,大于,0,0)|4,t_stat_resident_profile_work,值,大于,0,0)&5,price_mid_low,TGI,大于,100,0";
//        String s = "333333(2,t_stat_resident_profile_resident,值,大于,0,0&(2,t_stat_resident_profile_resident,值,大于,0,0|2,t_stat_resident_profile_work,值,大于,0,0)&4,price_mid_low,TGI,大于,100,0)|1111";
//        String s = "(2,t_stat_resident_profile_resident,值,大于,0,0&(2,t_stat_resident_profile_resident,值,大于,0,0|2,t_stat_resident_profile_work,值,大于,0,0))&4,price_mid_low,TGI,大于,100,0";
//        TestDemo testDemo = new TestDemo();
//        Map<String,List<Object>> map = testDemo.t(s);
//        System.out.println(map);
        List<String> sList = new LinkedList<>();
        List<String> list = new LinkedList<>();
        int start = 0;
        int startFlag = 0;
        int endFlag = 0;
        int end = 0;
        String s = "(1111(222)33)";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                startFlag++;
                if (startFlag == endFlag + 1) {
                    start = i;
                }
            } else if (s.charAt(i) == ')') {
                endFlag++;
                if (endFlag == startFlag) {
                    if(start > 1){
                        if(start - (end-1) != 1){
                            list.add(s.substring(end,start-1));
                            sList.add(String.valueOf(s.charAt(start-1)));
                        }

                    }
                    list.add(s.substring(start + 1, i));
                    if(i < s.length()-1){
                        sList.add(String.valueOf(s.charAt(i+1)));
                    }
                    end = i+2;
                    if(i+2 < s.length()){
                        String a = s.substring(i+2, s.length());
                        if(!a.contains("(")){
                            list.add(a);
                        }
                    }


                }
            }
        }
        list.forEach(i -> System.out.println(i));
        sList.forEach(i -> System.out.printf(i));
    }

    public Map<String,List<Object>> t(String s){
        List<Object> sList = new LinkedList<>();
        List<Object> list = new LinkedList<>();
        int start = 0;
        int startFlag = 0;
        int endFlag = 0;
        int end = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                startFlag++;
                if (startFlag == endFlag + 1) {
                    start = i;
                }
            } else if (s.charAt(i) == ')') {
                endFlag++;
                if (endFlag == startFlag) {
                    if(start > 1){
                        if(start - (end-1) != 1){
                            list.add(s.substring(end,start-1));
                            sList.add(String.valueOf(s.charAt(start-1)));
                        }

                    }
                    String aa = s.substring(start + 1, i);
                    if(aa.contains("(") && aa.contains(")")){
                        Map<String,List<Object>> map = t(aa);
                        list.add(map);
                    }
                    list.add(s.substring(start + 1, i));
                    if(i < s.length()-1){
                        sList.add(String.valueOf(s.charAt(i+1)));
                    }
                    end = i+2;
                    if(i+2 < s.length()){
                        String a = s.substring(i+2, s.length());
                        if(!a.contains("(")){
                            list.add(a);
                        }
                    }


                }
            }
        }
        Map<String,List<Object>> map = new HashMap<>();
        map.put("result",list);
        map.put("relationship",sList);
        return map;
    }

    public String A(){
        String fileName = this.getClass().getClassLoader().getResource("dataSort.txt").getPath();//获取文件路径  
        return fileName;
    }

    public void TestExcelReader(){
        File file = new File("D:\\work\\tool\\src\\main\\resources\\test.xlsx");
        ExcelReaderUtils excelReaderUtils = new ExcelReaderUtils(file,0,100);
        excelReaderUtils.setSkipSize(1);
        List<Map<String, Object>> values =  excelReaderUtils.getLines();
        for (Map<String, Object> value : values) {
            System.out.println(value.get("t1"));
        }

    }

    public void TestExcelWrite() throws IOException {
        ExcelWriterUtils writerUtils = new ExcelWriterUtils("D:\\work\\tool\\src\\main\\resources\\test1.xlsx");
        Map<String, Object> row1 = new LinkedHashMap<>();
        row1.put("姓名", "张三");
        row1.put("年龄", 23);
        row1.put("成绩", 88.32);
        row1.put("考试日期", DateUtil.date());

        Map<String, Object> row2 = new LinkedHashMap<>();
        row2.put("姓名", "李四");
        row2.put("年龄", 33);
        row2.put("成绩", 59.50);
        row2.put("考试日期", DateUtil.date());

        ArrayList<Map<String, Object>> rows = CollUtil.newArrayList(row1,row2);
        writerUtils.writer(rows);

        Map<String, Object> row3 = new LinkedHashMap<>();
        row3.put("姓名", "王五");
        row3.put("年龄", 35);
        row3.put("成绩", 55.50);
        row3.put("考试日期", DateUtil.date());
        ArrayList<Map<String, Object>> rows3 = CollUtil.newArrayList(row3);
        writerUtils.writer(rows3);

        writerUtils.close();
    }


    public void TestExcelReaderV1() throws IOException {

        ExcelReader excelReader = new ExcelReader("D:\\work\\tool\\src\\main\\resources\\test.xlsx",0);
        LinkedList<Map<String,Object>> linkedList = excelReader.getAllData();
        for (Map<String, Object> map : linkedList) {
            for(Map.Entry<String, Object> entry : map.entrySet()){
                System.out.println(entry.getKey() + ":" + entry.getValue());
            }
        }


    }

}
