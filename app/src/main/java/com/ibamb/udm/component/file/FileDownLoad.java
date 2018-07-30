package com.ibamb.udm.component.file;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.ibamb.udm.module.log.UdmLog;

public class FileDownLoad {
    private static final String DIR = "download";
    private static final String APK = "udm.apk";
    public static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DIR + "/" + APK;

    public static long downLoadFile(String fileUrl,Context context) {
        long requestId = -1;
        try{
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE);
            // 在Notification显示下载进度
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            // 设置Title
            request.setTitle("udm update");
            // 设置描述
            request.setDescription("download upgrade pack");
            request.setDestinationInExternalPublicDir(DIR, APK);
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            requestId = downloadManager.enqueue(request);
                      //downloadManager.remove(id);取消下载
        }catch (Exception e){
            UdmLog.error(e);
        }
        return requestId;
    }


}
