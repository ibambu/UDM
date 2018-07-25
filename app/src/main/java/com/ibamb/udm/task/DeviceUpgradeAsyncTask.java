package com.ibamb.udm.task;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;

import com.ibamb.udm.adapter.DeviceListAdapter;
import com.ibamb.udm.module.beans.DeviceModel;

public class DeviceUpgradeAsyncTask extends AsyncTask<DeviceModel, Boolean, String> {
    private ListView deviceList;

    @Override
    protected String doInBackground(DeviceModel... deviceInfos) {
        DeviceListAdapter adapter = (DeviceListAdapter)deviceList.getAdapter();


        int count = adapter.getCount();
        for(int i=0;i<count;i++){
            DeviceModel deviceInfo = (DeviceModel)adapter.getItem(i);

            for(int k=1;k<101;k++){
                deviceInfo.setUpgradeProgress(k);
                publishProgress(true);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        deviceList.setVisibility(View.VISIBLE);
        super.onPostExecute(s);
    }

    @Override
    protected void onProgressUpdate(Boolean... values) {

        deviceList.setAdapter(deviceList.getAdapter());
        super.onProgressUpdate(values);
    }

    public DeviceUpgradeAsyncTask(ListView deviceList) {
        this.deviceList = deviceList;

    }
}
