package com.ibamb.udm.module.net;

import com.ibamb.udm.log.UdmLog;
import com.ibamb.udm.module.constants.Constants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class UDPMessageSender {

    /**
     * 发送数据包
     *
     * @param sendData
     * @param expectReplyLenth
     * @return
     */
    public byte[] sendByBroadcast(byte[] sendData, int expectReplyLenth) {

        InetAddress address;
        byte[] recevBuffer = new byte[1024];
        byte[] recevData = null;
        DatagramPacket recevPacket = new DatagramPacket(recevBuffer, recevBuffer.length);
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket();
            address = InetAddress.getByName(Constants.UDM_BROADCAST_IP);
            DatagramPacket sendDataPacket = new DatagramPacket(sendData, sendData.length, address, Constants.UDM_UDP_SERVER_PORT);
            datagramSocket.setBroadcast(true);
            // 发送数据
            datagramSocket.send(sendDataPacket);
            // 接收数据
            Thread.sleep(200);//延迟200ms，然后再读取数据。
            datagramSocket.receive(recevPacket);
            recevData = recevPacket.getData();
        } catch (UnknownHostException ex) {

        } catch (IOException ex) {

        } catch (InterruptedException ex) {

        }
        return recevData;
    }

    /**
     * 单播方式发送数据
     *
     * @param sendData
     * @param expectReplyLenth
     * @param ip
     * @return
     */
    public byte[] sendByUnicast(byte[] sendData, int expectReplyLenth,String ip) {
        InetAddress address;
        byte[] recevBuffer = new byte[1024];
        byte[] recevData = null;
        DatagramPacket recevPacket = new DatagramPacket(recevBuffer, recevBuffer.length);
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket();
            datagramSocket.setSoTimeout(1500);//一秒后无返回则超时处理，避免任务无法终止导致内存泄露。
            address = InetAddress.getByName(Constants.UDM_BROADCAST_IP);

            DatagramPacket sendDataPacket = new DatagramPacket(sendData, sendData.length, address, Constants.UDM_UDP_SERVER_PORT);
            // 发送数据
//            System.out.print("send:");
//            for(int i=0;i<sendData.length;i++){
//                System.out.print(Integer.toHexString(sendData[i])+" ");
//            }
//            System.out.println();
            datagramSocket.send(sendDataPacket);
            // 接收数据
//            Thread.sleep(200);//延迟200ms，然后再读取数据。
            datagramSocket.receive(recevPacket);
            recevData = recevPacket.getData();
//            System.out.print("replay:");
//            for(int i=0;i<recevData.length;i++){
//                System.out.print(Integer.toHexString(recevData[i])+" ");
//            }
//            System.out.println();
        } catch (UnknownHostException ex) {
            UdmLog.e(this.getClass().getName(), ex.getMessage());
        } catch (IOException ex) {
            UdmLog.e(this.getClass().getName(), ex.getMessage());
        }  finally {
            if (datagramSocket != null) {
                datagramSocket.close();
            }
        }
        return recevData;
    }
}
