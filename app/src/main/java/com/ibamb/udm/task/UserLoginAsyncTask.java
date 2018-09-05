package com.ibamb.udm.task;

import android.os.AsyncTask;

import com.ibamb.udm.component.login.Login;
import com.ibamb.dnet.module.log.UdmLog;
import com.ibamb.dnet.module.security.UserAuth;


public class UserLoginAsyncTask extends AsyncTask<String, Boolean, Boolean> {

    @Override
    protected void onProgressUpdate(Boolean... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        boolean isSuccess = false;
        try{
            String userName = strings[0];
            String password = strings[1];
            String mac = strings[2];
            String ip = strings[3];
            isSuccess = Login.login(userName, password, mac,ip);
            publishProgress(isSuccess);
        }catch (Exception e){
            UdmLog.error(e);
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
