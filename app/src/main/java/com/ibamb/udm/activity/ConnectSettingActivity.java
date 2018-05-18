package com.ibamb.udm.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.beans.ChannelParameter;
import com.ibamb.udm.beans.ParameterItem;
import com.ibamb.udm.constants.UdmConstants;
import com.ibamb.udm.core.ParameterMapping;
import com.ibamb.udm.instruct.beans.Parameter;
import com.ibamb.udm.task.ChannelParamReadAsyncTask;

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

    private static final String[] CONNECT_SETTING_PARAMS_TAG = {"CONN_NET_PROTOCOL"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_setting);
        currentView = getWindow().getDecorView();
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

        vTcpSetting.setOnClickListener(menuItemClickListener);
        vUdpSetting.setOnClickListener(menuItemClickListener);
        vSerailSetting.setOnClickListener(menuItemClickListener);

        vTcpMore.setOnClickListener(menuItemClickListener);
        vUdpMore.setOnClickListener(menuItemClickListener);
        vSerailMore.setOnClickListener(menuItemClickListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            channelParameter = new ChannelParameter(mac, UdmConstants.UDM_IP_SETTING_CHNL);
            List<Parameter> parameters = ParameterMapping.getMappingByTags(CONNECT_SETTING_PARAMS_TAG, channelId);
            List<ParameterItem> items = new ArrayList<>();
            for (Parameter parameter : parameters) {
                items.add(new ParameterItem(parameter.getId(), null));
            }
            channelParameter.setParamItems(items);
            ChannelParamReadAsyncTask readerAsyncTask = new ChannelParamReadAsyncTask(currentView, channelParameter);
            readerAsyncTask.execute(mac);
        } catch (Exception e) {
            Log.e(AccessSettingActivity.class.getName(), e.getMessage());
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
            params.putString("CHNL_ID",channelId);
            if (intent != null) {
                intent.putExtras(params);
                startActivity(intent);
            }
        }
    };

}
