package com.ibamb.udm.task;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.ibamb.udm.net.UdmDatagramSocket;
import com.ibamb.udm.security.UserAuth;

import java.net.DatagramSocket;
import java.util.Arrays;

/**
 * Created by luotao on 18-4-18.
 */

public class UserLoginAsyncTask extends AsyncTask<String, Boolean, Boolean> {

    @Override
    protected void onProgressUpdate(Boolean... values) {
        super.onProgressUpdate(values);
        //登录失败
        if (!values[0]) {
            String loginFail = "login fail.";
        }
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String userName = strings[0];
        String password = strings[1];
        String mac = strings[2];
        boolean isSuccess = UserAuth.login(userName, password, mac);
        publishProgress(isSuccess);
        return isSuccess;
    }

    @Override
    protected void onPostExecute(Boolean isSuccess) {
        super.onPostExecute(isSuccess);
    }

    public UserLoginAsyncTask() {

    }
}
