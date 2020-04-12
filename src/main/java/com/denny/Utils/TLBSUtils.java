//package com.denny.Utils;
//
//import lombok.extern.log4j.Log4j;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @Description
// * @auther denny
// * @create 2020-03-04 16:42
// */
//@Log4j
//public class TLBSUtils {
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
//
//
//}
