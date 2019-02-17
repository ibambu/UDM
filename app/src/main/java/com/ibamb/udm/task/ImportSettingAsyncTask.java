package com.ibamb.udm.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.ibamb.dnet.module.api.UdmClient;
import com.ibamb.udm.R;

public class ImportSettingAsyncTask extends AsyncTask<String, String, Boolean> {
    private View view;
    private Activity activity;

    public ImportSettingAsyncTask(Activity activity) {
        this.view = activity.getWindow().getDecorView();
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        boolean isImportSuccess = UdmClient.getInstance().importDeviceParameters(strings[0],strings[1]);

        return isImportSuccess;
    }


    @Override
    protected void onPostExecute(Boolean isImportSuccess) {
        view.findViewById(R.id.profile_import_prog).setVisibility(View.GONE);
        if (isImportSuccess) {
            Toast.makeText(activity, "The import operation is successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "The import operation is fail,please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
