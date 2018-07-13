package com.ibamb.udm.component;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AutoUpdateBroadcastReceiver extends BroadcastReceiver {

    private String apkPath;

    public AutoUpdateBroadcastReceiver(String apkPath) {
        this.apkPath = apkPath;
    }

    public AutoUpdateBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
            Toast.makeText(context, "Download Compelted", Toast.LENGTH_SHORT).show();
            boolean isInstallSuccess = InstallAPK.installApk(context,apkPath);;
            Toast.makeText(context, isInstallSuccess ? "Upgrade Successfull" : "Upgrade Fail", Toast.LENGTH_SHORT).show();
        }
    }
}
