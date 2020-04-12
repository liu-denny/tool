package com.denny.DataStory.Utils;

/**
 * @Description
 * @auther denny
 * @create 2020-02-26 14:09
 */


import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;


public class EsClient {

    private String hostname;
    private int port1;
    private int port2;
    private RestHighLevelClient client;

    public EsClient(String hostname, int port1, int port2) {
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

