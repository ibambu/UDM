package com.ibamb.udm.task;

import android.os.AsyncTask;
import android.view.View;

import com.ibamb.udm.beans.ChannelParameter;
import com.ibamb.udm.instruct.IParamReader;
import com.ibamb.udm.instruct.impl.ParamReader;
import com.ibamb.udm.util.ViewElementDataUtil;

/**
 * Created by luotao on 18-4-21.
 */

public class ChannelParamReadAsyncTask extends AsyncTask<String, String, ChannelParameter> {

    private View view;
    private ChannelParameter channelParameter;

    public ChannelParamReadAsyncTask(View view,ChannelParameter channelParameter) {
        this.view = view;
        this.channelParameter = channelParameter;
    }

    @Override
    protected ChannelParameter doInBackground(String... strings) {
        try {
            IParamReader reader = new ParamReader();
            reader.readChannelParam(channelParameter);
        } catch (Exception e) {

        }
        return channelParameter;
    }

    @Override
    protected void onPostExecute(ChannelParameter channelParameter) {
        super.onPostExecute(channelParameter);
        //更新界面数据
        ViewElementDataUtil.setData(channelParameter, view);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
}
