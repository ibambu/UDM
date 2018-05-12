/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibamb.udm.search;

import android.util.Log;

import com.ibamb.udm.beans.DeviceInfo;
import com.ibamb.udm.constants.UdmConstants;
import com.ibamb.udm.constants.UdmControl;
import com.ibamb.udm.net.UDPMessageSender;
import com.ibamb.udm.util.DataTypeConvert;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Luo Tao
 */
public class DeviceSearch {

    public static ArrayList<DeviceInfo> searchDevice() {
        DatagramSocket datagramSocket = null;
        UDPMessageSender sender = new UDPMessageSender();
        ArrayList<DeviceInfo> deviceList = new ArrayList<>();
        try {
            datagramSocket = new DatagramSocket();
            datagramSocket.setBroadcast(true);
            datagramSocket.setSoTimeout(3000);
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
            /**
             * 发送报文
             */
            InetAddress address = InetAddress.getByName(UdmConstants.UDM_BROADCAST_IP);
            DatagramPacket sendDataPacket = new DatagramPacket(serachBytes, serachBytes.length, address, UdmConstants.UDM_UDP_SERVER_PORT);
            datagramSocket.send(sendDataPacket);
            //打印发送数据
//            System.out.print("send:");
//            for (int i = 0; i < serachBytes.length; i++) {
//                System.out.print(Integer.toHexString(serachBytes[i]) + " ");
//            }
//            System.out.println("");
            /**
             * 接收报文
             */
            byte[] recevBuffer = new byte[20];//如果需要返回更多信息需要重新拼帧。期望返回的长度也需要改变。
            DatagramPacket recevPacket = new DatagramPacket(recevBuffer, recevBuffer.length);
            while (!datagramSocket.isConnected()) {
                datagramSocket.receive(recevPacket);
                byte[] replyData = recevPacket.getData();
                DeviceInfo deviceInfo = parse(replyData);
                deviceInfo.setIndex(deviceList.size()+1);
                boolean isExists = false;
                for(DeviceInfo deviceInfo1:deviceList){
                    if(deviceInfo1.getMac().equalsIgnoreCase(deviceInfo.getMac())){
                        isExists = true;
                        break;
                    }
                }
                if (!isExists) {
                    deviceList.add(deviceInfo);
                }
            }
        } catch (SocketException ex) {
            Log.e("search device err",ex.getMessage());
        } catch (UnknownHostException e) {
            Log.e("search device err",e.getMessage());
        } catch (IOException e) {
            Log.e("search device err",e.getMessage());
        } finally {
            if (datagramSocket != null) {
                datagramSocket.close();
            }
        }
        return deviceList;
    }

    /**
     * 解析帧获取设备信息。
     * @param data
     * @return
     */
    private static DeviceInfo parse(byte[] data) {
        //解析一帧
        DeviceInfo devinfo = null;
        byte control = data[0];//返回值
        if (control == UdmControl.ACKNOWLEDGE) {
            byte id = data[1];//通信编号
            byte[] length = Arrays.copyOfRange(data, 2, 4);//报文总长度
            if (DataTypeConvert.bytesToShort(length) > 0) {
//              byte[] ipTypeId = Arrays.copyOfRange(replyData, dPos + 4, dPos + 6);//ip参数ID
//              byte subFrameLength = replyData[dPos + 6];//子帧长度
                byte[] ipValue = Arrays.copyOfRange(data, 7, 11);//IP地址
                StringBuilder ipBuffer = new StringBuilder();
                for (int i = 0; i < ipValue.length; i++) {
                    String tempIp = String.valueOf(DataTypeConvert.toUnsignedByte((ipValue[i])));
                    ipBuffer.append(tempIp).append(".");
                }
                ipBuffer.deleteCharAt(ipBuffer.length() - 1);
                byte[] macTypeId = Arrays.copyOfRange(data, 11, 13);//mac参数ID
                System.out.println(Arrays.toString(macTypeId));
                byte[] macValue = Arrays.copyOfRange(data, 14, 20);//mac地址
                System.out.println(Arrays.toString(macValue));
                StringBuilder macBuffer = new StringBuilder();
                for (int i = 0; i < macValue.length; i++) {
                    String macbit = DataTypeConvert.toHexString(macValue[i]);
                    macbit = macbit.length() < 2 ? "0" + macbit : macbit;
                    macBuffer.append(macbit).append(":");
                }
                macBuffer.deleteCharAt(macBuffer.length() - 1);
                devinfo = new DeviceInfo(ipBuffer.toString(), macBuffer.toString());
            }
        }
        return devinfo;
    }

}
