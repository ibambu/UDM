package com.ibamb.udm.listener;

import android.app.Activity;
import android.view.View;

import com.ibamb.udm.log.UdmLog;
import com.ibamb.udm.module.beans.ChannelParameter;
import com.ibamb.udm.task.ChannelParamReadAsyncTask;

/**
 * 重新读取参数值，用于因网络延迟等因素出现界面无数据时调用。
 */
public class UdmReloadParamsClickListener implements View.OnClickListener{

    private View view;
    private ChannelParameter channelParameter;
    private Activity activity;

    public UdmReloadParamsClickListener(Activity activity,View view, ChannelParameter channelParameter) {
        this.activity = activity;
        this.view = view;
        this.channelParameter = channelParameter;
    }

    @Override
    public void onClick(View v) {
        try{
            ChannelParamReadAsyncTask readerAsyncTask = new ChannelParamReadAsyncTask(activity,view,channelParameter);
            readerAsyncTask.execute(channelParameter.getMac());
        }catch (Exception ex){
            UdmLog.e(this.getClass().getName(),ex.getMessage());
        }
    }
}
