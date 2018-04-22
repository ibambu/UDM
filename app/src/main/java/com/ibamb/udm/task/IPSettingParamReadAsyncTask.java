package com.ibamb.udm.task;

import android.os.AsyncTask;
import android.view.View;

import com.ibamb.udm.beans.ChannelParameter;
import com.ibamb.udm.beans.ParameterItem;
import com.ibamb.udm.constants.UdmConstants;
import com.ibamb.udm.core.ParameterMapping;
import com.ibamb.udm.instruct.IParameterReaderWriter;
import com.ibamb.udm.instruct.beans.Parameter;
import com.ibamb.udm.instruct.impl.ParameterReaderWriter;
import com.ibamb.udm.net.UdmDatagramSocket;
import com.ibamb.udm.util.ViewElementDataUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luotao on 18-4-21.
 */

public class IPSettingParamReadAsyncTask extends AsyncTask<String, String, ChannelParameter> {

    private View vIPSettingView;

    public IPSettingParamReadAsyncTask(View vIPSettingView) {
        this.vIPSettingView = vIPSettingView;
    }

    @Override
    protected ChannelParameter doInBackground(String... strings) {
        String mac = strings[0];
        IParameterReaderWriter reader = new ParameterReaderWriter();
        List<Parameter> parameters = ParameterMapping.getChannelParamDef(UdmConstants.UDM_IP_SETTING_CHNL);//
        List<ParameterItem> items = new ArrayList<>();
        for(Parameter parameter:parameters){
            items.add(new ParameterItem(parameter.getId(),null));
        }
        ChannelParameter channelParameter = new ChannelParameter(mac, String.valueOf(UdmConstants.UDM_IP_SETTING_CHNL));
        channelParameter.setParamItems(items);
        reader.readChannelParam(UdmDatagramSocket.getDatagramSocket(),channelParameter);
        return channelParameter;
    }

    @Override
    protected void onPostExecute(ChannelParameter channelParameter) {
        super.onPostExecute(channelParameter);
        //更新界面数据
        ViewElementDataUtil.setData(channelParameter,vIPSettingView);

    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
}
