package com.ibamb.udm.task;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.ibamb.udm.R;
import com.ibamb.udm.beans.ChannelParameter;
import com.ibamb.udm.instruct.IParamReader;
import com.ibamb.udm.instruct.IParamWriter;
import com.ibamb.udm.instruct.impl.ParamReader;
import com.ibamb.udm.instruct.impl.ParamWriter;
import com.ibamb.udm.util.ViewElementDataUtil;

/**
 * Created by luotao on 18-4-30.
 */

public class ChannelParamWriteAsynTask extends AsyncTask <ChannelParameter, String, ChannelParameter> {

    private View view;

    public ChannelParamWriteAsynTask(View view) {
        this.view = view;
    }

    @Override
    protected ChannelParameter doInBackground(ChannelParameter... channelParameters) {
        ChannelParameter oldParams = null;
        ChannelParameter changedParams = null;
        try{
            oldParams = channelParameters[0];
            changedParams = channelParameters[1];
            IParamWriter writer = new ParamWriter();
            changedParams = writer.writeChannelParam(changedParams);
            String retMessage = changedParams.isSuccessful()?"successful":"fail";
            onProgressUpdate(retMessage);
            //修改后要重新读取一次
            IParamReader reader = new ParamReader();
            oldParams = reader.readChannelParam(oldParams);
        }catch (Exception e){
            Log.e(this.getClass().getName(),e.getMessage());
        }
        return oldParams;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Snackbar.make(view.findViewById(R.id.id_conect_setting_commit),  String.valueOf(values[0]), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    protected void onPostExecute(ChannelParameter channelParameter) {
        super.onPostExecute(channelParameter);
        //更新界面数据
        ViewElementDataUtil.setData(channelParameter, view);
    }
}
