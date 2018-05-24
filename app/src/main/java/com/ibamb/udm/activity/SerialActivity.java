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
import com.ibamb.udm.util.TaskBarQuiet;
import com.ibamb.udm.util.ViewElementDataUtil;

import java.util.ArrayList;
import java.util.List;

public class SerialActivity extends AppCompatActivity {

    private ChannelParameter channelParameter;
    private View currentView;
    private String mac;
    private String ip;
    private String channelId;

    private TextView commit;
    private TextView back;
    private TextView title;

    private static final String[] SERIAL_SETTING_PARAMS_TAG = {"UART_STOPBIT", "UART_DATABIT", "UART_BDRATE","UART_PARITY",
            "UART_FLOWRNT", "UART_IDLE_TIME_PACKINGR","CONN_LINK_LATCH_TIMEOUT"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial);
        TaskBarQuiet.setStatusBarColor(this, UdmConstants.TASK_BAR_COLOR);

        Bundle bundle = getIntent().getExtras();
        mac = bundle.getString("HOST_MAC");
        channelId = bundle.getString("CHNL_ID");
        currentView = getWindow().getDecorView();
        ip = bundle.getString("HOST_ADDRESS");
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
        title.setText("Serial Setting");
    }

    @Override
    protected void onStart() {
        super.onStart();
        try{
            channelParameter = new ChannelParameter(mac, channelId,ip);
            List<Parameter> parameters = ParameterMapping.getMappingByTags(SERIAL_SETTING_PARAMS_TAG,channelId);
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
            Log.e(AccessSettingActivity.class.getName(),e.getMessage());
        }
    }

    @Override
    public void finish() {
        super.finish();
    }
}
