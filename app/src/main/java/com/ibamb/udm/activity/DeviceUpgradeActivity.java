package com.ibamb.udm.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.service.DeviceUpgradeService;
import com.ibamb.udm.task.DeviceUpgradeAsyncTask;
import com.ibamb.udm.task.SearchUpgradeDeviceAsycTask;
import com.ibamb.udm.util.TaskBarQuiet;

public class DeviceUpgradeActivity extends AppCompatActivity implements DeviceUpgradeService.OnServiceProgressListener{

    private DeviceUpgradeService upgradeService;
    private UpgradeMsgReceiver msgReceiver;
    private Intent mIntent;


    private ImageView search;
    private ImageView back;
    private TextView title;
    private TextView upgradeProgress;
    private TextView vSearchNotice;

    private Button upgradeButton;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
                onProgressChanged(msg.what);
        }
    };

    private ServiceConnection serviceConnection ;

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_upgrade);
        TaskBarQuiet.setStatusBarColor(this, Constants.TASK_BAR_COLOR);

        /**
         * 注册Service
         */
        msgReceiver = new UpgradeMsgReceiver();
        IntentFilter filter = new IntentFilter("com.ibamb.udm.service");
        filter.addAction("com.ibamb.udm.service.RECEIVER");
//        registerReceiver(msgReceiver, filter);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(msgReceiver, filter);


        mListView = findViewById(R.id.upgrade_device_list);
        back = findViewById(R.id.go_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title = findViewById(R.id.title);
        title.setText(Constants.TITLE_DEVICE_UPGRADE);

        search = findViewById(R.id.do_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView vSearchNotice = findViewById(R.id.search_notice_info);
                SearchUpgradeDeviceAsycTask task = new SearchUpgradeDeviceAsycTask(mListView,vSearchNotice,getLayoutInflater());
                task.execute();
            }
        });

        vSearchNotice = findViewById(R.id.search_notice_info);

        upgradeProgress = findViewById(R.id.upgrade_progress);
        upgradeButton = findViewById(R.id.btn_upgrade_all);
        upgradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCommunication(null);
//                DeviceUpgradeAsyncTask upgradeTask = new DeviceUpgradeAsyncTask(mListView);
//                upgradeTask.execute();
            }
        });
        onBindService(null);
    }


    public void onBindService(View view) {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                upgradeService = ((DeviceUpgradeService.UpgradeServiceBinder) service).getService();
                System.out.println("get service instance:"+upgradeService);

            }
            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        Intent intent = new Intent(this, DeviceUpgradeService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    public void onCommunication(View view) {
        upgradeService.upgrade();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onProgressChanged(int progress) {
        //更新UI进度
        title.setText(String.valueOf(progress));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
//        unregisterReceiver(msgReceiver);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(msgReceiver);
    }
    class UpgradeMsgReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String value = intent.getStringExtra("extra_data");
            title.setText(value);
            System.out.println("sssssssssssssssss=="+value);
        }
    }
}
