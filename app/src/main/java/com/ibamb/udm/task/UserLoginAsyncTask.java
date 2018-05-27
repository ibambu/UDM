package com.ibamb.udm.task;

import android.os.AsyncTask;
import android.util.Log;

import com.ibamb.udm.module.security.UserAuth;

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
        boolean isSuccess = false;
        try{
            String userName = strings[0];
            String password = strings[1];
            String mac = strings[2];
            String ip = strings[3];
            isSuccess = UserAuth.login(userName, password, mac,ip);
            publishProgress(isSuccess);
        }catch (Exception e){
            Log.e(this.getClass().getName(),e.getMessage());
        }
        return isSuccess;
    }

    @Override
    protected void onPostExecute(Boolean isSuccess) {
        super.onPostExecute(isSuccess);
    }

    public UserLoginAsyncTask() {

    }
}
