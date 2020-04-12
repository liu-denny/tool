package com.denny.DataStory.Jira.Jira148;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.denny.DataStory.Jira.Jira129.Util;
import com.denny.DataStory.Utils.EsClient;
import com.denny.Utils.HttpClientUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @auther denny
 * @create 2020-02-15 16:49
 */
public class JiraTest {
    public static double distance = 707.11;

    public static void main(String[] args) throws Exception {
        double lat = 23.096271;
        double lng = 113.240136;
        EsClient esClient = new EsClient("dev1",9203,9204);
        RestHighLevelClient client = esClient.getClient();
        List<List<Double>> boundary = Util.getGridBoundary(lat,lng,1000);
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
        SearchResponse response = client.search(searchRequest);
        double dn = 0;
        List<D> dList = new ArrayList<>();
        if (response != null && response.getHits().getHits().length > 0) {
            for (SearchHit hit : response.getHits().getHits()) {
                Map<String,Object> map = hit.getSourceAsMap();
                Map<String,Object> location = (Map<String, Object>) map.get("location");
                double latitude = (double) location.get("latitude");
                double longitude = (double) location.get("longitude");
                double dis = Util.GetDistance(lat,lng,latitude,longitude);
                if(dis <= distance){
                    double di = distance - dis;
                    D d = new D(latitude,longitude,di,map);
                    dn = dn + di;
                    dList.add(d);
                }
            }
        }
        if(dList.size() > 4){
            dList = Jira.getNear(dList,lat,lng);
        }
        for (D d : dList) {
            double scale = d.getD() / dn;
            d.setScale(scale);
            JSONObject jsonObject = JSON.parseObject(d.getMap().get("resident_profile_1").toString());
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
                    String a = jsonObject.getJSONObject(father).getJSONObject(child).getString("percent").split("%")[0];
                    Double percent =  Double.valueOf(a) * scale;
                    if(d.getResultMap() == null){
                        d.setResultMap(new HashMap<>());
                    }
                    d.getResultMap().put(r,percent);
                }catch (Exception e){
                    System.out.println(r);
                }

            }
        }
        Map<String, Object> result = new HashMap<>();
        for (String r : Jira.residentList) {
            double sum = 0;
            for (D d : dList) {
                sum = sum + d.getResultMap().get(r);
            }
            result.put(r,sum);
        }
        Map<String,String> params = new HashMap<>();
        String nearby = "nearby(%s,%s,500)";
        params.put("key", "UJMBZ-T35WG-UEDQL-IGGWO-7V7TV-OGB4A");
        String url = "https://apis.map.qq.com/ws/place/v1/search" ;
        for (D d : dList) {
            nearby = String.format(nearby,lat,lng);
            params.put("boundary", nearby);
            for (String keyword : Jira.keywords) {
                params.put("keyword", keyword);
                String res = HttpClientUtils.doGet(url, params);
                double count = JSON.parseObject(res).getIntValue("count");
                d.getResultMap().put(keyword,count * d.getScale());
                Thread.sleep(300*1);
            }
        }


        System.out.println(1);

    }
}
