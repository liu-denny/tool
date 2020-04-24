package com.denny.DataStory.TLBS;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.denny.DataStory.Utils.ExcelUtils;
import com.denny.Utils.TLBSUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.*;

/**
 * @Description
 * @auther denny
 * @create 2020-04-22 14:40
 */
public class FlowNum {

    public static void main(String[] args) throws InterruptedException {
        String readPath = "C:\\Users\\denny\\Desktop\\7295V.xlsx";
        String writePath = "D:\\denny\\code\\javaCode\\tool\\src\\main\\resources";
        System.out.println("开始");
        ExcelReader reader = ExcelUtil.getReader(readPath,0);
        List<Map<String,Object>> readAll = reader.readAll();
        List<Map<String, Object>> resultList = new ArrayList<>();
        System.out.println("读取结束");
        int range = 2000;
        String peopleType = "1,2,3";
        String key = "UJMBZ-T35WG-UEDQL-IGGWO-7V7TV-OGB4A";
        String month = "201911";
        String type = "圆形";
        Map<String,String> trans = new HashMap<String, String>() {{
            put("holiday_avg","h");
            put("month_avg","m");
            put("workday_avg","w");
        }};
//        int n =1;
        for(int n=1500;n<4500;n++){
            Map<String,Object> map = readAll.get(n);
            Map<String,Object> target = new LinkedHashMap<>();
            String location = MapUtils.getString(map,"center");
            String adcode = MapUtils.getString(map,"adcode");
            target.put("center",location);
            target.put("adcode",adcode);
            try{
                Map<String,Object> result = TLBSUtils.getFlowNum(location,range,adcode,peopleType,key,null,month,type);
                if(result != null && result.size() != 0) {
                    for (Map.Entry<String, Object> entry : result.entrySet()) {
                        String rangeKey = entry.getKey();//100m
                        Map<String, Object> value = (Map<String, Object>) entry.getValue();
                        for (Map.Entry<String, Object> entryValue : value.entrySet()) {
                            String day = trans.get(entryValue.getKey());//h,w,m
                            Map<String, Object> value2 = (Map<String, Object>) entryValue.getValue();
                            String day_avg = MapUtils.getString(value2, "day_avg");
                            target.put("day_avg_" + day + "_" + rangeKey, day_avg);
                            Map<String, Object> hour_avg = (Map<String, Object>) value2.get("hour_avg");
                            for (int i = 0; i < 24; i++) {
                                target.put(rangeKey + "_" + day + "_" + i, hour_avg.get(String.valueOf(i)));
                            }
                        }
                    }
                }
            }catch (Exception e){
                System.out.println(location);
            }

            resultList.add(target);
            System.out.println("第" + n + "个");
            Thread.sleep(100);
        }

        System.out.println("开始写入");
        ExcelWriter writer = ExcelUtil.getWriter(writePath + "\\folwNum1500-4500.xlsx");
        writer.setSheet(0);
        writer.write(resultList);
        writer.close();
        reader.close();
        System.out.println("结束");
    }
}
