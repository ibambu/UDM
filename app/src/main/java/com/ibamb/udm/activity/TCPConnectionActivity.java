package com.ibamb.udm.activity;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.module.beans.ChannelParameter;
import com.ibamb.udm.module.beans.ParameterItem;
import com.ibamb.udm.module.constants.UdmConstants;
import com.ibamb.udm.module.core.ParameterMapping;
import com.ibamb.udm.module.instruct.beans.Parameter;
import com.ibamb.udm.listener.UdmReloadParamsClickListener;
import com.ibamb.udm.module.net.IPUtil;
import com.ibamb.udm.task.ChannelParamReadAsyncTask;
import com.ibamb.udm.task.ChannelParamWriteAsynTask;
import com.ibamb.udm.util.TaskBarQuiet;
import com.ibamb.udm.util.ViewElementDataUtil;

import java.util.ArrayList;
import java.util.List;

public class TCPConnectionActivity extends AppCompatActivity {
    private ChannelParameter channelParameter;
    private View currentView;
    private String mac;
    private String channelId;
    private String ip;

    private ImageView commit;
    private ImageView back;
    private TextView title;

    private static final String[] TCP_SETTING_PARAMS_TAG = {"CONN_TCP_WORK_MODE", "CONN_TCP_CONN_RESPONS",
            "CONN_TCP_HOST_PORT0", "CONN_TCP_HOST_IP0", "CONN_TCP_LOCAL_PORT"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcpconnection);
        TaskBarQuiet.setStatusBarColor(this, UdmConstants.TASK_BAR_COLOR);

        Bundle bundle = getIntent().getExtras();
        mac = bundle.getString("HOST_MAC");
        channelId = bundle.getString("CHNL_ID");
        ip = bundle.getString("HOST_ADDRESS");
        currentView = getWindow().getDecorView();

        commit = findViewById(R.id.do_commit);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int errId = checkData();
                if (errId == 0) {
                    ChannelParameter changedParam = ViewElementDataUtil.getChangedData(currentView, channelParameter, channelId);
                    ChannelParamWriteAsynTask task = new ChannelParamWriteAsynTask(currentView);
                    task.execute(channelParameter, changedParam);
                } else {
                    EditText editText = findViewById(errId);
                    editText.requestFocus();
                    String notice = editText.getText().toString() + " is invalid.";
                    Snackbar.make(editText, notice, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

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
        title.setText("TCP Connection");
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            channelParameter = new ChannelParameter(mac, ip,channelId);
            List<Parameter> parameters = ParameterMapping.getMappingByTags(TCP_SETTING_PARAMS_TAG, channelId);
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
            Log.e(AccessSettingActivity.class.getName(), e.getMessage());
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    private int checkData() {
        EditText remoteHost = ((EditText) findViewById(R.id.udm_conn_tcp_host_ip0));

        if (!IPUtil.isIPAddress(remoteHost.getText().toString())) {
            return R.id.udm_conn_tcp_host_ip0;
        }
        return 0;
    }
}
