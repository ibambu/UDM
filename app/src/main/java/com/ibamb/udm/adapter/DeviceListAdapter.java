package com.ibamb.udm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.dnet.module.beans.DeviceModel;

import java.util.ArrayList;

public class DeviceListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<DeviceModel> data;
    private int id;
    private LayoutInflater inflater;
    private TextView checkAll;
    private TextView title;


    public DeviceListAdapter(int res, LayoutInflater inflater, ArrayList<DeviceModel> data, TextView title, TextView checkAll) {
        this.data = data;
        this.inflater = inflater;
        this.id = res;
        this.checkAll = checkAll;
        this.title = title;

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


    public int getCheckedCount(){
        int count =0;
        for(DeviceModel deviceInfo:data){
            if(deviceInfo.isChecked()){
                count ++;
            }
        }
        return count;
    }

    @Override
    public View getView(final int position, View view, ViewGroup arg2) {
        TextView deviceIndex = null;
        TextView deviceIP = null;
        TextView deviceMac = null;
        CheckBox checkBox = null;
        if (view == null) {
            view = inflater.inflate(id, null);
            deviceIndex = (TextView) view.findViewById(R.id.device_index);
            deviceIP = (TextView) view.findViewById(R.id.device_ip);
            deviceMac = (TextView) view.findViewById(R.id.device_mac);
            checkBox = (CheckBox) view.findViewById(R.id.common_check_box);
            view.setTag(new DeviceListAdapter.ListViewColumns(deviceIndex, deviceIP, deviceMac, checkBox));
        } else {
            //得到保存的对象
            DeviceListAdapter.ListViewColumns columns = (DeviceListAdapter.ListViewColumns) view.getTag();
            deviceIndex = columns.index;
            deviceIP = columns.ip;
            deviceMac = columns.mac;
            checkBox = columns.checkBox;
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox1 = (CheckBox) v;
                DeviceModel deviceInfo = (DeviceModel) getItem(position);
                if (checkBox1.isChecked()) {
                    deviceInfo.setChecked(true);
                } else {
                    deviceInfo.setChecked(false);
                }
                int checkedItem = getCheckedCount();
                title.setText("Checked Items:"+checkedItem);
                if(checkedItem < data.size()){
                    checkAll.setText("Check All");
                }else{
                    checkAll.setText("Cancel All");
                }
            }
        });

        DeviceModel deviceInfo = (DeviceModel) data.get(position);
        //数据绑定到控件上
        deviceIndex.setText(String.format("%03d", deviceInfo.getIndex()));//三位数字
        deviceIP.setText(deviceInfo.getIp());
        deviceMac.setText(deviceInfo.getMac());
        if (deviceInfo.isChecked()) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
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
        CheckBox checkBox = null;

        public ListViewColumns(TextView index, TextView ip, TextView mac, CheckBox checkBox) {
            this.index = index;
            this.ip = ip;
            this.mac = mac;
            this.checkBox = checkBox;
        }
    }
}
