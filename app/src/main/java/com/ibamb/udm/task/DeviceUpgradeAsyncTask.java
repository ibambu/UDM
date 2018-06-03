package com.ibamb.udm.task;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.adapter.DeviceUpgradeListAdapter;
import com.ibamb.udm.module.beans.DeviceInfo;

public class DeviceUpgradeAsyncTask extends AsyncTask<DeviceInfo, Boolean, String> {
    private ListView deviceList;

    @Override
    protected String doInBackground(DeviceInfo... deviceInfos) {
        DeviceUpgradeListAdapter adapter = (DeviceUpgradeListAdapter)deviceList.getAdapter();


        int count = adapter.getCount();
        for(int i=0;i<count;i++){
            DeviceInfo deviceInfo = (DeviceInfo)adapter.getItem(i);

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
