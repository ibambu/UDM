package com.ibamb.udm.task;

import android.os.AsyncTask;

import com.ibamb.dnet.module.beans.DeviceParameter;
import com.ibamb.dnet.module.instruct.IParamReader;
import com.ibamb.dnet.module.instruct.ParamReader;
import com.ibamb.udm.component.constants.UdmConstant;

public class DetectSupportChannelsAsyncTask extends AsyncTask<String, String, DeviceParameter> {
    private DeviceParameter deviceParameter;

    public DetectSupportChannelsAsyncTask(DeviceParameter deviceParameter) {
        this.deviceParameter = deviceParameter;
    }

    @Override
    protected DeviceParameter doInBackground(String... strings) {
        try {

            IParamReader reader = new ParamReader();
            int tryCount = 0;
            /**
             * 如果无数据返回，重试3次。
             */
            reader.readDeviceParam(deviceParameter);
            while (!deviceParameter.isSuccessful() && tryCount < 3) {
                publishProgress(UdmConstant.WAIT_READ_PARAM);
                reader.readDeviceParam(deviceParameter);
                tryCount++;
            }

        } catch (Exception e) {

        }
        return deviceParameter;
    }
}
