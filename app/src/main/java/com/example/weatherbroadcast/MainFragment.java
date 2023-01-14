package com.example.weatherbroadcast;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;



public class MainFragment extends Fragment {

    private static final String TAG = "PhotoGalleryFragment";
    private RecyclerView mRecyclerView;
    private ImageView mWeatherImage;
    private TextView mDate_today;
    private TextView mMaxT;
    private TextView mMinT;
    private TextView mWeatherTody;
    private ImageView mTitleIcon;
    private WeatherLab mWeatherLab; //在onPostExecute()中实例化
    private Weather mTodayWeather; //在onPostExecute()中实例化
    private View view;
    private List<Weather> mItems = new ArrayList<>();
    private Callbacks mCallbacks;   //回调接口
    private String location = "长沙";       //记录当前的城市ID，如果有变化，需要刷新你页面
    private String temp_unit = "m";
    private String unit_text = "°";
    public MainFragment() {

    }

    //回调接口:需要托管activity实现
    public interface Callbacks{
        void onWeatherSelected(Weather weather);
    }

    //在fragment附加给activity时调用
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    //清空变量
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //实例化单例
        mWeatherLab = WeatherLab.get(getActivity());
        //添加Menu
        setHasOptionsMenu(true);
        //实现数据获取
        new FetchItemsTask().execute();
        //启动Notification服务
        Intent i = NotificationService.newIntent(getActivity());
        getActivity().startService(i);
        //调试：Notification是否能在后台启动
        NotificationService.setServiceAlarm(getActivity(),true);
    }

    //从Setting
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences pref = getActivity().getSharedPreferences("set", MODE_PRIVATE);
        String city = pref.getString("city","长沙");
        String unit = pref.getString("unit","m");
        String send = pref.getString("send","是");
        if(city!=location){     //在重新启动页面的时候，如果地址改变了，需要刷新
            location = city;
            new FetchItemsTask().execute();  //异步执行，获取网站上的json内容
            return;
        }
        if(unit!=temp_unit){     //在重新启动页面的时候，摄氏度/华氏度改变了，需要刷新
            temp_unit = unit;
            new FetchItemsTask().execute();  //异步执行，获取网站上的json内容
            return;
        }
        if (send=="是"){
            //开启后台服务，启动定时器，发送通知消息
            NotificationService.setServiceAlarm(getActivity(),true);
        }else{
            NotificationService.setServiceAlarm(getActivity(),false);
        }
    }

    /*
    onCreateView()
    1、相当于Activity中的onCreate()
    2、绑定视图
    3、实例化RecycleView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        //实例化RecycleView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.weather_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //updateUI();
        setupAdapter();
        return view;
    }

    private void setupAdapter() {
        if (isAdded()) {
            mRecyclerView.setAdapter(new WeatherAdapter(mItems));
        }
    }

    private int getIconId(Context mContext, String icon){
        int i=  getResources().getIdentifier(icon, "drawable", mContext.getPackageName()) ;
        if(i>0){
            Log.i("TAG","Success to get drawable resoure");
        }else{
            Log.i("TAG","Fail to get drawable resoure");
        }
        return i;
    }

    //实例化Mune
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_main,menu);
    }

    //响应菜单项选择事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_map:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri geoLocation = Uri.parse("geo:28.156198193290255,112.93207570910455?z=11");
                intent.setData(geoLocation);
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(intent);
                }
                return true;
            case R.id.action_setting:
                //启动设置界面
                Intent intent2 = new Intent(getActivity(),SettingActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /*
    ViewHolder类
    1、绑定list_item_weather布局
    2、实例化list_item_weather中的组件
    3、监听并响应点击事件
     */
    private class WeatherHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mWeatherTextView ;
        private TextView mDateTextView ;
        private TextView mMaxTextView ;
        private TextView mMinTextView ;
        private ImageView mImageView;
        private Weather mWeather;
        /*
        1、实例化组件
        2、设置监听
         */
        public WeatherHolder(LayoutInflater inflater,ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_weather,parent,false));
            //监听实现的关键
            itemView.setOnClickListener(this);
            mImageView=(ImageView) itemView.findViewById(R.id.rweather_image);
            mWeatherTextView = (TextView) itemView.findViewById(R.id.itemweather);
            mDateTextView = (TextView) itemView.findViewById(R.id.weather_date);
            mMaxTextView = (TextView) itemView.findViewById(R.id.rmax_Temprature);
            mMinTextView = (TextView) itemView.findViewById(R.id.rmin_Temprature);
        }

        @Override
        public void onClick(View v) {
            //跳转到细节视图
//            Intent intent = PagerActivity.newIntent(getActivity(), mWeather.getId());
//            startActivity(intent);
            //使用回调方法
            mCallbacks.onWeatherSelected(mWeather);
        }

        //Adapter中传入数据
        public void bind(Weather weather) {
            mWeather = weather;
            //设置组件数据
            mWeatherTextView.setText(mWeather.getWeather());
            mDateTextView.setText(mWeather.getDate());
            mMaxTextView.setText(mWeather.getMaxTemperature());
            mMinTextView.setText(mWeather.getMinTemperature());
            String icon2 = "a"+mWeather.getIconUrl();
            int id2 = getIconId(getContext(),icon2);
            Drawable drawable2 = getResources().getDrawable(id2);
            mImageView.setImageDrawable(drawable2);
        }
    }

    /*
    Adapter
    1、实例化单例WeatherLab
     */
    private class WeatherAdapter extends RecyclerView.Adapter<WeatherHolder> {

        private List<Weather> mWeathers = new ArrayList<>();

        //构造函数中的weathers由Fragment在onCreateView中实例化后传入
        public WeatherAdapter(List<Weather> weathers){
            //不显示今天的消息
            if(weathers.size()!=0)
                weathers.remove(0);
            mWeathers = weathers;
        }

        //当RecycleView需要新的ViewHolder显示列表项时调用。
        @Override
        public WeatherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //创建WeatherHolder
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new WeatherHolder(layoutInflater,parent);
        }

        //用于在指定位置显示数据。此方法用于更新itemView的内容，以反映给定位置的项。
        @Override
        public void onBindViewHolder(WeatherHolder holder, int position) {
            Weather weather = mWeathers.get(position);
            holder.bind(weather);
        }

        @Override
        public int getItemCount() {
            return mWeathers.size();
        }

    }

    //启动后台线程AsyncTask，实现数据获取
    private class FetchItemsTask extends AsyncTask<Void,Void,List<Weather>> {

        @SuppressLint("Range")
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected List<Weather> doInBackground(Void... params) {
            //如果没有网络连接，从数据库获取信息
            if(!isNetworkConnected(getActivity().getApplicationContext())){
                //Cursor cursor = mDatabase.query ("WeatherTable",null,null,null,null,null,null);
                if (mWeatherLab.isEmpty()) {   //如果数据库中没有数据
                    Log.e("DataBase","没有数据！！！");
                    return null;
                }
                else{
                    List<Weather> items = new ArrayList<>();
                    items = mWeatherLab.getWeathers();
//                    cursor.moveToFirst();
//                    while (cursor.moveToNext()){
//                        Weather item = new Weather();
//                        item.setDate(cursor.getString(cursor.getColumnIndex("date")));
//                        item.setMaxTemperature(cursor.getString(cursor.getColumnIndex("max_temp")));
//                        item.setMinTemperature(cursor.getString(cursor.getColumnIndex("min_temp")));
//                        item.setWeather(cursor.getString(cursor.getColumnIndex("weather")));
//                        item.setHumidity(cursor.getString(cursor.getColumnIndex("humidity")));
//                        item.setPressure(cursor.getString(cursor.getColumnIndex("pressure")));
//                        item.setWind(cursor.getString(cursor.getColumnIndex("wind")));
//                        item.setIconUrl(cursor.getString(cursor.getColumnIndex("icon")));
//                        items.add(item);
//                    }
//                    for(Weather item:items){
//                        cursor.moveToNext();
//                    }
//                    cursor.close();
                    return items;
                }
                }

            else{   //如果有网络连接
                List<Weather> items = new WeatherFetcher().fetchItems();
                //删除原来的数据库，将获取的数据放入数据库
                mWeatherLab.setWeathers(items);
//              //if(mDatabase)
//                mDatabase.execSQL("create table WeatherTable (_id INTEGER PRIMARY KEY AUTOINCREMENT,date text,max_temp text,min_temp text,weather text,humidity text,pressure text,wind text,icon text)");
//                for(Weather item:items){
//                    ContentValues values = new ContentValues();
//                    values.put("date",item.getDate());
//                    values.put("max_temp",item.getMaxTemperature());
//                    values.put("min_temp",item.getMinTemperature());
//                    values.put("weather",item.getWeather());
//                    values.put("humidity",item.getHumidity());
//                    values.put("pressure",item.getPressure());
//                    values.put("wind",item.getWind());
//                    values.put("icon",item.getIconUrl());
//                    Log.e("Insert value",item.getDate()+" "+item.getMaxTemperature()+" "+item.getMinTemperature()+" "+item.getWeather());
//                    mDatabase.insert("WeatherTable",null,values);
//                }

                return items;
            }
        }


        //在doInBackground()执行之后执行
        @Override
        protected void onPostExecute(List<Weather> items) {
            //实例化mWeatherLab
            mWeatherLab.setWeathers(items);
            mItems = items;
            //实例化mTodayWeather
            mTodayWeather = mItems.get(0);//第一条数据
//            .setContentText(pref.getString("city","长沙")+
//                    "  天气："+pref.getString("text","")+
//                    ", 最高温度："+pref.getString("max_temp","")+unit_text+
//                    ", 最低温度："+pref.getString("min_temp","")+unit_text)
            SharedPreferences.Editor editor = getContext().getSharedPreferences("set",MODE_PRIVATE).edit();
            editor.putString("text",mTodayWeather.getDate());
            editor.putString("max_temp",mTodayWeather.getMaxTemperature());
            editor.putString("min_temp",mTodayWeather.getMinTemperature());
            setupAdapter();
            updateView();
        }
    }

    private void updateView() {
        mWeatherImage = (ImageView) view.findViewById(R.id.weatherimage);
        String icon1 = "a"+mTodayWeather.getIconUrl();
        int id1 = getIconId(getContext(),icon1);
        Drawable drawable1 = getResources().getDrawable(id1);
        mWeatherImage.setImageDrawable(drawable1);
        mDate_today = (TextView) view.findViewById(R.id.date_today);
        mDate_today.setText(mTodayWeather.getDate());
        mMaxT = (TextView) view.findViewById(R.id.max_Temprature);
        mMaxT.setText(mTodayWeather.getMaxTemperature());
        mMinT = (TextView) view.findViewById(R.id.min_Temprature);
        mMinT.setText(mTodayWeather.getMinTemperature());
        mWeatherTody = (TextView) view.findViewById(R.id.weather_today);
        mWeatherTody.setText(mTodayWeather.getWeather());
        mTitleIcon = (ImageView) view.findViewById(R.id.iconImage);
        String iconTitle = "a"+"100";
        int idTitle = getIconId(getContext(),iconTitle);
        Drawable Titledrawable = getResources().getDrawable(idTitle);
        mTitleIcon.setImageDrawable(Titledrawable);
    }

    //判断网络是否连接
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetwork() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }

}