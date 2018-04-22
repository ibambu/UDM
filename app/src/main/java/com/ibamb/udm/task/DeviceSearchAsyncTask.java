package com.ibamb.udm.task;

import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ibamb.udm.R;
import com.ibamb.udm.adapter.InetAddressListAdapter;
import com.ibamb.udm.beans.DeviceInfo;
import com.ibamb.udm.search.DeviceSearch;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by luotao on 18-4-14.
 */

public class DeviceSearchAsyncTask extends AsyncTask<String, Integer, ArrayList<DeviceInfo>> {

    private ListView mListView;
    private LayoutInflater inflater;
    private FloatingActionButton searchButton;
    private ArrayList<DeviceInfo> deviceList;

    /**
     * 后台搜索设备（工作线程执行）
     * @param strings
     * @return
     */
    @Override
    protected ArrayList<DeviceInfo> doInBackground(String... strings) {
        if (deviceList != null) {
            deviceList.clear();
        }
        deviceList = DeviceSearch.searchDevice();
        if (deviceList == null) {
            deviceList = new ArrayList<>();
        }
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        publishProgress(deviceList.size());

        return deviceList;
    }

    /**
     * 将搜索到的设备更新界面列表（主线程执行）
     * @param dataList
     */
    @Override
    protected void onPostExecute(ArrayList<DeviceInfo> dataList) {
        super.onPostExecute(dataList);
        ListAdapter adapter = new InetAddressListAdapter(R.layout.item_device_layout, inflater, deviceList);
        mListView.setAdapter(adapter);
    }

    /**
     * 搜索进度
     * @param values
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Snackbar.make(searchButton, "Device:" + String.valueOf(values[0]), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public DeviceSearchAsyncTask(FloatingActionButton searchButton,ListView mListView, LayoutInflater inflater) {
        this.mListView = mListView;
        this.inflater = inflater;
        this.searchButton = searchButton;
    }
}
