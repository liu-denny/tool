package com.denny.DataStory.Jira.Jira129;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.denny.DataStory.Utils.EsClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.denny.DataStory.Jira.Jira129.jira_129_1.getGridBoundary;

/**
 * @Description
 * @auther denny
 * @create 2020-02-08 12:50
 */
public class Jira_Test {
    public static void main(String[] args) throws IOException {
        String readerPath = "D:\\Chrome下载\\渠道雷达-广州新增POI数据 - 提交V.xlsx";
        EsClient esClient = new EsClient("dev1",9203,9204);
        ExcelReader reader = ExcelUtil.getReader(readerPath,0);
        List<Map<String,Object>> readAll = reader.readAll();
        int i = 0;
        for (Map<String, Object> map : readAll) {
            String[] location = ((String) map.get("高德经纬度")).split(",");
            Double longitude = Double.valueOf(location[0]);
            Double latitude = Double.valueOf(location[1]);
            List<List<Double>> boundary = getGridBoundary(latitude,longitude,500);
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
            try{
                SearchResponse response = esClient.getClient().search(searchRequest);
                if (response != null && response.getHits().getHits().length > 1) {
                    i++;
                    System.out.println(i);
                }
            }catch (Exception e){

            }

        }
        esClient.close();
        reader.close();
    }
}
