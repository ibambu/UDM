package com.ibamb.udm.task;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ibamb.udm.R;
import com.ibamb.udm.adapter.InetAddressListAdapter;
import com.ibamb.udm.module.beans.DeviceInfo;
import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.module.search.DeviceSearch;

import java.util.ArrayList;


public class DeviceSearchAsyncTask extends AsyncTask<String, String, ArrayList<DeviceInfo>> {

    private ListView mListView;
    private LayoutInflater inflater;
    private ArrayList<DeviceInfo> deviceList;
    private TextView vSearchNotice;

    /**
     * 后台搜索设备（工作线程执行）
     *
     * @param strings
     * @return
     */
    @Override
    protected ArrayList<DeviceInfo> doInBackground(String... strings) {
        if (deviceList != null) {
            deviceList.clear();
        }

        String keyword = strings != null && strings.length > 0 ? strings[0] : null;
        publishProgress(Constants.INFO_SEARCH_PROGRESS);
        deviceList = DeviceSearch.searchDevice(keyword);
        if (deviceList == null) {
            int tryMaxCount = 3;
            for (int i = tryMaxCount; i > 0; i--) {
                deviceList = DeviceSearch.searchDevice(keyword);
                if (deviceList != null && !deviceList.isEmpty()) {
                    break;
                }
            }
            if(deviceList== null ){
                deviceList = new ArrayList<>();
            }
        }
//        try {
//            Thread.sleep(200);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        publishProgress(String.valueOf(deviceList.size()));

        return deviceList;
    }

    /**
     * 将搜索到的设备更新界面列表（主线程执行）
     *
     * @param dataList
     */
    @Override
    protected void onPostExecute(ArrayList<DeviceInfo> dataList) {
        super.onPostExecute(dataList);
        mListView.setVisibility(View.VISIBLE);
        ListAdapter adapter = new InetAddressListAdapter(R.layout.item_device_layout, inflater, deviceList);
        mListView.setAdapter(adapter);
        String notice = "";
        if (dataList.size() == 0) {
            notice = Constants.INFO_SEARCH_FAIL;
        } else {
            notice = "Found Device:" + String.valueOf(dataList.size());
        }
        Toast.makeText(mListView.getContext(), notice, Toast.LENGTH_SHORT).show();
//        Snackbar.make(mListView, notice, Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
        vSearchNotice.setVisibility(View.GONE);
    }

    /**
     * 搜索进度
     *
     * @param values
     */
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        mListView.setVisibility(View.GONE);
        vSearchNotice.setVisibility(View.VISIBLE);
        vSearchNotice.setText(values[0]);
    }

    public DeviceSearchAsyncTask(ListView mListView, TextView vSearchNotice, LayoutInflater inflater) {
        this.mListView = mListView;
        this.inflater = inflater;
        this.vSearchNotice = vSearchNotice;
    }
}
