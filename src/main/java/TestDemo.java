import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelReader;
import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
        TestDemo testDemo = new TestDemo();
//        testDemo.TestExcelReader();
        testDemo.TestExcelWrite();
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
}
