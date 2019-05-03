package com.ibamb.udm.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.ibamb.dnet.module.beans.DeviceModel;
import com.ibamb.dnet.module.beans.RetMessage;
import com.ibamb.dnet.module.core.ContextData;
import com.ibamb.dnet.module.log.UdmLog;
import com.ibamb.udm.R;
import com.ibamb.udm.beans.CacheFileInfo;
import com.ibamb.udm.component.constants.UdmConstant;
import com.ibamb.udm.component.file.FTPHelper;
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


public class DeviceMaintainAsyncTask extends AsyncTask<String, String, String> {

    private Activity activity;

    public DeviceMaintainAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... strings) {

        String retMessage = "";
        String mac = strings[0];
        String productName = strings[1];
        String productVersion = strings[2];
        FTPHelper ftpHelper = new FTPHelper(UdmConstant.UDM_SERVER_DOMAINS[0], 21, DefaultConstant.USER_NAME, DefaultConstant.PASSWORD);
        int retcode = ftpHelper.connect();
        if (retcode != 0) {
            FTPHelper ftpHelper1 = new FTPHelper(UdmConstant.UDM_SERVER_DOMAINS[1], 21, DefaultConstant.USER_NAME, DefaultConstant.PASSWORD);
            retcode = ftpHelper1.connect();
            if (retcode == 0) {
                ftpHelper = ftpHelper1;
            } else {
                retMessage = "The  operation is fail,please try again.";
                return retMessage;
            }
        }
        String localfile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DefaultConstant.BASE_DIR + "/" + UdmConstant.UDM_CMC_MAGIC;
        retcode = ftpHelper.download(UdmConstant.UDM_CMC_MAGIC, localfile);
        boolean isLocalLatest = true;
        CacheFileInfo latestfile = null;
        if (retcode == -5) {
            List<CacheFileInfo> productCacheInfos = readCacheFileInfos(localfile);

            for (CacheFileInfo cacheFileInfo : productCacheInfos) {
                if (cacheFileInfo.getProductName().equalsIgnoreCase(productName)
                        && !cacheFileInfo.getProductVersion().equalsIgnoreCase(productVersion)) {
                    isLocalLatest = false;
                    latestfile = cacheFileInfo;
                    break;
                }
            }
        }
        if (!isLocalLatest) {
            String localFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DefaultConstant.BASE_DIR + "/" + latestfile.getCacheFileName();
            int dCode = ftpHelper.download(latestfile.getCacheFileName(), localFile);//is zip file
            if (dCode == -5) {
                //clean the cache.
                RetMessage rtmsg = cleanDeviceCache(localFile, mac);
                if (rtmsg == null || rtmsg.getCode() != 1) {
                    retMessage = "The  operation is fail,please try again.";
                }
            } else {
                retMessage = "The  operation is fail,please try again.";
            }
        } else {
            retMessage = "The device is clean.";
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
                String[] cacheInfos = line.split("\\|");
                if (cacheInfos.length < 4) {
                    continue;
                }
                CacheFileInfo cacheFileInfo = new CacheFileInfo(cacheInfos[0], cacheInfos[1], cacheInfos[2],cacheInfos[3]);
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

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        activity.findViewById(R.id.profile_maintain_prog).setVisibility(View.GONE);
        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
}
