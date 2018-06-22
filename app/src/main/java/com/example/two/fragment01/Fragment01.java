package com.example.two.fragment01;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.two.R;
import com.example.two.gson.ParserJSONWithGSON;
import com.example.two.okhttp.RequestHttp;
import com.example.two.weather.jsonentity.Weather;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment01 extends Fragment {

    View mView;
    Context mContext;
    List<ListViewItem> listViewItemList=new ArrayList<>();
    ListAdapter adapter;
    ListView listView;
    TextView textView_nowTemperature;
    TextView textView_nowInformation;
    TextView textView_nowTemperatureSection;
    TextView textView_pm25;


    public static String weatherCityCode="CN101010100";
    public static String responseData;
    public static boolean isIntent=false;

    private SharedPreferences pre;
    private SharedPreferences.Editor editor;

    public Fragment01() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle saveInstancesState){
        super.onCreate(saveInstancesState);
        Log.d("Fragment01","onCreate");
        pre= PreferenceManager.getDefaultSharedPreferences(getContext());
        responseData=pre.getString("responseData",null);
        if (isIntent==true){
            Intent intent=getActivity().getIntent();
            weatherCityCode=intent.getStringExtra("weatherCityCode");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Fragment01","onCreateViewStart");

        // Inflate the layout for this fragment
        mView=inflater.inflate(R.layout.fragment_fragment01, container, false);
        mContext=mView.getContext();

        listView=mView.findViewById(R.id.listView_Fragment01);
        textView_nowTemperature =mView.findViewById(R.id.nowTemperature);
        textView_nowInformation =mView.findViewById(R.id.nowInformation);
        textView_nowTemperatureSection =mView.findViewById(R.id.nowTemperatureSection);
        textView_pm25 =mView.findViewById(R.id.pm25);
//        SwipeRefreshLayout swipeRefresh=(SwipeRefreshLayout)mView.findViewById(R.id.swipe_refresh);
//        //下拉刷新监听
//        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                updateWeather();
//            }
//        });

        if (responseData!=null){
            Log.d("Fragment01","responseData!=null");
            showWeather(responseData);
        }else{
            Log.d("Fragment01","responseData==null");
            updateWeather();
        }

        Log.d("Fragment01","onCreateViewEnd");
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);
        Log.d("Fragment01","onActivityCreated");
        adapter=new ListAdapter(mContext,R.layout.listview_item,listViewItemList);//可以先给一个空的List，在后面填充数据后再刷新adpter就可以了
        listView.setAdapter(adapter);
        int height=0;
        int listItemCount=adapter.getCount();
        for (int i=0;i<listItemCount;i++){
            //得到每一个ListView的每一个Item的view
            View listItem=adapter.getView(i,null,listView);
            //计算每一个Item的高度
            listItem.measure(0,0);
            //用getMeasuredHeight()得到刚刚测量的值然后和前一项相加
            height +=listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params=listView.getLayoutParams();
        //listView.getDividerHeight()获取ListView子项间分隔符占用的高度
        params.height=height+(listView.getDividerHeight()*(listItemCount-1));
        listView.setLayoutParams(params);
    }

    public void updateWeather(){

        String addressWeather="http://guolin.tech/api/weather?cityid="+weatherCityCode+"&key=33e7b04afdff480699f84ec64f1ed12b";
        RequestHttp.sendRequest(addressWeather, new Callback() {//封装了开启线程语句
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                responseData=response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeather(responseData);
                    }
                });
            }
        });


    }

    public void showWeather(String responseDataShow){
        Log.d("weather","hello");

        Weather weatherShow = ParserJSONWithGSON.weatherParserJsonResponse(responseDataShow);
        Log.d("weather",weatherShow.basic.update.updateTime);
        listViewItemList.clear();
//        Log.d("Fragmentweather",String.valueOf(weatherShow.forecastList.size()));
        for (int i=0;i<weatherShow.forecastList.size();i++){
            String date=weatherShow.forecastList.get(i).futureDate;
            String information=weatherShow.forecastList.get(i).futureInformation.info;
            String temperature=weatherShow.forecastList.get(i).futureTemperature.max+"/"+weatherShow.forecastList.get(i).futureTemperature.min;
            ListViewItem listViewItem=new ListViewItem(date,information,temperature);
            listViewItemList.add(listViewItem);
        }
//        adapter.notifyDataSetChanged();

        Log.d("LogTextView",weatherShow.now.temperature);
        textView_nowTemperature.setText(weatherShow.now.temperature);//设置当前温度
        Log.d("LogTextView","one");
        textView_nowInformation.setText(weatherShow.now.information.info);
        Log.d("LogTextView","two");

        textView_nowTemperatureSection.setText(weatherShow.forecastList.get(0).futureTemperature.max+"/"+weatherShow.forecastList.get(0).futureTemperature.min);
        Log.d("LogTextView","three");

        textView_pm25.setText(weatherShow.aqi.cityAQI.pm25);
        Log.d("LogTextView","four");

    }



    @Override
    public void onResume() {
        super.onResume();
        Log.d("Fragment01","onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Fragment01","onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Fragment01","onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Fragment01","onDestroy");
        editor=pre.edit();
        editor.putString("responseData",responseData);
        editor.apply();
    }

//    public void setListViewHeight(ListView listView){
//
//    }

}
