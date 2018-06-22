package com.example.two.weather.jsonentity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuhuan1 on 2018/6/19.
 */

public class Suggestion {
    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    public Sport sport;

    public class Comfort{//舒适度建议
        public String txt;
    }

    public class CarWash{//洗车建议
        public String txt;
    }

    public class Sport{//运动建议
        public String txt;
    }
}
