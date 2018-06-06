package com.ibamb.udm.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.ibamb.udm.module.beans.DeviceInfo;

import java.util.ArrayList;

public class DeviceUpgradeService extends Service {

    private ArrayList<DeviceInfo> deviceInfos;//待升级的设备列表
    private static boolean isRuning = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new UpgradeServiceBinder();
    }

    public interface OnServiceProgressListener {
        void onProgressChanged(int progress);
    }


    public class UpgradeServiceBinder extends Binder{
        public DeviceUpgradeService getService(){
            return DeviceUpgradeService.this;
        }
    }

    public void upgrade(){
        if(!isRuning){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    isRuning = true;
                    for (int i = 0; i < 100; i++) {
                        try {
                            //广播机制通信
                            Intent intent = new Intent("com.ibamb.udm.service");
                            intent.putExtra("extra_data", String.valueOf(i+1));
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    isRuning = false;
                }
            }).start();
        }
    }
}
