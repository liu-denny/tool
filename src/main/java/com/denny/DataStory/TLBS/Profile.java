package com.denny.DataStory.TLBS;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.denny.Utils.TLBSUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @auther denny
 * @create 2020-04-22 16:46
 */
public class Profile {
    public static void main(String[] args) throws InterruptedException {
        String readPath = "C:\\Users\\denny\\Desktop\\7295V.xlsx";
        String writePath = "D:\\denny\\code\\javaCode\\tool\\src\\main\\resources";
        System.out.println("开始");
        ExcelReader reader = ExcelUtil.getReader(readPath,0);
        List<Map<String,Object>> readAll = reader.readAll();
        List<Map<String, Object>> resultList = new ArrayList<>();
        System.out.println("读取结束");
        int range = 200;
        String peopleType = "2";
        String people = "居住人口";
        String dateType = "1";
        String date = "月均";
        String key = "UJMBZ-T35WG-UEDQL-IGGWO-7V7TV-OGB4A";
        String month = "201911";
        String type = "圆形";

        for(int n=6500;n<readAll.size();n++){
            Map<String,Object> map = readAll.get(n);
            Map<String,Object> target = new LinkedHashMap<>();
            String location = MapUtils.getString(map,"center");
            String adcode = MapUtils.getString(map,"adcode");
            target.put("center",location);
            target.put("adcode",adcode);
            try{
                Map<String,Object> result = TLBSUtils.getResident(location,range,adcode,peopleType,key,null,month,type);
                if(result != null && result.size() != 0) {
                    for (Map.Entry<String, Object> entry : result.entrySet()) {
                        Map<String, Object> value = (Map<String, Object>) entry.getValue();
                        for (Map.Entry<String, Object> entryValue : value.entrySet()) {
                            String valueKey = entryValue.getKey();
                            Map<String, Object> value2 = (Map<String, Object>) entryValue.getValue();
                            target.put(people + "_" + valueKey + "_" + "TGI",value2.get("TGI"));
                            target.put(people + "_" + valueKey + "_" + "percent",value2.get("percent"));
                        }
                    }
                }
                result = TLBSUtils.getFlow(location,range,adcode,dateType,key,null,month,type);
                if(result != null && result.size() != 0) {
                    for (Map.Entry<String, Object> entry : result.entrySet()) {
                        Map<String, Object> value = (Map<String, Object>) entry.getValue();
                        for (Map.Entry<String, Object> entryValue : value.entrySet()) {
                            String valueKey = entryValue.getKey();
                            Map<String, Object> value2 = (Map<String, Object>) entryValue.getValue();
                            target.put(date + "_" + valueKey + "_" + "TGI",value2.get("TGI"));
                            target.put(date + "_" + valueKey + "_" + "percent",value2.get("percent"));
                        }
                    }
                }
            }catch (Exception e){
                System.out.println(location);
            }
            resultList.add(target);
            System.out.println(n);
            Thread.sleep(100);
        }
        System.out.println("开始写入");
        ExcelWriter writer = ExcelUtil.getWriter(writePath + "\\6500.xlsx");
        writer.setSheet(0);
        writer.write(resultList);
        writer.close();
        reader.close();
        System.out.println("结束");
    }
}
