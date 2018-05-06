package com.ibamb.udm.listener;

import android.view.View;
import android.widget.TextView;

import com.ibamb.udm.task.UserLoginAsyncTask;

import java.net.DatagramSocket;

/**
 * Created by luotao on 18-4-21.
 */

public class UdmLoginDeviceButtonListener implements View.OnClickListener {
    private TextView noticeView;
    private DatagramSocket datagramSocket;
    private String[] loginParams;
    @Override
    public void onClick(View v) {

        UserLoginAsyncTask loginAsyncTask = new UserLoginAsyncTask();
        loginAsyncTask.execute(loginParams);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public UdmLoginDeviceButtonListener(String[] loginParams,TextView noticeView, DatagramSocket datagramSocket) {
        this.loginParams = loginParams;
        this.noticeView = noticeView;
        this.datagramSocket = datagramSocket;

    }
}
