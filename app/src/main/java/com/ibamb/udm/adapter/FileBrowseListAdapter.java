package com.ibamb.udm.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.beans.FileInfo;

import java.io.File;
import java.util.List;

public class FileBrowseListAdapter extends BaseAdapter implements ListAdapter {
    private List<FileInfo> dataList;
    private LayoutInflater inflater;
    private Context context;

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public FileBrowseListAdapter(Context context, List<FileInfo> dataList, LayoutInflater inflater) {
        this.dataList = dataList;
        this.inflater = inflater;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView fileName = null;
        TextView createTime = null;
        ImageView fileIcon = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_file_layout, null);
            fileName = (TextView) convertView.findViewById(R.id.file_name);
            createTime = (TextView) convertView.findViewById(R.id.create_time);
            fileIcon = convertView.findViewById(R.id.left_icon);
            convertView.setTag(new FileBrowseListAdapter.ListViewColumns(fileName, createTime, fileIcon));
        } else {
            //得到保存的对象
            FileBrowseListAdapter.ListViewColumns columns = (FileBrowseListAdapter.ListViewColumns) convertView.getTag();
            fileName = columns.fileName;
            createTime = columns.createTime;
            fileIcon = columns.fileIcon;
        }

        FileInfo fileInfo = (FileInfo) dataList.get(position);
        //数据绑定到控件上
        fileName.setText(fileInfo.getFileName());//三位数字
        if (fileInfo.isLable()) {
            fileIcon.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_action_goleft));
        }else if (!fileInfo.isDirectory()) {
            Drawable img = context.getResources().getDrawable(R.mipmap.ic_action_copy);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
//            fileName.setCompoundDrawables(img, null, null, null); //设置左图标
            fileIcon.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_action_copy));
        } else if (fileInfo.isDirectory()) {
            Drawable img = context.getResources().getDrawable(R.mipmap.ic_action_folder_closed);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
//            fileName.setCompoundDrawables(img, null, null, null); //设置左图标
            fileIcon.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_action_folder_closed));
        }
//        createTime.setText(fileInfo.getCreateTime());
        return convertView;
    }

    private final class ListViewColumns {
        TextView fileName = null;
        TextView createTime = null;
        ImageView fileIcon = null;

        public ListViewColumns(TextView fileName, TextView createTime, ImageView fileIcon) {
            this.fileName = fileName;
            this.createTime = createTime;
            this.fileIcon = fileIcon;
        }
    }
}
