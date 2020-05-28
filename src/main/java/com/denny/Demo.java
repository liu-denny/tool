package com.denny;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.denny.DataStory.Jira.Jira476.Main;
import com.denny.Utils.ExcelWriterUtils;
import com.denny.Utils.HttpClientUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.collections4.MapUtils;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description
 * @auther denny
 * @create 2020-05-21 16:29
 */
public class Demo {
    public static void main(String[] args) throws ParseException {
        String data = "2020/5/21";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date date = sdf.parse(data);
        System.out.println(date.getTime());
    }
}
