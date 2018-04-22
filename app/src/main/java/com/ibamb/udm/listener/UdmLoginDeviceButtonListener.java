package com.ibamb.udm.listener;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ibamb.udm.activity.LoginActivity;
import com.ibamb.udm.activity.MainActivity;
import com.ibamb.udm.net.UdmDatagramSocket;
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
        if(datagramSocket!=null){
//            Intent intent = new Intent((MainActivity) getActivity(), LoginActivity.class);
//            Bundle params = new Bundle();
//            params.putString("HOST_ADDRESS", ip);
//            params.putString("HOST_MAC", mac);
//            intent.putExtras(params);
//            params.putParcelable();
//            startActivityForResult(intent, 1);
        }
    }

    public UdmLoginDeviceButtonListener(String[] loginParams,TextView noticeView, DatagramSocket datagramSocket) {
        this.loginParams = loginParams;
        this.noticeView = noticeView;
        this.datagramSocket = datagramSocket;

    }
}
