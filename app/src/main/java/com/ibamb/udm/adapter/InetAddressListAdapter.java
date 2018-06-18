package com.ibamb.udm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;


import com.ibamb.udm.R;
import com.ibamb.udm.module.beans.DeviceInfo;

import java.util.ArrayList;


public class InetAddressListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<DeviceInfo> data;
    private int id;
    private LayoutInflater inflater;

    public InetAddressListAdapter(int item, LayoutInflater inflater, ArrayList<DeviceInfo> data) {
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
        if (view == null) {
            view = inflater.inflate(id, null);
            deviceIndex = (TextView) view.findViewById(R.id.device_index);
            deviceIP = (TextView)view.findViewById(R.id.device_ip);
            deviceMac = (TextView)view.findViewById(R.id.device_mac);
            view.setTag(new ListViewColumns(deviceIndex,deviceIP,deviceMac));
        } else {
            //得到保存的对象
            ListViewColumns columns = (ListViewColumns) view.getTag();
            deviceIndex = columns.index;
            deviceIP = columns.ip;
            deviceMac = columns.mac;
        }
        System.out.println("ddddddddd==="+data.size()+"  "+position);
        DeviceInfo deviceInfo = (DeviceInfo) data.get(position);
        //数据绑定到控件上
        deviceIndex.setText(String.format("%03d",deviceInfo.getIndex()));//三位数字
        deviceIP.setText(deviceInfo.getIp());
        deviceMac.setText(deviceInfo.getMac());

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

        public ListViewColumns(TextView index, TextView ip, TextView mac) {
            this.index = index;
            this.ip = ip;
            this.mac = mac;
        }
    }
}
