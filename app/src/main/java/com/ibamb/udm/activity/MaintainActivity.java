package com.ibamb.udm.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ibamb.dnet.module.core.ContextData;
import com.ibamb.udm.R;
import com.ibamb.udm.task.AllDeviceMaintainAsyncTask;

public class MaintainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintain);

        findViewById(R.id.do_commit).setVisibility(View.INVISIBLE);
        findViewById(R.id.go_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.action_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextData.getInstance().getDataInfos().size() > 0) {
                    ProgressBar progressBar = findViewById(R.id.upgrade_progress_bar);
                    progressBar.setVisibility(View.VISIBLE);
                    AllDeviceMaintainAsyncTask task = new AllDeviceMaintainAsyncTask(MaintainActivity.this);
                    task.execute();
                } else {
                    Toast.makeText(MaintainActivity.this, "Please search device first.", Toast.LENGTH_LONG).show();
                }

            }
        });

        TextView textView = findViewById(R.id.upgrade_progress);
        textView.setVisibility(View.VISIBLE);
        textView.setText("Maintain Count: " + "0/" + ContextData.getInstance().getDataInfos().size());
    }
}
