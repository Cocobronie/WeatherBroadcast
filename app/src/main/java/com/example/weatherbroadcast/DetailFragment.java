package com.example.weatherbroadcast;

import static com.example.weatherbroadcast.UtilFunc.getIconId;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DetailFragment extends Fragment {

    private ImageView mWeatherImage ;
    //private TextView mDay ;
    private TextView mMaxT ;
    private TextView mMinT;
    private TextView mHumidity ;
    private TextView mPressure ;
    private TextView mWind ;
    private TextView mDweather ;
    private TextView mDate ;
    private Weather mWeather;

    //ArgumentKey
    private static final String ARG_WEATHER_ID = "com/example/weatherbroadcast/DetailFragment.java.weather_id";

    //private Toolbar mToolbar;

    //mWeather必须初始化
    public DetailFragment() {

    }

    //创建DetailFragment的关键
    public static DetailFragment newInstance(UUID weatherid) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_WEATHER_ID,weatherid);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //添加Menu
        setHasOptionsMenu(true);
        //从Fragment的argument中获取ID
        UUID weatherid = (UUID) getArguments().getSerializable(ARG_WEATHER_ID);
        mWeather = WeatherLab.get(WeatherLab.getContext()).getWeather(weatherid);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        mDweather = (TextView) v.findViewById(R.id.dweather);
        mDate = (TextView) v.findViewById(R.id.detaildate);
        mWeatherImage = (ImageView) v.findViewById(R.id.dweather_image);
        //mDay = (TextView) v.findViewById(R.id.detailday);
        mMaxT = (TextView) v.findViewById(R.id.dmax_Temprature);
        mMinT = (TextView) v.findViewById(R.id.dmin_Temprature);
        mHumidity = (TextView) v.findViewById(R.id.humidity);
        mPressure = (TextView) v.findViewById(R.id.pressure);
        mWind = (TextView) v.findViewById(R.id.wind);

        //设置weather信息
        mDate.setText(mWeather.getDate());
        mDweather.setText(mWeather.getWeather());
        mDate.setText(mWeather.getDate());
        //mDay.setText(mWeather.getDate());
        mMaxT.setText(mWeather.getMaxTemperature());
        mMinT.setText(mWeather.getMinTemperature());
        mHumidity.setText("Humidity : "+mWeather.getHumidity());
        mPressure.setText("Pressure : "+mWeather.getPressure());
        mWind.setText("Wind : "+mWeather.getWind());
        String icon = "a"+mWeather.getIconUrl();
        int id = getIconId(getContext(),icon,this);
        Drawable drawable = getResources().getDrawable(id);
        mWeatherImage.setImageDrawable(drawable);

        //设置Toolbar
//        mToolbar = (Toolbar) v.findViewById(R.id.toolbar);
//        //在NoActionBar的主题中onCreateOptionsMenu方法不会运行，这里就需要将toolbar强制转换为ActionBar
//        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
//        mToolbar.inflateMenu(R.menu.fragment_detail);
//        mToolbar.setTitle("Title");
//        mToolbar.setSubtitle("SubTitle");
//        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                int id = item.getItemId();
////                if (id == R.id.menu_add_contact) {
////                    T.showToastBro(getActivity(), item.getTitle().toString());
////                }
////                if (id == R.id.menu_nearby_businesses) {
////                    T.showToastBro(getActivity(), item.getTitle().toString());
////                }
//                return true;
//            }
//        });
        return v;
    }

    //实例化Mune
    @Override
    public void onCreateOptionsMenu( Menu menu,  MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_detail,menu);
    }

    //响应菜单项选择事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
//            case R.id.action_map:
//                //启动MapActivity
////                Intent intent1 = new Intent(getActivity(),MapActivity.class);
////                startActivity(intent1);
////                //调用手机中安装的地图应用显示当前天气预报所对应的位置
////                if (isAvilible(getContext(), "com.autonavi.minimap")) {
////                    try {
////                        //sourceApplication
////                        Intent intent = Intent.getIntent("androidamap://navi?sourceApplication=公司的名称（随意写）&poiname=北京&lat=" + "39.90498" + "&lon=" + "116.40528" + "&dev=0");
////                        startActivity(intent);
////                    } catch (URISyntaxException e) {
////                        e.printStackTrace();
////                    }
////                } else {
////                    Log.e("U","您尚未安装高德地图或地图版本过低");
////                }
////                if (isAvilible(getContext(), "com.google.android.apps.maps")) {
////                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + "39.90498" + "," + "116.40528");
////                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
////                    mapIntent.setPackage("com.google.android.apps.maps");
////                    startActivity(mapIntent);
////                } else {
////                    Log.e("U","您尚未安装谷歌地图或地图版本过低");
////                }
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                Uri geoLocation = Uri.parse("geo:28.156198193290255,112.93207570910455?z=11");
//                intent.setData(geoLocation);
//                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
//                    startActivity(intent);
//                }
//                return true;
//            case R.id.action_setting:
//                //启动设置界面
//                Intent intent2 = new Intent(getActivity(),SettingActivity.class);
//                startActivity(intent2);
//                return true;
            case R.id.share:
                //Intent.ACTION_SEND:要执行的操作
                Intent i = new Intent(Intent.ACTION_SEND);
                //操作涉及的数据类型
                i.setType("text/plain");
                //待访问数据的位置
                i.putExtra(Intent.EXTRA_TEXT,createMessage());
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public String createMessage(){
        String message = "";
        message += "今天的天气状况为："+mWeather.getWeather();
        message += "    今天的最高温度是： "+mWeather.getMinTemperature();
        message += "    今天的最低温度是： "+mWeather.getMinTemperature();
        message += "    今天的湿度为： "+mWeather.getHumidity();
        message += "    今天的风速为："+mWeather.getWind();
        message += "    今天的气压为："+mWeather.getPressure();
        message += "    希望您拥有美好的一天!";
        return message;
    }

//    private boolean isAvilible(Context context, String packageName) {
//        //获取packagemanager
//        final PackageManager packageManager = context.getPackageManager();
//        //获取所有已安装程序的包信息
//        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
//        //用于存储所有已安装程序的包名
//        List<String> packageNames = new ArrayList<String>();
//        //从pinfo中将包名字逐一取出，压入pName list中
//        if (packageInfos != null) {
//            for (int i = 0; i < packageInfos.size(); i++) {
//                String packName = packageInfos.get(i).packageName;
//                packageNames.add(packName);
//            }
//        }
//        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
//        return packageNames.contains(packageName);
//    }

}