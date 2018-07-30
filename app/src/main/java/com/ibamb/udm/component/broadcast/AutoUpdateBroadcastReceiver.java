package com.ibamb.udm.component.broadcast;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.ibamb.udm.component.file.FileDownLoad;

public class AutoUpdateBroadcastReceiver extends BroadcastReceiver {



    public AutoUpdateBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
            Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show();
            InstallAPK.installApk(context, FileDownLoad.PATH);
           // Toast.makeText(context, isInstallSuccess ? "Upgrade Successfull" : "Upgrade Fail", Toast.LENGTH_SHORT).show();
        }
    }
}
