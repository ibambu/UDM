package com.ibamb.udm.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibamb.dnet.module.constants.Constants;
import com.ibamb.dnet.module.log.UdmLog;
import com.ibamb.udm.R;
import com.ibamb.udm.component.constants.UdmConstant;
import com.ibamb.udm.util.TaskBarQuiet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class UpdateSettingActivity extends AppCompatActivity {
    private ImageView commit;
    private ImageView back;
    private TextView title;

    private File filesDir;

    @Override
    protected void onStart() {
        super.onStart();
        FileInputStream inputStream  = null;
        try {
            inputStream = openFileInput("update-setting.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            StringBuilder strBuffer = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                strBuffer.append(line);
            }
            String[] content = strBuffer.toString().split("#");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>."+strBuffer.toString());
            if(content.length>3){
                EditText host = findViewById(R.id.ftp_server);
                EditText port = findViewById(R.id.ftp_port);
                EditText user = findViewById(R.id.ftp_user);
                EditText password = findViewById(R.id.ftp_password);
                host.setText(content[0]);
                port.setText(content[1]);
                user.setText(content[2]);
                password.setText(content[3]);
            }
        } catch (FileNotFoundException e) {
            UdmLog.error(e);
        } catch (IOException e) {
            UdmLog.error(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_setting);

        //修改任务栏背景颜色
        TaskBarQuiet.setStatusBarColor(this, UdmConstant.TASK_BAR_COLOR);

        //获取路径
        filesDir = getFilesDir();

        commit = findViewById(R.id.do_commit);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isOK=true;
                EditText host = findViewById(R.id.ftp_server);
                EditText port = findViewById(R.id.ftp_port);
                EditText user = findViewById(R.id.ftp_user);
                EditText password = findViewById(R.id.ftp_password);

                if(host.getText().toString().trim().length()==0){
                    Snackbar.make(host,  "Host can't be empty", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
                    isOK=false;
                }

                if(isOK && port.getText().toString().trim().length()==0){
                    Snackbar.make(port,  "Port can't be empty", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    isOK = false;
                }
                if(isOK){
                    String content = host.getText().toString()+"#"+port.getText().toString()+"#"+user.getText().toString()+"#"+password.getText().toString();
                    File[] files = filesDir.listFiles();
                    for(File file:files){
                        if(file.getName().equals("update-setting.txt")){
                            file.delete();//删除已有文件
                            break;
                        }
                    }
                    FileOutputStream outputStream = null;
                    try{
                        outputStream  = openFileOutput("update-setting.txt", MODE_PRIVATE);
                        outputStream.write(content.getBytes());//写入新文件
                        Snackbar.make(port,  "successfull", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }catch (Exception e){
                        UdmLog.error(e);
                    }finally {
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                            } catch (IOException e) {
                                UdmLog.error(e);
                            }
                        }
                    }
                }
            }
        });

        back = findViewById(R.id.go_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title = findViewById(R.id.title);
        title.setText("Update Setting");
    }


}
