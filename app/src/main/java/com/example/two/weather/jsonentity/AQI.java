package com.example.two.weather.jsonentity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuhuan1 on 2018/6/19.
 */

public class AQI {

    @SerializedName("city")
    public CityAQI cityAQI;

    public class CityAQI{
        public String aqi;
        public String pm25;
    }
}
