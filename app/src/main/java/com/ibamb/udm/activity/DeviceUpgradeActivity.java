package com.ibamb.udm.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.task.DeviceUpgradeAsyncTask;
import com.ibamb.udm.task.SearchUpgradeDeviceAsycTask;
import com.ibamb.udm.util.TaskBarQuiet;

public class DeviceUpgradeActivity extends AppCompatActivity {


    private ImageView search;
    private ImageView back;
    private TextView title;
    private TextView upgradeProgress;
    private TextView vSearchNotice;

    private Button upgradeButton;

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_upgrade);
        TaskBarQuiet.setStatusBarColor(this, Constants.TASK_BAR_COLOR);


        mListView = findViewById(R.id.upgrade_device_list);
        back = findViewById(R.id.go_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title = findViewById(R.id.title);
        title.setText(Constants.TITLE_DEVICE_UPGRADE);

        search = findViewById(R.id.do_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView vSearchNotice = findViewById(R.id.search_notice_info);
                SearchUpgradeDeviceAsycTask task = new SearchUpgradeDeviceAsycTask(mListView,vSearchNotice,getLayoutInflater());
                task.execute();
            }
        });

        vSearchNotice = findViewById(R.id.search_notice_info);

        upgradeProgress = findViewById(R.id.upgrade_progress);
        upgradeButton = findViewById(R.id.btn_upgrade_all);
        upgradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceUpgradeAsyncTask upgradeTask = new DeviceUpgradeAsyncTask(mListView);
                upgradeTask.execute();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
