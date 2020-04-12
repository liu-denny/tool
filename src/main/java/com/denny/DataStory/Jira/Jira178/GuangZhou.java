package com.denny.DataStory.Jira.Jira178;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.denny.Utils.EsUtils;
import com.denny.Utils.LocationUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @auther denny
 * @create 2020-03-06 16:36
 */
public class GuangZhou {
    private static int batchSize = 500;

    public static void main(String[] args) throws IOException {
        EsUtils esClient = new EsUtils("dev1",9203,9204);

        Map<String,Map<String,Integer>> gridMap = getGridAll(esClient);

        String path = "C:\\Users\\20190327R2\\Desktop\\Excel\\工作簿2.xlsx";
        ExcelReader reader = ExcelUtil.getReader(path,0);
        List<Map<String,Object>> readAll = reader.readAll();
        List<Map<String,Object>> resultList = new ArrayList<>();

        for (Map<String, Object> map : readAll) {
            String name = (String) map.get("name");
            if(name == null){
                break;
            }
            //拿到poi点
            BoolQueryBuilder bool = new BoolQueryBuilder();
            bool.must(QueryBuilders.termQuery("cityname.keyword","广州市"));
            bool.must(QueryBuilders.matchPhraseQuery("type",name));
            SearchSourceBuilder builder = new SearchSourceBuilder();
            builder.fetchSource(new String[]{"location"}, new String[]{});
            builder.query(bool).size(batchSize);
            SearchRequest searchRequest = new SearchRequest("test-map-v2");
            searchRequest.types("poi");
            searchRequest.source(builder).searchType(SearchType.DEFAULT);
            searchRequest.scroll(new TimeValue(300000));
            List<Map<String, Object>> poiList = esClient.esScroll(searchRequest,batchSize);
            for (Map<String, Object> objectMap : poiList) {
                String location = (String) objectMap.get("location");
                String[] loc = location.split("，");
                String lat = loc[1];
                String lng = loc[0];
                List<List<Double>> boundary = LocationUtils.getGridBoundary(Double.valueOf(lat),Double.valueOf(lng),500);
                String key = getGridByPoi(esClient,boundary);
                if(Strings.isEmpty(key)){
                    continue;
                }
                if(gridMap.containsKey(key)){
                    //存在
                    Map<String,Integer> poiNumMap = gridMap.get(key);
                    if(poiNumMap.containsKey(name)){
                        poiNumMap.put(name,poiNumMap.get(name) + 1);
                    }else {
                        poiNumMap.put(name,1);
                    }
                }else {
                    continue;
                }
            }
        }


        //包装数据，写入Excel
        for(Map.Entry<String, Map<String, Integer>> entry : gridMap.entrySet()){
            Map<String,Object> result = new HashMap<>();
            result.put("location",entry.getKey());
            Map<String, Integer> valueMap = entry.getValue();
            for(Map.Entry<String, Integer> value : valueMap.entrySet()){
                result.put(value.getKey(),value.getValue());
            }
            resultList.add(result);
        }

        String writePath = "D:\\work\\tool\\src\\main\\resources\\Jira178\\广州Poi匹配.xlsx";
        ExcelWriter writer = ExcelUtil.getWriter(writePath);
        writer.write(resultList);
        writer.close();
        reader.close();
        esClient.close();

    }

    //获取全部栅格
    public static Map<String,Map<String,Integer>> getGridAll(EsUtils esClient) throws IOException {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.fetchSource(new String[]{"location"}, new String[]{});
        SearchRequest searchRequest = new SearchRequest("t_map_grid");
        searchRequest.types("grid");
        searchRequest.source(builder.size(batchSize)).searchType(SearchType.DEFAULT);
        searchRequest.scroll(new TimeValue(300000));
        List<Map<String, Object>> result = esClient.esScroll(searchRequest,batchSize);
        Map<String,Map<String,Integer>> mapGrid = new HashMap<>();
        for (Map<String, Object> map : result) {
            if(map.containsKey("location")){
                Map<String,Object> location = (Map<String, Object>) map.get("location");
                String key = location.get("latitude") + "," + location.get("longitude");
                mapGrid.put(key,new HashMap<String, Integer>());
            }

        }
        return mapGrid;
    }

    //根据poi点获取对应栅格
    public static String getGridByPoi(EsUtils esClient, List<List<Double>> boundary) throws IOException {
        BoolQueryBuilder bool = QueryBuilders.boolQuery();
        RangeQueryBuilder rangeLatitude = QueryBuilders.rangeQuery("location.latitude");
        rangeLatitude.gte(boundary.get(0).get(0));
        rangeLatitude.lte(boundary.get(1).get(0));
        RangeQueryBuilder rangeLongitude = QueryBuilders.rangeQuery("location.longitude");
        rangeLongitude.gte(boundary.get(0).get(1));
        rangeLongitude.lte(boundary.get(2).get(1));
        bool.must(rangeLatitude);
        bool.must(rangeLongitude);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.fetchSource(new String[]{"location"}, new String[]{});
        builder.query(bool);
        SearchRequest searchRequest = new SearchRequest("t_map_grid");
        searchRequest.types("grid");
        searchRequest.source(builder);

        List<Map<String, Object>> result = esClient.esScroll(searchRequest,batchSize);
        String key = null;
        try{
            if(result != null || result.size() != 0){
                Map<String,Object> location = (Map<String, Object>) result.get(0).get("location");
                key = location.get("latitude") + "," + location.get("longitude");
            }
        }catch (Exception e){

        }
        return key;

    }

}
