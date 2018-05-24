package com.ibamb.udm.task;

import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.adapter.InetAddressListAdapter;
import com.ibamb.udm.beans.DeviceInfo;
import com.ibamb.udm.search.DeviceSearch;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by luotao on 18-4-14.
 */

public class DeviceSearchAsyncTask extends AsyncTask<String, String, ArrayList<DeviceInfo>> {

    private ListView mListView;
    private LayoutInflater inflater;
    private FloatingActionButton searchButton;
    private ArrayList<DeviceInfo> deviceList;
    private TextView  vSearchNotice;

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
        publishProgress("searching...");
        deviceList = DeviceSearch.searchDevice();
        if (deviceList == null) {
            deviceList = new ArrayList<>();
        }
        try {
            Thread.sleep(450);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        publishProgress(String.valueOf(deviceList.size()));

        return deviceList;
    }

    /**
     * 将搜索到的设备更新界面列表（主线程执行）
     * @param dataList
     */
    @Override
    protected void onPostExecute(ArrayList<DeviceInfo> dataList) {
        super.onPostExecute(dataList);
        mListView.setVisibility(View.VISIBLE);
        ListAdapter adapter = new InetAddressListAdapter(R.layout.item_device_layout, inflater, deviceList);
        mListView.setAdapter(adapter);
        String notice ="";
        if(dataList.size()==0){
            notice = "Possible network delay. Please try again.";
        }else{
            notice = "Device:" + String.valueOf(dataList.size());
        }
        Snackbar.make(searchButton, notice, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        vSearchNotice.setVisibility(View.GONE);
    }

    /**
     * 搜索进度
     * @param values
     */
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        mListView.setVisibility(View.GONE);
        vSearchNotice.setVisibility(View.VISIBLE);
        vSearchNotice.setText(values[0]);
    }

    public DeviceSearchAsyncTask(FloatingActionButton searchButton, ListView mListView,
                                 TextView vSearchNotice, LayoutInflater inflater) {
        this.mListView = mListView;
        this.inflater = inflater;
        this.searchButton = searchButton;
        this.vSearchNotice = vSearchNotice;
    }
}
