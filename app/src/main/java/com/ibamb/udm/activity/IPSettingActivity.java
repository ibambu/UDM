package com.ibamb.udm.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.beans.ChannelParameter;
import com.ibamb.udm.beans.ParameterItem;
import com.ibamb.udm.constants.UdmConstants;
import com.ibamb.udm.core.ParameterMapping;
import com.ibamb.udm.instruct.beans.Parameter;
import com.ibamb.udm.listener.UdmReloadParamsClickListener;
import com.ibamb.udm.task.ChannelParamReadAsyncTask;
import com.ibamb.udm.task.ChannelParamWriteAsynTask;
import com.ibamb.udm.util.ViewElementDataUtil;

import java.util.ArrayList;
import java.util.List;

public class IPSettingActivity extends AppCompatActivity {

    private ChannelParameter channelParameter;
    private String mac;
    private View currentView;

    private TextView commit;
    private TextView back;
    private TextView title;

    private static final String[] IP_SETTING_PARAMS_TAG = {"ETH_AUTO_OBTAIN_IP", "ETH_IP_ADDR",
            "PREFERRED_DNS_SERVER", "ETH_NETMASK_ADDR", "PREFERRED_DNS_SERVER", "ALTERNATE_DNS_SERVE", "ETH_GATEWAY_ADDR"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipsetting);

        Bundle bundle = getIntent().getExtras();
        mac = bundle.getString("HOST_MAC");
        currentView = getWindow().getDecorView();

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
                ChannelParameter changedParam = ViewElementDataUtil.getChangedData(currentView,channelParameter,UdmConstants.UDM_IP_SETTING_CHNL);
                ChannelParamWriteAsynTask task = new ChannelParamWriteAsynTask(currentView);
                task.execute(channelParameter,changedParam);
            }
        });

        title=findViewById(R.id.title);
        title.setText("IP Setting");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //在界面初始化完毕后读取默认通道的参数，并且刷新界面数据。
        try {
            channelParameter = new ChannelParameter(mac, UdmConstants.UDM_IP_SETTING_CHNL);
            List<Parameter> parameters = ParameterMapping.getMappingByTags(IP_SETTING_PARAMS_TAG, UdmConstants.UDM_IP_SETTING_CHNL);
            List<ParameterItem> items = new ArrayList<>();
            for (Parameter parameter : parameters) {
                items.add(new ParameterItem(parameter.getId(), null));
            }
            channelParameter.setParamItems(items);
            //点击重新读取参数值，并刷新界面。注意顺序，一定要在channelParameter赋值之后再绑定事件。
            title.setOnClickListener(new UdmReloadParamsClickListener(currentView,channelParameter));
            //后台异步读取参数值并更新界面数据。
            ChannelParamReadAsyncTask readerAsyncTask = new ChannelParamReadAsyncTask(currentView, channelParameter);
            readerAsyncTask.execute(mac);
        } catch (Exception e) {
            Log.e(AccessSettingActivity.class.getName(), e.getMessage());
        }

    }

    private void bindEvents(){

    }

}
