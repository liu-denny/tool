package com.denny.DataStory.Jira.Jira148;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
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

import java.io.IOException;
import java.util.*;

/**
 * @Description
 * @auther denny
 * @create 2020-02-17 18:16
 */
public class JiraMain {
    public static double distance = 707.11;
    public static Map<String,String> residentMap = new HashMap<String, String>() {{
        put("居住人口","resident_profile_2");
        put("常住人口","resident_profile_4");
    }};
//    神秘的Key：S34BZ-2O73Q-WGY56-GDSX6-T4IUJ-UQFJ4
//    志华的Key：UJMBZ-T35WG-UEDQL-IGGWO-7V7TV-OGB4A
//    方玲的Key：UUUBZ-XPCKP-2GODC-LWD26-3NFL7-NJFPN
//    利鑫的Key：CK6BZ-FUMLI-D44GB-5RTY5-D3I65-SBBKX
    public static final List<String> keys = Arrays.asList(
//            "S34BZ-2O73Q-WGY56-GDSX6-T4IUJ-UQFJ4",
//            "UJMBZ-T35WG-UEDQL-IGGWO-7V7TV-OGB4A",
//            "UUUBZ-XPCKP-2GODC-LWD26-3NFL7-NJFPN",
            "CK6BZ-FUMLI-D44GB-5RTY5-D3I65-SBBKX"
    );
    public static String url = "https://apis.map.qq.com/ws/place/v1/search" ;




    public List<D> getList(RestHighLevelClient client,double lat, double lng){
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
        SearchResponse response = null;
        List<D> dList = new ArrayList<>();
        try {
            response = client.search(searchRequest);
        } catch (IOException e) {
            return dList;
        }

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
                    dList.add(d);
                }
            }
        }
        return dList;
    }

    public void dealResident(List<D> dList){
        double dn = 0;
        for (D d : dList) {
            dn = dn + d.getD();
        }
        for (D d : dList) {
            double scale = d.getD() / dn;
            d.setScale(scale);

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
                        JSONObject jsonObject = JSON.parseObject(d.getMap().get(entry.getValue()).toString());
                        String a = jsonObject.getJSONObject(father).getJSONObject(child).getString("percent").split("%")[0];
                        Double percent =  Double.valueOf(a) * scale;
                        if(d.getResultMap() == null){
                            d.setResultMap(new HashMap<>());
                        }
                        d.getResultMap().put(entry.getKey() + "_" + r,percent);
                    }catch (Exception e){
                        System.out.println(d.getLat() + "," + d.getLng());
                        System.out.println(r);
                    }
                }
            }

        }
    }

    public Double dealKeyword(String key,String keyword,double lat, double lng){
        String nearby = "nearby(%s,%s,500)";
        nearby = String.format(nearby,lat,lng);
        Map<String,String> params = new HashMap<>();
        params.put("key", key);
        params.put("keyword", keyword);
        params.put("boundary", nearby);
        try{
            String response = HttpClientUtils.doGet(url, params);
            if(response.contains("此key每日调用量已达到上限")){
                return null;
            }
            double count = JSON.parseObject(response).getIntValue("count");
            return count;
        }catch (Exception e){
            return null;
        }
    }

    public Map<String, Object> deal(List<D> dList,Map<String, Object> result){
        for(Map.Entry<String,String> entry : residentMap.entrySet()) {
            for (String r : Jira.residentList) {
                double sum = 0;
                for (D d : dList) {
                    try{
                        sum = sum + d.getResultMap().get(entry.getKey() + "_" + r);
                    }catch (Exception e){
                        continue;
                    }
                }
                result.put(entry.getKey() + "_" + r, sum);
            }
        }

        return result;
    }



    public static void main(String[] args) {
        String path = "D:\\Chrome下载\\货架排面_样例数据for算法年龄段建模_20200121_（124家店+3个品牌）.xlsx";
        ExcelReader reader = ExcelUtil.getReader(path,0);
        List<Map<String,Object>> readAll = reader.readAll();
        List<Map<String, Object>> resultList = new ArrayList<>();
        EsClient esClient = new EsClient("dev1",9203,9204);
        JiraMain jiraMain = new JiraMain();

        for (Map<String, Object> map : readAll) {
            double lat = (double) map.get("纬度");
            double lng = (double) map.get("经度");
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("门店名称",map.get("门店名称"));
            result.put("SKU_ID",map.get("SKU_ID"));
            List<D> dList = jiraMain.getList(esClient.getClient(),lat,lng);
            jiraMain.dealResident(dList);
            result = jiraMain.deal(dList,result);
            resultList.add(result);
        }
        ExcelWriter writer = ExcelUtil.getWriter("D:\\work\\tool\\src\\main\\resources\\Jira148_2.xlsx");
        writer.write(resultList);
        writer.close();
        reader.close();
        esClient.close();

    }
}
