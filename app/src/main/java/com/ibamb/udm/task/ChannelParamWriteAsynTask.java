package com.ibamb.udm.task;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.ibamb.dnet.module.beans.ChannelParameter;
import com.ibamb.dnet.module.instruct.IParamReader;
import com.ibamb.dnet.module.instruct.IParamWriter;
import com.ibamb.dnet.module.instruct.ParamReader;
import com.ibamb.dnet.module.instruct.ParamWriter;
import com.ibamb.dnet.module.log.UdmLog;
import com.ibamb.udm.R;
import com.ibamb.udm.component.constants.UdmConstant;
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
            String retMessage = changedParams.isSuccessful()? UdmConstant.INFO_SUCCESS:UdmConstant.INFO_FAIL;
            onProgressUpdate(retMessage);
            //修改后要重新读取一次
            IParamReader reader = new ParamReader();
            oldParams = reader.readChannelParam(oldParams);
        }catch (Exception e){
            UdmLog.error(e);
        }
        return oldParams;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
//        Toast.makeText(view.getContext(), String.valueOf(values[0]), Toast.LENGTH_SHORT).show();
        Snackbar.make(view.findViewById(R.id.anchor),  String.valueOf(values[0]), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    protected void onPostExecute(ChannelParameter channelParameter) {
        super.onPostExecute(channelParameter);
        //更新界面数据
        String notice = "";
        if(!channelParameter.isSuccessful()){
            notice = UdmConstant.INFO_READ_PARAM_FAIL;
//            Toast.makeText(view.getContext(), String.valueOf(notice), Toast.LENGTH_SHORT).show();
            Snackbar.make(view.findViewById(R.id.anchor),  notice, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else{
            ViewElementDataUtil.setData(channelParameter, view);
        }
        //更新界面数据
        ViewElementDataUtil.setData(channelParameter, view);
    }
}
