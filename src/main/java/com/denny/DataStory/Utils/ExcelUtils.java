package com.denny.DataStory.Utils;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @auther denny
 * @create 2020-02-26 13:56
 */
public class ExcelUtils {

    private static ExcelReader reader;
    private static ExcelWriter writer;

    public static List<Map<String,Object>> readAll(String path){
        return readAll(path,0);
    }

    public static List<Map<String,Object>> readAll(String path,Integer sheetIndex){
        if(path == null){
            return null;
        }
        if(sheetIndex == null){
            sheetIndex = 0;
        }
        reader = ExcelUtil.getReader(path,sheetIndex);
        List<Map<String,Object>> readAll = reader.readAll();
        return readAll;
    }

    //写入0sheet
    public static void writer(String path,List<Map<String, Object>> resultList){
        if(path == null){
            throw new RuntimeException("路径有误");
        }
        writer = ExcelUtil.getWriter(path);
        writer.write(resultList);
    }

    public static void close(){
        if(reader != null){
            reader.close();
        }
        if(writer != null){
            writer.close();
        }
    }

}
