package com.example.two.weather.jsonentity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuhuan1 on 2018/6/19.
 */

public class Weather {//由于Weather类本身就可以表示出所有需要的数据，所以不用在本类再加Get和Set方法

    public String status;

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
