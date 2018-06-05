package com.ibamb.udm.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

public class DeviceUpgradeService extends Service {

    private OnServiceProgressListener listener;
    private Handler handler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new UpgradeServiceBinder();
    }

    public interface OnServiceProgressListener {
        void onProgressChanged(int progress);
    }

    public void setOnServiceProgressChangedListener(OnServiceProgressListener listener) {
        this.listener = listener;
    }

    public class UpgradeServiceBinder extends Binder{
        public DeviceUpgradeService getService(){
            return DeviceUpgradeService.this;
        }
    }

    public void upgrade(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        handler.sendEmptyMessage(i);
                       //广播机制通信
                        Intent intent = new Intent("com.ibamb.udm.service");
                        intent.putExtra("extra_data", String.valueOf(i));
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        handler.sendEmptyMessage(100);
        System.out.println("upgrade.....");
    }

    @Override
    public void onCreate() {
        //使用mainlooper 确保在UI线程执行
        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (listener != null) {
                    listener.onProgressChanged(msg.what);
                }
            }
        };
    }


}
