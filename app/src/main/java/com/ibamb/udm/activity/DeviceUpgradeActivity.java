package com.ibamb.udm.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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

import com.ibamb.dnet.module.log.UdmLog;
import com.ibamb.udm.R;
import com.ibamb.udm.beans.FtpServer;
import com.ibamb.udm.beans.UdmVersion;
import com.ibamb.udm.component.broadcast.InstallAPK;
import com.ibamb.udm.component.constants.UdmConstant;
import com.ibamb.udm.component.file.FileDirManager;
import com.ibamb.udm.component.file.FilePathParser;
import com.ibamb.udm.conf.DefaultConstant;
import com.ibamb.udm.service.DeviceUpgradeService;
import com.ibamb.udm.task.CheckForUpdatesAsyncTask;
import com.ibamb.udm.task.FileDownloadAysncTask;
import com.ibamb.udm.util.TaskBarQuiet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

public class DeviceUpgradeActivity extends AppCompatActivity implements DeviceUpgradeService.OnServiceProgressListener {

    private View currentView;

    private DeviceUpgradeService upgradeService;
    private UpgradeMsgReceiver msgReceiver;


    private Toolbar mToolbar;
    //提示信息
    private TextView versionInfo;
    //升级事件按钮
    private Button actionButton;

    public UdmVersion udmVersion;
    public FtpServer ftpServer;
    //service 连接
    private ServiceConnection serviceConnection;

    //本地更新包
    private String localUpdatePatch;

    private String localCheckFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+DefaultConstant.BASE_DIR+"/"
            + DefaultConstant.VERSION_CHECK_FILE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_upgrade);
        currentView = getWindow().getDecorView();
        //将ActionBar位置改放Toolbar.
        mToolbar = findViewById(R.id.upgrade_toolbar);
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
                if (item.getItemId() == android.R.id.home) {
                    finish();
                } else if (item.getItemId() == R.id.menu_upgrade_setting) {
                    Intent intent = new Intent(DeviceUpgradeActivity.this, UpdateSettingActivity.class);
                    startActivity(intent);
                }
//                else if (item.getItemId() == R.id.menu_import_update_patch) {
//                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                    intent.setType("*/*");
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                    startActivityForResult(intent, 1);
//                }
                return true;
            }
        });
        //修改任务栏背景颜色
        TaskBarQuiet.setStatusBarColor(this, UdmConstant.TASK_BAR_COLOR);
        try {

            int currentVesion = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            udmVersion = new UdmVersion();
            udmVersion.setCurrentVersionId(currentVesion);

        } catch (PackageManager.NameNotFoundException e) {
            UdmLog.error(e);
        }
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
                String[] content = null;
                FileDirManager fileDirManager = new FileDirManager(DeviceUpgradeActivity.this);
                File updateDir = fileDirManager.getFileByName("update");
                if (updateDir == null || !updateDir.exists()) {
                    updateDir = new File(getFilesDir() + "/update");
                    updateDir.mkdir();
                }
                if (actionButton.getText().toString().equals("Check for updates")) {

                    localUpdatePatch = null;
                    File f = new File(localCheckFileName);
                    if(f.exists()){
                        f.delete();
                    }
                    FileInputStream inputStream = null;
                    try {
                        inputStream = openFileInput("update-setting.txt");
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String line = null;
                        StringBuilder strBuffer = new StringBuilder();
                        while ((line = bufferedReader.readLine()) != null) {
                            strBuffer.append(line);
                        }
                        content = strBuffer.toString().split("#");
                        if (content.length > 3) {
                            String host = content[0];
                            String port = content[1];
                            String userName = content[2];
                            String password = content[3];
                            ftpServer = new FtpServer(host, port, userName, password);
                            CheckForUpdatesAsyncTask task = new CheckForUpdatesAsyncTask(
                                    DeviceUpgradeActivity.this, host, port, userName, password, updateDir);
                            task.execute();
                        }
                    } catch (FileNotFoundException e) {
                        UdmLog.error(e);
                        CheckForUpdatesAsyncTask task = new CheckForUpdatesAsyncTask(
                                DeviceUpgradeActivity.this,
                                DefaultConstant.FTP_HOST,
                                String.valueOf(DefaultConstant.FTP_PORT),
                                DefaultConstant.USER_NAME,
                                DefaultConstant.PASSWORD,
                                updateDir);
                        task.execute();
                    } catch (IOException e) {
                        UdmLog.error(e);
                    }
                } else if (actionButton.getText().toString().equals("Download update patch")) {
                    localUpdatePatch = null;
                    findViewById(R.id.upgrade_progress_bar).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.version_info)).setText("downloading...");
                    FileDownloadAysncTask task = new FileDownloadAysncTask(currentView,udmVersion);
                    String remoteApk = udmVersion.getApkFile();
                    String remoteUpdatePackage = udmVersion.getUpdatePackage();
                    task.execute(ftpServer.getHost(), ftpServer.getPort(), ftpServer.getUserName(), ftpServer.getPassword(),
                            remoteApk, remoteUpdatePackage);
                    actionButton.setClickable(false);

                } else if (actionButton.getText().toString().equals("Update")) {
                   if (udmVersion.getCurrentVersionId() < udmVersion.getVersionId()) {
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+
                                DefaultConstant.BASE_DIR+"/"+udmVersion.getApkFile();
                        InstallAPK.installApk(DeviceUpgradeActivity.this,path);
                    }else {
                        actionButton.setText("Restart");
                    }

                }else if(actionButton.getText().toString().equals("Restart")){
                    localUpdatePatch = null;
                    new Handler().postDelayed(new Runnable()  {
                        @Override public void run() {
                            Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(getApplication().getPackageName());
                            LaunchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(LaunchIntent);
                        }  }, 1000);// 1秒钟后重启应用
                }
            }
        });
        //最新版本信息
        versionInfo = findViewById(R.id.version_info);
    }

    /**
     * 获取UDM下载路径
     *
     * @return
     */
    private String getDownloadFilePath() {
        String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/udm";
        File f = new File(filepath);
        if (!f.exists()) {
            f.mkdir();
        }
        return filepath;
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
        //upgradeProgress.setText("success:" + progress);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(msgReceiver);
    }

    class UpgradeMsgReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String value = intent.getStringExtra("UPGRADE_COUNT");
            onProgressChanged(Integer.parseInt(value));
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            FilePathParser filePathParser = new FilePathParser();
            String path = "";
            if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                path = uri.getPath();
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                path = filePathParser.getPath(this, uri);
            } else {//4.4以下下系统调用方法
                path = filePathParser.getRealPathFromURI(uri, getContentResolver());
            }
            if (path != null && path.trim().length() > 0) {
                TextView textView = findViewById(R.id.file_path);
                File f = new File(path);
                textView.setText("Import File:"+f.getName());
                actionButton.setText("Update");
                localUpdatePatch = path;
            }
        }
    }
}
