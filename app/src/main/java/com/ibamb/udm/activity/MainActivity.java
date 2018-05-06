package com.ibamb.udm.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.fragment.BlankFragment;
import com.ibamb.udm.fragment.DeviceSearchListFragment;
import com.ibamb.udm.listener.UdmBottomMenuClickListener;
import com.ibamb.udm.listener.UdmToolbarMenuClickListener;
import com.ibamb.udm.task.UdmInitAsyncTask;

import java.lang.reflect.Method;
import java.net.InetAddress;

/**
 * 应用程序主入口
 */
public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView tabDeviceList;
    private TextView tabSetting;


    private DeviceSearchListFragment f1;
    private BlankFragment f2, f3, f4;

    private InetAddress broadcastAddress;

    @Override
    protected void onStart() {
        super.onStart();
//        // 绑定Service，绑定后就会调用mConnetion里的onServiceConnected方法
//        Intent bindIntent = new Intent(MainActivity.this, DeviceSearchService.class);
//        bindService(bindIntent, connection, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //将ActionBar位置改放Toolbar.
        mToolbar = (Toolbar) findViewById(R.id.udm_toolbar);
        mToolbar.setTitle("udm");
        setSupportActionBar(mToolbar);

        //设置右上角的填充菜单
        mToolbar.inflateMenu(R.menu.tool_bar_menu);
        //这句代码使启用Activity回退功能，并显示Toolbar上的左侧回退图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //绑定菜单点击事件
        mToolbar.setOnMenuItemClickListener(new UdmToolbarMenuClickListener());

        //沉静式工具栏,将任务栏的背景改为与Toolbar背景一致.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        //默认显示第一个界面
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideAllFragment(transaction);
        if (f1 == null) {
            f1 = DeviceSearchListFragment.newInstance("第一个Fragment", null);
            transaction.add(R.id.fragment_container, f1);
            transaction.show(f1);
        } else {
            transaction.show(f1);

        }
        transaction.commit();
        //底部菜单绑定点击事件,实现界面切换.
        tabDeviceList = (TextView) this.findViewById(R.id.tab_device_list);
        tabSetting = (TextView) this.findViewById(R.id.tab_setting);
        UdmBottomMenuClickListener bottomMenuClickListener = new UdmBottomMenuClickListener(fragmentManager,f1,tabDeviceList,tabSetting);
        tabDeviceList.setOnClickListener(bottomMenuClickListener);
        tabSetting.setOnClickListener(bottomMenuClickListener);
        tabDeviceList.requestFocus();
        //初始化应用基础数据
        AssetManager mAssetManger = getAssets();
        UdmInitAsyncTask initAsyncTask = new UdmInitAsyncTask();
        initAsyncTask.execute(mAssetManger);
        //判断WIFI是否开启
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            /**
             * 判断WIFI是否连接,非WIFI网络下不搜索设备.
             */
            String wifiIp = "";
            if (networkInfo != null && networkInfo.isConnected()) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    int ipAddress = wifiInfo.getIpAddress();
                    wifiIp = ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
                            + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
                }
            }

        } catch (Exception e) {
            Log.e(this.getClass().getName(),e.getMessage());
        }
    }

    //重置所有文本的选中状态
    public void selected() {
        tabDeviceList.setSelected(false);
        tabSetting.setSelected(false);
    }

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction) {
        if (f1 != null) {
            transaction.hide(f1);
        }
        if (f2 != null) {
            transaction.hide(f2);
        }
        if (f3 != null) {
            transaction.hide(f3);
        }
        if (f4 != null) {
            transaction.hide(f4);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tool_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }
}
