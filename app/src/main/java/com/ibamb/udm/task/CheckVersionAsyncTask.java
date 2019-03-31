package com.ibamb.udm.task;

import android.os.AsyncTask;
import android.os.Environment;

import com.ibamb.dnet.module.beans.DeviceModel;
import com.ibamb.dnet.module.core.ContextData;
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
import java.util.List;

public class CheckVersionAsyncTask extends AsyncTask<String, String, Integer> {

    @Override
    protected Integer doInBackground(String... strings) {

        FTPHelper ftpHelper = new FTPHelper(UdmConstant.UDM_SERVER_DOMAINS);
        int replyCode = ftpHelper.tryDefalutConnect();
        if (replyCode == 0) {
            String localfile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DefaultConstant.BASE_DIR + "/" + UdmConstant.UDM_CMC_MAGIC;
            replyCode = ftpHelper.download(UdmConstant.UDM_CMC_MAGIC, localfile);
            String requestCode = strings[0];
            if (replyCode == -5 && "YDOWN".equals(requestCode)) {
                List<CacheFileInfo> cacheFileInfoList = readCacheFileInfos(localfile);
                for (DeviceModel deviceModel : ContextData.getInstance().getDataInfos()) {
                    for (CacheFileInfo cacheFileInfo : cacheFileInfoList) {
                        //产品型号对应的版本号不一致，则需要更新。
                        if (deviceModel.getPruductName().equals(cacheFileInfo.getProductName())
                                && !deviceModel.getFirmwareVersion().equalsIgnoreCase(cacheFileInfo.getProductVersion())) {
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
        return replyCode;
    }

    private List<CacheFileInfo> readCacheFileInfos(String localfile) {
        BufferedReader bufferedReader = null;
        List<CacheFileInfo> productCacheInfos = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(localfile), "gbk"));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String[] cacheInfos = line.split("\\|");
                if (cacheInfos.length < 3) {
                    continue;
                }
                CacheFileInfo cacheFileInfo = new CacheFileInfo(cacheInfos[0], cacheInfos[1], cacheInfos[2]);
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
