package com.denny.DataStory.Utils;

import com.denny.DataStory.Jira.Jira148.D;
import com.google.common.collect.Lists;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.*;

/**
 * @Description
 * @auther denny
 * @create 2020-02-26 13:40
 */
public class LocationUtils {

    private static final double latitude_length = 0.004496634968;  // 500m
    private static final double longitude_length = 0.004885869565; // 500m
    private static double EARTH_RADIUS = 6378.137;//赤道半径（km）


    public static List<List<Double>> getGridBoundary(double latitude, double longitude, int length) {
        List<List<Double>> boundary = new LinkedList<>();
        double rate = 1000.0 / length;
        boundary.add(Lists.asList(latitude - latitude_length / rate, longitude - longitude_length / rate, new Double[]{}));
        boundary.add(Lists.asList(latitude + latitude_length / rate, longitude - longitude_length / rate, new Double[]{}));
        boundary.add(Lists.asList(latitude + latitude_length / rate, longitude + longitude_length / rate, new Double[]{}));
        boundary.add(Lists.asList(latitude - latitude_length / rate, longitude + longitude_length / rate, new Double[]{}));

        return boundary;
    }

    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    public static double GetDistance(double lat1, double lng1, double lat2, double lng2)
    {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 100d) / 100d;
        s = s * 1000;
        return s;
    }

    //计算最近的四个点
    public static List<Map<String,Object>> getNear(List<Map<String,Object>> list,double lat,double lng){
        List<Double> distanceList = new ArrayList<>();
        Map<Double,Map<String,Object>> result = new HashMap<>();
        for (Map<String,Object> map : list) {
            Map<String,Object> location = (Map<String, Object>) map.get("location");
            double distance = GetDistance((double) location.get("latitude"),(double) location.get("longitude"),lat,lng);
            distanceList.add(distance);
            result.put(distance,map);
        }
        Collections.sort(distanceList);
        Collections.reverse(distanceList);
        List<Map<String,Object>> resultList = new ArrayList<>();
        for(int i=0;i<4;i++){
            resultList.add(result.get(distanceList.get(i)));
        }
        return resultList;
    }

    public static List<Map<String,Object>> getEsResponse(RestHighLevelClient client,List<List<Double>> boundary){
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

        SearchRequest searchRequest = new SearchRequest("t_stat_grid_v8");
        searchRequest.types("grid");
        searchRequest.source(sourceBuilder);
        SearchResponse response = null;
        List<Map<String,Object>> list = new ArrayList<>();
        try {
            response = client.search(searchRequest);
        } catch (IOException e) {
            return list;
        }

        if (response != null && response.getHits().getHits().length > 0) {
            for (SearchHit hit : response.getHits().getHits()) {
                list.add(hit.getSourceAsMap());
            }
        }
        return list;
    }
}
