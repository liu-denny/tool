package com.denny.DataStory.Jira.Jira148;

import java.util.Map;

/**
 * @Description
 * @auther denny
 * @create 2020-02-15 18:06
 */
public class D {
    private double lat;
    private double lng;
    private double scale;
    private double d;
    private Map<String,Object> map;
    private Map<String,Double> resultMap;

    public Map<String, Double> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, Double> resultMap) {
        this.resultMap = resultMap;
    }

    public D(double lat, double lng, double d, Map<String, Object> map) {
        this.lat = lat;
        this.lng = lng;
        this.d = d;
        this.map = map;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }
}
