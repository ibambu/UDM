package com.ibamb.udm.module.net;

import android.util.Log;

import com.ibamb.udm.module.constants.UdmConstants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Created by luotao on 18-3-15.
 */
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
            address = InetAddress.getByName(UdmConstants.UDM_BROADCAST_IP);
            DatagramPacket sendDataPacket = new DatagramPacket(sendData, sendData.length, address, UdmConstants.UDM_UDP_SERVER_PORT);
            datagramSocket.setBroadcast(true);
            // 发送数据
            datagramSocket.send(sendDataPacket);
            // 接收数据
            Thread.sleep(200);//延迟200ms，然后再读取数据。
            datagramSocket.receive(recevPacket);
            recevData = recevPacket.getData();
        } catch (UnknownHostException ex) {
            Log.e(this.getClass().getName(), ex.getMessage());
        } catch (IOException ex) {
            Log.e(this.getClass().getName(), ex.getMessage());
        } catch (InterruptedException ex) {
            Log.e(this.getClass().getName(), ex.getMessage());
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
            datagramSocket.setSoTimeout(1000);//一秒后无返回则超时处理，避免任务无法终止导致内存泄露。
            address = InetAddress.getByName(UdmConstants.UDM_BROADCAST_IP);
            DatagramPacket sendDataPacket = new DatagramPacket(sendData, sendData.length, address, UdmConstants.UDM_UDP_SERVER_PORT);
            // 发送数据
            System.out.println("send:"+Arrays.toString(sendData));
            datagramSocket.send(sendDataPacket);
            // 接收数据
            Thread.sleep(200);//延迟200ms，然后再读取数据。
            datagramSocket.receive(recevPacket);
            recevData = recevPacket.getData();
            System.out.println("reply:"+Arrays.toString(recevData));
        } catch (UnknownHostException ex) {
            Log.e(this.getClass().getName(), ex.getMessage());
        } catch (IOException ex) {
            Log.e(this.getClass().getName(), ex.getMessage());
        } catch (InterruptedException e) {
            Log.e(this.getClass().getName(), e.getMessage());
        } finally {
            if (datagramSocket != null) {
                datagramSocket.close();
            }
        }
        return recevData;
    }
}
