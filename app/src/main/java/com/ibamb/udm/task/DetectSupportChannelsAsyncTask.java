package com.ibamb.udm.task;

import android.os.AsyncTask;

import com.ibamb.dnet.module.api.UdmClient;
import com.ibamb.dnet.module.beans.DeviceParameter;
import com.ibamb.udm.component.constants.UdmConstant;

public class DetectSupportChannelsAsyncTask extends AsyncTask<String, String, Integer> {
    private DeviceParameter deviceParameter;

    public DetectSupportChannelsAsyncTask(DeviceParameter deviceParameter) {
        this.deviceParameter = deviceParameter;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        int maxSupport = 0;
        try {

            int tryCount = 0;
            /**
             * 如果无数据返回，重试3次。
             */
            UdmClient udmClient = UdmClient.getInstance();
            udmClient.readDeviceParameter(deviceParameter);
            while (!deviceParameter.isSuccessful() && tryCount < 3) {
                publishProgress(UdmConstant.WAIT_READ_PARAM);
                udmClient.readDeviceParameter(deviceParameter);
                tryCount++;
            }
            maxSupport = udmClient.detectMaxSupportedChannel(deviceParameter.getMac());
        } catch (Exception e) {

        }
        return maxSupport;
    }
}
