package com.ibamb.udm.listener;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ibamb.udm.task.DeviceSearchAsyncTask;


public class UdmSearchButtonClickListener implements View.OnClickListener{
    private ListView mListView;
    private LayoutInflater inflater;
    private TextView vSearchNotice;
    private String keyword;
    @Override
    public void onClick(View v) {
        //通过异步任务开启工作线程在后台搜索设备。
        DeviceSearchAsyncTask task = new DeviceSearchAsyncTask();
        task.execute();
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * 通过构造函数将所需要的参数从主线程传入。
     * @param mListView
     * @param inflater
     */
    public UdmSearchButtonClickListener(ListView mListView,
                                        TextView vSearchNotice, LayoutInflater inflater) {
        this.mListView = mListView;
        this.inflater = inflater;
        this.vSearchNotice = vSearchNotice;
    }
}
