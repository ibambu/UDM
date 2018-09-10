package com.ibamb.udm.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.ibamb.dnet.module.beans.DeviceModel;
import com.ibamb.dnet.module.core.ContextData;
import com.ibamb.dnet.module.log.UdmLog;
import com.ibamb.udm.conf.DefaultConstant;
import com.ibamb.udm.task.UpgradeTask;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipFile;

public class DeviceUpgradeService extends Service {

    public ArrayList<DeviceModel> deviceInfos;//升级的设备列表
    private String updatePackageZip;


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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String deviceUpdatePackage = intent.getStringExtra("device-update-package");
        String deviceUpdateversion = intent.getStringExtra("device-update-version");
        upgrade(deviceUpdatePackage,deviceUpdateversion);
        return super.onStartCommand(intent, flags, startId);
    }

    public void upgrade(String updateZipPackageName,String deviceUpdateversion) {

        deviceInfos = ContextData.getInstance().getDataInfos();
        if (deviceInfos != null && !deviceInfos.isEmpty()) {
            //线程并发执行升级，并发数 5.
            updatePackageZip = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                    DefaultConstant.BASE_DIR + "/" + updateZipPackageName;
            try {
                ZipFile zipPackage = new ZipFile(new File(updatePackageZip));
                LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
                ExecutorService pool = Executors.newFixedThreadPool(5);
                String[] updateVersions = deviceUpdateversion.split(",");

                for (DeviceModel device : deviceInfos) {
                    /**
                     * 只有指定版本号的设备才升级
                     */
                    boolean isCanUpdate = false;
                    for(String updateVersion:updateVersions){
                        if(updateVersion.equalsIgnoreCase(device.getFirmwareVersion())){
                            isCanUpdate = true;
                            break;
                        }
                    }
                    UdmLog.info(" device "+device.getIp()+" can update:"+isCanUpdate);
                    if(isCanUpdate){
                        UpgradeTask upgradeTask = new UpgradeTask(zipPackage, device, broadcastManager);
                        pool.submit(upgradeTask);
                    }
                }
                pool.shutdown();
            } catch (Exception e) {
                UdmLog.error(e);
            }
        }
    }

}
