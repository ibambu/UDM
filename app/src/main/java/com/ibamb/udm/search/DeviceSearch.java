/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibamb.udm.search;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.ibamb.udm.beans.DeviceInfo;
import com.ibamb.udm.constants.UdmControl;
import com.ibamb.udm.net.UDPMessageSender;
import com.ibamb.udm.util.DataTypeConvert;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Luo Tao
 */
public class DeviceSearch {

    public static ArrayList<DeviceInfo> searchDevice() {
        DatagramSocket datagramSocket;
        UDPMessageSender sender = new UDPMessageSender();
        ArrayList<DeviceInfo> deviceList = new ArrayList<>();
        try {
            datagramSocket = new DatagramSocket();
            byte[] serachBytes = new byte[14];
            serachBytes[0] = DataTypeConvert.intToUnsignedByte(UdmControl.SEARCH_DEVICE);
            serachBytes[1] = DataTypeConvert.intToUnsignedByte(0xff);
            serachBytes[2] = DataTypeConvert.intToUnsignedByte(0x00);
            serachBytes[3] = DataTypeConvert.intToUnsignedByte(0x0e);
            serachBytes[4] = DataTypeConvert.intToUnsignedByte(0xff);
            serachBytes[5] = DataTypeConvert.intToUnsignedByte(0xff);
            serachBytes[6] = DataTypeConvert.intToUnsignedByte(0xff);
            serachBytes[7] = DataTypeConvert.intToUnsignedByte(0xff);
            //IP地址参数ID
            serachBytes[8] = DataTypeConvert.intToUnsignedByte(0x00);
            serachBytes[9] = DataTypeConvert.intToUnsignedByte(0x02);
            //IP地址子报文长度
            serachBytes[10] = DataTypeConvert.intToUnsignedByte(0x03);
            //MAC地址参数ID
            serachBytes[11] = DataTypeConvert.intToUnsignedByte(0x00);
            serachBytes[12] = DataTypeConvert.intToUnsignedByte(0x21);
            //MAC地址子报文长度
            serachBytes[13] = DataTypeConvert.intToUnsignedByte(0x03);
            byte[] replyData = sender.send(datagramSocket, serachBytes, 1024 * 3);
            /**
             * 解析报文，注意每台设备的回复的是一个完整的报文。
             */
            int deviceCount = 0;//搜索到的设备数量，默认为0.
            for (int k = 0; k < replyData.length; k = k + 20) {
                //解析一帧
                int dPos = deviceCount + k;
                byte control = replyData[dPos];//返回值
                /**
                 * 返回成功开始解析
                 */
                if (control == UdmControl.ACKNOWLEDGE) {
                    byte id = replyData[dPos + 1];//通信编号
                    byte[] length = Arrays.copyOfRange(replyData, dPos + 2, dPos + 4);//报文总长度
                    if (DataTypeConvert.bytesToShort(length) == 0) {
                        continue;
                    }
//                    byte[] ipTypeId = Arrays.copyOfRange(replyData, dPos + 4, dPos + 6);//ip参数ID
//                    byte subFrameLength = replyData[dPos + 6];//子帧长度
                    byte[] ipValue = Arrays.copyOfRange(replyData, dPos + 7, dPos + 11);//IP地址
                    StringBuilder ipBuffer = new StringBuilder();
                    for (int i = 0; i < ipValue.length; i++) {
                        String tempIp = String.valueOf(DataTypeConvert.toUnsignedByte((ipValue[i])));
                        ipBuffer.append(tempIp).append(".");
                    }
                    ipBuffer.deleteCharAt(ipBuffer.length() - 1);
                    byte[] macTypeId = Arrays.copyOfRange(replyData, dPos + 11, dPos + 13);//mac参数ID
                    System.out.println(Arrays.toString(macTypeId));
                    byte[] macValue = Arrays.copyOfRange(replyData, dPos + 14, dPos + 20);//mac地址
                    System.out.println(Arrays.toString(macValue));
                    StringBuilder macBuffer = new StringBuilder();
                    for (int i = 0; i < macValue.length; i++) {
                        String macbit = DataTypeConvert.toHexString(macValue[i]);
                        macbit = macbit.length() < 2 ? "0" + macbit : macbit;
                        macBuffer.append(macbit).append(":");
                    }
                    macBuffer.deleteCharAt(macBuffer.length() - 1);
                    DeviceInfo devinfo = new DeviceInfo(ipBuffer.toString(), macBuffer.toString());
                    System.out.println(devinfo.toString());
                    devinfo.setIndex(deviceList.size()+1);
                    deviceList.add(devinfo);

                }
            }
        } catch (SocketException ex) {
        }
        return deviceList;
    }

}
