package com.ibamb.udm.myapp;

import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.ibamb.udm.service.CheckVersionIntentService;

import java.util.List;

import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;

public class UdmApplication extends Application {

    private static final String TAG = UdmApplication.class.getName();
    /**
     * 全局缓存
     */
    private SharedPreferences udmContext;
    private NetworkChangeReceiver networkChangeReceiver;


    @Override
    public void onCreate() {
        super.onCreate();

        udmContext = getSharedPreferences("UDM_CONTEXT", MODE_PRIVATE);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver(networkChangeReceiver);
    }

    public SharedPreferences getUdmContext() {
        return udmContext;
    }

    /**
     * 网络状态监控
     */
    class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                switch (networkInfo.getType()) {
                    case TYPE_MOBILE:
//                        Toast.makeText(context, "正在使用2G/3G/4G网络", Toast.LENGTH_SHORT).show();
                        break;
                    case TYPE_WIFI:
//                        Toast.makeText(context, "正在使用wifi上网", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }

                if (!isServiceRunning(context, CheckVersionIntentService.class.getName())) {
                    Log.i(TAG, "CHECK VERSION SERVICE IS NOT RUN.");
                    CheckVersionIntentService.startActionCheckVersion(context, "YDOWN");
                } else {
                    Log.i(TAG, "CHECK VERSION SERVICE IS RUNNING.");
                }

            } else {
                Toast.makeText(context, "当前无网络连接", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static boolean isServiceRunning(Context context, String serviceName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = am.getRunningServices(200);
        if (runningServiceInfos.isEmpty()) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo serviceInfo : runningServiceInfos) {
            if (serviceInfo.service.getClassName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }


}
