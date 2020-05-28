package com.denny.DataStory.Grid;

import com.google.common.collect.Lists;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @Description
 * @auther denny
 * @create 2020-05-13 15:11
 */
public class TestGridV4 {

    private static final double latitude_length = 0.004496634968;  // 500m
    private static final double longitude_length = 0.004885869565; // 500m

    /**
     * 步骤一：将栅格转化为一张二维表
     */
    public static void main(String[] args) {
        List<String> loctions = Stream.of(
                "23.01,113.01",
                "23.01,114.01",
                "23.01,115.01",
                "23.01,116.01",

                "24.01,112.01",
                "24.01,114.01",
                "24.01,115.01",

                "25.01,113.01",
                "25.01,114.01",
                "25.01,115.01",
                "25.01,116.01",

                "26.01,112.01",
                "26.01,113.01",
                "26.01,115.01",
                "26.01,113.01",

                "27.01,112.01",
                "27.01,114.01",
                "27.01,115.01",
                "27.01,116.01"
                )
                .collect(toList());
        Set<Double> lat = new HashSet<>();
        Set<Double> lng = new HashSet<>();
        loctions.forEach(item -> {
            String[] str = item.split(",");
            lat.add(Double.valueOf(str[0]));
            lng.add(Double.valueOf(str[1]));
        });

        List latList = new ArrayList(lat);
        List lngList = new ArrayList(lng);
        Collections.sort(latList);
        Collections.sort(lngList);

        int distance = 1000 /500;

        List<String> userLocation = new ArrayList<>();
        Map<String,List<String>> map = new HashMap<>();
        for(int i=0;i<latList.size();i=i+distance){
            if(i+distance <= latList.size()){
                Double dd = 0.0;

                List<String> loList = new ArrayList<>();
                for(int j=0;j<distance;j++){
                    dd = dd + (Double) latList.get(i+j);
                    loList.add(latList.get(i+j) + ",");
                }
                dd = dd/distance;

                for(int k=lngList.size()-1;k>-1;k=k-distance){
                    if(k >= distance) {
                        Double ll = 0.0;
                        List<String> loList1 = new ArrayList<>();
                        for (int j = 0; j < distance; j++) {
                            ll = ll + (Double) lngList.get(k - j);
                            String l = lngList.get(k - j).toString();
                            for (int z = 0; z < loList.size(); z++) {
                                loList1.add(loList.get(z) + l);
                            }
                        }
                        ll = ll / distance;
                        map.put(dd + "," + ll, loList1);
                        userLocation.addAll(loList1);
                    }
                }
            }

        }


        for(Map.Entry<String,List<String>> entry :map.entrySet()){
            List<String> list = entry.getValue();
            String l = null;
            for (String s : list) {
                l = s + ";" + l;
            }
            System.out.println(entry.getKey() + "->" + l);
        }

        System.out.println("---------------------");
        loctions.removeAll(userLocation);

        loctions.forEach(item -> System.out.println(item));

    }


    public static List<List<Double>> getGridBoundary(double latitude, double longitude, int length) {
        List<List<Double>> boundary = new LinkedList<>();
        boundary.add(Lists.asList(latitude - latitude_length / 2, longitude - longitude_length / 2, new Double[]{}));
        boundary.add(Lists.asList(latitude + latitude_length / 2, longitude - longitude_length / 2, new Double[]{}));
        boundary.add(Lists.asList(latitude + latitude_length / 2, longitude + longitude_length / 2, new Double[]{}));
        boundary.add(Lists.asList(latitude - latitude_length / 2, longitude + longitude_length / 2, new Double[]{}));

        return boundary;
    }
}
