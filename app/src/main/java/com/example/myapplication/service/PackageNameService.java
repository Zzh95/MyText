package com.example.myapplication.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.List;

public class PackageNameService extends Service implements WindowChangeDetectingService.callback{
    ActivityManager activityManager = null;
    Handler handler = new Handler();
    String activityName = null;
    String activity_last = null;


    private WindowManager wm;
    private WindowManager.LayoutParams layoutParams;
    private View mItemView;
    private TextView mTvName1;
    private TextView mTvName2;
    public static boolean isOpen = false;


    @Override
    public void onChange(String packagename, String activityname) {
        isOpen = true;
        Log.e("123", "执行了onChange");
        mTvName1.setText("Package：" + packagename);
        mTvName2.setText("Class：" + activityname);
//        wm.updateViewLayout(mItemView, layoutParams);
    }
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
//            packageRetriever.get();
            List<ActivityManager.RunningTaskInfo> runningTaskInfo = activityManager.getRunningTasks(1);
            activityName = (runningTaskInfo.get(0).topActivity).toString();
            ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
            String shortClassName = info.topActivity.getShortClassName();    //类名
            String className = info.topActivity.getClassName();              //完整类名
            String packageName = info.topActivity.getPackageName();

            Intent intent = new Intent();
            intent.setAction("unorderbroadcast");
            intent.putExtra("state", "open");
            sendBroadcast(intent);
            intent.putExtra("state", "open");
            mTvName1.setText(className);
            mTvName2.setText(shortClassName);
            Log.e("123", "执行了run");
            Log.e("123", "执行了activityName：" + activityName);
            if (!(activityName.equals(activity_last))) {
                activity_last = activityName;
                Log.e("123", "执行了setName");
                wm.updateViewLayout(mItemView, layoutParams);

            }
            handler.postDelayed(runnable, 10);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.postDelayed(runnable, 10);
//        Log.e("123", "执行了onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        setName();
        WindowChangeDetectingService.setCallback(this);
        activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {
//        handler.removeCallbacks(runnable);
////        Log.e("123", "执行了onDestroy");
        wm.removeView(mItemView);
        isOpen=false;
    }

    private void setName() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mItemView = inflater.inflate(R.layout.item_name_view, null);

        mTvName1 = (TextView) mItemView.findViewById(R.id.tv_name1);
        mTvName2 = (TextView) mItemView.findViewById(R.id.tv_name2);

        wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        //设置TextView的属性
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //这里是关键，使控件始终在最上方
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        layoutParams.format = PixelFormat.TRANSLUCENT;
//        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //这个Gravity也不能少，不然的话，下面"移动歌词"的时候就会出问题了～ 可以试试[官网文档有说明]
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
        String shortClassName = info.topActivity.getShortClassName();    //类名
        String className = info.topActivity.getClassName();              //完整类名
        String packageName = info.topActivity.getPackageName();

        Intent intent = new Intent();
        intent.setAction("unorderbroadcast");
        intent.putExtra("state", "open");
        sendBroadcast(intent);
        intent.putExtra("state", "open");
        mTvName1.setText(className);
        mTvName2.setText(shortClassName);
        wm.addView(mItemView, layoutParams);
    }
}
