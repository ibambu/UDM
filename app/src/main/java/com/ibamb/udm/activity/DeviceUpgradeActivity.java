package com.ibamb.udm.activity;

import android.annotation.SuppressLint;
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
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.dnet.module.constants.Constants;
import com.ibamb.udm.service.DeviceUpgradeService;
import com.ibamb.udm.task.CheckForUpdatesAsyncTask;
import com.ibamb.udm.util.TaskBarQuiet;

import java.lang.reflect.Method;

public class DeviceUpgradeActivity extends AppCompatActivity implements DeviceUpgradeService.OnServiceProgressListener{

    private View currentView;

    private DeviceUpgradeService upgradeService;
    private UpgradeMsgReceiver msgReceiver;


    private Toolbar mToolbar;

    //升级事件按钮
    private Button actionButton;
    //版本信息
    private TextView versionInfo;
    //最新版本
    private int newVersion;
    //当前版本
    private int currentVesion;

    //service 连接
    private ServiceConnection serviceConnection ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_upgrade);
        currentView = getWindow().getDecorView();
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
        //修改任务栏背景颜色
        TaskBarQuiet.setStatusBarColor(this, Constants.TASK_BAR_COLOR);

        /**
         * 注册 Service 服务
         */
        msgReceiver = new UpgradeMsgReceiver();
        IntentFilter filter = new IntentFilter("com.ibamb.udm.service");
        filter.addAction("com.ibamb.udm.service.RECEIVER");
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(msgReceiver, filter);
        //绑定 service 服务
        onBindService();

        actionButton = findViewById(R.id.action_button);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (versionInfo.getText().toString()==null||versionInfo.getText().toString().trim().length()==0) {
                    CheckForUpdatesAsyncTask task = new CheckForUpdatesAsyncTask(currentView);
                    task.execute();
                }else{
                    upgradeService.upgrade();
                }
            }
        });
        //最新版本信息
        versionInfo = findViewById(R.id.version_info);
    }


    public void onBindService() {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                upgradeService = ((DeviceUpgradeService.UpgradeServiceBinder) service).getService();
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        Intent intent = new Intent(this, DeviceUpgradeService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onProgressChanged(int progress) {
        //更新UI进度
        TextView upgradeProgress = findViewById(R.id.upgrade_progress);
        upgradeProgress.setVisibility(View.VISIBLE);
        upgradeProgress.setText("Success:"+progress);
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
            String value = intent.getStringExtra("UPGRADE_COUNT");
            onProgressChanged(Integer.parseInt(value));
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
