package com.denny.DataStory.ImageDTC;

import com.denny.DataStory.Utils.EsClient;
import com.denny.DataStory.Utils.ExcelUtils;
import com.denny.DataStory.Utils.LocationUtils;
import com.denny.Utils.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @auther denny
 * @create 2020-02-26 13:54
 */
public class Main2 {

    public static double distance = 707.11;

    public static void main(String[] args) throws SQLException {
        List<Map<String, Object>> targetReadAll = ExcelUtils.readAll("C:\\Users\\denny\\Desktop\\aa.xlsx");
        EsClient esClient = new EsClient("dev1", 9203, 9204);
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://dev.mysql.proxy.hdp:3001/db_datastory_channel_radar?characterEncoding=utf-8";
        String user = "channel_radar";
        String pass = "5OQG8eF5xL9tI8n8";
        JdbcUtil jdbcUtil = new JdbcUtil(driver, url, user, pass);
        Connection connection = jdbcUtil.getConnection();
        PreparedStatement st = null;
        String select = "SELECT * FROM t_stat_baby WHERE is_imagedt = 1";
        st = connection.prepareStatement(select);
        ResultSet rs = null;
        rs = st.executeQuery();

        String update = "UPDATE t_stat_baby SET consumer_s1 = ?,consumer_s2 = ?,consumer_s3 = ? where id = ?";

        PreparedStatement updateSt = null;
        while (rs.next()) {
            String id = rs.getString("id");

            double one = 0;
            double two = 0;
            double three = 0;
            double four = 0;
            double five = 0;


            double lat = rs.getDouble("latitude");
            double lng = rs.getDouble("longitude");
            List<List<Double>> boundary = LocationUtils.getGridBoundary(lat, lng, 500);
            List<Map<String, Object>> responseList = LocationUtils.getEsResponse(esClient.getClient(), boundary);
            if (responseList.size() > 4) {
                responseList = LocationUtils.getNear(responseList, lat, lng);
            }
            List<Store> storeList = new ArrayList<>();
            double sum = 0;
            for (Map<String, Object> objectMap : responseList) {
                Map<String, Object> location = (Map<String, Object>) objectMap.get("location");
                double latitude = (double) location.get("latitude");
                double longitude = (double) location.get("longitude");
                double dis = LocationUtils.GetDistance(lat, lng, latitude, longitude);
                sum = sum + distance - dis;
                Store store = new Store(latitude, longitude, distance - dis);
                storeList.add(store);
            }
            //计算比例
            for (Store store : storeList) {
//                store.setScale(store.getDistance() / sum);
                double scale = store.getDistance() / sum;
                //计算各个浓度
                for (Map<String, Object> objectMap : targetReadAll) {
//                    if((store.getLat()) == Double.valueOf(objectMap.get("纬度").toString()) && store.getLng() == Double.valueOf(objectMap.get("纬度").toString())){
                    if (objectMap.get("纬度").equals(store.getLat()) && objectMap.get("经度").equals(store.getLng())) {
                        two = two + scale * Double.valueOf("".equals(objectMap.get("1段奶粉购买人群归一化")) ? "0.00" : objectMap.get("1段奶粉购买人群归一化").toString());
                        three = three + scale * Double.valueOf("".equals(objectMap.get("2段奶粉购买人群归一化")) ? "0.00" : objectMap.get("2段奶粉购买人群归一化").toString());
                        four = four + scale * Double.valueOf("".equals(objectMap.get("3段奶粉购买人群归一化")) ? "0.00" : objectMap.get("3段奶粉购买人群归一化").toString());
                        break;
                    }
                }
            }

            updateSt = connection.prepareStatement(update);
            updateSt.setDouble(1,two);
            updateSt.setDouble(2,three);
            updateSt.setDouble(3,four);
            updateSt.setString(4,id);
            updateSt.executeUpdate();
        }

        st.close();
        updateSt.close();
        connection.close();
        System.out.println("结束");
    }
}
