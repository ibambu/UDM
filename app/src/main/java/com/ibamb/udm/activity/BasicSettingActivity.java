package com.ibamb.udm.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.listener.UdmGestureListener;
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

public class BasicSettingActivity extends AppCompatActivity implements View.OnTouchListener{

    private ChannelParameter channelParameter;
    private View currentView;
    private String mac;
    private String ip;
    private String channelId;

    private ImageView commit;
    private ImageView back;
    private TextView title;

    private GestureDetector mGestureDetector;

    private static final String[] ACCESS_SETTING_PARAMS_TAG = {"BASIC_WEB_CONSOLE", "BASIC_TELNET_CONSOLE", "BASIC_CMD_TCP_CONSOLE"};

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
            title.setOnClickListener(new UdmReloadParamsClickListener(this,currentView,channelParameter));
            ChannelParamReadAsyncTask readerAsyncTask = new ChannelParamReadAsyncTask(this,currentView,channelParameter);
            readerAsyncTask.execute(mac);

            /**
             * 监听手势
             */
            UdmGestureListener listener = new UdmGestureListener(this,channelParameter,currentView);
            mGestureDetector = new GestureDetector(this, listener);
            findViewById(R.id.v_gesture).setOnTouchListener(this);
            findViewById(R.id.v_gesture_1).setOnTouchListener(this);
            findViewById(R.id.v_gesture_2).setOnTouchListener(this);
            findViewById(R.id.v_gesture_3).setOnTouchListener(this);
            findViewById(R.id.v_gesture_4).setOnTouchListener(this);
            findViewById(R.id.id_http_access).setOnTouchListener(this);
            findViewById(R.id.id_telnet_access).setOnTouchListener(this);
            findViewById(R.id.id_cloud_access).setOnTouchListener(this);
            findViewById(R.id.id_cmd_tcp_access).setOnTouchListener(this);
        }catch (Exception e){
            UdmLog.e(BasicSettingActivity.class.getName(),e.getMessage());
        }

    }

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
