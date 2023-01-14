package com.example.weatherbroadcast;

import static android.app.PendingIntent.FLAG_IMMUTABLE;
import static android.app.PendingIntent.FLAG_MUTABLE;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.concurrent.TimeUnit;

/*
轮询搜索结果
 */
public class NotificationService extends IntentService {
    private static final String TAG = "IntentService";
    //设置时间间隔是1分钟
    private static final long NOTI_INTERVAL_MS = TimeUnit.MINUTES.toMillis(1);
    public static void setServiceAlarm(Context context,boolean isOn){
        Intent i = NotificationService.newIntent(context);
        //创建一个用来启动Notification服务的PendingIntent
        PendingIntent pendingIntent = PendingIntent.getService(context,0,i,FLAG_MUTABLE);
        //设置定时器
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(isOn){
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),NOTI_INTERVAL_MS,pendingIntent);
        }
        else{
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }
    /*
    使用PendingIntent管理定时器
     */
    //1、判断定时器的启停
    public static boolean isServiceAlarmOn(Context context){
        Intent intent = NotificationService.newIntent(context);
        PendingIntent pendingIntent = PendingIntent.getService(context,0,intent,PendingIntent.FLAG_NO_CREATE);
        return pendingIntent!=null;
    }
    //2、

    public NotificationService() {
        super(TAG);
    }

    /*
    响应intent，提供服务
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.e(TAG,"接收一个intent："+intent);
    /*
    设置通知消息
     */
        Resources resources = getResources();
        Intent i = MainActivity.newIntent(this);
        //FLAG_IMMUTABLE：不可变的PendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,FLAG_IMMUTABLE);
        Notification notification = null;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setTicker(resources.getString(R.string.new_weather_title))
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(resources.getString(R.string.new_weather_title))
                .setContentText(resources.getString(R.string.new_weather_text))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
//        notificationManagerCompat.notify(0,notification);

        NotificationManager notificationManager =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //从Android8.0开始，应用显示通知时，必须为通知指定一个Channel。
            NotificationChannel channel = new NotificationChannel("to-do"   //ChannelId是自定义的字符串
                    , "待办消息",   //频道的名称
                    NotificationManager.IMPORTANCE_HIGH);   //优先级
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{500});
            //创建完NotificationChannel之后，还需要使用createNotificationChannels方法注册到系统中
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId("to-do");
            notification = builder.build();
        notificationManager.notify(0,notification);
        System.out.println(notification);
    }

    public static Intent newIntent(Context context){
        return new Intent(context, NotificationService.class);
    }


}
