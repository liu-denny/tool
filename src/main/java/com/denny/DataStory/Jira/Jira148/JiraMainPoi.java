package com.denny.DataStory.Jira.Jira148;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.denny.DataStory.Utils.EsClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.*;

/**
 * @Description
 * @auther denny
 * @create 2020-02-17 18:16
 */
public class JiraMainPoi {
    public static double distance = 707.11;
    public static Map<String,String> residentMap = new HashMap<String, String>() {{
        put("居住人口","resident_profile_2");
        put("常住人口","resident_profile_4");
    }};


    public Map<String,Object> dealResident(Map<String,Object> map,Map<String,Object> result){
        for(Map.Entry<String,String> entry : residentMap.entrySet()){
            for (String r : Jira.residentList) {
                String[] rs = r.split("-");
                String father = rs[0];
                String child;
                if(rs.length > 2){
                    child = rs[1] + "-" + rs[2];
                }else {
                    child = rs[1];
                }
                try{
                    JSONObject jsonObject = JSON.parseObject(map.get(entry.getValue()).toString());
                    String percent = jsonObject.getJSONObject(father).getJSONObject(child).getString("percent");
                    result.put(entry.getKey() + "_" + r,percent);
                }catch (Exception e){
                    System.out.println(r);
                }
            }
        }
        return result;

    }



    public static void main(String[] args) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        EsClient esClient = new EsClient("dev1",9203,9204);
        RestHighLevelClient client = esClient.getClient();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().size(1000);
        SearchRequest searchRequest = new SearchRequest("t_stat_grid_v8");
        searchRequest.types("grid");
        searchRequest.source(sourceBuilder).scroll(TimeValue.timeValueMinutes(60));
        JiraMainPoi jiraMainPoi = new JiraMainPoi();
        try {
            SearchResponse  response = client.search(searchRequest);
            String scrollId = null;
            if (response != null && response.getHits().getHits().length > 0) {
                for (SearchHit hit : response.getHits().getHits()) {
                    Map<String,Object> map = new LinkedHashMap<>();
                    Map<String,Object> location = (Map<String, Object>) hit.getSourceAsMap().get("location");
                    map.put("纬度",location.get("latitude"));
                    map.put("经度",location.get("longitude"));
                    map = jiraMainPoi.dealResident(hit.getSourceAsMap(),map);
                    resultList.add(map);
                }
                scrollId = response.getScrollId();
            }
            while (true) {
                if (scrollId == null) {
                    break;
                }
                SearchScrollRequest searchScrollRequest = new SearchScrollRequest(scrollId).scroll(TimeValue.timeValueMinutes(60));
                ;
                response = client.searchScroll(searchScrollRequest);
                if (response != null && response.getHits().getHits().length > 0) {
                    for (SearchHit hit : response.getHits().getHits()) {
                        Map<String,Object> map = new LinkedHashMap<>();
                        Map<String,Object> location = (Map<String, Object>) hit.getSourceAsMap().get("location");
                        map.put("latitude",location.get("latitude"));
                        map.put("longitude",location.get("longitude"));
                        map = jiraMainPoi.dealResident(hit.getSourceAsMap(),map);
                        resultList.add(map);
                    }
                    scrollId = response.getScrollId();
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        ExcelWriter writer = ExcelUtil.getWriter("D:\\work\\tool\\src\\main\\resources\\Jira148_poi.xlsx");
        writer.write(resultList);
        writer.close();
        esClient.close();

    }
}
