package com.ibamb.udm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.beans.FileInfo;

import java.util.List;

public class FileListAdapter extends BaseAdapter implements ListAdapter {

    private List<FileInfo> dataList;
    private LayoutInflater inflater;

    public FileListAdapter(List<FileInfo> dataList, LayoutInflater inflater) {
        this.dataList = dataList;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView fileName = null;
        TextView createTime = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_file_layout, null);
            fileName = (TextView) convertView.findViewById(R.id.file_name);
            createTime = (TextView) convertView.findViewById(R.id.create_time);
            convertView.setTag(new FileListAdapter.ListViewColumns(fileName, createTime));
        } else {
            //得到保存的对象
            FileListAdapter.ListViewColumns columns = (FileListAdapter.ListViewColumns) convertView.getTag();
            fileName = columns.fileName;
            createTime = columns.createTime;
        }

        FileInfo fileInfo = (FileInfo) dataList.get(position);
        //数据绑定到控件上
        fileName.setText(fileInfo.getFileName());//三位数字
        createTime.setText(fileInfo.getCreateTime());
        return convertView;
    }

    private final class ListViewColumns {
        TextView fileName = null;
        TextView createTime = null;

        public ListViewColumns(TextView fileName, TextView createTime) {
            this.fileName = fileName;
            this.createTime = createTime;
        }
    }
}
