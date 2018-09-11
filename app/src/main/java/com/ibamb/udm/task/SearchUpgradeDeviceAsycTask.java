package com.ibamb.udm.task;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;

import com.ibamb.dnet.module.beans.DeviceModel;
import com.ibamb.dnet.module.log.UdmLog;
import com.ibamb.dnet.module.search.DeviceSearch;
import com.ibamb.udm.conf.DefaultConstant;
import com.ibamb.udm.service.DeviceUpgradeService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class SearchUpgradeDeviceAsycTask extends AsyncTask<String, String, ArrayList<DeviceModel>> {
    private ArrayList<DeviceModel> deviceList;
    private Activity activity;


    public SearchUpgradeDeviceAsycTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<DeviceModel> doInBackground(String... strings) {
        if (deviceList != null) {
            deviceList.clear();
        }
        String keyword = strings != null && strings.length > 0 ? strings[0] : null;

        deviceList = DeviceSearch.searchDevice(keyword);
        if (deviceList == null) {
            int maxCount = 3;
            for(int i=0;i<maxCount;i++){
                deviceList = DeviceSearch.searchDevice(keyword);
                if(deviceList!=null && !deviceList.isEmpty()){
                    break;
                }
            }
            if(deviceList==null){
                deviceList = new ArrayList<>();
            }
        }
        return deviceList;
    }

    @Override
    protected void onPostExecute(ArrayList<DeviceModel> deviceModels) {
        super.onPostExecute(deviceModels);
        String versionFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+ DefaultConstant.BASE_DIR+"/"
                +DefaultConstant.VERSION_CHECK_FILE;
        FileInputStream versionFileIn = null;
        try{
            versionFileIn = new FileInputStream(versionFile);
            Properties properties = new Properties();
            properties.load(versionFileIn);

            Intent updateServiceIntent = new Intent(activity, DeviceUpgradeService.class);
            String updateVersions = properties.getProperty("device-update-version");
            String updatePackaage = properties.getProperty("device-update-package");
            updateServiceIntent.putExtra("device-update-package",updatePackaage);
            updateServiceIntent.putExtra("device-update-version",updateVersions);
            activity.startService(updateServiceIntent);
        }catch (Exception e){
            UdmLog.error(e);
        }finally {
            if(versionFileIn!=null){
                try {
                    versionFileIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
