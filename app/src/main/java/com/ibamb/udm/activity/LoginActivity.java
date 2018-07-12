package com.ibamb.udm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibamb.udm.R;
import com.ibamb.udm.module.log.UdmLog;
import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.module.core.TryUser;
import com.ibamb.udm.task.UserLoginAsyncTask;
import com.ibamb.udm.util.TaskBarQuiet;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    private String mac;
    private String ip;
    private boolean isLoginSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TaskBarQuiet.setStatusBarColor(this, Constants.TASK_BAR_COLOR);//修改任务栏背景颜色
        ((TextView)findViewById(R.id.title)).setText("Login");
        findViewById(R.id.do_commit).setVisibility(View.GONE);

        ImageView goback = findViewById(R.id.go_back);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            mac = intent.getStringExtra("HOST_MAC");
            ip = intent.getStringExtra("HOST_ADDRESS");
        }
        final AutoCompleteTextView userNameView = findViewById(R.id.dev_user_name);
        final EditText passwordView = findViewById(R.id.dev_password);
        final TextView noticeView = findViewById(R.id.notice_info);//显示登录结果
        Button sigInInButton = findViewById(R.id.alter_sign_in_button);
        sigInInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String userName = userNameView.getText().toString();
                    String password = passwordView.getText().toString();

                    UserLoginAsyncTask loginAsyncTask = new UserLoginAsyncTask();
                    String[] loginInfo = {userName, password, mac, ip};
                    String[] tryUser = {userName, password};
                    isLoginSuccess = loginAsyncTask.execute(loginInfo).get();
                    if (isLoginSuccess) {
                        TryUser.setTryUser(tryUser);
                        finish();
                    } else {
                        loginAsyncTask.cancel(true);
                        noticeView.setVisibility(View.VISIBLE);
                        noticeView.setText(Constants.INFO_LOGIN_FAIL);
                    }
                } catch (InterruptedException e) {
                    UdmLog.error(e);
                } catch (ExecutionException e) {
                    UdmLog.error(e);
                }
            }
        });
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        Bundle params = new Bundle();
        params.putString("HOST_ADDRESS", ip);
        params.putString("HOST_MAC", mac);
        params.putBoolean("IS_LOGIN_SUCCESS", isLoginSuccess);
        intent.putExtras(params);
        this.setResult(Constants.ACTIVITY_RESULT_FOR_LOGIN, intent);
        super.finish();
    }
}
