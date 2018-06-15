package com.ibamb.udm.task;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.ibamb.udm.R;
import com.ibamb.udm.module.sys.SysManager;

public class SaveAndRebootAsyncTask extends AsyncTask<String, Boolean, Boolean> {

    private View view;

    public SaveAndRebootAsyncTask(View view) {
        this.view = view;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String mac  = strings[0];
        return SysManager.saveAndReboot(mac);
    }

    @Override
    protected void onPostExecute(Boolean isSuccess) {
        super.onPostExecute(isSuccess);
        String notice = isSuccess ? "successful":"fail";
        View achor = view.findViewById(R.id.anchor);
        Snackbar.make(achor,  notice, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    protected void onProgressUpdate(Boolean... values) {
        super.onProgressUpdate(values);
    }
}
