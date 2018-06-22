package com.example.two.weather.jsonentity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuhuan1 on 2018/6/19.
 */

public class Forecast {
    @SerializedName("date")
    public String futureDate;

    @SerializedName("cond")
    public Information futureInformation;

    @SerializedName("tmp")
    public Temperature futureTemperature;

    public class Information{
        @SerializedName("txt_d")
        public String info;
    }

    public class Temperature{
        public String max;
        public String min;
    }

}
