package com.ibamb.udm.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ibamb.udm.R;
import com.ibamb.dnet.module.constants.Constants;
import com.ibamb.udm.component.constants.UdmConstant;
import com.ibamb.udm.util.TaskBarQuiet;

public class LoadParamDefActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_param_def);
        TaskBarQuiet.setStatusBarColor(this, UdmConstant.TASK_BAR_COLOR);

    }
}
