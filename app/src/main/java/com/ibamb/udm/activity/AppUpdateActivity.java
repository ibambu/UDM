package com.ibamb.udm.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ibamb.udm.R;
import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.module.log.UdmLog;
import com.ibamb.udm.task.AppUpdateAsyncTask;
import com.ibamb.udm.task.AppVersionCheckAsyncTask;
import com.ibamb.udm.util.TaskBarQuiet;

public class AppUpdateActivity extends AppCompatActivity {
    private static final String DIR = "download";
    private static final String APK = "udm.apk";
    private static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DIR + "/" + APK;

    private TextView vUpdateCenterUrl;
    private ProgressBar progressBar;
    private TextView vDownloadInfo;
    public  String lastestVersionDesc;
    public  String latestVersionDownUrl;
    public  String apkFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_update);
        TaskBarQuiet.setStatusBarColor(this, Constants.TASK_BAR_COLOR);

        TextView title = findViewById(R.id.title);
        title.setText("About udm");

        ImageView vCommit = findViewById(R.id.do_commit);
        vCommit.setVisibility(View.GONE);

        ImageView vGoBack = findViewById(R.id.go_back);
        vGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        vUpdateCenterUrl = findViewById(R.id.update_center_url);

        Button vCheckUpdate = findViewById(R.id.check_update);
        vCheckUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updateCenterUrl = vUpdateCenterUrl.getText().toString();
                if (!Patterns.WEB_URL.matcher(updateCenterUrl).matches() && !URLUtil.isValidUrl(updateCenterUrl)) {
                    vUpdateCenterUrl.setFocusable(true);
                    Toast.makeText(AppUpdateActivity.this, "Invalid Update Center URL.", Toast.LENGTH_SHORT).show();
                }else{
                    AppVersionCheckAsyncTask task = new AppVersionCheckAsyncTask(AppUpdateActivity.this);
                    task.execute(updateCenterUrl);
                }
            }
        });

        Button vUpdate = findViewById(R.id.do_upgrade);
        vUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUpdateAsyncTask task = new AppUpdateAsyncTask(AppUpdateActivity.this);
                task.execute(latestVersionDownUrl);
            }
        });

        progressBar = findViewById(R.id.app_update_progressr);
        vDownloadInfo = findViewById(R.id.download_info);

        /**
         * 当前版本信息
         */
        TextView vCurrentVersion = findViewById(R.id.current_version);
        TextView vNewVersion = findViewById(R.id.new_version);
        try{
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            vCurrentVersion.setText("Current version: "+versionName);
            vNewVersion.setText("Latest version: "+versionName);
        }catch (Exception e){
            UdmLog.error(e);
        }

        vNewVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Version Describle")
                        .setMessage(lastestVersionDesc)
                        .create().show();
            }
        });
        AppVersionCheckAsyncTask task = new AppVersionCheckAsyncTask(AppUpdateActivity.this);
        task.execute(vUpdateCenterUrl.getText().toString());

    }

    @Override
    protected void onStart() {

        super.onStart();
    }

}
