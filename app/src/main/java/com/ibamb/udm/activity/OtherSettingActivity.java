package com.ibamb.udm.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ibamb.udm.R;
import com.ibamb.udm.log.UdmLog;
import com.ibamb.udm.module.beans.ChannelParameter;
import com.ibamb.udm.module.beans.ParameterItem;
import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.module.core.ParameterMapping;
import com.ibamb.udm.module.instruct.beans.Parameter;
import com.ibamb.udm.listener.UdmReloadParamsClickListener;
import com.ibamb.udm.task.ChannelParamReadAsyncTask;
import com.ibamb.udm.task.ChannelParamWriteAsynTask;
import com.ibamb.udm.util.TaskBarQuiet;
import com.ibamb.udm.util.ViewElementDataUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OtherSettingActivity extends AppCompatActivity {
    private ChannelParameter channelParameter;
    private View currentView;
    private String mac;
    private String ip;
    private String channelId;

    private ImageView commit;
    private ImageView back;
    private TextView title;

    private EditText localDate;
    private EditText localTime;
    private static final String[] OTH_SETTING_PARAMS_TAG = {"BASIC_TIMER_SERVER","BASIC_RTC_TIME"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_setting);
        TaskBarQuiet.setStatusBarColor(this, Constants.TASK_BAR_COLOR);

        Bundle bundle = getIntent().getExtras();
        mac = bundle.getString("HOST_MAC");
        ip = bundle.getString("HOST_ADDRESS");
        channelId = bundle.getString("CHNL_ID");
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
                ChannelParameter changedParam = ViewElementDataUtil.getChangedData(currentView,channelParameter,channelId);
                ChannelParamWriteAsynTask task = new ChannelParamWriteAsynTask(currentView);
                task.execute(channelParameter,changedParam);
            }
        });

        title = findViewById(R.id.title);

        title.setText(Constants.TITLE_OTHER_SETTING);

        localDate = findViewById(R.id.id_local_date);
        localDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });

        localDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlg();
                }
            }
        });

        localTime = findViewById(R.id.id_local_time);
        localTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDateTimePickDlg();
                    return true;
                }
                return false;
            }
        });

        localTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDateTimePickDlg();
                }
            }
        });

    }

    protected void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(OtherSettingActivity.this,
                new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                localDate.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    protected void showDateTimePickDlg() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                localTime.setText(hour + ":" + minute+":00");
            }
        }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            channelParameter = new ChannelParameter(mac, ip, Constants.UDM_IP_SETTING_CHNL);
            List<Parameter> parameters = ParameterMapping.getMappingByTags(OTH_SETTING_PARAMS_TAG, channelId);
            List<ParameterItem> items = new ArrayList<>();
            for (Parameter parameter : parameters) {
                items.add(new ParameterItem(parameter.getId(), null));
            }
            channelParameter.setParamItems(items);
            //点击重新读取参数值，并刷新界面。
            title.setOnClickListener(new UdmReloadParamsClickListener(currentView,channelParameter));
            ChannelParamReadAsyncTask readerAsyncTask = new ChannelParamReadAsyncTask(currentView, channelParameter);
            readerAsyncTask.execute(mac);
        } catch (Exception e) {
            UdmLog.e(AccessSettingActivity.class.getName(), e.getMessage());
        }
    }

}
