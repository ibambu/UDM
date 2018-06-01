package com.ibamb.udm.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.listener.UdmReloadParamsClickListener;
import com.ibamb.udm.log.UdmLog;
import com.ibamb.udm.module.beans.ChannelParameter;
import com.ibamb.udm.module.beans.ParameterItem;
import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.module.core.ParameterMapping;
import com.ibamb.udm.module.instruct.beans.Parameter;
import com.ibamb.udm.task.ChannelParamReadAsyncTask;
import com.ibamb.udm.task.ChannelParamWriteAsynTask;
import com.ibamb.udm.util.TaskBarQuiet;
import com.ibamb.udm.util.ViewElementDataUtil;

import java.util.ArrayList;
import java.util.List;

public class AccessSettingActivity extends AppCompatActivity {

    private ChannelParameter channelParameter;
    private View currentView;
    private String mac;
    private String ip;
    private String channelId;

    private ImageView commit;
    private ImageView back;
    private TextView title;


    private static final String[] ACCESS_SETTING_PARAMS_TAG = {"BASIC_WEB_CONSOLE", "BASIC_TELNET_CONSOLE", "BASIC_CMD_TCP_CONSOL"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_setting);
        TaskBarQuiet.setStatusBarColor(this, Constants.TASK_BAR_COLOR);
        Bundle bundle = getIntent().getExtras();
        mac = bundle.getString("HOST_MAC");
        ip = bundle.getString("HOST_ADDRESS");
        channelId = bundle.getString("CHNL_ID");
        currentView = getWindow().getDecorView();

        commit = findViewById(R.id.do_commit);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChannelParameter changedParam = ViewElementDataUtil.getChangedData(currentView,channelParameter,channelId);
                ChannelParamWriteAsynTask task = new ChannelParamWriteAsynTask(currentView);
                task.execute(channelParameter,changedParam);
            }
        });

        back = findViewById(R.id.go_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title = findViewById(R.id.title);
        title.setText(Constants.TITLE_ACCESS_SETTING);


    }

    @Override
    protected void onStart() {
        super.onStart();
        try{
            channelParameter = new ChannelParameter(mac,ip,channelId);
            List<Parameter> parameters = ParameterMapping.getMappingByTags(ACCESS_SETTING_PARAMS_TAG,channelId);
            List<ParameterItem> items = new ArrayList<>();
            for (Parameter parameter : parameters) {
                items.add(new ParameterItem(parameter.getId(), null));
            }
            channelParameter.setParamItems(items);
            //点击重新读取参数值，并刷新界面。
            title.setOnClickListener(new UdmReloadParamsClickListener(currentView,channelParameter));
            ChannelParamReadAsyncTask readerAsyncTask = new ChannelParamReadAsyncTask(currentView,channelParameter);
            readerAsyncTask.execute(mac);
        }catch (Exception e){
            UdmLog.e(AccessSettingActivity.class.getName(),e.getMessage());
        }

    }

}
