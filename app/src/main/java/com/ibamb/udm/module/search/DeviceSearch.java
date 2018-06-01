/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibamb.udm.module.search;

import com.ibamb.udm.log.UdmLog;
import com.ibamb.udm.module.beans.DeviceInfo;
import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.module.constants.Control;
import com.ibamb.udm.module.util.Convert;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;


public class DeviceSearch {

    public static ArrayList<DeviceInfo> searchDevice(String keyword) {
        DatagramSocket datagramSocket = null;
        ArrayList<DeviceInfo> deviceList = new ArrayList<>();
//        for(int i=0;i<8;i++){
//
//            DeviceInfo test = new DeviceInfo("192.168.0.110","aa:3d:3f:aa:00:hh");
//            test.setIndex(i+1);
//            if(keyword!=null && keyword.trim().length()>0){
//                if(test.getMac().contains(keyword)||test.getIp().contains(keyword)){
//                    deviceList.add(test);
//                }
//            }else{
//                deviceList.add(test);
//            }
//        }
        try {
            datagramSocket = new DatagramSocket();
            datagramSocket.setBroadcast(true);
            datagramSocket.setSoTimeout(3000);
            byte[] serachBytes = new byte[14];
            serachBytes[0] = Convert.intToUnsignedByte(Control.SEARCH_DEVICE);
            serachBytes[1] = Convert.intToUnsignedByte(0xff);
            serachBytes[2] = Convert.intToUnsignedByte(0x00);
            serachBytes[3] = Convert.intToUnsignedByte(0x0e);
            serachBytes[4] = Convert.intToUnsignedByte(0xff);
            serachBytes[5] = Convert.intToUnsignedByte(0xff);
            serachBytes[6] = Convert.intToUnsignedByte(0xff);
            serachBytes[7] = Convert.intToUnsignedByte(0xff);
            //IP地址参数ID
            serachBytes[8] = Convert.intToUnsignedByte(0x00);
            serachBytes[9] = Convert.intToUnsignedByte(0x02);
            //IP地址子报文长度
            serachBytes[10] = Convert.intToUnsignedByte(0x03);
            //MAC地址参数ID
            serachBytes[11] = Convert.intToUnsignedByte(0x00);
            serachBytes[12] = Convert.intToUnsignedByte(0x21);
            //MAC地址子报文长度
            serachBytes[13] = Convert.intToUnsignedByte(0x03);
            /**
             * 发送报文
             */
            InetAddress address = InetAddress.getByName(Constants.UDM_BROADCAST_IP);
            DatagramPacket sendDataPacket = new DatagramPacket(serachBytes, serachBytes.length, address, Constants.UDM_UDP_SERVER_PORT);
            datagramSocket.send(sendDataPacket);
            /**
             * 接收报文
             */
            byte[] recevBuffer = new byte[20];//如果需要返回更多信息需要重新拼帧。期望返回的长度也需要改变。
            DatagramPacket recevPacket = new DatagramPacket(recevBuffer, recevBuffer.length);
            //因有一定数量的设备会响应，所以只要连接没有关闭，就一直读取。
            while (!datagramSocket.isConnected()) {
                datagramSocket.receive(recevPacket);
                byte[] replyData = recevPacket.getData();
                DeviceInfo deviceInfo = parse(replyData);
                deviceInfo.setIndex(deviceList.size()+1);
                boolean isExists = false;
                for(DeviceInfo deviceInfo1:deviceList){
                    //如果重复搜索到设备则过滤掉。
                    if(deviceInfo1.getMac().equalsIgnoreCase(deviceInfo.getMac())){
                        isExists = true;
                        break;
                    }
                }
                if (!isExists) {
                    if(keyword!=null && keyword.trim().length()>0){
                        //如果ip 或者mac 匹配上关键字，则返回该设备。
                        if((deviceInfo.getIp().contains(keyword) || deviceInfo.getMac().contains(keyword))){
                            deviceList.add(deviceInfo);
                        }
                    }else{
                        deviceList.add(deviceInfo);
                    }
                }
            }

        } catch (SocketException ex) {
            UdmLog.e("search device err",ex.getMessage());
        } catch (UnknownHostException e) {
            UdmLog.e("search device err",e.getMessage());
        } catch (IOException e) {
            UdmLog.e("search device err",e.getMessage());
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
        if (control == Control.ACKNOWLEDGE) {
            byte id = data[1];//通信编号
            byte[] length = Arrays.copyOfRange(data, 2, 4);//报文总长度
            if (Convert.bytesToShort(length) > 0) {
//              byte[] ipTypeId = Arrays.copyOfRange(replyData, dPos + 4, dPos + 6);//ip参数ID
//              byte subFrameLength = replyData[dPos + 6];//子帧长度
                byte[] ipValue = Arrays.copyOfRange(data, 7, 11);//IP地址
                StringBuilder ipBuffer = new StringBuilder();
                for (int i = 0; i < ipValue.length; i++) {
                    String tempIp = String.valueOf((short)(ipValue[i]));
                    ipBuffer.append(tempIp).append(".");
                }
                ipBuffer.deleteCharAt(ipBuffer.length() - 1);
//                byte[] macTypeId = Arrays.copyOfRange(data, 11, 13);//mac参数ID
                byte[] macValue = Arrays.copyOfRange(data, 14, 20);//mac地址
                StringBuilder macBuffer = new StringBuilder();
                for (int i = 0; i < macValue.length; i++) {
                    String macbit = Convert.toHexString(macValue[i]);
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
