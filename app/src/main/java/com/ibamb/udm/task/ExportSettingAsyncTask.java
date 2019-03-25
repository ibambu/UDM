package com.ibamb.udm.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ibamb.dnet.module.api.UdmClient;
import com.ibamb.dnet.module.beans.DataModel;
import com.ibamb.udm.R;
import com.ibamb.udm.conf.DefaultConstant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExportSettingAsyncTask extends AsyncTask<String, String, File> {
    private View view;
    private Activity activity;

    public ExportSettingAsyncTask(Activity activity) {
        this.view = activity.getWindow().getDecorView();
        this.activity = activity;
    }

    @Override
    protected File doInBackground(String... strings) {
        DataModel<String> dataModel = UdmClient.getInstance().exportDeviceParameters(strings[0]);
        if (dataModel.getCode() == 1) {
            String data = dataModel.getData();
            String udmDir = strings[1];
            if (strings[1] == null || strings[1].trim().length() == 0) {
                udmDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                        DefaultConstant.BASE_DIR + "/";
            }
            File baseDir = new File(udmDir);
            if (!baseDir.exists()) {
                baseDir.mkdir();
            }
            File file = new File(udmDir + strings[2] + ".dfg");
            FileWriter fileWriter = null;
            try {
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
                fileWriter = new FileWriter(file, true);
                fileWriter.write(data);
            } catch (IOException e) {

            } finally {
                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return file;
        }
        return null;
    }


    @Override
    protected void onPostExecute(final File s) {
        ImageView vExportFile = view.findViewById(R.id.profile_export_file);
        view.findViewById(R.id.profile_export_prog).setVisibility(View.GONE);
        if (s == null) {
            vExportFile.setVisibility(View.GONE);
            Toast.makeText(activity, "The export operation is fail,please try again.", Toast.LENGTH_SHORT).show();
        } else {
            vExportFile.setVisibility(View.VISIBLE);
        }
    }
}
