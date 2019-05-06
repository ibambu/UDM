package com.ibamb.udm.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.ibamb.dnet.module.log.UdmLog;
import com.ibamb.udm.beans.CacheFileInfo;
import com.ibamb.udm.component.constants.UdmConstant;
import com.ibamb.udm.component.file.FTPHelper;
import com.ibamb.udm.conf.DefaultConstant;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CheckVersionIntentService extends IntentService {

    private static final String LOG_TAG = CheckVersionIntentService.class.getName();

    private static final String ACTION_CHECK_VERSION = "com.ibamb.udm.service.action.CHECK_VERSION";

    private static final String CHECK_VERSION_DOWN = "com.ibamb.udm.service.extra.DOWN";

    public CheckVersionIntentService() {
        super("CheckVersionIntentService");
    }

    public static void startActionCheckVersion(Context context, String needDown) {
        Intent intent = new Intent(context, CheckVersionIntentService.class);
        intent.setAction(ACTION_CHECK_VERSION);
        intent.putExtra(CHECK_VERSION_DOWN, needDown);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            final String needDown = intent.getStringExtra(CHECK_VERSION_DOWN);
            if (ACTION_CHECK_VERSION.equals(action)) {
                handleActionCheckVersion(needDown);
            }
        }
    }

    private void handleActionCheckVersion(String needDown) {
        FTPHelper ftpHelper = new FTPHelper(UdmConstant.UDM_SERVER_DOMAINS);
        int replyCode = ftpHelper.tryDefalutConnect();
        List<CacheFileInfo> cacheFileInfoList = null;
        if (replyCode == 0) {
            String localfile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DefaultConstant.BASE_DIR + "/" + UdmConstant.UDM_CMC_MAGIC;
            replyCode = ftpHelper.download(UdmConstant.UDM_CMC_MAGIC, localfile);
            String requestCode = needDown;
            Log.i(LOG_TAG,"down version file reply code is "+replyCode+" and the request action is "+needDown);
            if (replyCode == -5 && "YDOWN".equals(requestCode)) {
                cacheFileInfoList = readCacheFileInfos(localfile);
                Map localVersionMap = getSharedPreferences("UDM_CONTEXT", MODE_PRIVATE).getAll();
                for (Iterator it = localVersionMap.keySet().iterator(); it.hasNext(); ) {
                    String productName = (String) it.next();
                    String versionInfo = (String) localVersionMap.get(productName);
                    String[] tempstrings = versionInfo.split("#");
                    if (versionInfo == null || tempstrings.length < 2) {
                        continue;
                    }
                    String serialNO = tempstrings[1];
                    for (CacheFileInfo cacheFileInfo : cacheFileInfoList) {
                        //产品型号对应的版本号不一致，则需要更新。
                        if (productName.equals(cacheFileInfo.getProductName())
                                && !serialNO.equalsIgnoreCase(cacheFileInfo.getSerailNO())) {
                            String localFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DefaultConstant.BASE_DIR + "/"
                                    + cacheFileInfo.getCacheFileName();
                            try {
                                ftpHelper.download(cacheFileInfo.getCacheFileName(), localFile);//is zip file
                            } catch (Exception e) {

                            }
                        }
                    }
                }
            }
        }
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
