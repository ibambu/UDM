package com.ibamb.udm.component.file;

import android.os.Environment;

import com.ibamb.dnet.module.log.UdmLog;
import com.ibamb.udm.conf.DefaultConstant;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;

public class FTPHelper {
    private String ftpServer;
    private int ftpPort;
    private String userName;
    private String password;

    public FTPHelper(String ftpServer, int ftpPort, String userName, String password) {
        this.ftpServer = ftpServer;
        this.ftpPort = ftpPort;
        this.userName = userName;
        this.password = password;
    }

    /**
     * FTP 下载文件
     *
     * @param remoteFile 远程FTP服务器上的文件名
     * @param localFile  下载到本地存储的文件名
     * @return
     */
    public int download(String remoteFile, String localFile) {
        FTPClient ftpClient = new FTPClient();
        int retCode = 0;
        OutputStream output = null;
        try {
            ftpClient.connect(ftpServer, ftpPort);
            int reply = ftpClient.getReplyCode();
            //是否连接成功
            if (!FTPReply.isPositiveCompletion(reply)) {
                retCode = -1;
                throw new ConnectException("The server refused to connect.");
            }
            //登陆
            if (!ftpClient.login(userName, password)) {
                retCode = -2;
                throw new ConnectException("Incorrect username or password.");
            }
            ftpClient.login(userName, password);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setControlEncoding("UTF-8");
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+ DefaultConstant.BASE_DIR+"/";
            File file = new File(path);
            if(!file.exists()){
                file.mkdir();
            }
            output = new FileOutputStream(localFile);
            boolean isSuccess = ftpClient.retrieveFile(remoteFile, output);
            if(isSuccess){
                retCode = -5;
            }

        } catch (ConnectException e){
            e.printStackTrace();
            retCode = -3;
        }catch (Exception e) {
            retCode = -4;
            e.printStackTrace();
            UdmLog.error(e);
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                UdmLog.error(e);
            }
        }
        return retCode;
    }
}
