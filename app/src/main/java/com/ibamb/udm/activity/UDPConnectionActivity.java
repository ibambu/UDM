package com.ibamb.udm.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ibamb.udm.R;
import com.ibamb.udm.beans.ChannelParameter;
import com.ibamb.udm.beans.ParameterItem;
import com.ibamb.udm.constants.UdmConstants;
import com.ibamb.udm.core.ParameterMapping;
import com.ibamb.udm.instruct.beans.Parameter;
import com.ibamb.udm.task.ChannelParamReadAsyncTask;

import java.util.ArrayList;
import java.util.List;

public class UDPConnectionActivity extends AppCompatActivity {
    private ChannelParameter channelParameter;
    private View currentView;
    private String mac;
    private String channelId;

    private static final String[] UDP_SETTING_PARAMS_TAG = {
            "CONN_LINK_LATCH_TIMEOUT",
            "CONN_UDP_DATA_MODE",
            "CONN_UDP_TMP_HOST_EN",
            "CONN_UDP_UNI_HOST_IP0",
            "CONN_UDP_MUL_LOCAL_PORT",
            "CONN_UDP_MUL_REMOTE_IP",
            "CONN_UDP_ACCEPTION",
            "CONN_UDP_UNI_LOCAL_PORT",
            "CONN_UDP_UNI_HOST_PORT0",
            "CONN_UDP_MUL_REMOTE_PORT"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udpconnection);
        Bundle bundle = getIntent().getExtras();
        mac = bundle.getString("HOST_MAC");
        channelId = bundle.getString("CHNL_ID");
        currentView = getWindow().getDecorView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            channelParameter = new ChannelParameter(mac, UdmConstants.UDM_IP_SETTING_CHNL);
            List<Parameter> parameters = ParameterMapping.getMappingByTags(UDP_SETTING_PARAMS_TAG, channelId);
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
}