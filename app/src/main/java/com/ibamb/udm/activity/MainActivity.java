package com.ibamb.udm.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.fragment.BlankFragment;
import com.ibamb.udm.fragment.DeviceSearchListFragment;
import com.ibamb.udm.net.LocalNetScanner;
import com.ibamb.udm.service.DeviceSearchService;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * main prog entry
 */
public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    private TextView tabDeviceList;
    private TextView tabOther1;
    private TextView tabOther2;
    private TextView tabSetting;


    private DeviceSearchListFragment f1;
    private BlankFragment f2, f3, f4;

    private InetAddress broadcastAddress;
    /**
     * 存储搜索到的设备列表
     */
    private ArrayList<InetAddress> inetAddresses = new ArrayList<>();

    private DeviceSearchService.DeviceSearchServiceBinder searchServiceBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            searchServiceBinder = (DeviceSearchService.DeviceSearchServiceBinder) service;
        }
    };
    /**
     * 菜单点击事件,切换到菜单对应的界面Fragment.
     */

    private View.OnClickListener menuOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            hideAllFragment(transaction);
            switch (v.getId()) {
//                case 0:
//                    Intent intent = new Intent(MainActivity.this, DeviceParamSettingActivity.class);
//                    startActivity(intent);
                case R.id.tab_device_list:
                    selected();
                    tabDeviceList.setSelected(true);
                    if (f1 == null) {
                        f1 = DeviceSearchListFragment.newInstance("第一个Fragment", null);
                        transaction.add(R.id.fragment_container, f1);
                    } else {
                        transaction.show(f1);
                    }
                    break;

                case R.id.tab_other1:
                    selected();
                    tabOther1.setSelected(true);
                    if (f2 == null) {
                        f2 = BlankFragment.newInstance("第四个Fragment", null);
                        transaction.add(R.id.fragment_container, f2);
                    } else {
                        transaction.show(f2);
                    }
                    break;

                case R.id.tab_other2:
                    selected();
                    tabOther2.setSelected(true);
                    if (f3 == null) {
                        f3 = BlankFragment.newInstance("第三个Fragment", null);
                        transaction.add(R.id.fragment_container, f3);
                    } else {
                        transaction.show(f3);
                    }
                    break;

                case R.id.tab_setting:
                    selected();
                    tabSetting.setSelected(true);
                    if (f4 == null) {
                        f4 = BlankFragment.newInstance("第四个Fragment", null);
                        transaction.add(R.id.fragment_container, f4);
                    } else {
                        transaction.show(f4);
                    }
                    break;
            }
            transaction.commit();
        }
    };

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



    @Override
    protected void onStart() {
        super.onStart();
        // 绑定Service，绑定后就会调用mConnetion里的onServiceConnected方法
        Intent bindIntent = new Intent(MainActivity.this, DeviceSearchService.class);
        bindService(bindIntent, connection, Context.BIND_AUTO_CREATE);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        bindView();

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
                    wifiIp = ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "." + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
                }
            }
            LocalNetScanner scanner = new LocalNetScanner();
            broadcastAddress = scanner.findBroadCastAddress(wifiIp);

        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        if (f1 == null) {
            f1 = DeviceSearchListFragment.newInstance("第一个Fragment", null);
            transaction.add(R.id.fragment_container, f1);
            transaction.show(f1);
        } else {
            transaction.show(f1);

        }
        transaction.commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tool_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    /**
     * UI组件初始化与事件绑定
     */
    private void bindView() {


        tabDeviceList = (TextView) this.findViewById(R.id.tab_device_list);
        tabOther1 = (TextView) this.findViewById(R.id.tab_other1);
        tabOther2 = (TextView) this.findViewById(R.id.tab_other2);
        tabSetting = (TextView) this.findViewById(R.id.tab_setting);


        tabDeviceList.setOnClickListener(menuOnClickListener);
        tabOther1.setOnClickListener(menuOnClickListener);
        tabOther2.setOnClickListener(menuOnClickListener);
        tabSetting.setOnClickListener(menuOnClickListener);

        toolbar = (Toolbar) findViewById(R.id.udm_toolbar);
        toolbar.setTitle("udm");
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.tool_bar_menu);//设置右上角的填充菜单
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//这句代码使启用Activity回退功能，并显示Toolbar上的左侧回退图标
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.id_menu_user_profile) {


                } else if (menuItemId == R.id.id_menu_or_code) {


                } else if (menuItemId == R.id.id_menu_join_cloud) {

                }
                return true;
            }
        });

        tabDeviceList.requestFocus();

    }


    //重置所有文本的选中状态
    public void selected() {
        tabDeviceList.setSelected(false);
        tabOther1.setSelected(false);
        tabOther2.setSelected(false);
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

    /**
     * 在设备列表的Fragment需要调用此方法刷新设备列表.
     *
     * @return
     */
    public ArrayList<InetAddress> getInetAddresses() {
        inetAddresses = searchServiceBinder.searchDeviceByBroadcast(broadcastAddress);
        return inetAddresses;
    }

}
