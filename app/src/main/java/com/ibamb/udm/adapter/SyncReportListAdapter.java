package com.ibamb.udm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.dnet.module.beans.DeviceSyncMessage;

import java.util.ArrayList;

public class SyncReportListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<DeviceSyncMessage> data;
    private int id;
    private LayoutInflater inflater;

    public SyncReportListAdapter(int item, LayoutInflater inflater, ArrayList<DeviceSyncMessage> data) {
        this.data = data;
        this.inflater = inflater;
        this.id = item;
    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View view, ViewGroup arg2) {
        TextView deviceIndex = null;
        TextView deviceIP = null;
        TextView deviceMac = null;
        TextView deviceInfoStr = null;
        TextView result = null;
        if (view == null) {
            view = inflater.inflate(id, null);
            deviceIndex = view.findViewById(R.id.device_index);
            deviceIP = view.findViewById(R.id.device_ip);
            deviceMac = view.findViewById(R.id.device_mac);
            deviceInfoStr = view.findViewById(R.id.device_info);
            result = view.findViewById(R.id.result_info);
            view.setTag(new SyncReportListAdapter.ListViewColumns(deviceIndex,deviceIP,deviceMac,deviceInfoStr,result));
        } else {
            //得到保存的对象
            SyncReportListAdapter.ListViewColumns columns = (SyncReportListAdapter.ListViewColumns) view.getTag();
            deviceIndex = columns.index;
            deviceIP = columns.ip;
            deviceMac = columns.mac;
            deviceInfoStr= columns.deviceInfo;
            result = columns.result;
        }
        DeviceSyncMessage deviceInfo = (DeviceSyncMessage) data.get(position);
        //数据绑定到控件上
        deviceIndex.setText(String.format("%03d",deviceInfo.getIndex()));//三位数字
        deviceIP.setText(deviceInfo.getIp());
        deviceMac.setText(deviceInfo.getMac());
        deviceInfoStr.setText(deviceInfo.getIp()+" | "+deviceInfo.getMac().toUpperCase());
        result.setText(deviceInfo.getResultInfo());

        return view;

    }

//    private void LoadImage(ImageView img, String path)
//    {
//        //异步加载图片资源
//        AsyncTaskImageLoad async=new AsyncTaskImageLoad(img);
//        //执行异步加载，并把图片的路径传送过去
//        async.execute(path);
//
//    }

    private final class ListViewColumns {
        TextView index = null;
        TextView ip = null;
        TextView mac = null;
        TextView deviceInfo = null;
        TextView result = null;

        public ListViewColumns(TextView index, TextView ip, TextView mac,TextView deviceInfo,TextView result) {
            this.index = index;
            this.ip = ip;
            this.mac = mac;
            this.deviceInfo = deviceInfo;
            this.result = result;
        }
    }
}
