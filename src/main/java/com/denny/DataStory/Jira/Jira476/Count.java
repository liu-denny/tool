package com.denny.DataStory.Jira.Jira476;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.denny.Utils.HttpClientUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.collections4.MapUtils;

import java.lang.reflect.Type;
import java.util.*;

/**
 * @Description
 * @auther denny
 * @create 2020-05-28 14:31
 */
public class Count {

    private static final Type T_MAP_STR_OBJ = new TypeToken<Map<String, Object>>() {}.getType();
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        String url = "https://restapi.amap.com/v3/place/text?parameters";

        ExcelReader reader = ExcelUtil.getReader("C:\\Users\\denny\\Desktop\\city.xlsx");
        List<Map<String,Object>> citys = reader.readAll();

        reader = ExcelUtil.getReader("C:\\Users\\denny\\Desktop\\key.xlsx");
        List<Map<String,Object>> keywords = reader.readAll();

        List<String> keys = Arrays.asList("d18a34c778905fc2a623d6f0b25fb1e5","4f0605cae114f3c2fb0d326951d94477","7eb8383c0ecaaefec045498f041d1bf1","13e61d04a6973f8c3c49400f156c8e19","f1e3071a36a2e738dd5ce0b8beea301f","415379e0d5bfd08fc612e46c5c32a0fa","a28505fdaad399fab6023a1026aef035");
        Map<String, String> params = new HashMap<>();
        params.put("offset","20");
        params.put("page","1");
        params.put("extensions","all");
//        int i =0;
        System.out.println("start");
        List<Map<String,Object>> resultList = new LinkedList<>();
        for (Map<String, Object> keyword : keywords) {
            String sKeyword = MapUtils.getString(keyword,"keywords");
            String sType = MapUtils.getString(keyword,"type");
            System.out.println(sKeyword);
            Map<String,Object> countMap = new LinkedHashMap<>();
            countMap.put("type",sType);
            for (Map<String, Object> city : citys) {
                String sCity = MapUtils.getString(city, "city");
//                sCity = sCity.trim();
                String key = keys.get(0);
//                if (i < 1599) {
//                    key = keys.get(0);
//                } else if (i < 3199) {
//                    key = keys.get(1);
//                } else if (i < 4699) {
//                    key = keys.get(2);
//                } else if (i < 6099) {
//                    key = keys.get(3);
//                } else if (i < 7599) {
//                    key = keys.get(4);
//                } else if (i < 9099) {
//                    key = keys.get(5);
//                } else {
//                    key = keys.get(6);
//                }

                params.put("key", key);
                params.put("keywords", sKeyword.trim());
                params.put("type", sType.trim());
                params.put("city", sCity);
                try {
                    String result = HttpClientUtils.doGet(url, params);
                    int count = 0;
                    if (result != null) {
                        Map<String, Object> map = gson.fromJson(result, T_MAP_STR_OBJ);
                        count = MapUtils.getInteger(map, "count");
                    }
                    countMap.put(sCity, count);
//                    i++;
                    Thread.sleep(300);
                } catch (Exception e) {
                    countMap.put(sCity, 0);
                }
            }

            resultList.add(countMap);
        }

        ExcelWriter writer = ExcelUtil.getWriter("C:\\Users\\denny\\Desktop\\result.xlsx");
        writer.write(resultList);
        writer.close();
        System.out.println("end");
    }
}
