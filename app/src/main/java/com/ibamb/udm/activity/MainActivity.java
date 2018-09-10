package com.ibamb.udm.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ibamb.dnet.module.constants.Constants;
import com.ibamb.dnet.module.core.TryUser;
import com.ibamb.dnet.module.log.UdmLog;
import com.ibamb.dnet.module.security.DefualtECryptValue;
import com.ibamb.dnet.module.security.ICryptStrategy;
import com.ibamb.udm.R;
import com.ibamb.udm.component.constants.UdmConstant;
import com.ibamb.udm.component.file.FileDirManager;
import com.ibamb.udm.component.file.FilePathParser;
import com.ibamb.udm.component.guide.MainActivityGuide;
import com.ibamb.udm.component.security.AESCrypt;
import com.ibamb.udm.component.security.PermissionUtils;
import com.ibamb.udm.conf.DefaultConstant;
import com.ibamb.udm.conf.Log;
import com.ibamb.udm.guide.guideview.Guide;
import com.ibamb.udm.guide.guideview.GuideBuilder;
import com.ibamb.udm.task.DeviceSearchAsyncTask;
import com.ibamb.udm.task.SearchUpgradeDeviceAsycTask;
import com.ibamb.udm.task.UdmInitAsyncTask;
import com.ibamb.udm.util.TaskBarQuiet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

/**
 * 应用程序主入口
 */
public class MainActivity extends AppCompatActivity {

    private TextView tabDeviceList;
    private TextView tabSetting;

    private Guide guide;

    private EditText vSearchKeyword;
    private ImageView vSearchIcon;
    private String showGuideFlag = "1";

    @Override
    protected void onStart() {
        super.onStart();
        FileInputStream inputStream = null;
        FileInputStream versionFileIn = null;
        FileDirManager fileDirManager = new FileDirManager(this);
        try {
            String udmDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                    DefaultConstant.BASE_DIR  +"/";
            File baseDir = new File(udmDir);
            if(!baseDir.exists()){
                baseDir.mkdir();
            }
            File runErrFile = new File(udmDir + UdmConstant.FILE_UDM_RUN_ERR_LOG);
            if (!runErrFile.exists()) {
                runErrFile.createNewFile();
            }
            UdmLog.setErrorLogFile(runErrFile);
            StringBuilder strbuffer = new StringBuilder();

            File tryUesrFile = fileDirManager.getFileByName(UdmConstant.TRY_USER_FILE);
            if (tryUesrFile != null) {
                inputStream = openFileInput(UdmConstant.TRY_USER_FILE);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    strbuffer.append(line);
                }
                ICryptStrategy aes = new AESCrypt();
                String content = aes.decode(strbuffer.toString(), DefualtECryptValue.KEY);
                String[] tryUsers = content.split("&");
                TryUser.setTryUser(tryUsers);
            }
            //初始化应用基础数据
            UdmInitAsyncTask initAsyncTask = new UdmInitAsyncTask(this);
            initAsyncTask.execute();

            //搜索并自动升级
            SearchUpgradeDeviceAsycTask task = new SearchUpgradeDeviceAsycTask(this);
            task.execute();

        } catch (Exception e) {
            UdmLog.error(e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (versionFileIn != null) {
                    versionFileIn.close();
                }
            } catch (IOException e) {
                UdmLog.error(e);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TaskBarQuiet.setStatusBarColor(this, UdmConstant.TASK_BAR_COLOR);//修改任务栏背景颜色

        if (PermissionUtils.isGrantExternalRW(this, 1)) {

        }

        //底部菜单绑定点击事件,实现界面切换.
        tabDeviceList = findViewById(R.id.tab_device_list);
        tabSetting = findViewById(R.id.tab_setting);
//        tabDeviceList.requestFocus();
//        tabDeviceList.setSelected(true);

        ImageView popMenuIcon = findViewById(R.id.pop_menu);
        popMenuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });

