package com.ibamb.udm.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.ibamb.udm.component.constants.ServiceConst;

public class DeviceSynchronizeReceiver  extends BroadcastReceiver {
    private View view;

    @Override
    public void onReceive(Context context, Intent intent) {
        int value = Integer.parseInt(intent.getStringExtra(ServiceConst.DEVICE_SYNCH_COUNT)) + 1;
    }
}
