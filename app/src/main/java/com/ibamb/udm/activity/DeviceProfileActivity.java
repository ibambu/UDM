package com.ibamb.udm.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.module.core.ParameterMapping;
import com.ibamb.udm.module.sys.SysManager;
import com.ibamb.udm.task.SaveAndRebootAsyncTask;
import com.ibamb.udm.util.TaskBarQuiet;

import java.util.List;

public class DeviceProfileActivity extends AppCompatActivity {

    private String ip;
    private String mac;
    private String[] supportedChannels;
    private Context currentContext;

    private ImageView back;
    private ImageView commit;
    private TextView title;

    private ImageView icSettingIp;

    private TextView vSettingIP;
    private TextView vSettingConnect;
    private TextView vSettingOther;
    private TextView vSettingAccess;

    private TextView vSaveAndReboot;
    private TextView vSyncToOther;

    private ImageView icSettingConnect;
    private ImageView icSettingOther;
    private ImageView icSettingAccess;
    private String channelId = "0";


    private View.OnClickListener profileMenuClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.profile_ip_more:
                case R.id.profile_ip_setting:
                    //不涉及某个通道，IP相关的参数当作是0通道。
                    Intent intent1 = new Intent(v.getContext(), IPSettingActivity.class);
                    Bundle params = new Bundle();
                    params.putString("HOST_ADDRESS", ip);
                    params.putString("HOST_MAC", mac);
                    params.putString("CHNL_ID", channelId);
                    intent1.putExtras(params);
                    startActivity(intent1);
                    break;
                case R.id.profile_access_more:
                case R.id.profile_access_setting:
                    //不涉及某个通道
                    Intent intent2 = new Intent(v.getContext(), AccessSettingActivity.class);
                    Bundle params2 = new Bundle();
                    params2.putString("HOST_ADDRESS", ip);
                    params2.putString("HOST_MAC", mac);
                    params2.putString("CHNL_ID", channelId);
                    intent2.putExtras(params2);
                    startActivity(intent2);
                    break;
                case R.id.profile_connect_setting:
                case R.id.profile_connect_more:
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Select Channel");
                    int checkItemIdx = 0;
                    for(int i=0;i<supportedChannels.length;i++){
                        if(supportedChannels[i].equalsIgnoreCase(vSettingConnect.getText().toString())){
                            checkItemIdx = i;
                            break;
                        }
                    }
                    builder.setSingleChoiceItems(supportedChannels, checkItemIdx,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Bundle params = new Bundle();
                            params.putString("HOST_ADDRESS", ip);
                            params.putString("HOST_MAC", mac);
                            params.putString("CHNL_ID", supportedChannels[which]);
                            Intent intent = new Intent(currentContext, ConnectSettingActivity.class);
                            intent.putExtras(params);
                            dialog.dismiss();
                            startActivity(intent);
                        }
                    });
                    builder.show();
                    break;
                case R.id.profile_other_setting:
                case R.id.profile_other_more:
                    //不涉及某个通道
                    Intent intent3 = new Intent(v.getContext(), OtherSettingActivity.class);
                    Bundle params3 = new Bundle();
                    params3.putString("HOST_ADDRESS", ip);
                    params3.putString("HOST_MAC", mac);
                    params3.putString("CHNL_ID", channelId);
                    intent3.putExtras(params3);
                    startActivity(intent3);
                    break;
                case R.id.profile_save_reboot:
                    SaveAndRebootAsyncTask task = new SaveAndRebootAsyncTask(getWindow().getDecorView());
                    task.execute(mac);
                    break;
                case R.id.profile_synchronize:
                    Intent intent4 = new Intent(v.getContext(), DeviceListActivity.class);
                    startActivity(intent4);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_profile);
        TaskBarQuiet.setStatusBarColor(this, Constants.TASK_BAR_COLOR);

        Bundle bundle = getIntent().getExtras();
        ip = (String) bundle.get("HOST_ADDRESS");
        mac = (String) bundle.get("HOST_MAC");
        TextView vIp = findViewById(R.id.host_ip);
//        TextView vMac = findViewById(R.id.host_mac);
        vIp.setText(ip+"/"+mac.toUpperCase());
//        vMac.setText(mac);

        back = findViewById(R.id.go_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        commit = findViewById(R.id.do_commit);


        title = findViewById(R.id.title);
        title.setText(Constants.TITLE_DEVICE_PROFILE);

        vSettingIP = findViewById(R.id.profile_ip_setting);
        vSettingConnect = findViewById(R.id.profile_connect_setting);
        vSettingOther = findViewById(R.id.profile_other_setting);
        vSettingAccess = findViewById(R.id.profile_access_setting);

        icSettingIp = findViewById(R.id.profile_ip_more);
        icSettingConnect = findViewById(R.id.profile_connect_more);
        icSettingOther = findViewById(R.id.profile_other_more);
        icSettingAccess = findViewById(R.id.profile_access_more);
        vSaveAndReboot = findViewById(R.id.profile_save_reboot);
        vSyncToOther = findViewById(R.id.profile_synchronize);

        vSettingIP.setOnClickListener(profileMenuClickListener);
        vSettingConnect.setOnClickListener(profileMenuClickListener);
        vSettingOther.setOnClickListener(profileMenuClickListener);
        vSettingAccess.setOnClickListener(profileMenuClickListener);
        vSaveAndReboot.setOnClickListener(profileMenuClickListener);
        vSyncToOther.setOnClickListener(profileMenuClickListener);

        icSettingIp.setOnClickListener(profileMenuClickListener);
        icSettingConnect.setOnClickListener(profileMenuClickListener);
        icSettingOther.setOnClickListener(profileMenuClickListener);
        icSettingAccess.setOnClickListener(profileMenuClickListener);


    }

    @Override
    protected void onStart() {
        super.onStart();
        currentContext = this;
        /**
         * 读取支持的通道
         */
        List<String> suppChannels = ParameterMapping.getSupportedChannels();
        supportedChannels = new String[suppChannels.size()];
        for (int i = 0; i < supportedChannels.length; i++) {
            supportedChannels[i] = suppChannels.get(i);
        }
    }
}
