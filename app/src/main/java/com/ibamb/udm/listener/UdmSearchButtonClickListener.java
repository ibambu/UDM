package com.ibamb.udm.listener;

import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.ibamb.udm.net.UdmDatagramSocket;
import com.ibamb.udm.task.DeviceSearchAsyncTask;

/**
 * Created by luotao on 18-4-18.
 */

public class UdmSearchButtonClickListener implements View.OnClickListener{
    private ListView mListView;
    private LayoutInflater inflater;
    private FloatingActionButton searchButton;
    @Override
    public void onClick(View v) {
        //通过异步任务开启工作线程在后台搜索设备。

        DeviceSearchAsyncTask task = new DeviceSearchAsyncTask(searchButton,mListView,inflater);
        task.execute();
    }

    /**
     * 通过构造函数将所需要的参数从主线程传入。
     * @param searchButton
     * @param mListView
     * @param inflater
     */
    public UdmSearchButtonClickListener(FloatingActionButton searchButton,ListView mListView, LayoutInflater inflater) {
        this.mListView = mListView;
        this.inflater = inflater;
        this.searchButton = searchButton;
    }
}
