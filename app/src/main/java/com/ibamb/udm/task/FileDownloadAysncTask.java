package com.ibamb.udm.task;

import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ibamb.dnet.module.log.UdmLog;
import com.ibamb.udm.R;
import com.ibamb.udm.beans.UdmVersion;
import com.ibamb.udm.component.constants.UdmConstant;
import com.ibamb.udm.conf.DefaultConstant;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;

public class FileDownloadAysncTask extends AsyncTask<String, String, Boolean> {

    private View currentView;
    private UdmVersion udmVersion;

    public FileDownloadAysncTask(View currentView, UdmVersion udmVersion) {
        this.currentView = currentView;
        this.udmVersion = udmVersion;
    }

    private static String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DefaultConstant.BASE_DIR + "/";

    @Override
    protected Boolean doInBackground(String... strings) {

        /**
         * 如果没有设置FTP SERVER则使用默认设置
         */
        boolean isSuccess = true;
        String userName = DefaultConstant.USER_NAME;
        String password = DefaultConstant.PASSWORD;
        String apkFile = strings[0];

        FTPClient ftpClient = new FTPClient();
        OutputStream apkFileOutput = null;
        OutputStream updatePatchOutput = null;
        try {
            ftpClient.connect(UdmConstant.UDM_SERVER_DOMAINS[0], DefaultConstant.FTP_PORT);
            int reply = ftpClient.getReplyCode();
            //是否连接成功
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.connect(UdmConstant.UDM_SERVER_DOMAINS[1], DefaultConstant.FTP_PORT);
                reply = ftpClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    throw new ConnectException("The server refused to connect.");
                }
            } else if (!ftpClient.login(userName, password)) {
                throw new ConnectException("Incorrect username or password.");
            }
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setControlEncoding("UTF-8");
            if (apkFile != null && apkFile.trim().length() > 0) {
                apkFileOutput = new FileOutputStream(baseDir + apkFile);
                isSuccess = ftpClient.retrieveFile(apkFile, apkFileOutput);
            }

        } catch (Exception e) {
            e.printStackTrace();
            UdmLog.error(e);
            isSuccess = false;
        } finally {
            try {
                if (apkFileOutput != null) {
                    apkFileOutput.close();
                }
                if (updatePatchOutput != null) {
                    updatePatchOutput.close();
                }
            } catch (IOException e) {
                UdmLog.error(e);
            }
        }
        return isSuccess;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        Button button = currentView.findViewById(R.id.action_button);
        if (aBoolean) {
            button.setText("Update");
            ((TextView) currentView.findViewById(R.id.version_info)).setText("download completed.");
        } else {
            ((TextView) currentView.findViewById(R.id.version_info)).setText("download fail.");
        }
        button.setClickable(true);
        currentView.findViewById(R.id.upgrade_progress_bar).setVisibility(View.GONE);
    }
}
