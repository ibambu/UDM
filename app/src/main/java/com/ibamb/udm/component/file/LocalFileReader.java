package com.ibamb.udm.component.file;

import com.ibamb.dnet.module.log.UdmLog;
import com.ibamb.udm.beans.CacheFileInfo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocalFileReader {
    public static List<CacheFileInfo> readCacheFileInfos(String localfile) {
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
                    String[] latestVersionInfo = cacheInfos[1].split("#");
                    if (latestVersionInfo.length > 0) {
                        String[] versionInfo = latestVersionInfo[0].split("\\|");
                        System.out.println("------------------->" + productName + ":" + Arrays.toString(versionInfo));
                        CacheFileInfo cacheFileInfo = new CacheFileInfo(productName, versionInfo[0], versionInfo[1], versionInfo[2]);
                        productCacheInfos.add(cacheFileInfo);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
