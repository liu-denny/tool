package com.denny.DataStory.Jira.Jira129;

import com.google.common.collect.Lists;

import java.util.LinkedList;
import java.util.List;

/**
 * @Description
 * @auther denny
 * @create 2020-02-15 16:14
 */
public class Util {
    private static final double latitude_length = 0.004496634968;  // 500m
    private static final double longitude_length = 0.004885869565; // 500m

    private static double EARTH_RADIUS = 6378.137;//赤道半径（km）

    public static List<List<Double>> getGridBoundary(double latitude, double longitude, int length) {
        List<List<Double>> boundary = new LinkedList<>();
        double rate = 1000.0 / length;
        boundary.add(Lists.asList(latitude - latitude_length / rate, longitude - longitude_length / rate, new Double[]{}));
        boundary.add(Lists.asList(latitude + latitude_length / rate, longitude - longitude_length / rate, new Double[]{}));
        boundary.add(Lists.asList(latitude + latitude_length / rate, longitude + longitude_length / rate, new Double[]{}));
        boundary.add(Lists.asList(latitude - latitude_length / rate, longitude + longitude_length / rate, new Double[]{}));

        return boundary;
    }

    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    public static double GetDistance(double lat1, double lng1, double lat2, double lng2)
    {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 100d) / 100d;
        s = s * 1000;
        return s;
    }



}
