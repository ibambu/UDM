package com.ibamb.udm.task;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.ibamb.udm.R;
import com.ibamb.udm.log.UdmLog;
import com.ibamb.udm.module.beans.ChannelParameter;
import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.module.instruct.IParamReader;
import com.ibamb.udm.module.instruct.IParamWriter;
import com.ibamb.udm.module.instruct.ParamReader;
import com.ibamb.udm.module.instruct.ParamWriter;
import com.ibamb.udm.util.ViewElementDataUtil;



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
            String retMessage = changedParams.isSuccessful()?Constants.INFO_SUCCESS:Constants.INFO_FAIL;
            onProgressUpdate(retMessage);
            //修改后要重新读取一次
            IParamReader reader = new ParamReader();
            oldParams = reader.readChannelParam(oldParams);
        }catch (Exception e){
            UdmLog.e(this.getClass().getName(),e.getMessage());
        }
        return oldParams;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Snackbar.make(view.findViewById(R.id.anchor),  String.valueOf(values[0]), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    protected void onPostExecute(ChannelParameter channelParameter) {
        super.onPostExecute(channelParameter);
        //更新界面数据
        String notice = "";
        if(!channelParameter.isSuccessful()){
            notice = Constants.INFO_READ_PARAM_FAIL;
            Snackbar.make(view.findViewById(R.id.anchor),  notice, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else{
            ViewElementDataUtil.setData(channelParameter, view);
        }
        //更新界面数据
        ViewElementDataUtil.setData(channelParameter, view);
    }
}
