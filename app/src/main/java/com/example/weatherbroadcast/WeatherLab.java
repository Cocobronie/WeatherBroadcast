package com.example.weatherbroadcast;

import static com.example.weatherbroadcast.DatabaseHelper.DbSchema.NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//public class WeatherLab {
//    private static WeatherLab sWeatherLab;
//    private List<Weather> mWeathers;
//
//    public static WeatherLab get(Context context){
//        if(sWeatherLab==null){
//            sWeatherLab=new WeatherLab(context);
//        }
//        return sWeatherLab;
//    }
//    public void addWeather(Weather weather){
//        mWeathers.add(weather);
//    }
//
//    public void setWeathers(List<Weather> weathers) {
//        mWeathers = weathers;
//    }
//
//    private WeatherLab(Context context){
//        mWeathers = new ArrayList<>();
//
//        //先创建几个调试一下
////        for(int i=0;i<11;i++){
////            Weather weather = new Weather();
////            weather.setWeather("晴");
////            weather.setDate("Tomorrow");
////            weather.setMaxTemperature("3℃");
////            weather.setMinTemperature("0℃");
////            mWeathers.add(weather);
////        }
//
//    }
//
//    public List<Weather> getWeathers(){
//        return mWeathers;
//    }
//
//    public Weather getWeather(UUID id){
//        for(Weather weather:mWeathers){
//            if(weather.getId().equals(id)){
//                return weather;
//            }
//        }
//        return null;
//    }
//
//}

public class WeatherLab {
    private static WeatherLab sWeatherLab;
    private SQLiteDatabase mDatabase;
    private Context mContext;

    //单例的意思：在类里已经实例化了
    public static WeatherLab get(Context context) {
        if (sWeatherLab == null) {
            sWeatherLab = new WeatherLab(context);
        }
        return sWeatherLab;
    }

    public static Context getContext(){
        return sWeatherLab.mContext;
    }

    private WeatherLab(Context context) {
        mContext = context.getApplicationContext();
        //打开数据库，如果不存在就先调用DataBaseHelper.onCreate()创建
        mDatabase = new DatabaseHelper(mContext).getWritableDatabase();

    }

//    //判断数据库是否为空
    public boolean isEmpty(){
        WeatherCursorWrapper cursor = queryWeathers(null, null);
        return cursor.getCount()==0||cursor==null;
    }

    //设置Weathers,删除原来的数据库，将获取的数据放入数据库
    public void setWeathers(List<Weather> weathers) {
        //如果数据库不为空
        if(!sWeatherLab.isEmpty()){
            mDatabase.execSQL("drop table "+DatabaseHelper.DbSchema.NAME);
            mDatabase.execSQL("create table "+ NAME+"("+
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    DatabaseHelper.DbSchema.Cols.UUID+" text, "+
                    DatabaseHelper.DbSchema.Cols.DATE+" text,"+
                    DatabaseHelper.DbSchema.Cols.MAX_TEMP+" text,"+
                    DatabaseHelper.DbSchema.Cols.MIN_TEMP+" text,"+
                    DatabaseHelper.DbSchema.Cols.WEATHER+" text,"+
                    DatabaseHelper.DbSchema.Cols.HUMIDITY+" text,"+
                    DatabaseHelper.DbSchema.Cols.PRESSURE+" text,"+
                    DatabaseHelper.DbSchema.Cols.WIND+" text,"+
                    DatabaseHelper.DbSchema.Cols.ICON+" text"+
                    ")");
        }

        for(Weather w:weathers){
            addWeather(w);
        }
    }

    //向数据库中插入一条记录
    public void addWeather(Weather w) {
        ContentValues values = getContentValues(w);
        mDatabase.insert(DatabaseHelper.DbSchema.NAME, null, values);
    }

