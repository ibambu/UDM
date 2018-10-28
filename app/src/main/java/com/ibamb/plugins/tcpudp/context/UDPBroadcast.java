package com.ibamb.plugins.tcpudp.context;

import com.ibamb.dnet.module.constants.Constants;
import com.ibamb.dnet.module.net.IPUtil;
import com.ibamb.plugins.tcpudp.listener.MessageListener;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPBroadcast {
    private DatagramSocket socket;
    private ConnectProperty connectProperty;
    private MessageListener listener;
    private InetAddress address;


    public UDPBroadcast(ConnectProperty connectProperty, MessageListener listener) {
        this.connectProperty = connectProperty;
        this.listener = listener;
    }

    public void create() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new DatagramSocket(connectProperty.getUdpBroadcastPort());
                    socket.setBroadcast(true);
                    address = InetAddress.getByName(Constants.UDM_BROADCAST_IP);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                reading();
            }
        }).start();
    }

    public void reading() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!socket.isClosed()) {
                        /* * 服务器端接受客户端的数据 */
                        byte[] data = new byte[1024];
                        //指定用于接受数据报的大小
                        DatagramPacket packet = new DatagramPacket(data, data.length);
                        socket.receive(packet);
                        //接受到数据之前该方法处于阻塞状态
                        byte[] recieveData = packet.getData();
//                        String reveiceData = new String(data, 0, packet.getLength(), Constant.DEFAULT_CHARSET);
                        InetAddress address = packet.getAddress();
                        InetAddress localAddress = IPUtil.getLocalAddress();
                        if (!packet.getAddress().getHostAddress().equals(localAddress.getHostAddress())) {
                            listener.onReceive(address.getHostAddress() , recieveData);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void send(final byte[] message, final MessageListener sendListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress localAddress = IPUtil.getLocalAddress();
                    if (!socket.isClosed()) {
//                        byte[] data = message.getBytes();
                        //2.创建数据报，包含发送的数据信息
                        DatagramPacket packet = new DatagramPacket(message, message.length, address, connectProperty.getUdpBroadcastPort());
                        //3.创建DatagramSocket用于数据的传输
                        //4.向服务器端发送数据报
                        socket.send(packet);

                        sendListener.onReceive(localAddress.getHostAddress() , message);
                    } else {
                        sendListener.onReceive(localAddress.getHostAddress(), ":Socket is closed".getBytes());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
            e.printStackTrace();
        }
    }
}
