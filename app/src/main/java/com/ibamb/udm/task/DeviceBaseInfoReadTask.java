package com.ibamb.udm.task;

import android.os.AsyncTask;

import com.ibamb.dnet.module.api.UdmClient;
import com.ibamb.dnet.module.beans.DeviceBaseInfo;

public class DeviceBaseInfoReadTask extends AsyncTask<String, String, DeviceBaseInfo> {

    private ResultListener resultListener;
    private int channelId;

    public DeviceBaseInfoReadTask(ResultListener resultListener, int channelId) {
        this.resultListener = resultListener;
        this.channelId = channelId;
    }

    @Override
    protected DeviceBaseInfo doInBackground(String... strings) {
        return UdmClient.getInstance().getDeviceBaseInfo(strings[0]);
    }

    @Override
    protected void onPostExecute(DeviceBaseInfo deviceBaseInfo) {
        super.onPostExecute(deviceBaseInfo);
        if (deviceBaseInfo != null) {
            for (DeviceBaseInfo.ComBaseInfo baseInfo : deviceBaseInfo.getComBaseInfoList()) {
                if (baseInfo.getComId() == channelId) {
                    resultListener.onCompleted(baseInfo);
                    break;
                }
            }
        }
    }

    public interface ResultListener {
        public void onCompleted(DeviceBaseInfo.ComBaseInfo baseInfo);
    }
}