    //直接从WeatherLab中获得List<Weather>
    public List<Weather> getWeathers() {
        List<Weather> weathers = new ArrayList<>();
        WeatherCursorWrapper cursor = queryWeathers(null, null);
        if(cursor==null){
            Log.e("cursor==null","没有数据表！！");
            return null;
        }
        else{
            try {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    weathers.add(cursor.getWeather());
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
            return weathers;
        }
    }

    //遍历查找weather
    public Weather getWeather(UUID id) {
        WeatherCursorWrapper cursor = queryWeathers(
                DatabaseHelper.DbSchema.Cols.UUID + " = ?",
                new String[]{String.valueOf(id)}
        );
        try {
            if (cursor.getCount() == 0) {
                Log.e("cursor.getCount() == 0","数据库没有数据");
                return null;
            }
            cursor.moveToFirst();
            return cursor.getWeather();
        } finally {
            cursor.close();
        }
    }

    //更新数据库记录
    public void updateWeather(Weather weather) {
        String uuid = weather.getId().toString();
        ContentValues values = getContentValues(weather);
        mDatabase.update(DatabaseHelper.DbSchema.NAME, values,
                DatabaseHelper.DbSchema.Cols.UUID + " = ?",
                new String[]{uuid});
    }

    //查询数据库：
    private WeatherCursorWrapper queryWeathers(String whereClause, String[] whereArgs) {

        Cursor cursor = mDatabase.query(
                DatabaseHelper.DbSchema.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new WeatherCursorWrapper(cursor);
    }

    //ContentValues:键值储存类，只能处理SQLite数据
    private static ContentValues getContentValues(Weather weather) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.DbSchema.Cols.UUID, weather.getId().toString());
        values.put(DatabaseHelper.DbSchema.Cols.DATE, weather.getDate());
        values.put(DatabaseHelper.DbSchema.Cols.MAX_TEMP, weather.getMaxTemperature());
        values.put(DatabaseHelper.DbSchema.Cols.MIN_TEMP, weather.getMinTemperature());
        values.put(DatabaseHelper.DbSchema.Cols.WEATHER, weather.getWeather());
        values.put(DatabaseHelper.DbSchema.Cols.HUMIDITY, weather.getHumidity());
        values.put(DatabaseHelper.DbSchema.Cols.PRESSURE, weather.getPressure());
        values.put(DatabaseHelper.DbSchema.Cols.WIND, weather.getWind());
        values.put(DatabaseHelper.DbSchema.Cols.ICON, weather.getIconUrl());
        return values;
    }

    //创建Cursor子类用于查询
    public class WeatherCursorWrapper extends CursorWrapper {

        public WeatherCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public Weather getWeather() {
            String UUID = getString(getColumnIndex(DatabaseHelper.DbSchema.Cols.UUID));
            String DATE = getString(getColumnIndex(DatabaseHelper.DbSchema.Cols.DATE));
            String MAX_TEMP = getString(getColumnIndex(DatabaseHelper.DbSchema.Cols.MAX_TEMP));
            String MIN_TEMP = getString(getColumnIndex(DatabaseHelper.DbSchema.Cols.MIN_TEMP));
            String WEATHER = getString(getColumnIndex(DatabaseHelper.DbSchema.Cols.WEATHER));
            String HUMIDITY = getString(getColumnIndex(DatabaseHelper.DbSchema.Cols.HUMIDITY));
            String PRESSURE = getString(getColumnIndex(DatabaseHelper.DbSchema.Cols.PRESSURE));
            String WIND = getString(getColumnIndex(DatabaseHelper.DbSchema.Cols.WIND));
            String ICON = getString(getColumnIndex(DatabaseHelper.DbSchema.Cols.ICON));

            Weather weather = new Weather();
            weather.setId(java.util.UUID.fromString(UUID));
            weather.setDate(DATE);
            weather.setMaxTemperature(MAX_TEMP);
            weather.setMinTemperature(MIN_TEMP);
            weather.setWeather(WEATHER);
            weather.setHumidity(HUMIDITY);
            weather.setPressure(PRESSURE);
            weather.setWind(WIND);
            weather.setIconUrl(ICON);

            return weather;
        }
    }



}

