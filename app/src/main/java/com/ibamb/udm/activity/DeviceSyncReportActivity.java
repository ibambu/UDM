package com.ibamb.udm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.adapter.SyncReportTabAdapter;
import com.ibamb.udm.component.constants.UdmConstant;
import com.ibamb.udm.fragment.SyncFailFragment;
import com.ibamb.udm.fragment.SyncSuccessFragment;
import com.ibamb.dnet.module.constants.Constants;
import com.ibamb.udm.util.TaskBarQuiet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeviceSyncReportActivity extends AppCompatActivity {

    private SyncSuccessFragment syncSuccessFragment;//同步成功列表
    private SyncFailFragment syncFailFragment;//同步失败列表

    private StringBuilder successBuffer = new StringBuilder();
    private StringBuilder failBuffer = new StringBuilder();

    private TextView title;
    private ImageView goback;
    private TextView syncTime;

    private TabLayout reportTab;
    private ViewPager reporViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_sync_report);

        TaskBarQuiet.setStatusBarColor(this, UdmConstant.TASK_BAR_COLOR);//修改任务栏背景颜色
        Intent intent = getIntent();
        final boolean isSyncMenuEnabled = intent.getBooleanExtra("SYNC_ENABLED",false);

        reportTab = findViewById(R.id.report_tab);
        reporViewPager = findViewById(R.id.report_tab_content);

        TabLayout.Tab successTab = reportTab.newTab();
        successTab.setText("Success List");
        reportTab.addTab(successTab);

        TabLayout.Tab failTab = reportTab.newTab();
        failTab.setText("Fail List");
        reportTab.addTab(failTab);

        reportTab.setupWithViewPager(reporViewPager);
        SyncReportTabAdapter adapter = new SyncReportTabAdapter(getSupportFragmentManager());
        adapter.setTitles(Arrays.asList("Success List","Fail List"));

        StringBuilder strBuffer = new StringBuilder();
        FileInputStream inputStream = null;
        String lastSyncTime ="";
        try {
            inputStream = openFileInput(UdmConstant.FILE_SYNC_TO_OTH_DEVICE_LOG);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            String lastLine = "";
            while ((line = bufferedReader.readLine()) != null) {
                lastLine = line;
                strBuffer.append(line).append("@");
            }
            int idx = strBuffer.lastIndexOf("@");
            if(idx!=-1){
                strBuffer.deleteCharAt(strBuffer.length()-1);//删除最后一个多余的 @ 符号。
            }
            lastSyncTime = (lastLine.split("#"))[0];

            syncTime = findViewById(R.id.sync_time);
            syncTime.setText("Synchronized time:"+lastSyncTime);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] logContent = strBuffer.toString().split("@");
        for(String log:logContent){
            String[] deviceInfo = log.split("#");
            if(deviceInfo[deviceInfo.length-1].equals(UdmConstant.SYNC_SUCCESS)){
                successBuffer.append(log).append("@");
            }else{
                failBuffer.append(log).append("@");
            }
        }
        if(successBuffer.length()>0){
            successBuffer.deleteCharAt(successBuffer.length()-1);
        }
        if(failBuffer.length()>0){
            failBuffer.deleteCharAt(failBuffer.length()-1);
        }

        List<Fragment> fragments = new ArrayList<>();
        syncSuccessFragment = SyncSuccessFragment.newInstance(successBuffer.toString(),isSyncMenuEnabled);
        syncFailFragment = SyncFailFragment.newInstance(failBuffer.toString(),isSyncMenuEnabled);
        fragments.add(syncSuccessFragment);
        fragments.add(syncFailFragment);
        adapter.setFragments(fragments);

        reporViewPager.setAdapter(adapter);
        title = findViewById(R.id.title);
        title.setText("Device Sync Report");

        findViewById(R.id.do_commit).setVisibility(View.GONE);

        goback = findViewById(R.id.go_back);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == UdmConstant.ACTIVITY_RESULT_FOR_LOGIN){
            boolean isLoginSuccess = data.getBooleanExtra("IS_LOGIN_SUCCESS",false);
            if(isLoginSuccess){
                Intent intent = new Intent(this, DeviceProfileActivity.class);
                Bundle bundle = data.getExtras();
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
