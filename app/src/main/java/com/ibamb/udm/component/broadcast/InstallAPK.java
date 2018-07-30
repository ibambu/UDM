package com.ibamb.udm.component.broadcast;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;

public class InstallAPK {
    /**
     * 安装APK
     * @param context
     * @param apkPath
     */
    public static boolean installApk(Context context, String apkPath) {

        try{

            if (context == null || TextUtils.isEmpty(apkPath)) {
                return false;
            }
            setPermission(apkPath);
            File file = new File(apkPath);
            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //判读版本是否在7.0以上
            if (Build.VERSION.SDK_INT >= 24) {
                Uri apkUri = FileProvider.getUriForFile(context, "com.ibamb.udm.fileprovider", file);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//赋予临时权限
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }
            context.startActivity(intent);

        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }
    /**
     * 提升读写权限
     * @param filePath 文件路径
     * @return
     * @throws IOException
     */
    public static void setPermission(String filePath)  {
        String command = "chmod " + "777" + " " + filePath;
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
