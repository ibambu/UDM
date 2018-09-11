package com.ibamb.udm.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.component.constants.UdmConstant;
import com.ibamb.udm.component.file.FileDirManager;
import com.ibamb.udm.component.file.FilePathParser;
import com.ibamb.udm.task.ImportTypeFileAsyncTask;
import com.ibamb.udm.util.TaskBarQuiet;

import java.io.File;


public class ImportTypeDefFileActivity extends AppCompatActivity {

    private View currentView;
    private String selectFilePath;
    private Button doImportButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_type_def_file);
        TaskBarQuiet.setStatusBarColor(this, UdmConstant.TASK_BAR_COLOR);

        currentView = getWindow().getDecorView();

        doImportButton = findViewById(R.id.do_import);
        doImportButton.setText("Select File");
        doImportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(doImportButton.getText().toString().equalsIgnoreCase("Select File")){
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");//设置类型.
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent, 1);
                }else if(doImportButton.getText().toString().equalsIgnoreCase("Import")){
                    ProgressBar bar = currentView.findViewById(R.id.import_progress_bar);
                    ImportTypeFileAsyncTask task = new ImportTypeFileAsyncTask(ImportTypeDefFileActivity.this,currentView);
                    task.execute(selectFilePath);
                }
            }
        });

        Button doResetButton = findViewById(R.id.do_reset);
        doResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 只要删除data/data目录的文件即可，因为该目录文件不存在时会读取资源目录下的默认文件。
                 */
                FileDirManager fileDirManager = new FileDirManager(ImportTypeDefFileActivity.this);
                File mappingFile = fileDirManager.getFileByName(UdmConstant.FILE_PARAM_MAPPING);
                if(mappingFile!=null){
                    mappingFile.delete();
                    ((TextView)findViewById(R.id.import_progress_notice)).setText("Reset Completed.");
                }
            }
        });

        TextView title = findViewById(R.id.title);
        title.setText("Import Type File");

        findViewById(R.id.do_commit).setVisibility(View.GONE);

        ImageView goback = findViewById(R.id.go_back);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            FilePathParser filePathParser = new FilePathParser();
            String path = "";
            if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                path = uri.getPath();
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                path = filePathParser.getPath(this, uri);
            } else {//4.4以下下系统调用方法
                path = filePathParser.getRealPathFromURI(uri, getContentResolver());
            }
            if (path != null && path.trim().length() > 0) {
                TextView textView = findViewById(R.id.file_path);
                File f = new File(path);
                textView.setText("Import File:"+f.getName());
                selectFilePath = path;
                doImportButton.setText("Import");
            }
        }
    }
}
