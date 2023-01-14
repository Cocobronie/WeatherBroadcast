package com.example.weatherbroadcast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements MainFragment.Callbacks{
    private static final String EXTRA_WEATHER_ID = "com/example/weatherbroadcast.weather_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masterdetail);
        //获取FragmentManager
        FragmentManager fm = getSupportFragmentManager();
        if(fm.findFragmentById(R.id.fragment_container)==null){
            fm.beginTransaction()   //创建一个Fragment事务
                    .add(R.id.fragment_container,new MainFragment())   //执行一个Fragment添加操作// 此处的R.id.fragment_container是要盛放fragment的父容器
                    .commit();//提交该事务
        }
    }

    /*
        实现其托管fragment的回调接口
            1、如果是Phone：启动新的PagerActivity
            2、如果是paid：将DetailFragment放入detailFragmentContainer中
     */
    @Override
    public void onWeatherSelected(Weather weather) {
        if (findViewById(R.id.detailFragmentContainer) == null) {   //Phone
           Intent intent = PagerActivity.newIntent(this,weather.getId());
           startActivity(intent);
        }
        else                                                        //Paid
        {
            Fragment newDetail = DetailFragment.newInstance(weather.getId());
            getSupportFragmentManager().beginTransaction().replace(R.id.detailFragmentContainer,newDetail).commit();
        }
    }

    //NotificationService会调用这个方法，把结果封装在一个PendingIntent中设置给通知消息
    public static Intent newIntent(Context context){
        return new Intent(context,MainFragment.class);
    }
}
