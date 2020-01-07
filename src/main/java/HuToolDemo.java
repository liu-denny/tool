import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @Description
 * @auther denny
 * @create 2020-01-06 11:32
 */
public class HuToolDemo {
    public static void main(String[] args) {
        HuToolDemo demo = new HuToolDemo();
        demo.Excel();
//        demo.bigExcel();
    }

    public void Excel(){
        // 通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter("D:\\work\\tool\\src\\main\\resources\\t1.xlsx");

        Map<String, Object> row1 = new LinkedHashMap<>();
        row1.put("姓名", "张三");
        row1.put("年龄", 23);
        row1.put("成绩", 88.32);
        row1.put("是否合格", true);
        row1.put("考试日期", DateUtil.date());
        ArrayList<Map<String, Object>> rows1 = CollUtil.newArrayList(row1);
        writer.write(rows1);

        Map<String, Object> row2 = new LinkedHashMap<>();
        row2.put("姓名", "李四");
        row2.put("年龄", 33);
        row2.put("成绩", 59.50);
        row2.put("是否合格", false);
        row2.put("考试日期", DateUtil.date());

        ArrayList<Map<String, Object>> rows2 = CollUtil.newArrayList(row2);
        writer.setSheet("sheet2");
        writer.write(rows2);


        writer.setSheet("sheet1");
        Map<String, Object> row3 = new LinkedHashMap<>();
        row3.put("姓名", "王五");
        row3.put("年龄", 35);
        row3.put("成绩", 55.50);
        row3.put("是否合格", false);
        row3.put("考试日期", DateUtil.date());
        ArrayList<Map<String, Object>> rows3 = CollUtil.newArrayList(row3);
        writer.write(rows3);

        // 关闭writer，释放内存
        writer.close();

    }

    public void bigExcel(){
//        BigExcelWriter writer = ExcelUtil.getBigWriter("D:\\work\\tool\\src\\main\\resources\\t1.xlsx");
        ExcelWriter writer = ExcelUtil.getWriter("D:\\work\\tool\\src\\main\\resources\\t1.xlsx");
        for (int i = 0; i < 500; i++) {
            LinkedList<Integer> list = new LinkedList<>();
            for (int z = 0; z < 10; z++) {
                for (int j = 0; j < 10; j++) {
                    list.add(i+z+j);
                }
            }
            writer.write(list,false);
        }
        // 关闭writer，释放内存
        writer.close();

//        ExcelUtil.read03BySax()

    }
}
