package com.ibamb.udm.net;

import com.ibamb.udm.constants.UdmConstants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by luotao on 18-3-15.
 */
public class UDPMessageSender {

    /**
     * 发送数据包
     *
     * @param datagramSocket
     * @param sendData
     * @param replyBytesLength
     * @return
     */
    public byte[] send(DatagramSocket datagramSocket, byte[] sendData, int replyBytesLength) {

        InetAddress address;
        byte[] recevBuffer = new byte[replyBytesLength];
        DatagramPacket recevPacket = new DatagramPacket(recevBuffer, recevBuffer.length);
        try {
            address = InetAddress.getByName(UdmConstants.UDM_BROADCAST_IP);
            DatagramPacket sendDataPacket = new DatagramPacket(sendData, sendData.length, address, UdmConstants.UDM_UDP_SERVER_PORT);
            // 发送数据
            System.out.print("send:");
            datagramSocket.send(sendDataPacket);
            for (int i = 0; i < sendData.length; i++) {
                System.out.print(Integer.toHexString(sendData[i]) + " ");
            }
            System.out.println("");
            // 接收数据
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
            datagramSocket.receive(recevPacket);
            byte[] recevData = recevPacket.getData();
            System.out.print("reply:");
            for (int i = 0; i < recevData.length; i++) {
                System.out.print(Integer.toHexString(recevData[i]) + " ");
            }
            System.out.println("");
        } catch (UnknownHostException ex) {
        } catch (IOException ex) {
        }
        return recevPacket.getData();
    }
}
