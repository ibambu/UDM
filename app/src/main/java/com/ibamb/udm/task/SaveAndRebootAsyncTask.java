package com.ibamb.udm.task;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.ibamb.dnet.module.api.UdmClient;

public class SaveAndRebootAsyncTask extends AsyncTask<String, Boolean, Boolean> {

    private View view;

    public SaveAndRebootAsyncTask(View view) {
        this.view = view;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String mac = strings[0];
        String actionType = strings[1];
        boolean isSuccess = false;
        if(actionType.equals("1")){
            //保存并重启
            isSuccess = UdmClient.getInstance().saveAndReboot(mac);
        }else if(actionType.equals("0")){
            //只重启，设置将恢复到修改前状态
            isSuccess = UdmClient.getInstance().reboot(mac);
        }
        return isSuccess;
    }

    @Override
    protected void onPostExecute(Boolean isSuccess) {
        super.onPostExecute(isSuccess);
        String notice = isSuccess ? "successful":"fail";
        Toast.makeText(view.getContext(), notice, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onProgressUpdate(Boolean... values) {
        super.onProgressUpdate(values);
    }
}
