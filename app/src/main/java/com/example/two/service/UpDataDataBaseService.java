package com.example.two.service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import com.example.two.database.MyDataBaseHelper;
import com.example.two.gson.ParserJSONWithGSON;
import com.example.two.okhttp.RequestHttp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UpDataDataBaseService extends Service {//用服务来更新数据库


    List<Integer> cityFatherIdList =new ArrayList<>(),
            countyFatherIdList =new ArrayList<>();

    public UpDataDataBaseService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate(){
        deleteDataBase();
        upDataDataBase();
    }

    public void upDataDataBase(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //先请求城市数据

//                List<Provinces> provincesList;
//                List<City> cityList;
//                List<County> countyList;



                final String provinceUrl="http://guolin.tech/api/china";
                RequestHttp.sendRequest(provinceUrl, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String provincesData=response.body().string();
                        ParserJSONWithGSON.provinceParserJsonResponse(UpDataDataBaseService.this,provincesData);

                        //一下查询数据库用来给City用来for循环
                        MyDataBaseHelper helper=new MyDataBaseHelper(UpDataDataBaseService.this,"weather.db",null,1);
                        SQLiteDatabase db=helper.getWritableDatabase();
                        Cursor cursor=db.query("provinces",null,null,null,null,null,null);
                        if (cursor.moveToFirst()){
                            do{
                                int fatherId=cursor.getInt(cursor.getColumnIndex("id"));
                                cityFatherIdList.add(fatherId);
                                Log.d("Tag",String.valueOf(fatherId));
                            }while(cursor.moveToNext());
                        }
                        db.close();
                        helper.close();

                        for (int i = 0; i< cityFatherIdList.size(); i++){
//                            Log.d("Tag","CityFor循环");
                            final int cityFatherId= cityFatherIdList.get(i);//当前循环的city的fatherCode,用于传参
                            final String cityUrl=provinceUrl+"/"+String.valueOf(cityFatherId);
                            RequestHttp.sendRequest(cityUrl, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.d("Tag","CityUrl错误");
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String responseData=response.body().string();//将response转化为string;
                                    ParserJSONWithGSON.cityParserJsonResponse(UpDataDataBaseService.this,responseData,cityFatherId);

                                    MyDataBaseHelper helper=new MyDataBaseHelper(UpDataDataBaseService.this,"weather.db",null,1);
                                    SQLiteDatabase db=helper.getWritableDatabase();
                                    Cursor cursor=db.query("cities",null,null,null,null,null,null);
                                    if (cursor.moveToFirst()){
                                        countyFatherIdList.clear();
                                        do {
                                            int fatherId=cursor.getInt(cursor.getColumnIndex("id"));
                                            countyFatherIdList.add(fatherId);

                                        }while(cursor.moveToNext());
                                    }
                                    db.close();
                                    helper.close();

                                    //因为县的数量太多了，每一个都存到数据库会报错
//                                    new Thread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            for (int j = 0; j< countyFatherIdList.size(); j++){//用来将所有的县的数据插入到数据库
//                                                final int countyFatherId= countyFatherIdList.get(j);
//                                                String countyUrl=cityUrl+"/"+String.valueOf(countyFatherId);
//                                                RequestHttp.sendRequest(countyUrl, new Callback() {
//                                                    @Override
//                                                    public void onFailure(Call call, IOException e) {
//                                                    }
//
//                                                    @Override
//                                                    public void onResponse(Call call, Response response) throws IOException {
//                                                        String responseData=response.body().string();
//                                                        ParserJSONWithGSON.countyParserJsonResponse(UpDataDataBaseService.this,responseData,countyFatherId);
//                                                    }
//                                                });
//                                            }
//                                        }
//                                    }).start();

                                }
                            });



                        }

                    }
                });



            }
        }).start();

    }

    public void deleteDataBase(){
        MyDataBaseHelper helper=new MyDataBaseHelper(UpDataDataBaseService.this,"weather.db",null,1);
        SQLiteDatabase db=helper.getWritableDatabase();
        db.delete("provinces",null,null);
        db.delete("cities",null,null);
        db.delete("counties",null,null);
        db.close();
        helper.close();
    }
}
