package com.denny.DataStory.Jira.Jira148;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

import java.util.*;

/**
 * @Description
 * @auther denny
 * @create 2020-02-18 22:27
 */
public class JiraMainKey {
    public static final List<String> keys = Arrays.asList(
//            "S34BZ-2O73Q-WGY56-GDSX6-T4IUJ-UQFJ4",
//            "UJMBZ-T35WG-UEDQL-IGGWO-7V7TV-OGB4A",
            "UUUBZ-XPCKP-2GODC-LWD26-3NFL7-NJFPN"
//            "CK6BZ-FUMLI-D44GB-5RTY5-D3I65-SBBKX"
    );

    public static void main(String[] args) throws InterruptedException {
        String path = "D:\\Chrome下载\\货架排面_样例数据for算法年龄段建模_20200121_（124家店+3个品牌） - 副本.xlsx";
        ExcelReader reader = ExcelUtil.getReader(path,0);
        List<Map<String,Object>> readAll = reader.readAll();
        List<Map<String, Object>> resultList = new ArrayList<>();
        int keyNum = 0;
        int time = 0;
        JiraMain jiraMain = new JiraMain();
        for (Map<String, Object> map : readAll) {
            double lat = (double) map.get("纬度");
            double lng = (double) map.get("经度");
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("门店名称",map.get("门店名称"));
            result.put("SKU_ID",map.get("SKU_ID"));
            for (String keyword : Jira.keywords) {
                Double count = jiraMain.dealKeyword(keys.get(keyNum),keyword,lat,lng);
                if(count == null){
                    String log = "key：%s,请求：%s次";
                    System.out.println(String.format(log, keys.get(keyNum),time));
                    time = 0;
                    System.out.println("切换key");
                    keyNum = keyNum + 1;
                    if(keyNum >= 1){
                        break;
                    }
                    count = jiraMain.dealKeyword(keys.get(keyNum),keyword,lat,lng);
                }
                time = time + 1;
                result.put(keyword,count);
                Thread.sleep(200);
            }
            resultList.add(result);
            if(keyNum >= 1){
                break;
            }
        }
        ExcelWriter writer = ExcelUtil.getWriter("D:\\work\\tool\\src\\main\\resources\\Jira148_key_1.xlsx");
        writer.write(resultList);
        writer.close();
        reader.close();
    }
}
