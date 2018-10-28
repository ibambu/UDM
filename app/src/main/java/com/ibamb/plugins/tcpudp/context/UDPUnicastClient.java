package com.ibamb.plugins.tcpudp.context;

import com.ibamb.dnet.module.log.UdmLog;
import com.ibamb.dnet.module.net.IPUtil;
import com.ibamb.plugins.tcpudp.listener.MessageListener;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPUnicastClient {

    private DatagramSocket socket;
    private ConnectProperty connectProperty;
    private MessageListener listener;

    public UDPUnicastClient(ConnectProperty connectProperty, MessageListener listener) {
        this.listener = listener;
        this.connectProperty = connectProperty;
    }

    public void create() {
        try {
            socket = new DatagramSocket(connectProperty.getUdpUniLocalPort());
            reading();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reading() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] recevBuffer = new byte[1024];
                byte[] recevData = null;
                DatagramPacket recevPacket = new DatagramPacket(recevBuffer, recevBuffer.length);
                try {
                    while (!socket.isClosed()) {
                        socket.receive(recevPacket);
                        recevData = recevPacket.getData();
//                        String message = new String(recevData, 0, recevData.length);
                        listener.onReceive(connectProperty.getUdpUniRemoteHost(),recevData);
                    }
                } catch (Exception ex) {
                    UdmLog.error(ex);
                }
            }
        }).start();
    }

    public void send(final byte[] message, final MessageListener sendListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String localAddress = IPUtil.getLocalAddress().getHostAddress();
                    if (!socket.isClosed()) {
                        InetAddress address = InetAddress.getByName(connectProperty.getUdpUniRemoteHost());
//                        byte[] sendData = message.getBytes();
                        DatagramPacket sendDataPacket = new DatagramPacket(message, message.length, address,
                                connectProperty.getUdpUniRemotePort());
                        // 发送数据
                        socket.send(sendDataPacket);
                        sendListener.onReceive(localAddress,message);
                    } else {
                        sendListener.onReceive(localAddress , ":Socket is closed".getBytes());
                    }

                } catch (Exception ex) {
                    UdmLog.error(ex);
                }
            }
        }).start();
    }

    public void close() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {

        }
    }
}
