package com.example.two;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.two.fragment01.Fragment01;
import com.example.two.fragment02.Fragment02;
import com.example.two.service.UpDataDataBaseService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
//    static {
//        System.loadLibrary("native-lib");
//    }
    public static ActionBar actionBar;
    List<Fragment> fragmentList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity","onCreate");
        actionBar=getSupportActionBar();
        final ViewPager viewPager=findViewById(R.id.viewPager01);

        initFragmentList();
        MyFragmentPagerAdapter fragmentPagerAdapter=new MyFragmentPagerAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(fragmentPagerAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //启动服务加载写入数据库
        Intent intent=new Intent(MainActivity.this, UpDataDataBaseService.class);
        startService(intent);


    }

    private void initFragmentList(){
        fragmentList.add(new Fragment01());
        fragmentList.add(new Fragment02());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch(menuItem.getItemId()){
            case R.id.chooseCity:
                Intent intent=new Intent(MainActivity.this,CityActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onNewIntent(Intent intent){
        super.onNewIntent(intent);
    }
    @Override
    public void onDestroy(){//在退出程序时结束服务，
        super.onDestroy();
        Intent intent=new Intent(MainActivity.this,UpDataDataBaseService.class);
        stopService(intent);
    }

//    @Override
//    public boolean onKeyDown(int keyCode,KeyEvent keyEvent){
//        if (keyCode==KeyEvent.KEYCODE_BACK){
//            System.exit(0);
//        }
//        return true;
//    }
}
