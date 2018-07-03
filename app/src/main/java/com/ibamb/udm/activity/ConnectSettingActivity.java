package com.ibamb.udm.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ibamb.udm.R;
import com.ibamb.udm.log.UdmLog;
import com.ibamb.udm.module.beans.ChannelParameter;
import com.ibamb.udm.module.beans.ParameterItem;
import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.module.core.ParameterMapping;
import com.ibamb.udm.module.instruct.beans.Parameter;
import com.ibamb.udm.listener.UdmReloadParamsClickListener;
import com.ibamb.udm.task.ChannelParamReadAsyncTask;
import com.ibamb.udm.task.ChannelParamWriteAsynTask;
import com.ibamb.udm.util.TaskBarQuiet;
import com.ibamb.udm.util.ViewElementDataUtil;

import java.util.ArrayList;
import java.util.List;

public class ConnectSettingActivity extends AppCompatActivity {

    private TextView vTcpSetting;
    private TextView vUdpSetting;
    private TextView vSerailSetting;


    private ImageView vTcpMore;
    private ImageView vUdpMore;
    private ImageView vSerailMore;

    private Switch vTcpEnabled;
    private Switch vUdpEnabled;

    private ChannelParameter channelParameter;
    private View currentView;
    private String mac;
    private String channelId;

    private String ip;

    private TextView title;
    private ImageView back;
    private ImageView commit;

    private static final String[] CONNECT_SETTING_PARAMS_TAG = {"CONN_NET_PROTOCOL"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_setting);
        currentView = getWindow().getDecorView();
        TaskBarQuiet.setStatusBarColor(this, Constants.TASK_BAR_COLOR);

        Bundle bundle = getIntent().getExtras();
        ip = (String) bundle.get("HOST_ADDRESS");
        mac = (String) bundle.get("HOST_MAC");
        channelId = bundle.getString("CHNL_ID");

        vTcpSetting = findViewById(R.id.tcp_connection_setting);
        vUdpSetting = findViewById(R.id.udp_connection_setting);
        vSerailSetting = findViewById(R.id.serial_setting);

        vTcpMore = findViewById(R.id.tcp_connection_more);
        vUdpMore = findViewById(R.id.udp_connection_more);
        vSerailMore = findViewById(R.id.serial_setting_more);


        vTcpEnabled = findViewById(R.id.tcp_enanbled_switch);
        vUdpEnabled = findViewById(R.id.udp_enanbled_switch);
        vTcpEnabled.setOnClickListener(protocolSwitchListener);
        vUdpEnabled.setOnClickListener(protocolSwitchListener);

        vTcpSetting.setOnClickListener(menuItemClickListener);
        vUdpSetting.setOnClickListener(menuItemClickListener);
        vSerailSetting.setOnClickListener(menuItemClickListener);

        vTcpMore.setOnClickListener(menuItemClickListener);
        vUdpMore.setOnClickListener(menuItemClickListener);
        vSerailMore.setOnClickListener(menuItemClickListener);

        commit = findViewById(R.id.do_commit);
        back = findViewById(R.id.go_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChannelParameter changedParam = ViewElementDataUtil.getChangedData(currentView, channelParameter, channelId);
                ChannelParamWriteAsynTask task = new ChannelParamWriteAsynTask(currentView);
                task.execute(channelParameter, changedParam);
            }
        });

        title = findViewById(R.id.title);
        title.setText("Connection of Channel " + channelId);
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            channelParameter = new ChannelParameter(mac, ip, channelId);
            List<Parameter> parameters = ParameterMapping.getMappingByTags(CONNECT_SETTING_PARAMS_TAG, channelId);
            List<ParameterItem> items = new ArrayList<>();
            for (Parameter parameter : parameters) {
                items.add(new ParameterItem(parameter.getId(), null));
            }
            channelParameter.setParamItems(items);
            //点击重新读取参数值，并刷新界面。
            title.setOnClickListener(new UdmReloadParamsClickListener(currentView, channelParameter));
            ChannelParamReadAsyncTask readerAsyncTask = new ChannelParamReadAsyncTask(currentView, channelParameter);
            readerAsyncTask.execute(mac);
        } catch (Exception e) {
            UdmLog.e(AccessSettingActivity.class.getName(), e.getMessage());
        }
    }

    private View.OnClickListener menuItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;

            switch (v.getId()) {
                case R.id.tcp_connection_setting:
                case R.id.tcp_connection_more:
                    intent = new Intent(v.getContext(), TCPConnectionActivity.class);
                    break;
                case R.id.udp_connection_setting:
                case R.id.udp_connection_more:
                    intent = new Intent(v.getContext(), UDPConnectionActivity.class);
                    break;
                case R.id.serial_setting:
                case R.id.serial_setting_more:
                    intent = new Intent(v.getContext(), SerialActivity.class);
                    break;
                default:
                    break;
            }
            Bundle params = new Bundle();
            params.putString("HOST_ADDRESS", ip);
            params.putString("HOST_MAC", mac);
            params.putString("CHNL_ID", channelId);
            if (intent != null) {
                intent.putExtras(params);
                startActivity(intent);
            }
        }
    };

    private View.OnClickListener protocolSwitchListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!vTcpEnabled.isChecked() && !vUdpEnabled.isChecked()) {
                if(v.getId()==R.id.tcp_enanbled_switch){
                    vTcpEnabled.setChecked(true);
                }else if(v.getId()==R.id.udp_enanbled_switch){
                    vUdpEnabled.setChecked(true);
                }
                String notice = "TCP/UDP one must be Enabled.";
                Toast.makeText(v.getContext(), notice, Toast.LENGTH_SHORT).show();
            }
        }
    };


}
