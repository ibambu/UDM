package com.ibamb.udm.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.constants.UdmConstants;
import com.ibamb.udm.core.TryUser;
import com.ibamb.udm.security.AECryptStrategy;
import com.ibamb.udm.security.DefualtECryptValue;
import com.ibamb.udm.security.ICryptStrategy;
import com.ibamb.udm.util.TaskBarQuiet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class UserProfileActivity extends AppCompatActivity {

    private TextView back;
    private TextView save;
    private TextView title;
    private Button loginCloud;

    private View currentView;
    private File filesDir;


    @Override
    protected void onStart() {
        super.onStart();
        FileInputStream inputStream = null;
        try {
            StringBuilder strbuffer = new StringBuilder();
            inputStream = openFileInput(UdmConstants.TRY_USER_FILE);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                strbuffer.append(line);
            }
            ICryptStrategy aes = new AECryptStrategy();

            String content = strbuffer.toString();//aes.decode(strbuffer.toString(), DefualtECryptValue.KEY);
            String[] tryUsers = content.split("&");
            if(tryUsers.length>5){
                ((EditText)findViewById(R.id.try_user1)).setText(tryUsers[0]);
                ((EditText)findViewById(R.id.try_password1)).setText(tryUsers[1]);
                ((EditText)findViewById(R.id.try_user2)).setText(tryUsers[2]);
                ((EditText)findViewById(R.id.try_password2)).setText(tryUsers[3]);
                ((EditText)findViewById(R.id.try_user3)).setText(tryUsers[4]);
                ((EditText)findViewById(R.id.try_password3)).setText(tryUsers[5]);
            }
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.getMessage());
        }finally {
            if (inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        TaskBarQuiet.setStatusBarColor(this, UdmConstants.TASK_BAR_COLOR);

        currentView = getWindow().getDecorView();
        filesDir = getFilesDir();
        try {
            //回退
            back = findViewById(R.id.go_back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            //保存
            save = findViewById(R.id.do_commit);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FileOutputStream outputStream = null;
                    try {
                        String userName1 = ((EditText) currentView.findViewById(R.id.try_user1)).getText().toString();
                        String password1 = ((EditText) currentView.findViewById(R.id.try_password1)).getText().toString();
                        String userName2 = ((EditText) currentView.findViewById(R.id.try_user2)).getText().toString();
                        String password2 = ((EditText) currentView.findViewById(R.id.try_password2)).getText().toString();
                        String userName3 = ((EditText) currentView.findViewById(R.id.try_user3)).getText().toString();
                        String password3 = ((EditText) currentView.findViewById(R.id.try_password3)).getText().toString();
                        StringBuilder strBuffer = new StringBuilder();
                        strBuffer.append(userName1).append("&").append(password1)
                                .append("&").append(userName2).append("&").append(password2)
                                .append("&").append(userName3).append("&").append(password3);
                        ICryptStrategy aes = new AECryptStrategy();
                        String content = strBuffer.toString();//aes.encode(strBuffer.toString(), DefualtECryptValue.KEY);
                        TryUser.setTryUser(content.split("&"));
                        File[] files = filesDir.listFiles();
                        for(File file:files){
                            if(file.getName().equals(UdmConstants.TRY_USER_FILE)){
                                file.delete();//删除已有文件
                                break;
                            }
                        }
                        outputStream = openFileOutput(UdmConstants.TRY_USER_FILE, MODE_APPEND);
                        outputStream.write(content.getBytes());//写入新文件

                    } catch (Exception e) {
                        Log.e(this.getClass().getName(), e.getMessage());
                    } finally {
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
            //设置标题
            title = findViewById(R.id.title);
            title.setText("User Profile");

            //登录云
            loginCloud = findViewById(R.id.login_cloud_btn);
            loginCloud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
                }
            });

        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.getMessage());
        }
    }
}
