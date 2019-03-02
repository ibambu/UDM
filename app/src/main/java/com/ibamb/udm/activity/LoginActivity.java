package com.ibamb.udm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibamb.dnet.module.core.TryUser;
import com.ibamb.dnet.module.log.UdmLog;
import com.ibamb.udm.R;
import com.ibamb.udm.component.constants.UdmConstant;
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
        TaskBarQuiet.setStatusBarColor(this, UdmConstant.TASK_BAR_COLOR);//修改任务栏背景颜色
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
        final TextView noticeView = findViewById(R.id.notice_info);//显示登录结果
        final AutoCompleteTextView userNameView = findViewById(R.id.dev_user_name);
        userNameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                noticeView.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final EditText passwordView = findViewById(R.id.dev_password);
        passwordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                noticeView.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
                        noticeView.setText(UdmConstant.INFO_LOGIN_FAIL);
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
        this.setResult(UdmConstant.ACTIVITY_RESULT_FOR_LOGIN, intent);
        super.finish();
    }
}
