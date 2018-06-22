package com.example.two.weather.jsonentity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuhuan1 on 2018/6/19.
 */

public class Basic {
    @SerializedName("city")
    String cityName;

    @SerializedName("id")
    String cityCode;

    public Update update;

    public class Update{
        @SerializedName("loc")//loc表示更新的时间
        public String updateTime;
    }

}
