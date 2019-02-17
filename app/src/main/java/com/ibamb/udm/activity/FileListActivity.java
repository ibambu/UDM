package com.ibamb.udm.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.ibamb.udm.R;
import com.ibamb.udm.adapter.FileListAdapter;
import com.ibamb.udm.beans.FileInfo;
import com.ibamb.udm.conf.DefaultConstant;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileListActivity extends AppCompatActivity {

    private ListView vFileListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);

        vFileListView = findViewById(R.id.file_list_view);

        String udmDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +DefaultConstant.BASE_DIR + "/";
        File baseDir = new File(udmDir);
        List<FileInfo> fileInfoList = new ArrayList<>();
        if (baseDir.exists()) {
            File[] files = baseDir.listFiles();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (File file : files) {
                String createTimeStr = "";
                try {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        BasicFileAttributes basicFileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                        long createTime = basicFileAttributes.creationTime().toMillis();
                        Date dCreateTime = new Date(createTime);
                        createTimeStr = sdf.format(dCreateTime);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!file.isDirectory() && file.getName().endsWith(".dfg")) {
                    fileInfoList.add(new FileInfo(file.getName(), createTimeStr));
                }
            }
        }
        FileListAdapter adapter = new FileListAdapter(fileInfoList, getLayoutInflater());
        vFileListView.setAdapter(adapter);
    }
}
