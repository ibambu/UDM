package com.ibamb.udm.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.fragment.SyncFailFragment;
import com.ibamb.udm.fragment.SyncSuccessFragment;
import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.util.TaskBarQuiet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class DeviceSyncReportActivity extends AppCompatActivity {

    private SyncSuccessFragment syncSuccessFragment;//同步成功列表
    private SyncFailFragment syncFailFragment;//同步失败列表
    private Button successButton;
    private Button failButton;
    private StringBuilder successBuffer = new StringBuilder();
    private StringBuilder failBuffer = new StringBuilder();
    private TextView title;
    private ImageView goback;
    private TextView syncTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_sync_report);
        TaskBarQuiet.setStatusBarColor(this, Constants.TASK_BAR_COLOR);//修改任务栏背景颜色


        StringBuilder strBuffer = new StringBuilder();
        FileInputStream inputStream = null;
        String lastSyncTime ="";
        try {
            inputStream = openFileInput(Constants.FILE_SYNC_TO_OTH_DEVICE_LOG);
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
            if(deviceInfo[deviceInfo.length-1].equals("true")){
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

        successButton = findViewById(R.id.success_button);
        failButton = findViewById(R.id.fail_button);
        /**
         * 按钮点击事件
         */
        successButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                if(syncFailFragment!=null){
                    transaction.hide(syncFailFragment);
                }
                if (syncSuccessFragment == null) {
                    syncSuccessFragment = SyncSuccessFragment.newInstance(successBuffer.toString());
                    transaction.add(R.id.report_frame, syncSuccessFragment);
                    transaction.show(syncSuccessFragment);
                } else {
                    transaction.show(syncSuccessFragment);
                }
                transaction.commit();
                failButton.setTextColor(Color.BLACK);
                successButton.setTextColor(Color.BLUE);
            }
        });

        failButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                if(syncSuccessFragment!=null){
                    transaction.hide(syncSuccessFragment);
                }
                if (syncFailFragment == null) {
                    syncFailFragment = SyncFailFragment.newInstance(failBuffer.toString());
                    transaction.add(R.id.report_frame, syncFailFragment);
                    transaction.show(syncFailFragment);
                } else {
                    transaction.show(syncFailFragment);
                }
                transaction.commit();
                failButton.setTextColor(Color.BLUE);
                successButton.setTextColor(Color.BLACK);

            }
        });

        /**
         * 默认显示同步成功列表
         */
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (syncSuccessFragment == null) {
            syncSuccessFragment = SyncSuccessFragment.newInstance(successBuffer.toString());
            transaction.add(R.id.report_frame, syncSuccessFragment);
            transaction.show(syncSuccessFragment);
        } else {
            transaction.show(syncSuccessFragment);
        }
        transaction.commit();
        failButton.setTextColor(Color.BLACK);
        successButton.setTextColor(Color.BLUE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Constants.ACTIVITY_RESULT_FOR_LOGIN){
            Intent intent = new Intent(this, DeviceProfileActivity.class);
            Bundle bundle = data.getExtras();
            intent.putExtras(bundle);
            startActivity(intent);
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
