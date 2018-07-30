package com.ibamb.udm.task;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ibamb.udm.R;

import java.io.File;


public class AppUpdateAsyncTask extends AsyncTask<String, Integer, Long> {
    private static final String DIR = "download";
    private static final String APK = "udm.apk";
    public static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DIR + "/" + APK;
    private Activity activity;


    public AppUpdateAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Long doInBackground(String... strings) {
        String url = strings[0];

        File apkfile = new File(PATH);
        if(apkfile.exists()){
            apkfile.delete();
        }
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE);
        // 在Notification显示下载进度

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        // 设置Title
        request.setTitle("udm update");
        // 设置描述
        request.setDescription("download udm pack");
        request.setDestinationInExternalPublicDir(DIR, APK);
        DownloadManager downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);

        long requestId = downloadManager.enqueue(request);

        //查询下载信息
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(requestId);

        boolean isDoing = true;

        while (isDoing) {
            Cursor cursor = downloadManager.query(query);

            if (cursor != null && cursor.moveToFirst()) {
                int bytesDownloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                //总需下载的字节数
                int bytesTotal = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                publishProgress(bytesTotal,bytesDownloaded);

                int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                switch (status) {
                    //如果下载状态为成功
                    case DownloadManager.STATUS_SUCCESSFUL:
                    case DownloadManager.STATUS_FAILED:
                    case DownloadManager.STATUS_PAUSED:
                        isDoing = false;
                        break;
                }
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return requestId;
    }


    @Override
    protected void onProgressUpdate(Integer... values) {

        int total = values[0];
        int downloaded = values[1];
        View view = activity.getWindow().getDecorView();
        ProgressBar progressBar = view.findViewById(R.id.app_update_progressr);
        progressBar.setMax(total);
        progressBar.setProgress(downloaded);
        progressBar.setVisibility(View.VISIBLE);

        float percent = (float) downloaded/total *100;
        TextView textView = view.findViewById(R.id.download_info);
        if(total==downloaded){
            textView.setText("Download completed.");
        }else {
            textView.setText("Downloading..."+(int)percent+"%");
        }
        textView.setVisibility(View.VISIBLE);

    }
}
