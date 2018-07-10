package com.ibamb.udm.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Paint;
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
import com.ibamb.udm.component.FileDirManager;
import com.ibamb.udm.component.ServiceConst;
import com.ibamb.udm.log.UdmLog;
import com.ibamb.udm.module.beans.DeviceSyncMessage;
import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.service.DeviceSynchronizeService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class DeviceSynchActivity extends AppCompatActivity {

    private DeviceSynchReceiver receiver;
    private ServiceConnection serviceConnection;
    private DeviceSynchronizeService synchronizeService;

    private ProgressBar progressBar;
    private TextView successInfo;
    private TextView failInfo;
    private TextView allItems;

    private Button actionButton;
    private Button selectDeviceButton;

    private String[] seletedDevices;
    private DeviceSyncMessage templateDevice;

    private ImageView goback;
    private TextView title;

    private File syncDeviceLog;//同步日志文件
    private FileDirManager fileDirManager;

    private TextView syncLogLink;
    /**
     * 日志内容格式：日期#IP#MAC#结果标志@日期#IP#MAC#结果标志@日期#IP#MAC#结果标志
     */
    private String reportLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_synch);

        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        String ip = params.getString("HOST_ADDRESS");
        final String mac = params.getString("HOST_MAC");


        (findViewById(R.id.do_commit)).setVisibility(View.GONE);//不显示右边打勾图标

        progressBar = findViewById(R.id.sync_progress_bar);

        allItems = findViewById(R.id.sync_dist_size);
        successInfo = findViewById(R.id.sync_success);
        failInfo = findViewById(R.id.sync_fail);


        templateDevice = new DeviceSyncMessage(ip, mac);

        actionButton = findViewById(R.id.action_button);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seletedDevices == null || seletedDevices.length == 0) {
                    Snackbar.make(v, "No Device Seleted!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    if(syncDeviceLog!=null){
                        syncDeviceLog.delete();
                    }
                    try{
                        synchronizeService.syncDeviceParam(templateDevice, seletedDevices);
                    }catch (Exception e){
                        UdmLog.e(this.getClass().getName(),e.getMessage());
                    }
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
                intent.putExtra("CURRENT_MAC",mac);
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

        syncLogLink = findViewById(R.id.sync_log_link);
        syncLogLink.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        syncLogLink.getPaint().setAntiAlias(true);//抗锯齿
        /**
         * 同步日志点击事件，点击后查看详细同步日志。
         */
        syncLogLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),DeviceSyncReportActivity.class);
                intent.putExtra("REPORT_LOG",reportLog);
                intent.putExtra("SYNC_ENABLED",false);
                startActivity(intent);
            }
        });


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
        /**
         * 同步日志文件
         */
        fileDirManager = new FileDirManager(this);
        syncDeviceLog = fileDirManager.getFileByName(Constants.FILE_SYNC_TO_OTH_DEVICE_LOG);
        if (syncDeviceLog != null) {
            StringBuilder strBuffer = new StringBuilder();
            FileInputStream inputStream = null;
            String syncTime = "";
            try {
                inputStream = openFileInput(Constants.FILE_SYNC_TO_OTH_DEVICE_LOG);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                String lastLine = "";
                while ((line = bufferedReader.readLine()) != null) {
                    lastLine = line;
                    strBuffer.append(line).append("@");
                }
                if (syncTime.trim().length() == 0) {
                    syncTime = (lastLine.split("#"))[0];
                }
                int idx = strBuffer.lastIndexOf("@");
                if(idx!=-1){
                    strBuffer.deleteCharAt(strBuffer.length()-1);//删除最后一个多余的 @ 符号。
                }
                reportLog = strBuffer.toString();
                syncLogLink.setText("Last Synchronized:" + syncTime);
                findViewById(R.id.sync_log_container).setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            findViewById(R.id.sync_log_container).setVisibility(View.GONE);
        }

    }

    public void onProgressChanged(int successCount,int failCount,String syncTime) {
        //更新UI进度
        progressBar.setProgress(successCount+failCount);
        successInfo.setText("SUCCESS: "+String.valueOf(successCount));
        failInfo.setText("FAIL: "+String.valueOf(failCount));
        if(failCount>0){
            failInfo.setTextColor(Color.RED);
        }
        if ((successCount+failCount )== progressBar.getMax()) {
            findViewById(R.id.sync_log_container).setVisibility(View.VISIBLE);
            actionButton.setClickable(true);
            actionButton.setText("Start Sync");
            selectDeviceButton.setClickable(true);
            syncLogLink.setText("Synchronized time:"+syncTime);
        } else {
            actionButton.setClickable(false);
            selectDeviceButton.setClickable(false);
            actionButton.setText("In sync...");
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
            int seletedCount = data.getIntExtra("SELECTED_COUNT", 0);
            seletedDevices = data.getStringArrayExtra("SELECTED_DEVICE");
            progressBar.setMax(seletedCount);
            allItems.setText("ALL: "+String.valueOf(seletedCount));
            failInfo.setText("FAIL: 0");

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Service 广播接收者
     */
    class DeviceSynchReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int successCount = intent.getIntExtra("SYNCH_SUCCESS_COUNT", 0);
            int failCount = intent.getIntExtra("SYNCH_FAIL_COUNT", 0);
            int targetCount = intent.getIntExtra("TARGET_DEVICE_NUMBER", 0);
            String synchTime = intent.getStringExtra("SYNCH_TIME");
                progressBar.setMax(targetCount);
                onProgressChanged(successCount,failCount,synchTime);
                /**
                 * 写同步报告
                 */
                String syncReport = synchTime + "#" + intent.getStringExtra("SYNCH_REPORT");
                BufferedWriter bufwriter = null;

                try {
                    //覆盖写，只保留最新日志
                    OutputStreamWriter writerStream = new OutputStreamWriter(openFileOutput(Constants.FILE_SYNC_TO_OTH_DEVICE_LOG, MODE_APPEND));
                    bufwriter = new BufferedWriter(writerStream);
                    bufwriter.write(syncReport);
                    bufwriter.newLine();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (bufwriter != null) {
                        try {
                            bufwriter.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        }
    }
}
