package com.denny.Utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description
 * @auther denny
 * @create 2020-03-04 16:42
 */
public class TLBSUtils {
    private static String getAdcodeUrl = "https://apis.map.qq.com/ws/geocoder/v1/";
    private static String residentNumUrl = "https://apis.map.qq.com/bigdata/popinsight/v1/residentnum";
    private static String residentUrl = "https://apis.map.qq.com/bigdata/popinsight/v1/residentuserprofile";
    private static String flowNumUrl = "https://apis.map.qq.com/bigdata/popinsight/v1/flownum";
    private static String flowUrl = "https://apis.map.qq.com/bigdata/popinsight/v1/flowuserprofile";
    private static final Gson GSON = new Gson();
    private static final Type T_MAP_STR_OBJ = new TypeToken<Map<String, Object>>() {
    }.getType();


    public static Map<String, Object> getAdcode(String location, String key){
        int i = 0;
        while (i<3){
            Map<String, String> params = new HashMap<>();
            params.put("location",location);
            params.put("key",key);
            String content = null;
            try{
                content = HttpClientUtils.doGet(getAdcodeUrl,params);
            }catch (Exception e){
                String error = "getAdcode error: url = %s,location = %s,key = %s";
                System.out.println(String.format(error,getAdcodeUrl,location,key));
            }
            if (StrUtil.isNotEmpty(content)) {
                Map<String, Object> ret = GSON.fromJson(content, T_MAP_STR_OBJ);
                Object result = ret.get("result");
                if (result instanceof Map && !result.toString().equals("{}")) {
                    return (Map<String, Object>) result;
                }
            }
            i++;
        }
        return new HashMap<>();
    }

    public static Map<String, Object> getResidentNum(String location,int range,String adcode,String key,List<List<Double>> boundary, String month,String type){
        int i = 0;
        while (i<2) {
            Map<String, Object> params = new HashMap<>();
            if("圆形".equals(type)){
                params.put("boundary_type", "circle");
                params.put("center", location);
                List<String> ranges = Stream.of("100","200", "500", "1000", "1500").collect(Collectors.toList());
                if (!ranges.contains(String.valueOf(range))) {
                    ranges.add(String.valueOf(range));
                }
                params.put("range", String.join(",", ranges));
            }else {
                params.put("boundary_type", "polygon");
                params.put("boundary", Lists.asList(boundary, new LinkedList[]{}));
            }
            params.put("adcode", adcode);
            params.put("min_area", 10000);
            params.put("month", month);
            params.put("people_type", "1,2,3,4");
            params.put("key", key);
            String content = null;
            try {
                content = HttpClientUtils.httpPostRaw(residentNumUrl, JSON.toJSONString(params),null, "UTF-8");
            } catch (Exception e) {
                String error = "getResident error: url = %s,params = %s";
                System.out.println(String.format(error,getAdcodeUrl,location,key));
            }
            if (StrUtil.isNotEmpty(content)) {
                Map<String, Object> ret = GSON.fromJson(content, T_MAP_STR_OBJ);
                Object result = ret.get("result");
                if (result instanceof Map && !result.toString().equals("{}")) {
                    return (Map<String, Object>) result;
                }
            }
            i++;
        }
        return new HashMap<>();
    }


    public static Map<String, Object> getResident(String location,int range,String adcode,String peopleType,String key, List<List<Double>> boundary, String month,String type){
        int i = 0;
        while (i<2) {
            Map<String, Object> params = new HashMap<>();
            if("圆形".equals(type)){
                params.put("boundary_type", "circle");
                params.put("center", location);
                params.put("range", range);
            }else {
                params.put("boundary_type", "polygon");
                params.put("boundary", Lists.asList(boundary, new LinkedList[]{}));
            }
            params.put("adcode", adcode);
            params.put("min_area", 10000);
            params.put("month", month);
            params.put("people_type", peopleType);
            params.put("key", key);
            params.put("label", "101010,101012,101015,101110,1012,1013,1112,1124,1120,1121,1125,1110,1117,1116,1119,1310,1311,1312,1401,1501,1610,1612");

            String content = null;
            try {
                content = HttpClientUtils.httpPostRaw(residentUrl, JSON.toJSONString(params),null, "UTF-8");
            } catch (Exception e) {
                String error = "getResident error: url = %s,params = %s";
                System.out.println(String.format(error,getAdcodeUrl,location,key));
            }
            if (StrUtil.isNotEmpty(content)) {
                Map<String, Object> ret = GSON.fromJson(content, T_MAP_STR_OBJ);
                Object result = ret.get("result");
                if (result instanceof Map && !result.toString().equals("{}")) {
                    return (Map<String, Object>) result;
                }
            }
            i++;
        }
        return new HashMap<>();
    }

    public static Map<String, Object> getFlow(String location,int range,String adcode,String peopleType,String key, List<List<Double>> boundary, String month,String type){
        int i = 0;
        while (i<2) {
            Map<String, Object> params = new HashMap<>();
            if("圆形".equals(type)){
                params.put("boundary_type", "circle");
                params.put("center", location);
                params.put("range", range);
            }else {
                params.put("boundary_type", "polygon");
                params.put("boundary", Lists.asList(boundary, new LinkedList[]{}));
            }
            params.put("adcode", adcode);
            params.put("min_area", 10000);
            params.put("month", month);
            params.put("date_type", peopleType);
            params.put("key", key);
            params.put("label", "101010,101012,101015,101110,1012,1013,1112,1124,1120,1121,1125,1110,1117,1116,1119,1310,1311,1312,1401,1501,1610,1612");

            String content = null;
            try {
                content = HttpClientUtils.httpPostRaw(flowUrl, JSON.toJSONString(params),null, "UTF-8");
            } catch (Exception e) {
                String error = "getFlow error: url = %s,params = %s";
                System.out.println(String.format(error,getAdcodeUrl,location,key));
            }
            if (StrUtil.isNotEmpty(content)) {
                Map<String, Object> ret = GSON.fromJson(content, T_MAP_STR_OBJ);
                Object result = ret.get("result");
                if (result instanceof Map && !result.toString().equals("{}")) {
                    return (Map<String, Object>) result;
                }
            }
            i++;
        }
        return new HashMap<>();
    }

