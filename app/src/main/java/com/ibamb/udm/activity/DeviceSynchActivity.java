package com.ibamb.udm.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.component.ServiceConst;
import com.ibamb.udm.module.beans.DeviceInfo;
import com.ibamb.udm.module.core.ContextData;
import com.ibamb.udm.service.DeviceSynchronizeService;

import java.util.ArrayList;
import java.util.Arrays;

public class DeviceSynchActivity extends AppCompatActivity {

    private DeviceSynchReceiver receiver;
    private ServiceConnection serviceConnection;
    private DeviceSynchronizeService synchronizeService;

    private ProgressBar progressBar;
    private TextView progressInfo;

    private Button actionButton;
    private Button selectDeviceButton;

    private String[] seletedDevices;
    private DeviceInfo templateDevice ;

    private ImageView goback;
    private TextView title;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_synch);

        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        String ip = params.getString("HOST_ADDRESS");
        String mac = params.getString("HOST_MAC");

        (findViewById(R.id.do_commit)).setVisibility(View.GONE);//不现实右边打勾图表

        progressBar = findViewById(R.id.sync_progress_bar);

        progressInfo = findViewById(R.id.sync_progress_info);
        progressInfo.setText("0/" + ContextData.getInstance().getCheckedItems());

        templateDevice = new DeviceInfo(ip,mac);

        actionButton = findViewById(R.id.action_button);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seletedDevices==null||seletedDevices.length==0){
                    Snackbar.make(v,  "No Device Seleted!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    synchronizeService.syncDeviceParam(templateDevice, seletedDevices);
                    actionButton.setClickable(false);
                    actionButton.setText("In Sync...");
                    selectDeviceButton.setClickable(false);
                }
            }
        });

        selectDeviceButton = findViewById(R.id.select_button);
        selectDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DeviceListActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        goback = findViewById(R.id.go_back);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title = findViewById(R.id.title);
        title.setText("Synchronize to Other Device");


        /**
         * 注册 Service 服务
         */
        receiver = new DeviceSynchActivity.DeviceSynchReceiver();
        IntentFilter filter = new IntentFilter(ServiceConst.DEVICE_SYNCH_SERVICE);
        filter.addAction(ServiceConst.DEVICE_SYNCH_SERVICE_RECEIVER);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, filter);
        /**
         * 绑定Service
         */
        onbindService();

    }

    public void onProgressChanged(int progress) {
        //更新UI进度
        progressBar.setProgress(progress);
        progressInfo.setText(progress + "/" + progressBar.getMax());
        if (progress == progressBar.getMax()) {
            actionButton.setClickable(true);
            actionButton.setText("Start Sync");
            selectDeviceButton.setClickable(true);
        } else {
            actionButton.setClickable(false);
            selectDeviceButton.setClickable(false);
            actionButton.setText("In sync...");
        }
    }

    /**
     * Service 广播接收者
     */
    class DeviceSynchReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int syncCount = intent.getIntExtra("SYNCH_COUNT",0) ;
            int targetCount = intent.getIntExtra("TARGET_DEVICE_NUMBER",0);
            onProgressChanged(syncCount);
            progressBar.setMax(targetCount);
        }
    }

    /**
     * 绑定 Service
     */
    public void onbindService() {
        try {
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
            bindService(intent, serviceConnection, BIND_AUTO_CREATE);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onDestroy() {
        //销毁时解除绑定
        super.onDestroy();
        unbindService(serviceConnection);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(receiver);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            int seletedCount = data.getIntExtra("SELECTED_COUNT",0);
            seletedDevices = data.getStringArrayExtra("SELECTED_DEVICE");
            progressBar.setMax(seletedCount);
            progressInfo.setText("0/"+seletedCount);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
