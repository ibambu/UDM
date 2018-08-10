package com.ibamb.udm.module.sys;


import com.ibamb.udm.module.constants.Constants;
import com.ibamb.udm.module.constants.Control;
import com.ibamb.udm.module.util.Convert;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class SysManager {
    public static boolean saveAndReboot(String mac){
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket();
            datagramSocket.setSoTimeout(1000);
            //将目标设备mac地址转成字节数组
            byte[] macData = Convert.hexStringtoBytes(mac.replaceAll(":", ""));
            byte[] byteBuffer = new byte[20];
            byteBuffer[0] = Convert.intToUnsignedByte(Control.SET_PARAMETERS);
            byteBuffer[1] = Convert.intToUnsignedByte(0x00);
            byteBuffer[2] = Convert.intToUnsignedByte(0x00);
            byteBuffer[3] = Convert.intToUnsignedByte(0x14);
            //IP地址
            byteBuffer[4] = Convert.intToUnsignedByte(0x00);
            byteBuffer[5] = Convert.intToUnsignedByte(0x00);
            byteBuffer[6] = Convert.intToUnsignedByte(0x00);
            byteBuffer[7] = Convert.intToUnsignedByte(0x00);
            //MAC地址
            for(int i=0;i<macData.length;i++){
                byteBuffer[8+i] = macData[i];
            }
            //SYS_SAVE_RESTART 参数 (0x200)
            byteBuffer[16] = Convert.intToUnsignedByte(0x02);
            byteBuffer[17] = Convert.intToUnsignedByte(0x00);
            byteBuffer[18] = Convert.intToUnsignedByte(0x04);
            byteBuffer[19] = Convert.intToUnsignedByte(0x03);
            /**
             * 发送报文
             */
            InetAddress address = InetAddress.getByName(Constants.UDM_BROADCAST_IP);
            DatagramPacket sendDataPacket = new DatagramPacket(byteBuffer, byteBuffer.length, address, Constants.UDM_UDP_SERVER_PORT);
            datagramSocket.send(sendDataPacket);
            /**
             * 接收报文
             */
            byte[] recevBuffer = new byte[8];
            DatagramPacket recevPacket = new DatagramPacket(recevBuffer, recevBuffer.length);
            datagramSocket.receive(recevPacket);
            byte[] replyData = recevPacket.getData();
            short replyType = Convert.bytesToShort(Arrays.copyOfRange(replyData, 0, 2));
            //返回0表示成功
            if (replyType ==0) {
                return true;
            } else {
                return false;
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (datagramSocket != null) {
                datagramSocket.close();
            }
        }
        return false;
    }
}
