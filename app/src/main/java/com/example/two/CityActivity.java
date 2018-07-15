package com.example.two;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.MenuItem;

import com.example.two.chinaCity.County;
import com.example.two.database.MyDataBaseHelper;
import com.example.two.fragment01.Fragment01;
import com.example.two.okhttp.RequestHttp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CityActivity extends AppCompatActivity {

    public static final int pagerProvince=0;
    public static final int pagerCity=1;
    public static final int pagerCounty=2;
    public static int PAGER_NOW=pagerProvince;//默认当前是省级视图
    public static int ITEM_POSITION=0;
    public static String WEATHER_ID="CN101010100";//北京的代号
    public static String cityRequest;//String类型 包含有city的url,在
    public List<String> nameList=new ArrayList<>();
    public List<Integer> idList =new ArrayList<>();
    public List<String> weatherList=new ArrayList<>();
    public ArrayAdapter adapter;

    public SharedPreferences pre;
    public SharedPreferences.Editor editor;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        listView=findViewById(R.id.listView_ActivityCity);
        adapter=new ArrayAdapter(CityActivity.this,android.R.layout.simple_list_item_1,nameList);//因为是最简单的Adapter，所以可以直接使用这种简单的设置适配器的方法
        listView.setAdapter(adapter);
        //显示出左上角的按钮
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        showProvinceData();

        //设置对ListView的Item的监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ITEM_POSITION=position;
                if (PAGER_NOW==pagerProvince){
                    showCityData();
                }else if (PAGER_NOW==pagerCity){
                    showCountyData();
                }else{
                    toastTemp();
                }
            }
        });

    }

    public void toastTemp(){
        pre= PreferenceManager.getDefaultSharedPreferences(CityActivity.this);
        editor=pre.edit();
        editor.putBoolean("isIntent",true);
        editor.putString("responseData",null);
        editor.apply();

        WEATHER_ID=weatherList.get(ITEM_POSITION);
        Intent intent=new Intent(CityActivity.this,MainActivity.class);
        intent.putExtra("weatherCityCode",WEATHER_ID);
//        intent.putExtra("isIntent",true);//传过去
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);//让MainActivity重新加载全部的生命周期
//        Toast.makeText(this,WEATHER_ID,Toast.LENGTH_SHORT).show();

        finish();
    }

    //监听Menu(返回按钮)
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch(menuItem.getItemId()){
            case android.R.id.home:
                if (PAGER_NOW==pagerCounty){
                    showCityData();
                }else if (PAGER_NOW==pagerCity){
                    showProvinceData();
                }else{
                    finish();
                }
                break;
        }

        return true;
    }

    private void showProvinceData(){
        MyDataBaseHelper helper=new MyDataBaseHelper(this,"weather.db",null,1);
        SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cursor=db.query("provinces",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            nameList.clear();
            idList.clear();
            Log.d("CityActivity","showProvinceData");
           do {
                int id=cursor.getInt(cursor.getColumnIndex("id"));
                String name=cursor.getString(cursor.getColumnIndex("name"));
                idList.add(id);
                nameList.add(name);
            } while(cursor.moveToNext());
            adapter.notifyDataSetChanged();
            PAGER_NOW=pagerProvince;
        }
        db.close();
        helper.close();
    }

    private void showCityData(){
        int fatherId=idList.get(ITEM_POSITION);
        cityRequest ="http://guolin.tech/api/china/"+String.valueOf(fatherId);//初始化Url供申请County的时候使用
        MyDataBaseHelper helper=new MyDataBaseHelper(this,"weather.db",null,1);
        SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cursor=db.query("cities",null,"fatherId = "+ fatherId,null,null,null,null);
        if (cursor.moveToFirst()){
            nameList.clear();
            idList.clear();
            Log.d("CityActivity","showCityData");
            do {
                int id=cursor.getInt(cursor.getColumnIndex("id"));
                String name=cursor.getString(cursor.getColumnIndex("name"));
                idList.add(id);
                nameList.add(name);
            }while(cursor.moveToNext());
            adapter.notifyDataSetChanged();
            PAGER_NOW=pagerCity;
        }
        db.close();
        helper.close();
    }

    private void showCountyData(){//由于全国的县的数据，数据库无法处理，报错，所以最后采用  用到就申请的方式
        final String request= cityRequest +"/"+String.valueOf(idList.get(ITEM_POSITION));
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestHttp.sendRequest(request, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String requestData=response.body().string();
                        Gson gson=new Gson();
                        List<County> countyList = gson.fromJson(requestData,new TypeToken<List<County>>(){}.getType());
                        idList.clear();
                        nameList.clear();
                        weatherList.clear();
                        for (County county:countyList){
                            idList.add(county.getId());
                            nameList.add(county.getName());
                            weatherList.add(county.getWeather_id());
                            Log.d("CityActivity","Thread");
                        }
                        //返回主线程刷新ListView
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("CityActivity","showCountyData");
                                adapter.notifyDataSetChanged();
                                PAGER_NOW=pagerCounty;

                            }
                        });

                    }
                });


            }
        }).start();

//        MyDataBaseHelper helper=new MyDataBaseHelper(this,"weather.db",null,1);
//        SQLiteDatabase db=helper.getWritableDatabase();
//        Cursor cursor=db.query("counties",null,"fatherId = "+ idList.get(ITEM_POSITION),null,null,null,null);
//        if (cursor.moveToFirst()){
//            nameList.clear();
//            idList.clear();
//           do {
//                int id=cursor.getInt(cursor.getColumnIndex("id"));
//                String name=cursor.getString(cursor.getColumnIndex("name"));
//                idList.add(id);
//                nameList.add(name);
//            } while(cursor.moveToNext());
//            adapter.notifyDataSetChanged();
//            PAGER_NOW=pagerCounty;
//        }
//        db.close();
//        helper.close();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("CityActivity","onDestroy");
    }
}