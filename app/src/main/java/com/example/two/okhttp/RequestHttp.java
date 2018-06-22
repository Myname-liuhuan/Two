package com.example.two.okhttp;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by liuhuan1 on 2018/6/14.
 */

public class RequestHttp {

    public static void sendRequest(String address,okhttp3.Callback callBack){//调用的时候传入实现接口Callback的类的对象
        OkHttpClient client = new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callBack);
    }
}