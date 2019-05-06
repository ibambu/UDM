package com.ibamb.udm.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ibamb.dnet.module.beans.DeviceModel;
import com.ibamb.dnet.module.beans.RetMessage;
import com.ibamb.dnet.module.core.ContextData;
import com.ibamb.dnet.module.log.UdmLog;
import com.ibamb.udm.R;
import com.ibamb.udm.beans.CacheFileInfo;
import com.ibamb.udm.component.constants.UdmConstant;
import com.ibamb.udm.conf.DefaultConstant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipFile;

public class AllDeviceMaintainAsyncTask extends AsyncTask<String, Integer, String> {

    private Activity activity;

    public AllDeviceMaintainAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... strings) {
        String localfile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DefaultConstant.BASE_DIR + "/" + UdmConstant.UDM_CMC_MAGIC;
        List<CacheFileInfo> cacheFileInfoList = readCacheFileInfos(localfile);
        int maintainCount = 0;
        ExecutorService pool = Executors.newFixedThreadPool(1);
        for (DeviceModel deviceModel : ContextData.getInstance().getDataInfos()) {
            for (CacheFileInfo cacheFileInfo : cacheFileInfoList) {
                //产品型号对应的版本号不一致，则需要更新。
                if (deviceModel.getPruductName().equals(cacheFileInfo.getProductName())
                        && !deviceModel.getSerailNO().equalsIgnoreCase(cacheFileInfo.getSerailNO())) {
                    String cacheFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DefaultConstant.BASE_DIR + "/"
                            + cacheFileInfo.getCacheFileName();
                    try {
                        ZipFile zipPackage = new ZipFile(new File(cacheFile));
                        UpgradeTask upgradeTask = new UpgradeTask(zipPackage, deviceModel, null);
                        RetMessage retMessage = (RetMessage) pool.submit(upgradeTask).get();
                        pool.shutdown();
                    } catch (Exception e) {
                        UdmLog.error(e);
                    }
                }
            }
            maintainCount++;
            publishProgress(maintainCount);//更新进度
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        ProgressBar progressBar = activity.findViewById(R.id.upgrade_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        TextView textView = activity.findViewById(R.id.upgrade_progress);
        textView.setVisibility(View.VISIBLE);
        textView.setText("Maintain Count: " + values[0] + "/" + ContextData.getInstance().getDataInfos().size());

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Toast.makeText(activity, "Maintain successful.", Toast.LENGTH_SHORT).show();
        ProgressBar progressBar = activity.findViewById(R.id.upgrade_progress_bar);
        progressBar.setVisibility(View.GONE);
    }


    public RetMessage cleanDeviceCache(String cacheFileZip, String mac) {

        List<DeviceModel> deviceInfos = ContextData.getInstance().getDataInfos();
        RetMessage retMessage = null;
        if (deviceInfos != null && !deviceInfos.isEmpty()) {
            try {
                ZipFile zipPackage = new ZipFile(new File(cacheFileZip));
                ExecutorService pool = Executors.newFixedThreadPool(1);
                for (DeviceModel device : deviceInfos) {
                    if (device.getMac().equalsIgnoreCase(mac)) {
                        UpgradeTask upgradeTask = new UpgradeTask(zipPackage, device, null);
                        retMessage = (RetMessage) pool.submit(upgradeTask).get();
                        break;
                    }
                }
                pool.shutdown();
            } catch (Exception e) {
                UdmLog.error(e);
            }
        }
        return retMessage;
    }

    private List<CacheFileInfo> readCacheFileInfos(String localfile) {
        BufferedReader bufferedReader = null;
        List<CacheFileInfo> productCacheInfos = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(localfile), "gbk"));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String[] cacheInfos = line.split(":");
                String productName = "";
                if (cacheInfos.length > 1) {
                    productName = cacheInfos[0];
                }
                String[] versions = cacheInfos[1].split("\\|");
                if (versions.length < 3) {
                    continue;
                }
                CacheFileInfo cacheFileInfo = new CacheFileInfo(productName, versions[0], versions[1], cacheInfos[2]);
                productCacheInfos.add(cacheFileInfo);
            }
        } catch (Exception e) {
            UdmLog.getErrorTrace(e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return productCacheInfos;
    }
}
