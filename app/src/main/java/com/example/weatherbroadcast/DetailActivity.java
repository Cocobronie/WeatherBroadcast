package com.example.weatherbroadcast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.UUID;

public class DetailActivity extends AppCompatActivity {
    private static final String EXTRA_WEATHER_ID = "com/example/weatherbroadcast/DetailActivity.java.weather_id";

    //启动DetailActivity并传递参数
    public static Intent newIntent(Context packageContext, UUID weathereId) {
        Intent intent = new Intent(packageContext, DetailActivity.class);
        intent.putExtra(EXTRA_WEATHER_ID, weathereId);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //绑定Fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container3);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container3, fragment)
                    .commit();
        }

}
    //创建Fragment
    private Fragment createFragment() {
        //获取intent传入的信息
        UUID weatherId = (UUID) getIntent().getSerializableExtra(EXTRA_WEATHER_ID);
        return DetailFragment.newInstance(weatherId);
    }
    }