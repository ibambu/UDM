package com.ibamb.udm.module.log;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UdmLog {

    public static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
    private static File errorLogFile;

    public static void setErrorLogFile(File errorLogFile) {
        UdmLog.errorLogFile = errorLogFile;
    }

    public static void e(String tag, String message) {
        if (message != null && message.trim().length() > 0) {

        }
    }

    public static void error(Throwable throwable) {
        writeLog(getErrorTrace(throwable), errorLogFile);
    }

    public static String getErrorTrace(Throwable t) {
        StringWriter stringWriter = null;
        StringBuffer buffer = new StringBuffer();
        PrintWriter writer = null;
        try {
            stringWriter = new StringWriter();
            writer = new PrintWriter(stringWriter);
            t.printStackTrace(writer);
            buffer = stringWriter.getBuffer();
        } catch (Exception e) {

        } finally {

            try {
                if (stringWriter != null) {
                    stringWriter.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {

            }
        }
        return buffer.toString();
    }

    /**
     * 将内容写到文本类型的文件。
     *
     * @param content
     * @param file
     */
    public static void writeLog(String content, File file) {
        FileWriter fileWriter = null;
        try {
            if(file!=null){
                if (!file.exists()) {
                    file.createNewFile();
                }
                String logtime = "----------------" + timeFormat.format(new Date()) + "---------------\n";
                fileWriter = new FileWriter(file, true);
                fileWriter.write(logtime + content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {

                }
            }
        }
    }

    /**
     * 将日志上传到远程服务器
     *
     * @param uploadUrl
     */

    public static void uploadErrorLog(String uploadUrl) {
        FileInputStream logInputStream = null;
        DataOutputStream dataOutputStream = null;
        try {
            String end = "\r\n";
            String twoHyphens = "--";
            String boundary = "******";
            URL url = new URL(uploadUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
            // 允许输入输出流
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            // 使用POST方法
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.writeBytes(twoHyphens + boundary + end);
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploadfile\"; filename=\""
                    + errorLogFile.getName() + "\"" + end);
            dataOutputStream.writeBytes(end);

            logInputStream = new FileInputStream(errorLogFile);
            byte[] buffer = new byte[8192]; // 一次读8k
            int count = 0;
            // 读取文件
            while ((count = logInputStream.read(buffer)) != -1) {
                dataOutputStream.write(buffer, 0, count);
            }
            logInputStream.close();
            dataOutputStream.writeBytes(end);
            dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dataOutputStream.flush();

            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String result = br.readLine();

        } catch (Exception e) {
            UdmLog.error(e);
        } finally {
            try {
                dataOutputStream.close();
                logInputStream.close();
            } catch (IOException e) {

            }
        }
    }

}
