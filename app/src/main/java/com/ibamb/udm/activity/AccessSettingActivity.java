package com.ibamb.udm.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
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

public class AccessSettingActivity extends AppCompatActivity {

    private ChannelParameter channelParameter;
    private View currentView;
    private String mac;
    private String channelId;

    private static final String[] ACCESS_SETTING_PARAMS_TAG = {"BASIC_WEB_CONSOLE", "BASIC_TELNET_CONSOLE", "BASIC_CMD_TCP_CONSOL"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_setting);
        Bundle bundle = getIntent().getExtras();
        mac = bundle.getString("HOST_MAC");
        channelId = bundle.getString("CHNL_ID");
        currentView = getWindow().getDecorView();
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        currentView = super.onCreateView(parent, name, context, attrs);
        return currentView;
    }

    @Override
    protected void onStart() {
        super.onStart();
        try{
            channelParameter = new ChannelParameter(mac, UdmConstants.UDM_IP_SETTING_CHNL);
            List<Parameter> parameters = ParameterMapping.getMappingByTags(ACCESS_SETTING_PARAMS_TAG,channelId);
            List<ParameterItem> items = new ArrayList<>();
            for (Parameter parameter : parameters) {
                items.add(new ParameterItem(parameter.getId(), null));
            }
            channelParameter.setParamItems(items);
            ChannelParamReadAsyncTask readerAsyncTask = new ChannelParamReadAsyncTask(currentView,channelParameter);
            readerAsyncTask.execute(mac);
        }catch (Exception e){
            Log.e(AccessSettingActivity.class.getName(),e.getMessage());
        }

    }
}
