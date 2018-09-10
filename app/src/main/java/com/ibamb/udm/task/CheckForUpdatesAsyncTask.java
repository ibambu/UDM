package com.ibamb.udm.task;

import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ibamb.dnet.module.log.UdmLog;
import com.ibamb.udm.R;
import com.ibamb.udm.activity.DeviceUpgradeActivity;
import com.ibamb.udm.component.file.FTPHelper;
import com.ibamb.udm.conf.DefaultConstant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Properties;

public class CheckForUpdatesAsyncTask extends AsyncTask<String,String,String> {

    private View currentView;
    private DeviceUpgradeActivity deviceUpgradeActivity;
    private String host;
    private int port;
    private String userName;
    private String password;
    private File localDir;

    private String localFileName;

    public CheckForUpdatesAsyncTask(DeviceUpgradeActivity deviceUpgradeActivity, String host, String port,
                                    String userName,String password,File localDir) {
        this.deviceUpgradeActivity = deviceUpgradeActivity;
        this.currentView = deviceUpgradeActivity.getWindow().getDecorView();
        this.host = host;
        this.port = Integer.parseInt(port);
        this.localDir = localDir;
        this.userName = userName;
        this.password = password;
    }

    @Override
    protected String doInBackground(String... strings) {
        String notice ="";
        try {
            /**
             * 检查版本
             */
            FTPHelper ftpHelper = new FTPHelper(host,port,userName,password);
            localFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+DefaultConstant.BASE_DIR+"/"
                    + DefaultConstant.VERSION_CHECK_FILE;
            publishProgress("connectting...");
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
            }

            publishProgress(notice);
        } catch (Exception e) {
            e.printStackTrace();
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
                String deviceUpdateVersion = properties.getProperty("device-update-version");
                String deviceUpdatePackage = properties.getProperty("device-update-package");

                deviceUpgradeActivity.udmVersion.setVersionId(versionId);
                deviceUpgradeActivity.udmVersion.setVersionName(versionName);
                deviceUpgradeActivity.udmVersion.setVersionDesc(versionDesc);
                deviceUpgradeActivity.udmVersion.setUpdatePackage(deviceUpdatePackage);
                deviceUpgradeActivity.udmVersion.setApkFile(udmApkFile);
                deviceUpgradeActivity.udmVersion.setDeviceUpdateVersion(deviceUpdateVersion);

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
            e.printStackTrace();
            UdmLog.error(e);
        } catch (Exception e) {
            e.printStackTrace();
            UdmLog.error(e);
        }
        super.onPostExecute(s);
    }

}
