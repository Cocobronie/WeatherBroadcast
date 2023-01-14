package com.example.weatherbroadcast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.UUID;

public class PagerActivity extends AppCompatActivity {

    private static final String EXTRA_WEATHER_ID = "com/example/weatherbroadcast/DetailActivity.java.weather_id";
    private ViewPager mViewPager;
    private List<Weather> mWeathers;

    //启动PagerActivity并传递参数
    public static Intent newIntent(Context packageContext, UUID weathereId) {
        Intent intent = new Intent(packageContext, PagerActivity.class);
        intent.putExtra(EXTRA_WEATHER_ID, weathereId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mWeathers = WeatherLab.get(this).getWeathers();

        //获取intent传入的信息
        UUID weatherId = (UUID) getIntent().getSerializableExtra(EXTRA_WEATHER_ID);

        //设置点击之后显示的界面
        for (int i = 0; i < mWeathers.size(); i++) {
            if (mWeathers.get(i).getId().equals(weatherId)) {
                mViewPager.setCurrentItem(i);
                break;
            }

            //创建Adapter
            FragmentManager fragmentManager = getSupportFragmentManager();
            mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
                @NonNull
                @Override
                public Fragment getItem(int position) {
                    Weather weather = mWeathers.get(position);
                    //实例化DetailFragment
                    return DetailFragment.newInstance(weather.getId());
                }

                @Override
                public int getCount() {
                    Log.e("mWeathers.size : ", String.valueOf(mWeathers.size()));
                    return mWeathers.size();
                }
            });
        }
    }
}
