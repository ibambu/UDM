package com.ibamb.udm.task;

import android.os.AsyncTask;

import com.ibamb.dnet.module.beans.ChannelParameter;
import com.ibamb.dnet.module.constants.Constants;
import com.ibamb.dnet.module.instruct.IParamReader;
import com.ibamb.dnet.module.instruct.ParamReader;
import com.ibamb.udm.component.constants.UdmConstant;

public class DetectSupportChannelsAsyncTask extends AsyncTask<String, String, ChannelParameter> {
    private ChannelParameter channelParameter;

    public DetectSupportChannelsAsyncTask(ChannelParameter channelParameter) {
        this.channelParameter = channelParameter;
    }

    @Override
    protected ChannelParameter doInBackground(String... strings) {
        try {

            IParamReader reader = new ParamReader();
            int tryCount = 0;
            /**
             * 如果无数据返回，重试3次。
             */
            reader.readChannelParam(channelParameter);
            while (!channelParameter.isSuccessful() && tryCount < 3) {
                publishProgress(UdmConstant.WAIT_READ_PARAM);
                reader.readChannelParam(channelParameter);
                tryCount++;
            }

        } catch (Exception e) {

        }
        return channelParameter;
    }
}
