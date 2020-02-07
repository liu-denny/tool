package com.denny;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

/**
 * @Description
 * @auther denny
 * @create 2020-02-07 10:17
 */
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
                )
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
