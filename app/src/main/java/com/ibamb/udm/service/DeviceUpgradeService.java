package com.ibamb.udm.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.ibamb.udm.module.beans.DeviceInfo;
import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.module.file.FileDownLoad;
import com.ibamb.udm.module.task.UpgradeTask;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipFile;

public class DeviceUpgradeService extends Service {

    public ArrayList<DeviceInfo> deviceInfos;//升级的设备列表

    private ZipFile upradePatch;//升级包,Zip格式。

    private static boolean isRuning = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new UpgradeServiceBinder();
    }

    public interface OnServiceProgressListener {
        void onProgressChanged(int progress);
    }


    public class UpgradeServiceBinder extends Binder {
        public DeviceUpgradeService getService() {
            return DeviceUpgradeService.this;
        }
    }

    public void upgrade() {

        if (!isRuning) {
            isRuning = true;
            /**
             * 搜索可升级的设备
             */
            deviceInfos = new ArrayList<>();
            for(int i=0;i<100;i++){
                deviceInfos.add(new DeviceInfo("192.168.0.110",""));
            }
            if (deviceInfos != null && !deviceInfos.isEmpty()) {
                /**
                 * 下载升级包
                 */
                FileDownLoad downLoad = new FileDownLoad();
                downLoad.downFile("");
                //线程并发执行升级，并发数 5.
                LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
                ExecutorService pool = Executors.newFixedThreadPool(5);
                for (DeviceInfo device : deviceInfos) {
                    UpgradeTask upgradeTask = new UpgradeTask(broadcastManager,device, upradePatch);
                    pool.submit(upgradeTask);
                }
                pool.shutdown();
                //循环读取升级进度，所有设备都升级完后终止发广播。

//                while (true) {
//                    int count = 0;
//                    for (DeviceInfo device : deviceInfos) {
//                        if (device.getUpgradeCode() == Constants.UPGRADE_SUCCESS_CODE) {
//                            count++;
//                            //广播机制通信
//                            Intent intent = new Intent("com.ibamb.udm.service");
//                            intent.putExtra("UPGRADE_COUNT", String.valueOf(count));
//                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
//                            System.out.println("aaaaaaaaaa:"+count);
//                        }
//                    }
//                    if (count == deviceInfos.size()) {
//                        break;
//                    }
//                }
            }
        }
    }
}
