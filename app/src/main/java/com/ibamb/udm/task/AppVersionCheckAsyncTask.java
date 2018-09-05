package com.ibamb.udm.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ibamb.udm.R;
import com.ibamb.udm.activity.AppUpdateActivity;
import com.ibamb.dnet.module.log.UdmLog;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AppVersionCheckAsyncTask extends AsyncTask<String, Integer, String> {

    private Activity activity;

    public AppVersionCheckAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... strings) {
        String versionCheckUrl = strings[0];
        String jsonData = readParse(versionCheckUrl);
        return jsonData;
    }

    private String readParse(String urlPath) {
        InputStream inStream = null;
        String versionInfo = null;
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int len = 0;
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            inStream = conn.getInputStream();
            while ((len = inStream.read(data)) != -1) {
                outStream.write(data, 0, len);
            }
            versionInfo = new String(outStream.toByteArray());//通过out.Stream.toByteArray获取到写的数据
        } catch (Exception e) {
            UdmLog.error(e);
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return versionInfo;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        if (s == null || s.trim().length() == 0) {
            Toast.makeText(activity, "Check update failure,mabye remote server no response.", Toast.LENGTH_SHORT).show();
        } else {
            View mainView = activity.getWindow().getDecorView();
            TextView vVersion = mainView.findViewById(R.id.new_version);
            try {
                //获取软件版本号，对应AndroidManifest.xml下android:versionCode
                int versionCode = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionCode;
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject != null) {
                    String versionId = jsonObject.getString("versionId");
                    if (versionCode < Integer.parseInt(versionId)) {
                        String versionName = jsonObject.getString("versionName");
                        vVersion.setText("Latest version: " + versionName);
                        String versionChangeDesc = jsonObject.getString("versionChangeDesc");
                        String packUrl = jsonObject.getString("packUrl");
                        String apkFileName = jsonObject.getString("apkFileName");
                        ((AppUpdateActivity) activity).lastestVersionDesc = versionChangeDesc;
                        ((AppUpdateActivity) activity).latestVersionDownUrl = packUrl;
                        ((AppUpdateActivity)activity).apkFileName =apkFileName;
                        mainView.findViewById(R.id.do_upgrade).setVisibility(View.VISIBLE);
                        mainView.findViewById(R.id.check_update).setVisibility(View.GONE);
                        Toast.makeText(activity, "Find a new version", Toast.LENGTH_SHORT).show();
                    } else {
                        mainView.findViewById(R.id.do_upgrade).setVisibility(View.GONE);
                        mainView.findViewById(R.id.check_update).setVisibility(View.VISIBLE);
                        Toast.makeText(activity, "It's the latest version", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mainView.findViewById(R.id.do_upgrade).setVisibility(View.GONE);
                    mainView.findViewById(R.id.check_update).setVisibility(View.VISIBLE);
                    Toast.makeText(activity, "Getting version information failed.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                UdmLog.error(e);
            }
        }
    }
}
