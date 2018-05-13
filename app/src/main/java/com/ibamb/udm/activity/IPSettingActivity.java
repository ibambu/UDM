package com.ibamb.udm.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
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

public class IPSettingActivity extends AppCompatActivity {

    private ChannelParameter channelParameter;
    private String mac;
    private View ipSetingView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipsetting);
        Bundle bundle = getIntent().getExtras();
        mac = bundle.getString("HOST_MAC");
        ipSetingView = getWindow().getDecorView();
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        ipSetingView = super.onCreateView(parent, name, context, attrs);
        return ipSetingView;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //在界面初始化完毕后读取默认通道的参数，并且刷新界面数据。
        channelParameter = new ChannelParameter(mac, UdmConstants.UDM_IP_SETTING_CHNL);
        List<Parameter> parameters = ParameterMapping.getChannelParamDef(Integer.parseInt(UdmConstants.UDM_IP_SETTING_CHNL));
        List<ParameterItem> items = new ArrayList<>();
        for (Parameter parameter : parameters) {
            items.add(new ParameterItem(parameter.getId(), null));
        }

        channelParameter.setParamItems(items);
        ChannelParamReadAsyncTask readerAsyncTask = new ChannelParamReadAsyncTask(ipSetingView,channelParameter);
        readerAsyncTask.execute(mac, UdmConstants.UDM_IP_SETTING_CHNL);
    }
}
