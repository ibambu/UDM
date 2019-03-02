package com.ibamb.udm.task;

import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ibamb.dnet.module.log.UdmLog;
import com.ibamb.udm.R;
import com.ibamb.udm.activity.DeviceUpgradeActivity;
import com.ibamb.udm.component.constants.UdmConstant;
import com.ibamb.udm.component.file.FTPHelper;
import com.ibamb.udm.conf.DefaultConstant;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Properties;

public class CheckForUpdatesAsyncTask extends AsyncTask<String,String,String> {

    private View currentView;
    private DeviceUpgradeActivity deviceUpgradeActivity;

    private String localFileName;

    public CheckForUpdatesAsyncTask(DeviceUpgradeActivity deviceUpgradeActivity) {
        this.deviceUpgradeActivity = deviceUpgradeActivity;
        this.currentView = deviceUpgradeActivity.getWindow().getDecorView();
    }

    @Override
    protected String doInBackground(String... strings) {
        String notice ="";
        try {
            /**
             * 检查版本
             */
            publishProgress("connectting...");
            FTPHelper ftpHelper = new FTPHelper(UdmConstant.UDM_SERVER_DOMAINS[0], DefaultConstant.FTP_PORT, DefaultConstant.USER_NAME, DefaultConstant.PASSWORD);
            int retcode = ftpHelper.connect();
            if (retcode != 0) {
                FTPHelper ftpHelper1 = new FTPHelper(UdmConstant.UDM_SERVER_DOMAINS[1], DefaultConstant.FTP_PORT, DefaultConstant.USER_NAME, DefaultConstant.PASSWORD);
                retcode = ftpHelper1.connect();
                if (retcode == 0) {
                    ftpHelper = ftpHelper1;
                }
            }
            localFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+DefaultConstant.BASE_DIR+"/"
                    + DefaultConstant.VERSION_CHECK_FILE;

            int retCode = ftpHelper.download(DefaultConstant.VERSION_CHECK_FILE,localFileName);
            if(retCode == -1){
                notice = "The server refused to connect.";
            }else if(retCode==-2){
                notice = "Incorrect username or password.";
            }else if(retCode==-3){
                notice = "Cannot connect to server.";
            }else if(retCode==-4){
                notice ="Connect to server fail.";
            }else if(retCode==-5){
                notice ="Download update patch fail.";
            }else if(retCode==-6){
                notice ="Cannot connect to server.";
            }
            publishProgress(notice);
        } catch (Exception e) {
            UdmLog.error(e);
        }
        return notice;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Button actionButton = currentView.findViewById(R.id.action_button);
        ((TextView)currentView.findViewById(R.id.version_info)).setText(values[0]);
        actionButton.setClickable(false);
        currentView.findViewById(R.id.upgrade_progress_bar).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(String s) {
        Button actionButton = currentView.findViewById(R.id.action_button);
        FileInputStream inputStream  = null;
        Properties properties = new Properties();
        try {
            currentView.findViewById(R.id.upgrade_progress_bar).setVisibility(View.GONE);
            inputStream = new FileInputStream(localFileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            properties.load(bufferedReader);
            String versionCode = properties.getProperty("udm-versionId");

            if(versionCode!=null){
                int versionId = Integer.parseInt(versionCode);
                String versionName = properties.getProperty("udm-versionName");
                String versionDesc = properties.getProperty("udm-versionDesc");
                String udmApkFile = properties.getProperty("udm-apk-file");


                deviceUpgradeActivity.udmVersion.setVersionId(versionId);
                deviceUpgradeActivity.udmVersion.setVersionName(versionName);
                deviceUpgradeActivity.udmVersion.setVersionDesc(versionDesc);
                deviceUpgradeActivity.udmVersion.setApkFile(udmApkFile);

                if(deviceUpgradeActivity.udmVersion.getCurrentVersionId() < versionId){
                    ((TextView)currentView.findViewById(R.id.version_info)).setText("Discover new version:"+ versionName);
                    actionButton.setText("Download update patch");
                }else if(deviceUpgradeActivity.udmVersion.getDeviceUpdateVersion().toString()!= null){
                    ((TextView)currentView.findViewById(R.id.version_info)).setText("Found new update patch.");
                    actionButton.setText("Download update patch");
                }else{
                    ((TextView)currentView.findViewById(R.id.version_info)).setText("It's the latest version.");
                    actionButton.setText("Check for updates");
                }
            }else{
                ((TextView)currentView.findViewById(R.id.version_info)).setText("It's the latest version.");
            }
            actionButton.setClickable(true);
        } catch (FileNotFoundException e) {
            actionButton.setClickable(true);
            UdmLog.error(e);
        } catch (Exception e) {
            UdmLog.error(e);
        }
        super.onPostExecute(s);
    }

}
