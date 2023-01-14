package com.example.weatherbroadcast;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

public class UtilFunc {
    //获取drawable图标资源的id
    public static int getIconId(Context mContext, String icon, Fragment fragment){
        //用getIdentifier()方法根据资源名来获取资源id
        //第一个参数为资源ID名，第二个为资源属性的类型，第三个为包名
        int i=  fragment.getResources().getIdentifier(icon, "drawable", mContext.getPackageName()) ;
        if(i>0){
            Log.i("TAG","Success to get drawable resoure");
        }else{
            Log.i("TAG","Fail to get drawable resoure");
        }
        return i;
    }
}