        vSearchKeyword = findViewById(R.id.search_keyword);
        /**
         * 软键盘中的搜索事件
         */
        vSearchKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchDevice();
                }
                return false;
            }
        });

        vSearchIcon = findViewById(R.id.search_icon);
        /**
         * 搜索图标的搜索事件
         */
        vSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDevice();
            }
        });
        /**
         * 判断是否显示新手指引
         */
        FileInputStream inputStream1 = null;
        try {
            FileDirManager fileDirManager = new FileDirManager(this);
            File guideFile = fileDirManager.getFileByName(UdmConstant.FILE_GUIDE_CONF);
            if (guideFile != null) {
                inputStream1 = openFileInput(UdmConstant.FILE_GUIDE_CONF);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream1));
                showGuideFlag = bufferedReader.readLine();
            }
            /*if("1".equals(showGuideFlag)){
                vSearchIcon.post(new Runnable() {
                    @Override
                    public void run() {
                        showGuideView();
                    }
                });
            }*/
        } catch (Exception e) {
            UdmLog.error(e);
        } finally {
            if (inputStream1 != null) {
                try {
                    inputStream1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 搜索设备
     */
    private void searchDevice() {
        //判断WIFI是否开启
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            /**
             * 判断WIFI是否连接,非WIFI网络下不搜索设备.
             */
            String wifiIp = "";
            if (networkInfo != null && networkInfo.isConnected()) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    int ipAddress = wifiInfo.getIpAddress();
                    wifiIp = ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
                            + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
                    /**
                     * 开启搜索任务
                     */
                    findViewById(R.id.tab_line_layout).setVisibility(View.GONE);
                    TextView bottomTtile = findViewById(R.id.tab_device_list);
                    bottomTtile.setText("Device List");
                    DeviceSearchAsyncTask task = new DeviceSearchAsyncTask();
                    task.setActivity(MainActivity.this);
                    task.setSupportFragmentManager(getSupportFragmentManager());
                    task.execute(vSearchKeyword.getText().toString());
                } else {
                    Toast.makeText(MainActivity.this, "Please connect to WIFI.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Please open WIFI.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            UdmLog.error(e);
        }
    }

    //重置所有文本的选中状态
    public void selected() {
        tabDeviceList.setSelected(false);
        tabSetting.setSelected(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tool_bar_menu, menu);
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
        if (resultCode == UdmConstant.FLAG_SPECIALLY_SEARCH) {
            if (data != null) {
                String searchKewWord = data.getStringExtra("SEARCH_KEY_WORD");

                DeviceSearchAsyncTask task = new DeviceSearchAsyncTask();
                task.setActivity(this);
                task.setSupportFragmentManager(getSupportFragmentManager());
                task.execute(searchKewWord);
                findViewById(R.id.tab_line_layout).setVisibility(View.GONE);
            }
        } else if (resultCode == UdmConstant.ACTIVITY_RESULT_FOR_LOGIN) {
            boolean isLoginSuccess = data.getBooleanExtra("IS_LOGIN_SUCCESS", false);
            if (isLoginSuccess) {
                Intent intent = new Intent(this, DeviceProfileActivity.class);
                Bundle bundle = data.getExtras();
                bundle.putBoolean("SYNC_ENABLED", true);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        } else if (resultCode == Activity.RESULT_OK) {
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
                Intent intent = new Intent(this, ImportTypeDefFileActivity.class);
                intent.putExtra("TYPE_DEF_FILE", path);
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //检验是否获取权限，如果获取权限，外部存储会处于开放状态.
                    String sdCard = Environment.getExternalStorageState();
                    if (sdCard.equals(Environment.MEDIA_MOUNTED)) {
//                        Toast.makeText(this,"获得授权",Toast.LENGTH_LONG).show();
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(MainActivity.this, "buxing", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void showGuideView() {
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(vSearchIcon)
                .setFullingViewId(R.id.search_icon)
                .setAlpha(150)
                .setHighTargetCorner(90)
                .setHighTargetPadding(24)
                .setOverlayTarget(false)
                .setOutsideTouchable(false);
        builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
            }

            @Override
            public void onDismiss() {
            }
        });
        MainActivityGuide mainActivityGuide = new MainActivityGuide("Connect WIFI,then click\n to search device");
        mainActivityGuide.setxOffset(-60);
        builder.addComponent(mainActivityGuide);
        guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(true);
        guide.show(this);
    }

    /**
     * 显示有上角弹出菜单
     *
     * @param view
     */
    public void showPopup(View view) {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
        Menu menu = popupMenu.getMenu();
        setIconEnable(menu, true);
        popupMenu.getMenuInflater().inflate(R.menu.tool_bar_menu, menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.id_menu_user_profile) {
                    Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                    startActivity(intent);
                } else if (menuItemId == R.id.id_menu_or_code) {
                    Intent intent = new Intent(MainActivity.this, ScanQRCodeActivity.class);
                    startActivityForResult(intent, -1);
                } else if (menuItemId == R.id.id_load_param_def) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");//设置类型.
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent, 1);

                } else if (menuItemId == R.id.id_upgrade_device) {
                    Intent intent = new Intent(MainActivity.this, DeviceUpgradeActivity.class);
                    startActivityForResult(intent, 1);

                } else if (menuItemId == R.id.spec_search_toolbar) {

                    findViewById(R.id.tab_line_layout).setVisibility(View.GONE);
                    TextView bottomTtile = findViewById(R.id.tab_device_list);
                    bottomTtile.setText("Device List");
                    DeviceSearchAsyncTask task = new DeviceSearchAsyncTask();
                    task.setActivity(MainActivity.this);
                    task.setSupportFragmentManager(getSupportFragmentManager());
                    task.execute();

                } else if (menuItemId == R.id.id_menu_sync_report) {
                    Intent intent = new Intent(MainActivity.this, DeviceSyncReportActivity.class);
                    intent.putExtra("SYNC_ENABLED", true);
                    startActivity(intent);
                } /*else if (menuItemId == R.id.id_menu_exit) {
                    finish();
                }*/ else if (menuItemId == R.id.app_about) {
                    Intent intent = new Intent(MainActivity.this, AppUpdateActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
        popupMenu.show();
    }

    /**
     * 右上角菜单显示图标
     *
     * @param menu
     * @param enable
     */
    private void setIconEnable(Menu menu, boolean enable) {
        try {
            Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);
            //传入参数
            m.invoke(menu, enable);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
