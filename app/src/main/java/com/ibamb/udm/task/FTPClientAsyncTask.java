package com.ibamb.udm.task;

import android.os.AsyncTask;
import android.os.Environment;

import com.ibamb.udm.component.file.FTPHelper;
import com.ibamb.udm.conf.DefaultConstant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FTPClientAsyncTask extends AsyncTask<String, String, Boolean> {
    @Override
    protected Boolean doInBackground(String... strings) {
        FTPHelper ftpHelper = new FTPHelper("192.168.1.5", 21, "udmuser", "123");
        int retcode = ftpHelper.download("test.txt", "1.txt");
        System.out.println("download::::::::"+retcode);
        String filename = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DefaultConstant.BASE_DIR + "/1.txt";
        File file = new File(filename);

        InputStream in = null;
        try {
            in = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            StringBuilder buffer = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line);
            }
            System.out.println(buffer.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
