package com.ibamb.udm.listener;

import android.app.Activity;
import android.view.View;

import com.ibamb.dnet.module.log.UdmLog;
import com.ibamb.dnet.module.beans.DeviceParameter;
import com.ibamb.udm.task.ChannelParamReadAsyncTask;

/**
 * 重新读取参数值，用于因网络延迟等因素出现界面无数据时调用。
 */
public class UdmReloadParamsClickListener implements View.OnClickListener{

    private View view;
    private DeviceParameter deviceParameter;
    private Activity activity;

    public UdmReloadParamsClickListener(Activity activity,View view, DeviceParameter deviceParameter) {
        this.activity = activity;
        this.view = view;
        this.deviceParameter = deviceParameter;
    }

    @Override
    public void onClick(View v) {
        try{
            ChannelParamReadAsyncTask readerAsyncTask = new ChannelParamReadAsyncTask(activity,view, deviceParameter);
            readerAsyncTask.execute(deviceParameter.getMac());
        }catch (Exception ex){
            UdmLog.error(ex);
        }
    }
}
