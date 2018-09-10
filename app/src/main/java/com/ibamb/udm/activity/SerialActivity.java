package com.ibamb.udm.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.component.constants.UdmConstant;
import com.ibamb.udm.listener.UdmGestureListener;
import com.ibamb.dnet.module.log.UdmLog;
import com.ibamb.dnet.module.beans.ChannelParameter;
import com.ibamb.dnet.module.beans.ParameterItem;
import com.ibamb.dnet.module.constants.Constants;
import com.ibamb.dnet.module.core.ParameterMapping;
import com.ibamb.dnet.module.instruct.beans.Parameter;
import com.ibamb.udm.listener.UdmReloadParamsClickListener;
import com.ibamb.udm.task.ChannelParamReadAsyncTask;
import com.ibamb.udm.task.ChannelParamWriteAsynTask;
import com.ibamb.udm.util.TaskBarQuiet;
import com.ibamb.udm.util.ViewElementDataUtil;

import java.util.ArrayList;
import java.util.List;

public class SerialActivity extends AppCompatActivity implements View.OnTouchListener{

    private ChannelParameter channelParameter;
    private View currentView;
    private String mac;
    private String ip;
    private String channelId;

    private ImageView commit;
    private ImageView back;
    private TextView title;

    private static final String[] SERIAL_SETTING_PARAMS_TAG = {"UART_STOPBIT", "UART_DATABIT", "UART_BDRATE","UART_PARITY",
            "UART_FLOWRNTRL", "UART_IDLE_TIME_PACKING","CONN_LINK_LATCH_TIMEOUT","UART_WORK_MODE","CONN_MERGE_TIMEOUT"};

    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial);
        TaskBarQuiet.setStatusBarColor(this, UdmConstant.TASK_BAR_COLOR);

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
        title.setText(UdmConstant.TITLE_SERIAL_SETTING);
    }

    @Override
    protected void onStart() {
        super.onStart();
        try{
            channelParameter = new ChannelParameter(mac,ip, channelId);
            List<Parameter> parameters = ParameterMapping.getInstance().getMappingByTags(SERIAL_SETTING_PARAMS_TAG,channelId);
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

            findViewById(R.id.udm_serial_protocol).setOnTouchListener(this);
            findViewById(R.id.udm_uart_bdrate).setOnTouchListener(this);
            findViewById(R.id.udm_uart_databit).setOnTouchListener(this);
            findViewById(R.id.udm_uart_stopbit).setOnTouchListener(this);
            findViewById(R.id.udm_uart_flowrnt).setOnTouchListener(this);
            findViewById(R.id.udm_uart_parity).setOnTouchListener(this);
            findViewById(R.id.udm_uart_idle_time_packingr).setOnTouchListener(this);
            findViewById(R.id.udm_conn_uart_out_ifg).setOnTouchListener(this);
            findViewById(R.id.udm_conn_link_latch_timeout).setOnTouchListener(this);
            findViewById(R.id.v_gesture).setOnTouchListener(this);
            findViewById(R.id.v_gesture1).setOnTouchListener(this);
            findViewById(R.id.v_gesture2).setOnTouchListener(this);
            findViewById(R.id.v_gesture3).setOnTouchListener(this);
            findViewById(R.id.v_gesture4).setOnTouchListener(this);
            findViewById(R.id.v_gesture5).setOnTouchListener(this);
            findViewById(R.id.v_gesture6).setOnTouchListener(this);
            findViewById(R.id.v_gesture7).setOnTouchListener(this);
        }catch (Exception e){
            UdmLog.error(e);
        }
    }

    @Override
    public void finish() {
        super.finish();
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
