package com.denny.DataStory.TemporaryTask;

import com.denny.DataStory.Utils.EsClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;

/**
 * 导出系统品牌清单数据——业态、品牌
 * @Description
 * @auther denny
 * @create 2020-03-23 18:21
 */
public class Task_1 {
    public static void main(String[] args) throws IOException {
        EsClient esClient = new EsClient("dev2",9203,9204);

        AggregationBuilder level_1Agg = AggregationBuilders.terms("level_1").field("level_1.keyword");
//        AggregationBuilder level_2Agg = AggregationBuilders.terms("level_2").field("level_2.keyword");
//        level_1Agg.subAggregation(level_2Agg);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.aggregation(level_1Agg);
        SearchRequest searchRequest = new SearchRequest("t_map_poi");
        searchRequest.types("poi");
        searchRequest.source(builder).searchType(SearchType.DEFAULT);

        SearchResponse response = esClient.getClient().search(searchRequest);
        System.out.println(response);


    }
}
