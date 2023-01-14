package com.example.weatherbroadcast;

import static com.example.weatherbroadcast.DatabaseHelper.DbSchema.NAME;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DBNAME="Userdata.db";   //  创建数据库名

    public DatabaseHelper(Context context){
        super(context,DBNAME,null,VERSION);
    }

    //text前面一定要有空格！！！
    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("create table WeatherTable (_id INTEGER PRIMARY KEY AUTOINCREMENT,date text,max_temp text,min_temp text,weather text,humidity text,pressure text,wind text,icon text)");
        db.execSQL("create table "+ NAME+"("+
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    DbSchema.Cols.UUID+" text, "+
                    DbSchema.Cols.DATE+" text,"+
                    DbSchema.Cols.MAX_TEMP+" text,"+
                    DbSchema.Cols.MIN_TEMP+" text,"+
                    DbSchema.Cols.WEATHER+" text,"+
                    DbSchema.Cols.HUMIDITY+" text,"+
                    DbSchema.Cols.PRESSURE+" text,"+
                    DbSchema.Cols.WIND+" text,"+
                    DbSchema.Cols.ICON+" text"+
                    ")");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO
    }

    //内部类描述数据表
    public static final class DbSchema {
        public static final String NAME = "WeatherTab";
        //db.execSQL("create table WeatherTable (_id INTEGER PRIMARY KEY AUTOINCREMENT,date text,max_temp text,min_temp text,weather text,humidity text,pressure text,wind text,icon text)");
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String DATE = "date";
            public static final String MAX_TEMP = "max_temp";
            public static final String MIN_TEMP = "min_temp";
            public static final String WEATHER = "weather";
            public static final String HUMIDITY = "humidity";
            public static final String PRESSURE = "pressure";
            public static final String WIND = "wind";
            public static final String ICON = "icon";
        }

    }
}
