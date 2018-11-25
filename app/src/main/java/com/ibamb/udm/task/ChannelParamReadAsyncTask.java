package com.ibamb.udm.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ibamb.dnet.module.api.UdmClient;
import com.ibamb.dnet.module.beans.DeviceParameter;
import com.ibamb.udm.R;
import com.ibamb.udm.component.constants.UdmConstant;
import com.ibamb.udm.component.login.LoginComponent;
import com.ibamb.udm.util.ViewElementDataUtil;


public class ChannelParamReadAsyncTask extends AsyncTask<String, String, DeviceParameter> {

    private View view;
    private DeviceParameter deviceParameter;
    private Activity activity;

    public ChannelParamReadAsyncTask(Activity activity, View view, DeviceParameter deviceParameter) {
        this.activity = activity;
        this.view = view;
        this.deviceParameter = deviceParameter;
    }

    @Override
    protected DeviceParameter doInBackground(String... strings) {
        try {

            int tryCount = 0;
            /**
             * 如果无数据返回，重试3次。
             */
            UdmClient udmClient = UdmClient.getInstance();
            udmClient.readDeviceParameter(deviceParameter);
            while (!deviceParameter.isSuccessful() && tryCount < 3) {
                publishProgress(UdmConstant.WAIT_READ_PARAM);
                udmClient.readDeviceParameter(deviceParameter);
                tryCount++;
            }
        } catch (Exception e) {

        }
        return deviceParameter;
    }

    @Override
    protected void onPostExecute(DeviceParameter deviceParameter) {
        super.onPostExecute(deviceParameter);
        //更新界面数据
        String notice = "";
        if (deviceParameter.isNoPermission()) {
            LoginComponent loginComponent = new LoginComponent(activity, deviceParameter.getMac(), deviceParameter.getIp());
            loginComponent.setToProfile(false);
            loginComponent.login();
        } else {
            if (!deviceParameter.isSuccessful()) {
                notice = "Possible network delay. Please click title try again.";
                Toast.makeText(view.getContext(), notice, Toast.LENGTH_SHORT).show();
            } else {
                if (view != null) {
                    ViewElementDataUtil.setData(deviceParameter, view);
                }
            }
            if (view != null) {
                TextView title = view.findViewById(R.id.title);
                if (title != null) {
                    String titleValue = title.getText().toString();
                    title.setText(titleValue.replaceAll(UdmConstant.WAIT_READ_PARAM, ""));
                }
            }
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        if (view != null) {
            TextView title = view.findViewById(R.id.title);
            if (title != null && !title.getText().toString().contains(UdmConstant.WAIT_READ_PARAM)) {
                title.setText(title.getText().toString() + UdmConstant.WAIT_READ_PARAM);
            }
        }
        super.onProgressUpdate(values);
    }
}
