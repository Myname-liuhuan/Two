package com.example.two.gson;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.two.chinaCity.City;
import com.example.two.chinaCity.Provinces;
import com.example.two.database.MyDataBaseHelper;
import com.example.two.weather.jsonentity.Weather;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by liuhuan1 on 2018/6/16.
 */

public class ParserJSONWithGSON {

    //把传入的response插入到表provinces里面
    public static void provinceParserJsonResponse(Context context,String provinceJsonResponse){
        List<Provinces> provincesList;
        try{
            Gson gson=new Gson();
            provincesList=gson.fromJson(provinceJsonResponse,new TypeToken<List<Provinces>>(){}.getType());
            MyDataBaseHelper helper=new MyDataBaseHelper(context,"weather.db",null,1);
            SQLiteDatabase db=helper.getWritableDatabase();
            for (Provinces provinces:provincesList){//每个循环插入一行数据
                ContentValues values=new ContentValues();
                values.put("id",provinces.getId());
                values.put("name",provinces.getName());
                db.insert("provinces",null,values);
            }
            db.close();
            helper.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //把传入的response插入到表cities里面去，并记录其fatherCode
    public static void cityParserJsonResponse(Context context,String cityJsonResponse,int fatherId){
        List<City> cityList;
        try{
            Gson gson=new Gson();
            cityList=gson.fromJson(cityJsonResponse,new TypeToken<List<City>>(){}.getType());
            MyDataBaseHelper helper=new MyDataBaseHelper(context,"weather.db",null,1);
            SQLiteDatabase db=helper.getWritableDatabase();
            for (City city:cityList){//每个循环插入一行数据
                ContentValues values=new ContentValues();
                values.put("fatherId",fatherId);
                values.put("id",city.getId());
                values.put("name",city.getName());
                db.insert("cities",null,values);

            }
            db.close();
            helper.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //把传入的数据插入到表countries,并记录fatherCode
//    public static void countyParserJsonResponse(String cityJsonResponse){
//        List<County> countyList;
//        try{
//            Gson gson=new Gson();
//            countyList=gson.fromJson(cityJsonResponse,new TypeToken<List<County>>(){}.getType());
////            MyDataBaseHelper helper=new MyDataBaseHelper(context,"weather.db",null,1);
////            SQLiteDatabase db=helper.getWritableDatabase();
////            for (County county:countyList){//每个循环插入一行数据
////                ContentValues values=new ContentValues();
////                values.put("fatherId",fatherId);
////                values.put("id",county.getId());
////                values.put("name",county.getName());
////                values.put("weather_id",county.getWeather_id());
////                db.insert("counties",null,values);
////                Log.d("Tag",county.getName());
////            }
////            db.close();
////            helper.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }


    public static Weather weatherParserJsonResponse(String weatherResponse){//由于数据太过复杂，所以放到这进行GSON转换前的剖析，变为Gson可处理的数据
        try{
            JSONObject jsonObject=new JSONObject(weatherResponse);//jsonObject是全部的数据，但是我们需要的数据还在里面的里面
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather");//得到HeWeather表示的数组
            String weatherContent=jsonArray.getJSONObject(0).toString();//jsonArray里面有且仅有一条数据，取出，转化为String
            Weather weather=new Gson().fromJson(weatherContent,Weather.class);//用Gson将数据解析成weather类的数据，
            return weather;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;//前面出现问题(传入错误的weatherResponse，或者otherSomething)的时候就返回空
    }
}