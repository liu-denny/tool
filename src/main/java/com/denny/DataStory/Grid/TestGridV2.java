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
public class TestGridV2 {

    private static final double latitude_length = 0.004496634968;  // 500m
    private static final double longitude_length = 0.004885869565; // 500m

    /**
     * 步骤一：将栅格转化为一张二维表
     */
    public static void main(String[] args) {
        List<String> loctions = Stream.of(
                "23.00,113.00",
                "23.00,114.00",
                "23.00,115.00",
                "23.00,116.00",
                "24.00,114.00",
                "24.00,115.00",
                "25.00,113.00",
                "25.00,114.00",
                "25.00,115.00",
                "25.00,116.00",
                "26.00,113.00",
                "26.00,115.00",
                "26.00,113.00",
                "26.00,114.00",
                "26.00,115.00",
                "26.00,116.00"
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
        int d = distance-1;

        List<Double> newLat = new ArrayList<>();
        List<Double> newLng = new ArrayList<>();

        for(int i=0;i<latList.size();i=i+distance){
            Double la = ((Double) latList.get(i) + (Double)latList.get(i+d)/2);
            newLat.add(la);
        }

        for(int i=lngList.size()-1;i>-1;i=i-distance){
            Double la = ((Double) lngList.get(i) + (Double)lngList.get(i-d)/2);
            newLng.add(la);
        }

        Map<String,List<String>> map = new HashMap<>();
        for(int i=0;i<latList.size();i=i+distance){
            Double dd = 0.0;

            List<String> loList = new ArrayList<>();
            for(int j=0;j<distance;j++){
                dd = dd + (Double) latList.get(i+j);
                loList.add(latList.get(i+j) + ",");
            }
            dd = dd/distance;

            for(int k=lngList.size()-1;k>-1;k=k-distance){
                Double ll = 0.0;
                List<String> loList1 = new ArrayList<>();
                for(int j=0;j<distance;j++){
                    ll = ll + (Double) lngList.get(k-j);
                    String l = lngList.get(k-j).toString();
                    for(int z=0;z<loList.size();z++){
                        loList1.add(loList.get(z) + l);
                    }
                }
                ll = ll/distance;
                map.put(dd + "," + ll,loList1);
            }
        }

//        for(int i=0;i<lngList.size();i=i+distance){
//            newLng.add((Double) lngList.get(i));
//            newLng.add((Double) lngList.get(i+2));
//        }

        newLat.forEach(item -> System.out.println(item));
        System.out.println("--------");
        newLng.forEach(item -> System.out.println(item));

        for(Map.Entry<String,List<String>> entry :map.entrySet()){
            List<String> list = entry.getValue();
            String l = null;
            for (String s : list) {
                l = s + ";" + l;
            }
            System.out.println(entry.getKey() + "->" + l);
        }

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
