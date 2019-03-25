package com.ibamb.udm.activity;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.adapter.FileBrowseListAdapter;
import com.ibamb.udm.beans.FileInfo;
import com.ibamb.udm.conf.DefaultConstant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileBrowseActivity extends AppCompatActivity {

    private ImageView goback;
    private ImageView docommit;
    private TextView pathInfo;
    private ListView mFileListView;//文件列表
    private List<FileInfo> fileInfoList = new ArrayList<>();
    private File baseDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DefaultConstant.BASE_DIR + "/");
    private FileBrowseListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browse);

        mFileListView = findViewById(R.id.file_browse_list);
        pathInfo = findViewById(R.id.path_desc);
        goback = findViewById(R.id.go_back);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTemp = new Intent();
                intentTemp.putExtra("filepath", "");
                setResult(991, intentTemp);
                finish();
            }
        });

        docommit = findViewById(R.id.do_commit);
        docommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTemp = new Intent();
                intentTemp.putExtra("filepath", baseDir.getAbsolutePath());
                setResult(991, intentTemp);
                finish();
            }
        });

        ((TextView)findViewById(R.id.title)).setText("File browser");

        if (baseDir.exists()) {
            fileInfoList = getFileList(baseDir);
            adapter = new FileBrowseListAdapter(this, fileInfoList, getLayoutInflater());
            /**
             * 设置点击事件，切换目录。
             */
            mFileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FileInfo fileInfo = fileInfoList.get(position);
                    if (fileInfo.isLable()) {
                        fileInfoList = getFileList(baseDir.getParentFile());
                        adapter.notifyDataSetChanged();
                    } else if (fileInfo.isDirectory()) {
                        fileInfoList = getFileList(fileInfo.getFile());
                        adapter.notifyDataSetChanged();
                    }
                }
            });
            mFileListView.setAdapter(adapter);
        }
    }

    @Override
    protected void onDestroy() {
        Intent intentTemp = new Intent();
        intentTemp.putExtra("filepath", baseDir.getAbsolutePath());
        setResult(1, intentTemp);
        super.onDestroy();
    }

    private List<FileInfo> getFileList(File file) {
        baseDir = file;
        pathInfo.setText(file.getAbsolutePath());
        File[] files = file.listFiles();
        FileInfo fileLable = new FileInfo("..", null);
        fileLable.setLable(true);
        fileLable.setDirectory(false);
        fileInfoList.clear();
        fileInfoList.add(fileLable);
        for (File f : files) {
            FileInfo afile = new FileInfo(f.getName(), null);
            afile.setDirectory(f.isDirectory());
            afile.setFile(f);
            fileInfoList.add(afile);
        }
        return fileInfoList;
    }


}