    public static Map<String, Object> getFlowNum(String location, int range, String adcode, String peopleType, String key, List<List<Double>> boundary, String month, String type){
        int i = 0;
        while (i<2) {
            Map<String, Object> params = new HashMap<>();
            if("圆形".equals(type)){
                params.put("boundary_type", "circle");
                params.put("center", location);
                List<String> ranges = Stream.of("100","200", "500", "1000", "1500").collect(Collectors.toList());
                if (!ranges.contains(String.valueOf(range))) {
                    ranges.add(String.valueOf(range));
                }
                params.put("range", String.join(",", ranges));
            }else {
                params.put("boundary_type", "polygon");
                params.put("boundary", Lists.asList(boundary, new LinkedList[]{}));
            }
            params.put("adcode", adcode);
            params.put("min_area", 10000);
            params.put("month", month);
            params.put("date_type", peopleType);
            params.put("key", key);
            String content = null;
            try {
                content = HttpClientUtils.httpPostRaw(flowNumUrl, JSON.toJSONString(params),null, "UTF-8");
            } catch (Exception e) {
                String error = "getFlowNum error: url = %s,params = %s";
                System.out.println(String.format(error,getAdcodeUrl,location,key));
            }
            if (StrUtil.isNotEmpty(content)) {
                Map<String, Object> ret = GSON.fromJson(content, T_MAP_STR_OBJ);
                Object result = ret.get("result");
                if (result instanceof Map && !result.toString().equals("{}")) {
                    return (Map<String, Object>) result;
                }
            }
            i++;
        }
        return new HashMap<>();
    }

//    private static String getAdcodeUrl = "https://apis.map.qq.com/ws/geocoder/v1/";
//    private static String residentUrl = "https://apis.map.qq.com/bigdata/popinsight/v1/residentuserprofile";
//    private static String flowNumUrl = "https://apis.map.qq.com/bigdata/popinsight/v1/flownum";
//    private static String flowUrl = "https://apis.map.qq.com/bigdata/popinsight/v1/flowuserprofile";
//
//
//
//    public static String getAdcode(String location,String key){
//        Map<String, String> params = new HashMap<>();
//        params.put("location",location);
//        params.put("key",key);
//        String content = null;
//        try{
//            content = HttpClientUtils.doGet(getAdcodeUrl,params);
//        }catch (Exception e){
//            String error = "getAdcode error: url = %s,location = %s,key = %s";
//            System.out.println(String.format(error,getAdcodeUrl,location,key));
//        }
//        return content;
//    }
//
//    public static String getResident(String location,int range,String adcode,String peopleType,String key){
//        String params = "{ \"boundary_type\": \"circle\", \"center\": \"%s\", \"range\": \"%s\", \"adcode\": \"%s\", \"min_area\": \"10000\", \"month\": \"201911\", \"people_type\": \"%s\", \"label\":\"101010,101012,101015,101110,1012,1013,1112,1124,1120,1121,1125,1110,1117,1116,1119,1310,1311,1312,1401,1501,1610,1612\", \"key\": \"%s\" }";
//        params = String.format(params, location, range,adcode,peopleType,key);
//        String result = null;
//        try{
//            result = HttpClientUtils.httpPostRaw(residentUrl, params, null, "UTF-8");
//        }catch (Exception e){
//            String error = "getResident error: url = %s,params = %s";
//            System.out.println(String.format(error,getAdcodeUrl,params));
//        }
//        return result;
//    }
//
//    public static String getFlow(String location,int range,String adcode,String peopleType,String key){
//        String params = "{ \"boundary_type\": \"circle\", \"center\": \"%s\", \"range\": \"%s\", \"adcode\": \"%s\", \"min_area\": \"10000\", \"month\": \"201911\", \"date_type\": \"%s\", \"label\":\"101010,101012,101015,101110,1012,1013,1112,1124,1120,1121,1125,1110,1117,1116,1119,1310,1311,1312,1401,1501,1610,1612\", \"key\": \"%s\" }";
//        params = String.format(params, location, range,adcode,peopleType,key);
//        String result = null;
//        try{
//            result = HttpClientUtils.httpPostRaw(flowUrl, params, null, "UTF-8");
//        }catch (Exception e){
//            String error = "getFlow error: url = %s,params = %s";
//            log.error(String.format(error,getAdcodeUrl,params));
//        }
//        return result;
//    }
//
//    public static String getFlowNum(String location,int range,String adcode,String peopleType,String key){
//        String params = "{ \"boundary_type\": \"circle\", \"center\": \"%s\", \"range\": \"%s\", \"adcode\": \"%s\", \"min_area\": \"10000\", \"month\": \"201911\", \"date_type\": \"%s\", \"key\": \"%s\" }";
//        params = String.format(params, location, range,adcode,peopleType,key);
//        String result = null;
//        try{
//            result = HttpClientUtils.httpPostRaw(flowNumUrl, params, null, "UTF-8");
//        }catch (Exception e){
//            String error = "getFlowNum error: url = %s,params = %s";
//            log.error(String.format(error,getAdcodeUrl,params));
//        }
//        return result;
//    }


}
