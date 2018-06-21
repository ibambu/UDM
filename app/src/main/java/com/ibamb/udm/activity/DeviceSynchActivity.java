package com.ibamb.udm.activity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import com.ibamb.udm.R;
import com.ibamb.udm.component.ServiceConst;
import com.ibamb.udm.module.beans.DeviceInfo;
import com.ibamb.udm.service.DeviceSynchronizeService;

import java.util.ArrayList;
import java.util.List;

public class DeviceSynchActivity extends AppCompatActivity {

    private DeviceSynchReceiver receiver;
    private ServiceConnection serviceConnection;
    private DeviceSynchronizeService synchronizeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_synch);
        /**
         * 注册 Service 服务
         */
        receiver = new DeviceSynchActivity.DeviceSynchReceiver();
        IntentFilter filter = new IntentFilter(ServiceConst.DEVICE_SYNCH_SERVICE);
        filter.addAction(ServiceConst.DEVICE_SYNCH_SERVICE);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, filter);
        /**
         * 绑定Service
         */
        onbindService();
        /**
         * 判断后台是否有Service正在执行同步，如果有Service正在执行同步，则显示同步进度。否则自动启动同步服务。
         */
        boolean isServiceRunning = isServiceRunning(this, ServiceConst.DEVICE_SYNCH_SERVICE);
        if (!isServiceRunning) {
            DeviceInfo templateDevice = null;
            ArrayList<DeviceInfo> targetDeviceList = null;
            synchronizeService.syncDeviceParam(templateDevice,targetDeviceList);
        }else{

        }
    }

    /**
     * 判断 Service 是否正在运行。
     * @param mContext
     * @param className
     * @return
     */
    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);

        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().contains(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * Service 广播接收者
     */
    class DeviceSynchReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String value = intent.getStringExtra("UPGRADE_COUNT");
            /**
             * 更新UI进度
             */

        }
    }

    /**
     * 绑定 Service
     */
    public boolean onbindService() {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                synchronizeService = ((DeviceSynchronizeService.DeviceSynchBinder) service).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        Intent intent = new Intent(this, DeviceSynchronizeService.class);
        return bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }


    @Override
    protected void onDestroy() {
        //销毁时解除绑定
        unbindService(serviceConnection);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(receiver);
        super.onDestroy();

    }
}
