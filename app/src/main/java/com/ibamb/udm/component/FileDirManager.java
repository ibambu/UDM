package com.ibamb.udm.component;

import android.app.Activity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

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
            System.out.println("====="+file.getName());
            if(file.getName().equals(fileName)){
                retFile = file;
                break;
            }
        }
        return retFile;
    }

    /**
     * 将内容写到文本类型的文件。
     * @param content
     * @param file
     */
    public void writeFile(String content,File file){
        BufferedWriter bufwriter = null;
        try {
            OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            bufwriter = new BufferedWriter(writerStream);
            bufwriter.write(content);
            bufwriter.newLine();
            bufwriter.close();
        } catch (IOException e) {

        } finally {
            if (bufwriter != null) {
                try {
                    bufwriter.close();
                } catch (IOException e) {

                }
            }
        }

    }
}
