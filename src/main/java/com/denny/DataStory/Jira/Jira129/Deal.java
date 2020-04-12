package com.denny.DataStory.Jira.Jira129;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @auther denny
 * @create 2020-02-09 20:56
 */
public class Deal {
    public static void main(String[] args) {
        String readerPath = "D:\\work\\tool\\src\\main\\resources\\result2.xlsx";
        String writePath = "D:\\work\\tool\\src\\main\\resources\\result_home.xlsx";

        for(int i=0;i<3;i++){
            ExcelReader reader = ExcelUtil.getReader(readerPath,i);
            List<Map<String,Object>> readAll = reader.readAll();

            List<Map<String, Object>> resultList = new ArrayList<>();
            for (Map<String, Object> map : readAll) {
//                String s = map.get("resident").toString().split("resident: ")[1];
                String s = map.get("resident").toString().split(",")[0];
                s = s.split("home:")[1].trim();

                Map<String, Object> row = new LinkedHashMap<>();
                row.put("具体POI/门店",map.get("name"));
                row.put("范围内小区数",map.get("residenceNum"));
                row.put("同属的栅格",map.get("local"));
                row.put("栅格对应的常驻人数（提供四种类型）",s);
                resultList.add(row);

            }
            ExcelWriter writer = ExcelUtil.getWriter(writePath);
            writer.setSheet(i);
            writer.write(resultList);
            writer.close();
        }
    }
}
