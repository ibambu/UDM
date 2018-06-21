package com.ibamb.udm.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.ibamb.udm.module.beans.DeviceInfo;
import com.ibamb.udm.module.beans.RetMessage;
import com.ibamb.udm.module.task.UpgradeTask;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeviceSynchronizeService extends Service {
    public DeviceSynchronizeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new DeviceSynchBinder();
    }

    /**
     * Binder 返回一个Service实例
     */
    public class DeviceSynchBinder extends Binder {
        public DeviceSynchronizeService getService() {
            return DeviceSynchronizeService.this;
        }
    }

    public RetMessage syncDeviceParam(DeviceInfo templateDevice, ArrayList<DeviceInfo> targetDeviceList){
        RetMessage retMessage = new RetMessage();
        try{
            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
            ExecutorService pool = Executors.newFixedThreadPool(5);
//            for (DeviceInfo device : deviceInfos) {
//                UpgradeTask upgradeTask = new UpgradeTask(broadcastManager,device, upradePatch);
//                pool.submit(upgradeTask);
//            }
//            pool.shutdown();
            Intent intent = new Intent("com.ibamb.udm.service");
            intent.putExtra("UPGRADE_COUNT", String.valueOf(1));
            broadcastManager.sendBroadcast(intent);
        }catch (Exception e){

        }finally {

        }
        return retMessage;
    }
}
