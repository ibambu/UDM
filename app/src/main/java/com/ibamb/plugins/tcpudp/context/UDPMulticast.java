package com.ibamb.plugins.tcpudp.context;

import com.ibamb.dnet.module.net.IPUtil;
import com.ibamb.plugins.tcpudp.listener.MessageListener;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class UDPMulticast {

    private ConnectProperty connectProperty;
    private MulticastSocket multicastSocket;
    private MessageListener listener;
    private InetAddress multicastAddress;
    private InetAddress localAddress;

    public UDPMulticast(ConnectProperty connectProperty, MessageListener listener) {
        this.connectProperty = connectProperty;
        this.listener = listener;
    }

    public void create() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    multicastAddress = InetAddress.getByName(connectProperty.getUdpMulAddress()); //组地址
                    localAddress = IPUtil.getLocalAddress();
                    //注意组播端口必须大于1024.小于1024的端口都属于操作系统使用的端口。非root权限的用户不能使用。
                    multicastSocket = new MulticastSocket(connectProperty.getUdpMulPort());
                    multicastSocket.setLoopbackMode(false);
                    multicastSocket.joinGroup(multicastAddress); //加入到组播组中
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
                    while (!multicastSocket.isClosed()) {
                        /* * 服务器端接受客户端的数据 */
                        byte[] data = new byte[1024];
                        //指定用于接受数据报的大小
                        DatagramPacket packet = new DatagramPacket(data, data.length);
                        //接受到数据之前该方法处于阻塞状态
                        multicastSocket.receive(packet);
                        byte[] receiveData = packet.getData();
//                        String receiveData = new String(data, 0, packet.getLength(), Constant.DEFAULT_CHARSET);
                        InetAddress address = packet.getAddress();
                        if (!address.getHostAddress().equals(localAddress.getHostAddress())) {
                            listener.onReceive(address.getHostAddress() , receiveData);
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
                    if (!multicastSocket.isClosed()) {
                        /* * 服务器端接受客户端的数据 */
//                        byte[] data = message.getBytes(Constant.DEFAULT_CHARSET);
                        //指定用于接受数据报的大小
                        DatagramPacket packet = new DatagramPacket(message, message.length, multicastAddress, connectProperty.getUdpMulPort());
                        //接受到数据之前该方法处于阻塞状态
                        multicastSocket.send(packet);
                        InetAddress localAddress = IPUtil.getLocalAddress();
                        sendListener.onReceive(localAddress.getHostAddress() , message);
                    } else {
                        sendListener.onReceive(localAddress.getHostAddress() ,":Socket is closed".getBytes());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void close(){
        try{
            if(multicastSocket!=null){
                multicastSocket.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
