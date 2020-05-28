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
public class TestGrid {

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
                "23.00,116.00"
//                "24.00,114.00",
//                "24.00,115.00",
//                "25.00,113.00",
//                "25.00,114.00",
//                "25.00,115.00",
//                "25.00,116.00",
//                "26.00,113.00",
//                "26.00,115.00",
//                "26.00,116.00"
                )
                .collect(toList());
        Set<Double> lat = new HashSet<>();
        Set<Double> lng = new HashSet<>();
        loctions.forEach(item -> {
            String[] str = item.split(",");
            List<List<Double>> boundary = getGridBoundary(Double.valueOf(str[0]),Double.valueOf(str[1]),500);
//            lng.add(boundary.get(0).get(1));
//            lng.add(boundary.get(2).get(1));
//            lat.add(boundary.get(0).get(0));
//            lat.add(boundary.get(1).get(0));
//            System.out.println(boundary.get(0).get(1));
//            System.out.println(boundary.get(1).get(1));
//            System.out.println();
        });

        List latList = new ArrayList(lat);
        List lngList = new ArrayList(lng);
        Collections.sort(latList);
        Collections.sort(lngList);

        int distance = 1000 /500;

        List<Double> newLat = new ArrayList<>();
        List<Double> newLng = new ArrayList<>();

        for(int i=0;i<latList.size();i=i+distance){
            newLat.add((Double) latList.get(i));
            newLat.add((Double) latList.get(i+2));
        }

        for(int i=0;i<lngList.size();i=i+distance){
            newLng.add((Double) lngList.get(i));
            newLng.add((Double) lngList.get(i+2));
        }

        newLat.forEach(item -> System.out.println(item));
        System.out.println("--------");
        newLng.forEach(item -> System.out.println(item));


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
