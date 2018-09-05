package com.ibamb.udm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.component.file.FileDirManager;
import com.ibamb.dnet.module.constants.Constants;
import com.ibamb.udm.task.ImportTypeFileAsyncTask;
import com.ibamb.udm.util.TaskBarQuiet;

import java.io.File;


public class ImportTypeDefFileActivity extends AppCompatActivity {

    private View currentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_type_def_file);
        TaskBarQuiet.setStatusBarColor(this, Constants.TASK_BAR_COLOR);

        currentView = getWindow().getDecorView();
        Intent intent = getIntent();
        final String file = intent.getStringExtra("TYPE_DEF_FILE");
        File typeFile = new File(file);
        TextView textView = findViewById(R.id.file_path);
        textView.setText("Import File:"+typeFile.getName());

        Button doImportButton = findViewById(R.id.do_import);
        doImportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressBar bar = currentView.findViewById(R.id.import_progress_bar);
                ImportTypeFileAsyncTask task = new ImportTypeFileAsyncTask(ImportTypeDefFileActivity.this,currentView);
                task.execute(file);
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
                File mappingFile = fileDirManager.getFileByName(Constants.FILE_PARAM_MAPPING);
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

}
