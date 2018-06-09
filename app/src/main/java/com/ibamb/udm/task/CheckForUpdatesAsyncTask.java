package com.ibamb.udm.task;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ibamb.udm.R;

public class CheckForUpdatesAsyncTask extends AsyncTask<String,String,String> {

    private View currentView;
    @Override
    protected String doInBackground(String... strings) {
        try {
            /**
             * 检查版本
             */
            /**
             * 如果有新版本，则下载文件升级包。
             */

            publishProgress("1.0");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "1.0";
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Button actionButton = currentView.findViewById(R.id.action_button);
        actionButton.setText("checking...");

    }

    @Override
    protected void onPostExecute(String s) {
        Button actionButton = currentView.findViewById(R.id.action_button);
        if(s!=null){
            ((TextView)currentView.findViewById(R.id.version_info)).setText("New Version :"+ s);
            actionButton.setText("Upgrade All");
        }else{
            actionButton.setText("Check for updates");
        }
        super.onPostExecute(s);
    }

    public CheckForUpdatesAsyncTask(View currentView) {
        this.currentView = currentView;
    }
}
