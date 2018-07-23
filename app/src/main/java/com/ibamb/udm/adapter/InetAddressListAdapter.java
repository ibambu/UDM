package com.ibamb.udm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.module.beans.Device;

import java.util.List;


public class InetAddressListAdapter extends BaseAdapter implements ListAdapter {
    private List<Device> data;
    private int id;
    private LayoutInflater inflater;

    public InetAddressListAdapter(int item, LayoutInflater inflater, List<Device> data) {
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
        TextView deviceName = null;
        TextView deviceProfile = null;
        if (view == null) {
            view = inflater.inflate(id, null);
            deviceIndex = view.findViewById(R.id.device_index);
            deviceIP = view.findViewById(R.id.device_ip);
            deviceMac = view.findViewById(R.id.device_mac);
            deviceName = view.findViewById(R.id.device_name);
            deviceProfile = view.findViewById(R.id.device_profile);
            view.setTag(new ListViewColumns(deviceIndex,deviceIP,deviceMac,deviceName,deviceProfile));
        } else {
            //得到保存的对象
            ListViewColumns columns = (ListViewColumns) view.getTag();
            deviceIndex = columns.index;
            deviceIP = columns.ip;
            deviceMac = columns.mac;
            deviceName = columns.deviceName;
            deviceProfile = columns.deviceProfile;

        }
        Device deviceInfo = (Device) data.get(position);
        //数据绑定到控件上
        deviceIndex.setText(String.format("%03d",deviceInfo.getIndex()));//三位数字
        deviceIP.setText(deviceInfo.getIp());
        deviceMac.setText(deviceInfo.getMac());
        deviceName.setText(deviceInfo.getDeviceName());
        deviceProfile.setText(deviceInfo.getIp()+" | "+deviceInfo.getMac().toUpperCase()+" | V1.2.9.R3");

        return view;

    }


    private final class ListViewColumns {
        TextView index = null;
        TextView ip = null;
        TextView mac = null;
        TextView deviceName = null;
        TextView deviceProfile = null;

        public ListViewColumns(TextView index, TextView ip, TextView mac,
                               TextView deviceName,TextView deviceProfile) {
            this.index = index;
            this.ip = ip;
            this.mac = mac;
            this.deviceName = deviceName;
            this.deviceProfile = deviceProfile;
        }
    }
}
