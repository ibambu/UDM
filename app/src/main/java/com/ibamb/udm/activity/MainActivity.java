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
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ibamb.udm.R;
import com.ibamb.udm.component.AESCrypt;
import com.ibamb.udm.component.FileDirManager;
import com.ibamb.udm.component.FilePathParser;
import com.ibamb.udm.component.PermissionUtils;
import com.ibamb.udm.fragment.DeviceSearchListFragment;
import com.ibamb.udm.listener.UdmToolbarMenuClickListener;
import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.module.core.TryUser;
import com.ibamb.udm.module.log.UdmLog;
import com.ibamb.udm.module.security.DefualtECryptValue;
import com.ibamb.udm.module.security.ICryptStrategy;
import com.ibamb.udm.task.DeviceSearchAsyncTask;
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

    private Toolbar mToolbar;
    private TextView tabDeviceList;
    private TextView tabSetting;

    private TabLayout deviceTabLayout;
    private ViewPager devcieViewPager;


    private DeviceSearchListFragment searchListFragment;


    @Override
    protected void onStart() {
        super.onStart();
        FileInputStream inputStream = null;
        FileDirManager fileDirManager = new FileDirManager(this);
        try {
            File runErrFile = fileDirManager.getFileByName(Constants.FILE_UDM_RUN_ERR_LOG);
            if(runErrFile==null){
                runErrFile = new File(getFilesDir()+"/"+Constants.FILE_UDM_RUN_ERR_LOG);
                runErrFile.createNewFile();
            }
            UdmLog.setErrorLogFile(runErrFile);
            StringBuilder strbuffer = new StringBuilder();

            File tryUesrFile = fileDirManager.getFileByName(Constants.TRY_USER_FILE);
            if(tryUesrFile!=null){
                inputStream = openFileInput(Constants.TRY_USER_FILE);
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

        } catch (Exception e) {
            UdmLog.error(e);
        }finally {
            if (inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    UdmLog.error(e);
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //将ActionBar位置改放Toolbar.
        mToolbar = (Toolbar) findViewById(R.id.udm_toolbar);
        setSupportActionBar(mToolbar);

        //设置右上角的填充菜单
        mToolbar.inflateMenu(R.menu.tool_bar_menu);
        //这句代码使启用Activity回退功能，并显示Toolbar上的左侧回退图标
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TaskBarQuiet.setStatusBarColor(this, Constants.TASK_BAR_COLOR);//修改任务栏背景颜色
        if (PermissionUtils.isGrantExternalRW(this, 1)) {

        }
        deviceTabLayout = findViewById(R.id.device_list_tab);
        devcieViewPager = findViewById(R.id.device_list_pager);

//        //默认显示第一个界面
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        hideAllFragment(transaction);
//        if (searchListFragment == null) {
//            searchListFragment = DeviceSearchListFragment.newInstance(null, null);
//            transaction.add(R.id.fragment_container, searchListFragment);
//            transaction.show(searchListFragment);
//        } else {
//            transaction.show(searchListFragment);
//        }
//        transaction.commit();
        //绑定菜单点击事件
        UdmToolbarMenuClickListener toolbarMenuClickListener = new UdmToolbarMenuClickListener(this,searchListFragment);
        toolbarMenuClickListener.setFragmentManager(getSupportFragmentManager());
        mToolbar.setOnMenuItemClickListener(toolbarMenuClickListener);
        //底部菜单绑定点击事件,实现界面切换.
        tabDeviceList = findViewById(R.id.tab_device_list);
        tabSetting = findViewById(R.id.tab_setting);
//        UdmBottomMenuClickListener bottomMenuClickListener = new UdmBottomMenuClickListener(fragmentManager,searchListFragment,
//                tabDeviceList,tabSetting);
//        tabDeviceList.setOnClickListener(bottomMenuClickListener);
//        tabSetting.setOnClickListener(bottomMenuClickListener);
        tabDeviceList.requestFocus();
        tabDeviceList.setSelected(true);


        //判断WIFI是否开启
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            /**
             * 判断WIFI是否连接,非WIFI网络下不搜索设备.
             */
            String wifiIp = "";
            if (networkInfo != null && networkInfo.isConnected()) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    int ipAddress = wifiInfo.getIpAddress();
                    wifiIp = ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
                            + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
                }
            }else{
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

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction) {
        if (searchListFragment != null) {
            transaction.hide(searchListFragment);
        }
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
        if(resultCode==Constants.FLAG_SPECIALLY_SEARCH){
            if(data!=null){
                String searchKewWord = data.getStringExtra("SEARCH_KEY_WORD");
                View view = searchListFragment.getView();
                ListView mListView =  view.findViewById(R.id.search_device_list);
                TextView vSearchNotice = view.findViewById(R.id.search_notice_info);
                DeviceSearchAsyncTask task = new DeviceSearchAsyncTask(mListView,vSearchNotice,
                        searchListFragment.getLayoutInflater());
                task.setActivity(this);
                task.setSupportFragmentManager(getSupportFragmentManager());
                task.execute(searchKewWord);
            }
        }else if(resultCode == Constants.ACTIVITY_RESULT_FOR_LOGIN){
            boolean isLoginSuccess = data.getBooleanExtra("IS_LOGIN_SUCCESS",false);
            if(isLoginSuccess){
                Intent intent = new Intent(this, DeviceProfileActivity.class);
                Bundle bundle = data.getExtras();
                bundle.putBoolean("SYNC_ENABLED",true);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }else if(resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            FilePathParser filePathParser = new FilePathParser();
            String path="";
            if ("file".equalsIgnoreCase(uri.getScheme())){//使用第三方应用打开
                path = uri.getPath();
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                path = filePathParser.getPath(this, uri);
            } else {//4.4以下下系统调用方法
                path = filePathParser.getRealPathFromURI(uri,getContentResolver());
            }
            if(path!= null && path.trim().length()>0){
                Intent intent = new Intent(this, ImportTypeDefFileActivity.class);
                intent.putExtra("TYPE_DEF_FILE",path);
                startActivity(intent);
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //检验是否获取权限，如果获取权限，外部存储会处于开放状态，会弹出一个toast提示获得授权
                    String sdCard = Environment.getExternalStorageState();
                    if (sdCard.equals(Environment.MEDIA_MOUNTED)){
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
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
