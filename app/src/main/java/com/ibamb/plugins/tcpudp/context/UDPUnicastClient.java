package com.ibamb.plugins.tcpudp.context;

import com.ibamb.dnet.module.log.UdmLog;
import com.ibamb.dnet.module.net.IPUtil;
import com.ibamb.plugins.tcpudp.listener.MessageListener;
import com.ibamb.plugins.tcpudp.listener.ResultListener;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class UDPUnicastClient {

    private DatagramSocket socket;
    private ConnectProperty connectProperty;
    private MessageListener listener;

    public UDPUnicastClient(ConnectProperty connectProperty, MessageListener listener) {
        this.listener = listener;
        this.connectProperty = connectProperty;
    }

    public void create(ResultListener resultListener) {
        String code ="0";
        try {
            socket = new DatagramSocket(connectProperty.getUdpUniLocalPort());
            code ="1";
            reading();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            resultListener.onResult(code);
        }
    }
    public boolean isReady() {
        return socket == null || socket.isClosed() ? false : true;
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
                        recevData =Arrays.copyOfRange(recevPacket.getData(), 0, recevPacket.getLength());
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
                        sendListener.onReceive(localAddress , Constant.SOCKET_IS_CLOSED.getBytes());
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
