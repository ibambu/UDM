package com.ibamb.udm.task;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.ibamb.dnet.module.api.UdmClient;
import com.ibamb.dnet.module.beans.DeviceParameter;
import com.ibamb.dnet.module.log.UdmLog;
import com.ibamb.udm.R;
import com.ibamb.udm.component.constants.UdmConstant;
import com.ibamb.udm.util.ViewElementDataUtil;



public class ChannelParamWriteAsynTask extends AsyncTask <DeviceParameter, String, DeviceParameter> {

    private View view;

    public ChannelParamWriteAsynTask(View view) {
        this.view = view;
    }

    @Override
    protected DeviceParameter doInBackground(DeviceParameter... deviceParameters) {
        DeviceParameter oldParams = null;
        DeviceParameter changedParams = null;
        try{
            oldParams = deviceParameters[0];
            changedParams = deviceParameters[1];
            changedParams = UdmClient.getInstance().writeDeviceParameter(changedParams);
            String retMessage = changedParams.isSuccessful()? UdmConstant.INFO_SUCCESS:UdmConstant.INFO_FAIL;
            onProgressUpdate(retMessage);
            //修改后要重新读取一次
            oldParams = UdmClient.getInstance().readDeviceParameter(oldParams);
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
    protected void onPostExecute(DeviceParameter deviceParameter) {
        super.onPostExecute(deviceParameter);
        //更新界面数据
        String notice = "";
        if(!deviceParameter.isSuccessful()){
            notice = UdmConstant.INFO_READ_PARAM_FAIL;
//            Toast.makeText(view.getContext(), String.valueOf(notice), Toast.LENGTH_SHORT).show();
            Snackbar.make(view.findViewById(R.id.anchor),  notice, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else{
            ViewElementDataUtil.setData(deviceParameter, view);
        }
        //更新界面数据
        ViewElementDataUtil.setData(deviceParameter, view);
    }
}
