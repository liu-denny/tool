package com.denny.DataStory.ImageDTC;

import lombok.Data;

/**
 * @Description
 * @auther denny
 * @create 2020-02-26 13:50
 */
@Data
public class Store {
    private double lat;
    private double lng;
    private double distance;
    private double scale;


    public Store(double lat, double lng, double distance) {
        this.lat = lat;
        this.lng = lng;
        this.distance = distance;
    }
}
