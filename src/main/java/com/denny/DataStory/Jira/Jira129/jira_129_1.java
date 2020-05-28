package com.denny.DataStory.Jira.Jira129;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Description
 * @auther denny
 * @create 2020-02-07 10:00
 */
public class jira_129_1 {
    private static final double latitude_length = 0.004496634968;  // 500m
    private static final double longitude_length = 0.004885869565; // 500m

    public void start(RestHighLevelClient client,String path,int sheetNum,String sheetName,String writePath) throws IOException {
        ExcelReader reader = ExcelUtil.getReader(path,sheetNum);
        List<Map<String,Object>> readAll = reader.readAll();
        List<Map<String, Object>> resultList = new ArrayList<>();

        for (Map<String, Object> map : readAll) {
            String loca = ((String) map.get("高德经纬度"));
            String[] location = ((String) map.get("高德经纬度")).split(",");
            Double longitude = Double.valueOf(location[0]);
            Double latitude = Double.valueOf(location[1]);
            List<List<Double>> boundary = getGridBoundary(latitude,longitude,500);

            String name = (String) map.get("门店name");
            Long residenceNum = getResidenceNum(client,boundary);
            String resident = getResident(client,boundary);

            Map<String, Object> row = new LinkedHashMap<>();
            row.put("具体POI/门店",name);
            row.put("范围内小区数",residenceNum);
            row.put("同属的栅格",loca);
            row.put("栅格对应的常驻人数（提供四种类型）",resident);
            resultList.add(row);
        }

            ExcelWriter writer = ExcelUtil.getWriter(writePath);
            writer.setSheet(sheetName);
            writer.write(resultList);
            writer.close();
        reader.close();
    }

    public static Long getResidenceNum(RestHighLevelClient client,List<List<Double>> boundary) throws IOException {
        BoolQueryBuilder bool = QueryBuilders.boolQuery();
        RangeQueryBuilder rangeLatitude = QueryBuilders.rangeQuery("location.latitude");
        rangeLatitude.gte(boundary.get(0).get(0));
        rangeLatitude.lte(boundary.get(1).get(0));
        RangeQueryBuilder rangeLongitude = QueryBuilders.rangeQuery("location.longitude");
        rangeLongitude.gte(boundary.get(0).get(1));
        rangeLongitude.lte(boundary.get(2).get(1));
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("level_1.keyword","商务住宅");

        bool.must(rangeLatitude);
        bool.must(rangeLongitude);
        bool.must(termQueryBuilder);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(bool);
        sourceBuilder.fetchSource(new String[]{"total"}, new String[]{});

        SearchRequest searchRequest = new SearchRequest("t_map_poi_v6");
        searchRequest.types("poi");
        searchRequest.source(sourceBuilder);

        SearchResponse response = client.search(searchRequest);
        return response.getHits().totalHits;

    }

    public static String getResident(RestHighLevelClient client,List<List<Double>> boundary) throws IOException {
//        BoolQueryBuilder bool = QueryBuilders.boolQuery();
//        TermQueryBuilder termQueryBuilder1 = QueryBuilders.termQuery("location.latitude",latitude);
//        TermQueryBuilder termQueryBuilder2 = QueryBuilders.termQuery("location.longitude",longitude);
//
//        bool.must(termQueryBuilder1);
//        bool.must(termQueryBuilder2);

        BoolQueryBuilder bool = QueryBuilders.boolQuery();
        RangeQueryBuilder rangeLatitude = QueryBuilders.rangeQuery("location.latitude");
        rangeLatitude.gte(boundary.get(0).get(0));
        rangeLatitude.lte(boundary.get(1).get(0));
        RangeQueryBuilder rangeLongitude = QueryBuilders.rangeQuery("location.longitude");
        rangeLongitude.gte(boundary.get(0).get(1));
        rangeLongitude.lte(boundary.get(2).get(1));
        bool.must(rangeLatitude);
        bool.must(rangeLongitude);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(bool);
        sourceBuilder.fetchSource(new String[]{"resident"}, new String[]{});

        SearchRequest searchRequest = new SearchRequest("t_stat_grid_v7");
        searchRequest.types("grid");
        searchRequest.source(sourceBuilder);

        SearchResponse response = client.search(searchRequest);
        String result = "home: %s, work: %s, same: %s, resident: %s";
        BigDecimal same_resident = new BigDecimal(0);
        BigDecimal work = new BigDecimal(0);
        BigDecimal home = new BigDecimal(0);
        BigDecimal resident = new BigDecimal(0);
        String scrollId = null;
        if (response != null && response.getHits().getHits().length > 0) {
            for (SearchHit hit : response.getHits().getHits()) {
//                JSONObject json = JSON.parseObject((String) hit.getSourceAsMap().get("resident")).getJSONObject("polygon");
                JSONObject json = JSON.parseObject((String) hit.getSourceAsMap().get("resident"));
                same_resident = same_resident.add(json.getBigDecimal("same_resident"));
                work = work.add(json.getBigDecimal("work"));
                home = home.add(json.getBigDecimal("home"));
                resident = resident.add(json.getBigDecimal("resident"));
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
                    JSONObject json = JSON.parseObject((String) hit.getSourceAsMap().get("resident"));
                    same_resident = same_resident.add(json.getBigDecimal("same_resident"));
                    work = work.add(json.getBigDecimal("work"));
                    home = home.add(json.getBigDecimal("home"));
                    resident = resident.add(json.getBigDecimal("resident"));
                }
                scrollId = response.getScrollId();
            }else {
                break;
            }
        }
        result = String.format(result,home,work,same_resident,resident);
        return result;
    }

    public static List<List<Double>> getGridBoundary(double latitude, double longitude, int length) {
        List<List<Double>> boundary = new LinkedList<>();
        boundary.add(Lists.asList(latitude - latitude_length / 2, longitude - longitude_length / 2, new Double[]{}));
        boundary.add(Lists.asList(latitude + latitude_length / 2, longitude - longitude_length / 2, new Double[]{}));
        boundary.add(Lists.asList(latitude + latitude_length / 2, longitude + longitude_length / 2, new Double[]{}));
        boundary.add(Lists.asList(latitude - latitude_length / 2, longitude + longitude_length / 2, new Double[]{}));

        return boundary;
    }

    public static void main(String[] args) {
//        Double latitude = 23.110797418879;
//        Double longitude = 113.32221471737098;
//        113.327100586937,113.331986456503

        Double latitude = 23.0;
        Double longitude = 113.0;

        List<List<Double>> lists = getGridBoundary(latitude,longitude,500);
        for (List<Double> list : lists) {
//            System.out.println(list.get(1) + "," + list.get(0));
            System.out.println(list.get(0) + "," + list.get(1));
        }
//        List<Double> list = lists.get(0);
//        System.out.println(list.get(0) + latitude_length / 2);
//        System.out.println(list.get(1) + longitude_length / 2);
    }



}
