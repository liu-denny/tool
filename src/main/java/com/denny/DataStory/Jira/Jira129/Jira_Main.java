package com.denny.DataStory.Jira.Jira129;

import com.denny.DataStory.Utils.EsClient;

import java.io.IOException;

/**
 * @Description
 * @auther denny
 * @create 2020-02-07 15:21
 */
public class Jira_Main {

    public static void main(String[] args) throws IOException {
//        String readerPath = "/home/denny/渠道雷达-广州新增POI数据 - 提交V2.xlsx";
//        String writePath = "/home/denny/result.xlsx";

        String readerPath = "D:\\Chrome下载\\渠道雷达-广州新增POI数据 - 提交V2.xlsx";
        String writePath = "D:\\work\\tool\\src\\main\\resources\\result1.xlsx";

        EsClient esClient = new EsClient("dev1",9203,9204);

//        jira_129_2 j2 = new jira_129_2();
//        j2.start(esClient.getClient(),writePath,"购物中心");
        jira_129_1 j1 = new jira_129_1();
//        j1.start(esClient.getClient(),readerPath,0,"便利店",writePath);
        j1.start(esClient.getClient(),readerPath,1,"超市",writePath);



        esClient.close();

    }
}
