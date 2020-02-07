package com.denny.DataStory;

import com.denny.EsClient;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description
 * @auther denny
 * @create 2020-02-07 15:21
 */
public class Jira_Main {

    public static void main(String[] args) throws IOException {
        String readerPath = "/home/denny/渠道雷达-广州新增POI数据 - 提交V2.xlsx";
        String writePath = "/home/denny/result.xlsx";

//        String readerPath = "D:\\Chrome下载\\渠道雷达-广州新增POI数据 - 提交V.xlsx";
//        String writePath = "D:\\work\\tool\\src\\main\\resources\\t4.xlsx";

        EsClient esClient = new EsClient("dev1",9203,9204);

        jira_129_1 j1 = new jira_129_1();
        j1.start(esClient.getClient(),readerPath,0,"便利店",writePath);
        j1.start(esClient.getClient(),readerPath,1,"超市",writePath);

        jira_129_2 j2 = new jira_129_2();
        j2.start(esClient.getClient(),writePath,"购物中心");

        esClient.close();
    }
}
