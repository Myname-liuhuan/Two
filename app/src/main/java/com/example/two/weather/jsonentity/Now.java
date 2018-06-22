package com.example.two.weather.jsonentity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuhuan1 on 2018/6/19.
 */

public class Now {
    @SerializedName("tmp")//当前温度
    public String temperature;

    @SerializedName("cond")
    public Information information;//当前天气信息，比如阵雨

    public class Information{
        @SerializedName("txt")
        public String info;
    }
}
