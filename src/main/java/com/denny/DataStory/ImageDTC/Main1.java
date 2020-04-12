package com.denny.DataStory.ImageDTC;

import com.denny.DataStory.Utils.EsClient;
import com.denny.DataStory.Utils.ExcelUtils;
import com.denny.DataStory.Utils.LocationUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @auther denny
 * @create 2020-02-26 13:54
 */
public class Main1 {

    public static double distance = 707.11;

    public static void main(String[] args) {
        String path = "C:\\Users\\20190327R2\\Desktop\\Excel\\工作簿1.xlsx";
//        String path = "C:\\Users\\20190327R2\\Desktop\\Excel\\AC-广州母婴POI数据清单-大众点评+补充大超市.xlsx";
        List<Map<String,Object>> readAll = ExcelUtils.readAll(path);
        List<Map<String,Object>> targetReadAll = ExcelUtils.readAll("C:\\Users\\20190327R2\\Desktop\\Excel\\【渠道雷达-联合产品】人群热力数据20200224.xlsx");
        List<Map<String, Object>> resultList = new ArrayList<>();
        EsClient esClient = new EsClient("dev1",9203,9204);
        for (Map<String, Object> map : readAll) {
            double one = 0;
            double two = 0;
            double three = 0;
            double four = 0;
            double five = 0;


            double lat = (double) map.get("纬度");
            double lng = (double) map.get("经度");
            List<List<Double>> boundary = LocationUtils.getGridBoundary(lat,lng,500);
            List<Map<String,Object>> responseList = LocationUtils.getEsResponse(esClient.getClient(),boundary);
            if(responseList.size() > 4){
                responseList = LocationUtils.getNear(responseList,lat,lng);
            }
            List<Store> storeList = new ArrayList<>();
            double sum = 0;
            for (Map<String, Object> objectMap : responseList) {
                Map<String,Object> location = (Map<String, Object>) objectMap.get("location");
                double latitude = (double) location.get("latitude");
                double longitude = (double) location.get("longitude");
                double dis = LocationUtils.GetDistance(lat,lng,latitude,longitude);
                sum = sum + distance-dis;
                Store store = new Store(latitude,longitude,distance-dis);
                storeList.add(store);
            }
            //计算比例
            for (Store store : storeList) {
//                store.setScale(store.getDistance() / sum);
                double scale = store.getDistance() / sum;
                //计算各个浓度
                for (Map<String, Object> objectMap : targetReadAll) {
//                    if((store.getLat()) == Double.valueOf(objectMap.get("纬度").toString()) && store.getLng() == Double.valueOf(objectMap.get("纬度").toString())){
                    if(objectMap.get("纬度").equals(store.getLat()) && objectMap.get("经度").equals(store.getLng())){
                        one = one + scale * Double.valueOf("".equals(objectMap.get("高消费人群归一化"))? "0.00": objectMap.get("高消费人群归一化").toString());
                        two = two + scale * Double.valueOf("".equals(objectMap.get("1段奶粉购买人群归一化")) ? "0.00": objectMap.get("1段奶粉购买人群归一化").toString());
                        three = three + scale * Double.valueOf("".equals(objectMap.get("2段奶粉购买人群归一化")) ? "0.00": objectMap.get("2段奶粉购买人群归一化").toString());
                        four = four + scale * Double.valueOf("".equals(objectMap.get("3段奶粉购买人群归一化")) ? "0.00": objectMap.get("3段奶粉购买人群归一化").toString());
                        five = five + scale * Double.valueOf("".equals(objectMap.get("潜力值")) ? "0.00": objectMap.get("潜力值").toString());
                        break;
                    }
                }
            }
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("门店名称",map.get("门店名称"));
            result.put("纬度",lat);
            result.put("经度",lng);
            result.put("高消费人群",one);
            result.put("1段奶粉购买人群",two);
            result.put("2段奶粉购买人群",three);
            result.put("3段奶粉购买人群",four);
            result.put("潜力值",five);
            for(int i =0;i<storeList.size();i++){
                Store store = storeList.get(i);
                result.put("栅格"+i,store.getLat() +","+ store.getLng());
            }
            resultList.add(result);
        }
        ExcelUtils.writer("D:\\work\\tool\\src\\main\\resources\\ImageDTC.xlsx",resultList);
        ExcelUtils.close();
        esClient.close();
    }
}
