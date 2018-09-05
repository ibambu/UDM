package com.ibamb.udm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.listener.UdmGestureListener;
import com.ibamb.udm.listener.UdmReloadParamsClickListener;
import com.ibamb.dnet.module.beans.ChannelParameter;
import com.ibamb.dnet.module.beans.ParameterItem;
import com.ibamb.dnet.module.constants.Constants;
import com.ibamb.dnet.module.core.ParameterMapping;
import com.ibamb.dnet.module.instruct.beans.Parameter;
import com.ibamb.dnet.module.log.UdmLog;
import com.ibamb.udm.task.ChannelParamReadAsyncTask;
import com.ibamb.udm.task.ChannelParamWriteAsynTask;
import com.ibamb.udm.util.TaskBarQuiet;
import com.ibamb.udm.util.ViewElementDataUtil;

import java.util.ArrayList;
import java.util.List;

public class ConnectSettingActivity extends AppCompatActivity implements View.OnTouchListener {

    private TextView vTcpSetting;
    private TextView vUdpSetting;
    private TextView vSerailSetting;


    private ImageView vTcpMore;
    private ImageView vUdpMore;
    private ImageView vSerailMore;

    private Switch vTcpMode;
    private Switch vUdpMode;
    private Switch vBothMode;

    private ChannelParameter channelParameter;
    private View currentView;
    private String mac;
    private String channelId;

    private String ip;

    private TextView title;
    private ImageView back;
    private ImageView commit;

    private static final String[] CONNECT_SETTING_PARAMS_TAG = {"CONN_NET_PROTOCOL"};

    private GestureDetector mGestureDetector;

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


        vTcpMode = findViewById(R.id.tcp_enanbled_switch);
        vTcpMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vUdpMode.setChecked(false);
                vBothMode.setChecked(false);
            }
        });
        vUdpMode = findViewById(R.id.udp_enanbled_switch);
        vUdpMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vTcpMode.setChecked(false);
                vBothMode.setChecked(false);
            }
        });
        vBothMode = findViewById(R.id.tcp_udp_enanbled_switch);
        vBothMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vTcpMode.setChecked(false);
                vUdpMode.setChecked(false);
            }
        });

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
            List<Parameter> parameters = ParameterMapping.getInstance().getMappingByTags(CONNECT_SETTING_PARAMS_TAG, channelId);
            List<ParameterItem> items = new ArrayList<>();
            for (Parameter parameter : parameters) {
                items.add(new ParameterItem(parameter.getId(), null));
            }
            channelParameter.setParamItems(items);
            //点击重新读取参数值，并刷新界面。
            title.setOnClickListener(new UdmReloadParamsClickListener(this, currentView, channelParameter));
            ChannelParamReadAsyncTask readerAsyncTask = new ChannelParamReadAsyncTask(this, currentView, channelParameter);
            readerAsyncTask.execute(mac);

            /**
             * 监听手势
             */
            UdmGestureListener listener = new UdmGestureListener(this, channelParameter, currentView);
            mGestureDetector = new GestureDetector(this, listener);
            findViewById(R.id.v_gesture).setOnTouchListener(this);

            vTcpSetting.setOnTouchListener(this);
            vUdpSetting.setOnTouchListener(this);
            vSerailSetting.setOnTouchListener(this);

            changeProtocol();
        } catch (Exception e) {
            UdmLog.error(e);
        }
    }

    private void changeProtocol() {
        if (vTcpMode.isChecked()) {
            vUdpMode.setChecked(false);
            vBothMode.setChecked(false);
        } else if (vUdpMode.isChecked()) {
            vTcpMode.setChecked(false);
            vBothMode.setChecked(false);
        } else if (vBothMode.isChecked()) {
            vTcpMode.setChecked(false);
            vUdpMode.setChecked(false);
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
            changeProtocol();
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }
}
