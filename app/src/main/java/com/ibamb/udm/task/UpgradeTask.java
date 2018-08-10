package com.ibamb.udm.task;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.ibamb.udm.module.beans.DeviceModel;
import com.ibamb.udm.module.beans.RetMessage;
import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.module.file.FileRemoteTransfer;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

    public UpgradeTask(LocalBroadcastManager broadcastManager, DeviceModel device, ZipFile upgradePatch) {
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
                    FileRemoteTransfer transfer = new FileRemoteTransfer(dataReader, dataWriter);
                    retMessage = transfer.sendZipFile(upgradePatch);
                    if(retMessage.getCode()==Constants.UPGRADE_SUCCESS_CODE){
                        //文件发送成功，升级成功,注意本机升级不自动重启.
                        byte[] bystes = Constants.UPGRADE_RESTART_CODE.getBytes();
                        char enter = 0x0d;
                        byte[] enterbytes = charToByte(enter);
                        byte[] alldata = new byte[bystes.length + enterbytes.length];
                        System.arraycopy(bystes, 0, alldata, 0, bystes.length);
                        System.arraycopy(enterbytes, 0, alldata, bystes.length, enterbytes.length);
                        dataWriter.write(alldata);
                    }
                }
            }

        } catch (Exception e) {
            retMessage.setCode(Constants.UPGRADE_FAIL_CODE);
            e.printStackTrace();
        } finally {
            Thread.sleep(2000);
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

    private byte[] charToByte(char c) {
        byte[] b = new byte[2];
        b[0] = (byte) ((c & 0xFF00) >> 8);
        b[1] = (byte) (c & 0xFF);
        return b;
    }

    /**
     * 是否合法主机
     *
     * @return
     */
    private boolean isValidHost() {
        boolean isValid = false;
        byte[] b = new byte[1024 * 10];
        try {
            int length = dataReader.read(b);
            String rsp = new String(b, 0, length);
            if (rsp.contains(Constants.UPGRADE_VLID_HOST)) {
                isValid = true;
            }

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
