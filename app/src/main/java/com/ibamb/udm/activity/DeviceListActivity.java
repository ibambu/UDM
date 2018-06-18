package com.ibamb.udm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.adapter.DeviceListAdapter;
import com.ibamb.udm.module.beans.DeviceInfo;
import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.module.core.ContextData;
import com.ibamb.udm.util.TaskBarQuiet;

import java.util.ArrayList;

public class DeviceListActivity extends AppCompatActivity {

    private ListView mListView;
    private TextView checkAll;
    private TextView title;
    private ImageView back;

    private ListAdapter adapter;
    private ArrayList<DeviceInfo> deviceInfos;

    private Button synchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        TaskBarQuiet.setStatusBarColor(this, Constants.TASK_BAR_COLOR);
        /**
         * 清空选中状态
         */
        ContextData contextData = ContextData.getInstance();
        contextData.cleanChecked();
        deviceInfos = contextData.getDataInfos();

        title = findViewById(R.id.title);//标题
        /**
         * 返回事件
         */
        back = findViewById(R.id.go_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /**
         * 全选事件
         */
        checkAll = findViewById(R.id.all_check);

        checkAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContextData contextData = ContextData.getInstance();
                if (!contextData.isCheckAll()) {

                    for (DeviceInfo deviceInfo : deviceInfos) {
                        deviceInfo.setChecked(true);
                    }
                    checkAll.setText("Cancel All");
                } else {

                    for (DeviceInfo deviceInfo : deviceInfos) {
                        deviceInfo.setChecked(false);
                    }
                    checkAll.setText("Check All");
                }
                mListView.setAdapter(adapter);
                title.setText("Checked Items:"+((DeviceListAdapter)adapter).getCheckedCount());
            }
        });



        mListView = findViewById(R.id.common_device_list);
        Button btn= findViewById(R.id.button_synchronize);
        /**
         * 重新计算ListView需要占用高度，避免底部按钮遮拦列表末尾数据。
         */
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        btn.measure(w, h);
        int height =btn.getMeasuredHeight();

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, height);
        mListView.setLayoutParams(lp);
        /**
         * 设置多选模式，并初始化数据。
         */
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        adapter = new DeviceListAdapter(R.layout.common_item_device_layout, getLayoutInflater(),
                deviceInfos,title,checkAll);
        mListView.setAdapter(adapter);
        /**
         * 设置点击事件
         */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = view.findViewById(R.id.common_check_box);
                DeviceInfo deviceInfo = deviceInfos.get(position);
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                    deviceInfo.setChecked(false);

                } else {
                    checkBox.setChecked(true);
                    deviceInfo.setChecked(true);

                }
                ContextData contextData = ContextData.getInstance();
                title.setText("Checked Items:"+contextData.getCheckedItems());
            }
        });

        synchButton = findViewById(R.id.button_synchronize);
        synchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),DeviceSynchActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
