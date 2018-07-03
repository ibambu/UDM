package com.ibamb.udm.activity;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ibamb.udm.R;
import com.ibamb.udm.component.CallbackBundle;
import com.ibamb.udm.component.OpenFileDialog;
import com.ibamb.udm.component.PermissionUtils;
import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.util.TaskBarQuiet;

import java.util.HashMap;
import java.util.Map;

public class FilePickerActivity extends AppCompatActivity {
    static private int openfileDialogId = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_picker);
        TaskBarQuiet.setStatusBarColor(this, Constants.TASK_BAR_COLOR);
        // 设置单击按钮时打开文件对话框
        findViewById(R.id.file_picker_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showDialog(openfileDialogId);
            }
        });


    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id==openfileDialogId){
            Map<String, Integer> images = new HashMap<String, Integer>();
            // 下面几句设置各文件类型的图标， 需要你先把图标添加到资源文件夹
            images.put(OpenFileDialog.sRoot, R.mipmap.ic_action_folder_open);	// 根目录图标
            images.put(OpenFileDialog.sParent, R.mipmap.ic_action_goleft);	//返回上一层的图标
            images.put(OpenFileDialog.sFolder, R.mipmap.ic_action_folder_closed);	//文件夹图标
            images.put("wav", R.mipmap.ic_action_record);	//wav文件图标
            images.put(OpenFileDialog.sEmpty, R.mipmap.ic_action_folder_open);
            Dialog dialog = OpenFileDialog.createDialog(id, this, "打开文件", new CallbackBundle() {
                        @Override
                        public void callback(Bundle bundle) {
                            String filepath = bundle.getString("path");
                            setTitle(filepath); // 把文件路径显示在标题上
                        }
                    },
                    ".wav;",
                    images);
            return dialog;
        }
        return null;
    }

}
