package com.denny.DataStory.Jira.Jira476;

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
 * @create 2020-05-21 11:44
 */
public class Main {
    private static final Type T_MAP_STR_OBJ = new TypeToken<Map<String, Object>>() {}.getType();
    private static final Gson gson = new Gson();

    public static void main(String[] args) throws Exception {
        String url = "https://restapi.amap.com/v3/place/text?parameters";
        List<String> keywords = Arrays.asList("百果园","菜当家","钱大妈");
        String city = "广州";
        String key = "4f0605cae114f3c2fb0d326951d94477";
        int count = 0;
        Main main = new Main();

        for (String keyword : keywords) {
            String path = "D:\\" + keyword + ".xlsx";
            ExcelWriter writer = ExcelUtil.getWriter(path);
            List<Map<String,Object>> resultList = new ArrayList<>();
            System.out.println(keyword + "开始");
            Map<String, String> params = new HashMap<>();
            params.put("key",key);
            params.put("keywords",keyword);
            params.put("city",city);
            params.put("offset","20");
            params.put("page","1");
            params.put("extensions","all");

            String result = HttpClientUtils.doGet(url,params);

            if (result != null) {
                Map<String, Object> map = gson.fromJson(result, T_MAP_STR_OBJ);
                count = MapUtils.getInteger(map,"count");

                List<Map<String,Object>> pois = (List<Map<String, Object>>) map.get("pois");
                //处理poi数据
                List<Map<String,Object>> resultListPoi = main.dealPOiData1(pois);

                resultList.addAll(resultListPoi);
                //翻页遍历
                int pageNum = count/20+1;
                for(int i =2;i<=pageNum;i++){
                    params.put("page",String.valueOf(i));
                    result = HttpClientUtils.doGet(url,params);
                    map = gson.fromJson(result, T_MAP_STR_OBJ);
                    pois = (List<Map<String, Object>>) map.get("pois");
                    resultListPoi = main.dealPOiData1(pois);
                    resultList.addAll(resultListPoi);
                }
                writer.write(resultList);
                writer.close();
                System.out.println(keyword + "结束");
            }

        }
    }

    public List<Map<String,Object>> dealPOiData(List<Map<String,Object>> pois){
        List<Map<String,Object>> resultList = new ArrayList<>();
        for (Map<String, Object> map : pois) {
            Map<String,Object> result = new LinkedHashMap<>();
            for(Map.Entry<String,Object> entry : map.entrySet()){
                if(entry.getValue() instanceof String) {
                    result.put(entry.getKey(), entry.getValue());
                }else  {
//                    if(entry.getKey().equals("indoor_data")){
//                        Map<String,Object> indoorDataMap = (Map<String, Object>) map.get(entry.getKey());
//                        for(Map.Entry<String,Object> inEntry : indoorDataMap.entrySet()){
//                            if(inEntry.getValue() instanceof ArrayList){
//                                result.put("indoor_data" + "_" + inEntry.getKey(),null);
//                            }else {
//                                result.put("indoor_data" + "_" + inEntry.getKey(),inEntry.getValue());
//                            }
//                        }
//                    }else if(entry.getKey().equals("biz_ext")){
//                        Map<String,Object> bizExtMap = (Map<String, Object>) map.get(entry.getKey());
//                        result.put("biz_ext" + "_" + "rating",bizExtMap.get("rating"));
//                        result.put("biz_ext" + "_" + "cost",bizExtMap.get("cost"));
//                        result.put("biz_ext" + "_" + "meal_ordering",bizExtMap.get("meal_ordering"));
//                    }else if(entry.getKey().equals("photos")){
//                        List<Map<String,Object>> photos = (List<Map<String, Object>>) map.get(entry.getKey());
//                        if(photos == null || photos.size() == 0){
//                            for(int i=0;i<4;i++){
//                                result.put("photos" + "_"+ "title" + "_" + (i+1),null);
//                                result.put("photos" + "_"+ "url" + "_" + (i+1),null);
//                            }
//                        }else {
//                            for(int i=0;i<photos.size();i++){
//                                Map<String,Object> photo = photos.get(i);
//                                result.put("photos" + "_"+ "title" + "_" + (i+1),photo.get("title"));
//                                result.put("photos" + "_"+ "url" + "_" + (i+1),photo.get("url"));
//                            }
//                        }
//
//                    }else {
//                        List<String> list = (List<String>) map.get(entry.getKey());
//                        if(list.isEmpty()){
//                            result.put(entry.getKey(),null);
//                        }else {
//                            result.put(entry.getKey(),String.join("-",list));
//                        }
//                    }
                    result.put(entry.getKey(),gson.toJson(entry.getValue()));
                }
            }
            resultList.add(result);
        }
        return resultList;
    }

    public List<Map<String,Object>> dealPOiData1(List<Map<String,Object>> pois) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Map<String, Object> map : pois) {
            Map<String,Object> result = new LinkedHashMap<>();
            result.put("name",map.get("name"));
            result.put("type",map.get("type"));
            result.put("biz_type",map.get("biz_type"));
            result.put("address",map.get("address"));
            result.put("location",map.get("location"));
            result.put("tel",map.get("tel"));
            result.put("website",map.get("website") == null ? null:gson.toJson(map.get("website")));
            result.put("email",map.get("email") == null ? null:gson.toJson(map.get("email")));
            result.put("pname",map.get("pname"));
            result.put("cityname",map.get("cityname"));
            result.put("adname",map.get("adname"));
            result.put("business_area",map.get("business_area"));
            result.put("tag",map.get("tag"));
            Map<String,Object> indoorDataMap = (Map<String, Object>) map.get("indoor_data");
            result.put("indoor_data_cpid", null);
            result.put("indoor_data_floor", null);
            result.put("indoor_data_truefloor", null);
            Map<String,Object> bizExtMap = (Map<String, Object>) map.get("biz_ext");
            result.put("biz_ext" + "_" + "rating",bizExtMap.get("rating"));
            result.put("biz_ext" + "_" + "cost",bizExtMap.get("cost"));
            result.put("biz_ext" + "_" + "meal_ordering",bizExtMap.get("meal_ordering"));
            List<Map<String,Object>> photos = (List<Map<String, Object>>) map.get("photos");
            for(int i=1;i<4;i++){
                if(i > photos.size()){
                    result.put("photos" + "_"+ "title" + "_" + i,null);
                    result.put("photos" + "_"+ "url" + "_" + i,null);
                }else {
                    Map<String,Object> photoMap = photos.get(i-1);
                    result.put("photos" + "_"+ "title" + "_" + i,photoMap.get("title"));
                    result.put("photos" + "_"+ "url" + "_" + i,photoMap.get("url"));
                }
            }
            resultList.add(result);
//
        }
        return resultList;
    }

}
