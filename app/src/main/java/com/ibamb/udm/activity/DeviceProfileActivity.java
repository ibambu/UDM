package com.ibamb.udm.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.component.guide.MainActivityGuide;
import com.ibamb.udm.guide.guideview.Guide;
import com.ibamb.udm.guide.guideview.GuideBuilder;
import com.ibamb.udm.module.beans.ChannelParameter;
import com.ibamb.udm.module.beans.DeviceModel;
import com.ibamb.udm.module.beans.ParameterItem;
import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.module.core.ContextData;
import com.ibamb.udm.module.core.ParameterMapping;
import com.ibamb.udm.module.log.UdmLog;
import com.ibamb.udm.task.DetectSupportChannelsAsyncTask;
import com.ibamb.udm.task.SaveAndRebootAsyncTask;
import com.ibamb.udm.util.TaskBarQuiet;

import java.util.ArrayList;
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
    private String selectedChannleId;

    private TextView hostName;
    private TextView hostIpMac;
    private TextView hostFirmwareVersion;

    Guide guide;

    private View.OnClickListener profileMenuClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Bundle params = new Bundle();
            params.putString("HOST_ADDRESS", ip);
            params.putString("HOST_MAC", mac);
            switch (v.getId()) {
                case R.id.profile_ip_more:
                case R.id.profile_ip_setting:
                    //不涉及某个通道，IP相关的参数当作是0通道。
                    Intent intent1 = new Intent(v.getContext(), NetworkSettingActivity.class);
                    params.putString("CHNL_ID", channelId);
                    intent1.putExtras(params);
                    startActivity(intent1);
                    break;
                case R.id.profile_access_more:
                case R.id.profile_access_setting:
                    //不涉及某个通道
                    Intent intent2 = new Intent(v.getContext(), BasicSettingActivity.class);
                    params.putString("CHNL_ID", channelId);
                    intent2.putExtras(params);
                    startActivity(intent2);
                    break;
                case R.id.profile_connect_setting:
                case R.id.profile_connect_more:
                    /**
                     * 如果支持多个通道，择弹出通道列表界面供选择。
                     */
                    if (supportedChannels.length == 1) {
                        params.putString("CHNL_ID", supportedChannels[0]);
                        selectedChannleId = supportedChannels[0];
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("Select Channel");
                        int checkItemIdx = 0;
                        for (int i = 0; i < supportedChannels.length; i++) {
                            if (supportedChannels[i].equalsIgnoreCase(selectedChannleId)) {
                                checkItemIdx = i;
                                break;
                            }
                        }

                        builder.setSingleChoiceItems(supportedChannels, checkItemIdx, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                params.putString("CHNL_ID", supportedChannels[which]);
                                selectedChannleId = supportedChannels[which];
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                    Intent intent = new Intent(currentContext, ConnectSettingActivity.class);
                    intent.putExtras(params);
                    startActivity(intent);
                    break;
                case R.id.profile_other_setting:
                case R.id.profile_other_more:
                    //不涉及某个通道
                    Intent intent3 = new Intent(v.getContext(), TimeServerSettingActivity.class);
                    params.putString("CHNL_ID", channelId);
                    intent3.putExtras(params);
                    startActivity(intent3);
                    break;
                case R.id.profile_save_reboot:
                    SaveAndRebootAsyncTask task = new SaveAndRebootAsyncTask(getWindow().getDecorView());
                    task.execute(mac);
                    break;
                case R.id.profile_synchronize:
                    Intent intent4 = new Intent(v.getContext(), DeviceSynchActivity.class);
                    params.putString("CHNL_ID", channelId);
                    intent4.putExtras(params);
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
        ip = bundle.getString("HOST_ADDRESS");
        mac = bundle.getString("HOST_MAC");

        hostName = findViewById(R.id.device_name);
        hostIpMac = findViewById(R.id.host_ip_mac);
        hostFirmwareVersion = findViewById(R.id.host_firmware_version);

        boolean syncEnabled = bundle.getBoolean("SYNC_ENABLED");
        if (!syncEnabled) {
            findViewById(R.id.profile_synchronize).setVisibility(View.GONE);
        }

        DeviceModel deviceModel = ContextData.getInstance().getDevice(mac);
        if (deviceModel != null) {
            hostName.setText("Device Name:"+deviceModel.getDeviceName());
            hostIpMac.setText("Address:"+deviceModel.getIp()+" / "+mac.toUpperCase());
            hostFirmwareVersion.setText("Firmware Version:"+deviceModel.getFirmwareVersion());
        }
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

        /*vSettingIP.post(new Runnable() {
            @Override
            public void run() {
                showGuideView();
            }
        });*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentContext = this;
        /**
         * 读取支持的通道
         */
        List<String> confChannels = ParameterMapping.getInstance().getSupportedChannels();
        List<String> supportChannels = new ArrayList<>();
        ChannelParameter testChannelParams = new ChannelParameter(mac, ip, "-1");
        testChannelParams.setParamItems(new ArrayList<ParameterItem>());

        for (String channelId : confChannels) {
            if (channelId.equals("0")) {
                continue;//0 通道是虚拟通道，需要排除。
            }
            ParameterItem item = new ParameterItem("CONN" + channelId + "_NET_PROTOCOL", null);
            testChannelParams.getParamItems().add(item);
        }
        try {
            DetectSupportChannelsAsyncTask task = new DetectSupportChannelsAsyncTask(testChannelParams);
            task.execute().get();
            for (int i = 1; i < 33; i++) {
                String channelId = String.valueOf(i);
                String paramId = "CONN" + channelId + "_NET_PROTOCOL";
                for (ParameterItem parameterItem : testChannelParams.getParamItems()) {
                    if (parameterItem.getParamId().equals(paramId)
                            && parameterItem.getParamValue() != null
                            && parameterItem.getParamValue().trim().length() > 0) {
                        supportChannels.add(String.valueOf(channelId));
                    }
                }
            }

            supportedChannels = new String[supportChannels.size()];
            for (int i = 0; i < supportChannels.size(); i++) {
                supportedChannels[i] = supportChannels.get(i);
            }
        } catch (Exception e) {
            UdmLog.error(e);
        }
    }

    public void showGuideView() {
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(vSettingIP)
                .setFullingViewId(R.id.profile_ip_setting)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPadding(10)
                .setOverlayTarget(false)
                .setOutsideTouchable(false);
        builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override public void onShown() {
            }

            @Override public void onDismiss() {
            }
        });

        builder.addComponent(new MainActivityGuide(""));
        guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(true);
        guide.show(this);
    }
}
