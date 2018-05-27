package com.ibamb.udm.task;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.ibamb.udm.R;
import com.ibamb.udm.module.beans.ChannelParameter;
import com.ibamb.udm.module.instruct.IParamReader;
import com.ibamb.udm.module.instruct.impl.ParamReader;
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
        String notice = "";
        if(!channelParameter.isSuccessful()){
            notice = "Possible network delay. Please click title try again.";
            Snackbar.make(view.findViewById(R.id.anchor),  notice, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else{
            ViewElementDataUtil.setData(channelParameter, view);
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
}
