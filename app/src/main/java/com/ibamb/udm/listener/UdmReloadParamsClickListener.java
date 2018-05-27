package com.ibamb.udm.listener;

import android.util.Log;
import android.view.View;

import com.ibamb.udm.module.beans.ChannelParameter;
import com.ibamb.udm.task.ChannelParamReadAsyncTask;

public class UdmReloadParamsClickListener implements View.OnClickListener{

    private View view;
    private ChannelParameter channelParameter;

    public UdmReloadParamsClickListener(View view, ChannelParameter channelParameter) {
        this.view = view;
        this.channelParameter = channelParameter;
    }

    @Override
    public void onClick(View v) {
        try{
            ChannelParamReadAsyncTask readerAsyncTask = new ChannelParamReadAsyncTask(view,channelParameter);
            readerAsyncTask.execute(channelParameter.getMac());
        }catch (Exception ex){
            Log.e(this.getClass().getName(),ex.getMessage());
        }
    }
}
