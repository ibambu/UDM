package com.ibamb.udm.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.lang.reflect.Method;

public class DeviceUpgradeActivity extends AppCompatActivity implements DeviceUpgradeService.OnServiceProgressListener{

    private ImageView search;
    private ImageView back;
    private TextView title;

    private DeviceUpgradeService upgradeService;
    private UpgradeMsgReceiver msgReceiver;

    private Intent mIntent;
    private ListView mListView;

    private Toolbar mToolbar;

    private TextView upgradeProgress;
    private TextView vSearchNotice;

    private ProgressBar progressBar;

    private Button upgradeButton;

    private ServiceConnection serviceConnection ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_upgrade);

        //将ActionBar位置改放Toolbar.
        mToolbar =  findViewById(R.id.upgrade_toolbar);
        setSupportActionBar(mToolbar);

        //设置右上角的填充菜单
        mToolbar.inflateMenu(R.menu.upgrade_setting_menu);
        //这句代码使启用Activity回退功能，并显示Toolbar上的左侧回退图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()== android.R.id.home){
                    finish();
                }
                return true;
            }
        });

        TaskBarQuiet.setStatusBarColor(this, Constants.TASK_BAR_COLOR);//修改任务栏背景颜色

        /**
         * 注册Service
         */
        msgReceiver = new UpgradeMsgReceiver();
        IntentFilter filter = new IntentFilter("com.ibamb.udm.service");
        filter.addAction("com.ibamb.udm.service.RECEIVER");
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(msgReceiver, filter);
        //绑定服务
        onBindService();
//
//
//        back = findViewById(R.id.go_back);
//
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        title = findViewById(R.id.title);
//        title.setText(Constants.TITLE_DEVICE_UPGRADE);
//
//        search = findViewById(R.id.do_search);
//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TextView vSearchNotice = findViewById(R.id.search_notice_info);
////                SearchUpgradeDeviceAsycTask task = new SearchUpgradeDeviceAsycTask(mListView,vSearchNotice,getLayoutInflater());
////                task.execute();
//            }
//        });
//        progressBar = findViewById(R.id.upgrade_progress_bar);
//        progressBar.setMax(100);
////        ClipDrawable d = new ClipDrawable(new ColorDrawable(Color.YELLOW), Gravity.LEFT,ClipDrawable.HORIZONTAL);
////        progressBar.setProgressDrawable(d);
//
//        vSearchNotice = findViewById(R.id.search_notice_info);
//
//        upgradeProgress = findViewById(R.id.upgrade_progress);
//        upgradeButton = findViewById(R.id.btn_upgrade_all);
//        upgradeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onCommunication(null);
//                DeviceUpgradeAsyncTask upgradeTask = new DeviceUpgradeAsyncTask(mListView);
//                upgradeTask.execute();
//            }
//        });

    }


    public void onBindService() {
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
//        title.setText(Constants.TITLE_DEVICE_UPGRADE+"("+progress+")");
//        progressBar.setProgress(progress);
//        ((TextView)findViewById(R.id.upgrade_report)).setText("Success:"+progress);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(msgReceiver);
    }

    class UpgradeMsgReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String value = intent.getStringExtra("extra_data");
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.upgrade_setting_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

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

}
