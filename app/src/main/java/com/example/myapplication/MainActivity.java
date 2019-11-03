package com.example.myapplication;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.apeng.permissions.EsayPermissions;
import com.apeng.permissions.OnPermission;
import com.apeng.permissions.Permission;
import com.example.myapplication.fragment.MainFragment;
import com.example.myapplication.service.PackageNameService;
import com.example.myapplication.service.WindowChangeDetectingService;
import com.example.myapplication.weight.TabLayoutIndictor;
import com.example.myapplication.weight.TabLayoutIndictor2;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends AppCompatActivity implements WindowChangeDetectingService.callback {
    private ViewPager mVpMain;
    private List<MainFragment> mFeagmentList = new ArrayList<>();
    private TabLayoutIndictor mTliMain;
    private TabLayoutIndictor2 mTlMain2;

    //view2

    private Switch mSwitch;

    private WindowManager wm;
    private WindowManager.LayoutParams layoutParams;
    private View mItemView;
    private TextView mTvName1;
    private TextView mTvName2;
    private Intent intent;
    //    private boolean isRun = false;
    public static boolean isOpen = false;
    private static final int ACTION_MANAGE_OVERLAY_PERMISSION = 1002;
    ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initView2();
//        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isOpen) {
            mSwitch.setChecked(true);
        }
    }

    private void initView() {
        mVpMain = (ViewPager) findViewById(R.id.vp_main);
        mTliMain = (TabLayoutIndictor) findViewById(R.id.tli_main);
        mTlMain2 = (TabLayoutIndictor2) findViewById(R.id.tl_main);
        for (int i = 0; i < 3; i++) {
            mFeagmentList.add(new MainFragment());
        }
        mVpMain.setAdapter(new MainFragmentAdapter(getSupportFragmentManager()));
        mTliMain.setViewPager(mVpMain);
        mTlMain2.setupWithViewPager(mVpMain);
        mTlMain2.setViewPager2(mVpMain);

    }

    @Override
    public void onChange(String packagename, String activityname) {
        if (isOpen) {
            mTvName1.setText(packagename);
            mTvName2.setText(activityname);
        }
    }

    private class MainFragmentAdapter extends FragmentPagerAdapter {

        public MainFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFeagmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFeagmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "项目" + position;
        }
    }

    private void initView2() {
        mSwitch = (Switch) findViewById(R.id.sw_open);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mItemView = inflater.inflate(R.layout.item_name_view, null);

        mTvName1 = (TextView) mItemView.findViewById(R.id.tv_name1);
        mTvName2 = (TextView) mItemView.findViewById(R.id.tv_name2);
        WindowChangeDetectingService.setCallback(this);
        intent = new Intent(MainActivity.this, PackageNameService.class);
        mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSwitch.isChecked()) {
                    if (EsayPermissions.isHasPermission(MainActivity.this, Permission.SYSTEM_ALERT_WINDOW)) {
                        // 已经申请过权限，做想做的事
                        run();
                    } else {
                        // 没有申请过权限，现在去申请
                        getPermission();
                    }
                } else {
                    wm.removeView(mItemView);
                    isOpen = false;
                }
            }
        });
    }


    private void setName() {

        wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        //设置TextView的属性
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
        String shortClassName = info.topActivity.getShortClassName();    //类名
        String className = info.topActivity.getClassName();              //完整类名
        String packageName = info.topActivity.getPackageName();


        mTvName1.setText(packageName);
        mTvName2.setText(className);


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
        wm.addView(mItemView, layoutParams);
    }

    //申请权限
    private void getPermission() {
        EsayPermissions.with(MainActivity.this)
                .constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                //支持请求6.0悬浮窗权限8.0请求安装权限
                .permission(Permission.SYSTEM_ALERT_WINDOW)
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
//                                    if (isAll) {
//                                        Tools.showToast(context,"获取权限成功");
//                                    }else {
//                                        Tools.showToast(context,"获取权限成功，部分权限未正常授予");
//                                    }
                        run();
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            Toast.makeText(MainActivity.this, "被永久拒绝授权，请手动授予权限", Toast.LENGTH_SHORT).show();
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            EsayPermissions.gotoPermissionSettings(MainActivity.this);
                        } else {
                            Toast.makeText(MainActivity.this, "获取权限失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void run() {
        isOpen = true;
        if (!isAccessibilitySettingsOn(this)) {
            //跳转到服务权限开启页面
            getAccessibilityPermission();
        }
        setName();
    }

    private boolean getAccessibilityPermission() {
        Log.e("321", "getAccessibilityPermission");
        Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
        return false;
    }


    private boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        // TestService为对应的服务
        final String service = getPackageName() + "/" + WindowChangeDetectingService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        } else {
        }
        return false;
    }
}
