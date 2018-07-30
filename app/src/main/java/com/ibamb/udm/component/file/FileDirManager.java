package com.ibamb.udm.component.file;

import android.app.Activity;

import java.io.File;

public class FileDirManager {
    private Activity activity;

    public FileDirManager(Activity activity) {
        this.activity = activity;
    }

    public  File getFileByName(String fileName){
        File filesDir = activity.getFilesDir();
        File[] files = filesDir.listFiles();
        File retFile = null;
        for(File file:files){
            if(file.getName().equals(fileName)){
                retFile = file;
                break;
            }
        }
        return retFile;
    }
}
