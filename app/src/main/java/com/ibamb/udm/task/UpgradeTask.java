package com.ibamb.udm.task;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.ibamb.dnet.module.beans.DeviceModel;
import com.ibamb.dnet.module.beans.RetMessage;
import com.ibamb.dnet.module.constants.Constants;
import com.ibamb.dnet.module.file.FileRemoteTransfer;
import com.ibamb.dnet.module.log.UdmLog;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.Callable;
import java.util.zip.ZipFile;

public class UpgradeTask implements Callable {
    private ZipFile upgradePatch;//升级包中的数据文件。
    private DeviceModel device;//升级设备

    private Socket socket;//套接字
    private DataInputStream dataReader;//数据读
    private DataOutputStream dataWriter;//数据写

    private LocalBroadcastManager broadcastManager;


    public UpgradeTask(ZipFile upgradePatch, DeviceModel device, LocalBroadcastManager broadcastManager) {
        this.upgradePatch = upgradePatch;
        this.device = device;
        this.broadcastManager = broadcastManager;
    }


    @Override
    public Object call() throws Exception {

        /**
         * 探测主机是否可达,3秒后无响应当作不可达。
         */
        boolean isValidHost;
        RetMessage retMessage = new RetMessage(false);
        try {
            InetAddress inetAddress = InetAddress.getByName(device.getIp());
            boolean isReachable = inetAddress.isReachable(3000);
            if (isReachable) {
                socket = new Socket(device.getIp(), Constants.UPGRADE_REMOTE_PORT);
                socket.setSoTimeout(Constants.UPGRADE_TIME_OUT);//设置连接超时时间
                dataReader = new DataInputStream(socket.getInputStream());
                dataWriter = new DataOutputStream(socket.getOutputStream());
                isValidHost = isValidHost();//读取返回信息，验证是否合法IP。
                if (isValidHost) {
                    UdmLog.info("start send file to "+device.getIp());
                    FileRemoteTransfer transfer = new FileRemoteTransfer(dataReader, dataWriter);
                    retMessage = transfer.sendZipFile(upgradePatch);
                    UdmLog.info("send file to "+device.getIp()+" result: "+retMessage.getCode());
                }
            }

        } catch (Exception e) {
            retMessage.setCode(Constants.UPGRADE_FAIL_CODE);
        } finally {
            if (socket != null) {
                socket.close();
            }
            if (dataReader != null) {
                dataReader.close();
            }
            if (dataWriter != null) {
                dataWriter.close();
            }
            device.setUpgradeCode(Constants.UPGRADE_SUCCESS_CODE);
            Intent intent = new Intent("com.ibamb.udm.service");
            intent.putExtra("UPGRADE_COUNT", String.valueOf(1));
            broadcastManager.sendBroadcast(intent);
        }
        return retMessage;
    }


    /**
     * 是否合法主机
     *
     * @return
     */
    private boolean isValidHost() {
        boolean isValid = false;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(dataReader));
            String rsp = reader.readLine();
            if (rsp.contains(Constants.UPGRADE_VLID_HOST)) {
                isValid = true;
            }
            UdmLog.info("check valid host response:"+rsp);
        } catch (SocketTimeoutException ex) {
        } catch (Exception e) {
        }
        return isValid;
    }

    /**
     * 写日志文件
     *
     * @param content
     * @throws IOException
     */
    private void writeToLogFile(String content, String filename) throws IOException {
        BufferedWriter bufwriter = null;
        String file = System.getProperty("user.dir") + "/logs";
        File fileObj = new File(file);
        if (!fileObj.exists()) {
            fileObj.mkdir();
        }
        try {
            OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(file + "/" + filename, true), "UTF-8");
            bufwriter = new BufferedWriter(writerStream);
            bufwriter.write(content);
            bufwriter.newLine();
            bufwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufwriter != null) {
                bufwriter.close();
            }
        }
    }

    /**
     * 写报告文件
     *
     * @param content
     * @throws IOException
     */
    private void writeReport(String content, String filename) throws IOException {
        BufferedWriter bufwriter = null;
        String file = System.getProperty("user.dir") + "/" + filename;
        try {
            OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8");
            bufwriter = new BufferedWriter(writerStream);
            bufwriter.write(content);
            bufwriter.newLine();
            bufwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufwriter != null) {
                bufwriter.close();
            }
        }
    }
}
