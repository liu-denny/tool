package com.denny.DataStory;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.denny.EsClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.*;

/**
 * @Description
 * @auther denny
 * @create 2020-02-07 14:54
 */
public class jira_129_2 {

    public void start(RestHighLevelClient client,String path,String sheetName) throws IOException {
        List<Map<String,Object>> targetList = getTarget(client);

        jira_129_1 j = new jira_129_1();
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Map<String, Object> map : targetList) {
            List<List<Double>> boundary = j.getGridBoundary(Double.valueOf(map.get("latitude").toString()),Double.valueOf(map.get("longitude").toString()),500);
            String name = (String) map.get("name");
            Long residenceNum = j.getResidenceNum(client,boundary);
            String resident = j.getResident(client,boundary);

            Map<String, Object> row = new LinkedHashMap<>();
            row.put("具体POI/门店",name);
            row.put("范围内小区数",residenceNum);
            row.put("同属的栅格",map.get("longitude") + "," +map.get("latitude"));
            row.put("栅格对应的常驻人数（提供四种类型）",resident);
            resultList.add(row);
        }
        ExcelWriter writer = ExcelUtil.getWriter(path);
        writer.setSheet(sheetName);
        writer.write(resultList);
        writer.close();
    }



    public static List<Map<String,Object>> getTarget(RestHighLevelClient client) throws IOException {
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("category.keyword","购物中心");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().size(1000);
        sourceBuilder.query(termQueryBuilder);
        sourceBuilder.fetchSource(new String[]{"name","location"}, new String[]{});

        SearchRequest searchRequest = new SearchRequest("t_map_poi_v6");
        searchRequest.types("poi");
        searchRequest.source(sourceBuilder);
        searchRequest.scroll(TimeValue.timeValueMillis(5L));

        SearchResponse response = client.search(searchRequest);
        String scrollId = null;
        List<Map<String, Object>> grids = new LinkedList<>();
        if (response != null && response.getHits().getHits().length > 0) {
            for (SearchHit hit : response.getHits().getHits()) {
                Map<String, Object> gridMap = new HashMap<>();
                Map<String, Object> location = (Map<String, Object>) hit.getSourceAsMap().get("location");
                gridMap.put("latitude",location.get("latitude"));
                gridMap.put("longitude",location.get("longitude"));
                gridMap.put("name",hit.getSourceAsMap().get("name"));
                grids.add(gridMap);
            }
            scrollId = response.getScrollId();
        }

        while (true){
            if(scrollId == null){
                break;
            }
            SearchScrollRequest searchScrollRequest = new SearchScrollRequest(scrollId);
            response = client.searchScroll(searchScrollRequest);
            if (response != null && response.getHits().getHits().length > 0) {
                for (SearchHit hit : response.getHits().getHits()) {
                    Map<String, Object> gridMap = new HashMap<>();
                    Map<String, Object> location = (Map<String, Object>) hit.getSourceAsMap().get("location");
                    gridMap.put("latitude",location.get("latitude"));
                    gridMap.put("longitude",location.get("longitude"));
                    gridMap.put("name",hit.getSourceAsMap().get("name"));
                    grids.add(gridMap);
                }
                scrollId = response.getScrollId();
            }else {
                break;
            }

        }
        return grids;
    }


}
