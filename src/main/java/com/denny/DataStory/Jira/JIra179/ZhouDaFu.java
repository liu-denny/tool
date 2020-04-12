package com.denny.DataStory.Jira.JIra179;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.denny.Utils.CityCode;
import com.denny.Utils.HttpClientUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @auther denny
 * @create 2020-03-05 17:39
 */
public class ZhouDaFu {

    public static void main(String[] args) throws IOException {
        String path = "C:\\Users\\20190327R2\\Desktop\\Excel\\周大福86点位置.xlsx" ;
        ExcelReader reader = ExcelUtil.getReader(path,0);
        List<Map<String,Object>> readAll = reader.readAll();
        List<Map<String, Object>> resultList = new ArrayList<>();
        Integer range = 3000;
        String url = "https://apis.map.qq.com/bigdata/popinsight/v1/residentuserprofile";

        for (Map<String, Object> map : readAll) {
            String params = "{ \"boundary_type\": \"circle\", \"center\": \"%s\", \"range\": \"%s\", \"adcode\": \"%s\", \"min_area\": \"10000\", \"month\": \"201911\", \"people_type\": \"4\", \"label\":\"101010,101012,101015,101110,1012,1013,1112,1124,1120,1121,1125,1110,1117,1116,1119,1310,1311,1312,1401,1501,1610,1612\", \"key\": \"UJMBZ-T35WG-UEDQL-IGGWO-7V7TV-OGB4A\" }";
            String name = (String) map.get("门店名称");
            Map<String, Object> resultMap = new LinkedHashMap<>();
            resultMap.put("门店名称",name);
            String adcode = CityCode.adcodeMap.get(map.get("行政区"));
            String[] strings = ((String) map.get("高德经纬度（地图标识点位）")).split(",");
            String gps = strings[1] + "," + strings[0];
            resultMap.put("location",gps);
            params = String.format(params, gps, range,adcode);
            String result = HttpClientUtils.httpPostRaw(url, params, null, "UTF-8");
            JSONObject object = JSON.parseObject(result).getJSONObject("result");
            if(object == null || object.equals("")){
                continue;
            }
            try{
                Map<String, Object> objectMap = object.getInnerMap();

                for(Map.Entry<String,Object> entry : objectMap.entrySet()){
                    String key = entry.getKey();
                    Map<String, Object> valueMap = (Map<String, Object>) entry.getValue();
                    for(Map.Entry<String,Object> valueMapEntry : valueMap.entrySet()){
                        String keyValue = valueMapEntry.getKey();
                        JSONObject value = (JSONObject) valueMapEntry.getValue();
                        resultMap.put(key + "-" + keyValue + "-TGI",value.getString("TGI"));
                        resultMap.put(key + "-" + keyValue + "-percent",value.getString("percent"));
                    }

                }

                resultList.add(resultMap);
                Thread.sleep(300);
            }catch (Exception e){
                System.out.println(object);
            }
        }


        String writePath = "D:\\work\\tool\\src\\main\\resources\\Jira179\\周大福" + range + "M.xlsx";
        ExcelWriter writer = ExcelUtil.getWriter(writePath);
        writer.write(resultList);
        writer.close();
        reader.close();
    }
}
