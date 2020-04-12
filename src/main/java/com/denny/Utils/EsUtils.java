package com.denny.Utils;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @auther denny
 * @create 2020-03-03 16:48
 */
public class EsUtils {
    private String hostname;
    private int port1;
    private int port2;
    private RestHighLevelClient client;

    public EsUtils(String hostname, int port1, int port2) {
        this.hostname = hostname;
        this.port1 = port1;
        this.port2 = port2;

        this.client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(hostname,port1,"http"),
                        new HttpHost(hostname,port2,"http")
                ).setMaxRetryTimeoutMillis(5*60*1000)
        );
    }

    public RestHighLevelClient getClient() {
        return client;
    }

    /**
     * es游标获取数据
     * @return
     */
    public List<Map<String, Object>> esScroll(SearchRequest request, int batchSize) throws IOException {
        RestHighLevelClient client = this.client;
        List<Map<String, Object>> result = new LinkedList<>();
        String scrollId = null;

        SearchResponse response = client.search(request);
        if (response != null && response.getHits().getHits().length > 0) {
            for (SearchHit hit : response.getHits().getHits()) {
                result.add(hit.getSourceAsMap());
            }
            scrollId = response.getScrollId();
        }

        // 当累计大小超过批次才需要scroll获取剩余部分
        if (result.size() >= batchSize) {
            while (scrollId != null) {
                SearchScrollRequest searchScrollRequest = new SearchScrollRequest(scrollId)
                        .scroll(TimeValue.timeValueMinutes(1));
                response = client.searchScroll(searchScrollRequest);
                if (response != null && response.getHits().getHits().length > 0) {
                    for (SearchHit hit : response.getHits().getHits()) {
                        result.add(hit.getSourceAsMap());
                    }
                    scrollId = response.getScrollId();
                } else {
                    break;
                }
            }
        }
        return result;
    }

    public void close() {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
